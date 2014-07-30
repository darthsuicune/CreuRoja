package net.creuroja.android.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.creuroja.android.database.CreuRojaContract;

/**
 * Created by lapuente on 29.10.13.
 */
public class Settings {
    public static final String ACCESS_TOKEN = "AccessToken";
    public static final String LAST_UPDATE_TIME = "LastUpdateTime";
    public static final String MAP_TYPE = "MapType";

    public static final String SHOW_ADAPTADAS = "adaptadas";
    public static final String SHOW_ASAMBLEA = "asamblea";
    public static final String SHOW_BRAVO = "bravo";
    public static final String SHOW_CUAP = "cuap";
    public static final String SHOW_HOSPITAL = "hospital";
    public static final String SHOW_MARITIMO = "maritimo";
    public static final String SHOW_NOSTRUM = "nostrum";
    public static final String SHOW_SOCIAL = "social";
    public static final String SHOW_TERRESTRE = "terrestre";

    public static void removeData(Context context) {
		ContentResolver cr = context.getContentResolver();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        cr.delete(CreuRojaContract.Locations.CONTENT_LOCATIONS, null, null);
        prefs.edit().clear().commit();
    }
}
