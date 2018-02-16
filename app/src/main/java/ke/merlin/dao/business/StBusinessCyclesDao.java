package ke.merlin.dao.business;

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

import ke.merlin.models.business.StBusinessCategories;
import ke.merlin.models.business.StBusinessCycles;
import ke.merlin.models.business.StNatureOfEmployment;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StBusinessCyclesDao {

    private StBusinessCycles stBusinessCycles;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StBusinessCyclesDao(){
        stBusinessCycles = new StBusinessCycles();
    }

    /**
     *
     * @return
     */
    public static String createBusinessCysclesTable(){
        return "CREATE TABLE 'st_business_cycles' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL,\n" +
                "  'description' text,\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_busc' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StBusinessCycles stBusinessCycles) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stBusinessCycles.getId());
        values.put("name", stBusinessCycles.getName());
        values.put("description", stBusinessCycles.getDescription());
        values.put("creator_id", stBusinessCycles.getCreatorId());
        values.put("deleted", stBusinessCycles.getDeleted());
        values.put("creation_date", String.valueOf(stBusinessCycles.getCreationDate()));
        values.put("updated_date", String.valueOf(stBusinessCycles.getUpdatedDate()));

        // Inserting Row
        db.insert("st_business_cycles", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StBusinessCycles> list = gson.fromJson(response, new TypeToken<List<StBusinessCycles>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StBusinessCycles stBusinessCycles: list) {

                ContentValues values = new ContentValues();
                values.put("id", stBusinessCycles.getId());
                values.put("name", stBusinessCycles.getName());
                values.put("description", stBusinessCycles.getDescription());
                values.put("creator_id", stBusinessCycles.getCreatorId());
                values.put("deleted", stBusinessCycles.getDeleted());
                values.put("creation_date", String.valueOf(stBusinessCycles.getCreationDate()));
                values.put("updated_date", String.valueOf(stBusinessCycles.getUpdatedDate()));


                cnt = db.insertWithOnConflict("st_business_cycles", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StBusinessCycles stBusinessCycles;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_business_cycles";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stBusinessCycles = new StBusinessCycles();
                stBusinessCycles.setId(cursor.getString(cursor.getColumnIndex("id")));
                stBusinessCycles.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stBusinessCycles.getId(), stBusinessCycles.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StBusinessCycles getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_business_cycles where id = ? and deleted = 0";

        int iCount =0;
        StBusinessCycles stBusinessCycles = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stBusinessCycles = new StBusinessCycles();
                stBusinessCycles.setId(cursor.getString(cursor.getColumnIndex("id")));
                stBusinessCycles.setName(cursor.getString(cursor.getColumnIndex("name")));
                stBusinessCycles.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stBusinessCycles.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stBusinessCycles.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stBusinessCycles.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stBusinessCycles.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stBusinessCycles;
    }
}
