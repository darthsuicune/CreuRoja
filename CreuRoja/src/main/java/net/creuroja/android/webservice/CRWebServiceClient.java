package net.creuroja.android.webservice;

import net.creuroja.android.model.LocationList;

/**
 * Created by lapuente on 18.06.14.
 */
public interface CRWebServiceClient {
	public LoginResponse signInUser(String username, String password);
	public LocationList getLocations(String accessToken);
	public LocationList getLocations(String accessToken, String lastUpdateTime);
}
