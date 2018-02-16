package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.StChamaCycles;
import ke.merlin.models.loans.StLoansArrearsStatus;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StLoansArrearsStatusDao {

    private StLoansArrearsStatus stLoansArrearsStatus;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StLoansArrearsStatusDao(){
        stLoansArrearsStatus = new StLoansArrearsStatus();
    }

    /**
     *
     * @return
     */
    public static String createLoanArrearsStatusTable(){
        return "CREATE TABLE 'st_loans_arrears_status' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_lsts' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StLoansArrearsStatus stLoansArrearsStatus) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stLoansArrearsStatus.getId());
        values.put("name", stLoansArrearsStatus.getName());
        values.put("creator_id", stLoansArrearsStatus.getCreatorId());
        values.put("deleted", stLoansArrearsStatus.getDeleted());
        values.put("creation_date", String.valueOf(stLoansArrearsStatus.getCreationDate()));
        values.put("updated_date", String.valueOf(stLoansArrearsStatus.getUpdatedDate()));

        // Inserting Row
        db.insert("st_loans_arrears_status", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StLoansArrearsStatus> list = gson.fromJson(response, new TypeToken<List<StLoansArrearsStatus>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StLoansArrearsStatus stLoansArrearsStatus: list) {

                ContentValues values = new ContentValues();
                values.put("id", stLoansArrearsStatus.getId());
                values.put("name", stLoansArrearsStatus.getName());
                values.put("creator_id", stLoansArrearsStatus.getCreatorId());
                values.put("deleted", stLoansArrearsStatus.getDeleted());
                values.put("creation_date", String.valueOf(stLoansArrearsStatus.getCreationDate()));
                values.put("updated_date", String.valueOf(stLoansArrearsStatus.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_loans_arrears_status", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public StLoansArrearsStatus getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_loans_arrears_status where id = ? and deleted = 0";

        int iCount =0;
        StLoansArrearsStatus stLoansArrearsStatus = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stLoansArrearsStatus = new StLoansArrearsStatus();
                stLoansArrearsStatus.setId(cursor.getString(cursor.getColumnIndex("id")));
                stLoansArrearsStatus.setName(cursor.getString(cursor.getColumnIndex("name")));
                stLoansArrearsStatus.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stLoansArrearsStatus.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stLoansArrearsStatus.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stLoansArrearsStatus.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stLoansArrearsStatus.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stLoansArrearsStatus;
    }
}
