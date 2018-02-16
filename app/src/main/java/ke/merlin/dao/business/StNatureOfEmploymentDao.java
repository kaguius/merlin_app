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

import ke.merlin.models.business.StBusinessCycles;
import ke.merlin.models.business.StNatureOfEmployment;
import ke.merlin.models.business.StNoOfEmployeesRange;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StNatureOfEmploymentDao {

    private StNatureOfEmployment stNatureOfEmployment;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StNatureOfEmploymentDao(){
        stNatureOfEmployment = new StNatureOfEmployment();
    }

    /**
     *
     * @return
     */
    public static String createNatureofEmploymentTable(){
        return "CREATE TABLE 'st_nature_of_employment' (\n" +
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
                "  CONSTRAINT 'fk_creator_noe' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StNatureOfEmployment stNatureOfEmployment) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stNatureOfEmployment.getId());
        values.put("name", stNatureOfEmployment.getName());
        values.put("description", stNatureOfEmployment.getDescription());
        values.put("creator_id", stNatureOfEmployment.getCreatorId());
        values.put("deleted", stNatureOfEmployment.getDeleted());
        values.put("creation_date", String.valueOf(stNatureOfEmployment.getCreationDate()));
        values.put("updated_date", String.valueOf(stNatureOfEmployment.getUpdatedDate()));

        // Inserting Row
        db.insert("st_nature_of_employment", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StNatureOfEmployment> list = gson.fromJson(response, new TypeToken<List<StNatureOfEmployment>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StNatureOfEmployment stNatureOfEmployment: list) {

                ContentValues values = new ContentValues();
                values.put("id", stNatureOfEmployment.getId());
                values.put("name", stNatureOfEmployment.getName());
                values.put("description", stNatureOfEmployment.getDescription());
                values.put("creator_id", stNatureOfEmployment.getCreatorId());
                values.put("deleted", stNatureOfEmployment.getDeleted());
                values.put("creation_date", String.valueOf(stNatureOfEmployment.getCreationDate()));
                values.put("updated_date", String.valueOf(stNatureOfEmployment.getUpdatedDate()));


                cnt = db.insertWithOnConflict("st_nature_of_employment", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StNatureOfEmployment stNatureOfEmployment;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_nature_of_employment";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stNatureOfEmployment = new StNatureOfEmployment();
                stNatureOfEmployment.setId(cursor.getString(cursor.getColumnIndex("id")));
                stNatureOfEmployment.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stNatureOfEmployment.getId(), stNatureOfEmployment.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StNatureOfEmployment getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_nature_of_employment where id = ? and deleted = 0";

        int iCount =0;
        StNatureOfEmployment stNatureOfEmployment = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stNatureOfEmployment = new StNatureOfEmployment();
                stNatureOfEmployment.setId(cursor.getString(cursor.getColumnIndex("id")));
                stNatureOfEmployment.setName(cursor.getString(cursor.getColumnIndex("name")));
                stNatureOfEmployment.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stNatureOfEmployment.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stNatureOfEmployment.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stNatureOfEmployment.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stNatureOfEmployment.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stNatureOfEmployment;
    }
}
