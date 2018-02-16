package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.Customers;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class CustomersDao {

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
    private Customers customers;

    public CustomersDao() {
        customers = new Customers();
    }

    /**
     *
     * @return
     */
    public static String createCustomersTable(){
        return "CREATE TABLE 'customers' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'first_name' varchar(50) NOT NULL,\n" +
                "  'last_name' varchar(50) NOT NULL,\n" +
                "  'also_known_as' varchar(50) DEFAULT NULL,\n" +
                "  'primary_phone' bigint(20) NOT NULL,\n" +
                "  'alternative_phone' bigint(20) DEFAULT NULL,\n" +
                "  'disburse_phone' bigint(20) DEFAULT NULL,\n" +
                "  'info_sources_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'other_sources' text,\n" +
                "  'lived_there_since' date DEFAULT NULL,\n" +
                "  'home_ownership_id' varchar(150) DEFAULT NULL,\n" +
                "  'home_address' text,\n" +
                "  'longitudes' double DEFAULT NULL,\n" +
                "  'latitudes' double DEFAULT NULL,\n" +
                "  'national_id' varchar(20) DEFAULT NULL,\n" +
                "  'date_of_birth' date DEFAULT NULL,\n" +
                "  'gender_id' varchar(150) DEFAULT NULL,\n" +
                "  'asset_list' tinyint(1) DEFAULT NULL,\n" +
                "  'title_id' varchar(150) DEFAULT NULL,\n" +
                "  'language_id' varchar(150) DEFAULT NULL,\n" +
                "  'marital_status_id' varchar(150) DEFAULT NULL,\n" +
                "  'active_status_id' varchar(150) DEFAULT NULL,\n" +
                "  'customer_state_id' varchar(150) DEFAULT NULL,\n" +
                "  'approval_status_id' varchar(150) DEFAULT NULL,\n" +
                "  'loan_officer_id' varchar(150) NOT NULL,\n" +
                "  'collections_officer_id' varchar(150) NOT NULL,\n" +
                "  'stations_id' varchar(150) NOT NULL,\n" +
                "  'market_id' varchar(150) DEFAULT NULL,\n" +
                "  'photo_path' text,\n" +
                "  'id_front_path' text,\n" +
                "  'id_back_path' text,\n" +
                "  'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_active_staus_c' FOREIGN KEY ('active_status_id') REFERENCES 'st_customer_active_status' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_approval_status_c' FOREIGN KEY ('approval_status_id') REFERENCES 'st_customer_approval_status' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_collections_officer_c' FOREIGN KEY ('collections_officer_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_c' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customer_state_c' FOREIGN KEY ('customer_state_id') REFERENCES 'st_customer_state' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_gender_id_c' FOREIGN KEY ('gender_id') REFERENCES 'st_genders' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_infosources_c' FOREIGN KEY ('info_sources_id') REFERENCES 'st_info_sources' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_language_c' FOREIGN KEY ('language_id') REFERENCES 'st_languages' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loan_officer_c' FOREIGN KEY ('loan_officer_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_marital_status_c' FOREIGN KEY ('marital_status_id') REFERENCES 'st_marital_status' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_markets_id_c' FOREIGN KEY ('market_id') REFERENCES 'stations_markets' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_stations_id_c' FOREIGN KEY ('stations_id') REFERENCES 'stations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_title_id_cus' FOREIGN KEY ('title_id') REFERENCES 'st_customer_titles' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(Customers customers) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", customers.getId());
        values.put("first_name", customers.getFirstName());
        values.put("last_name", customers.getLastName());
        values.put("also_known_as", customers.getAlsoKnownAs());
        values.put("primary_phone", customers.getPrimaryPhone());
        values.put("alternative_phone", customers.getAlternativePhone());
        values.put("disburse_phone", customers.getDisbursePhone());
        values.put("info_sources_id", customers.getInfoSourcesId());
        values.put("other_sources", customers.getOtherSources());
        values.put("national_id", customers.getNationalId());
        values.put("lived_there_since", customers.getLivedThereSince());
        values.put("home_ownership_id", customers.getHomeOwnershipId());
        values.put("home_address", customers.getHomeAddress());
        values.put("longitudes", customers.getLongitudes());
        values.put("latitudes", customers.getLatitudes());
        values.put("date_of_birth", customers.getDateOfBirth());
        values.put("gender_id", customers.getGenderId());
        values.put("asset_list", customers.getAssetList());
        values.put("title_id", customers.getTitleId());
        values.put("language_id", customers.getLanguageId());
        values.put("marital_status_id", customers.getMaritalStatusId());
        values.put("active_status_id", customers.getActiveStatusId());
        values.put("customer_state_id", customers.getCustomerStateId());
        values.put("approval_status_id", customers.getApprovalStatusId());
        values.put("loan_officer_id", customers.getLoanOfficerId());
        values.put("collections_officer_id", customers.getCollectionsOfficerId());
        values.put("stations_id", customers.getStationsId());
        values.put("market_id", customers.getMarketId());
        values.put("photo_path", customers.getPhotoPath());
        values.put("id_front_path", customers.getIdFrontPath());
        values.put("id_back_path", customers.getIdBackPath());
        values.put("creator_id", customers.getCreatorId());
        values.put("deleted", customers.getDeleted());
        values.put("creation_date", String.valueOf(customers.getCreationDate()));
        values.put("updated_date", String.valueOf(customers.getUpdatedDate()));

        // Inserting Row
        db.insert("customers", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;
        List<Customers> list = gson.fromJson(response, new TypeToken<List<Customers>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Customers customers : list) {

                ContentValues values = new ContentValues();
                values.put("id", customers.getId());
                values.put("first_name", customers.getFirstName());
                values.put("last_name", customers.getLastName());
                values.put("also_known_as", customers.getAlsoKnownAs());
                values.put("primary_phone", customers.getPrimaryPhone());
                values.put("alternative_phone", customers.getAlternativePhone());
                values.put("disburse_phone", customers.getDisbursePhone());
                values.put("info_sources_id", customers.getInfoSourcesId());
                values.put("other_sources", customers.getOtherSources());
                values.put("national_id", customers.getNationalId());
                values.put("lived_there_since", customers.getLivedThereSince());
                values.put("home_ownership_id", customers.getHomeOwnershipId());
                values.put("home_address", customers.getHomeAddress());
                values.put("longitudes", customers.getLongitudes());
                values.put("latitudes", customers.getLatitudes());
                values.put("date_of_birth", customers.getDateOfBirth());
                values.put("gender_id", customers.getGenderId());
                values.put("asset_list", customers.getAssetList());
                values.put("title_id", customers.getTitleId());
                values.put("language_id", customers.getLanguageId());
                values.put("marital_status_id", customers.getMaritalStatusId());
                values.put("active_status_id", customers.getActiveStatusId());
                values.put("customer_state_id", customers.getCustomerStateId());
                values.put("approval_status_id", customers.getApprovalStatusId());
                values.put("loan_officer_id", customers.getLoanOfficerId());
                values.put("collections_officer_id", customers.getCollectionsOfficerId());
                values.put("stations_id", customers.getStationsId());
                values.put("market_id", customers.getMarketId());
                values.put("photo_path", customers.getPhotoPath());
                values.put("id_front_path", customers.getIdFrontPath());
                values.put("id_back_path", customers.getIdBackPath());
                values.put("creator_id", customers.getCreatorId());
                values.put("deleted", customers.getDeleted());
                values.put("creation_date", String.valueOf(customers.getCreationDate()));
                values.put("updated_date", String.valueOf(customers.getUpdatedDate()));

                cnt = db.insertWithOnConflict("customers", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
        return cnt;
    }

    /**
     *
     * @param customers
     */
    public void update(Customers customers) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        if(customers.getFirstName() != null)
        values.put("first_name", customers.getFirstName());

        if(customers.getLastName() != null)
        values.put("last_name", customers.getLastName());

        if(customers.getAlsoKnownAs() != null)
        values.put("also_known_as", customers.getAlsoKnownAs());

        if(customers.getPrimaryPhone() != null)
        values.put("primary_phone", customers.getPrimaryPhone());

        if(customers.getAlternativePhone() != null)
        values.put("alternative_phone", customers.getAlternativePhone());

        if(customers.getDisbursePhone() != null)
        values.put("disburse_phone", customers.getDisbursePhone());

        if(customers.getInfoSourcesId() != null)
        values.put("info_sources_id", customers.getInfoSourcesId());

        if(customers.getOtherSources() != null)
        values.put("other_sources", customers.getOtherSources());

        if(customers.getNationalId() != null)
        values.put("national_id", customers.getNationalId());

        if(customers.getLivedThereSince() != null)
        values.put("lived_there_since", customers.getLivedThereSince());

        if(customers.getHomeOwnershipId() != null)
        values.put("home_ownership_id", customers.getHomeOwnershipId());

        if(customers.getHomeAddress() != null)
        values.put("home_address", customers.getHomeAddress());

        if(customers.getLongitudes() != null)
        values.put("longitudes", customers.getLongitudes());

        if(customers.getLatitudes() != null)
        values.put("latitudes", customers.getLatitudes());

        if(customers.getDateOfBirth() != null)
        values.put("date_of_birth", customers.getDateOfBirth());

        if(customers.getGenderId() != null)
        values.put("gender_id", customers.getGenderId());

        if(customers.getAssetList() != null)
        values.put("asset_list", customers.getAssetList());

        if(customers.getTitleId() != null)
        values.put("title_id", customers.getTitleId());

        if(customers.getLanguageId() != null)
        values.put("language_id", customers.getLanguageId());

        if(customers.getMaritalStatusId() != null)
        values.put("marital_status_id", customers.getMaritalStatusId());

        if(customers.getActiveStatusId() != null)
        values.put("active_status_id", customers.getActiveStatusId());

        if(customers.getCustomerStateId() != null)
        values.put("customer_state_id", customers.getCustomerStateId());

        if(customers.getApprovalStatusId() != null)
        values.put("approval_status_id", customers.getApprovalStatusId());

        if(customers.getLoanOfficerId() != null)
        values.put("loan_officer_id", customers.getLoanOfficerId());

        if(customers.getCollectionsOfficerId() != null)
        values.put("collections_officer_id", customers.getCollectionsOfficerId());

        if(customers.getStationsId() != null)
        values.put("stations_id", customers.getStationsId());

        if(customers.getMarketId() != null)
        values.put("market_id", customers.getMarketId());

        if(customers.getPhotoPath() != null)
        values.put("photo_path", customers.getPhotoPath());

        if(customers.getIdFrontPath() != null)
        values.put("id_front_path", customers.getIdFrontPath());

        if(customers.getIdBackPath() != null)
        values.put("id_back_path", customers.getIdBackPath());

        values.put("deleted", customers.getDeleted());

        if(customers.getUpdatedDate() != null)
        values.put("updated_date", String.valueOf(customers.getUpdatedDate()));

        // Inserting Row
        db.update("customers", values, "id = ? ", new String[]{customers.getId()});
        DatabaseManager.getInstance().closeDatabase();

    }

    public static ArrayList<Customers> getAllCustomers(String userid, String stationid, String roleid) {

        Customers beans;
        ArrayList<Customers> customerList = new ArrayList<>();

        try {

            Log.i("Data received: ", userid + " " + stationid + " " + roleid);

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String selectQuery;
            Cursor cursor = null;

            selectQuery = "select * from customers where deleted = 0 and stations_id = ? and (national_id is not null or national_id <> '' ) order by creation_date desc";
            cursor = db.rawQuery(selectQuery, new String[]{stationid});

            Log.i("Rows found: ", String.valueOf(cursor.getCount()));

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    beans = new Customers();
                    beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                    beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                    beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                    beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                    if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                        beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                    }
                    if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                        beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                    }
                    if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                        beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                    }
                    beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                    beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                    beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                    beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                    beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                    beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                    if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                        beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                    }
                    if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                        beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                    }
                    beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                    beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                    if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                        beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                    }
                    beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                    beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                    beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                    beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                    beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                    beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                    beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                    beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                    beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                    beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                    beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                    beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                    beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                    beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                    beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                    beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                    beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

                    customerList.add(beans);
                } while (cursor.moveToNext());
            }

            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return customerList;
    }

    public static ArrayList<Customers> searchCustomers(String station, String phone, String firstName, String lastName, String nationalId) {

        ArrayList<Customers> list = new ArrayList<>();
        ArrayList<Customers> finalList = new ArrayList<>();

        if (!TextUtils.isEmpty(phone)) {
            if (list.size() == 0) {
                List<Customers> byphoneList = searchByCustomerPhone(station, phone);
                list.addAll(byphoneList);
            } else {
                for (Customers customers : list) {
                    if (String.valueOf(customers.getPrimaryPhone()).startsWith(phone) && customers.getStationsId().equals(station)) {
                        finalList.add(customers);
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(firstName)) {
            if (list.size() == 0) {
                List<Customers> byphoneList = searchByCustomerfirstName(station, firstName);
                list.addAll(byphoneList);
            } else {
                for (Customers customers : list) {
                    if (customers.getFirstName().startsWith(firstName) && customers.getStationsId().equals(station)) {
                        finalList.add(customers);
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(lastName)) {
            if (list.size() == 0) {
                List<Customers> byphoneList = searchByCustomerlastName(station, lastName);
                list.addAll(byphoneList);
            } else {
                for (Customers customers : list) {
                    if (customers.getLastName().startsWith(lastName) && customers.getStationsId().equals(station)) {
                        finalList.add(customers);
                    }
                }
            }

        }

        if (!TextUtils.isEmpty(nationalId)) {
            if (list.size() == 0) {
                List<Customers> byphoneList = searchByCustomernationalId(station, nationalId);
                list.addAll(byphoneList);
            } else {
                for (Customers customers : list) {
                    if (customers.getId().startsWith(nationalId) && customers.getStationsId().equals(station)) {
                        finalList.add(customers);
                    }
                }

            }
        }

        if (finalList.size() == 0){
            finalList.addAll(list);
        }

        return finalList;
    }

    private static List<Customers> searchByCustomernationalId(String station, String nationalId) {

        Customers beans;
        List<Customers> list = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("customers", null, " stations_id = ? and (national_id is not null or national_id <> '' ) and national_id like ?", new String[]{station, nationalId + "%"}, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new Customers();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                    beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                    beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                    beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                }
                beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                    beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                }
                beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

                list.add(beans);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return list;
    }

    private static List<Customers> searchByCustomerlastName(String station, String lastName) {

        Customers beans;
        List<Customers> list = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("customers", null, " stations_id = ? and (national_id is not null or national_id <> '' ) and last_name like ?", new String[]{station, lastName + "%"}, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new Customers();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                    beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                    beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                    beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                }
                beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                    beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                }
                beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

                list.add(beans);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return list;
    }

    private static List<Customers> searchByCustomerfirstName(String station, String firstName) {

        Customers beans;
        List<Customers> list = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("customers", null, "stations_id = ? and (national_id is not null or national_id <> '' ) and first_name like ?", new String[]{station, firstName + "%"}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new Customers();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                    beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                    beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                    beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                }
                beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                    beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                }
                beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

                list.add(beans);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return list;
    }

    private static List<Customers> searchByCustomerPhone(String station, String phone) {

        Customers beans;
        List<Customers> list = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("customers", null, "stations_id = ? and (national_id is not null or national_id <> '' ) and primary_phone like ?", new String[]{station, phone + "%"}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new Customers();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                    beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                    beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                    beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                }
                beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                    beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                }
                beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

                list.add(beans);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return list;
    }

    public long getCustomersCount() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, "customers");

        db.close();
        DatabaseManager.getInstance().closeDatabase();

        return cnt;
    }

    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete("customers", "is_synced_created = 1", null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public Customers getByPhone(String primaryphone) {

        Customers beans = null;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("customers", null, " primary_phone = ?", new String[]{primaryphone}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new Customers();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                    beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                    beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                    beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                }
                beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                    beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                }
                beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return beans;
    }

    public static ArrayList<Customers> getAllLeads(String userid, String stationid, String roleid) {

        Customers beans;
        ArrayList<Customers> customerList = new ArrayList<>();

        try {

            Log.i("Data received: ", userid + " " + stationid + " " + roleid);

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String selectQuery;
            Cursor cursor = null;

            selectQuery = "select * from customers where deleted = 0 and stations_id = ? and (loan_officer_id = ? or collections_officer_id = ?) and (national_id is null or national_id = '') order by creation_date desc";
            cursor = db.rawQuery(selectQuery, new String[]{stationid, userid});

            Log.i("Rows found: ", String.valueOf(cursor.getCount()));

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    beans = new Customers();
                    beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                    beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                    beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                    beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                    if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                        beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                    }
                    if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                        beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                    }
                    if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                        beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                    }
                    beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                    beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                    beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                    beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                    beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                    beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                    if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                        beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                    }
                    if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                        beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                    }
                    beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                    beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                    if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                        beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                    }
                    beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                    beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                    beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                    beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                    beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                    beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                    beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                    beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                    beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                    beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                    beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                    beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                    beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                    beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                    beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                    beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                    beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));


                    customerList.add(beans);
                } while (cursor.moveToNext());
            }

            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return customerList;
    }

    public Customers getCustomerById(String id) {

        Customers beans = null;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("customers", null, " id = ?", new String[]{id}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new Customers();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                    beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                    beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                    beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                }
                beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                    beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                }
                beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));
                
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return beans;
    }

    public Customers getByDisPhone(String disburse_phone) {

        Customers beans = null;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("customers", null, " disburse_phone = ?", new String[]{disburse_phone}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new Customers();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                beans.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                beans.setAlsoKnownAs(cursor.getString(cursor.getColumnIndex("also_known_as")));

                if (!cursor.isNull(cursor.getColumnIndex("alternative_phone"))) {
                    beans.setAlternativePhone(Long.valueOf(cursor.getString(cursor.getColumnIndex("alternative_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("disburse_phone"))) {
                    beans.setDisbursePhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("disburse_phone"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("primary_phone"))) {
                    beans.setPrimaryPhone(Long.parseLong(cursor.getString(cursor.getColumnIndex("primary_phone"))));
                }
                beans.setInfoSourcesId(cursor.getString(cursor.getColumnIndex("info_sources_id")));
                beans.setOtherSources(cursor.getString(cursor.getColumnIndex("other_sources")));
                beans.setNationalId(cursor.getString(cursor.getColumnIndex("national_id")));
                beans.setLivedThereSince(cursor.getString(cursor.getColumnIndex("lived_there_since")));
                beans.setHomeOwnershipId(cursor.getString(cursor.getColumnIndex("home_ownership_id")));
                beans.setHomeAddress(cursor.getString(cursor.getColumnIndex("home_address")));

                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                beans.setDateOfBirth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                beans.setGenderId(cursor.getString(cursor.getColumnIndex("gender_id")));
                if (!cursor.isNull(cursor.getColumnIndex("asset_list"))) {
                    beans.setAssetList(Byte.valueOf(cursor.getString(cursor.getColumnIndex("asset_list"))));
                }
                beans.setTitleId(cursor.getString(cursor.getColumnIndex("title_id")));
                beans.setLanguageId(cursor.getString(cursor.getColumnIndex("language_id")));
                beans.setMaritalStatusId(cursor.getString(cursor.getColumnIndex("marital_status_id")));
                beans.setActiveStatusId(cursor.getString(cursor.getColumnIndex("active_status_id")));
                beans.setCustomerStateId(cursor.getString(cursor.getColumnIndex("customer_state_id")));
                beans.setApprovalStatusId(cursor.getString(cursor.getColumnIndex("approval_status_id")));
                beans.setLoanOfficerId(cursor.getString(cursor.getColumnIndex("loan_officer_id")));
                beans.setCollectionsOfficerId(cursor.getString(cursor.getColumnIndex("collections_officer_id")));
                beans.setStationsId(cursor.getString(cursor.getColumnIndex("stations_id")));
                beans.setMarketId(cursor.getString(cursor.getColumnIndex("market_id")));
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                beans.setIdFrontPath(cursor.getString(cursor.getColumnIndex("id_front_path")));
                beans.setIdBackPath(cursor.getString(cursor.getColumnIndex("id_back_path")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return beans;
    }
}
