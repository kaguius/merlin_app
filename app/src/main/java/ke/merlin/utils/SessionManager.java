package ke.merlin.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import ke.merlin.LoginActivity;
import ke.merlin.models.users.Users;
import ke.merlin.modules.password.ChangePasswordActivity;


/**
 * Created by mecmurimi on 21/03/2017.
 */

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoggedUserPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LANGUAGE = "language_id";
    public static final String KEY_CC_CAMPAIGN = "cc_campaign";
    public static final String KEY_CC_LIST = "cc_list";
    public static final String KEY_ROLES = "roles_id";
    public static final String KEY_STATION = "station_id";
    public static final String KEY_PHOTO = "photo_path";

    //Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create loggin session
     */

    public void createLoginSession(Users users) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_ID, String.valueOf(users.getId()));
        editor.putString(KEY_FIRST_NAME, users.getFirstName());
        editor.putString(KEY_LAST_NAME, users.getLastName());
        editor.putString(KEY_USERNAME, users.getUsername());
        editor.putString(KEY_EMAIL, users.getEmail());
        editor.putString(KEY_LANGUAGE, users.getLanguageId());
        editor.putString(KEY_CC_CAMPAIGN, users.getCcCampaign());
        editor.putString(KEY_CC_LIST, users.getCcList());
        editor.putString(KEY_ROLES, users.getRolesId());
        editor.putString(KEY_STATION, users.getStationId());
        editor.putString(KEY_PHOTO, users.getPhotoPath());

        editor.commit();
    }

    /**
     * Check login method will check user login status
     * if false, it will redirect user to login page
     * else won't do anything
     */

    public void checkLogin() {

        //check login status
        if (!this.isLoggedIn()) {

            //user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            //closing all activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //add new flag to start new activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        //Get values
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, null));
        user.put(KEY_LAST_NAME, pref.getString(KEY_LAST_NAME, null));
        user.put(KEY_STATION, pref.getString(KEY_STATION, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_LANGUAGE, pref.getString(KEY_LANGUAGE, null));
        user.put(KEY_CC_CAMPAIGN, pref.getString(KEY_CC_CAMPAIGN, null));
        user.put(KEY_CC_LIST, pref.getString(KEY_CC_LIST, null));
        user.put(KEY_ROLES, pref.getString(KEY_ROLES, null));
        user.put(KEY_STATION, pref.getString(KEY_STATION, null));
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null));

        return user;
    }

    /**
     * Clear session details
     */

    public void logoutUser() {

        //clearing all data from shared preferences
        editor.clear();
        editor.commit();

        //After logout redirect user to login activity

        Intent i = new Intent(context, LoginActivity.class);

        //closing all the activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Add new flag to start new acticity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //starting new login activity
        context.startActivity(i);


    }

    public void changepassword() {

        //clearing all data from shared preferences
        editor.clear();
        editor.commit();

        //After logout redirect user to login activity

        Intent i = new Intent(context, ChangePasswordActivity.class);

        //closing all the activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Add new flag to start new acticity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //starting new login activity
        context.startActivity(i);

    }

    /**
     * Quick check for login
     * <p>
     * Get login state
     */

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
