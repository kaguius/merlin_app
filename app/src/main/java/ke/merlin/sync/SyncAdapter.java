package ke.merlin.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by mecmurimi on 08/11/2017.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private AccountManager mAccountManager;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("merlinsync;", "onPerformSync for account[" + account.name + "]");

        try{



        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
