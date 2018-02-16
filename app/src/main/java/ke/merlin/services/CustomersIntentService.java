package ke.merlin.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;

import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.models.RegistrationsApi;
import ke.merlin.utils.MyDateTypeAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CustomersIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    String id, station, role;


    private static final String TAG = "CustomersIntentService";

    public CustomersIntentService() {
        super(LeadsIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "Load customers Service Started!");

            final ResultReceiver receiver = intent.getParcelableExtra("receiver");

            Bundle bundle = new Bundle();

            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                id = intent.getStringExtra("id");
                station = intent.getStringExtra("station");
                role = intent.getStringExtra("role");

                String results = "No contents";

                ArrayList<Customers> customersList = CustomersDao.getAllCustomers(id, station, role);
                if (customersList != null){
                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
                    results = gson.toJson(customersList);
                }

                Log.i("Results ", results);

                bundle.putString("result", results);
                receiver.send(STATUS_FINISHED, bundle);

            } catch (Exception e) {

                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }

            Log.d(TAG, "Service Stopping!");
            this.stopSelf();

        }
    }

}
