package net.creuroja.android.model.locations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import net.creuroja.android.model.db.CreuRojaContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by denis on 14.06.14.
 */
public class Location {
	public static final String sRemoteId = "id";
	public static final String sName = "name";
	public static final String sDescription = "description";
	public static final String sPhone = "phone";
	public static final String sAddress = "address";
	public static final String sLatitude = "latitude";
	public static final String sLongitude = "longitude";
	public static final String sLocationType = "location_type";
	public static final String sUpdatedAt = "updated_at";
	public static final String sActive = "active";

	public final int mRemoteId;
	public final String mName;
	public final String mDescription;
	public final String mPhone;
	public final String mAddress;
	public final double mLatitude;
	public final double mLongitude;
	public final LocationType mType;
	public final String mUpdatedAt;
	public final boolean mActive;

	public Location(JSONObject json) throws JSONException {
		mRemoteId = json.getInt(sRemoteId);
		mName = json.getString(sName);
		mDescription = (json.has(sDescription)) ? json.getString(sDescription) : "";
		mPhone = (json.has(sPhone) ? json.getString(sPhone) : "");
		mAddress = (json.has(sAddress)) ? json.getString(sAddress) : "";
		mLatitude = json.getDouble(sLatitude);
		mLongitude = json.getDouble(sLongitude);
		mType = LocationType.getType(json.getString(sLocationType));
		mUpdatedAt = json.getString(sUpdatedAt);
		mActive = json.getBoolean(sActive);
	}

	public Location(Cursor cursor) {
		mRemoteId = cursor.getInt(cursor.getColumnIndex(CreuRojaContract.Locations.REMOTE_ID));
		mName = cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.NAME));
		mDescription =
				cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.DESCRIPTION));
		mPhone = cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.PHONE));
		mAddress = cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.ADDRESS));
		mLatitude = cursor.getDouble(cursor.getColumnIndex(CreuRojaContract.Locations.LATITUD));
		mLongitude = cursor.getDouble(cursor.getColumnIndex(CreuRojaContract.Locations.LONGITUD));
		mType = LocationType
				.getType(cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.TYPE)));
		mUpdatedAt = cursor.getString(cursor.getColumnIndex(CreuRojaContract.Locations.UPDATED_AT));
		mActive = cursor.getInt(cursor.getColumnIndex(CreuRojaContract.Locations.UPDATED_AT)) == 1;
	}

	public ContentValues getAsValues() {
		ContentValues values = new ContentValues();
		values.put(CreuRojaContract.Locations.ACTIVE, (mActive) ? 1 : 0);
		values.put(CreuRojaContract.Locations.ADDRESS, mAddress);
		values.put(CreuRojaContract.Locations.DESCRIPTION, mDescription);
		values.put(CreuRojaContract.Locations.LATITUD, mLatitude);
		values.put(CreuRojaContract.Locations.LONGITUD, mLongitude);
		values.put(CreuRojaContract.Locations.NAME, mName);
		values.put(CreuRojaContract.Locations.PHONE, mPhone);
		values.put(CreuRojaContract.Locations.REMOTE_ID, mRemoteId);
		values.put(CreuRojaContract.Locations.TYPE, mType.toString());
		values.put(CreuRojaContract.Locations.UPDATED_AT, mUpdatedAt);
		return values;
	}

	public void update(ContentResolver cr) {
		Uri uri = CreuRojaContract.Locations.CONTENT_LOCATIONS;
		String where = CreuRojaContract.Locations.REMOTE_ID + "=?";
		String[] selectionArgs = {Integer.toString(mRemoteId)};
		cr.update(uri, this.getAsValues(), where, selectionArgs);
	}

	public void delete(ContentResolver cr) {
		Uri uri = CreuRojaContract.Locations.CONTENT_LOCATIONS;
		String where = CreuRojaContract.Locations.REMOTE_ID + "=?";
		String[] selectionArgs = {Integer.toString(mRemoteId)};
		cr.delete(uri, where, selectionArgs);
	}

	public boolean newerThan(String lastUpdate) {
		if (TextUtils.isEmpty(lastUpdate)) {
			return true;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
			Date updatedAt = format.parse(mUpdatedAt);
			Date saved = format.parse(lastUpdate);
			return updatedAt.after(saved);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isVisible(SharedPreferences prefs) {
		return mType.getViewable(prefs);
	}
}
