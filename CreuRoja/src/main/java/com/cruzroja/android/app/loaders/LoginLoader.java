package com.cruzroja.android.app.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.cruzroja.android.app.LoginResponse;
import com.cruzroja.android.app.Settings;
import com.cruzroja.android.app.utils.ConnectionClient;

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
        try{
            mPassword = Settings.getShaHash(args.getString(PASSWORD));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        return mClient.doLogin(mUsername, mPassword);
    }
}
