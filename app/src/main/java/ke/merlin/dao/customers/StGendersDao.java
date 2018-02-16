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
import ke.merlin.models.customers.StGenders;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StGendersDao {

    private StGenders stGenders;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StGendersDao(){
        stGenders = new StGenders();
    }

    /**
     *
     * @return
     */
    public static String createGenderTable(){
        return "CREATE TABLE 'st_genders' (\n" +
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
                "  CONSTRAINT 'fk_creator_g' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StGenders stGenders) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stGenders.getId());
        values.put("name", stGenders.getName());
        values.put("description", stGenders.getDescription());
        values.put("creator_id", stGenders.getCreatorId());
        values.put("deleted", stGenders.getDeleted());
        values.put("creation_date", String.valueOf(stGenders.getCreationDate()));
        values.put("updated_date", String.valueOf(stGenders.getUpdatedDate()));

        // Inserting Row
        db.insert("st_genders", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StGenders> list = gson.fromJson(response, new TypeToken<List<StGenders>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StGenders stGenders: list) {

                ContentValues values = new ContentValues();
                values.put("id", stGenders.getId());
                values.put("name", stGenders.getName());
                values.put("description", stGenders.getDescription());
                values.put("creator_id", stGenders.getCreatorId());
                values.put("deleted", stGenders.getDeleted());
                values.put("creation_date", String.valueOf(stGenders.getCreationDate()));
                values.put("updated_date", String.valueOf(stGenders.getUpdatedDate()));



                cnt = db.insertWithOnConflict("st_genders", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public StGenders getCustomerGenderById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_genders where id = ? and deleted = 0";

        int iCount =0;
        StGenders stGenders = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stGenders = new StGenders();
                stGenders.setId(cursor.getString(cursor.getColumnIndex("id")));
                stGenders.setName(cursor.getString(cursor.getColumnIndex("name")));
                stGenders.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stGenders.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stGenders.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stGenders.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stGenders.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stGenders;
    }

    public HashMap<String, String> getSpinnerItems() {

        StGenders stGenders;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_genders";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stGenders = new StGenders();
                stGenders.setId(cursor.getString(cursor.getColumnIndex("id")));
                stGenders.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stGenders.getId(), stGenders.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }
}
