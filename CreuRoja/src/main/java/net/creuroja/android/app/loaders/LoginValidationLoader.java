package net.creuroja.android.app.loaders;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import net.creuroja.android.app.Settings;
import net.creuroja.android.app.utils.ConnectionClient;
import net.creuroja.android.app.utils.RailsConnectionClient;

import java.io.IOException;

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
		mClient = new RailsConnectionClient();
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
}
