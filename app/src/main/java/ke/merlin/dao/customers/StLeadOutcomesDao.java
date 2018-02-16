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

import ke.merlin.models.customers.StLeadOutcomes;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StLeadOutcomesDao {

    private StLeadOutcomes stLeadOutcomes;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();


    public StLeadOutcomesDao() {
        stLeadOutcomes = new StLeadOutcomes();
    }

    /**
     *
     * @return
     */
    public static String createStLeadOutcomesTable(){
        return "CREATE TABLE 'st_lead_outcomes' (\n" +
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
                "  CONSTRAINT 'fk_creator_otcm' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StLeadOutcomes stLeadOutcomes) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stLeadOutcomes.getId());
        values.put("name", stLeadOutcomes.getName());
        values.put("creator_id", stLeadOutcomes.getCreatorId());
        values.put("deleted", stLeadOutcomes.getDeleted());
        values.put("creation_date", String.valueOf(stLeadOutcomes.getCreationDate()));
        values.put("updated_date", String.valueOf(stLeadOutcomes.getUpdatedDate()));

        // Inserting Row
        db.insert("st_lead_outcomes", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StLeadOutcomes> list = gson.fromJson(response, new TypeToken<List<StLeadOutcomes>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StLeadOutcomes stLeadOutcomes : list) {

                ContentValues values = new ContentValues();
                values.put("id", stLeadOutcomes.getId());
                values.put("name", stLeadOutcomes.getName());
                values.put("creator_id", stLeadOutcomes.getCreatorId());
                values.put("deleted", stLeadOutcomes.getDeleted());
                values.put("creation_date", String.valueOf(stLeadOutcomes.getCreationDate()));
                values.put("updated_date", String.valueOf(stLeadOutcomes.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_lead_outcomes", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public StLeadOutcomes getById(String id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_lead_outcomes where id = ? and deleted = 0";

        int iCount = 0;
        StLeadOutcomes stLeadOutcomes = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            do {
                stLeadOutcomes = new StLeadOutcomes();
                stLeadOutcomes.setId(cursor.getString(cursor.getColumnIndex("id")));
                stLeadOutcomes.setName(cursor.getString(cursor.getColumnIndex("name")));
                stLeadOutcomes.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stLeadOutcomes.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stLeadOutcomes.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stLeadOutcomes.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stLeadOutcomes.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stLeadOutcomes;
    }

    public HashMap<String, String> getSpinnerItems() {

        StLeadOutcomes stLeadOutcomes;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_lead_outcomes";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stLeadOutcomes = new StLeadOutcomes();
                stLeadOutcomes.setId(cursor.getString(cursor.getColumnIndex("id")));
                stLeadOutcomes.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stLeadOutcomes.getId(), stLeadOutcomes.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }
}
