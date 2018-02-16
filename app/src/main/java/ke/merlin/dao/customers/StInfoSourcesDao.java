package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ke.merlin.models.customers.StInfoSources;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StInfoSourcesDao {

    private StInfoSources stInfoSources;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StInfoSourcesDao(){
        stInfoSources = new StInfoSources();
    }

    /**
     *
     * @return
     */
    public static String createInfoSourcesTable(){
        return "CREATE TABLE 'st_info_sources' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' varchar(100) DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_info' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StInfoSources stInfoSources) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stInfoSources.getId());
        values.put("name", stInfoSources.getName());
        values.put("creator_id", stInfoSources.getCreatorId());
        values.put("deleted", stInfoSources.getDeleted());
        values.put("creation_date", String.valueOf(stInfoSources.getCreationDate()));
        values.put("updated_date", String.valueOf(stInfoSources.getUpdatedDate()));

        // Inserting Row
        db.insert("st_info_sources", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StInfoSources> list = gson.fromJson(response, new TypeToken<List<StInfoSources>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StInfoSources stInfoSources: list) {

                ContentValues values = new ContentValues();
                values.put("id", stInfoSources.getId());
                values.put("name", stInfoSources.getName());
                values.put("creator_id", stInfoSources.getCreatorId());
                values.put("deleted", stInfoSources.getDeleted());
                values.put("creation_date", String.valueOf(stInfoSources.getCreationDate()));
                values.put("updated_date", String.valueOf(stInfoSources.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_info_sources", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerInfoSourcesTypes() {
        StInfoSources info_source_types;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_info_sources";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                info_source_types= new StInfoSources();
                info_source_types.setId(cursor.getString(cursor.getColumnIndex("id")));
                info_source_types.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(info_source_types.getId(), info_source_types.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }
}
