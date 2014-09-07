package net.creuroja.android.vehicletracking.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lapuente on 04.09.14.
 */
public class Vehicle {
	public static final String INDICATIVE = "indicative";

	public final String indicative;
	public double latitude;
	public double longitude;

	public Vehicle(JSONObject json) throws JSONException {
		this.indicative = json.getString(INDICATIVE);
	}
	public Vehicle(String indicative) {
		this.indicative = indicative;
	}

	public void setPosition(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void upload() {
		Log.d("ASDF", "I'm ALIVE!!!" + indicative + " " + this.latitude + " " + this.longitude);
	}

	public String toString() {
		return indicative;
	}
}
