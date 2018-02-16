package ke.merlin.dao.stations;

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

import ke.merlin.models.customers.StLeadOutcomes;
import ke.merlin.models.customers.StMaritalStatus;
import ke.merlin.models.stations.StationsMarkets;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StationsMarketsDao {

    private StationsMarkets stationsMarkets;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StationsMarketsDao(){
        stationsMarkets = new StationsMarkets();
    }

    /**
     *
     * @return
     */
    public static String createStationMarketsTable(){
        return "CREATE TABLE 'stations_markets' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL,\n" +
                "  'station_id' varchar(150) NOT NULL,\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_mkts' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_stations_id_mkts' FOREIGN KEY ('station_id') REFERENCES 'stations' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StationsMarkets stationsMarkets) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stationsMarkets.getId());
        values.put("name", stationsMarkets.getName());
        values.put("description", stationsMarkets.getDescription());
        values.put("creator_id", stationsMarkets.getCreatorId());
        values.put("station_id", stationsMarkets.getStationId());
        values.put("deleted", stationsMarkets.getDeleted());
        values.put("creation_date", String.valueOf(stationsMarkets.getCreationDate()));
        values.put("updated_date", String.valueOf(stationsMarkets.getUpdatedDate()));

        // Inserting Row
        db.insert("stations_markets", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StationsMarkets> list = gson.fromJson(response, new TypeToken<List<StationsMarkets>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StationsMarkets stationsMarkets: list) {

                ContentValues values = new ContentValues();
                values.put("id", stationsMarkets.getId());
                values.put("name", stationsMarkets.getName());
                values.put("description", stationsMarkets.getDescription());
                values.put("creator_id", stationsMarkets.getCreatorId());
                values.put("station_id", stationsMarkets.getStationId());
                values.put("deleted", stationsMarkets.getDeleted());
                values.put("creation_date", String.valueOf(stationsMarkets.getCreationDate()));
                values.put("updated_date", String.valueOf(stationsMarkets.getUpdatedDate()));

                cnt = db.insertWithOnConflict("stations_markets", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems(String station) {

        StationsMarkets stationsMarkets;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from stations_markets where station_id = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[] { station });
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stationsMarkets = new StationsMarkets();
                stationsMarkets.setId(cursor.getString(cursor.getColumnIndex("id")));
                stationsMarkets.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stationsMarkets.getId(), stationsMarkets.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StationsMarkets getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from stations_markets where id = ? and deleted = 0";

        int iCount =0;
        StationsMarkets stationsMarkets = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stationsMarkets = new StationsMarkets();
                stationsMarkets.setId(cursor.getString(cursor.getColumnIndex("id")));
                stationsMarkets.setName(cursor.getString(cursor.getColumnIndex("name")));
                stationsMarkets.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stationsMarkets.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stationsMarkets.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stationsMarkets.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stationsMarkets.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stationsMarkets;
    }
}
