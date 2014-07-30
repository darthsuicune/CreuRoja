package net.creuroja.android.app.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import net.creuroja.android.app.Location;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by lapuente on 05.12.13.
 */
public class DirectionsLoader extends AsyncTaskLoader<List<LatLng>> {
    public static final String ARG_ORIG_LAT = "originLatitude";
    public static final String ARG_ORIG_LONG = "originLongitude";

    private List<LatLng> mDirections;
    private double origLat;
    private double origLong;
    private final Location mDestination;

    public DirectionsLoader(Context context, Bundle args, Location destination) {
        super(context);
        origLat = args.getDouble(ARG_ORIG_LAT);
        origLong = args.getDouble(ARG_ORIG_LONG);
        mDestination = destination;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mDirections == null){
            forceLoad();
        }
    }

    @Override
    public List<LatLng> loadInBackground() {
        try{
            mDirections = mDestination.getDirections(origLat, origLong);
        } catch (IOException e) {
            //return empty list
            if(mDirections != null) {
                mDirections.clear();
            }
        }
        return mDirections;
    }
}
