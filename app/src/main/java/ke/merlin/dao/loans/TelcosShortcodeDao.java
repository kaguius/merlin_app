package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.Telcos;
import ke.merlin.models.loans.TelcosShortcodes;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.database.DatabaseManager;

/**
 * Created by mecmurimi on 25/09/2017.
 */

public class TelcosShortcodeDao {
    private TelcosShortcodes telcosShortcodes;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public TelcosShortcodeDao(){
        telcosShortcodes = new TelcosShortcodes();
    }

    /**
     *
     * @return
     */
    public static String createTelcosShortcodesTable(){
        return "CREATE TABLE 'telcos_shortcodes' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                "  'shortcode_type_id' varchar(150) NOT NULL,\n" +
                "  'telco_id' varchar(150) NOT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_stcds' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_shortcode_type_id' FOREIGN KEY ('shortcode_type_id') REFERENCES 'shortcode_types' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_telcos_id' FOREIGN KEY ('telco_id') REFERENCES 'telcos' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(TelcosShortcodes telcosShortcodes) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", telcosShortcodes.getId());
        values.put("name", telcosShortcodes.getName());
        values.put("description", telcosShortcodes.getDescription());
        values.put("shortcode_type_id", telcosShortcodes.getShortcodeTypeId());
        values.put("telco_id", telcosShortcodes.getTelcoId());
        values.put("creator_id", telcosShortcodes.getCreatorId());
        values.put("deleted", telcosShortcodes.getDeleted());
        values.put("creation_date", String.valueOf(telcosShortcodes.getCreationDate()));
        values.put("updated_date", String.valueOf(telcosShortcodes.getUpdatedDate()));

        // Inserting Row
        db.insert("telcos_shortcodes", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<TelcosShortcodes> list = gson.fromJson(response, new TypeToken<List<TelcosShortcodes>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (TelcosShortcodes telcosShortcodes: list) {

                ContentValues values = new ContentValues();
                values.put("id", telcosShortcodes.getId());
                values.put("name", telcosShortcodes.getName());
                values.put("description", telcosShortcodes.getDescription());
                values.put("shortcode_type_id", telcosShortcodes.getShortcodeTypeId());
                values.put("telco_id", telcosShortcodes.getTelcoId());
                values.put("creator_id", telcosShortcodes.getCreatorId());
                values.put("deleted", telcosShortcodes.getDeleted());
                values.put("creation_date", String.valueOf(telcosShortcodes.getCreationDate()));
                values.put("updated_date", String.valueOf(telcosShortcodes.getUpdatedDate()));

                cnt = db.insertWithOnConflict("telcos_shortcodes", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
