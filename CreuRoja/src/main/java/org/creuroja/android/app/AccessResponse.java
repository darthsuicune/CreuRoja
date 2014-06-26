package org.creuroja.android.app;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lapuente on 07.04.14.
 */
public class AccessResponse {
    public static final String IS_VALID_ARG = "isValid";
    public boolean isValid = true;

    public AccessResponse(HttpResponse response) throws IOException {
        String result = readResponse(response);
        try{
            JSONObject jsonResponse = new JSONObject(result);
            this.isValid = jsonResponse.getBoolean(IS_VALID_ARG);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    private String readResponse(HttpResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent()));
        String line;
        while((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
