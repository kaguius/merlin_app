package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.StInteractionsCategories;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StInteractionsCategoriesDao {

    private StInteractionsCategories stInteractionsCategories;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StInteractionsCategoriesDao(){
        stInteractionsCategories = new StInteractionsCategories();
    }

    /**
     *
     * @return
     */
    public static String createInteractionsCategoriesTable(){
        return "CREATE TABLE 'st_interactions_categories' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_int_c' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StInteractionsCategories stInteractionsCategories) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stInteractionsCategories.getId());
        values.put("name", stInteractionsCategories.getName());
        values.put("creator_id", stInteractionsCategories.getCreatorId());
        values.put("deleted", stInteractionsCategories.getDeleted());
        values.put("creation_date", String.valueOf(stInteractionsCategories.getCreationDate()));
        values.put("updated_date", String.valueOf(stInteractionsCategories.getUpdatedDate()));

        // Inserting Row
        db.insert("st_interactions_categories", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StInteractionsCategories> list = gson.fromJson(response, new TypeToken<List<StInteractionsCategories>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StInteractionsCategories stInteractionsCategories: list) {

                ContentValues values = new ContentValues();
                values.put("id", stInteractionsCategories.getId());
                values.put("name", stInteractionsCategories.getName());
                values.put("creator_id", stInteractionsCategories.getCreatorId());
                values.put("deleted", stInteractionsCategories.getDeleted());
                values.put("creation_date", String.valueOf(stInteractionsCategories.getCreationDate()));
                values.put("updated_date", String.valueOf(stInteractionsCategories.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_interactions_categories", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
