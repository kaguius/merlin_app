package ke.merlin.dao.users;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ke.merlin.models.users.Users;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 23/07/2017.
 */

public class UsersDao {

    private Users users;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public UsersDao(){
        users = new Users();
    }

    /**
     *
     * @return
     */
    public static String createUsersTable(){
        return "CREATE TABLE 'users' (\n" +
                "  'id' varchar(150) NOT NULL,\n" +
                "  'first_name' varchar(100) NOT NULL,\n" +
                "  'last_name' varchar(100) NOT NULL,\n" +
                "  'username' varchar(45) NOT NULL,\n" +
                "  'email' varchar(100) NOT NULL,\n" +
                "  'user_status_id' varchar(150) NOT NULL DEFAULT '1',\n" +
                "  'language_id' varchar(150) NOT NULL DEFAULT '1',\n" +
                "  'roles_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'station_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'photo_path' text,\n" +
                "  'cc_campaign' varchar(150) DEFAULT NULL,\n" +
                "  'cc_list' varchar(150) DEFAULT NULL,\n" +
                "  'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_language_id_u' FOREIGN KEY ('language_id') REFERENCES 'st_languages' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_roles_id_u' FOREIGN KEY ('roles_id') REFERENCES 'roles' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_station_id_u' FOREIGN KEY ('station_id') REFERENCES 'stations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_user_status_u' FOREIGN KEY ('user_status_id') REFERENCES 'st_users_status' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_users_usr' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }


    public void insert(Users users) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", users.getId());
        values.put("first_name", users.getFirstName());
        values.put("last_name", users.getLastName());
        values.put("username", users.getUsername());
        values.put("email", users.getEmail());
        values.put("user_status_id", users.getUserStatusId());
        values.put("language_id", users.getLanguageId());
        values.put("cc_campaign", users.getCcCampaign());
        values.put("cc_list", users.getCcList());
        values.put("creator_id", users.getCreatorId());
        values.put("deleted", users.getDeleted());
        values.put("creation_date", String.valueOf(users.getCreationDate()));
        values.put("updated_date", String.valueOf(users.getUpdatedDate()));
        values.put("roles_id", users.getRolesId());
        values.put("station_id", users.getStationId());
        values.put("photo_path", users.getPhotoPath());

        // Inserting Row
        db.insertWithOnConflict("users", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long getUsersCount(){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, "users");

        db.close();
        DatabaseManager.getInstance().closeDatabase();

        return cnt;
    }

    public long insertList(String response) {
        long cnt = 0;

        List<Users> list = gson.fromJson(response, new TypeToken<List<Users>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Users users: list) {

                ContentValues values = new ContentValues();
                values.put("id", users.getId());
                values.put("first_name", users.getFirstName());
                values.put("last_name", users.getLastName());
                values.put("username", users.getUsername());
                values.put("email", users.getEmail());
                values.put("user_status_id", users.getUserStatusId());
                values.put("language_id", users.getLanguageId());
                values.put("cc_campaign", users.getCcCampaign());
                values.put("cc_list", users.getCcList());
                values.put("creator_id", users.getCreatorId());
                values.put("deleted", users.getDeleted());
                values.put("creation_date", String.valueOf(users.getCreationDate()));
                values.put("updated_date", String.valueOf(users.getUpdatedDate()));
                values.put("roles_id", users.getRolesId());
                values.put("station_id", users.getStationId());
                values.put("photo_path", users.getPhotoPath());

                cnt = db.insertWithOnConflict("users", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public Users getUserByUsername(String email) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from users where username = ? and deleted = 0 and user_status_id = 1";

        int iCount =0;
        Users users = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(email) } );

        if (cursor.moveToFirst()) {
            do {
                users = new Users();
                users.setId(cursor.getString(cursor.getColumnIndex("id")));
                users.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                users.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                users.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                users.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                users.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                users.setCcCampaign(cursor.getString(cursor.getColumnIndex("cc_campaign")));
                users.setCcList(cursor.getString(cursor.getColumnIndex("cc_list")));
                users.setRolesId(cursor.getString(cursor.getColumnIndex("roles_id")));
                users.setStationId(cursor.getString(cursor.getColumnIndex("station_id")));
                users.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return users;
    }

    public HashMap<String, String> getSpinnerData(String station, String roles_id) {
        Users users;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from users where station_id = ? and roles_id = ? and deleted = 0 and user_status_id = 1";

        Cursor cursor = db.rawQuery(selectQuery, new String[] { station, roles_id } );
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                users= new Users();
                users.setId(cursor.getString(cursor.getColumnIndex("id")));
                users.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                users.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                map.put(users.getId(), users.getFirstName() + " " + users.getLastName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public Users getUserById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from users where id = ? and deleted = 0";

        int iCount =0;
        Users users = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                users = new Users();
                users.setId(cursor.getString(cursor.getColumnIndex("id")));
                users.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                users.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                users.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                users.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                users.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                users.setCcCampaign(cursor.getString(cursor.getColumnIndex("cc_campaign")));
                users.setCcList(cursor.getString(cursor.getColumnIndex("cc_list")));
                users.setRolesId(cursor.getString(cursor.getColumnIndex("roles_id")));
                users.setStationId(cursor.getString(cursor.getColumnIndex("station_id")));
                users.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return users;
    }
}
