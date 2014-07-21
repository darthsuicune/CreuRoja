package org.creuroja.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreuRojaOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "CreuRoja";
    private static final int DB_VERSION = 2;
    private static final String CREATE = "CREATE TABLE ";
    private static final String KEY = " INTEGER PRIMARY KEY AUTOINCREMENT, ";

    public CreuRojaOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }

        db.execSQL(CREATE + CreuRojaContract.Locations.TABLE_NAME + " ("
                + CreuRojaContract.Locations._ID + KEY
                + CreuRojaContract.Locations.LATITUD + " DOUBLE NOT NULL, "
                + CreuRojaContract.Locations.LONGITUD + " DOUBLE NOT NULL, "
                + CreuRojaContract.Locations.ICON + " TEXT NOT NULL, "
                + CreuRojaContract.Locations.NAME + " TEXT NOT NULL, "
                + CreuRojaContract.Locations.ADDRESS + " TEXT, "
                + CreuRojaContract.Locations.DETAILS + " TEXT, "
                + CreuRojaContract.Locations.ACTIVE + " INTEGER NOT NULL, "
                + CreuRojaContract.Locations.LAST_MODIFIED + " TEXT NOT NULL)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(oldVersion) {
			case 1:
				db.execSQL("ALTER TABLE " + CreuRojaContract.Locations.TABLE_NAME + " ADD COLUMN "
						   + CreuRojaContract.Locations.ACTIVE + " INTEGER NOT NULL DEFAULT 1");
				break;
			default:
				break;
		}
    }
}
