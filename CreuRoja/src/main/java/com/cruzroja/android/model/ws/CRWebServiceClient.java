package com.cruzroja.android.model.ws;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 18.06.14.
 */
public class CRWebServiceClient implements WebServiceClient {
	public static final String ARG_EMAIL = "email";
	public static final String ARG_PASSWORD = "password";
	public static final String ARG_ACCESS_TOKEN = "access_token";
	public static final String ARG_LAST_UPDATE = "last_update";

	private static final String WS_URL = "http://kls.servequake.com:808/";
	private static final String WS_CONNECTION_POINT = "webservice.php";
	private static final String WS_RESOURCE_LOGIN = "?q=request_access";
	private static final String WS_RESOURCE_LOCATIONS = "?q=get_locations";

	@Override
	public LoginResponse signInUser(String username, String password) {
		HttpClient client = getHttpClient();
		try {
			HttpUriRequest request = getLoginRequest(username, password);
			HttpResponse response = client.execute(request);
			String result = readResponse(response);
			return new CRLoginResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LocationList getLocations() {
		return getLocations("0");
	}

	@Override
	public LocationList getLocations(String lastUpdateTime) {

		HttpClient client = getHttpClient();
		try {
			HttpUriRequest request = getLocationsRequest();
			HttpResponse response = client.execute(request);
			String result = readResponse(response);
			return new CRLocationList(result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.d("WSClient", "Incorrect URL");
			return null;
		}
	}

	private HttpClient getHttpClient() {
		BasicHttpParams params = new BasicHttpParams();
		DefaultHttpClient client = new DefaultHttpClient(params);
		return client;
	}

	private HttpUriRequest getLoginRequest(String email, String password)
			throws URISyntaxException, UnsupportedEncodingException {
		HttpPost request = new HttpPost();
		StringBuilder builder = new StringBuilder();
		builder.append(WS_URL);
		builder.append(WS_CONNECTION_POINT);
		builder.append(WS_RESOURCE_LOGIN);
		URI uri = new URI(builder.toString());
		request.setURI(uri);
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair(ARG_EMAIL, email));
		nameValuePairs.add(new BasicNameValuePair(ARG_PASSWORD, password));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return request;
	}

	private HttpUriRequest getLocationsRequest() throws URISyntaxException {
		HttpPost post = new HttpPost();
		StringBuilder builder = new StringBuilder();
		builder.append(WS_URL);
		builder.append(WS_CONNECTION_POINT);
		builder.append(WS_RESOURCE_LOCATIONS);
		URI uri = new URI(builder.toString());
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
		return builder.toString();
	}
}
