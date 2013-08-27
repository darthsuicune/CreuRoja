package com.cruzroja.creuroja.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.content.Context;

import com.cruzroja.creuroja.User;

/**
 * Usage: 
 * 		new ReportTool().report(getActivity(), mUser, e.printStackTrace());
 *
 */
public class ReportTool {
	public static final String URL_REPORT = ConnectionClient.BASE_URL + "android/node/create";

	private static final String PETITION_PART_1 = "<?xml version='1.0' ?>"
		    + "<methodCall>\n"
			+ "  <methodName>node.create</methodName>\n"
		    + "  <params>\n"
			+ "    <param>\n"
		    + "      <value>\n"
			+ "        <struct>\n"
		    + "          <member>\n"
		    + "            <name>type</name>\n"
			+ "            <value>\n"
		    + "              <string>bug_report</string>\n"
			+ "            </value>\n"
		    + "          </member>\n"
		    + "          <member>\n"
			+ "            <name>body</name>\n"
		    + "              <value>\n"
			+ "                <struct>\n"
		    + "                  <member>\n"
			+ "                    <name>und</name>\n"
		    + "                    <value>\n"
			+ "                      <array>\n"
		    + "                        <data>\n"
			+ "                          <value>\n"
		    + "                            <struct>\n"
			+ "                              <member>\n"
			+ "                                <name>value</name>\n"
			+ "                                <value>\n"
			+ "                                  <string>";
	private static final String PETITION_PART_2 = "</string>\n"
		    + "                                </value>\n"
		    + "                              </member>\n"
			+ "                            </struct>\n"
		    + "                          </value>\n"
		    + "                        </data>\n"
		    + "                      </array>\n"
		    + "                    </value>\n"
		    + "                  </member>\n"
			+ "                </struct>\n"
		    + "              </value>\n"
		    + "            </member>\n"
		    + "            <member>\n"
		    + "              <name>title</name>\n"
			+ "              <value>\n"
		    + "                <string>";
	private static final String PETITION_PART_3 = "</string>\n"
		    + "              </value>\n"
		    + "            </member>\n"
			+ "          </struct>\n"
		    + "        </value>\n"
		    + "      </param>\n"
		    + "  </params>\n"
		    + "</methodCall>\n";

	public void report(Context context, User user, String stackTrace) {
		if (ConnectionClient.isConnected(context)) {
			new Reporter(context, user, stackTrace).start();
		}
	}

	public class Reporter extends Thread {
		Context mContext;
		User mUser;
		String mStackTrace;

		public Reporter(Context context, User user, String stackTrace) {
			mContext = context;
			mUser = user;
			mStackTrace = stackTrace;
		}

		private String createText(String stackTrace) {
			return "This is a test: "
		    + stackTrace;
		}

		private String createTitle(User user) {
			return "Bug Report # -1 by "
		    + user.mName;
		}

		@Override
		public void run() {

			HttpPost request = new HttpPost(URL_REPORT);
			DefaultHttpClient httpClient = new DefaultHttpClient();

			try {
				request.setEntity(new StringEntity(PETITION_PART_1 + createText(mStackTrace)
						+ PETITION_PART_2 + createTitle(mUser) + PETITION_PART_3));
				request.setHeader(new BasicHeader(ConnectionClient.CONTENT_TYPE,
						ConnectionClient.CONTENT_TYPE_XML));
				httpClient.execute(request);
			} catch (UnsupportedEncodingException e) {
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
		}
	}
}