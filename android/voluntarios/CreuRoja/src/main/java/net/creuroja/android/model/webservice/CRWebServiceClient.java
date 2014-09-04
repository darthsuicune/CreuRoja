package net.creuroja.android.model.webservice;

/**
 * Created by lapuente on 18.06.14.
 */
public interface CRWebServiceClient {
	public void signInUser(String username, String password);
	public void getLocations(String accessToken);
	public void getLocations(String accessToken, String lastUpdateTime);
}