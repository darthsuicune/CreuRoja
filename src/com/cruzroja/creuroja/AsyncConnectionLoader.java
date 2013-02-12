package com.cruzroja.creuroja;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

public class AsyncConnectionLoader extends AsyncTaskLoader<ArrayList<String>> {
	ArrayList<String> mItemsList;

	public AsyncConnectionLoader(Context context, Bundle args) {
		super(context);
		mItemsList = new ArrayList<String>();
	}

	@Override
	public ArrayList<String> loadInBackground() {
		String response = connectToServer();
		parseResponse(response);

		return mItemsList;
	}

	private String connectToServer() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost();
		try {
			HttpResponse response = httpClient.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			return reader.readLine();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void parseResponse(String response) {
		
	}
}
