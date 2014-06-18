package com.cruzroja.android.model.ws;

import android.util.Log;

import com.cruzroja.android.model.Location;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by lapuente on 18.06.14.
 */
public class CRWebServiceClient implements WebServiceClient {
	public static final String ARG_EMAIL = "email";
	public static final String ARG_PASS = "password";
	public static final String ARG_ACCESS_TOKEN = "access_token";
	public static final String ARG_LAST_UPDATE = "last_update";

	private static final String WS_PROTOCOL = "http";
	private static final String WS_URL = "kls.servequake.com";
	private static final String WS_PORT = "808";
	private static final String WS_CONNECTION_POINT = "webservice.php";
	private static final String WS_RESOURCE_LOGIN = "?q=request_access";
	private static final String WS_RESOURCE_LOCATIONS = "?q=get_locations";

	@Override
	public String signInUser(String username, String password) {
		HttpClient client = getHttpClient();
		try {
			HttpUriRequest request = getLoginRequest();
			HttpResponse response = client.execute(request);
			String result = readResponse(response);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.d("WSClient", "Incorrect URL");
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

	private HttpUriRequest getLoginRequest() throws URISyntaxException {
		HttpPost post = new HttpPost();
		StringBuilder builder = new StringBuilder();
		builder.append(WS_PROTOCOL);
		builder.append(WS_URL);
		builder.append(":");
		builder.append(WS_PORT);
		builder.append(WS_CONNECTION_POINT);
		builder.append(WS_RESOURCE_LOGIN);
		URI uri = new URI(builder.toString());
		Log.d("est", uri.toString());
		post.setURI(uri);
		return post;
	}

	private String readResponse(HttpResponse response) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return line;
	}
}
