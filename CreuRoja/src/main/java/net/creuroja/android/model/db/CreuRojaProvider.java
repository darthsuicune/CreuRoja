package net.creuroja.android.model.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CreuRojaProvider extends ContentProvider {
	public static final String CONTENT_NAME = "net.creuroja.android.model.db.Provider";
	private static final int LOCATIONS = 1;
	private static final int LOCATIONS_ID = 2;
	private static final int SERVICES = 3;
	private static final int SERVICES_ID = 4;
	private static final int VEHICLES = 5;
	private static final int VEHICLES_ID = 6;
	private static final int USERS = 7;
	private static final int USERS_ID = 8;
	private static final int DISTINCT_LOCATIONS = 9;
	static UriMatcher sUriMatcher;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Locations.TABLE_NAME, LOCATIONS);
		sUriMatcher
				.addURI(CONTENT_NAME, CreuRojaContract.Locations.TABLE_NAME + "/#", LOCATIONS_ID);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Services.TABLE_NAME, SERVICES);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Services.TABLE_NAME + "/#", SERVICES_ID);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Vehicles.TABLE_NAME, VEHICLES);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Vehicles.TABLE_NAME + "/#", VEHICLES_ID);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Users.TABLE_NAME, USERS);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Users.TABLE_NAME + "/#", USERS_ID);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Locations.DISTINCT_LOCATIONS,
				DISTINCT_LOCATIONS);
	}

	CreuRojaOpenHelper mDbHelper;

	@Override
	public boolean onCreate() {
		mDbHelper = new CreuRojaOpenHelper(getContext());
		return mDbHelper != null;
	}

	@Override
	public String getType(Uri uri) {
		String dirBase = ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_NAME + ".";
		String itemBase = ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_NAME + ".";
		switch (sUriMatcher.match(uri)) {
			case LOCATIONS:
				return dirBase + CreuRojaContract.Locations.TABLE_NAME;
			case LOCATIONS_ID:
				return itemBase + CreuRojaContract.Locations.TABLE_NAME;
			case SERVICES:
				return dirBase + CreuRojaContract.Services.TABLE_NAME;
			case SERVICES_ID:
				return itemBase + CreuRojaContract.Services.TABLE_NAME;
			case VEHICLES:
				return dirBase + CreuRojaContract.Vehicles.TABLE_NAME;
			case VEHICLES_ID:
				return itemBase + CreuRojaContract.Vehicles.TABLE_NAME;
			case USERS:
				return dirBase + CreuRojaContract.Users.TABLE_NAME;
			case USERS_ID:
				return itemBase + CreuRojaContract.Users.TABLE_NAME;
			case DISTINCT_LOCATIONS:
				return dirBase + CreuRojaContract.Locations.DISTINCT_LOCATIONS;
			default:
				return null;
		}
	}

	private String getTable(Uri uri) {
		switch (sUriMatcher.match(uri)) {
			case DISTINCT_LOCATIONS:
			case LOCATIONS_ID:
			case LOCATIONS:
				return CreuRojaContract.Locations.TABLE_NAME;
			case SERVICES_ID:
			case SERVICES:
				return CreuRojaContract.Services.TABLE_NAME;
			case VEHICLES_ID:
			case VEHICLES:
				return CreuRojaContract.Vehicles.TABLE_NAME;
			case USERS_ID:
			case USERS:
				return CreuRojaContract.Users.TABLE_NAME;
			default:
				return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = getTable(uri);
		long id = mDbHelper.getWritableDatabase().insert(table, null, values);
		Uri result = null;
		if (id != -1) {
			result = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(uri, null);
			getContext().getContentResolver().notifyChange(result, null);
		}
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
						String sortOrder) {
		String table = getTable(uri);
		boolean distinct = (sUriMatcher.match(uri) == DISTINCT_LOCATIONS);
		String groupBy = null;
		String having = null;
		String limit = null;
		Cursor cursor = mDbHelper.getReadableDatabase()
				.query(distinct, table, projection, selection, selectionArgs, groupBy, having,
						sortOrder, limit);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String table = getTable(uri);
		int count = mDbHelper.getWritableDatabase().delete(table, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String table = getTable(uri);
		int count = mDbHelper.getWritableDatabase().update(table, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}

