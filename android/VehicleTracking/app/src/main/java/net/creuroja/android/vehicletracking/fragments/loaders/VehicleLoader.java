package net.creuroja.android.vehicletracking.fragments.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.creuroja.android.vehicletracking.model.Vehicle;
import net.creuroja.android.vehicletracking.model.Vehicles;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by lapuente on 04.09.14.
 */
public class VehicleLoader extends AsyncTaskLoader<List<Vehicle>> {
	List<Vehicle> mList;
	String mToken;

	public VehicleLoader(Context context, String token) {
		super(context);
		mToken = token;
	}

	@Override protected void onStartLoading() {
		super.onStartLoading();
		if (mList == null) {
			forceLoad();
		}
	}

	@Override public List<Vehicle> loadInBackground() {
		try {
			return Vehicles.getFromServer(mToken);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
