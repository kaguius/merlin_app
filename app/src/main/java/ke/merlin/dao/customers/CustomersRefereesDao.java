package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.CustomersReferees;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class CustomersRefereesDao {

    private CustomersReferees customersReferees;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public CustomersRefereesDao(){
        customersReferees = new CustomersReferees();
    }

    /**
     *
     * @return
     */
    public static String createCustomersRefereesTable(){
        return "CREATE TABLE 'customers_referees' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customers_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'first_name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'last_name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'mobile' bigint(20) NOT NULL,\n" +
                "  'relationship_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'also_known_as' varchar(150) DEFAULT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_cref' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_cref' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_relationships_cref' FOREIGN KEY ('relationship_id') REFERENCES 'st_referees_relationship' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(CustomersReferees customersReferees) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", customersReferees.getId());
        values.put("customers_id", customersReferees.getCustomersId());
        values.put("first_name", customersReferees.getFirstName());
        values.put("last_name", customersReferees.getLastName());
        values.put("also_known_as", customersReferees.getAlsoKnownAs());
        values.put("mobile", customersReferees.getMobile());
        values.put("relationship_id", customersReferees.getRelationshipId());
        values.put("creator_id", customersReferees.getCreatorId());
        values.put("deleted", customersReferees.getDeleted());
        values.put("creation_date", String.valueOf(customersReferees.getCreationDate()));
        values.put("updated_date", String.valueOf(customersReferees.getUpdatedDate()));

        // Inserting Row
        db.insert("customers_referees", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<CustomersReferees> list = gson.fromJson(response, new TypeToken<List<CustomersReferees>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (CustomersReferees customersReferees : list) {
                ContentValues values = new ContentValues();
                values.put("id", customersReferees.getId());
                values.put("customers_id", customersReferees.getCustomersId());
                values.put("first_name", customersReferees.getFirstName());
                values.put("last_name", customersReferees.getLastName());
                values.put("also_known_as", customersReferees.getAlsoKnownAs());
                values.put("mobile", customersReferees.getMobile());
                values.put("relationship_id", customersReferees.getRelationshipId());
                values.put("creator_id", customersReferees.getCreatorId());
                values.put("deleted", customersReferees.getDeleted());
                values.put("creation_date", String.valueOf(customersReferees.getCreationDate()));
                values.put("updated_date", String.valueOf(customersReferees.getUpdatedDate()));

                cnt = db.insertWithOnConflict("customers_referees", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
