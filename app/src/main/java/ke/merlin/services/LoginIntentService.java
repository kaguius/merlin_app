package ke.merlin.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.util.List;

import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.dao.users.UsersDao;
import ke.merlin.models.RegistrationsApi;
import ke.merlin.models.users.Users;
import ke.merlin.utils.UrlConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "LoginIntentService";

    String responseBody = "Failed";

    private OkHttpClient client;

    String username, password;

    public LoginIntentService() {
        super(LoginIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.i(TAG, "Login Service Started!");

            final ResultReceiver receiver = intent.getParcelableExtra("receiver");

            Bundle bundle = new Bundle();

            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                username = intent.getStringExtra("username");
                password = intent.getStringExtra("password");

                if (checkIfHasNetwork()) {

                Log.i("Username Password ", username + " " + password);

//                UsersDao usersDao = new UsersDao();
//                Users users = usersDao.getUserByUsername(username);
//
//                if (users != null) {
                    //Log.i("User found: ", users.getUsername());

                    client = new OkHttpClient.Builder().build();

                    RegistrationsApiDao registrationsApiDao = new RegistrationsApiDao();
                    RegistrationsApi registrationsApi = registrationsApiDao.getRegistration();
                    String regid = registrationsApi.getId();
                    String code = registrationsApi.getVerificationCode();

                    String credentials = regid + ":" + code;
                    String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                    String urlString = UrlConstants.UsersByUsername_URL + username + "/" + password;

                    Request request = new Request.Builder().url(urlString).addHeader("authorization", auth).build();

                    try {
                        Response response = client.newCall(request).execute();
                        Log.i("response code ", String.valueOf(response.code()) + response);
                        if (response.code() == 200) {
                            responseBody = "SUCCESS|SUCCESS";

                            String res = response.body().string();
                            bundle.putString("usersData", res);
                        }else{
                            responseBody = "FAILED|Username and password don't match. Try again";
                        }
                        response.close();
                    } catch (Exception e) {
                        Log.e("Error 1: ", String.valueOf(e));
                    }

//                } else {
//                    Log.i("User: ", "User not found:");
//                    responseBody = "FAILED|No such user. Try again";
//                }

                Log.i("User: ", responseBody);
                bundle.putString("result", responseBody);
                receiver.send(STATUS_FINISHED, bundle);

                } else {
                    intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    if (isCallable(intent)) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }
                }

            } catch (Exception e) {
                bundle.putString(Intent.EXTRA_TEXT, "FAILED|An error was encountered. Try again");
                e.printStackTrace();
                receiver.send(STATUS_ERROR, bundle);
            }

            Log.d(TAG, "Service Stopping!");
            this.stopSelf();
        }

    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
