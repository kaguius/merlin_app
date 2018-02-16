package ke.merlin.dao.business;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.business.BusinessExpenses;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class BusinessExpensesDao {

    private BusinessExpenses businessExpenses;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public BusinessExpensesDao(){
        businessExpenses = new BusinessExpenses();
    }

    /**
     *
     * @return
     */
    public static String createBusinessExpensesTable(){
        return "CREATE TABLE 'business_expenses' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'business_rent' double NOT NULL,\n" +
                "  'business_utilities' double NOT NULL,\n" +
                "  'no_of_employees_types_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'nature_of_employment_types_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'employees_salary' double NOT NULL,\n" +
                "  'licensing_fee' double NOT NULL,\n" +
                "  'transport_fee' double NOT NULL,\n" +
                "  'storage_fee' double NOT NULL,\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                "  'site_visit_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_be' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_id_be' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_nature_of_employment_id' FOREIGN KEY ('nature_of_employment_types_id') REFERENCES 'st_nature_of_employment' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_no_of_employees_type_id_bexp' FOREIGN KEY ('no_of_employees_types_id') REFERENCES 'st_no_of_employees_range' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_site_visit_id_be' FOREIGN KEY ('id') REFERENCES 'site_visit' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(BusinessExpenses businessExpenses) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", businessExpenses.getId());
        values.put("business_rent", businessExpenses.getBusinessRent());
        values.put("business_utilities", businessExpenses.getBusinessUtilities());
        values.put("no_of_employees_types_id", businessExpenses.getNoOfEmployeesTypesId());
        values.put("nature_of_employment_types_id", businessExpenses.getNatureOfEmploymentTypesId());
        values.put("employees_salary", businessExpenses.getEmployeesSalary());
        values.put("licensing_fee", businessExpenses.getLicensingFee());
        values.put("transport_fee", businessExpenses.getTransportFee());
        values.put("storage_fee", businessExpenses.getStorageFee());
        values.put("customers_id", businessExpenses.getCustomersId());
        values.put("site_visit_id", businessExpenses.getSiteVisitId());
        values.put("creator_id", businessExpenses.getCreatorId());
        values.put("deleted", businessExpenses.getDeleted());
        values.put("creation_date", String.valueOf(businessExpenses.getCreationDate()));
        values.put("updated_date", String.valueOf(businessExpenses.getUpdatedDate()));

        // Inserting Row
        db.insert("business_expenses", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<BusinessExpenses> list = gson.fromJson(response, new TypeToken<List<BusinessExpenses>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (BusinessExpenses businessExpenses : list) {

                ContentValues values = new ContentValues();
                values.put("id", businessExpenses.getId());
                values.put("business_rent", businessExpenses.getBusinessRent());
                values.put("business_utilities", businessExpenses.getBusinessUtilities());
                values.put("no_of_employees_types_id", businessExpenses.getNoOfEmployeesTypesId());
                values.put("nature_of_employment_types_id", businessExpenses.getNatureOfEmploymentTypesId());
                values.put("employees_salary", businessExpenses.getEmployeesSalary());
                values.put("licensing_fee", businessExpenses.getLicensingFee());
                values.put("transport_fee", businessExpenses.getTransportFee());
                values.put("storage_fee", businessExpenses.getStorageFee());
                values.put("customers_id", businessExpenses.getCustomersId());
                values.put("site_visit_id", businessExpenses.getSiteVisitId());
                values.put("creator_id", businessExpenses.getCreatorId());
                values.put("deleted", businessExpenses.getDeleted());
                values.put("creation_date", String.valueOf(businessExpenses.getCreationDate()));
                values.put("updated_date", String.valueOf(businessExpenses.getUpdatedDate()));

                cnt = db.insertWithOnConflict("business_expenses", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
