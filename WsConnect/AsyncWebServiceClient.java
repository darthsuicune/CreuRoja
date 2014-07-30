package com.abbyy.eu.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by lapuente on 30.07.14.
 */
public class AsyncWebServiceClient extends WebServiceClient {
	WebServiceClientListener listener;

	public AsyncWebServiceClient(WebServiceClientListener listener, String protocol,
								 String serverUrl) {
		super(protocol, serverUrl);
		this.listener = listener;
	}

	@Override public synchronized HttpResponse get(String resource, WebServiceFormat format,
									  List<WebServiceOption> options) throws IOException {

		Thread thread = new Thread(
				new AsyncWebServiceClientRunner(WebServiceRequestType.GET, listener, resource,
						format, options));
		thread.start();
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override public HttpResponse put(String resource, WebServiceFormat format,
									  List<WebServiceOption> options) throws IOException {
		return null;
	}

	@Override public HttpResponse post(String resource, WebServiceFormat format,
									   List<WebServiceOption> options) throws IOException {
		return null;
	}

	@Override public HttpResponse patch(String resource, WebServiceFormat format,
										List<WebServiceOption> options) throws IOException {
		return null;
	}

	@Override public HttpResponse delete(String resource, WebServiceFormat format,
										 List<WebServiceOption> options) throws IOException {
		return null;
	}

	@Override
	protected HttpResponse getHttpResponse(String resource, WebServiceFormat format,
										   List<WebServiceOption> options) throws IOException {
		Thread thread = new Thread(
				new AsyncWebServiceClientRunner(WebServiceRequestType.GET, listener, resource,
						format, options));
		thread.start();
		return null;
	}

	private class AsyncWebServiceClientRunner implements Runnable {
		String resource;
		WebServiceFormat format;
		List<WebServiceOption> options;
		WebServiceClientListener listener;
		WebServiceRequestType requestType;

		public AsyncWebServiceClientRunner(WebServiceRequestType requestType,
										   WebServiceClientListener listener, String resource,
										   WebServiceFormat format,
										   List<WebServiceOption> options) {
			this.resource = resource;
			this.format = format;
			this.options = options;
			this.listener = listener;
			this.requestType = requestType;
		}

		@Override
		public synchronized void run() {
			CloseableHttpClient client = getClient();
			try {
				HttpUriRequest request = getRequest(requestType, resource, format, options);
				HttpResponse response = client.execute(request);
				parseResult(response);
				client.close();
			} catch (ClientProtocolException e) {
				listener.onError("ClientProtocolException");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				listener.onError("UnsupportedEncodingException");
				e.printStackTrace();
			} catch (IOException e) {
				listener.onError("IOException");
				e.printStackTrace();
			}
			notify();
		}

		private void parseResult(HttpResponse response) throws IOException {
			switch (response.getStatusLine().getStatusCode()) {
				case 200:
					String result = getAsString(response);
					listener.onSuccess(result);
					break;
				case 401:
					listener.onError("Unauthorized");
					break;
				case 404:
					listener.onError("404");
					break;
				default:
					listener.onError("Unknown error " + response.getStatusLine().getStatusCode());
			}
		}
	}

	public interface WebServiceClientListener {
		public void onSuccess(String response);

		public void onError(String response);
	}
}
