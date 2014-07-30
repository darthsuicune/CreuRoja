package net.creuroja.android.app;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.creuroja.android.app.utils.PHPConnectionClient;
import net.creuroja.android.database.CreuRojaContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lapuente on 29.10.13.
 */
public class Location {
	public static final String sLocations = "locations";
	public static final String sLatitude = "latitude";
	public static final String sLongitude = "longitude";
	public static final String sName = "name";
	public static final String sType = "location_type";
	public static final String sAddress = "address";
	public static final String sActive= "active";
	public static final String sDescription = "description";
	public static final String sLastUpdateTime = "updated_at";
	
	public final double mLatitude;
	public final double mLongitude;
	public final String mName;
	public final Type mType;
	public final String mAddress;
	public final String mDescription;
	public final String mLastModified;
	public final boolean mActive;
	public Marker mMarker;

	public Location(double latitude, double longitude, String name, String type, String address,
					String details, String lastModified, boolean active) {
		mLatitude = latitude;
		mLongitude = longitude;
		mName = name;
		mType = Type.getType(type);
		mAddress = address;
		mDescription = details;
		mLastModified = lastModified;
		mActive = active;
	}

	public Location(JSONObject object) throws JSONException {
		mLatitude = object.getDouble(sLatitude);
		mLongitude = object.getDouble(sLongitude);
		mName = object.getString(sName);
		mType = Type.getType(object.getString(sType));
		mAddress = object.getString(sAddress);
		if (object.isNull(sDescription) ||
			object.getString(sDescription) == "null") {
			mDescription = "";
		} else {
			mDescription = object.getString(sDescription);
		}

		mLastModified = object.getString(sLastUpdateTime);
		mActive = (object.has(sActive)) ?
				(object.getInt(sActive) == 1) : true;
	}

