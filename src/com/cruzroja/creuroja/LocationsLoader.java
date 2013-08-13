package com.cruzroja.creuroja;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.cruzroja.creuroja.utils.ConnectionClient;
import com.cruzroja.creuroja.utils.FileUtils;

public class LocationsLoader extends AsyncTaskLoader<ArrayList<Location>> {
	
	private boolean fromFile;
	
	private boolean isStarted = false;

	public LocationsLoader(Context context, boolean fromFile) {
		super(context);
		this.fromFile = fromFile;
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
		if(fromFile){
			return FileUtils.readFromFiles(getContext());
		} else {
			return ConnectionClient.downloadLocations(getContext());
		}
	}

}
