package com.cruzroja.creuroja;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.view.View;

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

	public boolean canSeeMarker(int markerType) {
		if (mRoles.contains(ROLE_ADMIN)) {
			return true;
		}
		switch (markerType) {
		case R.drawable.adaptadas:
			return mRoles.contains(ROLE_SOCIAL);
		case R.drawable.asamblea:
			return mRoles.contains(ROLE_SOCIAL) || mRoles.contains(ROLE_SOCORROS);
		case R.drawable.bravo:
			return mRoles.contains(ROLE_SOCORROS);
		case R.drawable.cuap:
			return mRoles.contains(ROLE_SOCORROS);
		case R.drawable.hospital:
			return mRoles.contains(ROLE_SOCORROS);
		case R.drawable.maritimo:
			return mRoles.contains(ROLE_ACUATICO);
		case R.drawable.terrestre:
			return mRoles.contains(ROLE_SOCORROS);
		case R.drawable.nostrum:
			return mRoles.contains(ROLE_SOCORROS);
		default:
			return false;
		}
	}
	
	public int canSeeCheckBox(int markerType) {
		switch (markerType) {
		case R.drawable.adaptadas:
			return (mRoles.contains(ROLE_SOCIAL)) ? View.VISIBLE : View.GONE;
		case R.drawable.asamblea:
			return (mRoles.contains(ROLE_SOCIAL) || mRoles.contains(ROLE_SOCORROS)) ? View.VISIBLE : View.GONE;
		case R.drawable.bravo:
			return (mRoles.contains(ROLE_SOCORROS)) ? View.VISIBLE : View.GONE;
		case R.drawable.cuap:
			return (mRoles.contains(ROLE_SOCORROS)) ? View.VISIBLE : View.GONE;
		case R.drawable.hospital:
			return (mRoles.contains(ROLE_SOCORROS)) ? View.VISIBLE : View.GONE;
		case R.drawable.maritimo:
			return (mRoles.contains(ROLE_ACUATICO)) ? View.VISIBLE : View.GONE;
		case R.drawable.terrestre:
			return (mRoles.contains(ROLE_SOCORROS)) ? View.VISIBLE : View.GONE;
		case R.drawable.nostrum:
			return (mRoles.contains(ROLE_SOCORROS)) ? View.VISIBLE : View.GONE;
		default:
			return View.GONE;
		}
	}

	public void save(SharedPreferences prefs) {
		prefs.edit().putBoolean(Settings.IS_VALID_USER, true)
				.putString(Settings.USERNAME, this.mName)
				.putBoolean(ROLE_ADMIN, mRoles.contains(ROLE_ADMIN))
				.putBoolean(ROLE_ACUATICO, mRoles.contains(ROLE_ACUATICO))
				.putBoolean(ROLE_SOCIAL, mRoles.contains(ROLE_SOCIAL))
				.putBoolean(ROLE_SOCORROS, mRoles.contains(ROLE_SOCORROS)).commit();
	}

	public void save(SharedPreferences prefs, String password) {
		prefs.edit().putBoolean(Settings.IS_VALID_USER, true)
				.putString(Settings.USERNAME, this.mName)
				.putString(Settings.PASSWORD, Settings.getEncodedPassword(password))
				.putBoolean(ROLE_ADMIN, mRoles.contains(ROLE_ADMIN))
				.putBoolean(ROLE_ACUATICO, mRoles.contains(ROLE_ACUATICO))
				.putBoolean(ROLE_SOCIAL, mRoles.contains(ROLE_SOCIAL))
				.putBoolean(ROLE_SOCORROS, mRoles.contains(ROLE_SOCORROS)).commit();
	}

	public static User getSavedUser(SharedPreferences prefs) {
		if (prefs.contains(Settings.USERNAME)) {
			return new User(prefs.getString(Settings.USERNAME, ""), getRoles(prefs));
		}
		return null;
	}

	private static ArrayList<String> getRoles(SharedPreferences prefs) {
		ArrayList<String> roles = new ArrayList<String>();
		if (prefs.contains(ROLE_ADMIN)) {
			if (prefs.getBoolean(ROLE_ADMIN, false)) {
				roles.add(ROLE_ADMIN);
			}
		}
		if (prefs.contains(ROLE_ACUATICO)) {
			if (prefs.getBoolean(ROLE_ACUATICO, false)) {
				roles.add(ROLE_ACUATICO);
			}
		}
		if (prefs.contains(ROLE_SOCIAL)) {
			if (prefs.getBoolean(ROLE_SOCIAL, false)) {
				roles.add(ROLE_SOCIAL);
			}
		}
		if (prefs.contains(ROLE_SOCORROS)) {
			if (prefs.getBoolean(ROLE_SOCORROS, false)) {
				roles.add(ROLE_SOCORROS);
			}
		}
		return roles;
	}
}
