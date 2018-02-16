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

import ke.merlin.models.business.StNatureOfEmployment;
import ke.merlin.models.business.StNoOfEmployeesRange;
import ke.merlin.models.customers.StHouseTypes;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StNoOfEmployeesRangeDao {

    private StNoOfEmployeesRange stNoOfEmployeesRange;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StNoOfEmployeesRangeDao(){
        stNoOfEmployeesRange = new StNoOfEmployeesRange();
    }

    /**
     *
     * @return
     */
    public static String createNoOfEmployeesRangeTable(){
        return "CREATE TABLE 'st_no_of_employees_range' (\n" +
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
                "  CONSTRAINT 'fk_creator_noem' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StNoOfEmployeesRange stNoOfEmployeesRange) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stNoOfEmployeesRange.getId());
        values.put("name", stNoOfEmployeesRange.getName());
        values.put("description", stNoOfEmployeesRange.getDescription());
        values.put("creator_id", stNoOfEmployeesRange.getCreatorId());
        values.put("deleted", stNoOfEmployeesRange.getDeleted());
        values.put("creation_date", String.valueOf(stNoOfEmployeesRange.getCreationDate()));
        values.put("updated_date", String.valueOf(stNoOfEmployeesRange.getUpdatedDate()));

        // Inserting Row
        db.insert("st_no_of_employees_range", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StNoOfEmployeesRange> list = gson.fromJson(response, new TypeToken<List<StNoOfEmployeesRange>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StNoOfEmployeesRange stNoOfEmployeesRange: list) {

                ContentValues values = new ContentValues();
                values.put("id", stNoOfEmployeesRange.getId());
                values.put("name", stNoOfEmployeesRange.getName());
                values.put("description", stNoOfEmployeesRange.getDescription());
                values.put("creator_id", stNoOfEmployeesRange.getCreatorId());
                values.put("deleted", stNoOfEmployeesRange.getDeleted());
                values.put("creation_date", String.valueOf(stNoOfEmployeesRange.getCreationDate()));
                values.put("updated_date", String.valueOf(stNoOfEmployeesRange.getUpdatedDate()));


                cnt = db.insertWithOnConflict("st_no_of_employees_range", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StNoOfEmployeesRange stNoOfEmployeesRange;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_no_of_employees_range";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stNoOfEmployeesRange = new StNoOfEmployeesRange();
                stNoOfEmployeesRange.setId(cursor.getString(cursor.getColumnIndex("id")));
                stNoOfEmployeesRange.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stNoOfEmployeesRange.getId(), stNoOfEmployeesRange.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StNoOfEmployeesRange getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_no_of_employees_range where id = ? and deleted = 0";

        int iCount =0;
        StNoOfEmployeesRange stNoOfEmployeesRange = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stNoOfEmployeesRange = new StNoOfEmployeesRange();
                stNoOfEmployeesRange.setId(cursor.getString(cursor.getColumnIndex("id")));
                stNoOfEmployeesRange.setName(cursor.getString(cursor.getColumnIndex("name")));
                stNoOfEmployeesRange.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stNoOfEmployeesRange.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stNoOfEmployeesRange.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stNoOfEmployeesRange.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stNoOfEmployeesRange.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stNoOfEmployeesRange;
    }
}
