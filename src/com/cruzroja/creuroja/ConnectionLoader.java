package com.cruzroja.creuroja;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

public class ConnectionLoader extends AsyncTaskLoader<ArrayList<Location>> {
	public static final String PUNTOS_FIJOS = "http://r0uzic.net/voluntarios.cr/permanentes.json";
	public static final String PUNTOS_VARIABLES = "http://r0uzic.net/voluntarios.cr/temporales.json";

	public ConnectionLoader(Context context, Bundle args) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
		super.onStartLoading();
	}

	@Override
	public ArrayList<Location> loadInBackground() {
		DefaultHttpClient httpClient = createHttpClient();
		HttpGet requestFijos = new HttpGet(PUNTOS_FIJOS);
		HttpGet requestVariables = new HttpGet(PUNTOS_VARIABLES);

		ArrayList<Location> locationList = null;
		try {
			locationList = getLocations(requestFijos, httpClient, true);
			locationList.addAll(getLocations(requestVariables, httpClient,
					false));
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return locationList;
	}

	private DefaultHttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		return new DefaultHttpClient(params);
	}

	ArrayList<Location> getLocations(HttpGet request, DefaultHttpClient client,
			boolean isFijo) throws ClientProtocolException, IOException {
		HttpResponse response = client.execute(request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));

		String data = "";
		String line = "";

		while ((line = reader.readLine()) != null) {
			data = data + line;
		}
		data = data.replace("\t", "");
		JSONParser.saveToDisk(getContext(), data, isFijo);
		return JSONParser.parseJson(data);
	}
}
