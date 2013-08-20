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
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains("session_name")) {
				extract_session_name(line);
			} else if (line.contains("user_name")) {
				extract_session_id(line);
			} else if (line.contains("user_name")) {
				extract_user_name(line);
			} else if (line.contains(USER_ROLE_REGISTERED)) {
				mRoles.add(USER_ROLE_REGISTERED);
			} else if (line.contains(USER_ROLE_ADMIN)) {
				mRoles.add(USER_ROLE_ADMIN);
			} else if (line.contains(USER_ROLE_ACUATICO)) {
				mRoles.add(USER_ROLE_ACUATICO);
			} else if (line.contains("<fault>")) {
				mName = "";
				reader.close();
				return;
			}
			// TODO: Insert all possible remaining cases here
		}
		reader.close();
	}

	private void extract_session_name(String line) {
		mSessionName = line;
	}
	
	private void extract_session_id(String line) {
		mSessionId = line;
	}

	private void extract_user_name(String line) {
		mName = line;
	}

}
