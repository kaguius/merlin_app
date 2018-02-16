package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.LoansInteractions;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class LoansInteractionsDao {

    private LoansInteractions loansInteractions;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LoansInteractionsDao(){
        loansInteractions = new LoansInteractions();
    }

    /**
     *
     * @return
     */
    public static String createLoansInteractionsTable(){
        return "CREATE TABLE 'loans_interactions' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'loan_id' varchar(150) NOT NULL,\n" +
                "  'customer_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'interaction_type_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'interaction_category_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'next_interaction_date' date NOT NULL,\n" +
                "  'callback_time' time NOT NULL,\n" +
                "  'amount' double NOT NULL DEFAULT '0',\n" +
                "  'comments' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_inter' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_cint' FOREIGN KEY ('customer_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_interactions_cat_int' FOREIGN KEY ('interaction_category_id') REFERENCES 'st_interactions_categories' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_interactions_type_cint' FOREIGN KEY ('interaction_type_id') REFERENCES 'st_interactions_call_outcomes' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loan_id_intrs' FOREIGN KEY ('loan_id') REFERENCES 'loans' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(LoansInteractions loansInteractions) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", loansInteractions.getId());
        values.put("loan_id", loansInteractions.getLoanId());
        values.put("customer_id", loansInteractions.getCustomerId());
        values.put("interaction_type_id", loansInteractions.getInteractionTypeId());
        values.put("interaction_category_id", loansInteractions.getInteractionCategoryId());
        values.put("next_interaction_date", loansInteractions.getNextInteractionDate());
        values.put("callback_time", String.valueOf(loansInteractions.getCallbackTime()));
        values.put("amount", loansInteractions.getAmount());
        values.put("comments", loansInteractions.getComments());
        values.put("creator_id", loansInteractions.getCreatorId());
        values.put("deleted", loansInteractions.getDeleted());
        values.put("creation_date", String.valueOf(loansInteractions.getCreationDate()));
        values.put("updated_date", String.valueOf(loansInteractions.getUpdatedDate()));

        // Inserting Row
        db.insert("loans_interactions", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<LoansInteractions> list = gson.fromJson(response, new TypeToken<List<LoansInteractions>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (LoansInteractions loansInteractions : list) {

                ContentValues values = new ContentValues();
                values.put("id", loansInteractions.getId());
                values.put("loan_id", loansInteractions.getLoanId());
                values.put("customer_id", loansInteractions.getCustomerId());
                values.put("interaction_type_id", loansInteractions.getInteractionTypeId());
                values.put("interaction_category_id", loansInteractions.getInteractionCategoryId());
                values.put("next_interaction_date", loansInteractions.getNextInteractionDate());
                values.put("callback_time", String.valueOf(loansInteractions.getCallbackTime()));
                values.put("amount", loansInteractions.getAmount());
                values.put("comments", loansInteractions.getComments());
                values.put("creator_id", loansInteractions.getCreatorId());
                values.put("deleted", loansInteractions.getDeleted());
                values.put("creation_date", String.valueOf(loansInteractions.getCreationDate()));
                values.put("updated_date", String.valueOf(loansInteractions.getUpdatedDate()));

                cnt = db.insertWithOnConflict("loans_interactions", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
