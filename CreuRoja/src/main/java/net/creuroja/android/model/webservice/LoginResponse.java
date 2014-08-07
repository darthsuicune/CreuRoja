package net.creuroja.android.model.webservice;

/**
 * Created by denis on 19.06.14.
 */
public interface LoginResponse {
	public static final String IS_VALID = "isValid";
	public static final String ERROR_CODE = "errorCode";
	public static final String ERROR_MESSAGE = "errorMessage";

	public boolean isValid();
	public String authToken();
	public int errorCode();
	public String errorMessage();
}
