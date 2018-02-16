package ke.merlin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;

import ke.merlin.models.Last_sync;
import ke.merlin.utils.database.DatabaseManager;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class Last_syncDao {

    private Last_sync last_sync;

    public Last_syncDao(){
        last_sync = new Last_sync();
    }

    /**
     *
     * @return
     */
    public static String createlastSyncedTable(){
        return "CREATE TABLE 'last_sync' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'table_name' varchar(100) NOT NULL UNIQUE DEFAULT '',\n" +
                "  'from_date' varchar(100) NOT NULL  DEFAULT '',\n" +
                "  'to_date' varchar(100) NOT NULL  DEFAULT '',\n" +
                "  'is_synced' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')" +
                ")";
    }

    public void insert(Last_sync last_sync) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("id", last_sync.getId());
        values.put("table_name", last_sync.getTable_name());
        values.put("from_date", last_sync.getFrom_date());
        values.put("to_date", last_sync.getTo_date());
        values.put("is_synced", last_sync.getIs_synced());
        values.put("creation_date", String.valueOf(last_sync.getCreationDate()));
        values.put("updated_date", String.valueOf(last_sync.getUpdatedDate()));

        // Inserting Row
        db.insertWithOnConflict("last_sync", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        DatabaseManager.getInstance().closeDatabase();
    }

    public Last_sync isTableSynced(String st_languages) {
        boolean exists = false;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from last_sync where table_name = ? and is_synced = 1 limit 1";

        Last_sync last_sync = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { st_languages } );

        if (cursor.moveToFirst()) {
            do {
                last_sync = new Last_sync();
                last_sync.setId(cursor.getString(cursor.getColumnIndex("id")));
                last_sync.setTable_name(cursor.getString(cursor.getColumnIndex("table_name")));
                last_sync.setFrom_date(cursor.getString(cursor.getColumnIndex("from_date")));
                last_sync.setTo_date(cursor.getString(cursor.getColumnIndex("to_date")));
                last_sync.setIs_synced(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_synced"))));
                last_sync.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                last_sync.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return last_sync;
    }
}
