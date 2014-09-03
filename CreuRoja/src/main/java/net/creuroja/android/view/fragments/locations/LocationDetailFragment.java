package net.creuroja.android.view.fragments.locations;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.creuroja.android.R;
import net.creuroja.android.model.db.CreuRojaContract;
import net.creuroja.android.model.locations.Location;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the
 * {@link OnLocationDetailsInteractionListener}
 * interface.
 */
public class LocationDetailFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final String ARG_LOCATION_ID = "locationId";
	private static final int LOADER_LOCATION = 1;

	private Location mLocation;

	private TextView mNameView;

	private OnLocationDetailsInteractionListener mListener;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LocationDetailFragment() {
	}

	public static LocationDetailFragment newInstance(int locationId) {
		LocationDetailFragment fragment = new LocationDetailFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_LOCATION_ID, locationId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//If getArguments is null, we come from an intent. If not, we put the fragment manually
		if (getArguments() == null) {
			getLoaderManager()
					.restartLoader(LOADER_LOCATION, getActivity().getIntent().getExtras(), this);
		} else {
			getLoaderManager().restartLoader(LOADER_LOCATION, getArguments(), this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_locationdetail, container, false);
		mNameView = (TextView) view.findViewById(R.id.fragment_location_detail_name);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnLocationDetailsInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString() + " must implement OnLocationDetailsInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		Uri uri = CreuRojaContract.Locations.CONTENT_LOCATIONS;
		String selection = CreuRojaContract.Locations.REMOTE_ID + "=?";
		String[] selectionArgs = {Integer.toString(bundle.getInt(ARG_LOCATION_ID))};
		return new CursorLoader(getActivity(), uri, null, selection, selectionArgs, null);
	}

	@Override public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		if(cursor.moveToFirst()) {
			mLocation = new Location(cursor);
			mNameView.setText(mLocation.mName);
		}
	}

	@Override public void onLoaderReset(Loader<Cursor> cursorLoader) {

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
	public interface OnLocationDetailsInteractionListener {
	}

}
