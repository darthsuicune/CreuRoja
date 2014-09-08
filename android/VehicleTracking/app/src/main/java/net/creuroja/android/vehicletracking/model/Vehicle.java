package net.creuroja.android.vehicletracking.model;

import android.util.Log;

import net.creuroja.android.vehicletracking.model.webservice.Auth;
import net.creuroja.android.vehicletracking.model.webservice.ServerData;
import net.creuroja.android.vehicletracking.model.webservice.lib.RestWebServiceClient;
import net.creuroja.android.vehicletracking.model.webservice.lib.WebServiceOption;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lapuente on 04.09.14.
 */
public class Vehicle {
	public final String indicative;
	public double latitude;
	public double longitude;
	public int id;

	public Vehicle(JSONObject json) throws JSONException {
		this.indicative = json.getString(ServerData.INDICATIVE);
		this.id = json.getInt(ServerData.ID);
	}

	public Vehicle(int id, String indicative) {
		this.indicative = indicative;
		this.id = id;
	}

	public void setPosition(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void upload(String accessToken) throws IOException {
		RestWebServiceClient client = new RestWebServiceClient(RestWebServiceClient.PROTOCOL_HTTPS,
				ServerData.SERVER_ADDRESS);
		HttpResponse response = post(accessToken);
		switch (response.getStatusLine().getStatusCode()) {
			case 200:
				Log.d("Something", "Something, something something");
				break;
			default:
				Log.d("Something", "Something, something");
				break;
		}

	}

	private HttpResponse post(String accessToken) throws IOException {
		HttpClient client = new DefaultHttpClient();
		return client.execute(getRequest(accessToken));
	}

	private HttpUriRequest getRequest(String accessToken) throws UnsupportedEncodingException {
		HttpPost post = new HttpPost(
				RestWebServiceClient.PROTOCOL_HTTPS + "://" + ServerData.SERVER_ADDRESS + "/" +
				ServerData.RESOURCE_NEW_VEHICLE_POSITION + ".json");
		setAuth(post, accessToken);
		setParams(post);
		return post;
	}

	private void setParams(HttpPost post) throws UnsupportedEncodingException {
		String json = asJson().toString();
		post.setEntity(new StringEntity(asJson().toString()));
	}

	private void setAuth(HttpPost post, String accessToken) {
		String name = ServerData.ARG_ACCESS_TOKEN;
		String value = "token=\"" + accessToken + "\"";
		post.addHeader(name, value);
		post.addHeader("Content-Type", "application/json");
	}

	private List<WebServiceOption> getOptions(String accessToken) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(Auth.getAuthOption(accessToken));
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST,
				ServerData.ARG_VEHICLE_POSITION, asJson().toString()));
		return options;
	}

	public JSONObject asJson() {
		Map<String, Map<String, String>> holder = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		map.put(ServerData.ARG_VEHICLE_ID, Integer.toString(id));
		map.put(ServerData.ARG_VEHICLE_LATITUDE, Double.toString(latitude));
		map.put(ServerData.ARG_VEHICLE_LONGITUDE, Double.toString(longitude));
		holder.put(ServerData.ARG_VEHICLE_POSITION, map);
		return new JSONObject(holder);
	}

	public String toString() {
		return indicative;
	}
}
