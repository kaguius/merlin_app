package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.StRefereesRelationship;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StRefereesRelationshipDao {

    private StRefereesRelationship stRefereesRelationship;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StRefereesRelationshipDao(){
        stRefereesRelationship = new StRefereesRelationship();
    }

    /**
     * @return
     */
    public static String createRefereeRelationshipTable(){
        return "CREATE TABLE 'st_referees_relationship' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text NOT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                ")";
    }

    public void insert(StRefereesRelationship stRefereesRelationship) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stRefereesRelationship.getId());
        values.put("name", stRefereesRelationship.getName());
        values.put("description", stRefereesRelationship.getDescription());
        values.put("creator_id", stRefereesRelationship.getCreatorId());
        values.put("deleted", stRefereesRelationship.getDeleted());
        values.put("creation_date", String.valueOf(stRefereesRelationship.getCreationDate()));
        values.put("updated_date", String.valueOf(stRefereesRelationship.getUpdatedDate()));

        // Inserting Row
        db.insert("st_referees_relationship", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StRefereesRelationship> list = gson.fromJson(response, new TypeToken<List<StRefereesRelationship>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StRefereesRelationship stRefereesRelationship: list) {

                ContentValues values = new ContentValues();
                values.put("id", stRefereesRelationship.getId());
                values.put("name", stRefereesRelationship.getName());
                values.put("description", stRefereesRelationship.getDescription());
                values.put("creator_id", stRefereesRelationship.getCreatorId());
                values.put("deleted", stRefereesRelationship.getDeleted());
                values.put("creation_date", String.valueOf(stRefereesRelationship.getCreationDate()));
                values.put("updated_date", String.valueOf(stRefereesRelationship.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_referees_relationship", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
