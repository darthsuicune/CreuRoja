package net.creuroja.android.vehicletracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import net.creuroja.android.vehicletracking.model.Vehicle;

public class PositionUpdaterService extends Service {
	public static final String SERVICE_NAME = "PositionUpdaterService";
	public static final String ACTION_NOTIFY = "net.creuroja.android.vehicletracking.action.NOTIFY";
	public static final String ACTION_STOP = "net.creuroja.android.vehicletracking.action.STOP";

	public static final String EXTRA_INDICATIVE =
			"net.creuroja.android.vehicletracking.extra.EXTRA_INDICATIVE";

	private Vehicle mVehicle;
	private boolean isRunning = false;
	private SharedPreferences prefs;
	private PendingIntent pendingIntent = null;

	public PositionUpdaterService() {
	}

	@Override public void onDestroy() {
		super.onDestroy();
		isRunning = false;
		setAlarm();
	}

	@Override public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.hasExtra(EXTRA_INDICATIVE)) {
			mVehicle = new Vehicle(intent.getStringExtra(EXTRA_INDICATIVE));
		}
		isRunning = true;
		setAlarm();
		return super.onStartCommand(intent, flags, startId);
	}

	private void setAlarm() {
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		if (pendingIntent == null) {
			Intent intent = new Intent();
		}
		if (!isRunning) {
			manager.cancel(pendingIntent);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
