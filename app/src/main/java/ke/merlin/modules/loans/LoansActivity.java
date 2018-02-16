package ke.merlin.modules.loans;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ke.merlin.R;
import ke.merlin.models.loans.Loans;
import ke.merlin.models.sms.SmsOutgoing;
import ke.merlin.modules.sms.SmsActivity;
import ke.merlin.modules.sms.SmsDataAdaptor;
import ke.merlin.utils.AuthDetails;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.UrlConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoansActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Loans> mArrayList;
    private LoansdataAdaptor mAdapter;

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Customer Loans");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");

        } else {
            id = (String) savedInstanceState.getSerializable("id");
        }

        initViews();

    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LoansActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);

        sendRequest(id);

    }

    private void sendRequest(String id) {

        final ProgressDialog pDialog = new ProgressDialog(LoansActivity.this);
        pDialog.setMessage("Processing...");
        pDialog.show();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(UrlConstants.LoansByCustomer_URL + id)
                .addHeader("authorization", AuthDetails.getAuth())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        String responseBody = response.body().string();
                        if (responseBody != null && !responseBody.equals("")) {
                            Log.i(" responseBody ", responseBody.substring(0, 1));

                            final ArrayList<Loans> list = gson.fromJson(responseBody, new TypeToken<List<Loans>>() {
                            }.getType());

                            LoansActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mArrayList = list;
                                    mAdapter = new LoansdataAdaptor(mArrayList);
                                    mRecyclerView.setAdapter(mAdapter);

                                    pDialog.hide();

                                }
                            });

                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("id", id);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id = savedInstanceState.getString("id");
    }

}
