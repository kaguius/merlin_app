package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.LoansOverpayments;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class LoansOverpaymentsDao {

    private LoansOverpayments loansOverpayments;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LoansOverpaymentsDao(){
        loansOverpayments = new LoansOverpayments();
    }

    /**
     *
     * @return
     */
    public static String createLoansOverpaymentTable(){
        return "CREATE TABLE 'loans_overpayments' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customer_id' varchar(150) NOT NULL,\n" +
                "  'from_loans_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'to_loans_id' varchar(150) DEFAULT '',\n" +
                "  'amount' double NOT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_lo' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_lover' FOREIGN KEY ('customer_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_overpaid_loans_id' FOREIGN KEY ('from_loans_id') REFERENCES 'loans' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_used_loans_id' FOREIGN KEY ('to_loans_id') REFERENCES 'loans' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(LoansOverpayments loansOverpayments) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", loansOverpayments.getId());
        values.put("customer_id", loansOverpayments.getCustomerId());
        values.put("from_loans_id", loansOverpayments.getFromLoansId());
        values.put("to_loans_id", loansOverpayments.getToLoansId());
        values.put("amount", loansOverpayments.getAmount());
        values.put("creator_id", loansOverpayments.getCreatorId());
        values.put("deleted", loansOverpayments.getDeleted());
        values.put("creation_date", String.valueOf(loansOverpayments.getCreationDate()));
        values.put("updated_date", String.valueOf(loansOverpayments.getUpdatedDate()));

        // Inserting Row
        db.insert("loans_overpayments", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<LoansOverpayments> list = gson.fromJson(response, new TypeToken<List<LoansOverpayments>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (LoansOverpayments loansOverpayments : list) {
                ContentValues values = new ContentValues();
                values.put("id", loansOverpayments.getId());
                values.put("customer_id", loansOverpayments.getCustomerId());
                values.put("from_loans_id", loansOverpayments.getFromLoansId());
                values.put("to_loans_id", loansOverpayments.getToLoansId());
                values.put("amount", loansOverpayments.getAmount());
                values.put("creator_id", loansOverpayments.getCreatorId());
                values.put("deleted", loansOverpayments.getDeleted());
                values.put("creation_date", String.valueOf(loansOverpayments.getCreationDate()));
                values.put("updated_date", String.valueOf(loansOverpayments.getUpdatedDate()));

                cnt = db.insertWithOnConflict("loans_overpayments", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
