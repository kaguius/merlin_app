package ke.merlin.utils;

import android.util.Base64;

import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.models.RegistrationsApi;

/**
 * Created by mecmurimi on 31/10/2017.
 */

public class AuthDetails {

    public static String getAuth(){

        RegistrationsApiDao registrationsApiDao = new RegistrationsApiDao();
        RegistrationsApi registrationsApi = registrationsApiDao.getRegistration();
        String regid = registrationsApi.getId();
        String code = registrationsApi.getVerificationCode();

        String credentials = regid + ":" + code;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return auth;
    }

}
