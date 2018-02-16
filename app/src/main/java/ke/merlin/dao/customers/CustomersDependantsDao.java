package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.CustomersDependants;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class CustomersDependantsDao {

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
    private CustomersDependants customersDependants;

    public CustomersDependantsDao() {
        customersDependants = new CustomersDependants();
    }

    /**
     * @return
     */
    public static String createCustomersDependantsTable() {
        return "CREATE TABLE 'customers_dependants' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customer_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'first_name' varchar(100) DEFAULT NULL,\n" +
                "  'last_name' varchar(100) DEFAULT NULL,\n" +
                "  'date_of_birth' date NOT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_cds' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_id_cus_deps' FOREIGN KEY ('customer_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(CustomersDependants customersDependants) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("id", customersDependants.getId());
        values.put("customer_id", customersDependants.getCustomerId());
        values.put("first_name", customersDependants.getFirstName());
        values.put("last_name", customersDependants.getLastName());
        values.put("date_of_birth", String.valueOf(customersDependants.getDateOfBirth()));
        values.put("creator_id", customersDependants.getCreatorId());
        values.put("deleted", customersDependants.getDeleted());
        values.put("creation_date", String.valueOf(customersDependants.getCreationDate()));
        values.put("updated_date", String.valueOf(customersDependants.getUpdatedDate()));

        // Inserting Row
        db.insert("customers_dependants", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<CustomersDependants> list = gson.fromJson(response, new TypeToken<List<CustomersDependants>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (CustomersDependants customersDependants : list) {

                ContentValues values = new ContentValues();
                values.put("id", customersDependants.getId());
                values.put("customer_id", customersDependants.getCustomerId());
                values.put("first_name", customersDependants.getFirstName());
                values.put("last_name", customersDependants.getLastName());
                values.put("date_of_birth", String.valueOf(customersDependants.getDateOfBirth()));
                values.put("creator_id", customersDependants.getCreatorId());
                values.put("deleted", customersDependants.getDeleted());
                values.put("creation_date", String.valueOf(customersDependants.getCreationDate()));
                values.put("updated_date", String.valueOf(customersDependants.getUpdatedDate()));

                cnt = db.insertWithOnConflict("customers_dependants", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
