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

import ke.merlin.models.customers.StCustomerTitles;
import ke.merlin.models.stations.StationsMarkets;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StCustomerTitlesDao {

    private StCustomerTitles stCustomerTitles;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StCustomerTitlesDao(){
        stCustomerTitles = new StCustomerTitles();
    }

    /**
     *
     * @return
     */
    public static String createCustomerTitlesTable(){
        return "CREATE TABLE 'st_customer_titles' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' varchar(150) DEFAULT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'created_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_titles' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StCustomerTitles stCustomerTitles) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stCustomerTitles.getId());
        values.put("name", stCustomerTitles.getName());
        values.put("description", stCustomerTitles.getDescription());
        values.put("creator_id", stCustomerTitles.getCreatorId());
        values.put("deleted", stCustomerTitles.getDeleted());
        values.put("created_date", String.valueOf(stCustomerTitles.getCreatedDate()));
        values.put("updated_date", String.valueOf(stCustomerTitles.getUpdatedDate()));

        // Inserting Row
        db.insert("st_customer_titles", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StCustomerTitles> list = gson.fromJson(response, new TypeToken<List<StCustomerTitles>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StCustomerTitles stCustomerTitles: list) {

                ContentValues values = new ContentValues();
                values.put("id", stCustomerTitles.getId());
                values.put("name", stCustomerTitles.getName());
                values.put("description", stCustomerTitles.getDescription());
                values.put("creator_id", stCustomerTitles.getCreatorId());
                values.put("deleted", stCustomerTitles.getDeleted());
                values.put("created_date", String.valueOf(stCustomerTitles.getCreatedDate()));
                values.put("updated_date", String.valueOf(stCustomerTitles.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_customer_titles", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public StCustomerTitles getCustomerTitleById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_customer_titles where id = ? and deleted = 0";

        int iCount =0;
        StCustomerTitles stCustomerTitles = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stCustomerTitles = new StCustomerTitles();
                stCustomerTitles.setId(cursor.getString(cursor.getColumnIndex("id")));
                stCustomerTitles.setName(cursor.getString(cursor.getColumnIndex("name")));
                stCustomerTitles.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stCustomerTitles.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stCustomerTitles.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stCustomerTitles.setCreatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("created_date"))));
                stCustomerTitles.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stCustomerTitles;
    }

    public HashMap<String, String> getSpinnerItems() {

        StCustomerTitles stCustomerTitles;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_customer_titles";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stCustomerTitles = new StCustomerTitles();
                stCustomerTitles.setId(cursor.getString(cursor.getColumnIndex("id")));
                stCustomerTitles.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stCustomerTitles.getId(), stCustomerTitles.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }
}
