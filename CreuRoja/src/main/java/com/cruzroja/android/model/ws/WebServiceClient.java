package com.cruzroja.android.model.ws;

import com.cruzroja.android.model.Location;

import java.util.List;

/**
 * Created by lapuente on 18.06.14.
 */
public interface WebServiceClient {
	public String signInUser(String username, String password);
	public List<Location> getLocations();
	public List<Location> getLocations(String lastUpdateTime);
}
