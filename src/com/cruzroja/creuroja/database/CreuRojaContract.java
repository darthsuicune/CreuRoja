package com.cruzroja.creuroja.database;

import android.provider.BaseColumns;

public final class CreuRojaContract {

	public static class Locations implements BaseColumns {
		public static final String TABLE_NAME = "locations";
		public static final String DEFAULT_ORDER = _ID + " DESC";

		public static final String LATITUD = "latitud";
		public static final String LONGITUD = "longitud";
		public static final String ICON = "icon";
		public static final String NAME = "name";
		public static final String ADDRESS = "address";
		public static final String DETAILS = "details";
	}

}
