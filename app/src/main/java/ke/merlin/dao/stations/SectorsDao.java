package ke.merlin.dao.stations;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.stations.Sectors;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 25/07/2017.
 */

public class SectorsDao {

    private Sectors sectors;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public SectorsDao(){
        sectors = new Sectors();
    }

    /**
     *
     * @return
     */
    public static String createSectorsTable(){
        return "CREATE TABLE 'sectors' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                "  'bm_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_bm_id_s' FOREIGN KEY ('bm_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_sct' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(Sectors sectors) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", sectors.getId());
        values.put("name", sectors.getName());
        values.put("creator_id", sectors.getCreatorId());
        values.put("deleted", sectors.getDeleted());
        values.put("creation_date", String.valueOf(sectors.getCreationDate()));
        values.put("updated_date", String.valueOf(sectors.getUpdatedDate()));

        // Inserting Row
        db.insert("sectors", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<Sectors> list = gson.fromJson(response, new TypeToken<List<Sectors>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Sectors sectors: list) {

                ContentValues values = new ContentValues();
                values.put("id", sectors.getId());
                values.put("name", sectors.getName());
                values.put("creator_id", sectors.getCreatorId());
                values.put("deleted", sectors.getDeleted());
                values.put("creation_date", String.valueOf(sectors.getCreationDate()));
                values.put("updated_date", String.valueOf(sectors.getUpdatedDate()));

                cnt = db.insertWithOnConflict("sectors", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
