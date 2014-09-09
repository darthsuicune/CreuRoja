package net.creuroja.android.vehicletracking.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lapuente on 04.09.14.
 */
public class Settings {
	public static final String INTERVAL = "interval";
	public static final long DEFAULT_INTERVAL = 5000;
	public static final String ACCESS_TOKEN = "access token";
	public static final String DEFAULT_VEHICLE = "default_vehicle";

	public static boolean isConnected(Context context) {
		ConnectivityManager connectivity =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		return (info != null && info.isConnected() && info.isAvailable());
	}
}
