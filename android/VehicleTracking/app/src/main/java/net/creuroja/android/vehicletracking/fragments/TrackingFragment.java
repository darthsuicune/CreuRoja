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

import net.creuroja.android.vehicletracking.PositionNotifierService;
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
	private static final int REQUEST_SERVICE = 1;

	private OnTrackingFragmentInteractionListener mListener;

	boolean isStarted;
	boolean inProgress;

	private SharedPreferences prefs;

	private Spinner mVehicleListSpinner;
	private View mProgressView;
	private View mMainView;

	private PendingIntent pendingIntent;

	public TrackingFragment() {
		// Required empty public constructor
	}

	@Override public void onNotificationReceived(int id) {

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
		public void onTrackingStartRequested();

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
					mListener.onTrackingStopRequested();
				} else {
					((Button) view).setText(R.string.stop_tracking);
					startTracking();
					mListener.onTrackingStartRequested();
				}
				isStarted = !isStarted;
			}
		});
		trackingButton.setText((isStarted) ? R.string.stop_tracking : R.string.start_tracking);
	}

	private void startTracking() {
		startService();
		mListener.onTrackingStartRequested();
		prefs.edit().putBoolean(IS_TRACKING, true).apply();
	}

	private void stopTracking() {
		stopService();
		mListener.onTrackingStopRequested();
		prefs.edit().putBoolean(IS_TRACKING, false).apply();
	}

	private void startService() {
		Intent intent = new Intent(PositionNotifierService.ACTION_NOTIFY);
		Vehicle vehicle = (Vehicle) mVehicleListSpinner.getSelectedItem();
		intent.putExtra(PositionNotifierService.EXTRA_INDICATIVE, vehicle.indicative);
		setAlarm(intent, true);
	}

	private void stopService() {
		Intent intent = new Intent(PositionNotifierService.ACTION_STOP);
		setAlarm(intent, false);
	}

	private void setAlarm(Intent intent, boolean set) {
		AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		if (set) {
			pendingIntent = PendingIntent.getService(getActivity(), REQUEST_SERVICE, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			long interval = prefs.getLong(Settings.INTERVAL, Settings.DEFAULT_INTERVAL);
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, 0, interval, pendingIntent);
		} else {
			alarm.cancel(pendingIntent);
		}

	}

	private void populateList(List<Vehicle> vehicles) {
		mVehicleListSpinner.setAdapter(
				new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
						vehicles));
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
			} else {
				Log.d(PositionNotifierService.SERVICE_NAME, "Error while retrieving vehicles");
				getLoaderManager().restartLoader(LOADER_VEHICLES, null, this);
			}
			showProgress(false);
		}

		@Override public void onLoaderReset(Loader<List<Vehicle>> objectLoader) {

		}
	}
}
