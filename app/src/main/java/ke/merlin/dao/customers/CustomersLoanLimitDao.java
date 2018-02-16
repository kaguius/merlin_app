package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.CustomersLoanLimit;
import ke.merlin.models.customers.LeadsOutcomes;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.database.DatabaseManager;

/**
 * Created by mecmurimi on 24/09/2017.
 */

public class CustomersLoanLimitDao {
    private CustomersLoanLimit customersLoanLimit;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public CustomersLoanLimitDao(){
        customersLoanLimit = new CustomersLoanLimit();
    }

    /**
     *
     * @return
     */
    public static String createCustomersLoanLimitTable(){
        return "CREATE TABLE 'customers_loan_limit' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customers_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'comments' text NOT NULL,\n" +
                "  'loan_amount' int(11) NOT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_cll' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_id_cll' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(CustomersLoanLimit customersLoanLimit) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", customersLoanLimit.getId());
        values.put("customers_id", customersLoanLimit.getCustomersId());
        values.put("comments", customersLoanLimit.getComments());
        values.put("loan_amount", customersLoanLimit.getLoanAmount());
        values.put("creator_id", customersLoanLimit.getCreatorId());
        values.put("deleted", customersLoanLimit.getDeleted());
        values.put("creation_date", String.valueOf(customersLoanLimit.getCreationDate()));
        values.put("updated_date", String.valueOf(customersLoanLimit.getUpdatedDate()));

        // Inserting Row
        db.insert("customers_loan_limit", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<CustomersLoanLimit> list = gson.fromJson(response, new TypeToken<List<CustomersLoanLimit>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (CustomersLoanLimit customersLoanLimit: list) {

                ContentValues values = new ContentValues();
                values.put("id", customersLoanLimit.getId());
                values.put("customers_id", customersLoanLimit.getCustomersId());
                values.put("comments", customersLoanLimit.getComments());
                values.put("loan_amount", customersLoanLimit.getLoanAmount());
                values.put("creator_id", customersLoanLimit.getCreatorId());
                values.put("deleted", customersLoanLimit.getDeleted());
                values.put("creation_date", String.valueOf(customersLoanLimit.getCreationDate()));
                values.put("updated_date", String.valueOf(customersLoanLimit.getUpdatedDate()));


                cnt = db.insertWithOnConflict("customers_loan_limit", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }



        return cnt;
    }
}
