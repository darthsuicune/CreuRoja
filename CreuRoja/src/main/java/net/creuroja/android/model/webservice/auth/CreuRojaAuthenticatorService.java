package net.creuroja.android.model.webservice.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CreuRojaAuthenticatorService extends Service {
	private CreuRojaAuthenticator mAuthenticator;
    public CreuRojaAuthenticatorService() {
    }

	@Override
	public void onCreate() {
		this.mAuthenticator = new CreuRojaAuthenticator(this);
	}

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
