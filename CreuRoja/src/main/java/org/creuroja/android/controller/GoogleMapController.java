package org.creuroja.android.controller;

import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;

import org.creuroja.android.app.Settings;

/**
 * Created by lapuente on 16.06.14.
 */
public class GoogleMapController implements MapController {
	MapFragment mapFragment;
	GoogleMap map;
	SharedPreferences prefs;

	public GoogleMapController(SharedPreferences prefs) {
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
