package net.creuroja.android.vehicletracking.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lapuente on 04.09.14.
 */
public class Vehicle {
	public static final String INDICATIVE = "indicative";
	public static final String LICENSE = "license";

	public final String mIndicative;
	public final String mLicense;

	public Vehicle(JSONObject json) throws JSONException {
		mIndicative = json.getString(INDICATIVE);
		mLicense = json.getString(LICENSE);
	}

	public String toString() {
		return mIndicative + " - " + mLicense;
	}
}
