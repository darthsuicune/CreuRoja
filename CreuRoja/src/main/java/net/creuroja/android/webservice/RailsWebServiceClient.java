package net.creuroja.android.webservice;

import net.creuroja.android.model.CRLocationList;
import net.creuroja.android.model.LocationList;
import net.creuroja.android.webservice.lib.RestWebServiceClient;
import net.creuroja.android.webservice.lib.WebServiceFormat;
import net.creuroja.android.webservice.lib.WebServiceOption;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	private static final String ARG_ACCESS_TOKEN = "token";
	private static final String ARG_LAST_UPDATE = "updated_at";
	private static final String RESOURCE_SESSIONS = "sessions";
	private static final String RESOURCE_LOCATIONS = "locations";

	private RestWebServiceClient mClient;

	public RailsWebServiceClient(RestWebServiceClient client) {
		if (client == null) {
			new RestWebServiceClient(PROTOCOL, URL);
		} else {
			mClient = client;
		}
	}

	@Override public LoginResponse signInUser(String email, String password) {
		try {
			List<WebServiceOption> options = getLoginOptions(email, password);
			HttpResponse response = mClient.get(RESOURCE_SESSIONS, WebServiceFormat.JSON, options);
			return new RailsLoginResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override public LocationList getLocations(String accessToken) {
		return getLocations(accessToken, "0");
	}

	@Override public LocationList getLocations(String accessToken, String lastUpdateTime) {
		try {
			List<WebServiceOption> options = getLocationOptions(accessToken, lastUpdateTime);
			HttpResponse response = mClient.get(RESOURCE_LOCATIONS, WebServiceFormat.JSON, options);
			return new CRLocationList(response);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private HttpClient getHttpClient() {
		BasicHttpParams params = new BasicHttpParams();
		DefaultHttpClient client = new DefaultHttpClient(params);
		return client;
	}

	private String readResponse(HttpResponse response) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		reader.close();
		return builder.toString();
	}

	public List<WebServiceOption> getLoginOptions(String email, String password) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST, ARG_EMAIL, email));
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST, ARG_PASSWORD, password));
		return options;
	}

	private List<WebServiceOption> getLocationOptions(String accessToken, String lastUpdateTime) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(new WebServiceOption(WebServiceOption.OptionType.HEADER, ARG_ACCESS_TOKEN,
				accessToken));
		if (!lastUpdateTime.equals("0")) {
			options.add(new WebServiceOption(WebServiceOption.OptionType.GET, ARG_LAST_UPDATE,
					lastUpdateTime));
		}
		return options;
	}
}
