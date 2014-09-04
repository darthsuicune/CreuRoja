package net.creuroja.android.view.fragments.locations.factories;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.SupportMapFragment;

import net.creuroja.android.view.fragments.locations.GoogleMapFragmentHandler;
import net.creuroja.android.view.fragments.locations.MapFragmentHandler;

import static net.creuroja.android.view.fragments.locations.GoogleMapFragmentHandler.getMapOptions;

/**
 * Created by lapuente on 03.09.14.
 */
public class MapFragmentHandlerFactory {
	public static MapFragmentHandler getHandler(Fragment fragment,
												GoogleMapFragmentHandler.MapInteractionListener listener,
												SharedPreferences prefs) {
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance(getMapOptions());
			fragment.setRetainInstance(true);
		}
		return new GoogleMapFragmentHandler(fragment, listener, prefs);
	}
}
