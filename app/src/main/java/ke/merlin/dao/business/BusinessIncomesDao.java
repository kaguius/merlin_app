package ke.merlin.dao.business;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.business.BusinessIncomes;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class BusinessIncomesDao {

    private BusinessIncomes businessIncomes;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public BusinessIncomesDao(){
        businessIncomes = new BusinessIncomes();
    }

    /**
     *
     * @return
     */
    public static String createBusinessIncomesTable(){
        return "CREATE TABLE 'business_incomes' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'business_cycle_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'current_stock_value' double NOT NULL,\n" +
                "  'sales_per_cycle' double NOT NULL,\n" +
                "  'spend_on_stock' double NOT NULL,\n" +
                "  'income_explanation' text NOT NULL,\n" +
                "  'cycle_restocking_frequency' int(11) NOT NULL,\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                "  'site_visit_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_business_cycle_id_bus_met' FOREIGN KEY ('business_cycle_id') REFERENCES 'st_business_cycles' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_id_bus_met' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_id_bi' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_site_visit_id_bi' FOREIGN KEY ('site_visit_id') REFERENCES 'site_visit' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(BusinessIncomes businessIncomes) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", businessIncomes.getId());
        values.put("business_cycle_id", businessIncomes.getBusinessCycleId());
        values.put("current_stock_value", businessIncomes.getCurrentStockValue());
        values.put("sales_per_cycle", businessIncomes.getSalesPerCycle());
        values.put("spend_on_stock", businessIncomes.getSpendOnStock());
        values.put("income_explanation", businessIncomes.getIncomeExplanation());
        values.put("cycle_restocking_frequency", businessIncomes.getCycleRestockingFrequency());
        values.put("customers_id", businessIncomes.getCustomersId());
        values.put("site_visit_id", businessIncomes.getSiteVisitId());
        values.put("creator_id", businessIncomes.getCreatorId());
        values.put("deleted", businessIncomes.getDeleted());
        values.put("creation_date", String.valueOf(businessIncomes.getCreationDate()));
        values.put("updated_date", String.valueOf(businessIncomes.getUpdatedDate()));

        // Inserting Row
        db.insert("business_incomes", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<BusinessIncomes> list = gson.fromJson(response, new TypeToken<List<BusinessIncomes>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (BusinessIncomes businessIncomes : list) {

                ContentValues values = new ContentValues();
                values.put("id", businessIncomes.getId());
                values.put("business_cycle_id", businessIncomes.getBusinessCycleId());
                values.put("current_stock_value", businessIncomes.getCurrentStockValue());
                values.put("sales_per_cycle", businessIncomes.getSalesPerCycle());
                values.put("spend_on_stock", businessIncomes.getSpendOnStock());
                values.put("income_explanation", businessIncomes.getIncomeExplanation());
                values.put("cycle_restocking_frequency", businessIncomes.getCycleRestockingFrequency());
                values.put("customers_id", businessIncomes.getCustomersId());
                values.put("site_visit_id", businessIncomes.getSiteVisitId());
                values.put("creator_id", businessIncomes.getCreatorId());
                values.put("deleted", businessIncomes.getDeleted());
                values.put("creation_date", String.valueOf(businessIncomes.getCreationDate()));
                values.put("updated_date", String.valueOf(businessIncomes.getUpdatedDate()));

                cnt = db.insertWithOnConflict("business_incomes", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
