package ke.merlin.modules.setup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ke.merlin.LoginActivity;
import ke.merlin.R;
import ke.merlin.services.FirsttimeSyncIntentService;
import ke.merlin.services.IntentServiceReceiver;

public class FirsttimeSyncActivity extends AppCompatActivity implements IntentServiceReceiver.Receiver, View.OnClickListener {

    String url = "url";
    Button btnDone, btnLogin;
    TextView txtDone;
    private IntentServiceReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sync);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Synchronize");

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        txtDone = (TextView) findViewById(R.id.txtDone);

        mReceiver = new IntentServiceReceiver(new Handler());
        mReceiver.setReceiver(this);

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case FirsttimeSyncIntentService.STATUS_RUNNING:
                txtDone.setText("Processing...");
                btnDone.setVisibility(View.GONE);
                break;
            case FirsttimeSyncIntentService.STATUS_FINISHED:
                Toast.makeText(this, "service finished", Toast.LENGTH_LONG).show();
                txtDone.setText("Completed.");
                btnDone.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);

                break;
            case FirsttimeSyncIntentService.STATUS_ERROR:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                txtDone.setText("Error: " + error);
                btnDone.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                break;
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                Log.i("Sync", "sync started");

                Intent intent = new Intent(Intent.ACTION_SYNC, null, this, FirsttimeSyncIntentService.class);
                intent.putExtra("url", url);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("requestId", 101);
                startService(intent);

                txtDone.setText("Service started");
                break;

            case R.id.btnLogin:
                Intent intent1 = new Intent(FirsttimeSyncActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
        }


    }
}
