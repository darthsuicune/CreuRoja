package net.creuroja.android.vehicletracking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import net.creuroja.android.vehicletracking.model.Settings;
import net.creuroja.android.vehicletracking.model.Vehicle;

import java.io.IOException;

public class PositionUpdaterService extends Service implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	public static final String SERVICE_NAME = "PositionUpdaterService";

	public static final String EXTRA_INDICATIVE =
			"net.creuroja.android.vehicletracking.extra.EXTRA_INDICATIVE";
	public static final String EXTRA_VEHICLE_ID =
			"net.creuroja.android.vehicletracking.extra.EXTRA_VEHICLE_ID";

	public static final String KEY_PERMANENT_NOTIFICATION = "permanentNotification";
	public static final String KEY_FINISHED_NOTIFICATION = "finishedNotification";
	public static final int NOTIFICATION_PERMANENT = 1;
	public static final int NOTIFICATION_FINISHED = 2;

	private Vehicle mVehicle;
	private LocationClient mLocationClient;
	private boolean isConnected = false;
	private SharedPreferences prefs;
	private Location mLocation;

	public PositionUpdaterService() {
	}

	@Override public void onDestroy() {
		super.onDestroy();
		mLocationClient.disconnect();
		setNotification(false);
	}

	@Override public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();
	}

	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		if (isConnected) {
			if (mVehicle == null && intent != null && intent.getExtras() != null &&
				intent.hasExtra(EXTRA_INDICATIVE) && intent.hasExtra(EXTRA_VEHICLE_ID)) {
				mVehicle = new Vehicle(intent.getIntExtra(EXTRA_VEHICLE_ID, 0),
						intent.getStringExtra(EXTRA_INDICATIVE));
				setNotification(true);
			}
			updatePosition();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void updatePosition() {
		if(mLocation == null) {
			Location location = mLocationClient.getLastLocation();
			if (location != null) {
				mVehicle.setPosition(location.getLatitude(), location.getLongitude());
				upload(location);
			}
		} else {
			upload(mLocation);
		}
	}

	private void upload(Location location) {
		if (mVehicle != null) {
			mVehicle.setPosition(location.getLatitude(), location.getLongitude());
			VehicleUploadTask task =
					new VehicleUploadTask(prefs.getString(Settings.ACCESS_TOKEN, ""));
			task.execute();
		}
	}

	private void setNotification(boolean permanent) {
		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification;
		int id;
		if(permanent) {
			notification = getPermanentNotification();
			id = NOTIFICATION_PERMANENT;
		} else {
			notification = getFinishedNotification();
			id = NOTIFICATION_FINISHED;
		}
		notificationManager.notify(id, notification);
	}

	private Notification getPermanentNotification() {
		Notification.Builder builder = new Notification.Builder(this);
		builder.setOngoing(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return builder.build();
		} else {
			return builder.getNotification();
		}
	}

	private Notification getFinishedNotification() {
		Notification.Builder builder = new Notification.Builder(this);
		builder.setOngoing(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return builder.build();
		} else {
			return builder.getNotification();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override public void onConnected(Bundle bundle) {
		isConnected = true;
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(5000);
		request.setFastestInterval(5000);
		mLocationClient.requestLocationUpdates(request, this);
	}

	@Override public void onDisconnected() {
		isConnected = false;
	}

	@Override public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	@Override public void onLocationChanged(Location location) {
		mLocation = location;
	}

	private class VehicleUploadTask extends AsyncTask<Void, Void, Void> {
		String mToken;

		public VehicleUploadTask(String token) {
			mToken = token;
		}

		@Override protected Void doInBackground(Void... voids) {
			try {
				mVehicle.upload(mToken);
			} catch (IOException e) {
				Log.d(SERVICE_NAME, "Error connecting to server");
				e.printStackTrace();
			}

			return null;
		}
	}
}
