package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.CustomersSocials;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class CustomersSocialsDao {

    private CustomersSocials customersSocials;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public CustomersSocialsDao(){
        customersSocials = new CustomersSocials();
    }

    /**
     *
     * @return
     */
    public static String createCustomersSocialsTable(){
        return "CREATE TABLE 'customers_socials' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'have_an_account' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'account_organisation' varchar(150) DEFAULT NULL,\n" +
                "  'ever_taken_a_loan' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'loan_organisation' varchar(150) DEFAULT NULL,\n" +
                "  'other_access_to_credit' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'other_credit_organisation' varchar(150) DEFAULT NULL,\n" +
                "  'any_current_loan' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'outstanding_loan_amount' double NOT NULL DEFAULT '0',\n" +
                "  'current_loan_organisation' varchar(150) DEFAULT NULL,\n" +
                "  'daily_customers' int(11) NOT NULL DEFAULT '0',\n" +
                "  'employed' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'education_level_id' varchar(150) NOT NULL DEFAULT '0',\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                "  'site_visit_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_account_organisation_cs' FOREIGN KEY ('account_organisation') REFERENCES 'st_credit_organisations_types' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_cs' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_current_loan_cs' FOREIGN KEY ('current_loan_organisation') REFERENCES 'st_credit_organisations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_cs' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_education_level_idcs' FOREIGN KEY ('education_level_id') REFERENCES 'st_education_levels' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loan_organisation_cs' FOREIGN KEY ('loan_organisation') REFERENCES 'st_credit_organisations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_others_organisation_cs' FOREIGN KEY ('other_credit_organisation') REFERENCES 'st_credit_organisations' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(CustomersSocials customersSocials) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", customersSocials.getId());
        values.put("have_an_account", customersSocials.getHaveAnAccount());
        values.put("account_organisation", customersSocials.getAccountOrganisation());
        values.put("ever_taken_a_loan", customersSocials.getEverTakenALoan());
        values.put("loan_organisation", customersSocials.getLoanOrganisation());
        values.put("other_access_to_credit", customersSocials.getOtherAccessToCredit());
        values.put("other_credit_organisation", customersSocials.getOtherCreditOrganisation());
        values.put("any_current_loan", customersSocials.getAnyCurrentLoan());
        values.put("outstanding_loan_amount", customersSocials.getOutstandingLoanAmount());
        values.put("current_loan_organisation", customersSocials.getCurrentLoanOrganisation());
        values.put("daily_customers", customersSocials.getDailyCustomers());
        values.put("employed", customersSocials.getEmployed());
        values.put("education_level_id", customersSocials.getEducationLevelId());
        values.put("customers_id", customersSocials.getCustomersId());
        values.put("site_visit_id", customersSocials.getSiteVisitId());
        values.put("creator_id", customersSocials.getCreatorId());
        values.put("deleted", customersSocials.getDeleted());
        values.put("creation_date", String.valueOf(customersSocials.getCreationDate()));
        values.put("updated_date", String.valueOf(customersSocials.getUpdatedDate()));

        // Inserting Row
        db.insert("customers_socials", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<CustomersSocials> list = gson.fromJson(response, new TypeToken<List<CustomersSocials>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (CustomersSocials customersSocials : list) {

                ContentValues values = new ContentValues();
                values.put("id", customersSocials.getId());
                values.put("have_an_account", customersSocials.getHaveAnAccount());
                values.put("account_organisation", customersSocials.getAccountOrganisation());
                values.put("ever_taken_a_loan", customersSocials.getEverTakenALoan());
                values.put("loan_organisation", customersSocials.getLoanOrganisation());
                values.put("other_access_to_credit", customersSocials.getOtherAccessToCredit());
                values.put("other_credit_organisation", customersSocials.getOtherCreditOrganisation());
                values.put("any_current_loan", customersSocials.getAnyCurrentLoan());
                values.put("outstanding_loan_amount", customersSocials.getOutstandingLoanAmount());
                values.put("current_loan_organisation", customersSocials.getCurrentLoanOrganisation());
                values.put("daily_customers", customersSocials.getDailyCustomers());
                values.put("employed", customersSocials.getEmployed());
                values.put("education_level_id", customersSocials.getEducationLevelId());
                values.put("customers_id", customersSocials.getCustomersId());
                values.put("site_visit_id", customersSocials.getSiteVisitId());
                values.put("creator_id", customersSocials.getCreatorId());
                values.put("deleted", customersSocials.getDeleted());
                values.put("creation_date", String.valueOf(customersSocials.getCreationDate()));
                values.put("updated_date", String.valueOf(customersSocials.getUpdatedDate()));

                cnt = db.insertWithOnConflict("customers_socials", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
