package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.LoansBfc;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class LoansBfcDao {

    private LoansBfc loansBfc;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LoansBfcDao(){
        loansBfc = new LoansBfc();
    }

    /**
     *
     * @return
     */
    public static String createLoansBfcTable(){
        return "CREATE TABLE 'loans_bfc' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'loans_id' varchar(150) NOT NULL,\n" +
                "  'customer_id' varchar(150) NOT NULL,\n" +
                "  'suspicious' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'professional' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'first_loan' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'loans_history' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'timely_payments' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'willing_to_pay' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'pay_full_amount' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'payment_plan' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'customer_sector' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'notable_person' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'staff_assessment' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'default_reason_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'other_sources' text NOT NULL,\n" +
                "  'points_earned' int(11) DEFAULT NULL,\n" +
                "  'customer_state' varchar(150) DEFAULT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_bqa' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_lbfc' FOREIGN KEY ('customer_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loans_lbfc' FOREIGN KEY ('loans_id') REFERENCES 'loans' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_reason_default_lns_bfc' FOREIGN KEY ('default_reason_id') REFERENCES 'st_reason_for_default' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(LoansBfc loansBfc) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", loansBfc.getId());
        values.put("loans_id", loansBfc.getLoansId());
        values.put("customer_id", loansBfc.getCustomerId());
        values.put("default_reason_id", loansBfc.getDefaultReasonId());
        values.put("other_sources", loansBfc.getOtherSources());
        values.put("suspicious", loansBfc.getSuspicious());
        values.put("professional", loansBfc.getProfessional());
        values.put("first_loan", loansBfc.getFirstLoan());
        values.put("loans_history", loansBfc.getLoansHistory());
        values.put("willing_to_pay", loansBfc.getWillingToPay());
        values.put("timely_payments", loansBfc.getTimelyPayments());
        values.put("pay_full_amount", loansBfc.getPayFullAmount());
        values.put("payment_plan", loansBfc.getPaymentPlan());
        values.put("customer_sector", loansBfc.getCustomerSector());
        values.put("notable_person", loansBfc.getNotablePerson());
        values.put("staff_assessment", loansBfc.getStaffAssessment());
        values.put("points_earned", loansBfc.getPointsEarned());
        values.put("customer_state", loansBfc.getCustomerState());
        values.put("creator_id", loansBfc.getCreatorId());
        values.put("deleted", loansBfc.getDeleted());
        values.put("creation_date", String.valueOf(loansBfc.getCreationDate()));
        values.put("updated_date", String.valueOf(loansBfc.getUpdatedDate()));

        // Inserting Row
        db.insert("loans_bfc", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<LoansBfc> list = gson.fromJson(response, new TypeToken<List<LoansBfc>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (LoansBfc loansBfc : list) {
                ContentValues values = new ContentValues();
                values.put("id", loansBfc.getId());
                values.put("loans_id", loansBfc.getLoansId());
                values.put("customer_id", loansBfc.getCustomerId());
                values.put("default_reason_id", loansBfc.getDefaultReasonId());
                values.put("other_sources", loansBfc.getOtherSources());
                values.put("suspicious", loansBfc.getSuspicious());
                values.put("professional", loansBfc.getProfessional());
                values.put("first_loan", loansBfc.getFirstLoan());
                values.put("loans_history", loansBfc.getLoansHistory());
                values.put("willing_to_pay", loansBfc.getWillingToPay());
                values.put("timely_payments", loansBfc.getTimelyPayments());
                values.put("pay_full_amount", loansBfc.getPayFullAmount());
                values.put("payment_plan", loansBfc.getPaymentPlan());
                values.put("customer_sector", loansBfc.getCustomerSector());
                values.put("notable_person", loansBfc.getNotablePerson());
                values.put("staff_assessment", loansBfc.getStaffAssessment());
                values.put("points_earned", loansBfc.getPointsEarned());
                values.put("customer_state", loansBfc.getCustomerState());
                values.put("creator_id", loansBfc.getCreatorId());
                values.put("deleted", loansBfc.getDeleted());
                values.put("creation_date", String.valueOf(loansBfc.getCreationDate()));
                values.put("updated_date", String.valueOf(loansBfc.getUpdatedDate()));

                cnt = db.insertWithOnConflict("loans_bfc", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
