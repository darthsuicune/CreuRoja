package net.creuroja.android.model.webservice.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CRSyncService extends Service {
	private static final Object sSyncAdapterLock = new Object();
	private static CRSyncAdapter sSyncAdapter = null;
    public CRSyncService() {
    }

	@Override public void onCreate() {
		synchronized (sSyncAdapterLock) {
			if (sSyncAdapter == null) {
				sSyncAdapter = new CRSyncAdapter(getApplicationContext(), true);
			}
		}
		super.onCreate();

	}

	@Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
