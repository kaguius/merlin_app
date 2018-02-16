package ke.merlin.dao.business;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.business.Business;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class BusinessDao {

    private Business business;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public BusinessDao(){
        business = new Business();
    }

    /**
     *
     * @return
     */
    public static String createBusinessTable(){
        return "CREATE TABLE 'business' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'business_category_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'business_type_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'product_trading_start_date' date NOT NULL,\n" +
                "  'is_reset' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'photo_path' text,\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                "  'site_visit_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_business_category_id_b' FOREIGN KEY ('business_category_id') REFERENCES 'st_business_categories' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_business_type_id_b' FOREIGN KEY ('business_type_id') REFERENCES 'st_business_types' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_b' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_b' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_site_visit_id_b' FOREIGN KEY ('site_visit_id') REFERENCES 'site_visit' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(Business business) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", business.getId());
        values.put("business_category_id", business.getBusinessCategoryId());
        values.put("business_type_id", business.getBusinessTypeId());
        values.put("product_trading_start_date", String.valueOf(business.getProductTradingStartDate()));
        values.put("is_reset", business.getIsReset());
        values.put("photo_path", business.getPhotoPath());
        values.put("customers_id", business.getCustomersId());
        values.put("site_visit_id", business.getSiteVisitId());
        values.put("creator_id", business.getCreatorId());
        values.put("deleted", business.getDeleted());
        values.put("creation_date", String.valueOf(business.getCreationDate()));
        values.put("updated_date", String.valueOf(business.getUpdatedDate()));

        // Inserting Row
        db.insert("business", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<Business> list = gson.fromJson(response, new TypeToken<List<Business>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Business business : list) {

                ContentValues values = new ContentValues();
                values.put("id", business.getId());
                values.put("business_category_id", business.getBusinessCategoryId());
                values.put("business_type_id", business.getBusinessTypeId());
                values.put("product_trading_start_date", String.valueOf(business.getProductTradingStartDate()));
                values.put("is_reset", business.getIsReset());
                values.put("photo_path", business.getPhotoPath());
                values.put("customers_id", business.getCustomersId());
                values.put("site_visit_id", business.getSiteVisitId());
                values.put("creator_id", business.getCreatorId());
                values.put("deleted", business.getDeleted());
                values.put("creation_date", String.valueOf(business.getCreationDate()));
                values.put("updated_date", String.valueOf(business.getUpdatedDate()));

                cnt = db.insertWithOnConflict("business", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
