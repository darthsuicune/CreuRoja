package net.creuroja.android.model.webservice;

import net.creuroja.android.model.webservice.lib.RestWebServiceClient;
import net.creuroja.android.model.webservice.lib.WebServiceFormat;
import net.creuroja.android.model.webservice.lib.WebServiceOption;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 06.08.14.
 */
public class RailsWebServiceClient implements CRWebServiceClient {
	public static final String PROTOCOL = "https";
	public static final String URL = "creuroja.net";
	private static final String WS_CR_TAG = "CreuRoja Rails webservice";
	private static final String ARG_EMAIL = "email";
	private static final String ARG_PASSWORD = "password";
	private static final String ARG_ACCESS_TOKEN = "Authorization: Token ";
	private static final String ARG_LAST_UPDATE = "updated_at";
	private static final String RESOURCE_SESSIONS = "sessions";
	private static final String RESOURCE_LOCATIONS = "locations";

	private RestWebServiceClient mClient;
	private ClientConnectionListener listener;

	public RailsWebServiceClient(RestWebServiceClient client, ClientConnectionListener listener) {
		if (client == null) {
			mClient = new RestWebServiceClient(PROTOCOL, URL);
		} else {
			mClient = client;
		}
		this.listener = listener;
	}

	@Override public void signInUser(String email, String password) {
		try {
			List<WebServiceOption> options = getLoginOptions(email, password);
			HttpResponse response = mClient.post(RESOURCE_SESSIONS, WebServiceFormat.JSON, options);
			sendResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			listener.onServerError();
		}
	}

	@Override public void getLocations(String accessToken) {
		getLocations(accessToken, "0");
	}

	@Override public void getLocations(String accessToken, String lastUpdateTime) {
		try {
			List<WebServiceOption> options = getLocationOptions(accessToken, lastUpdateTime);
			HttpResponse response = mClient.get(RESOURCE_LOCATIONS, WebServiceFormat.JSON, options);
			sendResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			listener.onServerError();
		}
	}

	private void sendResponse(HttpResponse response) {
		switch(response.getStatusLine().getStatusCode()) {
			case 200:
				listener.onValidResponse(response);
				break;
			case 401:
				listener.onUnauthorized();
				break;
			case 500:
				listener.onServerError();
				break;
		}
	}

	private List<WebServiceOption> getLoginOptions(String email, String password) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST, ARG_EMAIL, email));
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST, ARG_PASSWORD, password));
		return options;
	}

	private List<WebServiceOption> getLocationOptions(String accessToken, String lastUpdateTime) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(new WebServiceOption(WebServiceOption.OptionType.HEADER, ARG_ACCESS_TOKEN,
				"token=\"" + accessToken + "\""));

		if (!lastUpdateTime.equals("0")) {
			options.add(new WebServiceOption(WebServiceOption.OptionType.GET, ARG_LAST_UPDATE,
					lastUpdateTime));
		}
		return options;
	}
}
