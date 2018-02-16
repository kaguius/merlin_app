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

import ke.merlin.models.customers.StChamaCycles;
import ke.merlin.models.customers.StCreditOrganisations;
import ke.merlin.models.customers.StEducationLevels;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StEducationLevelsDao {

    private StEducationLevels stEducationLevels;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StEducationLevelsDao(){
        stEducationLevels = new StEducationLevels();
    }

    /**
     *
     * @return
     */
    public static String createEducationLevelsTable(){
        return "CREATE TABLE 'st_education_levels' (\n" +
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
                "  CONSTRAINT 'fk_creator_el' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StEducationLevels stEducationLevels) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stEducationLevels.getId());
        values.put("name", stEducationLevels.getName());
        values.put("description", stEducationLevels.getDescription());
        values.put("creator_id", stEducationLevels.getCreatorId());
        values.put("deleted", stEducationLevels.getDeleted());
        values.put("creation_date", String.valueOf(stEducationLevels.getCreationDate()));
        values.put("updated_date", String.valueOf(stEducationLevels.getUpdatedDate()));

        // Inserting Row
        db.insert("st_education_levels", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StEducationLevels> list = gson.fromJson(response, new TypeToken<List<StEducationLevels>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StEducationLevels stEducationLevels: list) {

                ContentValues values = new ContentValues();
                values.put("id", stEducationLevels.getId());
                values.put("name", stEducationLevels.getName());
                values.put("description", stEducationLevels.getDescription());
                values.put("creator_id", stEducationLevels.getCreatorId());
                values.put("deleted", stEducationLevels.getDeleted());
                values.put("creation_date", String.valueOf(stEducationLevels.getCreationDate()));
                values.put("updated_date", String.valueOf(stEducationLevels.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_education_levels", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StEducationLevels stEducationLevels;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_education_levels";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stEducationLevels = new StEducationLevels();
                stEducationLevels.setId(cursor.getString(cursor.getColumnIndex("id")));
                stEducationLevels.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stEducationLevels.getId(), stEducationLevels.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StEducationLevels getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_education_levels where id = ? and deleted = 0";

        int iCount =0;
        StEducationLevels stEducationLevels = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stEducationLevels = new StEducationLevels();
                stEducationLevels.setId(cursor.getString(cursor.getColumnIndex("id")));
                stEducationLevels.setName(cursor.getString(cursor.getColumnIndex("name")));
                stEducationLevels.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stEducationLevels.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stEducationLevels.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stEducationLevels.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stEducationLevels.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stEducationLevels;
    }
}
