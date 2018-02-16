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

import ke.merlin.models.customers.StCustomerState;
import ke.merlin.models.customers.StHomeOwnerships;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StCustomerStateDao {

    private StCustomerState stCustomerState;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StCustomerStateDao(){
        stCustomerState = new StCustomerState();
    }

    /**
     *
     * @return
     */
    public static String createCustomerStateTable(){
        return "CREATE TABLE 'st_customer_state' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_cst' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StCustomerState stCustomerState) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stCustomerState.getId());
        values.put("name", stCustomerState.getName());
        values.put("description", stCustomerState.getDescription());
        values.put("creator_id", stCustomerState.getCreatorId());
        values.put("deleted", stCustomerState.getDeleted());
        values.put("creation_date", String.valueOf(stCustomerState.getCreationDate()));
        values.put("updated_date", String.valueOf(stCustomerState.getUpdatedDate()));

        // Inserting Row
        db.insert("st_customer_state", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StCustomerState> list = gson.fromJson(response, new TypeToken<List<StCustomerState>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            try {
            for (StCustomerState stCustomerState: list) {

                ContentValues values = new ContentValues();
                values.put("id", stCustomerState.getId());
                values.put("name", stCustomerState.getName());
                values.put("description", stCustomerState.getDescription());
                values.put("creator_id", stCustomerState.getCreatorId());
                values.put("deleted", stCustomerState.getDeleted());
                values.put("creation_date", String.valueOf(stCustomerState.getCreationDate()));
                values.put("updated_date", String.valueOf(stCustomerState.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_customer_state", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public StCustomerState getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_customer_state where id = ? and deleted = 0";

        int iCount =0;
        StCustomerState stCustomerState = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stCustomerState = new StCustomerState();
                stCustomerState.setId(cursor.getString(cursor.getColumnIndex("id")));
                stCustomerState.setName(cursor.getString(cursor.getColumnIndex("name")));
                stCustomerState.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stCustomerState.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stCustomerState.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stCustomerState.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stCustomerState.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stCustomerState;
    }
}
