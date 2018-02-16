package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.Loans;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class LoansDao {

    private Loans loans;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LoansDao(){
        loans = new Loans();
    }

    /**
     *
     * @return
     */
    public static String createLoansTable(){
        return "CREATE TABLE 'loans' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'mobile' bigint(15) NOT NULL,\n" +
                "  'amount' double NOT NULL,\n" +
                "  'fees' int(11) NOT NULL DEFAULT '0',\n" +
                "  'interest' int(11) NOT NULL DEFAULT '0',\n" +
                "  'early_settlement' int(11) NOT NULL DEFAULT '0',\n" +
                "  'loan_date' date NOT NULL,\n" +
                "  'loan_due_date' date NOT NULL,\n" +
                "  'transaction_code' varchar(100) DEFAULT NULL,\n" +
                "  'loan_application_id' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'is_fraud' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'arrears_status_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'assignment_status_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'collector_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_arrears_status_lns' FOREIGN KEY ('arrears_status_id') REFERENCES 'st_loans_arrears_status' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_assignment_status_lns' FOREIGN KEY ('assignment_status_id') REFERENCES 'st_loans_assignment_status' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_collector_lns' FOREIGN KEY ('collector_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_l' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loans_application_lns' FOREIGN KEY ('loan_application_id') REFERENCES 'loans_applications' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(Loans loans) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", loans.getId());
        values.put("mobile", loans.getMobile());
        values.put("amount", loans.getAmount());
        values.put("fees", loans.getFees());
        values.put("interest", loans.getInterest());
        values.put("early_settlement", loans.getEarlySettlement());
        values.put("loan_date", loans.getLoanDate());
        values.put("loan_due_date", loans.getLoanDueDate());
        values.put("transaction_code", loans.getTransactionCode());
        values.put("loan_application_id", loans.getLoanApplicationId());
        values.put("is_fraud", loans.getIsFraud());
        values.put("arrears_status_id", loans.getArrearsStatusId());
        values.put("assignment_status_id", loans.getAssignmentStatusId());
        values.put("collector_id", loans.getCollectorId());
        values.put("creator_id", loans.getCreatorId());
        values.put("deleted", loans.getDeleted());
        values.put("creation_date", String.valueOf(loans.getCreationDate()));
        values.put("updated_date", String.valueOf(loans.getUpdatedDate()));

        // Inserting Row
        db.insert("loans", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<Loans> list = gson.fromJson(response, new TypeToken<List<Loans>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Loans loans : list) {

                ContentValues values = new ContentValues();
                values.put("id", loans.getId());
                values.put("mobile", loans.getMobile());
                values.put("amount", loans.getAmount());
                values.put("fees", loans.getFees());
                values.put("interest", loans.getInterest());
                values.put("early_settlement", loans.getEarlySettlement());
                values.put("loan_date", loans.getLoanDate());
                values.put("loan_due_date", loans.getLoanDueDate());
                values.put("transaction_code", loans.getTransactionCode());
                values.put("loan_application_id", loans.getLoanApplicationId());
                values.put("is_fraud", loans.getIsFraud());
                values.put("arrears_status_id", loans.getArrearsStatusId());
                values.put("assignment_status_id", loans.getAssignmentStatusId());
                values.put("collector_id", loans.getCollectorId());
                values.put("creator_id", loans.getCreatorId());
                values.put("deleted", loans.getDeleted());
                values.put("creation_date", String.valueOf(loans.getCreationDate()));
                values.put("updated_date", String.valueOf(loans.getUpdatedDate()));

                cnt = db.insertWithOnConflict("loans", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
