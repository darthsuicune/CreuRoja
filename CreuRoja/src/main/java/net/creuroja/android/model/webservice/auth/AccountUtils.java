package net.creuroja.android.model.webservice.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

/**
 * Created by lapuente on 18.06.14.
 */
public class AccountUtils {
	public static final String ACCOUNT_TYPE = "Creu Roja";
	public static final String AUTH_TOKEN_TYPE = "";

	public static Account getAccount(Context context) {
		return AccountManager.get(context).getAccountsByType(ACCOUNT_TYPE)[0];
	}

	public static void validateLogin(final Activity activity, final LoginManager entryPoint) {
		AccountManager accountManager = AccountManager.get(activity);
		MyAccountCallback callback = new MyAccountCallback(entryPoint);
		Handler handler = new MyAccountHandler();

		accountManager
				.getAuthTokenByFeatures(ACCOUNT_TYPE, AUTH_TOKEN_TYPE, null, activity, null, null,
						callback, handler);
	}

	public interface LoginManager {
		public void successfulLogin();

		public void failedLogin();
	}

	public static class MyAccountCallback implements AccountManagerCallback<Bundle> {
		private LoginManager mEntryPoint;

		public MyAccountCallback(LoginManager entryPoint) {
			mEntryPoint = entryPoint;
		}

		@Override
		public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
			LoginResponseTask response = new LoginResponseTask(accountManagerFuture, mEntryPoint);
			response.execute();
		}
	}

	public static class MyAccountHandler extends Handler {
	}

	public static class LoginResponseTask extends AsyncTask<Void, Void, String> {
		private LoginManager mEntryPoint;
		private AccountManagerFuture<Bundle> mAccountManagerFuture;

		public LoginResponseTask(AccountManagerFuture<Bundle> accountManagerFuture,
								 LoginManager entryPoint) {
			mEntryPoint = entryPoint;
			mAccountManagerFuture = accountManagerFuture;
		}

		@Override
		protected String doInBackground(Void... voids) {
			Bundle bundle;
			String authToken = null;
			try {
				bundle = mAccountManagerFuture.getResult();
				authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
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
				mEntryPoint.successfulLogin();
			}
		}
	}
}
