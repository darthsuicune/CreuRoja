package com.cruzroja.android.model.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by lapuente on 16.06.14.
 */
public class CreuRojaAuthenticator extends AbstractAccountAuthenticator {
	public CreuRojaAuthenticator(Context context) {
		super(context);
	}

	@Override public Bundle editProperties(
			AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
		return null;
	}

	@Override public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse,
									   String s, String s2, String[] strings, Bundle bundle)
			throws NetworkErrorException {
		return null;
	}

	@Override public Bundle confirmCredentials(
			AccountAuthenticatorResponse accountAuthenticatorResponse, Account account,
			Bundle bundle) throws NetworkErrorException {
		return null;
	}

	@Override public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse,
										 Account account, String s, Bundle bundle)
			throws NetworkErrorException {
		return null;
	}

	@Override public String getAuthTokenLabel(String s) {
		return null;
	}

	@Override public Bundle updateCredentials(
			AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s,
			Bundle bundle) throws NetworkErrorException {
		return null;
	}

	@Override public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse,
										Account account, String[] strings)
			throws NetworkErrorException {
		return null;
	}
}
