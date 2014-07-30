package org.creuroja.android.app;

import org.apache.http.HttpResponse;
import org.creuroja.android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by lapuente on 03.12.13.
 */
public abstract class LoginResponse {
    public List<Location> mLocationList;
    public int mErrorMessage;

    public LoginResponse(int errorMessage) {
        mErrorMessage = errorMessage;
    }

    public LoginResponse(HttpResponse response) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            parseResponse(reader);
        } catch (IOException e) {
            mLocationList = null;
            mErrorMessage = R.string.error_connecting;
        }
    }

    public abstract void parseResponse(BufferedReader reader) throws IOException;

    public abstract boolean isValid();
}
