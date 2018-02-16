package ke.merlin.dao.users;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.users.Privileges;
import ke.merlin.models.users.Roles;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.database.DatabaseManager;

/**
 * Created by mecmurimi on 24/09/2017.
 */

public class PrivilegesDao {
    private Privileges privileges;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public PrivilegesDao(){
        privileges = new Privileges();
    }

    /**
     *
     * @return
     */
    public static String createPrivilegesTable(){
        return "CREATE TABLE 'privileges' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' text,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_id_sp' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";

    }

    public void insert(Privileges privileges) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", privileges.getId());
        values.put("name", privileges.getName());
        values.put("creator_id", privileges.getCreatorId());
        values.put("deleted", privileges.getDeleted());
        values.put("creation_date", String.valueOf(privileges.getCreationDate()));
        values.put("updated_date", String.valueOf(privileges.getUpdatedDate()));

        // Inserting Row
        db.insert("privileges", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<Privileges> list = gson.fromJson(response, new TypeToken<List<Privileges>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Privileges privileges: list) {

                ContentValues values = new ContentValues();
                values.put("id", privileges.getId());
                values.put("name", privileges.getName());
                values.put("creator_id", privileges.getCreatorId());
                values.put("deleted", privileges.getDeleted());
                values.put("creation_date", String.valueOf(privileges.getCreationDate()));
                values.put("updated_date", String.valueOf(privileges.getUpdatedDate()));

                cnt = db.insertWithOnConflict("privileges", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
