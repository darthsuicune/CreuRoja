package net.creuroja.android.controller;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by lapuente on 18.06.14.
 */
public interface MapController {
	public void setMapType(int mapType);
	public void setMapFragment(MapFragment fragment);
	public GoogleMapOptions getMapOptions();
}
