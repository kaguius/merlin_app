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
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StHomeOwnershipsDao {

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
    private StHomeOwnerships stHomeOwnerships;

    public StHomeOwnershipsDao() {
        stHomeOwnerships = new StHomeOwnerships();
    }

    /**
     * @return
     */
    public static String createHomeOwnershipTable() {
        return "CREATE TABLE 'st_home_ownerships' (\n" +
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
                "  CONSTRAINT 'fk_creator_ho' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StHomeOwnerships stHomeOwnerships) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("id", stHomeOwnerships.getId());
        values.put("name", stHomeOwnerships.getName());
        values.put("description", stHomeOwnerships.getDescription());
        values.put("creator_id", stHomeOwnerships.getCreatorId());
        values.put("deleted", stHomeOwnerships.getDeleted());
        values.put("creation_date", String.valueOf(stHomeOwnerships.getCreationDate()));
        values.put("updated_date", String.valueOf(stHomeOwnerships.getUpdatedDate()));

        // Inserting Row
        db.insert("st_home_ownerships", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StHomeOwnerships> list = gson.fromJson(response, new TypeToken<List<StHomeOwnerships>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StHomeOwnerships stHomeOwnerships : list) {

                ContentValues values = new ContentValues();
                values.put("id", stHomeOwnerships.getId());
                values.put("name", stHomeOwnerships.getName());
                values.put("description", stHomeOwnerships.getDescription());
                values.put("creator_id", stHomeOwnerships.getCreatorId());
                values.put("deleted", stHomeOwnerships.getDeleted());
                values.put("creation_date", String.valueOf(stHomeOwnerships.getCreationDate()));
                values.put("updated_date", String.valueOf(stHomeOwnerships.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_home_ownerships", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StHomeOwnerships stHomeOwnerships;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_home_ownerships";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stHomeOwnerships = new StHomeOwnerships();
                stHomeOwnerships.setId(cursor.getString(cursor.getColumnIndex("id")));
                stHomeOwnerships.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stHomeOwnerships.getId(), stHomeOwnerships.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StHomeOwnerships getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_home_ownerships where id = ? and deleted = 0";

        int iCount =0;
        StHomeOwnerships stHomeOwnerships = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stHomeOwnerships = new StHomeOwnerships();
                stHomeOwnerships.setId(cursor.getString(cursor.getColumnIndex("id")));
                stHomeOwnerships.setName(cursor.getString(cursor.getColumnIndex("name")));
                stHomeOwnerships.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stHomeOwnerships.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stHomeOwnerships.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stHomeOwnerships.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stHomeOwnerships.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stHomeOwnerships;
    }
}
