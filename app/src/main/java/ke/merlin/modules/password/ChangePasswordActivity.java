package ke.merlin.modules.password;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import ke.merlin.LoginActivity;
import ke.merlin.R;
import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.models.RegistrationsApi;
import ke.merlin.modules.setup.FirsttimeSyncActivity;
import ke.merlin.modules.setup.RegistrationActivity;
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

public class ChangePasswordActivity extends AppCompatActivity {

    EditText txt_username_l, txt_old_password_l, txt_new_password_l, txt_confirm_password_l;
    Button btn_save_l;

    String username, old_password, new_password, confirm_password;

    View focusView = null;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Change Password");

        initViews();
        setListeners();

    }

    private void setListeners() {
        btn_save_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();

            }
        });
    }

    private void changePassword() {

        resetErrors();
        getFieldsText();

        if (checkValidData()) {
            focusView.requestFocus();
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final ProgressDialog pDialog = new ProgressDialog(ChangePasswordActivity.this);
                    pDialog.setMessage("Processing. Please be patient....");
                    pDialog.show();

                    sendToServer(ChangePasswordActivity.this);

                    pDialog.hide();
                }
            });

        }
    }

    private void sendToServer(final Context context) {
        if (checkIfHasNetwork()) {
            String urlString = UrlConstants.ChangePassword_URL + username + "/" + old_password + "/" + new_password;

            RegistrationsApiDao registrationsApiDao = new RegistrationsApiDao();
            RegistrationsApi registrationsApi = registrationsApiDao.getRegistration();
            String regid = registrationsApi.getId();
            String code = registrationsApi.getVerificationCode();

            String credentials = regid + ":" + code;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder().url(urlString).addHeader("authorization", auth).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);

                        if (response.code() == 200) {
                            String res = response.body().string();

                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            String results = response.body().string();

                            // Run view-related code back on the main thread
                            final String finalResults = results;
                            ChangePasswordActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChangePasswordActivity.this, finalResults, Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        response.close();
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

    private boolean checkValidData() {
        boolean cancel = false;

        if (TextUtils.isEmpty(username)) {
            txt_username_l.setError(getString(R.string.error_field_required));
            focusView = txt_username_l;
            cancel = true;
        }

        if (TextUtils.isEmpty(old_password)) {
            txt_old_password_l.setError(getString(R.string.error_field_required));
            focusView = txt_old_password_l;
            cancel = true;
        }

        if (TextUtils.isEmpty(new_password)) {
            txt_new_password_l.setError(getString(R.string.error_field_required));
            focusView = txt_new_password_l;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirm_password)) {
            txt_confirm_password_l.setError(getString(R.string.error_field_required));
            focusView = txt_confirm_password_l;
            cancel = true;
        }

        if (!new_password.equals(confirm_password)){
            txt_confirm_password_l.setError("New password and confirm password do not match.");
            focusView = txt_confirm_password_l;
            cancel = true;
        }

        if (old_password.equals(new_password)){
            txt_new_password_l.setError("New password and old password are the same. Choose a different one.");
            focusView = txt_new_password_l;
            cancel = true;
        }


        return cancel;

    }

    private void getFieldsText() {
        // Store values at the time of the registration attempt.
        username = txt_username_l.getText().toString();
        old_password = txt_old_password_l.getText().toString();
        new_password = txt_new_password_l.getText().toString();
        confirm_password = txt_confirm_password_l.getText().toString();
    }

    private void resetErrors() {
        // Reset errors.
        txt_username_l.setError(null);
        txt_old_password_l.setError(null);
        txt_new_password_l.setError(null);
        txt_confirm_password_l.setError(null);
    }

    private void initViews() {
        txt_username_l = (EditText) findViewById(R.id.txt_username_l);
        txt_old_password_l = (EditText) findViewById(R.id.txt_old_password_l);
        txt_new_password_l = (EditText) findViewById(R.id.txt_new_password_l);
        txt_confirm_password_l = (EditText) findViewById(R.id.txt_confirm_password_l);

        btn_save_l = (Button) findViewById(R.id.btn_save_l);


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
