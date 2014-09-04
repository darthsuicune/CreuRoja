package net.creuroja.android.vehicletracking.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 04.09.14.
 */
public class Vehicles {
	public static final String KEY_VEHICLES = "vehicles";
	private List<Vehicle> mVehicleList;

	public Vehicles(String json) throws JSONException {
		mVehicleList = new ArrayList<Vehicle>();
		JSONObject object = new JSONObject(json);
		if(object.has(KEY_VEHICLES)) {
			JSONArray array = object.getJSONArray(KEY_VEHICLES);
			for(int i = 0; i < array.length(); i++) {
				mVehicleList.add(new Vehicle(array.getJSONObject(i)));
			}
		}
	}

	public List<Vehicle> getAsList() {
		return mVehicleList;
	}

	public static List<Vehicle> getFromServer() throws JSONException{
		String jsonResponse = "{vehicles:[{\"indicative\": \"asdf\", \"license\":\"asdf\"}]}";
		Vehicles vehicles = new Vehicles(jsonResponse);
		return vehicles.getAsList();
	}
}
