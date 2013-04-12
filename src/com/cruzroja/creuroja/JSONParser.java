package com.cruzroja.creuroja;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
	public static final String sPos = "position";
	public static final String sLat = "lat";
	public static final String sLong = "lng";
	public static final String sIcon = "icon";
	public static final String sContent = "contenido";

	public static ArrayList<Location> parseJson(String data) {
		data = data.replace("\t", "");

		try {
			JSONArray array = new JSONArray(data);

			ArrayList<Location> locationsList = new ArrayList<Location>();

			/**
			 * Structure for the JSON: Array with repeatable objects with the
			 * following content:
			 * 
			 * [{position:{lat:<double> latitud, lng:<double>
			 * longitud},icon:'<String> name', <contenido>:"String html"}, 
			 * {same as before, different values},
			 * ...,
			 * {}]
			 */

			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonLocation = array.getJSONObject(i);
				JSONObject jsonPosition = jsonLocation.getJSONObject(sPos);
				Location location = new Location(jsonPosition.getDouble(sLat),
						jsonPosition.getDouble(sLong),
						jsonLocation.getString(sIcon),
						jsonLocation.getString(sContent));
				locationsList.add(location);
			}
			return locationsList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}