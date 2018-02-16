package ke.merlin.dao.sms;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.sms.SmsOutgoing;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 25/07/2017.
 */

public class SmsOutgoingDao {
    private SmsOutgoing smsOutgoing;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public SmsOutgoingDao(){
        smsOutgoing = new SmsOutgoing();
    }

    /**
     *
     * @return
     */
    public static String createSmsOutgoingTable(){
        return "CREATE TABLE 'sms_outgoing' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customer_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'mobile' bigint(15) NOT NULL,\n" +
                "  'message' text NOT NULL,\n" +
                "  'is_delivered' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'shortcode_id' varchar(150) NOT NULL,\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_otgs' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_sms_out' FOREIGN KEY ('customer_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_shortcodes_os' FOREIGN KEY ('shortcode_id') REFERENCES 'telcos_shortcodes' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(SmsOutgoing smsOutgoing) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("id", smsOutgoing.getId());
        values.put("mobile", smsOutgoing.getMobile());
        values.put("creator_id", smsOutgoing.getCreatorId());
        values.put("message", smsOutgoing.getMessage());
        values.put("is_delivered", smsOutgoing.getIsDelivered());
        values.put("shortcode_id", smsOutgoing.getShortcodeId());
        values.put("customer_id", smsOutgoing.getCustomerId());
        values.put("deleted", smsOutgoing.getDeleted());
        values.put("creation_date", String.valueOf(smsOutgoing.getCreationDate()));
        values.put("updated_date", String.valueOf(smsOutgoing.getUpdatedDate()));

        // Inserting Row
        db.insert("sms_outgoing", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<SmsOutgoing> list = gson.fromJson(response, new TypeToken<List<SmsOutgoing>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (SmsOutgoing smsOutgoing: list) {

                ContentValues values = new ContentValues();
                values.put("id", smsOutgoing.getId());
                values.put("mobile", smsOutgoing.getMobile());
                values.put("creator_id", smsOutgoing.getCreatorId());
                values.put("message", smsOutgoing.getMessage());
                values.put("is_delivered", smsOutgoing.getIsDelivered());
                values.put("shortcode_id", smsOutgoing.getShortcodeId());
                values.put("customer_id", smsOutgoing.getCustomerId());
                values.put("deleted", smsOutgoing.getDeleted());
                values.put("creation_date", String.valueOf(smsOutgoing.getCreationDate()));
                values.put("updated_date", String.valueOf(smsOutgoing.getUpdatedDate()));


                cnt = db.insertWithOnConflict("sms_outgoing", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();

        }

        return cnt;
    }
}
