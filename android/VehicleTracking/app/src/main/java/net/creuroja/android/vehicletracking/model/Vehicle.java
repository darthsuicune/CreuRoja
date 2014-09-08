package net.creuroja.android.vehicletracking.model;

import android.util.Log;

import net.creuroja.android.vehicletracking.model.webservice.Auth;
import net.creuroja.android.vehicletracking.model.webservice.ServerData;
import net.creuroja.android.vehicletracking.model.webservice.lib.RestWebServiceClient;
import net.creuroja.android.vehicletracking.model.webservice.lib.WebServiceFormat;
import net.creuroja.android.vehicletracking.model.webservice.lib.WebServiceOption;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		HttpResponse response =
				client.post(ServerData.RESOURCE_NEW_VEHICLE_POSITION, WebServiceFormat.JSON,
						getOptions(accessToken));
		switch (response.getStatusLine().getStatusCode()) {
			case 200:
				Log.d("Something", "Something, something something");
				break;
			default:
				Log.d("Something", "Something, something");
				break;
		}

	}

	private List<WebServiceOption> getOptions(String accessToken) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(Auth.getAuthOption(accessToken));
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST,
				ServerData.ARG_VEHICLE_POSITION, this.asJson()));
		return options;
	}

	private String asJson() {
		return "{" +
			   "\"" + ServerData.ARG_VEHICLE_ID + "\":\"" + id + "\","+
			   "\"" + ServerData.ARG_VEHICLE_LATITUDE + "\":" + latitude + ","+
			   "\"" + ServerData.ARG_VEHICLE_LONGITUDE + "\":" + longitude + ""+
			   "}";
	}

	public String toString() {
		return indicative;
	}
}
