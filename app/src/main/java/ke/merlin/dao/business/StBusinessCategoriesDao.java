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
import ke.merlin.models.customers.StHomeOwnerships;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StBusinessCategoriesDao {

    private StBusinessCategories stBusinessCategories;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StBusinessCategoriesDao(){
        stBusinessCategories = new StBusinessCategories();
    }

    /**
     *
     * @return
     */
    public static String createBusinessCategoriesTable(){
        return "CREATE TABLE 'st_business_categories' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_bc' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StBusinessCategories stBusinessCategories) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stBusinessCategories.getId());
        values.put("name", stBusinessCategories.getName());
        values.put("description", stBusinessCategories.getDescription());
        values.put("creator_id", stBusinessCategories.getCreatorId());
        values.put("deleted", stBusinessCategories.getDeleted());
        values.put("creation_date", String.valueOf(stBusinessCategories.getCreationDate()));
        values.put("updated_date", String.valueOf(stBusinessCategories.getUpdatedDate()));

        // Inserting Row
        db.insert("st_business_categories", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StBusinessCategories> list = gson.fromJson(response, new TypeToken<List<StBusinessCategories>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StBusinessCategories stBusinessCategories: list) {

                ContentValues values = new ContentValues();
                values.put("id", stBusinessCategories.getId());
                values.put("name", stBusinessCategories.getName());
                values.put("description", stBusinessCategories.getDescription());
                values.put("creator_id", stBusinessCategories.getCreatorId());
                values.put("deleted", stBusinessCategories.getDeleted());
                values.put("creation_date", String.valueOf(stBusinessCategories.getCreationDate()));
                values.put("updated_date", String.valueOf(stBusinessCategories.getUpdatedDate()));


                cnt = db.insertWithOnConflict("st_business_categories", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StBusinessCategories stBusinessCategories;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_business_categories";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stBusinessCategories = new StBusinessCategories();
                stBusinessCategories.setId(cursor.getString(cursor.getColumnIndex("id")));
                stBusinessCategories.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stBusinessCategories.getId(), stBusinessCategories.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StBusinessCategories getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_business_categories where id = ? and deleted = 0";

        int iCount =0;
        StBusinessCategories stBusinessCategories = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stBusinessCategories = new StBusinessCategories();
                stBusinessCategories.setId(cursor.getString(cursor.getColumnIndex("id")));
                stBusinessCategories.setName(cursor.getString(cursor.getColumnIndex("name")));
                stBusinessCategories.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stBusinessCategories.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stBusinessCategories.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stBusinessCategories.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stBusinessCategories.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stBusinessCategories;
    }
}
