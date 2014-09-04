package net.creuroja.android.vehicletracking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.creuroja.android.vehicletracking.R;
import net.creuroja.android.vehicletracking.fragments.TrackingFragment;


public class TrackingActivity extends ActionBarActivity
		implements TrackingFragment.OnTrackingFragmentInteractionListener {
	public static final String TAG_TRACKING_FRAGMENT = "trackingFragment";
	public static final String KEY_PERMANENT_NOTIFICATION = "permanentNotification";
	private static final String KEY_FINISHED_NOTIFICATION = "finishedNotification";

	private OnNotificationReceivedListener mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey(KEY_PERMANENT_NOTIFICATION)) {
				mListener.onNotificationReceived(TrackingFragment.NOTIFICATION_PERMANENT);
			} else if (extras.containsKey(KEY_FINISHED_NOTIFICATION)) {
				mListener.onNotificationReceived(TrackingFragment.NOTIFICATION_FINISHED);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tracking, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_settings:
				openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override public void onTrackingStarted() {
		//Nothing to do for now
	}

	@Override public void onTrackingStopped() {
		//Nothing to do for now
	}
}
