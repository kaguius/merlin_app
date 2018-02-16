package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.StLoansAssignmentStatus;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StLoansAssignmentStatusDao {

    private StLoansAssignmentStatus stLoansAssignmentStatus;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StLoansAssignmentStatusDao(){
        stLoansAssignmentStatus = new StLoansAssignmentStatus();
    }

    /**
     *
     * @return
     */
    public static String createLoanAssignmentStatusTable(){
        return "CREATE TABLE 'st_loans_assignment_status' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_st_las' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StLoansAssignmentStatus stLoansAssignmentStatus) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stLoansAssignmentStatus.getId());
        values.put("name", stLoansAssignmentStatus.getName());
        values.put("creator_id", stLoansAssignmentStatus.getCreatorId());
        values.put("deleted", stLoansAssignmentStatus.getDeleted());
        values.put("creation_date", String.valueOf(stLoansAssignmentStatus.getCreationDate()));
        values.put("updated_date", String.valueOf(stLoansAssignmentStatus.getUpdatedDate()));

        // Inserting Row
        db.insert("st_loans_assignment_status", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StLoansAssignmentStatus> list = gson.fromJson(response, new TypeToken<List<StLoansAssignmentStatus>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StLoansAssignmentStatus stLoansAssignmentStatus: list) {

                ContentValues values = new ContentValues();
                values.put("id", stLoansAssignmentStatus.getId());
                values.put("name", stLoansAssignmentStatus.getName());
                values.put("creator_id", stLoansAssignmentStatus.getCreatorId());
                values.put("deleted", stLoansAssignmentStatus.getDeleted());
                values.put("creation_date", String.valueOf(stLoansAssignmentStatus.getCreationDate()));
                values.put("updated_date", String.valueOf(stLoansAssignmentStatus.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_loans_assignment_status", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
