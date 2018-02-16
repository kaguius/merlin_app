package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.CustomersExpenses;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class CustomersExpensesDao {

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
    private CustomersExpenses customersExpenses;

    public CustomersExpensesDao() {
        customersExpenses = new CustomersExpenses();
    }

    /**
     * @return
     */
    public static String createCustomersExpensesTable() {
        return "CREATE TABLE 'customers_expenses' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'house_type_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'home_rent' double NOT NULL,\n" +
                "  'house_utilities' double NOT NULL,\n" +
                "  'food_cost' double NOT NULL,\n" +
                "  'school_fees' double NOT NULL,\n" +
                "  'saving_amount' double NOT NULL,\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                "  'site_visit_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_cusme' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_cusme' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_house_type_cusme' FOREIGN KEY ('house_type_id') REFERENCES 'st_house_types' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_site_visit_id_ce' FOREIGN KEY ('site_visit_id') REFERENCES 'customers_expenses' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(CustomersExpenses customersExpenses) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", customersExpenses.getId());
        values.put("house_type_id", customersExpenses.getHouseTypeId());
        values.put("home_rent", customersExpenses.getHomeRent());
        values.put("house_utilities", customersExpenses.getHouseUtilities());
        values.put("food_cost", customersExpenses.getFoodCost());
        values.put("school_fees", customersExpenses.getSchoolFees());
        values.put("saving_amount", customersExpenses.getSavingAmount());
        values.put("customers_id", customersExpenses.getCustomersId());
        values.put("site_visit_id", customersExpenses.getSiteVisitId());
        values.put("creator_id", customersExpenses.getCreatorId());
        values.put("deleted", customersExpenses.getDeleted());
        values.put("creation_date", String.valueOf(customersExpenses.getCreationDate()));
        values.put("updated_date", String.valueOf(customersExpenses.getUpdatedDate()));

        // Inserting Row
        db.insert("customers_expenses", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<CustomersExpenses> list = gson.fromJson(response, new TypeToken<List<CustomersExpenses>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (CustomersExpenses customersExpenses : list) {

                ContentValues values = new ContentValues();
                values.put("id", customersExpenses.getId());
                values.put("house_type_id", customersExpenses.getHouseTypeId());
                values.put("home_rent", customersExpenses.getHomeRent());
                values.put("house_utilities", customersExpenses.getHouseUtilities());
                values.put("food_cost", customersExpenses.getFoodCost());
                values.put("school_fees", customersExpenses.getSchoolFees());
                values.put("saving_amount", customersExpenses.getSavingAmount());
                values.put("customers_id", customersExpenses.getCustomersId());
                values.put("site_visit_id", customersExpenses.getSiteVisitId());
                values.put("creator_id", customersExpenses.getCreatorId());
                values.put("deleted", customersExpenses.getDeleted());
                values.put("creation_date", String.valueOf(customersExpenses.getCreationDate()));
                values.put("updated_date", String.valueOf(customersExpenses.getUpdatedDate()));

                cnt = db.insertWithOnConflict("customers_expenses", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
