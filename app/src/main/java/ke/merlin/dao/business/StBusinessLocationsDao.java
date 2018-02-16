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
import ke.merlin.models.business.StBusinessLocations;
import ke.merlin.models.customers.StHomeOwnerships;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StBusinessLocationsDao {

    private StBusinessLocations stBusinessLocations;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StBusinessLocationsDao(){
        stBusinessLocations = new StBusinessLocations();
    }

    /**
     *
     * @return
     */
    public static String createBusinessStLocationsTable(){
        return "CREATE TABLE 'st_business_locations' (\n" +
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
                "  CONSTRAINT 'fk_creator_blt' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StBusinessLocations stBusinessLocations) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stBusinessLocations.getId());
        values.put("name", stBusinessLocations.getName());
        values.put("description", stBusinessLocations.getDescription());
        values.put("creator_id", stBusinessLocations.getCreatorId());
        values.put("deleted", stBusinessLocations.getDeleted());
        values.put("creation_date", String.valueOf(stBusinessLocations.getCreationDate()));
        values.put("updated_date", String.valueOf(stBusinessLocations.getUpdatedDate()));

        // Inserting Row
        db.insert("st_business_locations", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StBusinessLocations> list = gson.fromJson(response, new TypeToken<List<StBusinessLocations>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StBusinessLocations stBusinessLocations: list) {

                ContentValues values = new ContentValues();
                values.put("id", stBusinessLocations.getId());
                values.put("name", stBusinessLocations.getName());
                values.put("description", stBusinessLocations.getDescription());
                values.put("creator_id", stBusinessLocations.getCreatorId());
                values.put("deleted", stBusinessLocations.getDeleted());
                values.put("creation_date", String.valueOf(stBusinessLocations.getCreationDate()));
                values.put("updated_date", String.valueOf(stBusinessLocations.getUpdatedDate()));


                cnt = db.insertWithOnConflict("st_business_locations", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StBusinessLocations stBusinessLocations;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_business_locations";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stBusinessLocations = new StBusinessLocations();
                stBusinessLocations.setId(cursor.getString(cursor.getColumnIndex("id")));
                stBusinessLocations.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stBusinessLocations.getId(), stBusinessLocations.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StBusinessLocations getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_business_locations where id = ? and deleted = 0";

        int iCount =0;
        StBusinessLocations stBusinessLocations = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stBusinessLocations = new StBusinessLocations();
                stBusinessLocations.setId(cursor.getString(cursor.getColumnIndex("id")));
                stBusinessLocations.setName(cursor.getString(cursor.getColumnIndex("name")));
                stBusinessLocations.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stBusinessLocations.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stBusinessLocations.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stBusinessLocations.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stBusinessLocations.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stBusinessLocations;
    }
}
