package net.creuroja.android.view.fragments.locations;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import net.creuroja.android.R;
import net.creuroja.android.controller.locations.LocationsListListener;
import net.creuroja.android.model.locations.LocationList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link LocationsListListener} callbacks
 * interface.
 */
public class LocationListFragment extends ListFragment implements AbsListView.OnItemClickListener {

	private LocationList mLocationList;

	private LocationsListListener mListener;

	// The fragment's ListView/GridView.
	private AbsListView mListView;

	// The Adapter which will be used to populate the ListView/GridView with Views.
	private ListAdapter mAdapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LocationListFragment() {
	}

	public static LocationListFragment newInstance(LocationList locationList) {
		LocationListFragment fragment = new LocationListFragment();
		fragment.mLocationList = locationList;
		return fragment;
	}

	public void setLocationList(LocationList locationList) {
		mLocationList = locationList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: Change Adapter to display your content
		mAdapter = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_locationlist, container, false);

		// Set the adapter
		mListView = (AbsListView) view.findViewById(android.R.id.list);
		mListView.setAdapter(mAdapter);

		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (LocationsListListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString() + " must implement LocationsListListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			mListener.onLocationListItemSelected(mLocationList.get(id));
		}
	}

	/**
	 * The default content for this Fragment has a TextView that is shown when
	 * the list is empty. If you would like to change the text, call this method
	 * to supply the text it should use.
	 */
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();

		if (emptyText instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
	}
}
