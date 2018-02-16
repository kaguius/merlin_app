package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ke.merlin.models.customers.StCustomerApprovalStatus;
import ke.merlin.models.customers.StLanguages;
import ke.merlin.models.customers.StMaritalStatus;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 23/07/2017.
 */

public class StLanguagesDao {

    private StLanguages stLanguages;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StLanguagesDao(){
        stLanguages = new StLanguages();
    }

    /**
     *
     * @return
     */
    public static String createLanguagesTable(){
        return "CREATE TABLE 'st_languages' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) DEFAULT NULL,\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) DEFAULT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                ")";
    }

    public void insert(StLanguages stLanguages) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stLanguages.getId());
        values.put("name", stLanguages.getName());
        values.put("description", stLanguages.getDescription());
        values.put("creator_id", stLanguages.getCreatorId());
        values.put("deleted", stLanguages.getDeleted());
        values.put("creation_date", String.valueOf(stLanguages.getCreationDate()));
        values.put("updated_date", String.valueOf(stLanguages.getUpdatedDate()));

        // Inserting Row
        db.insert("st_languages", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }


    public long insertList(String response) {
        long cnt = 0;

        List<StLanguages> list = gson.fromJson(response, new TypeToken<List<StLanguages>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StLanguages stLanguages: list) {

                ContentValues values = new ContentValues();
                values.put("id", stLanguages.getId());
                values.put("name", stLanguages.getName());
                values.put("description", stLanguages.getDescription());
                values.put("creator_id", stLanguages.getCreatorId());
                values.put("deleted", stLanguages.getDeleted());
                values.put("creation_date", String.valueOf(stLanguages.getCreationDate()));
                values.put("updated_date", String.valueOf(stLanguages.getUpdatedDate()));


                cnt = db.insertWithOnConflict("st_languages", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StLanguages stLanguages;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_languages";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stLanguages = new StLanguages();
                stLanguages.setId(cursor.getString(cursor.getColumnIndex("id")));
                stLanguages.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stLanguages.getId(), stLanguages.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StLanguages getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_languages where id = ? and deleted = 0";

        int iCount =0;
        StLanguages stLanguages = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stLanguages = new StLanguages();
                stLanguages.setId(cursor.getString(cursor.getColumnIndex("id")));
                stLanguages.setName(cursor.getString(cursor.getColumnIndex("name")));
                stLanguages.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stLanguages.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stLanguages.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stLanguages.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stLanguages.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stLanguages;
    }
}
