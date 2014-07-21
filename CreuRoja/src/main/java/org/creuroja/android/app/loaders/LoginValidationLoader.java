package org.creuroja.android.app.loaders;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import org.apache.http.HttpResponse;
import org.creuroja.android.app.Settings;
import org.creuroja.android.app.utils.ConnectionClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lapuente on 15.07.14.
 */
public class LoginValidationLoader extends AsyncTaskLoader<Boolean> {
	public static final String PARAMETER_IS_VALID = "isValid";

	private ConnectionClient mClient;

	public LoginValidationLoader(Context context) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if (mClient == null) {
			forceLoad();
		}
	}

	@Override
	public Boolean loadInBackground() {
		mClient = new ConnectionClient();
		Boolean isValid = true;
		String accessToken = PreferenceManager.getDefaultSharedPreferences(getContext())
				.getString(Settings.ACCESS_TOKEN, "");
		try {
			isValid = mClient.validateLogin(accessToken);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isValid;
	}

	public static Boolean parse(HttpResponse response) {
		Boolean isValid = true;
		BufferedReader reader;
		try {
			reader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			if (reader != null) {
				reader.close();
			}
			// Parse the resulting string. Save to disk if its correct
			isValid = parseResponse(builder.toString());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isValid;
	}

	private static Boolean parseResponse(String response) {
		//This is the response that would lead to a invalid user.
		return !response.equals("\"{\\\"isValid\\\":false}\"");
	}
}
