package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.StInteractionsCallOutcomes;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StInteractionsCallOutcomessDao {

    private StInteractionsCallOutcomes stInteractionsCallOutcomes;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StInteractionsCallOutcomessDao(){
        stInteractionsCallOutcomes = new StInteractionsCallOutcomes();
    }

    /**
     *
     * @return
     */
    public static String createInteractionCallOutcomesTable(){
        return "CREATE TABLE 'st_interactions_call_outcomes' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'desciption' text,\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'is_synced_created' tinyint(1) NOT NULL DEFAULT '1',\n" +
                "  'is_synced_updated' tinyint(1) NOT NULL DEFAULT '1',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_iot' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StInteractionsCallOutcomes stInteractionsCallOutcomes) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stInteractionsCallOutcomes.getId());
        values.put("name", stInteractionsCallOutcomes.getName());
        values.put("creator_id", stInteractionsCallOutcomes.getCreatorId());
        values.put("deleted", stInteractionsCallOutcomes.getDeleted());
        values.put("creation_date", String.valueOf(stInteractionsCallOutcomes.getCreationDate()));
        values.put("updated_date", String.valueOf(stInteractionsCallOutcomes.getUpdatedDate()));

        // Inserting Row
        db.insert("st_interactions_call_outcomes", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StInteractionsCallOutcomes> list = gson.fromJson(response, new TypeToken<List<StInteractionsCallOutcomes>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StInteractionsCallOutcomes stInteractionsCallOutcomes: list) {

                ContentValues values = new ContentValues();
                values.put("id", stInteractionsCallOutcomes.getId());
                values.put("name", stInteractionsCallOutcomes.getName());
                values.put("creator_id", stInteractionsCallOutcomes.getCreatorId());
                values.put("deleted", stInteractionsCallOutcomes.getDeleted());
                values.put("creation_date", String.valueOf(stInteractionsCallOutcomes.getCreationDate()));
                values.put("updated_date", String.valueOf(stInteractionsCallOutcomes.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_interactions_call_outcomes", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
