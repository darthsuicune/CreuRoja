package net.creuroja.android.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.android.gms.maps.model.LatLng;

import net.creuroja.android.app.Location;
import net.creuroja.android.app.LoginResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 29.10.13.
 */
public abstract class ConnectionClient {
	public static final String DIRECTIONS_API_BASE_URL =
			"https://maps.googleapis.com/maps/api/directions/json?region=es&";
	public static final String ORIGIN_URL = "origin=";
	public static final String DESTINATION_URL = "destination=";
	public static final String SENSOR_URL = "sensor=";
	public static final String PROGRAM_VERSION_VAR = "version=";
	public static final String PROGRAM_VERSION_NUMBER = "1.0";
	public static final int STATUS_OK = 0;
	public static final int STATUS_NOT_OK = 1;
	public static final int STATUS_LIMIT_REACHED = 2;
	public static final String sStatus = "status";
	public static final String sRoutes = "routes";
	public static final String sOverviewPolyline = "overview_polyline";
	public static final String sPoints = "points";

	public static boolean isConnected(Context context) {
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
	}

	private static List<LatLng> parseDirectionsResponse(HttpResponse response) {
		List<LatLng> result = null;
		if (response.getStatusLine() != null) {
			switch (response.getStatusLine().getStatusCode()) {
				case 200:
					// Get everything as a string ready to parse
					BufferedReader reader;
					try {
						reader = new BufferedReader(
								new InputStreamReader(response.getEntity().getContent()));
						StringBuilder builder = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
						if (reader != null) {
							reader.close();
						}
						// Parse the resulting string. Save to disk if its correct
						result = parseDirections(builder.toString());
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
		return result;
	}

	public static List<LatLng> parseDirections(String data) {
		try {
			JSONObject response = new JSONObject(data);
			ArrayList<LatLng> points = new ArrayList<LatLng>();
			int status = parseStatus(response.getString(sStatus));
			if (status == STATUS_OK) {
				JSONArray routes = response.getJSONArray(sRoutes);
				for (int i = 0; i < routes.length(); i++) {
					JSONObject route = routes.getJSONObject(i);
					points.addAll(
							decodePoly(route.getJSONObject(sOverviewPolyline).getString(sPoints)));
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
	 * <p/>
	 * It converts the List<LatLng> encoded in the response from Google into an array of points.
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

	/**
	 * Placeholder in case we want to introduce sensor detection
	 */
	private static String getSensorAvailability() {
		return "true";
	}

	public List<LatLng> getDirections(double latitudeStart, double longitudeStart,
									  Location destination) throws IOException {
		return parseDirectionsResponse(executeRequest(createHttpClient(),
				getDirectionsRequest(latitudeStart, longitudeStart, destination)));
	}

	private HttpUriRequest getDirectionsRequest(double latitudeStart, double longitudeStart,
												Location destination) {
		return new HttpGet(URI.create(
				DIRECTIONS_API_BASE_URL + ORIGIN_URL + latitudeStart + "," + longitudeStart + "&" +
				DESTINATION_URL + destination.mLatitude + "," + destination.mLongitude + "&" +
				SENSOR_URL + getSensorAvailability()));
	}

	DefaultHttpClient createHttpClient() {
		return new DefaultHttpClient();
	}

	HttpResponse executeRequest(HttpClient httpClient, HttpUriRequest request)
			throws IOException {
		return httpClient.execute(request);
	}

	public abstract LoginResponse doLogin(String email, String password) throws IOException;

	public abstract List<Location> requestUpdates(String accessToken, String lastUpdate)
			throws IOException;

	public abstract Boolean validateLogin(String accessToken) throws IOException;
}
