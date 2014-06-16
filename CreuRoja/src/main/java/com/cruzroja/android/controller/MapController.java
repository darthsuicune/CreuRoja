package com.cruzroja.android.controller;

import android.content.SharedPreferences;

import com.cruzroja.android.app.Settings;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by lapuente on 16.06.14.
 */
public class MapController {
	MapFragment mapFragment;
	GoogleMap map;
	SharedPreferences prefs;

	public MapController(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	public void setMapFragment(MapFragment fragment) {
		this.mapFragment = fragment;
		map = mapFragment.getMap();
	}

	public GoogleMapOptions getMapOptions(){
		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(prefs.getInt(Settings.MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL));
		options.compassEnabled(false);
		options.zoomControlsEnabled(false);
		return options;
	}

	public void setMapType(int mapType) {
		if(map == null) {
			map = mapFragment.getMap();
		}
		if(map != null) {
			map.setMapType(mapType);
		}
	}
}
