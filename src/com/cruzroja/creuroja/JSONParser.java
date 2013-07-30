package com.cruzroja.creuroja;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Structure for the JSON: Array with repeatable objects with the following
 * content:
 * 
 * [{position:{lat:<double> latitud, lng:<double> longitud},icon:'<String>
 * name', <contenido>:"String html"}, {same as before, different values}, ...,
 * {}]
 */
public class JSONParser {
	private static final String FILE_NAME_FIJOS = "fijos";
	private static final String FILE_NAME_VARIABLES = "variables";

	public static final String sPos = "position";
	public static final String sLat = "lat";
	public static final String sLong = "lng";
	public static final String sIcon = "icon";
	public static final String sContent = "contenido";

	public static final String sStatus = "status";
	public static final String sRoutes = "routes";
	public static final String sLegs = "legs";
	public static final String sSteps = "steps";
	public static final String sStartLocation = "start_location";
	public static final String sEndLocation = "end_location";
	public static final String sOverviewPolyline = "overview_polyline";
	public static final String sPoints = "points";

	public static final int STATUS_OK = 0;
	public static final int STATUS_NOT_OK = 1;
	public static final int STATUS_LIMIT_REACHED = 2;

    /**
     *
     * @param data
     * @return
     */
	public static ArrayList<Location> parseLocations(String data) {
        ArrayList<Location> locationsList = new ArrayList<Location>();

        try {
			JSONArray array = new JSONArray(data);

			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonLocation = array.getJSONObject(i);
				JSONObject jsonPosition = jsonLocation.getJSONObject(sPos);
				Location location = new Location(jsonPosition.getDouble(sLat),
						jsonPosition.getDouble(sLong),
						jsonLocation.getString(sIcon),
						jsonLocation.getString(sContent));
				locationsList.add(location);
			}
		} catch (JSONException e) {
			e.printStackTrace();
            return null;
		}
        return locationsList;
	}

	public static void saveToDisk(Context context, String data, boolean isFijo) {
		PrintStream ps = null;
		try {
			if (isFijo) {
				ps = new PrintStream(context.openFileOutput(FILE_NAME_FIJOS,
						Context.MODE_PRIVATE));
			} else {
				ps = new PrintStream(context.openFileOutput(
						FILE_NAME_VARIABLES, Context.MODE_PRIVATE));
			}
			ps.print(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			ps.close();
		}
	}

	public static ArrayList<Location> getFromDisk(Context context) {
		ArrayList<Location> locationList;
        try {
            String data = getDataFromFile(context, FILE_NAME_FIJOS);
            if (data.equals("")) {
                return null;
            }
            locationList = parseLocations(data);

            data = getDataFromFile(context, FILE_NAME_VARIABLES);
            if (data.equals("")) {
                return null;
            }
            locationList.addAll(parseLocations(data));
        } catch (NullPointerException e){
            return null;
        }

		return locationList;
	}

    public static boolean removeFromDisk() {
        if (new File(FILE_NAME_FIJOS).delete() && new File(FILE_NAME_VARIABLES).delete()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDataFromFile(Context context, String fileName) {
		String data = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					context.openFileInput(fileName)));
			String line = "";

			while ((line = reader.readLine()) != null) {
				data = data + line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return data;
	}

    public static ArrayList<LatLng> parseDirections(String fullResponse) {
        ArrayList<LatLng> points = new ArrayList<LatLng>();
		try {
			
			JSONObject response = new JSONObject(fullResponse);
			int status = parseStatus(response.getString(sStatus));
			if (status == STATUS_OK) {
				JSONArray routes = response.getJSONArray(sRoutes);
				for (int i = 0; i < routes.length(); i++) {
					JSONObject route = routes.getJSONObject(i);
					points.addAll(decodePoly(route.getJSONObject(
							sOverviewPolyline).getString(sPoints)));
				}
			} else if (status == STATUS_LIMIT_REACHED) {
				// Handle error
				return points;
			} else {
				return null;
			}
		} catch (JSONException e) {
			return null;
		}
		
		return points;
	}

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

	public static int parseStatus(String status) {
		if (status.equals("OK") || status.equals("ZERO_RESULTS")) {
			return STATUS_OK;
		} else if (status.equals("OVER_QUERY_LIMIT")) {
			return STATUS_LIMIT_REACHED;
		} else {
			return STATUS_NOT_OK;
		}
	}
}