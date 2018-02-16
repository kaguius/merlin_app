package ke.merlin.modules.customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ke.merlin.R;
import ke.merlin.models.customers.Customers;
import ke.merlin.services.CustomersIntentService;
import ke.merlin.services.IntentServiceReceiver;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.SessionManager;

public class CustomerActivity extends AppCompatActivity implements IntentServiceReceiver.Receiver{

    private RecyclerView mRecyclerView;
    private ArrayList<Customers> mArrayList;
    private CustomerDataAdapter mAdapter;

    ProgressDialog pDialog;

    private IntentServiceReceiver mReceiver;

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Customers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        initViews();

        if (savedInstanceState == null) {
            initializeService();
        }else{
            String values = savedInstanceState.getString("savedArray");
            mArrayList = gson.fromJson(values, new TypeToken<List<Customers>>() {}.getType());
            mAdapter = new CustomerDataAdapter(this, mArrayList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        if (mArrayList != null) {
            String values = gson.toJson(mArrayList);
            savedState.putString("savedArray", values);
        }
    }


    private void initViews(){
        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mReceiver = new IntentServiceReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    private void initializeService() {
        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(SessionManager.KEY_ID);
        String station = user.get(SessionManager.KEY_STATION);
        String role = user.get(SessionManager.KEY_ROLES);

        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, CustomersIntentService.class);
        intent.putExtra("id", id);
        intent.putExtra("station", station);
        intent.putExtra("role", role);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 103);
        startService(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mAdapter != null) mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver.setReceiver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReceiver.setReceiver(null);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case CustomersIntentService.STATUS_RUNNING:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Processing...");
                pDialog.show();

                break;
            case CustomersIntentService.STATUS_FINISHED:
                pDialog.hide();
                String results = resultData.getString("result");
                if (results.equals("Failed")) {
                    Toast.makeText(CustomerActivity.this, "Failed to get data.", Toast.LENGTH_LONG).show();
                }else if (results.equals("No contents")){
                    Toast.makeText(CustomerActivity.this, "No leads found.", Toast.LENGTH_LONG).show();
                }else {
                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
                    mArrayList = gson.fromJson(results, new TypeToken<List<Customers>>() {
                    }.getType());
                    mAdapter = new CustomerDataAdapter(this, mArrayList);
                    mRecyclerView.setAdapter(mAdapter);
                }

                break;
            case CustomersIntentService.STATUS_ERROR:
                pDialog.hide();
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
