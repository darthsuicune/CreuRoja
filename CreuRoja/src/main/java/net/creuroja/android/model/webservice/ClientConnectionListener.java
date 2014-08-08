package net.creuroja.android.model.webservice;

import org.apache.http.HttpResponse;

/**
 * Created by lapuente on 08.08.14.
 */

public interface ClientConnectionListener {
	public void onValidResponse(HttpResponse response);
	public void onUnauthorized();
	public void onServerError();
}
