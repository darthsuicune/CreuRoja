package net.creuroja.android.vehicletracking.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import net.creuroja.android.vehicletracking.R;
import net.creuroja.android.vehicletracking.fragments.TrackingFragment;
import net.creuroja.android.vehicletracking.model.Settings;


public class TrackingActivity extends ActionBarActivity
		implements TrackingFragment.OnTrackingFragmentInteractionListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	public static final String KEY_PERMANENT_NOTIFICATION = "permanentNotification";
	private static final String KEY_FINISHED_NOTIFICATION = "finishedNotification";
	public static final int NOTIFICATION_PERMANENT = 1;
	public static final int NOTIFICATION_FINISHED = 2;

	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int REQUEST_LOGIN_ACTIVITY = 1;

	private TrackingFragment mTrackingFragment;
	private LocationClient mLocationClient;
	private boolean hasLocation = false;
	private boolean isConnected = false;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getString(Settings.ACCESS_TOKEN, null) == null) {
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
		setFragment();
		parseNotifications();
		new ConnectionAvailabilityTask().execute();
		setUpLocationClient();
	}

	private void setFragment() {
		setContentView(R.layout.activity_tracking);

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
			if (extras.containsKey(KEY_PERMANENT_NOTIFICATION)) {
				listener.onNotificationReceived(NOTIFICATION_PERMANENT);
			} else if (extras.containsKey(KEY_FINISHED_NOTIFICATION)) {
				listener.onNotificationReceived(NOTIFICATION_FINISHED);
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
				startTrackingActivity();
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tracking, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_settings:
				openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override public void onTrackingStartRequested() {
		setNotification(true);
	}

	@Override public void onTrackingStopRequested() {
		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_PERMANENT);
		setNotification(false);
	}

	private void setNotification(boolean permanent) {
		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification =
				(permanent) ? getPermanentNotification() : getFinishedNotification();
		//notificationManager.notify(NOTIFICATION_PERMANENT, notification);
		//TODO: Uncomment when implemented notifications
	}

	private Notification getPermanentNotification() {
		return null;
	}

	private Notification getFinishedNotification() {
		return null;
	}

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
		if (mLocationClient.getLastLocation() == null) {
			new LocationAvailabilityTask().execute();
		}
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
			hasLocation = true;
			if (isConnected) {
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

			while (Settings.isConnected(TrackingActivity.this)) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			isConnected = true;
			if (hasLocation) {
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
