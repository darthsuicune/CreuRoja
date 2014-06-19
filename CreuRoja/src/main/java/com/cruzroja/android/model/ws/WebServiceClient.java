package com.cruzroja.android.model.ws;

/**
 * Created by lapuente on 18.06.14.
 */
public interface WebServiceClient {
	public LoginResponse signInUser(String username, String password);
	public LocationList getLocations();
	public LocationList getLocations(String lastUpdateTime);
}
