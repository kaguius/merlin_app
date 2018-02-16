package ke.merlin.utils.network;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.models.RegistrationsApi;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class VolleyGetTransactional extends Thread {

    private static Context mCtx;
    String tag;
    String url;
    String table_name;
    String id;

    public VolleyGetTransactional(String tag, String url, String table_name, String id ,Context context) {
        this.tag = tag;
        this.url = url;
        this.table_name = table_name;
        this.id = id;
        mCtx = context;
    }

    @Override
    public void run() {

        StringRequest strReq = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response " + table_name, response);

//                if (table_name.equals("leads")) {
//                    LeadsDao leadsDao = new LeadsDao();
//                    long res = leadsDao.insertList(response);
//                    Log.i("Leads Result: ", String.valueOf(res) + " " + url);
//                }

                if (table_name.equals("customers")) {
                    CustomersDao smsOutgoingDao = new CustomersDao();
                    long res = smsOutgoingDao.insertList(response);
                    Log.i("Customers Result: ", String.valueOf(res) + " " + url);
                }



            }
        },
                new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "error" + error.networkResponse.statusCode;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(mCtx.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    mCtx.getApplicationContext().startActivity(intent);
                }
                message = error.toString();
                Log.e("Error", message);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                RegistrationsApiDao registrationsApiDao = new RegistrationsApiDao();
                RegistrationsApi registrationsApi = registrationsApiDao.getRegistration();
                String id = registrationsApi.getId();
                String code = registrationsApi.getVerificationCode();

                String credentials = id + ":" + code;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        strReq.setTag(tag);
        MySingleton.getInstance(mCtx).addToRequestQueue(strReq);

    }


}
