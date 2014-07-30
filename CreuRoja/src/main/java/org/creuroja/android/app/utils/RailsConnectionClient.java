package org.creuroja.android.app.utils;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.creuroja.android.app.Location;
import org.creuroja.android.app.LoginResponse;

import java.io.IOException;
import java.util.List;

/**
 * Created by lapuente on 28.07.14.
 */
public class RailsConnectionClient extends ConnectionClient {
	private static final String SERVER_URL = "http://testing.creuroja.net/";
	private static final String LOGIN_REQUEST = "sessions/create";
	private static final String VALIDATE_REQUEST = "sessions";
	private static final String LOCATIONS_REQUEST = "locations";
	private static final String FORMAT = ".json";
	private static final String VAR_SEPARATOR = "?";

	/**
	 * Method that connects to the rails webservice and authenticates
	 * @param email
	 * @param password
	 * @return
	 * @throws IOException
	 */
	@Override
	public LoginResponse doLogin(String email, String password) throws IOException {

		return null;
	}

	@Override
	public List<Location> requestUpdates(String accessToken, String lastUpdate)
			throws IOException {
		return null;
	}

	@Override
	public Boolean validateLogin(String accessToken) throws IOException {
		return null;
	}

	private HttpUriRequest buildRequest(String requestType) {
		String address = "" + FORMAT + VAR_SEPARATOR + PROGRAM_VERSION_VAR + PROGRAM_VERSION_NUMBER;
		HttpUriRequest request = new HttpPost(address);
		return request;
	}
}
