package com.cruzroja.creuroja;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

public class DirectionsLoader extends AsyncTaskLoader<String> {
	public static final String DIRECTIONS_API_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?region=es&";
	public static final String ORIGIN_URL = "origin=";
	public static final String DESTINATION_URL = "destination=";
	public static final String SENSOR_URL = "sensor=";

	public static final String ARG_ORIGIN_LAT = "originLat";
	public static final String ARG_ORIGIN_LNG = "originLng";
	public static final String ARG_DESTINATION_LAT = "destinationLat";
	public static final String ARG_DESTINATION_LNG = "destinationLng";

	Bundle mArgs;

	public DirectionsLoader(Context context, Bundle args) {
		super(context);
		mArgs = args;
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
		super.onStartLoading();
	}

	@Override
	public String loadInBackground() {
		DefaultHttpClient httpClient = createHttpClient();
		HttpGet request = new HttpGet(getUri());
		InputStream response = null;
		String data = "";
		try {
			response = httpClient.execute(request).getEntity().getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response));
			String line = "";

			while ((line = reader.readLine()) != null) {
				data = data + line;
			}
			reader.close();
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private URI getUri() {

		return URI.create(DIRECTIONS_API_BASE_URL + ORIGIN_URL
				+ mArgs.getDouble(ARG_ORIGIN_LAT) + ","
				+ mArgs.getDouble(ARG_ORIGIN_LNG) + "&" + DESTINATION_URL
				+ mArgs.getDouble(ARG_DESTINATION_LAT) + ","
				+ mArgs.getDouble(ARG_DESTINATION_LNG) + "&" + SENSOR_URL
				+ getSensorAvailability());
	}

	private DefaultHttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		return new DefaultHttpClient(params);
	}

	private String getSensorAvailability() {
		return "true";
	}
}
