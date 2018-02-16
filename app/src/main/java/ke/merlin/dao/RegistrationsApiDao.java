package ke.merlin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;

import ke.merlin.models.RegistrationsApi;
import ke.merlin.utils.database.DatabaseManager;

/**
 * Created by mecmurimi on 16/07/2017.
 */

public class RegistrationsApiDao {
    RegistrationsApi beans;

    public RegistrationsApiDao(){
        beans = new RegistrationsApi();
    }

    /**
     *
     * @return
     */
    public static String createRegistrationApiTable(){
        return "CREATE TABLE 'registrations_api' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'username' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'email' varchar(150) NOT NULL,\n" +
                "  'verification_code' text NOT NULL,\n" +
                "  'accessor_name' text NOT NULL,\n" +
                "  'station_id' varchar(150) DEFAULT NULL,\n" +
                "  'creator_id' varchar(150) DEFAULT NULL,\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'is_approved' tinyint(1) DEFAULT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                ")";
    }

    public void insert(RegistrationsApi beans){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("id", beans.getId());
        values.put("username", beans.getUsername());
        values.put("email", beans.getEmail());
        values.put("verification_code", beans.getVerificationCode());
        values.put("accessor_name", beans.getAccessorName());
        values.put("station_id", beans.getStation());
        values.put("creation_date", String.valueOf(beans.getCreationDate()));
        values.put("is_approved", beans.getIsApproved());
        values.put("deleted", beans.getDeleted());
        values.put("updated_date", String.valueOf(beans.getUpdatedDate()));

        // Inserting Row
        db.insert("registrations_api", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public RegistrationsApi getRegistration(){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from registrations_api limit 1";

        RegistrationsApi registrationsApi = null;

        Cursor cursor = db.rawQuery(selectQuery, null );

        if (cursor.moveToFirst()) {
            do {
                registrationsApi = new RegistrationsApi();
                registrationsApi.setId(cursor.getString(cursor.getColumnIndex("id")));
                registrationsApi.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                registrationsApi.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                registrationsApi.setVerificationCode(cursor.getString(cursor.getColumnIndex("verification_code")));
                registrationsApi.setAccessorName(cursor.getString(cursor.getColumnIndex("accessor_name")));
                registrationsApi.setStation(cursor.getString(cursor.getColumnIndex("station_id")));
                registrationsApi.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                registrationsApi.setIsApproved(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_approved"))));
                registrationsApi.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                registrationsApi.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return registrationsApi;
    }
}
