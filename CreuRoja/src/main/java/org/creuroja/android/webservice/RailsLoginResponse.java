package org.creuroja.android.webservice;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lapuente on 06.08.14.
 */
public class RailsLoginResponse implements LoginResponse {
	public static final String AUTH_TOKEN_HOLDER = "token";

	private boolean isValid = false;
	private String authToken;
	private int errorCode = 0;
	private String errorMessage;

	public RailsLoginResponse(String json) {
		try {
			JSONObject response = new JSONObject(json);
			if(response.has(AUTH_TOKEN_HOLDER)) {
				authToken = response.getString(AUTH_TOKEN_HOLDER);
				isValid = true;
			} else {
				errorCode = response.getInt(ERROR_CODE);
				errorMessage = response.getString(ERROR_MESSAGE);
			}
		} catch (JSONException e) {
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
