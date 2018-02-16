package ke.merlin.dao.business;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.business.BusinessAssessment;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class BusinessAssessmentDao {

    BusinessAssessment businessAssessment;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public BusinessAssessmentDao(){
        businessAssessment = new BusinessAssessment();
    }

    /**
     *
     * @return
     */
    public static String createBusinessAssessmentTable(){
        return "CREATE TABLE 'business_assessment' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'stock_neat' tinyint(1) NOT NULL,\n" +
                "  'accurate_ledger_book' tinyint(1) NOT NULL,\n" +
                "  'sales_activity' tinyint(1) NOT NULL,\n" +
                "  'permanent_operation' tinyint(1) NOT NULL,\n" +
                "  'proof_of_ownership' tinyint(1) NOT NULL,\n" +
                "  'forthcoming_and_transparent' tinyint(1) NOT NULL,\n" +
                "  'known_to_market_authorities' tinyint(1) NOT NULL,\n" +
                "  'sound_reputation' tinyint(1) NOT NULL,\n" +
                "  'would_I_lend' tinyint(1) NOT NULL,\n" +
                "  'lend_amount' double DEFAULT NULL,\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                "  'site_visit_id' varchar(150) NOT NULL DEFAULT '',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_bus_ass' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_id_ba' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_site_visit_id_ba' FOREIGN KEY ('site_visit_id') REFERENCES 'site_visit' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(BusinessAssessment businessAssessment) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", businessAssessment.getId());
        values.put("stock_neat", businessAssessment.getStockNeat());
        values.put("accurate_ledger_book", businessAssessment.getAccurateLedgerBook());
        values.put("sales_activity", businessAssessment.getSalesActivity());
        values.put("permanent_operation", businessAssessment.getPermanentOperation());
        values.put("proof_of_ownership", businessAssessment.getProofOfOwnership());
        values.put("forthcoming_and_transparent", businessAssessment.getForthcomingAndTransparent());
        values.put("known_to_market_authorities", businessAssessment.getKnownToMarketAuthorities());
        values.put("sound_reputation", businessAssessment.getSoundReputation());
        values.put("would_I_lend", businessAssessment.getWouldILend());
        values.put("lend_amount", businessAssessment.getLendAmount());
        values.put("customers_id", businessAssessment.getCustomersId());
        values.put("site_visit_id", businessAssessment.getSiteVisitId());
        values.put("creator_id", businessAssessment.getCreatorId());
        values.put("deleted", businessAssessment.getDeleted());
        values.put("creation_date", String.valueOf(businessAssessment.getCreationDate()));
        values.put("updated_date", String.valueOf(businessAssessment.getUpdatedDate()));

        // Inserting Row
        db.insert("business_assessment", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<BusinessAssessment> list = gson.fromJson(response, new TypeToken<List<BusinessAssessment>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (BusinessAssessment businessAssessment : list) {

                ContentValues values = new ContentValues();
                values.put("id", businessAssessment.getId());
                values.put("stock_neat", businessAssessment.getStockNeat());
                values.put("accurate_ledger_book", businessAssessment.getAccurateLedgerBook());
                values.put("sales_activity", businessAssessment.getSalesActivity());
                values.put("permanent_operation", businessAssessment.getPermanentOperation());
                values.put("proof_of_ownership", businessAssessment.getProofOfOwnership());
                values.put("forthcoming_and_transparent", businessAssessment.getForthcomingAndTransparent());
                values.put("known_to_market_authorities", businessAssessment.getKnownToMarketAuthorities());
                values.put("sound_reputation", businessAssessment.getSoundReputation());
                values.put("would_I_lend", businessAssessment.getWouldILend());
                values.put("lend_amount", businessAssessment.getLendAmount());
                values.put("customers_id", businessAssessment.getCustomersId());
                values.put("site_visit_id", businessAssessment.getSiteVisitId());
                values.put("creator_id", businessAssessment.getCreatorId());
                values.put("deleted", businessAssessment.getDeleted());
                values.put("creation_date", String.valueOf(businessAssessment.getCreationDate()));
                values.put("updated_date", String.valueOf(businessAssessment.getUpdatedDate()));

                cnt = db.insertWithOnConflict("business_assessment", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
