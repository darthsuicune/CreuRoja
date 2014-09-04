package net.creuroja.android.vehicletracking.model;

import android.util.Log;

/**
 * Created by lapuente on 04.09.14.
 */
public class VehiclePosition {

	private final String mIndicative;
	private final double mLatitude;
	private final double mLongitude;

	public VehiclePosition(String indicative, double latitude, double longitude) {
		mIndicative = indicative;
		mLatitude = latitude;
		mLongitude = longitude;
	}

	public void upload() {
		Log.d(mIndicative, "I'm ALIVE!!! located in " + mLatitude + ", " + mLongitude);
	}
}
