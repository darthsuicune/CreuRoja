package net.creuroja.android.controller.general;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.creuroja.android.R;
import net.creuroja.android.model.webservice.CRWebServiceClient;
import net.creuroja.android.model.webservice.ClientConnectionListener;
import net.creuroja.android.model.webservice.LoginResponse;
import net.creuroja.android.model.webservice.RailsLoginResponse;
import net.creuroja.android.model.webservice.RailsWebServiceClient;
import net.creuroja.android.model.webservice.auth.AccountUtils;
import net.creuroja.android.model.webservice.lib.RestWebServiceClient;

import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AccountAuthenticatorActivity implements LoaderCallbacks<Cursor> {
	public static final int E_MAIL_AUTO_COMPLETE_LOADER = 0;
	public static final String ARG_ACCOUNT_TYPE = "accountType";
	public static final String ARG_AUTH_TYPE = "authType";
	public static final String ARG_IS_ADDING_NEW_ACCOUNT = "is adding new account";

	// Keep track of the login task to ensure we can cancel it if requested.
	private UserLoginTask mAuthTask = null;

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		populateAutoComplete();

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
	}

	private void populateAutoComplete() {
		getLoaderManager().initLoader(E_MAIL_AUTO_COMPLETE_LOADER, null, this);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;


		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_user));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(email, password);
			mAuthTask.execute((Void) null);
		}
	}

	private boolean isEmailValid(String email) {
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		return password.length() > 2;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		Uri uri = null;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		switch (id) {
			case E_MAIL_AUTO_COMPLETE_LOADER:
				// Retrieve data rows for the device user's 'profile' contact.
				uri = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
				projection = ProfileQuery.PROJECTION;
				// Select only email addresses.
				selection = ContactsContract.Contacts.Data.MIMETYPE + " = ?";
				selectionArgs =
						new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				sortOrder = ContactsContract.Contacts.Data.IS_PRIMARY + " DESC";
				break;
			default:
				break;
		}
		return new CursorLoader(this, uri, projection, selection, selectionArgs, sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		switch (cursorLoader.getId()) {
			case E_MAIL_AUTO_COMPLETE_LOADER:
				List<String> emails = new ArrayList<>();
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					emails.add(cursor.getString(ProfileQuery.ADDRESS));
					cursor.moveToNext();
				}

				addEmailsToAutoComplete(emails);
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		//Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
		ArrayAdapter<String> adapter =
				new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line,
						emailAddressCollection);

		mEmailView.setAdapter(adapter);
	}


	private interface ProfileQuery {
		String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS,
							   ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Void>
			implements ClientConnectionListener {

		private final CRWebServiceClient mClient;
		private final String mEmail;
		private final String mPassword;
		private Intent intent;

		UserLoginTask(String email, String password) {
			mClient = new RailsWebServiceClient(
					new RestWebServiceClient(RailsWebServiceClient.PROTOCOL,
							RailsWebServiceClient.URL), this);
			mEmail = email;
			mPassword = password;
			intent = new Intent();
		}

		@Override
		protected Void doInBackground(Void... params) {
			mClient.signInUser(mEmail, mPassword);
			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {
			mAuthTask = null;
			showProgress(false);

			if (intent.getBooleanExtra(LoginResponse.IS_VALID, false)) {
				successfulLogin(intent);
			} else {
				mPasswordView.setError(getString(R.string.error_invalid_password));
				mPasswordView.setText("");
				mPasswordView.requestFocus();
				Toast.makeText(getApplicationContext(),
						intent.getStringExtra(LoginResponse.ERROR_MESSAGE), Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

		private void successfulLogin(Intent intent) {
			String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			String accountPassword = intent.getStringExtra(AccountManager.KEY_PASSWORD);

			final AccountManager manager = AccountManager.get(LoginActivity.this);
			final Account account = new Account(accountName,
					intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

			if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
				String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
				String authTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
				// Creating the account on the device and setting the auth token we got
				manager.addAccountExplicitly(account, accountPassword, null);
				manager.setAuthToken(account, authTokenType, authToken);
			} else {
				manager.setPassword(account, accountPassword);
			}
			setAccountAuthenticatorResult(intent.getExtras());
			setResult(RESULT_OK);
			finish();
		}

		@Override public void onValidResponse(HttpResponse httpResponse) {
			LoginResponse response = new RailsLoginResponse(httpResponse);
			intent.putExtra(LoginResponse.IS_VALID, response.isValid());
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mEmail);
			intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountUtils.ACCOUNT_TYPE);
			intent.putExtra(AccountManager.KEY_AUTHTOKEN, response.authToken());
			intent.putExtra(AccountManager.KEY_PASSWORD, mPassword);
		}


		@Override public void onUnauthorized() {
			intent.putExtra(LoginResponse.IS_VALID, false);
			intent.putExtra(LoginResponse.ERROR_CODE, 401);
			intent.putExtra(LoginResponse.ERROR_MESSAGE, R.string.error_invalid_user);

		}

		@Override public void onServerError() {
			intent.putExtra(LoginResponse.IS_VALID, false);
			intent.putExtra(LoginResponse.ERROR_CODE, 500);
			intent.putExtra(LoginResponse.ERROR_MESSAGE, R.string.error_invalid_response);
		}
	}
}
