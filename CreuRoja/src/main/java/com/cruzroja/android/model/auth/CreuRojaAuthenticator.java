package com.cruzroja.android.model.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by lapuente on 16.06.14.
 *
 * More info on
 * http://developer.android.com/reference/android/accounts/AbstractAccountAuthenticator.html
 * http://udinic.wordpress.com/2013/04/24/write-your-own-android-authenticator/
 */
public class CreuRojaAuthenticator extends AbstractAccountAuthenticator {

	public CreuRojaAuthenticator(Context context) {
		super(context);
	}

	/**
	 *
	 * @param response used to set the result for the request. If the Constants.INTENT_KEY is set
	 * 		in the bundle then this response field is to be used for sending future results if and
	 *   	when the Intent is started.
	 * @param accountType type of account
	 * @return a Bundle containing the result or the Intent to start to continue the request. If
	 * 		this is null then the request is considered to still be active and the result should
	 * 		sent later using response
	 */
	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		return null;
	}

	/**
	 *
	 * @param response to send the result back to the AccountManager, will never be null
	 * @param accountType type of account to add, will never be null
	 * @param authTokenType type of auth token to retrieve after adding the account, can be null
	 * @param requiredFeatures features the account must support, can be null
	 * @param options options for the authenticator, can be null
	 * @return a Bundle result or null if the result is to be returned via the response.
	 * The result will contain either:
	 *		KEY_INTENT, or
	 * 		KEY_ACCOUNT_NAME and KEY_ACCOUNT_TYPE of the account that was added, or
	 * 		KEY_ERROR_CODE and KEY_ERROR_MESSAGE to indicate an error
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
							 String authTokenType, String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		return null;
	}

	/**
	 *
	 * @param response to send the result back to the AccountManager, will never be null
	 * @param account the account whose credentials are to be checked, will never be null
	 * @param options a Bundle of authenticator-specific options, may be null
	 * @return
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
									 Bundle options) throws NetworkErrorException {
		return null;
	}

	/**
	 *
	 * @param response to send the result back to the AccountManager, will never be null
	 * @param account the account whose credentials are to be retrieved, will never be null
	 * @param authTokenType the type of auth token to retrieve, will never be null
	 * @param options a Bundle of authenticator-specific options, may be null
	 * @return a Bundle result or null if the result is to be returned via the response.
	 * The result will contain either:
	 *		KEY_INTENT, or
	 * 		KEY_ACCOUNT_NAME and KEY_ACCOUNT_TYPE of the account that was added, or
	 * 		KEY_ERROR_CODE and KEY_ERROR_MESSAGE to indicate an error
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
							   String authTokenType, Bundle options) throws NetworkErrorException {
		return null;
	}

	/**
	 *
	 * @param authTokenType the authTokenType whose label is to be returned, will never be null
	 * @return the localized label of the auth token type, may be null if the type isn't known
	 */
	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	/**
	 *
	 * @param response to send the result back to the AccountManager, will never be null
	 * @param account the account whose credentials are to be updated, will never be null
	 * @param authTokenType the type of auth token to retrieve after updating, may be null
	 * @param options a Bundle of authenticator-specific options, may be null
	 * @return
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
									String authTokenType, Bundle options)
			throws NetworkErrorException {
		return null;
	}

	/**
	 *
	 * @param response to send the result back to the AccountManager, will never be null
	 * @param account the account to check, will never be null
	 * @param features an array of features to check, will never be null
	 * @return
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
							  String[] features) throws NetworkErrorException {
		return null;
	}
}
