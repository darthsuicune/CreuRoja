package net.creuroja.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import net.creuroja.android.app.Settings;

public class CreuRojaOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "CreuRoja";
	private static final int DB_VERSION = 4;
	private static final String CREATE = "CREATE TABLE ";
	private static final String KEY = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
	private Context mContext;

	public CreuRojaOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (db.isReadOnly()) {
			db = getWritableDatabase();
		}

		db.execSQL(CREATE + CreuRojaContract.Locations.TABLE_NAME + " (" +
				   CreuRojaContract.Locations._ID + KEY + CreuRojaContract.Locations.LATITUD +
				   " DOUBLE NOT NULL, " + CreuRojaContract.Locations.LONGITUD +
				   " DOUBLE NOT NULL, " + CreuRojaContract.Locations.ICON + " TEXT NOT NULL, " +
				   CreuRojaContract.Locations.NAME + " TEXT NOT NULL, " +
				   CreuRojaContract.Locations.ADDRESS + " TEXT, " +
				   CreuRojaContract.Locations.DETAILS + " TEXT, " +
				   CreuRojaContract.Locations.ACTIVE + " INTEGER NOT NULL, " +
				   CreuRojaContract.Locations.PHONE + " TEXT, " +
				   CreuRojaContract.Locations.LAST_MODIFIED + " TEXT NOT NULL)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
			case 1:
				db.execSQL("ALTER TABLE " + CreuRojaContract.Locations.TABLE_NAME + " ADD COLUMN " +
						   CreuRojaContract.Locations.ACTIVE + " INTEGER NOT NULL DEFAULT 1");
			case 2:
				db.execSQL("ALTER TABLE " + CreuRojaContract.Locations.TABLE_NAME + " ADD COLUMN " +
						   CreuRojaContract.Locations.PHONE + " TEXT");
			case 3:
				//We need to remove the old values as we need the new one
				db.execSQL("DELETE FROM " + CreuRojaContract.Locations.TABLE_NAME);
				PreferenceManager.getDefaultSharedPreferences(mContext).edit()
						.remove(Settings.LAST_UPDATE_TIME).commit();
				//Then we can add the column to the DB and it will update itself
				db.execSQL("ALTER TABLE " + CreuRojaContract.Locations.TABLE_NAME + " ADD COLUMN " +
						   CreuRojaContract.Locations.REMOTE_ID + " INTEGER NOT NULL");
			default:
				break;
		}
	}
}
