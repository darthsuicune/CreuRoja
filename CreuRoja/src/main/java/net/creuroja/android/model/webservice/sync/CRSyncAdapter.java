package net.creuroja.android.model.webservice.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import net.creuroja.android.model.Settings;
import net.creuroja.android.model.locations.RailsLocationList;
import net.creuroja.android.model.locations.LocationList;
import net.creuroja.android.model.webservice.CRWebServiceClient;
import net.creuroja.android.model.webservice.ClientConnectionListener;
import net.creuroja.android.model.webservice.RailsWebServiceClient;
import net.creuroja.android.model.webservice.auth.AccountUtils;
import net.creuroja.android.model.webservice.lib.RestWebServiceClient;

import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by lapuente on 20.06.14.
 */
public class CRSyncAdapter extends AbstractThreadedSyncAdapter implements ClientConnectionListener {
	private static final String SYNC_ADAPTER_TAG = "CreuRoja SyncAdapter";
	private final AccountManager mAccountManager;
	Context mContext;
	SharedPreferences prefs;

	public CRSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
		mAccountManager = AccountManager.get(context);
	}

	/**
	 * @param account               The account that triggers the sync
	 * @param extras                Bundle with flags sent by the event
	 * @param authority             Authority of the content provider
	 * @param contentProviderClient Used for connection with the ContentProvider
	 * @param syncResult            Object to send information back to the sync framework
	 */
	@Override public void onPerformSync(Account account, Bundle extras, String authority,
										ContentProviderClient contentProviderClient,
										SyncResult syncResult) {
		ConnectivityManager connectivity =
				(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity.getActiveNetworkInfo().isAvailable() &&
			connectivity.getActiveNetworkInfo().isConnected()) {
			try {
				prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
				RestWebServiceClient restClient =
						new RestWebServiceClient(RailsWebServiceClient.PROTOCOL,
								RailsWebServiceClient.URL);
				CRWebServiceClient client = new RailsWebServiceClient(restClient, this);

				String accessToken = mAccountManager
						.blockingGetAuthToken(account, AccountUtils.AUTH_TOKEN_TYPE, true);

				String lastUpdateTime = prefs.getString(Settings.LAST_UPDATE_TIME, "0");
				client.getLocations(accessToken, lastUpdateTime);
			} catch (OperationCanceledException e) {
				Log.i(SYNC_ADAPTER_TAG, "Synchronization cancelled by the user");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d(SYNC_ADAPTER_TAG, "There was an error while getting the Auth token");
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				Log.e(SYNC_ADAPTER_TAG, "Error authenticating");
				e.printStackTrace();
			}
		}
	}

	@Override public void onValidResponse(HttpResponse response) {
		LocationList locationList = new RailsLocationList(response);
		locationList.save(mContext.getContentResolver());

		prefs.edit().putString(Settings.LAST_UPDATE_TIME, locationList.getLastUpdateTime())
				.apply();
	}

	@Override public void onUnauthorized() {
		Log.d(SYNC_ADAPTER_TAG, "You are unauthorized.");
		Settings.clean(prefs, mContext.getContentResolver());
	}

	@Override public void onServerError() {
		Log.d(SYNC_ADAPTER_TAG, "Server error");
	}
}
