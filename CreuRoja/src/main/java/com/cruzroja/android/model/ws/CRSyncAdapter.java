package com.cruzroja.android.model.ws;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.cruzroja.android.app.Settings;

/**
 * Created by lapuente on 20.06.14.
 */
public class CRSyncAdapter extends AbstractThreadedSyncAdapter {
	Context mContext;

	public CRSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
	}

	/**
	 *
	 * @param account The account that triggers the sync
	 * @param extras Bundle with flags sent by the event
	 * @param authority Authority of the content provider
	 * @param contentProviderClient Used for connection with the ContentProvider
	 * @param syncResult Object to send information back to the sync framework
	 */
	@Override public void onPerformSync(Account account, Bundle extras, String authority,
										ContentProviderClient contentProviderClient,
										SyncResult syncResult) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		WebServiceClient client = new CRWebServiceClient();

		String lastUpdateTime = prefs.getString(Settings.LAST_UPDATE_TIME, "0");
		LocationList locationList = client.getLocations(lastUpdateTime);
		lastUpdateTime = locationList.save(mContext.getContentResolver());

		prefs.edit().putString(Settings.LAST_UPDATE_TIME, lastUpdateTime).commit();

	}
}
