package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.StReasonForDefault;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StReasonForDefaultDao {

    private StReasonForDefault stReasonForDefault;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StReasonForDefaultDao(){
        stReasonForDefault = new StReasonForDefault();
    }

    /**
     *
     * @return
     */
    public static String createReasonForDefaultTable(){
        return "CREATE TABLE 'st_reason_for_default' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                ")";
    }

    public void insert(StReasonForDefault stReasonForDefault) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stReasonForDefault.getId());
        values.put("name", stReasonForDefault.getName());
        values.put("creator_id", stReasonForDefault.getCreatorId());
        values.put("deleted", stReasonForDefault.getDeleted());
        values.put("creation_date", String.valueOf(stReasonForDefault.getCreationDate()));
        values.put("updated_date", String.valueOf(stReasonForDefault.getUpdatedDate()));

        // Inserting Row
        db.insert("st_reason_for_default", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StReasonForDefault> list = gson.fromJson(response, new TypeToken<List<StReasonForDefault>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StReasonForDefault stReasonForDefault: list) {

                ContentValues values = new ContentValues();
                values.put("id", stReasonForDefault.getId());
                values.put("name", stReasonForDefault.getName());
                values.put("creator_id", stReasonForDefault.getCreatorId());
                values.put("deleted", stReasonForDefault.getDeleted());
                values.put("creation_date", String.valueOf(stReasonForDefault.getCreationDate()));
                values.put("updated_date", String.valueOf(stReasonForDefault.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_reason_for_default", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
