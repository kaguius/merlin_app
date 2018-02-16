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

import ke.merlin.models.business.StNoOfEmployeesRange;
import ke.merlin.models.customers.StChamaCycles;
import ke.merlin.models.customers.StHouseTypes;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StHouseTypesDao {

    private StHouseTypes stHouseTypes;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StHouseTypesDao(){
        stHouseTypes = new StHouseTypes();
    }

    /**
     *
     * @return
     */
    public static String createHouseTypesTable(){
        return "CREATE TABLE 'st_house_types' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_toh' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StHouseTypes stHouseTypes) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stHouseTypes.getId());
        values.put("name", stHouseTypes.getName());
        values.put("description", stHouseTypes.getDescription());
        values.put("creator_id", stHouseTypes.getCreatorId());
        values.put("deleted", stHouseTypes.getDeleted());
        values.put("creation_date", String.valueOf(stHouseTypes.getCreationDate()));
        values.put("updated_date", String.valueOf(stHouseTypes.getUpdatedDate()));

        // Inserting Row
        db.insert("st_house_types", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StHouseTypes> list = gson.fromJson(response, new TypeToken<List<StHouseTypes>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StHouseTypes stHouseTypes: list) {

                ContentValues values = new ContentValues();
                values.put("id", stHouseTypes.getId());
                values.put("name", stHouseTypes.getName());
                values.put("description", stHouseTypes.getDescription());
                values.put("creator_id", stHouseTypes.getCreatorId());
                values.put("deleted", stHouseTypes.getDeleted());
                values.put("creation_date", String.valueOf(stHouseTypes.getCreationDate()));
                values.put("updated_date", String.valueOf(stHouseTypes.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_house_types", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StHouseTypes stHouseTypes;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_house_types";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stHouseTypes = new StHouseTypes();
                stHouseTypes.setId(cursor.getString(cursor.getColumnIndex("id")));
                stHouseTypes.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stHouseTypes.getId(), stHouseTypes.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StHouseTypes getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_house_types where id = ? and deleted = 0";

        int iCount =0;
        StHouseTypes stHouseTypes = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stHouseTypes = new StHouseTypes();
                stHouseTypes.setId(cursor.getString(cursor.getColumnIndex("id")));
                stHouseTypes.setName(cursor.getString(cursor.getColumnIndex("name")));
                stHouseTypes.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stHouseTypes.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stHouseTypes.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stHouseTypes.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stHouseTypes.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stHouseTypes;
    }
}
