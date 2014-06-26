package org.creuroja.android.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.creuroja.android.R;
import org.creuroja.android.app.LoginResponse;
import org.creuroja.android.app.Settings;
import org.creuroja.android.app.loaders.LoginLoader;
import org.creuroja.android.app.utils.ConnectionClient;
import org.creuroja.android.app.utils.LocationDownloader;

/**
 * Created by lapuente on 29.10.13.
 */
public class LoginFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<LoginResponse> {
	private static final int LOADER_LOGIN = 1;
	private EditText mEmailView, mPasswordView;

	public LoginFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		mEmailView = (EditText) v.findViewById(R.id.login_email);
		mPasswordView = (EditText) v.findViewById(R.id.login_password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});
		v.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		return v;
	}

	public void attemptLogin() {
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		Bundle args = new Bundle();
		args.putString(LoginLoader.EMAIL, email);
		args.putString(LoginLoader.PASSWORD, password);
		getLoaderManager().restartLoader(LOADER_LOGIN, args, this);
	}

	public void loginSuccessful(LoginResponse loginResponse) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		prefs.edit().putString(Settings.ACCESS_TOKEN, loginResponse.mToken.mAccessToken).commit();
		new LocationDownloader(loginResponse.mToken.mAccessToken,
							   getActivity().getContentResolver(), prefs)
				.saveLocations(loginResponse.mLocationList);
		getActivity().setResult(Activity.RESULT_OK);
		showProgress(false);
		getActivity().finish();
	}

	public void loginFailed(LoginResponse response) {
		mPasswordView.setText("");
		showProgress(false);
		if (response != null) {
			Toast.makeText(getActivity(), response.mErrorMessage, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		final View loginStatusView = getActivity().findViewById(R.id.login_status);
		final View loginFormView = getActivity().findViewById(R.id.login_form);
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			loginStatusView.setVisibility(View.VISIBLE);
			loginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			loginFormView.setVisibility(View.VISIBLE);
			loginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public Loader<LoginResponse> onCreateLoader(int id, Bundle args) {
		if (!ConnectionClient.isConnected(getActivity())) {
			Toast.makeText(getActivity(), R.string.error_no_connection, Toast.LENGTH_LONG).show();
		}
		showProgress(true);
		Loader<LoginResponse> loader = null;
		switch (id) {
			case LOADER_LOGIN:
				loader = new LoginLoader(getActivity(), args);
				break;
			default:
				break;
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<LoginResponse> loader, LoginResponse response) {
		switch (loader.getId()) {
			case LOADER_LOGIN:
				if (response != null && response.isValid()) {
					loginSuccessful(response);
				} else {
					loginFailed(response);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<LoginResponse> loader) {
	}
}