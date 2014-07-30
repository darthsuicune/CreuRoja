package net.creuroja.android.app.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;

import net.creuroja.android.app.Location;
import net.creuroja.android.app.Settings;
import net.creuroja.android.database.CreuRojaContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 20.12.13.
 */
public class LocationDownloader implements Runnable {
    private String mAccessToken;
    private ContentResolver mResolver;
    private SharedPreferences mPrefs;

    public LocationDownloader(String accessToken, ContentResolver cr, SharedPreferences prefs) {
        mAccessToken = accessToken;
        mResolver = cr;
        mPrefs = prefs;
    }

    public void saveLocations(List<Location> locationList) {
        String lastUpdateTime = mPrefs.getString(Settings.LAST_UPDATE_TIME, "");
        List<Location> currentLocations = LocationsProvider.getCurrentLocations(mResolver);
        ArrayList<ContentValues> insertValues = new ArrayList<>();
        for (Location location : locationList) {
            if(location.newerThan(lastUpdateTime)){
                lastUpdateTime = location.mLastModified;
            }
            ContentValues value = new ContentValues();
            value.put(CreuRojaContract.Locations.ADDRESS, location.mAddress);
            value.put(CreuRojaContract.Locations.DETAILS, location.mDescription);
            value.put(CreuRojaContract.Locations.ICON, location.mType.toString().toLowerCase());
            value.put(CreuRojaContract.Locations.LAST_MODIFIED, location.mLastModified);
            value.put(CreuRojaContract.Locations.LATITUD, location.mLatitude);
            value.put(CreuRojaContract.Locations.LONGITUD, location.mLongitude);
            value.put(CreuRojaContract.Locations.NAME, location.mName);
            value.put(CreuRojaContract.Locations.ACTIVE, (location.mActive) ? "1" : "0");

            if (currentLocations.contains(location)) {
                String where = CreuRojaContract.Locations.LATITUD + "=? AND " +
                        CreuRojaContract.Locations.LONGITUD + "=?";
                String[] whereArgs = {
                        Double.toString(location.mLatitude),
                        Double.toString(location.mLongitude)
                };
                if (location.mActive) {
                    mResolver.update(CreuRojaContract.Locations.CONTENT_LOCATIONS, value, where, whereArgs);
                } else {
                    mResolver.delete(CreuRojaContract.Locations.CONTENT_LOCATIONS, where, whereArgs);
                }
            } else {
                if (location.mActive) {
                    insertValues.add(value);
                }
            }
        }
        if (insertValues.size() > 0) {
            mResolver.bulkInsert(CreuRojaContract.Locations.CONTENT_LOCATIONS,
                    insertValues.toArray(new ContentValues[insertValues.size()]));
        }
        mPrefs.edit().putString(Settings.LAST_UPDATE_TIME, lastUpdateTime).commit();
    }

    @Override
    public void run() {
        try {
            List<Location> locationList = new PHPConnectionClient().
                    requestUpdates(mAccessToken, mPrefs.getString(Settings.LAST_UPDATE_TIME, ""));
            //TODO: implement something useful instead of this piece of crap
            saveLocations(locationList);
        } catch (IOException e) {
            //In case problems during the connection arise, just leave a log.
            e.printStackTrace();
        }
    }
}
