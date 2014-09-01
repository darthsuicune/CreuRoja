package net.creuroja.android.view.fragments.locations;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.creuroja.android.model.locations.Location;
import net.creuroja.android.model.locations.LocationList;
import net.creuroja.android.model.locations.LocationType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lapuente on 08.08.14.
 */
public class GoogleMapFragmentHandler extends MapFragmentHandler {
	private static final LatLng DEFAULT_POSITION = new LatLng(41.3958, 2.1739);

	SupportMapFragment mMapFragment;
	GoogleMap map;

	MapInteractionListener listener;

	LocationList locationList;
	Map<Marker, Location> mCurrentMarkers;

	public GoogleMapFragmentHandler(Fragment fragment, MapInteractionListener listener) {
		this.mMapFragment = (SupportMapFragment) fragment;
		this.listener = listener;
		mCurrentMarkers = new HashMap<>();
	}

	public static GoogleMapOptions getMapOptions() {
		GoogleMapOptions options = new GoogleMapOptions();
		CameraPosition.Builder cameraBuilder = new CameraPosition.Builder();
		cameraBuilder.target(DEFAULT_POSITION).zoom(12);
		options.compassEnabled(false).rotateGesturesEnabled(true).zoomControlsEnabled(false)
				.zoomGesturesEnabled(true).scrollGesturesEnabled(true).tiltGesturesEnabled(true)
				.camera(cameraBuilder.build());
		return options;
	}

	@Override public void setMapType(MapType mapType) {
		if(setUpMap()) {
			map.setMapType(getMapType(mapType));
		}
	}

	private int getMapType(MapType mapType) {
		switch (mapType) {
			case MAP_TYPE_TERRAIN:
				return GoogleMap.MAP_TYPE_TERRAIN;
			case MAP_TYPE_SATELLITE:
				return GoogleMap.MAP_TYPE_SATELLITE;
			case MAP_TYPE_HYBRID:
				return GoogleMap.MAP_TYPE_HYBRID;
			case MAP_TYPE_NORMAL:
			default:
				return GoogleMap.MAP_TYPE_NORMAL;
		}
	}

	public void drawMarkers() {
		if (setUpMap() && locationList != null) {
			for (Location location : locationList.getLocations()) {
				drawMarker(location);
			}
		}
	}

	private boolean setUpMap() {
		if (map == null && mMapFragment != null) {
			map = mMapFragment.getMap();
		}
		return map != null;
	}

	@Override public void drawDirections(LatLng currentPosition, Location location) {
		//TODO: Implement
		Toast.makeText(mMapFragment.getActivity(), "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show();
	}

	@Override public void toggleLocations(LocationType type, boolean newState) {
		for (Marker marker : mCurrentMarkers.keySet()) {
			if (mCurrentMarkers.get(marker).mType == type) {
				marker.setVisible(newState);
			}
		}
	}

	@Override public void setLocationList(LocationList locationList) {
		this.locationList = locationList;
		drawMarkers();
	}

	@Override public Fragment getFragment() {
		return mMapFragment;
	}

	private void drawMarker(Location location) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(location.mLatitude, location.mLongitude));
		options.icon(BitmapDescriptorFactory.fromResource(location.mType.mIcon));
		mCurrentMarkers.put(map.addMarker(options), location);
		map.setInfoWindowAdapter(new OnLocationClickAdapter());
	}

	public interface MapInteractionListener {
		public void onLocationClicked(Location location);
	}

	private class OnLocationClickAdapter implements GoogleMap.InfoWindowAdapter {
		@Override public View getInfoWindow(Marker marker) {
			return null;
		}

		@Override public View getInfoContents(Marker marker) {
			listener.onLocationClicked(mCurrentMarkers.get(marker));
			return null;
		}
	}
}
