package com.cruzroja.android.app.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.cruzroja.android.R;
import com.cruzroja.android.app.LoginResponse;
import com.cruzroja.android.app.Settings;
import com.cruzroja.android.app.utils.ConnectionClient;
import com.cruzroja.android.app.utils.HashGenerator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lapuente on 27.11.13.
 */
public class LoginLoader extends AsyncTaskLoader<LoginResponse> {
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";

    private ConnectionClient mClient;
    private String mUsername, mPassword;

    public LoginLoader(Context context, Bundle args) {
        super(context);
        mUsername = args.getString(USERNAME);
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
            response = mClient.doLogin(mUsername, mPassword);
        } catch(IOException e){
            response = new LoginResponse(R.string.error_connecting);
        }
        return response;
    }
}
