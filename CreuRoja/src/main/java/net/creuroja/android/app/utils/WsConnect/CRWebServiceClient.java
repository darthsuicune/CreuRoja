package net.creuroja.android.app.utils.WsConnect;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

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
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest request = getRequest(WebServiceRequestType.GET, resource, format, options);
		return client.execute(request);
	}

	@Override public HttpResponse post(String resource, WebServiceFormat format,
									   List<WebServiceOption> options) throws IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest request = getRequest(WebServiceRequestType.POST, resource, format, options);
		return client.execute(request);
	}
}
