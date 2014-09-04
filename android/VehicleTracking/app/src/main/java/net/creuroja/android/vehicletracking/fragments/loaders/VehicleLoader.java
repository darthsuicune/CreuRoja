package net.creuroja.android.vehicletracking.fragments.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.creuroja.android.vehicletracking.model.Vehicle;
import net.creuroja.android.vehicletracking.model.Vehicles;

import org.json.JSONException;

import java.util.List;

/**
 * Created by lapuente on 04.09.14.
 */
public class VehicleLoader extends AsyncTaskLoader<List<Vehicle>> {
	List<Vehicle> mList;
	public VehicleLoader(Context context) {
		super(context);
	}

	@Override protected void onStartLoading() {
		super.onStartLoading();
		if(mList == null) {
			forceLoad();
		}
	}

	@Override public List<Vehicle> loadInBackground() {
		try {
			return Vehicles.getFromServer();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
