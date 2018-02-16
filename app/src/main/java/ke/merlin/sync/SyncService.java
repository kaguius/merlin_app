package ke.merlin.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

    private static SyncAdapter sSyncAdapter = null;

    public SyncService() {
        super();
    }

    @Override
    public void onCreate() {
        if (sSyncAdapter == null)
            sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
