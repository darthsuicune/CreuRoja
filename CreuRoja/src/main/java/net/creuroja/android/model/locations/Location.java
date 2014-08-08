package net.creuroja.android.model.locations;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import net.creuroja.android.model.db.CreuRojaContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 14.06.14.
 */
public class Location {
	public int mId;

	public Location(int id) {
		mId = id;
	}

	public static List<LocationType> getAvailableTypes(Context context) {
		ArrayList<LocationType> result = new ArrayList<>();
		ContentResolver cr = context.getContentResolver();
		Uri uri = CreuRojaContract.Locations.CONTENT_LOCATIONS;
		String[] projection = {"DISTINCT " + CreuRojaContract.Locations.TYPE};
		String selection = null;
		String[] selectionArgs = null;

		cr.query(uri, projection, selection, selectionArgs, null);
		return result;
	}


}
