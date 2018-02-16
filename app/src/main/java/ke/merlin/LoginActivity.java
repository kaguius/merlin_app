package ke.merlin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import ke.merlin.dao.users.UsersDao;
import ke.merlin.models.users.Users;
import ke.merlin.modules.password.ChangePasswordActivity;
import ke.merlin.modules.setup.RegistrationActivity;
import ke.merlin.services.IntentServiceReceiver;
import ke.merlin.services.LoginIntentService;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.SessionManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, IntentServiceReceiver.Receiver {

    Button btn_login;
    TextView txt_years, txt_forgot_passwd;
    CheckBox chk_show_password;
    EditText txtusername, txtpassword;

    ProgressDialog pDialog;

    private IntentServiceReceiver mReceiver;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        File database = getApplicationContext().getDatabasePath("merlin.db");
        if (!database.exists()) {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setListeners();
    }


    private void initViews() {

        txtusername = (EditText) findViewById(R.id.username);
        txtpassword = (EditText) findViewById(R.id.password);
        txt_forgot_passwd = (TextView) findViewById(R.id.txt_forgot_passwd);
        chk_show_password = (CheckBox) findViewById(R.id.chk_show_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        txt_years = (TextView) findViewById(R.id.txt_years);
        txt_years.setText("Merlin © 2014 - " + Calendar.getInstance().get(Calendar.YEAR));

        mReceiver = new IntentServiceReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    private void setListeners() {
        txt_forgot_passwd.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        chk_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    txtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                attemptLogin();
                break;

            case R.id.txt_forgot_passwd:
//                Intent intent1 = new Intent(LoginActivity.this, SyncUsersActivity.class);
//                startActivity(intent1);
                break;

        }

    }

    private void attemptLogin() {

        // Reset errors.
        txtusername.setError(null);
        txtpassword.setError(null);

        // Store values at the time of the login attempt.
        String email = txtusername.getText().toString();
        String password = txtpassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            txtpassword.setError(getString(R.string.error_invalid_password));
            focusView = txtpassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            txtusername.setError(getString(R.string.error_field_required));
            focusView = txtusername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            initializeService(email, password);

        }
    }

    private void initializeService(String username, String password) {

        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, LoginIntentService.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 105);
        startService(intent);

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
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
            case LoginIntentService.STATUS_RUNNING:
                pDialog = new ProgressDialog(LoginActivity.this);
                pDialog.setMessage("Processing...");
                pDialog.show();

                break;
            case LoginIntentService.STATUS_FINISHED:
                pDialog.hide();
                String results = resultData.getString("result");
                Log.d("Results ", results);

                String[] pieces = results.split("\\|");
                Log.d("Response State ", pieces[0]);

                if (pieces[0].equals("SUCCESS")) {
                    String usersData = resultData.getString("usersData");
                    Log.i("Data converted: ", usersData);
                    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

                    Users users = gson.fromJson(usersData, Users.class);

                    if (users.getReset() == 0) {

                        UsersDao usersDao = new UsersDao();
                        usersDao.insert(users);

                        session.createLoginSession(users);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                        startActivity(intent);
                        finish();

                    }

                } else {
                    View focusView;
                    txtpassword.setError(results);
                    focusView = txtpassword;
                    focusView.requestFocus();
                }

                break;
            case LoginIntentService.STATUS_ERROR:
                pDialog.hide();
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }
}

