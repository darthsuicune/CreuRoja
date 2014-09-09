package net.creuroja.android.vehicletracking.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import net.creuroja.android.vehicletracking.PositionUpdaterService;
import net.creuroja.android.vehicletracking.R;
import net.creuroja.android.vehicletracking.activities.OnNotificationReceivedListener;
import net.creuroja.android.vehicletracking.fragments.loaders.VehicleLoader;
import net.creuroja.android.vehicletracking.model.Settings;
import net.creuroja.android.vehicletracking.model.Vehicle;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTrackingFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TrackingFragment extends Fragment implements OnNotificationReceivedListener {
	private static final int LOADER_VEHICLES = 1;

	private static final String IS_TRACKING = "Tracking";
	private static final int REQUEST_NOTIFY_SERVICE = 1;

	private OnTrackingFragmentInteractionListener mListener;

	boolean isStarted;
	public boolean inProgress;
	public boolean hasLocation;
	public boolean isConnected;

	private SharedPreferences prefs;

	private Spinner mVehicleListSpinner;
	private View mProgressView;
	private View mMainView;

	private PendingIntent pendingIntent;

	public TrackingFragment() {
		// Required empty public constructor
	}

	@Override public void onNotificationReceived(int id) {
		switch (id) {
			case PositionUpdaterService.NOTIFICATION_FINISHED:
				break;
			case PositionUpdaterService.NOTIFICATION_PERMANENT:
				break;
		}
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnTrackingFragmentInteractionListener {
		public void onTrackingStartRequested(Vehicle vehicle);

		public void onTrackingStopRequested();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnTrackingFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString() + " must implement OnTrackingFragmentInteractionListener");
		}
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		isStarted = prefs.getBoolean(IS_TRACKING, false);
		getLoaderManager().restartLoader(LOADER_VEHICLES, null, new VehicleLoaderCallbacks());
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tracking, container, false);
		prepareViews(v);
		return v;
	}

	private void prepareViews(View v) {
		mVehicleListSpinner = (Spinner) v.findViewById(R.id.vehicle_list_spinner);
		mProgressView = v.findViewById(R.id.progress);
		mMainView = v.findViewById(R.id.main);
		if (inProgress) {
			showProgress(true);
		}
		Button trackingButton = (Button) v.findViewById(R.id.tracking_button);
		trackingButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				if (isStarted) {
					stopTracking();
					((Button) view).setText(R.string.start_tracking);
				} else {
					((Button) view).setText(R.string.stop_tracking);
					startTracking();
				}
				isStarted = !isStarted;
			}
		});
		trackingButton.setText((isStarted) ? R.string.stop_tracking : R.string.start_tracking);
	}

	private void startTracking() {
		Vehicle vehicle = (Vehicle) mVehicleListSpinner.getSelectedItem();
		startService(vehicle);
		mListener.onTrackingStartRequested(vehicle);

		prefs.edit().putBoolean(IS_TRACKING, true)
				.putString(Settings.DEFAULT_VEHICLE, vehicle.indicative).apply();

	}

	private void startService(Vehicle vehicle) {
		createPendingIntent(vehicle, true);
		setAlarm(true);
	}

	private void setAlarm(boolean run) {
		AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		if (run) {
			manager.setRepeating(AlarmManager.RTC_WAKEUP, 0,
					prefs.getLong(Settings.INTERVAL, Settings.DEFAULT_INTERVAL), pendingIntent);
		} else {
			createPendingIntent(null, run);
			manager.cancel(pendingIntent);
		}
	}

	private void stopTracking() {
		stopService();
		mListener.onTrackingStopRequested();
		prefs.edit().putBoolean(IS_TRACKING, false).apply();
	}

	private void stopService() {
		setAlarm(false);
		Intent intent = new Intent(getActivity(), PositionUpdaterService.class);
		getActivity().stopService(intent);
	}

	private void createPendingIntent(Vehicle vehicle, boolean run) {
		if (pendingIntent == null) {
			Intent intent = new Intent(getActivity(), PositionUpdaterService.class);
			if (run) {
				intent.putExtra(PositionUpdaterService.EXTRA_VEHICLE_ID, vehicle.id);
				intent.putExtra(PositionUpdaterService.EXTRA_INDICATIVE, vehicle.indicative);
			}
			pendingIntent = PendingIntent.getService(getActivity(), REQUEST_NOTIFY_SERVICE, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			if (run) {
				getActivity().startService(intent);
			}
		}
	}

	private void populateList(List<Vehicle> vehicles) {
		mVehicleListSpinner.setAdapter(
				new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
						vehicles));
		mVehicleListSpinner.setSelection(
				findVehicle(vehicles, prefs.getString(Settings.DEFAULT_VEHICLE, "")));
	}

	private int findVehicle(List<Vehicle> vehicles, String indicative) {
		int i = 0;
		for (Vehicle vehicle : vehicles) {
			if (vehicle.indicative.equals(indicative)) {
				return i;
			}
			i++;
		}
		return 0;
	}

	public void showProgress(boolean show) {
		if (mProgressView != null) {
			mProgressView.setVisibility((show) ? View.VISIBLE : View.GONE);
			mMainView.setVisibility((show) ? View.GONE : View.VISIBLE);
		}
	}

	private class VehicleLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Vehicle>> {
		@Override public Loader<List<Vehicle>> onCreateLoader(int i, Bundle bundle) {
			inProgress = true;
			showProgress(true);
			return new VehicleLoader(getActivity(), prefs.getString(Settings.ACCESS_TOKEN, null));
		}

		@Override public void onLoadFinished(Loader<List<Vehicle>> objectLoader,
											 List<Vehicle> vehicleList) {
			if (vehicleList != null) {
				populateList(vehicleList);
				if (isConnected && hasLocation) {
					showProgress(false);
				}
			} else {
				Log.d("Vehicle Tracking", "Error while retrieving vehicles");
				getLoaderManager().restartLoader(LOADER_VEHICLES, null, this);
			}
			inProgress = false;
		}

		@Override public void onLoaderReset(Loader<List<Vehicle>> objectLoader) {

		}
	}
}
