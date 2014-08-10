package net.creuroja.android.view.fragments.locations;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.creuroja.android.R;
import net.creuroja.android.controller.locations.LocationsListListener;
import net.creuroja.android.model.locations.Location;
import net.creuroja.android.model.locations.LocationList;
import net.creuroja.android.model.locations.LocationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private List<Location> mShownLocations;
	private Map<LocationType, Boolean> toggledLocations;
	private LocationsListListener mListener;

	// The fragment's ListView/GridView.
	private ListView mListView;

	// The Adapter which will be used to populate the ListView/GridView with Views.
	private ListAdapter mAdapter;

	private SharedPreferences prefs;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LocationListFragment() {
	}

	public static LocationListFragment newInstance(LocationList locationList) {
		LocationListFragment fragment = new LocationListFragment();
		fragment.setLocationList(locationList);
		return fragment;
	}

	public void setLocationList(LocationList locationList) {
		mLocationList = locationList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		toggledLocations = new HashMap<>();
		for(LocationType type : LocationType.values()) {
			toggledLocations.put(type, type.getViewable(prefs));
		}
		mShownLocations = new ArrayList<>();
		if(mShownLocations.isEmpty()) {
			for (Location location : mLocationList.getLocations()) {
				if (location.mType.getViewable(prefs)) {
					mShownLocations.add(location);
				}
			}
		}
		mAdapter = new LocationListAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_locationlist, container, false);

		// Set the adapter
		mListView = (ListView) view.findViewById(android.R.id.list);
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
			mListener.onLocationListItemSelected(mLocationList.getById(id));
		}
	}

	public void toggleLocations(LocationType type, boolean newState) {
		//TODO: Fix this
		toggledLocations.put(type, newState);
		for(Location location : mLocationList.getLocations()) {
			if(location.mType == type) {
				if (newState) {
					mShownLocations.add(location);
				} else {
					mShownLocations.remove(location);
				}
			}
		}
		mAdapter = new LocationListAdapter(getActivity());
		mListView.setAdapter(mAdapter);
	}

	private class LocationListAdapter extends ArrayAdapter<Location> {
		public LocationListAdapter(Context context) {
			super(context, R.layout.location_list_entry, mShownLocations);
		}

		@Override public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.location_list_entry, parent, false);
				holder = populateHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			populateView(holder, mLocationList.get(position));
			return convertView;
		}

		private ViewHolder populateHolder(View convertView) {
			ViewHolder holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.location_list_entry_icon);
			holder.name = (TextView) convertView.findViewById(R.id.location_list_entry_name);
			holder.description =
					(TextView) convertView.findViewById(R.id.location_list_entry_description);
			holder.address = (TextView) convertView.findViewById(R.id.location_list_entry_address);
			holder.phone = (TextView) convertView.findViewById(R.id.location_list_entry_phone);
			return holder;
		}

		private void populateView(ViewHolder holder, Location location) {
			if (location != null) {
				holder.icon.setImageResource(location.mType.mIcon);
				holder.name.setText(location.mName);
				holder.description.setText(location.mDescription);
				holder.address.setText(location.mAddress);
				holder.phone.setText(location.mPhone);
			}
		}
	}

	static class ViewHolder {
		ImageView icon;
		TextView name;
		TextView description;
		TextView phone;
		TextView address;
	}
}
