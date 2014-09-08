package net.creuroja.android.vehicletracking.model;

import android.text.TextUtils;

import net.creuroja.android.vehicletracking.model.webservice.Auth;
import net.creuroja.android.vehicletracking.model.webservice.ServerData;
import net.creuroja.android.vehicletracking.model.webservice.lib.RestWebServiceClient;
import net.creuroja.android.vehicletracking.model.webservice.lib.WebServiceFormat;
import net.creuroja.android.vehicletracking.model.webservice.lib.WebServiceOption;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 04.09.14.
 */
public class Vehicles {
	private List<Vehicle> mVehicleList;

	public Vehicles(String json) throws JSONException {
		mVehicleList = new ArrayList<>();
		JSONArray array = new JSONArray(json);
		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.getJSONObject(i);
			if (!TextUtils.isEmpty(item.getString(ServerData.INDICATIVE)) &&
				!item.getString(ServerData.INDICATIVE).equals("null")) {
				mVehicleList.add(new Vehicle(item));
			}
		}
	}

	public List<Vehicle> getAsList() {
		return mVehicleList;
	}

	public static List<Vehicle> getFromServer(String accessToken)
			throws JSONException, IOException {
		String jsonResponse = requestVehicleList(accessToken);
		Vehicles vehicles = new Vehicles(jsonResponse);
		return vehicles.getAsList();
	}

	private static String requestVehicleList(String accessToken) throws IOException {
		RestWebServiceClient client = new RestWebServiceClient(RestWebServiceClient.PROTOCOL_HTTPS,
				ServerData.SERVER_ADDRESS);
		List<WebServiceOption> options = getOptions(accessToken);
		HttpResponse response =
				client.get(ServerData.RESOURCE_VEHICLES, WebServiceFormat.JSON, options);
		String json = RestWebServiceClient.getAsString(response);
		return json;
	}

	private static List<WebServiceOption> getOptions(String accessToken) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(Auth.getAuthOption(accessToken));
		return options;
	}
}
