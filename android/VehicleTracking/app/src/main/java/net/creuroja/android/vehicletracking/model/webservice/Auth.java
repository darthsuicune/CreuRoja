package net.creuroja.android.vehicletracking.model.webservice;

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
 * Created by lapuente on 05.09.14.
 */
public class Auth {
	RestWebServiceClient mClient;

	public Auth() {
		mClient = new RestWebServiceClient(RestWebServiceClient.PROTOCOL_HTTPS,
				ServerData.SERVER_ADDRESS);
	}

	public String getToken(String email, String password) {
		try {
			List<WebServiceOption> options = getLoginOptions(email, password);
			HttpResponse response =
					mClient.post(ServerData.RESOURCE_SESSIONS, WebServiceFormat.JSON, options);
			return parseResponse(RestWebServiceClient.getAsString(response));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String parseResponse(String json) {
		String authToken = null;
		try {
			JSONObject object = new JSONObject(json);
			if (object.has(ServerData.AUTH_TOKEN_HOLDER)) {
				authToken = object.getString(ServerData.AUTH_TOKEN_HOLDER);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return authToken;
	}

	private List<WebServiceOption> getLoginOptions(String email, String password) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST, ServerData.ARG_EMAIL,
				email));
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST, ServerData.ARG_PASSWORD,
				password));
		return options;
	}

	public static WebServiceOption getAuthOption(String accessToken) {
		return new WebServiceOption(WebServiceOption.OptionType.HEADER, ServerData.ARG_ACCESS_TOKEN,
				"token=\"" + accessToken + "\"");
	}
}
