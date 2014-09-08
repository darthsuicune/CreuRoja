package net.creuroja.android.vehicletracking;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import net.creuroja.android.vehicletracking.model.Settings;
import net.creuroja.android.vehicletracking.model.Vehicle;

import java.io.IOException;

public class PositionUpdaterService extends Service
		implements GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	public static final String SERVICE_NAME = "PositionUpdaterService";

	public static final String EXTRA_INDICATIVE =
			"net.creuroja.android.vehicletracking.extra.EXTRA_INDICATIVE";

	private Vehicle mVehicle;
	private LocationClient mLocationClient;
	private boolean isConnected = false;
	private SharedPreferences prefs;

	public PositionUpdaterService() {
	}

	@Override public void onDestroy() {
		super.onDestroy();
		mLocationClient.disconnect();
	}

	@Override public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();
	}

	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		if (isConnected) {
			if (intent != null && intent.getExtras() != null && intent.hasExtra(EXTRA_INDICATIVE)) {
				mVehicle = new Vehicle(intent.getStringExtra(EXTRA_INDICATIVE));
			}
			updatePosition();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void updatePosition() {
		Location location = mLocationClient.getLastLocation();
		if (mVehicle != null && location != null) {
			mVehicle.setPosition(location.getLatitude(), location.getLongitude());
			try {
				mVehicle.upload(prefs.getString(Settings.ACCESS_TOKEN, ""));
			} catch (IOException e) {
				Log.d(SERVICE_NAME, "Error connecting to server");
				e.printStackTrace();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override public void onConnected(Bundle bundle) {
		isConnected = true;
	}

	@Override public void onDisconnected() {
		isConnected = false;
	}

	@Override public void onConnectionFailed(ConnectionResult connectionResult) {

	}
}
