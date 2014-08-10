package net.creuroja.android.view.fragments.locations;

import com.google.android.gms.maps.model.LatLng;

import net.creuroja.android.model.locations.Location;
import net.creuroja.android.model.locations.LocationList;
import net.creuroja.android.model.locations.LocationType;

/**
 * Created by denis on 10.08.14.
 */
public interface MapFragmentHandler {
	public void setMapType(int mapType);
	public void drawMarkers();
	public void drawDirections(LatLng currentPosition, Location location);
	public void toggleLocations(LocationType type, boolean active);
	public void setLocationList(LocationList locationList);
}
