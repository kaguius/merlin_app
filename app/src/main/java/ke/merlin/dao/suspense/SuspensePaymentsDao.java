package ke.merlin.dao.suspense;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.models.suspense.SuspensePayments;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.database.DatabaseManager;

/**
 * Created by mecmurimi on 25/09/2017.
 */

public class SuspensePaymentsDao {
    private SuspensePayments suspensePayments;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public SuspensePaymentsDao(){
        suspensePayments = new SuspensePayments();
    }

    /**
     * @return
     */
    public static String createSuspensePaymentsTable(){
        return "CREATE TABLE 'suspense_payments' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'account_number' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'mobile' bigint(15) NOT NULL,\n" +
                "  'shortcode' varchar(10) NOT NULL DEFAULT '',\n" +
                "  'amount' double NOT NULL DEFAULT '0',\n" +
                "  'payment_date' date NOT NULL,\n" +
                "  'resolved' tinyint(1) NOT NULL DEFAULT '0',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_spmnts' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(SuspensePayments siteVisit) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", suspensePayments.getId());
        values.put("name", suspensePayments.getName());
        values.put("account_number", suspensePayments.getAccountNumber());
        values.put("mobile", suspensePayments.getMobile());
        values.put("shortcode", suspensePayments.getShortcode());
        values.put("amount", suspensePayments.getAmount());
        values.put("payment_date", suspensePayments.getPaymentDate());
        values.put("resolved", suspensePayments.getResolved());
        values.put("creator_id", suspensePayments.getCreatorId());
        values.put("deleted", suspensePayments.getDeleted());
        values.put("creation_date", String.valueOf(suspensePayments.getCreationDate()));
        values.put("updated_date", String.valueOf(suspensePayments.getUpdatedDate()));

        // Inserting Row
        db.insert("suspense_payments", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<SuspensePayments> list = gson.fromJson(response, new TypeToken<List<SuspensePayments>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (SuspensePayments suspensePayments: list) {

                ContentValues values = new ContentValues();
                values.put("id", suspensePayments.getId());
                values.put("name", suspensePayments.getName());
                values.put("account_number", suspensePayments.getAccountNumber());
                values.put("mobile", suspensePayments.getMobile());
                values.put("shortcode", suspensePayments.getShortcode());
                values.put("amount", suspensePayments.getAmount());
                values.put("payment_date", suspensePayments.getPaymentDate());
                values.put("resolved", suspensePayments.getResolved());
                values.put("creator_id", suspensePayments.getCreatorId());
                values.put("deleted", suspensePayments.getDeleted());
                values.put("creation_date", String.valueOf(suspensePayments.getCreationDate()));
                values.put("updated_date", String.valueOf(suspensePayments.getUpdatedDate()));

                cnt = db.insertWithOnConflict("suspense_payments", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
