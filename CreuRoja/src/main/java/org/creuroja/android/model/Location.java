package org.creuroja.android.model;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import org.creuroja.android.model.db.CreuRojaContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 14.06.14.
 */
public class Location {

	public static List<Type> getAvailableTypes(Context context) {
		ArrayList<Type> result = new ArrayList<>();
		ContentResolver cr = context.getContentResolver();
		Uri uri = CreuRojaContract.Locations.CONTENT_LOCATIONS;
		String[] projection = { "DISTINCT " + CreuRojaContract.Locations.TYPE };
		String selection = null;
		String[] selectionArgs = null;

		cr.query(uri, projection, selection, selectionArgs, null);
		return result;
	}

	public enum Type {
		//TODO replace with actual values
		NONE(0, 0),
		ASSEMBLY (0, 0),
		BRAVO (0, 0),
		CUAP (0, 0);

		public int mLegendId;
		public int mNameString;

		Type (int legendId, int nameString) {
			mLegendId = legendId;
			mNameString = nameString;
		}

		public static Type getType(String s) {
			switch (s.toLowerCase()){
				case "assembly":
					return ASSEMBLY;
				case "bravo":
					return BRAVO;
				case "cuap":
					return CUAP;
				default:
					return NONE;
			}
		}
	}
}
