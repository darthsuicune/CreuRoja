package net.creuroja.android.vehicletracking;

import android.app.IntentService;
import android.content.Intent;

import net.creuroja.android.vehicletracking.model.VehiclePosition;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class HeartBeatService extends IntentService {
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	public static final String SERVICE_NAME = "HeartBeatService";
	public static final String ACTION_BEAT = "net.creuroja.android.vehicletracking.action.BEAT";

	public static final String INDICATIVE = "net.creuroja.android.vehicletracking.extra.INDICATIVE";
	public static final String LATITUDE = "net.creuroja.android.vehicletracking.extra.LATITUDE";
	public static final String LONGITUDE = "net.creuroja.android.vehicletracking.extra.LONGITUDE";

	public HeartBeatService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_BEAT.equals(action)) {
				final String indicative = intent.getStringExtra(INDICATIVE);
				final double latitude = intent.getDoubleExtra(LATITUDE, 0);
				final double longitude = intent.getDoubleExtra(LONGITUDE, 0);
				handleActionBeat(indicative, latitude, longitude);
			}
		}
	}

	/**
	 * Handle action Beat in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionBeat(String indicative, double latitude, double longitude) {
		VehiclePosition vehiclePosition = new VehiclePosition(indicative, latitude, longitude);
		vehiclePosition.upload();
	}
}