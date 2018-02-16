package ke.merlin.modules.setup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import ke.merlin.R;
import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.models.RegistrationsApi;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.UrlConstants;
import ke.merlin.utils.network.ServerResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegistrationActivity extends AppCompatActivity {

    EditText etRegName, etRegEmail, etRegUsername, etRegStation, etRegPassword;
    Button btnRegister, btnSync;
    RegistrationsApiDao registrations_api_dao;

    String regName = "", regEmail = "", regUsername = "", regStation = "", regPassword = "";

    public static final MediaType mediaType = MediaType.parse("application/json");

    private final OkHttpClient client = new OkHttpClient();

    View focusView = null;
    // Get a RequestQueue
    //RequestQueue queue;
    String TAG = "REGISTRATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Registration");

        initViews();
        setListeners();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initViews() {
        etRegName = (EditText) findViewById(R.id.etRegName);
        etRegEmail = (EditText) findViewById(R.id.etRegEmail);
        etRegUsername = (EditText) findViewById(R.id.etRegUsername);
        etRegStation = (EditText) findViewById(R.id.etRegStation);
        etRegPassword = (EditText) findViewById(R.id.etRegPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnSync = (Button) findViewById(R.id.btnSync);
    }

    private void setListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });
    }

    private void register() {

        resetErrors();
        getFieldsText();

        if (checkValidData()) {
            focusView.requestFocus();
        } else {
            Log.i("Sending data:", "sync started.");
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ProgressDialog pDialog = new ProgressDialog(RegistrationActivity.this);
                        pDialog.setMessage("Processing. Please be patient....");
                        pDialog.show();

                        sendRegistrationToServer(RegistrationActivity.this);

                        pDialog.hide();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private boolean checkValidData() {
        boolean cancel = false;

        // Check for valid data
        if (TextUtils.isEmpty(regName)) {
            etRegName.setError(getString(R.string.error_field_required));
            focusView = etRegName;
            cancel = true;
        }
        if (TextUtils.isEmpty(regEmail)) {
            etRegEmail.setError(getString(R.string.error_field_required));
            focusView = etRegEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(regUsername)) {
            etRegUsername.setError(getString(R.string.error_field_required));
            focusView = etRegUsername;
            cancel = true;
        }
        if (TextUtils.isEmpty(regStation)) {
            etRegStation.setError(getString(R.string.error_field_required));
            focusView = etRegStation;
            cancel = true;
        }
        if (TextUtils.isEmpty(regPassword)) {
            etRegPassword.setError(getString(R.string.error_field_required));
            focusView = etRegPassword;
            cancel = true;
        }

        return cancel;
    }

    private void getFieldsText() {
        // Store values at the time of the registration attempt.
        regName = etRegName.getText().toString();
        regEmail = etRegEmail.getText().toString();
        regUsername = etRegUsername.getText().toString();
        regStation = etRegStation.getText().toString();
        regPassword = etRegPassword.getText().toString();
    }

    private void resetErrors() {
        // Reset errors.
        etRegName.setError(null);
        etRegEmail.setError(null);
        etRegUsername.setError(null);
        etRegStation.setError(null);
        etRegPassword.setError(null);
    }

    private void sendRegistrationToServer(final Context context) {

        final RegistrationsApi registrationsApi = new RegistrationsApi(regName, regEmail, regUsername, regStation, regPassword);
        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).serializeNulls().create();
        final String requestBody = gson.toJson(registrationsApi);
        Log.i("Generated Json String:", requestBody);

        final String[] successerror = {"success"};

        if (checkIfHasNetwork()) {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody);
            Request request = new Request.Builder()
                    .url(UrlConstants.REGISTRATION_URL)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        call.cancel();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            final ServerResponse serverResponse = gson.fromJson(responseBody.string(), ServerResponse.class);
                            if (serverResponse.getSuccess()) {
                                registrations_api_dao = new RegistrationsApiDao();
                                registrations_api_dao.insert(registrationsApi);
                                Log.i("SQLITE: ", "Registered");

                                Intent intent = new Intent(context, FirsttimeSyncActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Log.i("error ", " error is " + serverResponse.getMessage());
                                successerror[0] = serverResponse.getMessage();

                                // Run view-related code back on the main thread
                                RegistrationActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        View focusView;
                                        EditText etRegUsername = (EditText) findViewById(R.id.etRegUsername);
                                        etRegUsername.setError(successerror[0]);
                                        focusView = etRegUsername;
                                        focusView.requestFocus();
                                    }
                                });

                            }
                        }
                    }
                });

        }else{
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            if (isCallable(intent)) {
                startActivity(intent);
            }else{
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        }
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
