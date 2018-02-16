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

import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.models.RegistrationsApi;
import ke.merlin.models.customers.Customers;
import ke.merlin.utils.MyDateTypeAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LeadsIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static final String TAG = "LeadsIntentService";
    String id, station, role;
    private OkHttpClient client;

    public LeadsIntentService() {
        super(LeadsIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "Load leads Service Started!");

            final ResultReceiver receiver = intent.getParcelableExtra("receiver");

            Bundle bundle = new Bundle();

            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                id = intent.getStringExtra("id");
                station = intent.getStringExtra("station");
                role = intent.getStringExtra("role");

//                String results = getHttpResponse(url);
                String results = "No contents";

                ArrayList<Customers> leadList = CustomersDao.getAllLeads(id, station, role);
                if (leadList != null){
                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
                    results = gson.toJson(leadList);
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

    public String getHttpResponse(String url) {

        String responseBody = "Failed";

       if (checkIfHasNetwork()){

           client = new OkHttpClient.Builder().build();

           RegistrationsApiDao registrationsApiDao = new RegistrationsApiDao();
           RegistrationsApi registrationsApi = registrationsApiDao.getRegistration();
           String regid = registrationsApi.getId();
           String code = registrationsApi.getVerificationCode();

           String credentials = regid + ":" + code;
           String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

           String urlString = url + id + "/" + station;

           Request request = new Request.Builder().url(urlString).addHeader("authorization", auth).build();
           try {
               Response response = client.newCall(request).execute();
               if (response.code() == 200) {
                   responseBody = response.body().string();

                   CustomersDao leadsDao = new CustomersDao();
                   leadsDao.delete();
                   leadsDao.insertList(responseBody);
               }

               if (response.code() == 204) {
                   responseBody = "No contents";
               }
               response.close();
           } catch (Exception e) {
               Log.e("Error 1: ", e.getMessage());
           }
       }else{

           ArrayList<Customers> leadList = CustomersDao.getAllCustomers(id, station, role);
           if (leadList != null){
               Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
               responseBody = gson.toJson(leadList);
           }else{
               responseBody = "No contents";
           }

       }

        return responseBody;
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
