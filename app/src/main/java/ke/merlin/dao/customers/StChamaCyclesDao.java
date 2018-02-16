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
import ke.merlin.models.customers.StHouseTypes;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StChamaCyclesDao {

    private StChamaCycles stChamaCycles;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StChamaCyclesDao(){
        stChamaCycles = new StChamaCycles();
    }

    /**
     *
     * @return
     */
    public static String createChamaCyclesTable(){
        return "CREATE TABLE 'st_chama_cycles' (\n" +
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
                "  CONSTRAINT 'fk_creator_chm' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StChamaCycles stChamaCycles) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stChamaCycles.getId());
        values.put("name", stChamaCycles.getName());
        values.put("description", stChamaCycles.getDescription());
        values.put("creator_id", stChamaCycles.getCreatorId());
        values.put("deleted", stChamaCycles.getDeleted());
        values.put("creation_date", String.valueOf(stChamaCycles.getCreationDate()));
        values.put("updated_date", String.valueOf(stChamaCycles.getUpdatedDate()));

        // Inserting Row
        db.insert("st_chama_cycles", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StChamaCycles> list = gson.fromJson(response, new TypeToken<List<StChamaCycles>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StChamaCycles stChamaCycles: list) {

                ContentValues values = new ContentValues();
                values.put("id", stChamaCycles.getId());
                values.put("name", stChamaCycles.getName());
                values.put("description", stChamaCycles.getDescription());
                values.put("creator_id", stChamaCycles.getCreatorId());
                values.put("deleted", stChamaCycles.getDeleted());
                values.put("creation_date", String.valueOf(stChamaCycles.getCreationDate()));
                values.put("updated_date", String.valueOf(stChamaCycles.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_chama_cycles", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StChamaCycles stChamaCycles;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_chama_cycles";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stChamaCycles = new StChamaCycles();
                stChamaCycles.setId(cursor.getString(cursor.getColumnIndex("id")));
                stChamaCycles.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stChamaCycles.getId(), stChamaCycles.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StChamaCycles getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_chama_cycles where id = ? and deleted = 0";

        int iCount =0;
        StChamaCycles stChamaCycles = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stChamaCycles = new StChamaCycles();
                stChamaCycles.setId(cursor.getString(cursor.getColumnIndex("id")));
                stChamaCycles.setName(cursor.getString(cursor.getColumnIndex("name")));
                stChamaCycles.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stChamaCycles.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stChamaCycles.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stChamaCycles.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stChamaCycles.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stChamaCycles;
    }
}
