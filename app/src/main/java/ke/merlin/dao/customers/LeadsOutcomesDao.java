package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.Customers;
import ke.merlin.models.customers.LeadsOutcomes;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class LeadsOutcomesDao {

    private LeadsOutcomes leadsOutcomes;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LeadsOutcomesDao(){
        leadsOutcomes = new LeadsOutcomes();
    }

    /**
     *
     * @return
     */
    public static String createLeadOutcomeTable(){
        return "CREATE TABLE 'leads_outcomes' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customers_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'outcomes_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'outcome_explanation' text NOT NULL,\n" +
                "  'next_visit_date' date NOT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_ldo' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON DELETE NO ACTION ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_id_clo' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_outcomes_id_lot' FOREIGN KEY ('outcomes_id') REFERENCES 'st_lead_outcomes' ('id') ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(LeadsOutcomes leadsOutcomes) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", leadsOutcomes.getId());
        values.put("customers_id", leadsOutcomes.getCustomersId());
        values.put("outcomes_id", leadsOutcomes.getOutcomesId());
        values.put("outcome_explanation", leadsOutcomes.getOutcomeExplanation());
        values.put("next_visit_date", leadsOutcomes.getNextVisitDate());
        values.put("creator_id", leadsOutcomes.getCreatorId());
        values.put("deleted", leadsOutcomes.getDeleted());
        values.put("creation_date", String.valueOf(leadsOutcomes.getCreationDate()));
        values.put("updated_date", String.valueOf(leadsOutcomes.getUpdatedDate()));

        // Inserting Row
        db.insert("leads_outcomes", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<LeadsOutcomes> list = gson.fromJson(response, new TypeToken<List<LeadsOutcomes>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (LeadsOutcomes leadsOutcomes: list) {

                ContentValues values = new ContentValues();
                values.put("id", leadsOutcomes.getId());
                values.put("customers_id", leadsOutcomes.getCustomersId());
                values.put("outcomes_id", leadsOutcomes.getOutcomesId());
                values.put("outcome_explanation", leadsOutcomes.getOutcomeExplanation());
                values.put("next_visit_date", leadsOutcomes.getNextVisitDate());
                values.put("creator_id", leadsOutcomes.getCreatorId());
                values.put("deleted", leadsOutcomes.getDeleted());
                values.put("creation_date", String.valueOf(leadsOutcomes.getCreationDate()));
                values.put("updated_date", String.valueOf(leadsOutcomes.getUpdatedDate()));


                cnt = db.insertWithOnConflict("leads_outcomes", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }



        return cnt;
    }

    public ArrayList<LeadsOutcomes> getOutcomesByCustomerId(String id) {

        LeadsOutcomes beans;
        ArrayList<LeadsOutcomes> leadsOutcomesList = new ArrayList<>();

        try {

            Log.i("Data received: ", id );

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String selectQuery;
            Cursor cursor = null;

            selectQuery = "select * from leads_outcomes where deleted = 0 and customers_id = ?";
            cursor = db.rawQuery(selectQuery, new String[]{id});

            Log.i("Rows found: ", String.valueOf(cursor.getCount()));

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    beans = new LeadsOutcomes();
                    beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                    beans.setCustomersId(cursor.getString(cursor.getColumnIndex("customers_id")));
                    beans.setOutcomesId(cursor.getString(cursor.getColumnIndex("outcomes_id")));
                    beans.setOutcomeExplanation(cursor.getString(cursor.getColumnIndex("outcome_explanation")));
                    beans.setNextVisitDate(cursor.getString(cursor.getColumnIndex("next_visit_date")));
                    beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                    beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                    beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                    beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

                    leadsOutcomesList.add(beans);
                } while (cursor.moveToNext());
            }

            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return leadsOutcomesList;
    }
}
