package com.cruzroja.android.app.utils;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.cruzroja.android.app.Location;
import com.cruzroja.android.database.CreuRojaContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 20.12.13.
 */
public class LocationDownloader implements Runnable {
    private String mAccessToken;
    private long mLastUpdateTime;
    private ContentResolver mResolver;

    public LocationDownloader(String accessToken, long lastUpdateTime,
                              ContentResolver cr) {
        mAccessToken = accessToken;
        mLastUpdateTime = lastUpdateTime;
        mResolver = cr;
    }

    public static void saveLocations(ContentResolver cr, List<Location> locationList) {
        List<Location> currentLocations = LocationsProvider.getCurrentLocations(cr);
        ArrayList<ContentValues> insertValues = new ArrayList<>();
        for (Location location : locationList) {
            ContentValues value = new ContentValues();
            value.put(CreuRojaContract.Locations.ADDRESS, location.mAddress);
            value.put(CreuRojaContract.Locations.DETAILS, location.mDetails);
            value.put(CreuRojaContract.Locations.ICON, location.mType.toString().toLowerCase());
            value.put(CreuRojaContract.Locations.LAST_MODIFIED, location.mLastModified);
            value.put(CreuRojaContract.Locations.LATITUD, location.mLatitude);
            value.put(CreuRojaContract.Locations.LONGITUD, location.mLongitude);
            value.put(CreuRojaContract.Locations.NAME, location.mName);
            value.put(CreuRojaContract.Locations.EXPIRE_DATE, location.mExpireDate);

            if (!currentLocations.contains(location)) {
                if (location.mExpireDate == 0
                        || location.mExpireDate > System.currentTimeMillis()) {
                    insertValues.add(value);
                }
            } else {
                String where = CreuRojaContract.Locations.LATITUD + "=? AND " +
                        CreuRojaContract.Locations.LONGITUD + "=?";
                String[] whereArgs = {
                        Double.toString(location.mLatitude),
                        Double.toString(location.mLongitude)
                };
                if (location.mExpireDate == 0
                        || location.mExpireDate > System.currentTimeMillis()) {
                    cr.update(CreuRojaContract.Locations.CONTENT_LOCATIONS, value, where, whereArgs);
                } else {
                    cr.delete(CreuRojaContract.Locations.CONTENT_LOCATIONS, where, whereArgs);
                }
            }
        }
        if (insertValues.size() > 0) {
            cr.bulkInsert(CreuRojaContract.Locations.CONTENT_LOCATIONS,
                    insertValues.toArray(new ContentValues[insertValues.size()]));
        }
    }

    @Override
    public void run() {
        try {
            checkExpiredLocations(mResolver);
            List<Location> locationList = new ConnectionClient().
                    requestUpdates(mAccessToken, mLastUpdateTime);
            //TODO: implement something useful instead of this piece of crap
            saveLocations(mResolver, locationList);
        } catch (IOException e) {
            //In case problems during the connection arise, just leave a log.
            e.printStackTrace();
        }
    }

    public void checkExpiredLocations(final ContentResolver cr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String where = CreuRojaContract.Locations.EXPIRE_DATE + "<? AND "
                        + CreuRojaContract.Locations.EXPIRE_DATE + ">0";
                String[] whereArgs = {
                        Long.toString(System.currentTimeMillis())
                };
                cr.delete(CreuRojaContract.Locations.CONTENT_LOCATIONS,
                        where, whereArgs);
            }
        }).start();
    }
}
