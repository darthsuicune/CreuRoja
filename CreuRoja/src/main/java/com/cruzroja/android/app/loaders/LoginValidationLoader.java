package com.cruzroja.android.app.loaders;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.cruzroja.android.R;
import com.cruzroja.android.app.AccessResponse;
import com.cruzroja.android.app.Settings;
import com.cruzroja.android.app.utils.ConnectionClient;

import java.io.IOException;

/**
 * Created by lapuente on 07.04.14.
 */
public class LoginValidationLoader extends AsyncTaskLoader<AccessResponse> {
    AccessResponse response = null;

    public LoginValidationLoader(Context context){
        super(context);
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (response == null) {
            forceLoad();
        }
    }

    @Override
    public AccessResponse loadInBackground() {
        try {
            response = new ConnectionClient().validateLogin(
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getString(Settings.ACCESS_TOKEN, ""));
        } catch (IOException e) {
            Log.e("LoginValidation", getContext().getString(R.string.error_invalid_response));
        }

        return response;
    }
}
