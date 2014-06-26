package org.creuroja.android.model.ws;

/**
 * Created by denis on 19.06.14.
 */
public interface LoginResponse {
	public boolean isValid();
	public String authToken();
	public int errorCode();
	public String errorMessage();
}
