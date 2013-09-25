package com.cruzroja.creuroja.utils;

import com.cruzroja.creuroja.Location;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

	public static final int STATUS_OK = 0;
	public static final int STATUS_NOT_OK = 1;
	public static final int STATUS_LIMIT_REACHED = 2;

	public static final String sPos = "position";
	public static final String sLat = "lat";
	public static final String sLong = "lng";
	public static final String sIcon = "icon";
	public static final String sContent = "contenido";

	public static final String sStatus = "status";
	public static final String sRoutes = "routes";
	public static final String sOverviewPolyline = "overview_polyline";
	public static final String sPoints = "points";

	public static ArrayList<Location> parseLocations(String data) {
		try {
			JSONArray array = new JSONArray(data);

			ArrayList<Location> locationsList = new ArrayList<Location>();
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

	public static ArrayList<LatLng> parseDirections(String data) {
		try {
			JSONObject response = new JSONObject(data);
			ArrayList<LatLng> points = new ArrayList<LatLng>();
			int status = parseStatus(response.getString(sStatus));
			if (status == STATUS_OK) {
				JSONArray routes = response.getJSONArray(sRoutes);
				for (int i = 0; i < routes.length(); i++) {
					JSONObject route = routes.getJSONObject(i);
					points.addAll(decodePoly(route.getJSONObject(
							sOverviewPolyline).getString(sPoints)));
				}
				return points;
			} else if (status == STATUS_LIMIT_REACHED) {
				// Handle error
				return points;
			} else {
				return null;
			}
		} catch (JSONException e) {
			return null;
		}

	}

    /**
     * This method was completely copied from some point in stackoverflow.com
     * If you wanna know what it does, good luck finding it again.
     *
     * It converts the polyline encoded in the response from Google into an array of points.
     *
     * @param encoded
     * @return
     */
	private static ArrayList<LatLng> decodePoly(String encoded) {
		ArrayList<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			poly.add(position);
		}
		return poly;
	}

	private static int parseStatus(String status) {
		if (status.equals("OK") || status.equals("ZERO_RESULTS")) {
			return STATUS_OK;
		} else if (status.equals("OVER_QUERY_LIMIT")) {
			return STATUS_LIMIT_REACHED;
		} else {
			return STATUS_NOT_OK;
		}
	}
}
