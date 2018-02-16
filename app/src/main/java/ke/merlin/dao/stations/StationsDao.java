package ke.merlin.dao.stations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.StCustomerTitles;
import ke.merlin.models.stations.Stations;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StationsDao {

    private Stations stations;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StationsDao(){
        stations = new Stations();
    }

    /**
     *
     * @return
     */
    public static String createStationsTable(){
        return "CREATE TABLE 'stations' (\n" +
                "  'id' varchar(150) NOT NULL,\n" +
                "  'name' varchar(100) NOT NULL,\n" +
                "  'longitude' varchar(45) DEFAULT '',\n" +
                "  'latitude' varchar(45) DEFAULT '',\n" +
                "  'parent_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'sector_id' varchar(150) DEFAULT NULL,\n" +
                "  'countries_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_countries_id_s' FOREIGN KEY ('countries_id') REFERENCES 'st_countries' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_stns' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_parent_sts' FOREIGN KEY ('parent_id') REFERENCES 'stations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_sector_ids' FOREIGN KEY ('sector_id') REFERENCES 'sectors' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(Stations stations) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stations.getId());
        values.put("name", stations.getName());
        values.put("longitude", stations.getLongitude());
        values.put("latitude", stations.getLatitude());
        values.put("parent_id", stations.getParentId());
        values.put("sector_id", stations.getSectorId());
        values.put("countries_id", stations.getCountriesId());
        values.put("creator_id", stations.getCreatorId());
        values.put("deleted", stations.getDeleted());
        values.put("creation_date", String.valueOf(stations.getCreationDate()));
        values.put("updated_date", String.valueOf(stations.getUpdatedDate()));

        // Inserting Row
        db.insert("stations", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<Stations> list = gson.fromJson(response, new TypeToken<List<Stations>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Stations stations: list) {

                ContentValues values = new ContentValues();
                values.put("id", stations.getId());
                values.put("name", stations.getName());
                values.put("longitude", stations.getLongitude());
                values.put("latitude", stations.getLatitude());
                values.put("parent_id", stations.getParentId());
                values.put("sector_id", stations.getSectorId());
                values.put("countries_id", stations.getCountriesId());
                values.put("creator_id", stations.getCreatorId());
                values.put("deleted", stations.getDeleted());
                values.put("creation_date", String.valueOf(stations.getCreationDate()));
                values.put("updated_date", String.valueOf(stations.getUpdatedDate()));

                cnt = db.insertWithOnConflict("stations", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public Stations getStationById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from stations where id = ? and deleted = 0";

        int iCount =0;
        Stations stations = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stations = new Stations();
                stations.setId(cursor.getString(cursor.getColumnIndex("id")));
                stations.setName(cursor.getString(cursor.getColumnIndex("name")));
                stations.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                stations.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                stations.setParentId(cursor.getString(cursor.getColumnIndex("parent_id")));
                stations.setSectorId(cursor.getString(cursor.getColumnIndex("sector_id")));
                stations.setCountriesId(cursor.getString(cursor.getColumnIndex("countries_id")));
                stations.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stations.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stations.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stations.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stations;
    }
}
