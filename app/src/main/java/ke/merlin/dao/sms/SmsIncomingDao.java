package ke.merlin.dao.sms;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.sms.SmsIncoming;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 25/07/2017.
 */

public class SmsIncomingDao {
    private SmsIncoming smsIncoming;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public SmsIncomingDao(){
        smsIncoming = new SmsIncoming();
    }

    /**
     *
     * @return
     */
    public static String createSmsIncomingTable(){
        return "CREATE TABLE 'sms_incoming' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customer_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'mobile' bigint(15) NOT NULL,\n" +
                "  'message' text NOT NULL,\n" +
                "  'shortcode_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_is' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_sms_inc' FOREIGN KEY ('customer_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_shortcode_id_is' FOREIGN KEY ('shortcode_id') REFERENCES 'telcos_shortcodes' ('id') ON UPDATE CASCADE\n" +
                ")";
    }


    public void insert(SmsIncoming smsIncoming) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("id", smsIncoming.getId());
        values.put("mobile", smsIncoming.getMobile());
        values.put("creator_id", smsIncoming.getCreatorId());
        values.put("message", smsIncoming.getMessage());
        values.put("shortcode_id", smsIncoming.getShortcodeId());
        values.put("customer_id", smsIncoming.getCustomerId());
        values.put("deleted", smsIncoming.getDeleted());
        values.put("creation_date", String.valueOf(smsIncoming.getCreationDate()));
        values.put("updated_date", String.valueOf(smsIncoming.getUpdatedDate()));

        // Inserting Row
        db.insert("sms_incoming", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<SmsIncoming> list = gson.fromJson(response, new TypeToken<List<SmsIncoming>>() {}.getType());
        if (list != null && !list.isEmpty()) {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (SmsIncoming smsIncoming: list) {

                ContentValues values = new ContentValues();
                values.put("id", smsIncoming.getId());
                values.put("mobile", smsIncoming.getMobile());
                values.put("creator_id", smsIncoming.getCreatorId());
                values.put("message", smsIncoming.getMessage());
                values.put("shortcode_id", smsIncoming.getShortcodeId());
                values.put("customer_id", smsIncoming.getCustomerId());
                values.put("deleted", smsIncoming.getDeleted());
                values.put("creation_date", String.valueOf(smsIncoming.getCreationDate()));
                values.put("updated_date", String.valueOf(smsIncoming.getUpdatedDate()));


                cnt = db.insertWithOnConflict("sms_incoming", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();

        }

        return cnt;
    }
}
