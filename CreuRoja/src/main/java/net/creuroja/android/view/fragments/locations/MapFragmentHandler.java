package net.creuroja.android.view.fragments.locations;

import android.support.v4.app.Fragment;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import net.creuroja.android.model.locations.Location;
import net.creuroja.android.model.locations.LocationList;
import net.creuroja.android.model.locations.LocationType;

import static net.creuroja.android.view.fragments.locations.GoogleMapFragmentHandler.MapInteractionListener;
import static net.creuroja.android.view.fragments.locations.GoogleMapFragmentHandler.getMapOptions;

/**
 * Created by denis on 10.08.14.
 */
public abstract class MapFragmentHandler {
	public static MapFragmentHandler getHandler(Fragment fragment,
												MapInteractionListener listener) {
		if(fragment == null) {
			fragment = SupportMapFragment.newInstance(getMapOptions());
		}
		return new GoogleMapFragmentHandler(fragment, listener);
	}

	public abstract void setMapType(MapType mapType);

	public abstract void drawDirections(LatLng currentPosition, Location location);

	public abstract void toggleLocations(LocationType type, boolean active);

	public abstract void setLocationList(LocationList locationList);

	public abstract Fragment getFragment();

	public enum MapType {
		MAP_TYPE_NORMAL(0), MAP_TYPE_TERRAIN(1), MAP_TYPE_SATELLITE(2), MAP_TYPE_HYBRID(3);

		private final int mValue;

		MapType(int value) {
			mValue = value;
		}

		public static MapType fromValue(final int value) {
			switch (value) {
				case 1:
					return MAP_TYPE_TERRAIN;
				case 2:
					return MAP_TYPE_SATELLITE;
				case 3:
					return MAP_TYPE_HYBRID;
				case 0:
				default:
					return MAP_TYPE_NORMAL;
			}
		}

		public int getValue() {
			return mValue;
		}
	}
}
