package net.creuroja.android.app;

import net.creuroja.android.R;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by lapuente on 28.07.14.
 */
public class RailsLoginResponse extends LoginResponse {
	private static final String ERROR = "error";
	private static final String STATUS = "status";
	private static final String TOKEN = "token";

	private String mToken;
	public RailsLoginResponse(int errorMessage) {
		super(errorMessage);
	}

	public RailsLoginResponse(HttpResponse response) {
		super(response);
	}

	@Override public String getToken() {
		return mToken;
	}

	@Override public void parseResponse(BufferedReader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		String line = reader.readLine();
		while (line != null){
			builder.append(line);
			line = reader.readLine();
		}
		parseToken(builder.toString());
	}

	private void parseToken(String s) {
		try {
			JSONObject object = new JSONObject(s);
			if(object.has(TOKEN)){
				mToken = object.getString(TOKEN);
			} else {
				if (object.has(ERROR)) {
					if(object.getInt(ERROR) == 401) {
						fail(R.string.error_invalid_password);
					} else {
						fail(R.string.error_invalid_response);
					}
				}
			}
		} catch (JSONException e) {
			fail(R.string.error_invalid_response);
			e.printStackTrace();
		}
	}

	private void fail(int message) {
		mToken = null;
		mLocationList = null;
		mErrorMessage = message;
	}

	@Override public boolean isValid() {
		return mToken != null;
	}
}
