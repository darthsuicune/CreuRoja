package org.creuroja.android.webservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.creuroja.android.model.LocationList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * Created by lapuente on 06.08.14.
 */
public class RailsWebServiceClient implements CRWebServiceClient {
	private static final String WS_CR_TAG = "CreuRoja Rails webservice";
	private static final String ARG_EMAIL = "email";
	private static final String ARG_PASSWORD = "password";
	private static final String ARG_ACCESS_TOKEN = "token";
	private static final String ARG_LAST_UPDATE = "updated_at";

	private static final String URL = "https://creuroja.net/";
	private static final String RESOURCE_SESSIONS = "sessions";
	private static final String RESOURCE_LOCATIONS = "locations";


	@Override public LoginResponse signInUser(String username, String password) {
		HttpClient client = getHttpClient();
		try {
			HttpUriRequest request = getLoginRequest(username, password);
			HttpResponse response = client.execute(request);
			String result = readResponse(response);
			return new RailsLoginResponse(result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override public LocationList getLocations(String accessToken) {
		return getLocations(accessToken, "0");
	}

	@Override public LocationList getLocations(String accessToken, String lastUpdateTime) {
		return null;
	}

	private HttpClient getHttpClient() {
		BasicHttpParams params = new BasicHttpParams();
		DefaultHttpClient client = new DefaultHttpClient(params);
		return client;
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

	/**
	 * A login request performs a POST request to the sessions resource. This will return
	 * the session token from the server once executed
	 * @param username
	 * @param password
	 * @return the UriRequest for the connection to be performed.
	 * @throws URISyntaxException
	 */
	private HttpUriRequest getLoginRequest(String username, String password)
			throws URISyntaxException {
		
		return null;
	}
}
