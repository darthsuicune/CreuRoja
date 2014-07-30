package net.creuroja.android.app;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by lapuente on 28.07.14.
 */
public class RailsLoginResponse extends LoginResponse {
	public RailsLoginResponse(int errorMessage) {
		super(errorMessage);
	}

	public RailsLoginResponse(HttpResponse response) {
		super(response);
	}

	@Override public String getToken() {
		return null;
	}

	@Override public void parseResponse(BufferedReader reader) throws IOException {

	}

	@Override public boolean isValid() {
		return false;
	}
}
