package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ke.merlin.models.customers.StCustomerActiveStatus;
import ke.merlin.models.customers.StCustomerApprovalStatus;
import ke.merlin.models.customers.StLanguages;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StCustomerApprovalStatusDao {

    private StCustomerApprovalStatus stCustomerApprovalStatus;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StCustomerApprovalStatusDao(){
        stCustomerApprovalStatus = new StCustomerApprovalStatus();
    }

    /**
     *
     * @return
     */
    public static String createCustomerApprovalStatusTable(){
        return "CREATE TABLE 'st_customer_approval_status' (\n" +
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
                "  CONSTRAINT 'fk_creator_id_cus_app_st' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StCustomerApprovalStatus stCustomerApprovalStatus) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stCustomerApprovalStatus.getId());
        values.put("name", stCustomerApprovalStatus.getName());
        values.put("description", stCustomerApprovalStatus.getDescription());
        values.put("creator_id", stCustomerApprovalStatus.getCreatorId());
        values.put("deleted", stCustomerApprovalStatus.getDeleted());
        values.put("creation_date", String.valueOf(stCustomerApprovalStatus.getCreationDate()));
        values.put("updated_date", String.valueOf(stCustomerApprovalStatus.getUpdatedDate()));

        // Inserting Row
        db.insert("st_customer_approval_status", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StCustomerApprovalStatus> list = gson.fromJson(response, new TypeToken<List<StCustomerApprovalStatus>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StCustomerApprovalStatus stCustomerApprovalStatus: list) {

                ContentValues values = new ContentValues();
                values.put("id", stCustomerApprovalStatus.getId());
                values.put("name", stCustomerApprovalStatus.getName());
                values.put("description", stCustomerApprovalStatus.getDescription());
                values.put("creator_id", stCustomerApprovalStatus.getCreatorId());
                values.put("deleted", stCustomerApprovalStatus.getDeleted());
                values.put("creation_date", String.valueOf(stCustomerApprovalStatus.getCreationDate()));
                values.put("updated_date", String.valueOf(stCustomerApprovalStatus.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_customer_approval_status", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StCustomerApprovalStatus approvalStatus;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_customer_approval_status";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                approvalStatus = new StCustomerApprovalStatus();
                approvalStatus.setId(cursor.getString(cursor.getColumnIndex("id")));
                approvalStatus.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(approvalStatus.getId(), approvalStatus.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StCustomerApprovalStatus getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_customer_approval_status where id = ? and deleted = 0";

        int iCount =0;
        StCustomerApprovalStatus stCustomerApprovalStatus = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stCustomerApprovalStatus = new StCustomerApprovalStatus();
                stCustomerApprovalStatus.setId(cursor.getString(cursor.getColumnIndex("id")));
                stCustomerApprovalStatus.setName(cursor.getString(cursor.getColumnIndex("name")));
                stCustomerApprovalStatus.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stCustomerApprovalStatus.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stCustomerApprovalStatus.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stCustomerApprovalStatus.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stCustomerApprovalStatus.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stCustomerApprovalStatus;
    }
}
