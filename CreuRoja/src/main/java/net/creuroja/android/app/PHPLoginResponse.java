package net.creuroja.android.app;

import net.creuroja.android.R;
import net.creuroja.android.app.utils.LocationsProvider;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by lapuente on 28.07.14.
 */
public class PHPLoginResponse extends LoginResponse {
	public static final String sAccessToken = "accessToken";
	public static final String sAccessTokenString = "accessTokenString";
	public AccessToken mToken;

	public PHPLoginResponse(int errorMessage) {
		super(errorMessage);
	}

	public PHPLoginResponse(HttpResponse response) {
		super(response);
	}

	@Override public String getToken() {
		return mToken.mAccessToken;
	}

	@Override
	public void parseResponse(BufferedReader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		try {
			JSONObject object = new JSONObject(builder.toString());
			if(object.has(sAccessToken)){
				mToken = new AccessToken(object.getJSONObject(sAccessToken));
				mLocationList = LocationsProvider.getLocationList(object);
			} else {
				mToken = null;
				mLocationList = null;
				mErrorMessage = R.string.error_invalid_password;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mToken = null;
			mLocationList = null;
			mErrorMessage = R.string.error_invalid_response;
		}
	}

	@Override
	public boolean isValid() {
		return mToken != null;
	}

	public class AccessToken {
		public String mAccessToken;

		public AccessToken(JSONObject object) throws JSONException {
			mAccessToken = object.getString(sAccessTokenString);
		}
	}
}
