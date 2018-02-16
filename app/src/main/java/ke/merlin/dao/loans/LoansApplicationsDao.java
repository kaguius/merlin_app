package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.LoansApplications;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class LoansApplicationsDao {

    private LoansApplications loansApplications;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public LoansApplicationsDao(){
        loansApplications = new LoansApplications();
    }

    /**
     *
     * @return
     */
    public static String createLoansApplicationTable(){
        return "CREATE TABLE 'loans_applications' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customers_id' varchar(150) NOT NULL,\n" +
                "  'mobile' bigint(20) NOT NULL,\n" +
                "  'amount' double NOT NULL,\n" +
                "  'product_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'loan_officer_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'collections_officer_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'station_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'telcos_id' varchar(150) DEFAULT NULL,\n" +
                "  'approval_status' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'site_visit_id' varchar(150) DEFAULT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_approval_status_lns_aps' FOREIGN KEY ('approval_status') REFERENCES 'st_loans_approval_status' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_collections_officer_la' FOREIGN KEY ('collections_officer_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_lap' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_id_la' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loan_officer_la' FOREIGN KEY ('loan_officer_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loan_product_id_la' FOREIGN KEY ('product_id') REFERENCES 'products' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_site_visit_id_la' FOREIGN KEY ('site_visit_id') REFERENCES 'site_visit' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_station_id_la' FOREIGN KEY ('station_id') REFERENCES 'stations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_telcos_details_la' FOREIGN KEY ('telcos_id') REFERENCES 'telcos' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(LoansApplications loansApplications) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", loansApplications.getId());
        values.put("customers_id", loansApplications.getCustomersId());
        values.put("mobile", loansApplications.getMobile());
        values.put("amount", loansApplications.getAmount());
        values.put("product_id", loansApplications.getProductId());
        values.put("loan_officer_id", loansApplications.getLoanOfficerId());
        values.put("collections_officer_id", loansApplications.getCollectionsOfficerId());
        values.put("station_id", loansApplications.getStationId());
        values.put("approval_status", loansApplications.getApprovalStatus());
        values.put("telcos_id", loansApplications.getTelcosId());
        values.put("site_visit_id", loansApplications.getSiteVisitId());
        values.put("creator_id", loansApplications.getCreatorId());
        values.put("deleted", loansApplications.getDeleted());
        values.put("creation_date", String.valueOf(loansApplications.getCreationDate()));
        values.put("updated_date", String.valueOf(loansApplications.getUpdatedDate()));

        // Inserting Row
        db.insert("loans_applications", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<LoansApplications> list = gson.fromJson(response, new TypeToken<List<LoansApplications>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (LoansApplications loansApplications : list) {

                ContentValues values = new ContentValues();
                values.put("id", loansApplications.getId());
                values.put("customers_id", loansApplications.getCustomersId());
                values.put("mobile", loansApplications.getMobile());
                values.put("amount", loansApplications.getAmount());
                values.put("product_id", loansApplications.getProductId());
                values.put("loan_officer_id", loansApplications.getLoanOfficerId());
                values.put("collections_officer_id", loansApplications.getCollectionsOfficerId());
                values.put("station_id", loansApplications.getStationId());
                values.put("approval_status", loansApplications.getApprovalStatus());
                values.put("telcos_id", loansApplications.getTelcosId());
                values.put("site_visit_id", loansApplications.getSiteVisitId());
                values.put("creator_id", loansApplications.getCreatorId());
                values.put("deleted", loansApplications.getDeleted());
                values.put("creation_date", String.valueOf(loansApplications.getCreationDate()));
                values.put("updated_date", String.valueOf(loansApplications.getUpdatedDate()));

                cnt = db.insertWithOnConflict("loans_applications", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }
}
