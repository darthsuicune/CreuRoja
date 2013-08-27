package com.cruzroja.creuroja.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class Settings {
	public static final String IS_VALID_USER = "isValidUser";
	public static final String LOG = "CreuRoja log";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	public static final String SHOW_ADAPTADAS = "showAdaptadas";
	public static final String SHOW_ASAMBLEA = "showAsamblea";
	public static final String SHOW_BRAVO = "showBravo";
	public static final String SHOW_CUAP = "showCuap";
	public static final String SHOW_HOSPITAL = "showHospital";
	public static final String SHOW_MARITIMO = "showMaritimo";
	public static final String SHOW_NOSTRUM = "showNostrum";
	public static final String SHOW_TERRESTRE = "showTerrestre";

	public static final String MAP_STYLE = "mapStyle";
	public static final int MAP_STYLE_NORMAL = 0;
	public static final int MAP_STYLE_HYBRID = 1;
	public static final int MAP_STYLE_TERRAIN = 2;
	public static final int MAP_STYLE_SATELLITE = 3;

	public static final String LOGIN_UNKNOWN_ERROR = "loginError";
	public static final String INVALID_CREDENTIALS = "invalidCredentials";

	public static void clean(Context context) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
	}

	public static String getEncodedPassword(String password) {
		return password;
	}

	public static String getDecodedPassword(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(PASSWORD, "");
	}

}
