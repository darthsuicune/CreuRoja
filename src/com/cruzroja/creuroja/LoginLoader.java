package com.cruzroja.creuroja;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * PETITION FORMAT:
 * <\?xml version="1.0"\?>
 * <methodCall>
 * <methodName>user.login</methodName>
 * <params>
 * <param>
 * <value><string>username</string></value>
 * </param>
 * <param>
 * <value><string>password</string></value>
 * </param>
 * </params>
 * </methodCall>
 * <p/>
 * Petition requirements:
 * -Send via POST
 * -Set content-type header to "text/xml" or "application/xml"
 */
public class LoginLoader extends AsyncTaskLoader<String> {
    //Args for the loader
    public static final String ARG_USERNAME = "username";
    public static final String ARG_PASSWORD = "password";
    //URL for the login
    public static final String LOGIN_URL = "http://r0uzic.net/voluntarios.cr/user/login?q=android";
    //XML tags
    public static final String TAG_HEADER = "<?xml version=\"1.0\"?>";
    public static final String TAG_METHOD_CALL = "<methodCall>";
    public static final String TAG_METHOD_CALL_CLOSE = "</methodCall>";
    public static final String TAG_METHOD_NAME = "<methodName>";
    public static final String TAG_METHOD_NAME_CLOSE = "</methodName>";
    public static final String TAG_PARAMS = "<params>";
    public static final String TAG_PARAMS_CLOSE = "</params>";
    public static final String TAG_PARAM = "<param>";
    public static final String TAG_PARAM_CLOSE = "</param>";
    public static final String TAG_VALUE = "<value>";
    public static final String TAG_VALUE_CLOSE = "</value>";
    public static final String TAG_STRING = "<string>";
    public static final String TAG_STRING_CLOSE = "</string>";
    public static final String METHOD_NAME = "user.login";

    //XML preconstructed strings
    public static final String XML_PETITION_1 = "<?xml version=\"1.0\"?>\n" +
            "<methodCall>\n" +
            "   <methodName>user.login</methodName>\n" +
            "   <params>\n" +
            "     <param>\n" +
            "        <value><string>";
    public static final String XML_PETITION_2 = "</string></value>\n" +
            "     </param>\n" +
            "    <param>\n" +
            "        <value><string>";
    public static final String XML_PETITION_3 = "</string></value>\n" +
            "     </param>\n" +
            "   </params>\n" +
            "</methodCall>";
    public static final String CONTENT_TYPE = "content-type";
    public static final String CONTENT_TYPE_XML = "text/xml";

    //Responses for errors during the loader process
    public static final String RESPONSE_WRONG_ID = "wrong id";
    public static final String RESPONSE_401 = "401";
    public static final String RESPONSE_406 = "406";
    public static final String RESPONSE_NO_ID = "no id";
    public static final String RESPONSE_IO_EXCEPTION = "io exc";
    public static final String RESPONSE_PROTOCOL_EXCEPTION = "prot exc";

    //Internal variables
    private DefaultHttpClient httpClient = null;
    private String username, password;

    public LoginLoader(Context context, Bundle args) {
        super(context);
        username = args.getString(ARG_USERNAME);
        password = args.getString(ARG_PASSWORD);
    }

    @Override
    protected void onStartLoading() {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }
        if (httpClient == null) {
            forceLoad();
        }
        super.onStartLoading();
    }

    @Override
    public String loadInBackground() {
        createHttpClient();
        try {
            return parseResponse(httpClient.execute(createRequest()));
        } catch (IOException e) {
            return RESPONSE_IO_EXCEPTION;
        } catch (NullPointerException e) {
            return RESPONSE_NO_ID;
        }
    }

    private void createHttpClient() {
        HttpParams params = new BasicHttpParams();
        httpClient = new DefaultHttpClient(params);
    }

    private HttpPost createRequest() {
        HttpPost request = new HttpPost(LOGIN_URL);
        request.setHeader(new BasicHeader(CONTENT_TYPE, CONTENT_TYPE_XML));
        try {
            request.setEntity(new StringEntity(buildLogin()));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return request;
    }

    private String buildLogin() {
        return XML_PETITION_1 + username + XML_PETITION_2 + password + XML_PETITION_3;
    }

    private String parseResponse(HttpResponse response) {
        try {
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (isCorrectLogin(line)) {
                        return line;
                    } else {
                        return RESPONSE_WRONG_ID;
                    }
                }

            } else if (response.getStatusLine().getStatusCode() == 401) {
                return RESPONSE_401;
            } else if (response.getStatusLine().getStatusCode() == 406) {
                return RESPONSE_406;
            }
        } catch (ClientProtocolException e) {
            return RESPONSE_PROTOCOL_EXCEPTION;
        } catch (IOException e) {
            return RESPONSE_IO_EXCEPTION;
        }
        return RESPONSE_NO_ID;
    }

    private boolean isCorrectLogin(String s) {
        return s.contains("session_name");
    }
}
