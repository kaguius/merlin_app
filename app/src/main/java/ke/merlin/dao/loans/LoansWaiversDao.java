package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.LoansWaivers;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class LoansWaiversDao {

    private LoansWaivers loansWaivers;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LoansWaiversDao(){
        loansWaivers = new LoansWaivers();
    }

    /**
     *
     * @return
     */
    public static String createLoansWaiversTable(){
        return "CREATE TABLE 'loans_waivers' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'loans_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'amount' double NOT NULL DEFAULT '0',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_wvs' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loans_id_w' FOREIGN KEY ('loans_id') REFERENCES 'loans' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(LoansWaivers loansWaivers) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", loansWaivers.getId());
        values.put("loans_id", loansWaivers.getLoansId());
        values.put("amount", loansWaivers.getAmount());
        values.put("creator_id", loansWaivers.getCreatorId());
        values.put("deleted", loansWaivers.getDeleted());
        values.put("creation_date", String.valueOf(loansWaivers.getCreationDate()));
        values.put("updated_date", String.valueOf(loansWaivers.getUpdatedDate()));

        // Inserting Row
        db.insert("loans_waivers", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<LoansWaivers> list = gson.fromJson(response, new TypeToken<List<LoansWaivers>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (LoansWaivers loansWaivers : list) {

                ContentValues values = new ContentValues();
                values.put("id", loansWaivers.getId());
                values.put("loans_id", loansWaivers.getLoansId());
                values.put("amount", loansWaivers.getAmount());
                values.put("creator_id", loansWaivers.getCreatorId());
                values.put("deleted", loansWaivers.getDeleted());
                values.put("creation_date", String.valueOf(loansWaivers.getCreationDate()));
                values.put("updated_date", String.valueOf(loansWaivers.getUpdatedDate()));

                cnt = db.insertWithOnConflict("loans_waivers", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
