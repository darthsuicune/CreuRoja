package net.creuroja.android.webservice;

import net.creuroja.android.webservice.lib.RestWebServiceClient;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by lapuente on 06.08.14.
 */
public class RailsLoginResponse implements LoginResponse {
	public static final String AUTH_TOKEN_HOLDER = "token";

	private boolean isValid = false;
	private String authToken;
	private int errorCode = 0;
	private String errorMessage;

	public RailsLoginResponse(HttpResponse response) {
		try {
			JSONObject object = new JSONObject(RestWebServiceClient.getAsString(response));
			if(object.has(AUTH_TOKEN_HOLDER)) {
				authToken = object.getString(AUTH_TOKEN_HOLDER);
				isValid = true;
			} else {
				errorCode = object.getInt(ERROR_CODE);
				errorMessage = object.getString(ERROR_MESSAGE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override public boolean isValid() {
		return isValid;
	}

	@Override public String authToken() {
		return authToken;
	}

	@Override public int errorCode() {
		return errorCode;
	}

	@Override public String errorMessage() {
		return errorMessage;
	}
}
