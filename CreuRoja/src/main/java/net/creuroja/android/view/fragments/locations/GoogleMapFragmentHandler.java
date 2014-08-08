package net.creuroja.android.view.fragments.locations;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import net.creuroja.android.model.locations.Location;

/**
 * Created by lapuente on 08.08.14.
 */
public class GoogleMapFragmentHandler {
	SupportMapFragment mMapFragment;
	GoogleMap map;

	MapInteractionListener listener;

	public GoogleMapFragmentHandler(SupportMapFragment fragment, MapInteractionListener listener) {
		this.mMapFragment = fragment;
		this.listener = listener;
	}
	private static final LatLng DEFAULT_POSITION = new LatLng(41.3958, 2.1739);

	public static GoogleMapOptions getMapOptions() {
		GoogleMapOptions options = new GoogleMapOptions();
		CameraPosition.Builder cameraBuilder = new CameraPosition.Builder();
		cameraBuilder.target(DEFAULT_POSITION).zoom(12);
		options.compassEnabled(false).rotateGesturesEnabled(true).zoomControlsEnabled(false)
				.zoomGesturesEnabled(true).scrollGesturesEnabled(true).tiltGesturesEnabled(true)
				.camera(cameraBuilder.build());
		return options;
	}

	public void setMapType(int mapType) {
		map.setMapType(mapType);
	}

	public interface MapInteractionListener {
		public void onLocationClicked(Location location);
	}
}
