package org.creuroja.android.model.auth;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import org.creuroja.android.view.activities.LocationsIndexActivity;

/**
 * Created by lapuente on 18.06.14.
 */
public class AccountUtils {
	public static final String ACCOUNT_TYPE = "Creu Roja";
	public static final String AUTH_TOKEN_TYPE = "";

	private Context mContext;
	private LoginManager mEntryPoint;

	public AccountUtils(Context context, LoginManager entryPoint) {
		mContext = context;
		mEntryPoint = entryPoint;
	}

	public void getAuth(final LocationsIndexActivity activity) {
		AccountManager accountManager = AccountManager.get(activity);
		MyAccountCallback callback = new MyAccountCallback(mEntryPoint);
		Handler handler = new MyAccountHandler();

		accountManager
				.getAuthTokenByFeatures(ACCOUNT_TYPE, AUTH_TOKEN_TYPE, null, activity, null, null,
						callback, handler);

	}

	public interface LoginManager {
		public void successfulLogin(String authToken);

		public void failedLogin();
	}

	public static class MyAccountCallback implements AccountManagerCallback {
		private LoginManager mEntryPoint;

		public MyAccountCallback(LoginManager entryPoint) {
			mEntryPoint = entryPoint;
		}

		@Override
		public void run(AccountManagerFuture accountManagerFuture) {
			LoginResponse response = new LoginResponse(accountManagerFuture, mEntryPoint);
			response.execute();
		}
	}

	public static class MyAccountHandler extends Handler {

	}

	public static class LoginResponse extends AsyncTask<Void, Void, String> {
		private LoginManager mEntryPoint;
		private AccountManagerFuture<Bundle> mAccountManagerFuture;

		public LoginResponse(AccountManagerFuture accountManagerFuture, LoginManager entryPoint) {
			mEntryPoint = entryPoint;
			mAccountManagerFuture = accountManagerFuture;
		}

		@Override
		protected String doInBackground(Void... voids) {
			Bundle bnd;
			String authToken = null;
			try {
				bnd = mAccountManagerFuture.getResult();
				authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return authToken;
		}

		@Override
		protected void onPostExecute(String authToken) {
			super.onPostExecute(authToken);
			if (TextUtils.isEmpty(authToken)) {
				mEntryPoint.failedLogin();
			} else {
				mEntryPoint.successfulLogin(authToken);
			}
		}
	}
}
