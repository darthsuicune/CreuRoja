package com.cruzroja.android.app.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.cruzroja.android.R;
import com.cruzroja.android.app.LoginResponse;
import com.cruzroja.android.app.utils.ConnectionClient;

import java.io.IOException;

/**
 * Created by lapuente on 27.11.13.
 */
public class LoginLoader extends AsyncTaskLoader<LoginResponse> {
    public static final String EMAIL = "e-mail";
    public static final String PASSWORD = "Password";

    private ConnectionClient mClient;
    private String mEmail, mPassword;

    public LoginLoader(Context context, Bundle args) {
        super(context);
        mEmail = args.getString(EMAIL);
        mPassword = args.getString(PASSWORD);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mClient == null){
            forceLoad();
        }
    }

    @Override
    public LoginResponse loadInBackground() {
        mClient = new ConnectionClient();
        LoginResponse response = null;
        try{
            response = mClient.doLogin(mEmail, mPassword);
        } catch(IOException e){
            response = new LoginResponse(R.string.error_connecting);
        }
        return response;
    }
}
