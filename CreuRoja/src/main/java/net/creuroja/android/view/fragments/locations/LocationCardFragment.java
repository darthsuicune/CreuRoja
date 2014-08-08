package net.creuroja.android.view.fragments.locations;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.creuroja.android.R;
import net.creuroja.android.model.locations.Location;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationCardFragment.OnLocationCardInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LocationCardFragment extends Fragment {
    // the fragment initialization parameters
    private Location mLocation;

	//Callback for the Activity
    private OnLocationCardInteractionListener mListener;
	//General location cardview
	private View cardView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationCardFragment.
     */
    public static LocationCardFragment newInstance(Location location) {
        LocationCardFragment fragment = new LocationCardFragment();
		fragment.setLocation(location);
        return fragment;
    }
    public LocationCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		cardView = inflater.inflate(R.layout.fragment_location_card, container, false);
		updateView();
		return cardView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLocationCardInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLocationCardInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

	public void setLocation(Location location) {
		mLocation = location;
		updateView();
	}

	private void updateView() {
		if(cardView != null && mLocation != null) {
			//TODO: Implement
		}
	}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLocationCardInteractionListener {
        public void onDirectionsRequested(Location location);
		public void onCardCloseRequested();
		public void onCardDetailsRequested(Location location);
    }

}
