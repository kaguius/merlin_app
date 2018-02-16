package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.StCustomerActiveStatus;
import ke.merlin.models.customers.StCustomerState;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StCustomerActiveStatusDao {

    private StCustomerActiveStatus stCustomerActiveStatus;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StCustomerActiveStatusDao(){
        stCustomerActiveStatus = new StCustomerActiveStatus();
    }

    /**
     *
     * @return
     */
    public static String createCustomerActiveStatusTable(){
        return "CREATE TABLE 'st_customer_active_status' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_cus_act_st' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StCustomerActiveStatus stCustomerActiveStatus) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stCustomerActiveStatus.getId());
        values.put("name", stCustomerActiveStatus.getName());
        values.put("description", stCustomerActiveStatus.getDescription());
        values.put("creator_id", stCustomerActiveStatus.getCreatorId());
        values.put("deleted", stCustomerActiveStatus.getDeleted());
        values.put("creation_date", String.valueOf(stCustomerActiveStatus.getCreationDate()));
        values.put("updated_date", String.valueOf(stCustomerActiveStatus.getUpdatedDate()));

        // Inserting Row
        db.insert("st_customer_active_status", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StCustomerActiveStatus> list = gson.fromJson(response, new TypeToken<List<StCustomerActiveStatus>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StCustomerActiveStatus stCustomerActiveStatus: list) {

                ContentValues values = new ContentValues();
                values.put("id", stCustomerActiveStatus.getId());
                values.put("name", stCustomerActiveStatus.getName());
                values.put("description", stCustomerActiveStatus.getDescription());
                values.put("creator_id", stCustomerActiveStatus.getCreatorId());
                values.put("deleted", stCustomerActiveStatus.getDeleted());
                values.put("creation_date", String.valueOf(stCustomerActiveStatus.getCreationDate()));
                values.put("updated_date", String.valueOf(stCustomerActiveStatus.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_customer_active_status", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public StCustomerActiveStatus getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_customer_active_status where id = ? and deleted = 0";

        int iCount =0;
        StCustomerActiveStatus stCustomerActiveStatus = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stCustomerActiveStatus = new StCustomerActiveStatus();
                stCustomerActiveStatus.setId(cursor.getString(cursor.getColumnIndex("id")));
                stCustomerActiveStatus.setName(cursor.getString(cursor.getColumnIndex("name")));
                stCustomerActiveStatus.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stCustomerActiveStatus.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stCustomerActiveStatus.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stCustomerActiveStatus.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stCustomerActiveStatus.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stCustomerActiveStatus;
    }
}