	public Location(Cursor cursor) {
		mLatitude = cursor.getDouble(cursor.getColumnIndex(CreuRojaContract.Locations.LATITUD));
		mLongitude = cursor.getDouble(cursor.getColumnIndex(CreuRojaContract.Locations.LONGITUD));
		mName = cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.NAME));
		mType = Type.getType(
				cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.ICON)));
		mAddress = cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.ADDRESS));
		mDescription = cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.DETAILS));
		mLastModified =
				cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.LAST_MODIFIED));
		mActive = (cursor.getInt(cursor.getColumnIndex(CreuRojaContract.Locations.ACTIVE)) == 1);
	}

	public boolean newerThan(String lastUpdate) {
		if (TextUtils.isEmpty(lastUpdate)) {
			return true;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date updatedAt = format.parse(mLastModified);
			Date saved = format.parse(lastUpdate);
			return updatedAt.after(saved);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean equals(Object location) {
		Location loc = (Location) location;
		return ((Double.compare(loc.mLatitude, this.mLatitude) == 0) &&
				(Double.compare(loc.mLongitude, this.mLongitude) == 0));
	}

	public LatLng getPosition() {
		return new LatLng(mLatitude, mLongitude);
	}

	public List<LatLng> getDirections(double latitudeStart, double longitudeStart)
			throws IOException {
		return new PHPConnectionClient().getDirections(latitudeStart, longitudeStart, this);
	}

	public MarkerOptions getMarker() {
		MarkerOptions marker = new MarkerOptions().position(getPosition());
		if (mType.mIcon != 0) {
			marker.icon(BitmapDescriptorFactory.fromResource(mType.mIcon));
		}

		return marker;
	}

	public boolean shouldBeShown(String filter, SharedPreferences prefs) {
		if (!matchesFilter(filter)) {
			return false;
		}
		switch (mType) {
			case ADAPTADAS:
				return prefs.getBoolean(Settings.SHOW_ADAPTADAS, true);
			case ASAMBLEA:
				return prefs.getBoolean(Settings.SHOW_ASAMBLEA, true);
			case BRAVO:
				return prefs.getBoolean(Settings.SHOW_BRAVO, true);
			case CUAP:
				return prefs.getBoolean(Settings.SHOW_CUAP, true);
			case HOSPITAL:
				return prefs.getBoolean(Settings.SHOW_HOSPITAL, true);
			case MARITIMO:
				return prefs.getBoolean(Settings.SHOW_MARITIMO, true);
			case NOSTRUM:
				return prefs.getBoolean(Settings.SHOW_NOSTRUM, true);
			case SOCIAL:
				return prefs.getBoolean(Settings.SHOW_SOCIAL, true);
			case TERRESTRE:
				return prefs.getBoolean(Settings.SHOW_TERRESTRE, true);
			default:
				return true;
		}
	}

	private boolean matchesFilter(String filter) {
		if (TextUtils.isEmpty(filter)) {
			return true;
		} else {
			filter = dehyphenize(filter);
			String address = null;
			if (mAddress != null) {
				address = dehyphenize(mAddress);
			}
			return (dehyphenize(mName).contains(filter) ||
					((address != null) && (address.contains(filter))));
		}
	}

	@SuppressLint("DefaultLocale")
	private String dehyphenize(String input) {
		if (TextUtils.isEmpty(input)) {
			return "";
		}
		input = input.toLowerCase();
		return input.replace("à", "a").replace("á", "a").replace("é", "e").replace("è", "e")
				.replace("í", "i").replace("ì", "i").replace("ó", "o").replace("ò", "o")
				.replace("ú", "u").replace("ù", "u");
	}

	public enum Type {
		NONE(net.creuroja.android.R.string.no_marker_type, 0),
		ADAPTADAS(net.creuroja.android.R.string.marker_type_adaptadas, net.creuroja.android.R.drawable.adaptadas),
		ASAMBLEA(net.creuroja.android.R.string.marker_type_asamblea, net.creuroja.android.R.drawable.asamblea),
		BRAVO(net.creuroja.android.R.string.marker_type_bravo, net.creuroja.android.R.drawable.bravo),
		CUAP(net.creuroja.android.R.string.marker_type_cuap, net.creuroja.android.R.drawable.cuap),
		HOSPITAL(net.creuroja.android.R.string.marker_type_hospital, net.creuroja.android.R.drawable.hospital),
		MARITIMO(net.creuroja.android.R.string.marker_type_maritimo, net.creuroja.android.R.drawable.maritimo),
		NOSTRUM(net.creuroja.android.R.string.marker_type_nostrum, net.creuroja.android.R.drawable.nostrum),
		SOCIAL(net.creuroja.android.R.string.marker_type_social, net.creuroja.android.R.drawable.social),
		TERRESTRE(net.creuroja.android.R.string.marker_type_terrestre, net.creuroja.android.R.drawable.terrestre);
		public final int mIcon;
		public final int mMarkerType;

		Type(int markerType, int icon) {
			mMarkerType = markerType;
			mIcon = icon;
		}

		public static Type getType(String type) {
			if (type.equals(Settings.SHOW_ADAPTADAS)) {
				return ADAPTADAS;
			} else if (type.equals(Settings.SHOW_ASAMBLEA)) {
				return ASAMBLEA;
			} else if (type.equals(Settings.SHOW_BRAVO)) {
				return BRAVO;
			} else if (type.equals(Settings.SHOW_CUAP)) {
				return CUAP;
			} else if (type.equals(Settings.SHOW_HOSPITAL)) {
				return HOSPITAL;
			} else if (type.equals(Settings.SHOW_MARITIMO)) {
				return MARITIMO;
			} else if (type.equals(Settings.SHOW_NOSTRUM)) {
				return NOSTRUM;
			} else if (type.equals(Settings.SHOW_SOCIAL)) {
				return SOCIAL;
			} else if (type.equals(Settings.SHOW_TERRESTRE)) {
				return TERRESTRE;
			} else {
				return NONE;
			}
		}

		public static List<Type> getAvailableTypes(ContentResolver cr) {
			ArrayList<Type> typeList = new ArrayList<>();
			String[] projection = {"DISTINCT " + CreuRojaContract.Locations.ICON};
			Cursor cursor =
					cr.query(CreuRojaContract.Locations.CONTENT_LOCATIONS, projection, null, null,
							null);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					typeList.add(getType(cursor
							.getString(cursor.getColumnIndex(CreuRojaContract.Locations.ICON))));
				} while (cursor.moveToNext());
				cursor.close();
			}
			return typeList;
		}
	}
}
