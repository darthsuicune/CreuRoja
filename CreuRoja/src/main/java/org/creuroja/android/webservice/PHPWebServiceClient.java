package org.creuroja.android.webservice;

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
import org.creuroja.android.model.CRLocationList;
import org.creuroja.android.model.LocationList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 18.06.14.
 */
public class PHPWebServiceClient implements CRWebServiceClient {
	private static final String WS_CR_TAG = "CreuRoja webservice PHP test";
	private static final String ARG_EMAIL = "email";
	private static final String ARG_PASSWORD = "password";
	private static final String ARG_ACCESS_TOKEN = "access_token";
	private static final String ARG_LAST_UPDATE = "last_update";

	private static final String WS_URL = "http://kls.servequake.com:808/";
	private static final String WS_CONNECTION_POINT = "webservice.php";
	private static final String WS_RESOURCE_LOGIN = "?q=request_access";
	private static final String WS_RESOURCE_LOCATIONS = "?q=get_locations";
	private static final String WS_VERSION = "&version=";
	private static final String ARG_VERSION = "2.0";

	@Override
	public LoginResponse signInUser(String username, String password) {
		HttpClient client = getHttpClient();
		try {
			HttpUriRequest request = getLoginRequest(username, password);
			HttpResponse response = client.execute(request);
			String result = readResponse(response);
			return new PHPLoginResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LocationList getLocations(String accessToken) {
		return getLocations(accessToken, "0");
	}

	@Override
	public LocationList getLocations(String accessToken, String lastUpdateTime) {
		HttpClient client = getHttpClient();
		try {
			HttpUriRequest request = getLocationsRequest(lastUpdateTime, accessToken);
			HttpResponse response = client.execute(request);
			String result = readResponse(response);
			return new CRLocationList(result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.d(WS_CR_TAG, "Incorrect URL " + e.getMessage());
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
		HttpPost request =
				new HttpPost(WS_URL + WS_CONNECTION_POINT + WS_RESOURCE_LOGIN + WS_VERSION +
							 ARG_VERSION);
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair(ARG_EMAIL, email));
		nameValuePairs.add(new BasicNameValuePair(ARG_PASSWORD, password));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return request;
	}

	private HttpUriRequest getLocationsRequest(String lastUpdateTime, String accessToken)
			throws URISyntaxException, UnsupportedEncodingException {
		HttpPost request = new HttpPost(WS_URL + WS_CONNECTION_POINT + WS_RESOURCE_LOCATIONS +
										WS_VERSION + ARG_VERSION);
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair(ARG_LAST_UPDATE, lastUpdateTime));
		nameValuePairs.add(new BasicNameValuePair(ARG_ACCESS_TOKEN, accessToken));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return request;
	}

	private String readResponse(HttpResponse response) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		reader.close();
		return builder.toString();
	}
}
