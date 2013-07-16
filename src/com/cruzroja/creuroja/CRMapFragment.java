package com.cruzroja.creuroja;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.*;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.Collection;

public class CRMapFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Location>>, CompoundButton.OnCheckedChangeListener,
        GoogleMap.OnInfoWindowClickListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final int LOADER_CONNECTION = 1;
    private static final int LOADER_DIRECTIONS = 2;

    private static final String MAP_STYLE = "mapStyle";
    private static final int MAP_STYLE_NORMAL = 0;
    private static final int MAP_STYLE_HYBRID = 1;
    private static final int MAP_STYLE_TERRAIN = 2;
    private static final int MAP_STYLE_SATELLITE = 3;

    private static final String SHOW_ALPHA = "showAlpha";
    private static final String SHOW_ASAMBLEA = "showAsamblea";
    private static final String SHOW_AVISO = "showAviso";
    private static final String SHOW_BRAVO = "showBravo";
    private static final String SHOW_CUAP = "showCuap";
    private static final String SHOW_HOSPITAL = "showHospital";
    private static final String SHOW_MIKE = "showMike";

    private GoogleMap mGoogleMap;
    private LocationClient mLocationClient;
    private ArrayList<Location> mLocationsList;
    private Polyline mPolyline;

    private CheckBox mAlphaCheckBox;
    private CheckBox mAsambleaCheckBox;
    private CheckBox mAvisoCheckBox;
    private CheckBox mBravoCheckBox;
    private CheckBox mCuapCheckBox;
    private CheckBox mHospitalCheckBox;
    private CheckBox mMikeCheckBox;

    private View mMarkerPanel;

    private boolean isMarkerPanelShowing;

    private SharedPreferences prefs;

    public CRMapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mMarkerPanel = v.findViewById(R.id.marker_panel);

        if (mMarkerPanel != null) {
            mAlphaCheckBox = (CheckBox) v.findViewById(R.id.checkbox_alpha);
            mAsambleaCheckBox = (CheckBox) v.findViewById(R.id.checkbox_asamblea);
            mAvisoCheckBox = (CheckBox) v.findViewById(R.id.checkbox_aviso);
            mBravoCheckBox = (CheckBox) v.findViewById(R.id.checkbox_bravo);
            mCuapCheckBox = (CheckBox) v.findViewById(R.id.checkbox_cuap);
            mHospitalCheckBox = (CheckBox) v.findViewById(R.id.checkbox_hospital);
            mMikeCheckBox = (CheckBox) v.findViewById(R.id.checkbox_mike);

            mAlphaCheckBox.setChecked(prefs.getBoolean(SHOW_ALPHA, true));
            mAsambleaCheckBox.setChecked(prefs.getBoolean(SHOW_ASAMBLEA, true));
            mAvisoCheckBox.setChecked(prefs.getBoolean(SHOW_AVISO, true));
            mBravoCheckBox.setChecked(prefs.getBoolean(SHOW_BRAVO, true));
            mCuapCheckBox.setChecked(prefs.getBoolean(SHOW_CUAP, true));
            mHospitalCheckBox.setChecked(prefs.getBoolean(SHOW_HOSPITAL, true));
            mMikeCheckBox.setChecked(prefs.getBoolean(SHOW_MIKE, true));

            mAlphaCheckBox.setOnCheckedChangeListener(this);
            mAsambleaCheckBox.setOnCheckedChangeListener(this);
            mAvisoCheckBox.setOnCheckedChangeListener(this);
            mBravoCheckBox.setOnCheckedChangeListener(this);
            mCuapCheckBox.setOnCheckedChangeListener(this);
            mHospitalCheckBox.setOnCheckedChangeListener(this);
            mMikeCheckBox.setOnCheckedChangeListener(this);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestGooglePlayServicesAvailability();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setActionBar();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        if (savedInstanceState == null) {
            mapFragment.setRetainInstance(true);
            downloadData();
        } else {
            mGoogleMap = mapFragment.getMap();
        }

        setMap();

        mLocationClient = new LocationClient(getActivity(), this, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setSearchOptions(menu);
        }
    }

    public boolean onBackPressed() {
        if (isMarkerPanelShowing) {
            mMarkerPanel.setVisibility(View.GONE);
            isMarkerPanelShowing = false;
        }
        return isMarkerPanelShowing;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                /*
                 * If the result code is Activity.RESULT_OK, try
                 * to connect again
                 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        requestGooglePlayServicesAvailability();
                        break;
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_locate:
                moveToLocation();
                return true;
            case R.id.search:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    getActivity().onSearchRequested();
                }
                return true;
            case android.R.id.home:
            case R.id.menu_show_panel:
                showMarkerPanel();
                return true;
            case R.id.menu_refresh:
                downloadData();
                return true;
            case R.id.menu_show_hybrid:
                return setMapStyle(MAP_STYLE_HYBRID);
            case R.id.menu_show_normal:
                return setMapStyle(MAP_STYLE_NORMAL);
            case R.id.menu_show_satellite:
                return setMapStyle(MAP_STYLE_SATELLITE);
            case R.id.menu_show_terrain:
                return setMapStyle(MAP_STYLE_TERRAIN);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Check that Google Play services is available
     */
    private boolean requestGooglePlayServicesAvailability() {
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),
                        "Location Updates");
            }
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setActionBar() {
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setSearchOptions(Menu menu) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new QueryListener());
    }

    private void downloadData() {
        if (isConnected()) {
            getLoaderManager().restartLoader(LOADER_CONNECTION, null,
                    this);
        } else {
            Toast.makeText(getActivity(), R.string.no_connection,
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
    }

    private void setMap() {
        if (mGoogleMap == null) {
            mGoogleMap = ((SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (mGoogleMap != null) {
                setMapStyle(prefs.getInt(MAP_STYLE, MAP_STYLE_NORMAL));
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(41.3958, 2.1739), 12));
            }
        }

        mLocationsList = JSONParser.getFromDisk(getActivity());
        drawMarkers(null);
    }

    private boolean setMapStyle(int mapStyle) {
        if (mGoogleMap == null) {
            return false;
        }

        switch (mapStyle) {
            case MAP_STYLE_NORMAL:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case MAP_STYLE_HYBRID:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case MAP_STYLE_SATELLITE:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case MAP_STYLE_TERRAIN:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                return false;
        }
        prefs.edit().putInt(MAP_STYLE, mapStyle).commit();
        return true;
    }

    public void drawMarkers(String filter) {
        if (mGoogleMap == null || mLocationsList == null) {
            return;
        }

        mGoogleMap.clear();

        if (mPolyline != null) {
            drawLine(mPolyline.getPoints());
        }

        for (Location location : mLocationsList) {
            if (!shouldShowMarker(location, filter)) {
                continue;
            }

            MarkerOptions marker = new MarkerOptions().position(location.getPosition());
            if (location.mIcono != 0) {
                marker.icon(BitmapDescriptorFactory.fromResource(location.mIcono));
            }
            if (location.mContenido.mNombre != null) {
                marker.title(location.mContenido.mNombre);
            }
            if (location.mContenido.mSnippet != null) {
                marker.snippet(location.mContenido.mSnippet);
            }

            mGoogleMap.addMarker(marker);
        }
        mGoogleMap.setInfoWindowAdapter(new MarkerAdapter());
        mGoogleMap.setOnInfoWindowClickListener(this);
    }

    private void drawLine(Collection<LatLng> points) {
        if (mPolyline != null) {
            mPolyline.remove();
        }
        if (mGoogleMap == null || points == null) {
            return;
        }
        if (points.size() == 0) {
            Toast.makeText(getActivity(), R.string.limit_reached,
                    Toast.LENGTH_LONG).show();
        }

        mPolyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(points)
                .color(Color.parseColor("#CC0000")));
    }

    private boolean shouldShowMarker(Location location, String filter) {
        if (!matchFilter(location, filter)) {
            return false;
        }
        switch (location.mIcono) {
            case R.drawable.alfa:
                return prefs.getBoolean(SHOW_ALPHA, true);
            case R.drawable.asamblea:
                return prefs.getBoolean(SHOW_ASAMBLEA, true);
            case R.drawable.aviso:
                return prefs.getBoolean(SHOW_AVISO, true);
            case R.drawable.bravo:
                return prefs.getBoolean(SHOW_BRAVO, true);
            case R.drawable.cuap:
                return prefs.getBoolean(SHOW_CUAP, true);
            case R.drawable.hospital:
                return prefs.getBoolean(SHOW_HOSPITAL, true);
            case R.drawable.mike:
                return prefs.getBoolean(SHOW_MIKE, true);
            default:
                return true;
        }
    }

    private boolean matchFilter(Location location, String filter) {
        if (filter != null) {
            filter = dehyphenize(filter);
            String nombre = dehyphenize(location.mContenido.mNombre);
            String lugar = null;
            if (location.mContenido.mLugar != null) {
                lugar = dehyphenize(location.mContenido.mLugar);
            }
            if (!nombre.contains(filter)) {
                if (lugar == null || !lugar.contains(filter)) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("DefaultLocale")
    private String dehyphenize(String input) {
        input = input.toLowerCase();
        return input.replace("à", "a").replace("á", "a").replace("é", "e")
                .replace("è", "e").replace("í", "i").replace("ì", "i")
                .replace("ó", "o").replace("ò", "o").replace("ú", "u")
                .replace("ù", "u");
    }

    private void moveToLocation() {
        if (mGoogleMap != null) {
            if (locationServicesAreEnabled()) {
                if (mLocationClient.getLastLocation() != null) {
                    mGoogleMap.animateCamera(CameraUpdateFactory
                            .newLatLng(new LatLng(mLocationClient.getLastLocation()
                                    .getLatitude(), mLocationClient.getLastLocation()
                                    .getLongitude())));
                } else {
                    Toast.makeText(getActivity(), R.string.locating, Toast.LENGTH_SHORT).show();
                }
            } else {
                showLocationSettings();
            }
        }
    }

    private boolean locationServicesAreEnabled() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }

    private void showMarkerPanel() {
        if (isMarkerPanelShowing) {
            mMarkerPanel.setVisibility(View.GONE);
            isMarkerPanelShowing = false;
        } else {
            isMarkerPanelShowing = true;
            mMarkerPanel.setVisibility(View.VISIBLE);
        }

    }

    private void showLocationSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.location_disabled_title);
        builder.setMessage(R.string.location_disabled_message);
        builder.setPositiveButton(R.string.open_location_settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    @Override
    public Loader<ArrayList<Location>> onCreateLoader(int id, Bundle args) {
        Loader<ArrayList<Location>> loader = null;
        switch (id) {
            case LOADER_CONNECTION:
                loader = new LocationsLoader(getActivity());
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Location>> loader,
                               ArrayList<Location> locations) {
        switch (loader.getId()) {
            case LOADER_CONNECTION:
                if (locations == null) {
                    return;
                }
                mLocationsList = locations;
                drawMarkers(null);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Location>> loader) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = prefs.edit();
        switch (buttonView.getId()) {
            case R.id.checkbox_alpha:
                editor.putBoolean(SHOW_ALPHA, isChecked);
                break;
            case R.id.checkbox_asamblea:
                editor.putBoolean(SHOW_ASAMBLEA, isChecked);
                break;
            case R.id.checkbox_aviso:
                editor.putBoolean(SHOW_AVISO, isChecked);
                break;
            case R.id.checkbox_bravo:
                editor.putBoolean(SHOW_BRAVO, isChecked);
                break;
            case R.id.checkbox_cuap:
                editor.putBoolean(SHOW_CUAP, isChecked);
                break;
            case R.id.checkbox_hospital:
                editor.putBoolean(SHOW_HOSPITAL, isChecked);
                break;
            case R.id.checkbox_mike:
                editor.putBoolean(SHOW_MIKE, isChecked);
                break;
        }
        editor.commit();
        drawMarkers(null);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getPosition() == null || !mGoogleMap.isMyLocationEnabled()) {
            return;
        }
        if (locationServicesAreEnabled()) {
            if (mLocationClient.getLastLocation() != null) {
                Bundle args = new Bundle();
                args.putDouble(DirectionsLoader.ARG_ORIGIN_LAT, mLocationClient.getLastLocation()
                        .getLatitude());
                args.putDouble(DirectionsLoader.ARG_ORIGIN_LNG, mLocationClient.getLastLocation()
                        .getLongitude());
                args.putDouble(DirectionsLoader.ARG_DESTINATION_LAT,
                        marker.getPosition().latitude);
                args.putDouble(DirectionsLoader.ARG_DESTINATION_LNG,
                        marker.getPosition().longitude);
                getLoaderManager().restartLoader(LOADER_DIRECTIONS, args,
                        new DirectionsLoaderHelper());
            } else {
                Toast.makeText(getActivity(), R.string.locating, Toast.LENGTH_SHORT).show();
            }
        } else {
            showLocationSettings();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), R.string.location_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = ((LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.map_marker, null);

            ((TextView) v.findViewById(R.id.location)).setText(marker
                    .getTitle());

            if (marker.getSnippet() != null) {
                String address = marker.getSnippet().substring(0,
                        marker.getSnippet().indexOf(Location.MARKER_NEW_LINE));
                String other = marker.getSnippet().substring(
                        marker.getSnippet().indexOf(Location.MARKER_NEW_LINE)
                                + Location.MARKER_NEW_LINE.length(),
                        marker.getSnippet().length());
                ((TextView) v.findViewById(R.id.address)).setText(address);
                ((TextView) v.findViewById(R.id.other_information))
                        .setText(other);
            }

            return v;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class QueryListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextChange(String newText) {
            drawMarkers(newText);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            drawMarkers(query);
            return false;
        }
    }

    private class DirectionsLoaderHelper implements LoaderManager.LoaderCallbacks<String> {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_DIRECTIONS) {
                return new DirectionsLoader(getActivity(), args);
            } else {
                return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String response) {
            if (loader.getId() == LOADER_DIRECTIONS) {
                drawLine(JSONParser.parseDirections(response));
            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    /*
     * Mandatory overrides for the google location services interface. Nothing to do here.
     */
    @Override
    public void onConnected(Bundle bundle) {
        //Nothing to do here.
    }

    @Override
    public void onDisconnected() {
        //Nothing to do here.
    }
}
