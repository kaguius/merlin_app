package ke.merlin.dao.users;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.users.Roles;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 25/07/2017.
 */

public class RolesDao {

    private Roles roles;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public RolesDao(){
        roles = new Roles();
    }

    /**
     *
     * @return
     */
    public static String createRolesTable(){
        return "CREATE TABLE 'roles' (\n" +
                "  'id' varchar(150) NOT NULL,\n" +
                "  'name' varchar(100) NOT NULL DEFAULT '',\n" +
                "  'description' varchar(100) DEFAULT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_rls' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(Roles roles) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", roles.getId());
        values.put("name", roles.getName());
        values.put("creator_id", roles.getCreatorId());
        values.put("deleted", roles.getDeleted());
        values.put("creation_date", String.valueOf(roles.getCreationDate()));
        values.put("updated_date", String.valueOf(roles.getUpdatedDate()));

        // Inserting Row
        db.insert("roles", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<Roles> list = gson.fromJson(response, new TypeToken<List<Roles>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (Roles roles: list) {

                ContentValues values = new ContentValues();
                values.put("id", roles.getId());
                values.put("name", roles.getName());
                values.put("creator_id", roles.getCreatorId());
                values.put("deleted", roles.getDeleted());
                values.put("creation_date", String.valueOf(roles.getCreationDate()));
                values.put("updated_date", String.valueOf(roles.getUpdatedDate()));

                cnt = db.insertWithOnConflict("roles", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
