package net.creuroja.android.app.utils.WsConnect;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.List;

/**
 * Created by lapuente on 31.07.14.
 */
public class CRWebServiceClient extends WebServiceClient {

	public CRWebServiceClient(String protocol, String serverUrl) {
		super(protocol, serverUrl);
	}

	@Override public HttpResponse get(String resource, WebServiceFormat format,
									  List<WebServiceOption> options) throws IOException {

		return null;
	}
}
