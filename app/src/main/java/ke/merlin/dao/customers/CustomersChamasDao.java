package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.CustomersChamas;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class CustomersChamasDao {

    private CustomersChamas customersChamas;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public CustomersChamasDao(){
        customersChamas = new CustomersChamas();
    }

    /**
     *
     * @return
     */
    public static String createCustomersChamasTable(){
        return "CREATE TABLE 'customers_chamas' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'chama_cycle_id' varchar(150) NOT NULL,\n" +
                "  'chama_contribution' double NOT NULL,\n" +
                "  'no_of_chama_members' int(11) NOT NULL,\n" +
                "  'chama_payout_frequency' int(11) NOT NULL,\n" +
                "  'chama_payout_amount' int(11) NOT NULL,\n" +
                "  'chama_has_records' tinyint(1) NOT NULL,\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                "  'site_visit_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_chama_cycle_chm' FOREIGN KEY ('chama_cycle_id') REFERENCES 'st_chama_cycles' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_id_cusc' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_chm' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_site_visit_id_cc' FOREIGN KEY ('site_visit_id') REFERENCES 'site_visit' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(CustomersChamas customersChamas) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", customersChamas.getId());
        values.put("chama_cycle_id", customersChamas.getChamaCycleId());
        values.put("chama_contribution", customersChamas.getChamaContribution());
        values.put("no_of_chama_members", customersChamas.getNoOfChamaMembers());
        values.put("chama_payout_frequency", customersChamas.getChamaPayoutFrequency());
        values.put("chama_payout_amount", customersChamas.getChamaPayoutAmount());
        values.put("chama_has_records", customersChamas.getChamaHasRecords());
        values.put("customers_id", customersChamas.getCustomersId());
        values.put("site_visit_id", customersChamas.getSiteVisitId());
        values.put("creator_id", customersChamas.getCreatorId());
        values.put("deleted", customersChamas.getDeleted());
        values.put("creation_date", String.valueOf(customersChamas.getCreationDate()));
        values.put("updated_date", String.valueOf(customersChamas.getUpdatedDate()));

        // Inserting Row
        db.insert("customers_chamas", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<CustomersChamas> list = gson.fromJson(response, new TypeToken<List<CustomersChamas>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (CustomersChamas customersChamas : list) {

                ContentValues values = new ContentValues();
                values.put("id", customersChamas.getId());
                values.put("chama_cycle_id", customersChamas.getChamaCycleId());
                values.put("chama_contribution", customersChamas.getChamaContribution());
                values.put("no_of_chama_members", customersChamas.getNoOfChamaMembers());
                values.put("chama_payout_frequency", customersChamas.getChamaPayoutFrequency());
                values.put("chama_payout_amount", customersChamas.getChamaPayoutAmount());
                values.put("chama_has_records", customersChamas.getChamaHasRecords());
                values.put("customers_id", customersChamas.getCustomersId());
                values.put("site_visit_id", customersChamas.getSiteVisitId());
                values.put("creator_id", customersChamas.getCreatorId());
                values.put("deleted", customersChamas.getDeleted());
                values.put("creation_date", String.valueOf(customersChamas.getCreationDate()));
                values.put("updated_date", String.valueOf(customersChamas.getUpdatedDate()));

                cnt = db.insertWithOnConflict("customers_chamas", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
