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

import ke.merlin.models.customers.StGenders;
import ke.merlin.models.customers.StHomeOwnerships;
import ke.merlin.models.customers.StMaritalStatus;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StMaritalStatusDao {

    private StMaritalStatus stMaritalStatus;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StMaritalStatusDao(){
        stMaritalStatus = new StMaritalStatus();
    }

    /**
     *
     * @return
     */
    public static String createMaritalStatusTable(){
        return "CREATE TABLE 'st_marital_status' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_mst' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StMaritalStatus stMaritalStatus) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stMaritalStatus.getId());
        values.put("name", stMaritalStatus.getName());
        values.put("description", stMaritalStatus.getDescription());
        values.put("creator_id", stMaritalStatus.getCreatorId());
        values.put("deleted", stMaritalStatus.getDeleted());
        values.put("creation_date", String.valueOf(stMaritalStatus.getCreationDate()));
        values.put("updated_date", String.valueOf(stMaritalStatus.getUpdatedDate()));

        // Inserting Row
        db.insert("st_marital_status", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StMaritalStatus> list = gson.fromJson(response, new TypeToken<List<StMaritalStatus>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StMaritalStatus stMaritalStatus: list) {

                ContentValues values = new ContentValues();
                values.put("id", stMaritalStatus.getId());
                values.put("name", stMaritalStatus.getName());
                values.put("description", stMaritalStatus.getDescription());
                values.put("creator_id", stMaritalStatus.getCreatorId());
                values.put("deleted", stMaritalStatus.getDeleted());
                values.put("creation_date", String.valueOf(stMaritalStatus.getCreationDate()));
                values.put("updated_date", String.valueOf(stMaritalStatus.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_marital_status", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StMaritalStatus stMaritalStatus;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_marital_status";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stMaritalStatus = new StMaritalStatus();
                stMaritalStatus.setId(cursor.getString(cursor.getColumnIndex("id")));
                stMaritalStatus.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stMaritalStatus.getId(), stMaritalStatus.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StMaritalStatus getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_marital_status where id = ? and deleted = 0";

        int iCount =0;
        StMaritalStatus stMaritalStatus = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stMaritalStatus = new StMaritalStatus();
                stMaritalStatus.setId(cursor.getString(cursor.getColumnIndex("id")));
                stMaritalStatus.setName(cursor.getString(cursor.getColumnIndex("name")));
                stMaritalStatus.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stMaritalStatus.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stMaritalStatus.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stMaritalStatus.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stMaritalStatus.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stMaritalStatus;
    }
}
