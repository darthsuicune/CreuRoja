package com.cruzroja.creuroja.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CreuRojaProvider extends ContentProvider {
	protected static final String CONTENT_NAME = "com.cruzroja.creuroja.database.Provider";
	public static final Uri CONTENT_LOCATIONS = Uri.parse("content://"
			+ CONTENT_NAME + "/locations");

	CreuRojaOpenHelper mDbHelper;

	private static final int LOCATIONS = 1;
	private static final int LOCATIONS_ID = 2;

	static UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Locations.TABLE_NAME,
				LOCATIONS);
		sUriMatcher.addURI(CONTENT_NAME, CreuRojaContract.Locations.TABLE_NAME
				+ "/#", LOCATIONS_ID);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new CreuRojaOpenHelper(getContext());
		return mDbHelper != null;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case LOCATIONS:
			return ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_NAME + "."
					+ CreuRojaContract.Locations.TABLE_NAME;
		case LOCATIONS_ID:
			return ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_NAME + "."
					+ CreuRojaContract.Locations.TABLE_NAME;
		default:
			return null;
		}
	}

	private String getTable(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case LOCATIONS_ID:
		case LOCATIONS:
			return CreuRojaContract.Locations.TABLE_NAME;
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
		}
		getContext().getContentResolver().notifyChange(uri, null);
		getContext().getContentResolver().notifyChange(result, null);
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String table = getTable(uri);
		boolean distinct = false;
		String groupBy = null;
		String having = null;
		String limit = null;
		Cursor cursor = mDbHelper.getReadableDatabase().query(distinct, table,
				projection, selection, selectionArgs, groupBy, having,
				sortOrder, limit);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String table = getTable(uri);
		int count = mDbHelper.getWritableDatabase().delete(table, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String table = getTable(uri);
		int count = mDbHelper.getWritableDatabase().update(table, values,
				selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
