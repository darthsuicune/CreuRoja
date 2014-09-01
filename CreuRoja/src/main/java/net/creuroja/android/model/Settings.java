package net.creuroja.android.model;

import android.content.ContentResolver;
import android.content.SharedPreferences;

import net.creuroja.android.model.db.CreuRojaContract;

/**
 * Created by denis on 14.06.14.
 */
public class Settings {
	public final static String VIEW_MODE = "view_mode";
	public final static String MAP_TYPE = "map_type";
	public static final String LAST_UPDATE_TIME = "last_update_time";
	public static final String LOCATIONS_INDEX_TYPE = "index_type";
	public static final String SHOW_CUAP = "show_cuap";
	public static final String SHOW_BRAVO = "show_bravo";
	public static final String SHOW_ASSEMBLY = "show_assembly";
	public static final String SHOW_ADAPTED = "show_adapted";
	public static final String SHOW_GAS_STATION = "show_gas_station";
	public static final String SHOW_HOSPITAL = "show_hospital";
	public static final String SHOW_SEA_SERVICE = "show_sea_service";
	public static final String SHOW_NOSTRUM = "show_nostrum";
	public static final String SHOW_SEA_BASE = "show_sea_base";
	public static final String SHOW_TERRESTRIAL = "show_terrestrial";
	/**
	 * Per the design guidelines, you should show the drawer on launch until the user manually
	 * expands it. This shared preference tracks this.
	 */
	public static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	public static void clean(SharedPreferences prefs, ContentResolver cr) {
		prefs.edit().clear().apply();
		cr.delete(CreuRojaContract.Locations.CONTENT_LOCATIONS, null, null);
		cr.delete(CreuRojaContract.Services.CONTENT_SERVICES, null, null);
		cr.delete(CreuRojaContract.Vehicles.CONTENT_VEHICLES, null, null);
		cr.delete(CreuRojaContract.Users.CONTENT_USERS, null, null);
	}
}
