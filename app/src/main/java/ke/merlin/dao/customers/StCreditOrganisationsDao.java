package ke.merlin.dao.customers;

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

import ke.merlin.models.customers.StChamaCycles;
import ke.merlin.models.customers.StCreditOrganisations;
import ke.merlin.models.customers.StHomeOwnerships;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StCreditOrganisationsDao {

    private StCreditOrganisations stCreditOrganisations;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StCreditOrganisationsDao(){
        stCreditOrganisations = new StCreditOrganisations();
    }

    /**
     *
     * @return
     */
    public static String createCreditOrganisationTable(){
        return "CREATE TABLE 'st_credit_organisations' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'credit_organisation_type_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_credit_organisation_type_cro' FOREIGN KEY ('credit_organisation_type_id') REFERENCES 'st_credit_organisations_types' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StCreditOrganisations stCreditOrganisations) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stCreditOrganisations.getId());
        values.put("name", stCreditOrganisations.getName());
        values.put("description", stCreditOrganisations.getDescription());
        values.put("creator_id", stCreditOrganisations.getCreatorId());
        values.put("deleted", stCreditOrganisations.getDeleted());
        values.put("creation_date", String.valueOf(stCreditOrganisations.getCreationDate()));
        values.put("updated_date", String.valueOf(stCreditOrganisations.getUpdatedDate()));

        // Inserting Row
        db.insert("st_credit_organisations", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StCreditOrganisations> list = gson.fromJson(response, new TypeToken<List<StCreditOrganisations>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StCreditOrganisations stCreditOrganisations: list) {

                ContentValues values = new ContentValues();
                values.put("id", stCreditOrganisations.getId());
                values.put("name", stCreditOrganisations.getName());
                values.put("description", stCreditOrganisations.getDescription());
                values.put("creator_id", stCreditOrganisations.getCreatorId());
                values.put("deleted", stCreditOrganisations.getDeleted());
                values.put("creation_date", String.valueOf(stCreditOrganisations.getCreationDate()));
                values.put("updated_date", String.valueOf(stCreditOrganisations.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_credit_organisations", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StCreditOrganisations stCreditOrganisations;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_credit_organisations";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stCreditOrganisations = new StCreditOrganisations();
                stCreditOrganisations.setId(cursor.getString(cursor.getColumnIndex("id")));
                stCreditOrganisations.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stCreditOrganisations.getId(), stCreditOrganisations.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }

    public StCreditOrganisations getById(String id) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "select * from st_credit_organisations where id = ? and deleted = 0";

        int iCount =0;
        StCreditOrganisations stCreditOrganisations = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) } );

        if (cursor.moveToFirst()) {
            do {
                stCreditOrganisations = new StCreditOrganisations();
                stCreditOrganisations.setId(cursor.getString(cursor.getColumnIndex("id")));
                stCreditOrganisations.setName(cursor.getString(cursor.getColumnIndex("name")));
                stCreditOrganisations.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                stCreditOrganisations.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                stCreditOrganisations.setDeleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("deleted"))));
                stCreditOrganisations.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                stCreditOrganisations.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return stCreditOrganisations;
    }
}
