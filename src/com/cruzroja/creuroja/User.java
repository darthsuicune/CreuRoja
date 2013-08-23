package com.cruzroja.creuroja;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.SharedPreferences;

import com.cruzroja.creuroja.utils.Settings;

public class User {
	public static final String ROLE_ADMIN = "administrador";
	public static final String ROLE_ACUATICO = "acuático";
	public static final String ROLE_SOCIAL = "social";
	public static final String ROLE_SOCORROS = "socorros";

	public String mName;
	public String mSessionName;
	public String mSessionId;
	public ArrayList<String> mRoles;

	public User() {
		mRoles = new ArrayList<String>();
	}

	public User(String name, ArrayList<String> roles) {
		mName = name;
		mRoles = roles;
	}

	public User(InputStream stream) throws IOException {
		mRoles = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains("session_name")) {
				mSessionName = extractValue(line);
			} else if (line.contains("sessid")) {
				mSessionId = extractValue(line);
			} else if (mName == null && line.contains("<name>name</name>")) {
				mName = extractValue(line);
			} else if (line.contains(ROLE_ADMIN)) {
				mRoles.add(ROLE_ADMIN);
			} else if (line.contains(ROLE_ACUATICO)) {
				mRoles.add(ROLE_ACUATICO);
			} else if (line.contains(ROLE_SOCIAL)) {
				mRoles.add(ROLE_SOCIAL);
			} else if (line.contains(ROLE_SOCORROS)) {
				mRoles.add(ROLE_SOCORROS);
			} else if (line.contains("<fault>")) {
				mName = "";
				reader.close();
				return;
			}
			// TODO: Insert all possible remaining cases here
		}
		reader.close();
	}

	/**
	 * <member><name>NAME</name><value><string>VALUE</string></value></member>
	 * 
	 * @param line
	 */

	private String extractValue(String line) {
		return line.substring(line.indexOf("<string>") + "<string>".length(),
				line.lastIndexOf("</string"));
	}
	
	public void save(SharedPreferences prefs) {
		prefs.edit().putBoolean(Settings.IS_VALID_USER, true)
		.putString(Settings.USERNAME, this.mName).commit();
	}

	public void save(SharedPreferences prefs, String password) {
		prefs.edit().putBoolean(Settings.IS_VALID_USER, true)
				.putString(Settings.USERNAME, this.mName)
				.putString(Settings.PASSWORD, Settings.getEncodedPassword(password)).commit();
	}

	public static User getSavedUser(SharedPreferences prefs) {
		if (prefs.contains(Settings.USERNAME)) {
			return new User();
		}
		return null;
	}
}
