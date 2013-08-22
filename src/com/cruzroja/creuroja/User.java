package com.cruzroja.creuroja;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class User {
	public static final String USER_ROLE_ADMIN = "administrador";
	public static final String USER_ROLE_REGISTERED = "authenticated user";
	public static final String USER_ROLE_ACUATICO = "acuático";
	public static final String USER_ROLE_SOCIAL = "social";
	public static final String USER_ROLE_SOCORROS = "socorros";

	public String mName;
	public String mSessionName;
	public String mSessionId;
	public ArrayList<String> mRoles;

	public User() {
		mRoles = new ArrayList<String>();
	}

	public User(String name, String sessionName, ArrayList<String> roles) {
		mName = name;
		mSessionName = sessionName;
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
			} else if (line.contains(USER_ROLE_REGISTERED)) {
				mRoles.add(USER_ROLE_REGISTERED);
			} else if (line.contains(USER_ROLE_ADMIN)) {
				mRoles.add(USER_ROLE_ADMIN);
			} else if (line.contains(USER_ROLE_ACUATICO)) {
				mRoles.add(USER_ROLE_ACUATICO);
			} else if (line.contains(USER_ROLE_SOCIAL)) {
				mRoles.add(USER_ROLE_SOCIAL);
			} else if (line.contains(USER_ROLE_SOCORROS)) {
				mRoles.add(USER_ROLE_SOCORROS);
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

	public void save() {

	}
}
