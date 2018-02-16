package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ke.merlin.models.customers.StInfoSources;
import ke.merlin.models.loans.Products;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class ProductsDao {

    private Products products;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public ProductsDao(){
        products = new Products();
    }

    /**
     *
     * @return
     */
    public static String createProductsTable(){
        return "CREATE TABLE 'products' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                "  'interest' double NOT NULL DEFAULT '0',\n" +
                "  'interest_cycle_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'minimum_interest_period' int(11) NOT NULL,\n" +
                "  'minimum_interest_cycle_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'max_loan_period' int(11) NOT NULL DEFAULT '0',\n" +
                "  'max_loan_cycle_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'interest_charge_type_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'country_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_country_id_p' FOREIGN KEY ('country_id') REFERENCES 'st_countries' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_lpds' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_interest_charge_p' FOREIGN KEY ('interest_charge_type_id') REFERENCES 'st_charge_type' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_interest_cycles_id_lp' FOREIGN KEY ('interest_cycle_id') REFERENCES 'st_duration_frequency' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_max_loan_cycle_p' FOREIGN KEY ('max_loan_cycle_id') REFERENCES 'st_durations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_minimum_interest_cycle' FOREIGN KEY ('minimum_interest_cycle_id') REFERENCES 'st_durations' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(Products products) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", products.getId());
        values.put("name", products.getName());
        values.put("creator_id", products.getCreatorId());
        values.put("description", products.getDescription());
        values.put("interest", products.getInterest());
        values.put("interest_cycle_id", products.getInterestCycleId());
        values.put("minimum_interest_period", products.getMinimumInterestPeriod());
        values.put("minimum_interest_cycle_id", products.getMinimumInterestCycleId());
        values.put("max_loan_period", products.getMaxLoanPeriod());
        values.put("max_loan_cycle_id", products.getMaxLoanCycleId());
        values.put("interest_charge_type_id", products.getInterestChargeTypeId());
        values.put("country_id", products.getCountryId());
        values.put("deleted", products.getDeleted());
        values.put("creation_date", String.valueOf(products.getCreationDate()));
        values.put("updated_date", String.valueOf(products.getUpdatedDate()));

        // Inserting Row
        db.insert("products", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<Products> list = gson.fromJson(response, new TypeToken<List<Products>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Products products: list) {

                ContentValues values = new ContentValues();
                values.put("id", products.getId());
                values.put("name", products.getName());
                values.put("creator_id", products.getCreatorId());
                values.put("description", products.getDescription());
                values.put("interest", products.getInterest());
                values.put("interest_cycle_id", products.getInterestCycleId());
                values.put("minimum_interest_period", products.getMinimumInterestPeriod());
                values.put("minimum_interest_cycle_id", products.getMinimumInterestCycleId());
                values.put("max_loan_period", products.getMaxLoanPeriod());
                values.put("max_loan_cycle_id", products.getMaxLoanCycleId());
                values.put("interest_charge_type_id", products.getInterestChargeTypeId());
                values.put("country_id", products.getCountryId());
                values.put("deleted", products.getDeleted());
                values.put("creation_date", String.valueOf(products.getCreationDate()));
                values.put("updated_date", String.valueOf(products.getUpdatedDate()));

                cnt = db.insertWithOnConflict("products", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerTypes() {
        Products products;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from products";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                products= new Products();
                products.setId(cursor.getString(cursor.getColumnIndex("id")));
                products.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(products.getId(), products.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }
}
