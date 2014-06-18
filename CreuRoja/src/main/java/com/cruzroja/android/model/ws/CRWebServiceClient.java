package com.cruzroja.android.model.ws;

import com.cruzroja.android.model.Location;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by lapuente on 18.06.14.
 */
public class CRWebServiceClient implements WebServiceClient {
	private static final String WS_PROTOCOL = "http";
	private static final String WS_URL = "kls.servequake.com";
	private static final String WS_PORT = "808";
	private static final String WS_RESOURCE_LOGIN = "";
	private static final String WS_RESOURCE_LOCATIONS = "";

	@Override
	public String signInUser(String username, String password) {
		HttpClient client = getHttpClient();
		HttpUriRequest request = getHttpRequest();
		try {
			HttpResponse response = client.execute(request);
			StringBuilder builder = new StringBuilder();
			BufferedReader reader =
					new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Location> getLocations() {
		return null;
	}

	@Override
	public List<Location> getLocations(String lastUpdateTime) {
		return null;
	}

	private HttpClient getHttpClient() {
		BasicHttpParams params = new BasicHttpParams();
		DefaultHttpClient client = new DefaultHttpClient(params);
		return client;
	}

	private HttpUriRequest getHttpRequest() {
		return null;
	}
}
