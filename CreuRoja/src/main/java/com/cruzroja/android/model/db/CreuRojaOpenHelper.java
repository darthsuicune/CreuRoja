package com.cruzroja.android.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lapuente on 16.06.14.
 */
public class CreuRojaOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "CreuRoja";
	private static final int DB_VERSION = 1;
	private static final String CREATE = "CREATE TABLE ";

	public CreuRojaOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override public void onCreate(SQLiteDatabase db) {
		if (db.isReadOnly()) {
			db = getWritableDatabase();
		}
		createLocations(db);
		createUsers(db);
	}

	private void createLocations(SQLiteDatabase db) {
		StringBuilder builder = new StringBuilder();
		db.execSQL(builder.toString());
	}

	private void createUsers(SQLiteDatabase db) {
		StringBuilder builder = new StringBuilder();
		db.execSQL(builder.toString());
	}

	@Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

	}
}
