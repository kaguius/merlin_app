package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.LoansPenalties;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class LoansPenaltiesDao {
    private LoansPenalties loansPenalties;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LoansPenaltiesDao(){
        loansPenalties = new LoansPenalties();
    }

    /**
     *
     * @return
     */
    public static String createLoansPenaltiesTable(){
        return "CREATE TABLE 'loans_penalties' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'loans_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'amount' double NOT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_lp' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loans_id_lp' FOREIGN KEY ('loans_id') REFERENCES 'loans' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(LoansPenalties loansPenalties) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", loansPenalties.getId());
        values.put("loans_id", loansPenalties.getLoansId());
        values.put("amount", loansPenalties.getAmount());
        values.put("creator_id", loansPenalties.getCreatorId());
        values.put("deleted", loansPenalties.getDeleted());
        values.put("creation_date", String.valueOf(loansPenalties.getCreationDate()));
        values.put("updated_date", String.valueOf(loansPenalties.getUpdatedDate()));

        // Inserting Row
        db.insert("loans_penalties", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<LoansPenalties> list = gson.fromJson(response, new TypeToken<List<LoansPenalties>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (LoansPenalties loansPenalties : list) {

                ContentValues values = new ContentValues();
                values.put("id", loansPenalties.getId());
                values.put("loans_id", loansPenalties.getLoansId());
                values.put("amount", loansPenalties.getAmount());
                values.put("creator_id", loansPenalties.getCreatorId());
                values.put("deleted", loansPenalties.getDeleted());
                values.put("creation_date", String.valueOf(loansPenalties.getCreationDate()));
                values.put("updated_date", String.valueOf(loansPenalties.getUpdatedDate()));

                cnt = db.insertWithOnConflict("loans_penalties", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
