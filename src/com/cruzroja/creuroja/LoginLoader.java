package com.cruzroja.creuroja;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.cruzroja.creuroja.utils.ConnectionClient;
import com.cruzroja.creuroja.utils.Settings;

public class LoginLoader extends AsyncTaskLoader<Integer> {
	public static final int UNKNOWN_ERROR = -1;
	public static final int INVALID_CREDENTIALS = 1;
	public static final int USER_REGISTERED = 3;

	private String mUsername;
	private String mPassword;
	private boolean isStarted = false;

	public LoginLoader(Context context, Bundle args) {
		super(context);
		mUsername = args.getString(Settings.USERNAME);
		mPassword = args.getString(Settings.PASSWORD);
	}

	@Override
	protected void onStartLoading() {
		if (!isStarted) {
			isStarted = true;
			forceLoad();
		}
		super.onStartLoading();
	}

	@Override
	public Integer loadInBackground() {
		return ConnectionClient.doLogin(mUsername, mPassword);
	}

}
