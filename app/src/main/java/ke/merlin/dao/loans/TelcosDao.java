package ke.merlin.dao.loans;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.loans.Telcos;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class TelcosDao {

    private Telcos telcos;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public TelcosDao(){
        telcos = new Telcos();
    }

    /**
     *
     * @return
     */
    public static String createTelcosTable(){
        return "CREATE TABLE 'telcos' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                "  'country_id' varchar(150) NOT NULL,\n" +
                "  'telco_image_path' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL,\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_country_id_t' FOREIGN KEY ('country_id') REFERENCES 'st_countries' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_creator_tlcs' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(Telcos telcos) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", telcos.getId());
        values.put("name", telcos.getName());
        values.put("creator_id", telcos.getCreatorId());
        values.put("country_id", telcos.getCountryId());
        values.put("telco_image_path", telcos.getTelcoImagePath());
        values.put("deleted", telcos.getDeleted());
        values.put("creation_date", String.valueOf(telcos.getCreationDate()));
        values.put("updated_date", String.valueOf(telcos.getUpdatedDate()));

        // Inserting Row
        db.insert("telcos", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<Telcos> list = gson.fromJson(response, new TypeToken<List<Telcos>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Telcos telcos: list) {

                ContentValues values = new ContentValues();
                values.put("id", telcos.getId());
                values.put("name", telcos.getName());
                values.put("creator_id", telcos.getCreatorId());
                values.put("country_id", telcos.getCountryId());
                values.put("telco_image_path", telcos.getTelcoImagePath());
                values.put("deleted", telcos.getDeleted());
                values.put("creation_date", String.valueOf(telcos.getCreationDate()));
                values.put("updated_date", String.valueOf(telcos.getUpdatedDate()));

                cnt = db.insertWithOnConflict("telcos", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
