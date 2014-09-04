package net.creuroja.android.app.utils;

import net.creuroja.android.app.Location;
import net.creuroja.android.app.LoginResponse;
import net.creuroja.android.app.RailsLoginResponse;
import net.creuroja.android.app.utils.WsConnect.CRWebServiceClient;
import net.creuroja.android.app.utils.WsConnect.WebServiceFormat;
import net.creuroja.android.app.utils.WsConnect.WebServiceOption;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 28.07.14.
 */
public class RailsConnectionClient extends ConnectionClient {
	private static final String SERVER_PROTOCOL = "https";
	private static final String SERVER_URL = "creuroja.net";
	private static final String LOGIN_REQUEST = "sessions";
	private static final String LOCATIONS_REQUEST = "locations";
	private static final String ACCESS_TOKEN_HEADER = "Authorization: Token ";
	private static final String LAST_UPDATE_PARAMETER = "updated_at";

	CRWebServiceClient client;

	public RailsConnectionClient() {
		client = new CRWebServiceClient(SERVER_PROTOCOL, SERVER_URL);
	}

	/**
	 * Method that connects to the rails webservice and authenticates
	 *
	 * @param email
	 * @param password
	 * @return
	 * @throws IOException
	 */
	@Override
	public LoginResponse doLogin(String email, String password) throws IOException {
		List<WebServiceOption> options = createLoginOptions(email, password);
		HttpResponse httpResponse = client.post(LOGIN_REQUEST, WebServiceFormat.JSON, options);
		LoginResponse response = new RailsLoginResponse(httpResponse);
		return response;
	}

	@Override
	public List<Location> requestUpdates(String accessToken, String lastUpdate) throws IOException {
		List<WebServiceOption> options = createLocationsOptions(accessToken, lastUpdate);
		HttpResponse response = client.get(LOCATIONS_REQUEST, WebServiceFormat.JSON, options);
		return LocationsProvider.getLocationList(response);
	}

	@Override
	public Boolean validateLogin(String accessToken) throws IOException {
		List<WebServiceOption> options = createLocationsOptions(accessToken, null);
		HttpResponse response = client.get(LOCATIONS_REQUEST, WebServiceFormat.JSON, options);
		return response == null || response.getStatusLine().getStatusCode() != 401;
	}

	private List<WebServiceOption> createLoginOptions(String email, String password) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST, EMAIL_VAR, email));
		options.add(new WebServiceOption(WebServiceOption.OptionType.POST, PASS_VAR, password));
		return options;
	}

	private List<WebServiceOption> createLocationsOptions(String accessToken, String lastUpdate) {
		List<WebServiceOption> options = new ArrayList<>();
		options.add(new WebServiceOption(WebServiceOption.OptionType.HEADER, ACCESS_TOKEN_HEADER,
				"token=\"" + accessToken + "\""));
		if(lastUpdate != null){
			options.add(new WebServiceOption(WebServiceOption.OptionType.GET, LAST_UPDATE_PARAMETER,
					lastUpdate));
		}
		return options;
	}
}
