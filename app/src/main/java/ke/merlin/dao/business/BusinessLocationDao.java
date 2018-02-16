package ke.merlin.dao.business;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.business.BusinessLocation;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class BusinessLocationDao {

    private BusinessLocation businessLocation;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public BusinessLocationDao(){
        businessLocation = new BusinessLocation();
    }

    /**
     *
     * @return
     */
    public static String createBusinessLocationTable(){
        return "CREATE TABLE 'business_location' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'business_location_types_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'location_trading_start_date' date NOT NULL,\n" +
                "  'business_address' text NOT NULL,\n" +
                "  'longitudes' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'latitudes' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'site_visit_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_business_location_types_id_bl' FOREIGN KEY ('business_location_types_id') REFERENCES 'st_business_locations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_id_bl' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_id_bl' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_site_visit_id_bl' FOREIGN KEY ('site_visit_id') REFERENCES 'site_visit' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(BusinessLocation businessLocation) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", businessLocation.getId());
        values.put("business_location_types_id", businessLocation.getBusinessLocationTypesId());
        values.put("location_trading_start_date", businessLocation.getLocationTradingStartDate());
        values.put("business_address", businessLocation.getBusinessAddress());
        values.put("longitudes", businessLocation.getLongitudes());
        values.put("latitudes", businessLocation.getLatitudes());
        values.put("customers_id", businessLocation.getCustomersId());
        values.put("site_visit_id", businessLocation.getSiteVisitId());
        values.put("creator_id", businessLocation.getCreatorId());
        values.put("deleted", businessLocation.getDeleted());
        values.put("creation_date", String.valueOf(businessLocation.getCreationDate()));
        values.put("updated_date", String.valueOf(businessLocation.getUpdatedDate()));

        // Inserting Row
        db.insert("business_location", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<BusinessLocation> list = gson.fromJson(response, new TypeToken<List<BusinessLocation>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (BusinessLocation businessLocation : list) {

                ContentValues values = new ContentValues();
                values.put("id", businessLocation.getId());
                values.put("business_location_types_id", businessLocation.getBusinessLocationTypesId());
                values.put("location_trading_start_date", businessLocation.getLocationTradingStartDate());
                values.put("business_address", businessLocation.getBusinessAddress());
                values.put("longitudes", businessLocation.getLongitudes());
                values.put("latitudes", businessLocation.getLatitudes());
                values.put("customers_id", businessLocation.getCustomersId());
                values.put("site_visit_id", businessLocation.getSiteVisitId());
                values.put("creator_id", businessLocation.getCreatorId());
                values.put("deleted", businessLocation.getDeleted());
                values.put("creation_date", String.valueOf(businessLocation.getCreationDate()));
                values.put("updated_date", String.valueOf(businessLocation.getUpdatedDate()));

                cnt = db.insertWithOnConflict("business_location", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
