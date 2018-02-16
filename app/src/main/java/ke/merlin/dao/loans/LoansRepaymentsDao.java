package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.LoansRepayments;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 27/07/2017.
 */

public class LoansRepaymentsDao {

    private LoansRepayments loansRepayments;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LoansRepaymentsDao(){
        loansRepayments = new LoansRepayments();
    }

    /**
     *
     * @return
     */
    public static String createLoansRepaymentsTable(){
        return "CREATE TABLE 'loans_repayments' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'loans_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'mobile' bigint(15) NOT NULL,\n" +
                "  'amount' double NOT NULL DEFAULT '0',\n" +
                "  'shortcode' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'payment_date' date NOT NULL,\n" +
                "  'collector_id' varchar(150) DEFAULT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_collector_id_lr' FOREIGN KEY ('collector_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_lrp' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loans_id_lr' FOREIGN KEY ('loans_id') REFERENCES 'loans' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_shortcodes_lns_rep' FOREIGN KEY ('shortcode') REFERENCES 'telcos_shortcodes' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(LoansRepayments loansRepayments) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", loansRepayments.getId());
        values.put("loans_id", loansRepayments.getLoansId());
        values.put("mobile", loansRepayments.getMobile());
        values.put("amount", loansRepayments.getAmount());
        values.put("shortcode", loansRepayments.getShortcode());
        values.put("payment_date", loansRepayments.getPaymentDate());
        values.put("collector_id", loansRepayments.getCollectorId());
        values.put("creator_id", loansRepayments.getCreatorId());
        values.put("deleted", loansRepayments.getDeleted());
        values.put("creation_date", String.valueOf(loansRepayments.getCreationDate()));
        values.put("updated_date", String.valueOf(loansRepayments.getUpdatedDate()));

        // Inserting Row
        db.insert("loans_repayments", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<LoansRepayments> list = gson.fromJson(response, new TypeToken<List<LoansRepayments>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (LoansRepayments loansRepayments : list) {

                ContentValues values = new ContentValues();
                values.put("id", loansRepayments.getId());
                values.put("loans_id", loansRepayments.getLoansId());
                values.put("mobile", loansRepayments.getMobile());
                values.put("amount", loansRepayments.getAmount());
                values.put("shortcode", loansRepayments.getShortcode());
                values.put("payment_date", loansRepayments.getPaymentDate());
                values.put("collector_id", loansRepayments.getCollectorId());
                values.put("creator_id", loansRepayments.getCreatorId());
                values.put("deleted", loansRepayments.getDeleted());
                values.put("creation_date", String.valueOf(loansRepayments.getCreationDate()));
                values.put("updated_date", String.valueOf(loansRepayments.getUpdatedDate()));

                cnt = db.insertWithOnConflict("loans_repayments", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
