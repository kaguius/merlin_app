package ke.merlin.dao.customers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ke.merlin.models.customers.StCreditOrganisations;
import ke.merlin.models.customers.StCreditOrganisationsTypes;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class StCreditOrganisationsTypesDao {

    private StCreditOrganisationsTypes stCreditOrganisationsTypes;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StCreditOrganisationsTypesDao(){
        stCreditOrganisationsTypes = new StCreditOrganisationsTypes();
    }

    /**
     *
     * @return
     */
    public static String createCreditOrganisationTypesTable(){
        return "CREATE TABLE 'st_credit_organisations_types' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_cot' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(StCreditOrganisationsTypes stCreditOrganisationsTypes) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stCreditOrganisationsTypes.getId());
        values.put("name", stCreditOrganisationsTypes.getName());
        values.put("description", stCreditOrganisationsTypes.getDescription());
        values.put("creator_id", stCreditOrganisationsTypes.getCreatorId());
        values.put("deleted", stCreditOrganisationsTypes.getDeleted());
        values.put("creation_date", String.valueOf(stCreditOrganisationsTypes.getCreationDate()));
        values.put("updated_date", String.valueOf(stCreditOrganisationsTypes.getUpdatedDate()));

        // Inserting Row
        db.insert("st_credit_organisations_types", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StCreditOrganisationsTypes> list = gson.fromJson(response, new TypeToken<List<StCreditOrganisationsTypes>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StCreditOrganisationsTypes stCreditOrganisationsTypes: list) {

                ContentValues values = new ContentValues();
                values.put("id", stCreditOrganisationsTypes.getId());
                values.put("name", stCreditOrganisationsTypes.getName());
                values.put("description", stCreditOrganisationsTypes.getDescription());
                values.put("creator_id", stCreditOrganisationsTypes.getCreatorId());
                values.put("deleted", stCreditOrganisationsTypes.getDeleted());
                values.put("creation_date", String.valueOf(stCreditOrganisationsTypes.getCreationDate()));
                values.put("updated_date", String.valueOf(stCreditOrganisationsTypes.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_credit_organisations_types", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public HashMap<String, String> getSpinnerItems() {

        StCreditOrganisationsTypes stCreditOrganisationsTypes;
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "select * from st_credit_organisations_types";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                stCreditOrganisationsTypes = new StCreditOrganisationsTypes();
                stCreditOrganisationsTypes.setId(cursor.getString(cursor.getColumnIndex("id")));
                stCreditOrganisationsTypes.setName(cursor.getString(cursor.getColumnIndex("name")));
                map.put(stCreditOrganisationsTypes.getId(), stCreditOrganisationsTypes.getName());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return map;
    }
}
