package com.cruzroja.android.model.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by lapuente on 16.06.14.
 */
public class CreuRojaAccount extends Account {
	public static final String ACCOUNT_TYPE = "Creu Roja";

	private CreuRojaAccount(String name) {
		super(name, ACCOUNT_TYPE);
	}

	public static boolean exists(Context context) {
		AccountManager manager = AccountManager.get(context);
		return manager.getAccountsByType(ACCOUNT_TYPE).length > 0;
	}

	public static CreuRojaAccount create(Activity activity, String name) {
		CreuRojaAccount account = new CreuRojaAccount(name);
		account.save(activity);
		return account;
	}

	public void save(Activity activity) {
		AccountManager manager = AccountManager.get(activity);
		String authTokenType = "";
		String[] requiredFeatures = {};
		Bundle addAccountOptions = new Bundle();
		AccountManagerCallback callback = new MyAccountManagerCallback();
		AccountHandler handler = new AccountHandler();
		manager.addAccount(ACCOUNT_TYPE, authTokenType, requiredFeatures, addAccountOptions,
				activity, callback, handler);
	}

	static class MyAccountManagerCallback implements AccountManagerCallback {
		@Override public void run(AccountManagerFuture accountManagerFuture) {

		}
	}

	static class AccountHandler extends Handler {

	}
}
