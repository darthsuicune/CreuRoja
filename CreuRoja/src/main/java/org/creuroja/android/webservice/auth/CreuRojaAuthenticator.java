package org.creuroja.android.webservice.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import org.creuroja.android.view.activities.LoginActivity;

/**
 * Created by lapuente on 16.06.14.
 * <p/>
 * More info on
 * http://developer.android.com/reference/android/accounts/AbstractAccountAuthenticator.html
 * http://udinic.wordpress.com/2013/04/24/write-your-own-android-authenticator/
 */
public class CreuRojaAuthenticator extends AbstractAccountAuthenticator {
	private Context mContext;

	public CreuRojaAuthenticator(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * @param response         to send the result back to the AccountManager, will never be null
	 * @param accountType      type of account to add, will never be null
	 * @param authTokenType    type of auth token to retrieve after adding the account, can be null
	 * @param requiredFeatures features the account must support, can be null
	 * @param options          options for the authenticator, can be null
	 * @return a Bundle result or null if the result is to be returned via the response.
	 * The result will contain either:
	 * KEY_INTENT, or
	 * KEY_ACCOUNT_NAME and KEY_ACCOUNT_TYPE of the account that was added, or
	 * KEY_ERROR_CODE and KEY_ERROR_MESSAGE to indicate an error
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
							 String authTokenType, String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		final AccountManager manager = AccountManager.get(mContext);
		if(manager.getAccountsByType(AccountUtils.ACCOUNT_TYPE).length > 0) {
			Toast.makeText(mContext, "Test", Toast.LENGTH_LONG).show();
			return null;
		}
		return prepareAuthenticatorActivity(response, accountType, authTokenType, true);
	}

	/**
	 * @param response      to send the result back to the AccountManager, will never be null
	 * @param account       the account whose credentials are to be retrieved, will never be null
	 * @param authTokenType the type of auth token to retrieve, will never be null
	 * @param options       a Bundle of authenticator-specific options, may be null
	 * @return a Bundle result or null if the result is to be returned via the response.
	 * The result will contain either:
	 * KEY_INTENT, or
	 * KEY_ACCOUNT_NAME and KEY_ACCOUNT_TYPE of the account that was added, or
	 * KEY_ERROR_CODE and KEY_ERROR_MESSAGE to indicate an error
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
							   String authTokenType, Bundle options) throws NetworkErrorException {

		final AccountManager manager = AccountManager.get(mContext);
		//Try to get an existing token
		String authToken = manager.peekAuthToken(account, authTokenType);

		Bundle result;
		//If there is none, launch the login activity
		if (TextUtils.isEmpty(authToken)) {
			result = prepareAuthenticatorActivity(response, account.type, authTokenType, false);
		} else {
			result = new Bundle();
			result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
			result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
			result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
		}
		return result;
	}

	/**
	 * @param response    used to set the result for the request. If the Constants.INTENT_KEY is set
	 *                    in the bundle then this response field is to be used for sending future results if and
	 *                    when the Intent is started.
	 * @param accountType type of account
	 * @return a Bundle containing the result or the Intent to start to continue the request. If
	 * this is null then the request is considered to still be active and the result should
	 * sent later using response
	 */
	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		return null;
	}

	/**
	 * @param response to send the result back to the AccountManager, will never be null
	 * @param account  the account whose credentials are to be checked, will never be null
	 * @param options  a Bundle of authenticator-specific options, may be null
	 * @return a Bundle result or null if the result is to be returned via the response.
	 * The result will contain either:
	 * KEY_INTENT, or
	 * KEY_ACCOUNT_NAME and KEY_ACCOUNT_TYPE of the account that was added, or
	 * KEY_ERROR_CODE and KEY_ERROR_MESSAGE to indicate an error
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
									 Bundle options) throws NetworkErrorException {
		return null;
	}

	/**
	 * @param authTokenType the authTokenType whose label is to be returned, will never be null
	 * @return the localized label of the auth token type, may be null if the type isn't known
	 */
	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	/**
	 * @param response      to send the result back to the AccountManager, will never be null
	 * @param account       the account whose credentials are to be updated, will never be null
	 * @param authTokenType the type of auth token to retrieve after updating, may be null
	 * @param options       a Bundle of authenticator-specific options, may be null
	 * @return An AccountManagerFuture which resolves to a Bundle with these fields if an activity
	 * was supplied and the account credentials were successfully updated:
	 * KEY_ACCOUNT_NAME - the name of the account created
	 * KEY_ACCOUNT_TYPE - the type of the account
	 * If no activity was specified, the returned Bundle contains only KEY_INTENT with
	 * the Intent needed to launch the password prompt. If an error occurred, getResult()
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
									String authTokenType, Bundle options)
			throws NetworkErrorException {
		return null;
	}

	/**
	 * @param response to send the result back to the AccountManager, will never be null
	 * @param account  the account to check, will never be null
	 * @param features an array of features to check, will never be null
	 * @return a Bundle result or null if the result is to be returned via the response.
	 * The result will contain either:
	 * KEY_INTENT, or
	 * KEY_ACCOUNT_NAME and KEY_ACCOUNT_TYPE of the account that was added, or
	 * KEY_ERROR_CODE and KEY_ERROR_MESSAGE to indicate an error
	 * @throws NetworkErrorException
	 */
	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
							  String[] features) throws NetworkErrorException {
		return null;
	}

	private Bundle prepareAuthenticatorActivity(AccountAuthenticatorResponse response,
												String accountType, String authType,
												boolean isNewAccount) {
		final Bundle result = new Bundle();
		final Intent intent = new Intent(mContext, LoginActivity.class);
		intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
		intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authType);
		intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, isNewAccount);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		result.putParcelable(AccountManager.KEY_INTENT, intent);
		return result;
	}
}
