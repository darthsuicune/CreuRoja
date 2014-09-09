package net.creuroja.android.vehicletracking.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import net.creuroja.android.vehicletracking.PositionUpdaterService;
import net.creuroja.android.vehicletracking.R;
import net.creuroja.android.vehicletracking.fragments.TrackingFragment;
import net.creuroja.android.vehicletracking.model.Settings;
import net.creuroja.android.vehicletracking.model.Vehicle;


public class TrackingActivity extends ActionBarActivity
		implements TrackingFragment.OnTrackingFragmentInteractionListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int REQUEST_LOGIN_ACTIVITY = 1;

	private TrackingFragment mTrackingFragment;
	private LocationClient mLocationClient;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getString(Settings.ACCESS_TOKEN, null) == null) {
			startLoginActivity();
		} else {
			startTrackingActivity();
		}
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, REQUEST_LOGIN_ACTIVITY);
	}

	private void startTrackingActivity() {
		setContentView(R.layout.activity_tracking);
		setFragment();
		parseNotifications();
		new ConnectionAvailabilityTask().execute();
		setUpLocationClient();
	}

	private void setFragment() {
		mTrackingFragment = (TrackingFragment) getSupportFragmentManager()
				.findFragmentById(R.id.tracking_fragment);
	}

	private void setUpLocationClient() {
		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();
		servicesConnected();
	}

	private void parseNotifications() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			OnNotificationReceivedListener listener = mTrackingFragment;
			if (extras.containsKey(PositionUpdaterService.KEY_PERMANENT_NOTIFICATION)) {
				listener.onNotificationReceived(PositionUpdaterService.NOTIFICATION_PERMANENT);
			} else if (extras.containsKey(PositionUpdaterService.KEY_FINISHED_NOTIFICATION)) {
				listener.onNotificationReceived(PositionUpdaterService.NOTIFICATION_FINISHED);
			}
		}
	}

	/*
	 * Handle results returned to the FragmentActivity
	 * by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
			case CONNECTION_FAILURE_RESOLUTION_REQUEST:
				// If the result code is Activity.RESULT_OK, try to connect again
				switch (resultCode) {
					case Activity.RESULT_OK:
						// Try the request again
						servicesConnected();
						break;
				}
			case REQUEST_LOGIN_ACTIVITY:
				switch (resultCode) {
					case RESULT_OK:
						startTrackingActivity();
						break;
					default:
						finish();
				}
		}
	}

	@Override public void onTrackingStartRequested(Vehicle vehicle) {
	}

	@Override public void onTrackingStopRequested() {}

		// Define a DialogFragment that displays the error dialog
		public static class ErrorDialogFragment extends DialogFragment {
			// Global field to contain the error dialog
			private Dialog mDialog;

			// Default constructor. Sets the dialog field to null
			public ErrorDialogFragment() {
				super();
				mDialog = null;
			}

			// Set the dialog to display
			public void setDialog(Dialog dialog) {
				mDialog = dialog;
			}

			// Return a Dialog to the DialogFragment.
			@Override @NonNull
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return mDialog;
			}
		}

		// Check that Google Play services is available

	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS != resultCode) {
			// Try to get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil
					.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			if (errorDialog != null) {
				showErrorFragment(errorDialog, "Location Updates");
			}
		}
		return ConnectionResult.SUCCESS == resultCode;
	}

	private void showErrorFragment(Dialog dialog, String tag) {
		// Create a new DialogFragment for the error dialog, set and show it
		ErrorDialogFragment errorFragment = new ErrorDialogFragment();
		errorFragment.setDialog(dialog);
		errorFragment.show(getSupportFragmentManager(), tag);
	}

	@Override public void onConnected(Bundle bundle) {
		new LocationAvailabilityTask().execute();
	}

	@Override public void onDisconnected() {
	}

	@Override public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult
						.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			showErrorFragment(builder.create(), "CONNECTION FAILED");
		}
	}

	private class LocationAvailabilityTask extends AsyncTask<Void, Void, Void> {

		@Override protected Void doInBackground(Void... voids) {
			runOnUiThread(new Runnable() {
				@Override public void run() {
					mTrackingFragment.showProgress(true);
				}
			});

			while (mLocationClient.getLastLocation() == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			mTrackingFragment.hasLocation = true;
			if (mTrackingFragment.isConnected && !mTrackingFragment.inProgress) {
				runOnUiThread(new Runnable() {
					@Override public void run() {
						mTrackingFragment.showProgress(false);
					}
				});
			}
			return null;
		}
	}

	private class ConnectionAvailabilityTask extends AsyncTask<Void, Void, Void> {

		@Override protected Void doInBackground(Void... voids) {
			runOnUiThread(new Runnable() {
				@Override public void run() {
					mTrackingFragment.showProgress(true);
				}
			});

			while (!Settings.isConnected(TrackingActivity.this)) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			mTrackingFragment.isConnected = true;
			if (mTrackingFragment.hasLocation && !mTrackingFragment.inProgress) {
				runOnUiThread(new Runnable() {
					@Override public void run() {
						mTrackingFragment.showProgress(false);
					}
				});
			}
			return null;
		}
	}
}
