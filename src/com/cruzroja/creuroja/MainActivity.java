package com.cruzroja.creuroja;

import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends FragmentActivity {
	private static final int LOADER_CONNECTION = 1;
	SupportMapFragment mMapFragment;
	GoogleMap mGoogleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(isConnected()){
			downloadData();
		}
		
		setContentView(R.layout.activity_main);
		
		setMap();
		configureMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private boolean isConnected(){
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		return manager.getActiveNetworkInfo().isConnected();
	}
	
	private void downloadData(){
		getSupportLoaderManager().restartLoader(LOADER_CONNECTION, null, new ConnectionHelper());
	}
	
	private void setMap(){
		mMapFragment = new SupportMapFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(0, mMapFragment); //TODO switch 0 for containerViewID
		transaction.commit();
	}
	
	private void configureMap(){
		mGoogleMap = mMapFragment.getMap();
		//TODO configure map options
	}
	
	private void setExtraMapElements(ArrayList<String> elementsList){
		
	}
	
	private void showProgress(boolean show){
		if(show){
			
		} else {
			
		}
	}
	
	private class ConnectionHelper implements LoaderCallbacks<ArrayList<String>>{

		@Override
		public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args) {
			Loader<ArrayList<String>> loader = null;
			switch(id){
			case LOADER_CONNECTION:
				showProgress(true);
				loader = new AsyncConnectionLoader(getApplicationContext(), args);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<ArrayList<String>> loader,
				ArrayList<String> arrayList) {
			switch(loader.getId()){
			case LOADER_CONNECTION:
				setExtraMapElements(arrayList);
				showProgress(false);
				break;
			}
		}

		@Override
		public void onLoaderReset(Loader<ArrayList<String>> loader) {
		}
	}
}
