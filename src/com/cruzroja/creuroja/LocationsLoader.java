package com.cruzroja.creuroja;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.cruzroja.creuroja.utils.ConnectionClient;

public class LocationsLoader extends AsyncTaskLoader<ArrayList<Location>> {
	
	private boolean fromDb;
	
	private boolean isStarted = false;

	public LocationsLoader(Context context, boolean fromDb) {
		super(context);
		this.fromDb = fromDb;
	}

	@Override
	protected void onStartLoading() {
		if (!isStarted) {
			isStarted = true;
			forceLoad();
		}
		super.onStartLoading();
	}
	
	@Override
	public ArrayList<Location> loadInBackground() {
		if(fromDb){
			return null; // TODO:
		} else {
			return ConnectionClient.downloadLocations(getContext());
		}
	}

}
