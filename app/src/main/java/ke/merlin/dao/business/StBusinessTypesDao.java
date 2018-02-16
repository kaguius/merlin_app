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
import ke.merlin.models.business.StBusinessTypes;
import ke.merlin.models.customers.StHomeOwnerships;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StBusinessTypesDao {

    private StBusinessTypes stBusinessTypes;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StBusinessTypesDao(){
        stBusinessTypes = new StBusinessTypes();
    }

    /**
     *
     * @return
     */
    public static String createBusinessTypesTable(){
        return "CREATE TABLE 'st_business_types' (\n" +
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
                "  CONSTRAINT 'fk_creator_bust' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StBusinessTypes stBusinessTypes) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stBusinessTypes.getId());
        values.put("name", stBusinessTypes.getName());
        values.put("description", stBusinessTypes.getDescription());
        values.put("creator_id", stBusinessTypes.getCreatorId());
        values.put("deleted", stBusinessTypes.getDeleted());
        values.put("creation_date", String.valueOf(stBusinessTypes.getCreationDate()));
        values.put("updated_date", String.valueOf(stBusinessTypes.getUpdatedDate()));

        // Inserting Row
        db.insert("st_business_types", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StBusinessTypes> list = gson.fromJson(response, new TypeToken<List<StBusinessTypes>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StBusinessTypes stBusinessTypes: list) {

                ContentValues values = new ContentValues();
                values.put("id", stBusinessTypes.getId());
                values.put("name", stBusinessTypes.getName());
                values.put("description", stBusinessTypes.getDescription());
                values.put("creator_id", stBusinessTypes.getCreatorId());
                values.put("deleted", stBusinessTypes.getDeleted());
                values.put("creation_date", String.valueOf(stBusinessTypes.getCreationDate()));
                values.put("updated_date", String.valueOf(stBusinessTypes.getUpdatedDate()));


                cnt = db.insertWithOnConflict("st_business_types", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StBusinessTypes stBusinessTypes;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_business_types";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stBusinessTypes = new StBusinessTypes();
                stBusinessTypes.setId(cursor.getString(cursor.getColumnIndex("id")));
                stBusinessTypes.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stBusinessTypes.getId(), stBusinessTypes.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StBusinessTypes getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_business_types where id = ? and deleted = 0";

        int iCount =0;
        StBusinessTypes stBusinessTypes = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stBusinessTypes = new StBusinessTypes();
                stBusinessTypes.setId(cursor.getString(cursor.getColumnIndex("id")));
                stBusinessTypes.setName(cursor.getString(cursor.getColumnIndex("name")));
                stBusinessTypes.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stBusinessTypes.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stBusinessTypes.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stBusinessTypes.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stBusinessTypes.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stBusinessTypes;
    }
}
