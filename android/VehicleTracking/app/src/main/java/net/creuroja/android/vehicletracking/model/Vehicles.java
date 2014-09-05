package net.creuroja.android.vehicletracking.model;

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
	private static final String SERVER_ADDRESS = "creuroja.net";
	private static final String RESOURCE_VEHICLES = "vehicles";

	public static final String KEY_VEHICLES = "vehicles";

	private static final String ARG_ACCESS_TOKEN = "Authorization: Token ";
	private List<Vehicle> mVehicleList;

	public Vehicles(String json) throws JSONException {
		mVehicleList = new ArrayList<>();
		JSONObject object = new JSONObject(json);
		if (object.has(KEY_VEHICLES)) {
			JSONArray array = object.getJSONArray(KEY_VEHICLES);
			for (int i = 0; i < array.length(); i++) {
				mVehicleList.add(new Vehicle(array.getJSONObject(i)));
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
		RestWebServiceClient client =
				new RestWebServiceClient(RestWebServiceClient.PROTOCOL_HTTPS, SERVER_ADDRESS);
		List<WebServiceOption> options = getOptions(accessToken);
		HttpResponse response = client.get(RESOURCE_VEHICLES, WebServiceFormat.JSON, options);
		return RestWebServiceClient.getAsString(response);
	}

	private static List<WebServiceOption> getOptions(String accessToken) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(new WebServiceOption(WebServiceOption.OptionType.HEADER, ARG_ACCESS_TOKEN,
				"token=\"" + accessToken + "\""));
		return options;
	}
}
