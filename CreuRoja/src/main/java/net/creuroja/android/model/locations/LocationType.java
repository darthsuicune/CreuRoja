package net.creuroja.android.model.locations;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import net.creuroja.android.R;
import net.creuroja.android.model.Settings;
import net.creuroja.android.model.db.CreuRojaContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 08.08.14.
 */

public enum LocationType {
	//TODO replace with actual values
	NONE(0, 0),
	ADAPTED(R.id.navigation_legend_adaptadas, R.string.marker_type_adaptadas),
	ASSEMBLY(R.id.navigation_legend_asamblea, R.string.marker_type_asamblea),
	BRAVO(R.id.navigation_legend_bravo, R.string.marker_type_bravo),
	CUAP(R.id.navigation_legend_cuap, R.string.marker_type_cuap),
	GAS_STATION(R.id.navigation_legend_gasolinera, R.string.marker_type_gasolinera),
	HOSPITAL(R.id.navigation_legend_hospital, R.string.marker_type_hospital),
	SEA_SERVICE(R.id.navigation_legend_maritimo, R.string.marker_type_maritimo),
	NOSTRUM(R.id.navigation_legend_nostrum, R.string.marker_type_nostrum),
	SEA_BASE(R.id.navigation_legend_salvamento, R.string.marker_type_salvamento),
	TERRESTRIAL(R.id.navigation_legend_terrestre, R.string.marker_type_terrestre);

	public int mLegendViewId;
	public int mNameString;

	private static final String sAdapted = "adaptadas";
	private static final String sAssembly = "asamblea";
	private static final String sBravo = "bravo";
	private static final String sCuap = "cuap";
	private static final String sGasStation = "gasolinera";
	private static final String sHospital = "hospital";
	private static final String sSeaService = "maritimo";
	private static final String sNostrum = "nostrum";
	private static final String sSeaBase = "salvamento";
	private static final String sTerrestrial = "terrestre";

	LocationType(int legendId, int nameString) {
		mLegendViewId = legendId;
		mNameString = nameString;
	}
	
	@Override public String toString() {
		switch (this) {
			case ADAPTED:
				return sAdapted;
			case ASSEMBLY:
				return sAssembly;
			case BRAVO:
				return sBravo;
			case CUAP:
				return sCuap;
			case GAS_STATION:
				return sGasStation;
			case HOSPITAL:
				return sHospital;
			case SEA_SERVICE:
				return sSeaService;
			case NOSTRUM:
				return sNostrum;
			case SEA_BASE:
				return sSeaBase;
			case TERRESTRIAL:
				return sTerrestrial;
			default:
				return "";
		}
	}

	public static LocationType getType(String s) {
		switch (s.toLowerCase()) {
			case sAdapted:
				return ADAPTED;
			case sAssembly:
				return ASSEMBLY;
			case sBravo:
				return BRAVO;
			case sCuap:
				return CUAP;
			case sGasStation:
				return GAS_STATION;
			case sHospital:
				return HOSPITAL;
			case sSeaService:
				return SEA_SERVICE;
			case sNostrum:
				return NOSTRUM;
			case sSeaBase:
				return SEA_BASE;
			case sTerrestrial:
				return TERRESTRIAL;
			default:
				return NONE;
		}
	}

	public static List<LocationType> getCurrentItems(ContentResolver cr) {
		Uri uri = CreuRojaContract.Locations.CONTENT_DISTINCT_LOCATIONS;
		String[] projection = {
				CreuRojaContract.Locations._ID,
				CreuRojaContract.Locations.TYPE
		};

		Cursor cursor = cr.query(uri, projection, null, null, null);

		List<LocationType> currentTypes = new ArrayList<>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				LocationType type = getType(cursor
						.getString(cursor.getColumnIndex(CreuRojaContract.Locations.TYPE)));
				if (!currentTypes.contains(type)) {
					currentTypes.add(type);
				}
			} while (cursor.moveToNext());
		}
		return currentTypes;
	}

	public boolean getViewable(SharedPreferences prefs) {
		switch (this) {
			case ADAPTED:
				return prefs.getBoolean(Settings.SHOW_ADAPTED, true);
			case ASSEMBLY:
				return prefs.getBoolean(Settings.SHOW_ASSEMBLY, true);
			case BRAVO:
				return prefs.getBoolean(Settings.SHOW_BRAVO, true);
			case CUAP:
				return prefs.getBoolean(Settings.SHOW_CUAP, true);
			case GAS_STATION:
				return prefs.getBoolean(Settings.SHOW_GAS_STATION, true);
			case HOSPITAL:
				return prefs.getBoolean(Settings.SHOW_HOSPITAL, true);
			case SEA_SERVICE:
				return prefs.getBoolean(Settings.SHOW_SEA_SERVICE, true);
			case NOSTRUM:
				return prefs.getBoolean(Settings.SHOW_NOSTRUM, true);
			case SEA_BASE:
				return prefs.getBoolean(Settings.SHOW_SEA_BASE, true);
			case TERRESTRIAL:
				return prefs.getBoolean(Settings.SHOW_TERRESTRIAL, true);
			default:
				return true;
		}
	}
}