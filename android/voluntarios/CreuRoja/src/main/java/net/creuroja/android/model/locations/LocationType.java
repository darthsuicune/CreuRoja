package net.creuroja.android.model.locations;

import android.content.SharedPreferences;
import android.database.Cursor;

import net.creuroja.android.R;
import net.creuroja.android.model.Settings;
import net.creuroja.android.model.db.CreuRojaContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lapuente on 08.08.14.
 */

public enum LocationType {
	NONE(0, 0, 0, ""),
	ADAPTED(R.id.navigation_legend_adaptadas, R.string.marker_type_adaptadas, R.drawable.adaptadas,
			Settings.SHOW_ADAPTED),
	ASSEMBLY(R.id.navigation_legend_asamblea, R.string.marker_type_asamblea, R.drawable.asamblea,
			Settings.SHOW_ASSEMBLY),
	BRAVO(R.id.navigation_legend_bravo, R.string.marker_type_bravo, R.drawable.bravo,
			Settings.SHOW_BRAVO),
	CUAP(R.id.navigation_legend_cuap, R.string.marker_type_cuap, R.drawable.cuap,
			Settings.SHOW_CUAP),
	GAS_STATION(R.id.navigation_legend_gasolinera, R.string.marker_type_gasolinera,
			R.drawable.gasolinera, Settings.SHOW_GAS_STATION),
	HOSPITAL(R.id.navigation_legend_hospital, R.string.marker_type_hospital, R.drawable.hospital,
			Settings.SHOW_HOSPITAL),
	SEA_SERVICE(R.id.navigation_legend_maritimo, R.string.marker_type_maritimo, R.drawable.maritimo,
			Settings.SHOW_SEA_SERVICE),
	NOSTRUM(R.id.navigation_legend_nostrum, R.string.marker_type_nostrum, R.drawable.nostrum,
			Settings.SHOW_NOSTRUM),
	SEA_BASE(R.id.navigation_legend_salvamento, R.string.marker_type_salvamento,
			R.drawable.salvamento, Settings.SHOW_SEA_BASE),
	TERRESTRIAL(R.id.navigation_legend_terrestre, R.string.marker_type_terrestre,
			R.drawable.terrestre, Settings.SHOW_TERRESTRIAL);

	public int mLegendViewId;
	public int mNameString;
	public int mIcon;
	public String mPrefs;

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

	LocationType(int legendId, int nameString, int icon, String prefs) {
		mLegendViewId = legendId;
		mNameString = nameString;
		mIcon = icon;
		mPrefs = prefs;
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
				return "N/A";
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

	public static List<LocationType> getCurrentTypes(Cursor cursor) {
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
		return prefs.getBoolean(mPrefs, true);
	}
}