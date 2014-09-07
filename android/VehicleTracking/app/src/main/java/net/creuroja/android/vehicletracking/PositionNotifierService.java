package net.creuroja.android.vehicletracking;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import net.creuroja.android.vehicletracking.model.Vehicle;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class PositionNotifierService extends IntentService
		implements GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	public static final String SERVICE_NAME = "HeartBeatService";
	public static final String ACTION_NOTIFY = "net.creuroja.android.vehicletracking.action.NOTIFY";
	public static final String ACTION_STOP = "net.creuroja.android.vehicletracking.action.STOP";

	public static final String EXTRA_INDICATIVE =
			"net.creuroja.android.vehicletracking.extra.EXTRA_INDICATIVE";

	Vehicle mVehicle;
	LocationClient mLocationClient;

	public PositionNotifierService() {
		super(SERVICE_NAME);
	}

	@Override protected void onHandleIntent(Intent intent) {
		Log.d("ASDF", "Something");
		if (intent != null) {
			if (mLocationClient == null) {
				mLocationClient = new LocationClient(getApplicationContext(), this, this);
			}
			final String action = intent.getAction();
			if (ACTION_NOTIFY.equals(action)) {
				if (mVehicle == null) {
					mVehicle = new Vehicle(intent.getStringExtra(EXTRA_INDICATIVE));
				}

				if(!mLocationClient.isConnected()) {
					mLocationClient.connect();
				} else {
					notifyPositionToServer();
				}
			} else if (ACTION_STOP.equals(action)) {
				mLocationClient.disconnect();
			}
		}
	}

	/**
	 * Handle action Beat in the provided background thread with the provided
	 * parameters.
	 */
	private void notifyPositionToServer() {
		Location location = mLocationClient.getLastLocation();
		if (location != null) {
			Log.d("ASDF", "SomethingSomething");
			mVehicle.setPosition(location.getLatitude(), location.getLongitude());
			mVehicle.upload();
		}
	}

	@Override public void onConnected(Bundle bundle) {
		notifyPositionToServer();
	}

	@Override public void onDisconnected() {
	}

	@Override public void onConnectionFailed(ConnectionResult connectionResult) {
	}
}