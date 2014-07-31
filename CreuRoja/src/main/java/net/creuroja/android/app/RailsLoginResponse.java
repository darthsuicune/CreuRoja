package net.creuroja.android.app;

import android.util.Log;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by lapuente on 28.07.14.
 */
public class RailsLoginResponse extends LoginResponse {
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
		Log.d("Server returns: ", s);
		mToken = s;
	}

	@Override public boolean isValid() {
		return mToken != null;
	}
}
