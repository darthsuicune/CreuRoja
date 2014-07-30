package net.creuroja.android.app.utils;

import net.creuroja.android.app.Location;
import net.creuroja.android.app.LoginResponse;
import net.creuroja.android.app.PHPLoginResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 28.07.14.
 */
public class PHPConnectionClient extends ConnectionClient {
	public static final String EMAIL_VAR = "email";
	public static final String PASS_VAR = "password";
	public static final String ACCESS_TOKEN_VAR = "access_token";
	public static final String LAST_UPDATE_VAR = "last_update";

	private static final String SERVER_URL = "https://creuroja.net";
	private static final String QUERY = "/webservice.php?q=";
	private static final String LOGIN_REQUEST = "request_access";
	private static final String VALIDATE_REQUEST = "validate_access";
	private static final String LOCATIONS_REQUEST = "get_locations";

	@Override
	public LoginResponse doLogin(String email, String password) throws IOException {
		HttpResponse response =
				executeRequest(createHttpClient(), getLoginRequest(email, password));

		return new PHPLoginResponse(response);

	}

	@Override
	public List<Location> requestUpdates(String accessToken, String lastUpdate) throws IOException {
		HttpResponse response =
				executeRequest(createHttpClient(), getLocationsRequest(accessToken, lastUpdate));

		return LocationsProvider.getLocationList(response);
	}

	@Override
	public Boolean validateLogin(String accessToken) throws IOException {
		HttpResponse response =
				executeRequest(createHttpClient(), getValidationRequest(accessToken));
		return parseValidation(response);

	}

	private HttpUriRequest getLoginRequest(String email, String password) {
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair(EMAIL_VAR, email));
		nameValuePairs.add(new BasicNameValuePair(PASS_VAR, password));
		return buildRequest(LOGIN_REQUEST, nameValuePairs);
	}

	private HttpUriRequest getValidationRequest(String accessToken) {
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair(ACCESS_TOKEN_VAR, accessToken));
		return buildRequest(VALIDATE_REQUEST, nameValuePairs);
	}

	private HttpUriRequest getLocationsRequest(String accessToken, String lastUpdate) {
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair(ACCESS_TOKEN_VAR, accessToken));
		nameValuePairs.add(new BasicNameValuePair(LAST_UPDATE_VAR, lastUpdate));
		return buildRequest(LOCATIONS_REQUEST, nameValuePairs);
	}

	private HttpUriRequest buildRequest(String requestType, List<NameValuePair> entity) {
		String address = SERVER_URL + QUERY + requestType + "&" + PROGRAM_VERSION_VAR +
						 PROGRAM_VERSION_NUMBER;
		HttpPost request = new HttpPost(address);
		try {
			request.setEntity(new UrlEncodedFormEntity(entity));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return request;
	}

	public Boolean parseValidation(HttpResponse response) {
		Boolean isValid = true;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			if (reader != null) {
				reader.close();
			}
			// Parse the resulting string. Save to disk if its correct
			isValid = parseResponse(builder.toString());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isValid;
	}

	private Boolean parseResponse(String response) {
		//This is the response that would lead to a invalid user.
		return !response.equals("\"{\\\"isValid\\\":false}\"");
	}
}
