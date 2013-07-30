package com.cruzroja.creuroja;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationsLoader extends AsyncTaskLoader<ArrayList<Location>> {
    public static final String PUNTOS_FIJOS = "http://r0uzic.net/voluntarios/permanentes.json";
	public static final String PUNTOS_VARIABLES = "http://r0uzic.net/voluntarios/temporales.json";

    private DefaultHttpClient httpClient = null;

    public LocationsLoader(Context context) {
        super(context);
	}

	@Override
	protected void onStartLoading() {
        if (httpClient == null) {
            forceLoad();
        }
        super.onStartLoading();
	}

	@Override
	public ArrayList<Location> loadInBackground() {
        createHttpClient();
        HttpGet requestFijos = new HttpGet(PUNTOS_FIJOS);
		HttpGet requestVariables = new HttpGet(PUNTOS_VARIABLES);

		ArrayList<Location> locationList = new ArrayList<Location>();
		try {
            ArrayList<Location> aux;
            aux = getLocations(requestFijos, httpClient);
            if (aux != null) {
                locationList.addAll(aux);
            }
            aux = getLocations(requestVariables, httpClient);
            if (aux != null) {
                locationList.addAll(aux);
            }
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return locationList;
	}

    private void createHttpClient() {
        HttpParams params = new BasicHttpParams();
        httpClient = new DefaultHttpClient(params);
    }

	private ArrayList<Location> getLocations(HttpGet request, DefaultHttpClient client)
            throws IOException {
		HttpResponse response = client.execute(request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));

		String data = "";
		String line;

		while ((line = reader.readLine()) != null) {
			data = data + line;
		}
		data = data.replace("\t", "");
		return JSONParser.parseLocations(data);
	}
}
