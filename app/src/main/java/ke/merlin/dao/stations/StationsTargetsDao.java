package ke.merlin.dao.stations;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.stations.Sectors;
import ke.merlin.models.stations.StationsTargets;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.database.DatabaseManager;

/**
 * Created by mecmurimi on 24/09/2017.
 */

public class StationsTargetsDao {
    private StationsTargets stationsTargets;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StationsTargetsDao(){
        stationsTargets = new StationsTargets();
    }

    /**
     *
     * @return
     */
    public static String createStationsTargetsTable(){
        return "CREATE TABLE 'stations_targets' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'station_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'daily_target' double NOT NULL,\n" +
                "  'weekly_target' double NOT NULL,\n" +
                "  'monthly_target' double NOT NULL,\n" +
                "  'monthly_customers' int(11) NOT NULL,\n" +
                "  'freeze' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'repeater' tinyint(1) NOT NULL DEFAULT '0',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_stsg' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_station_idst' FOREIGN KEY ('station_id') REFERENCES 'stations' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StationsTargets stationsTargets) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stationsTargets.getId());
        values.put("station_id", stationsTargets.getStationId());
        values.put("daily_target", stationsTargets.getDailyTarget());
        values.put("weekly_target", stationsTargets.getWeeklyTarget());
        values.put("monthly_target", stationsTargets.getMonthlyTarget());
        values.put("monthly_customers", stationsTargets.getMonthlyCustomers());
        values.put("freeze", stationsTargets.getDailyTarget());
        values.put("repeater", stationsTargets.getDailyTarget());
        values.put("creator_id", stationsTargets.getCreatorId());
        values.put("deleted", stationsTargets.getDeleted());
        values.put("creation_date", String.valueOf(stationsTargets.getCreationDate()));
        values.put("updated_date", String.valueOf(stationsTargets.getUpdatedDate()));

        // Inserting Row
        db.insert("stations_targets", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StationsTargets> list = gson.fromJson(response, new TypeToken<List<StationsTargets>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StationsTargets stationsTargets: list) {

                ContentValues values = new ContentValues();
                values.put("id", stationsTargets.getId());
                values.put("station_id", stationsTargets.getStationId());
                values.put("daily_target", stationsTargets.getDailyTarget());
                values.put("weekly_target", stationsTargets.getWeeklyTarget());
                values.put("monthly_target", stationsTargets.getMonthlyTarget());
                values.put("monthly_customers", stationsTargets.getMonthlyCustomers());
                values.put("freeze", stationsTargets.getDailyTarget());
                values.put("repeater", stationsTargets.getDailyTarget());
                values.put("creator_id", stationsTargets.getCreatorId());
                values.put("deleted", stationsTargets.getDeleted());
                values.put("creation_date", String.valueOf(stationsTargets.getCreationDate()));
                values.put("updated_date", String.valueOf(stationsTargets.getUpdatedDate()));

                cnt = db.insertWithOnConflict("stations_targets", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
