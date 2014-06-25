package com.cruzroja.android.model.ws;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by denis on 19.06.14.
 */
public class CRLoginResponse implements LoginResponse {
	public static final String IS_VALID = "isValid";
	public static final String AUTH_TOKEN_HOLDER = "accessToken";
	public static final String AUTH_TOKEN = "accessTokenString";
	public static final String ERROR_CODE = "errorCode";
	public static final String ERROR_MESSAGE = "errorMessage";

	private boolean isValid = false;
	private String authToken = null;
	private int errorCode = 0;
	private String errorMessage;

	public CRLoginResponse(String json) {
		try {
			JSONObject response = new JSONObject(json);
			if(response.has(AUTH_TOKEN_HOLDER)) {
				authToken = response.getJSONObject(AUTH_TOKEN_HOLDER).getString(AUTH_TOKEN);
				isValid = true;
			} else {
				errorCode = response.getInt(ERROR_CODE);
				errorMessage = response.getString(ERROR_MESSAGE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	@Override
	public String authToken() {
		return authToken;
	}

	@Override
	public int errorCode() {
		return errorCode;
	}

	@Override
	public String errorMessage() {
		return errorMessage;
	}
}
