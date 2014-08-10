package net.creuroja.android.view.fragments.locations;

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
public class GoogleMapFragmentHandler implements MapFragmentHandler {
	private static final LatLng DEFAULT_POSITION = new LatLng(41.3958, 2.1739);

	SupportMapFragment mMapFragment;
	GoogleMap map;

	MapInteractionListener listener;

	LocationList locationList;
	Map<Marker, Location> mCurrentMarkers;

	public GoogleMapFragmentHandler(SupportMapFragment fragment, MapInteractionListener listener) {
		this.mMapFragment = fragment;
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

	@Override public void setMapType(int mapType) {
		map.setMapType(mapType);
	}

	@Override public void drawMarkers() {
		if (map == null && mMapFragment != null) {
			map = mMapFragment.getMap();
			if (map != null) {
				for (Location location : locationList.getLocations()) {
					drawMarker(location);
				}
			}
		}
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
	}

	public interface MapInteractionListener {
		public void onLocationClicked(Location location);
	}

	private void drawMarker(Location location) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(location.mLatitude, location.mLongitude));
		options.icon(BitmapDescriptorFactory.fromResource(location.mType.mIcon));
		mCurrentMarkers.put(map.addMarker(options), location);
		map.setInfoWindowAdapter(new OnLocationClickAdapter());
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
