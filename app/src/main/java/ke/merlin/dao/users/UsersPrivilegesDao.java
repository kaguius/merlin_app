package ke.merlin.dao.users;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.users.UsersPrivileges;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 26/07/2017.
 */

public class UsersPrivilegesDao {

    private UsersPrivileges usersPrivileges;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public UsersPrivilegesDao(){
        usersPrivileges = new UsersPrivileges();
    }

    /**
     *
     * @return
     */
    public static String createUserPrivilegesTable(){
        return "CREATE TABLE 'users_privileges' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'users_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'privileges_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'is_removable' tinyint(1) NOT NULL DEFAULT '0',\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_creator_upv' FOREIGN KEY ('creator_id') REFERENCES 'users' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_privileges_up' FOREIGN KEY ('privileges_id') REFERENCES 'privileges' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_users_id_upv' FOREIGN KEY ('users_id') REFERENCES 'users' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(UsersPrivileges usersPrivileges) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", usersPrivileges.getId());
        values.put("users_id", usersPrivileges.getUsersId());
        values.put("privileges_id", usersPrivileges.getPrivilegesId());
        values.put("is_removable", usersPrivileges.getIsRemovable());
        values.put("creator_id", usersPrivileges.getCreatorId());
        values.put("deleted", usersPrivileges.getDeleted());
        values.put("creation_date", String.valueOf(usersPrivileges.getCreationDate()));
        values.put("updated_date", String.valueOf(usersPrivileges.getUpdatedDate()));

        // Inserting Row
        db.insert("Users_privileges", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<UsersPrivileges> list = gson.fromJson(response, new TypeToken<List<UsersPrivileges>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (UsersPrivileges usersPrivileges: list) {

                ContentValues values = new ContentValues();
                values.put("id", usersPrivileges.getId());
                values.put("users_id", usersPrivileges.getUsersId());
                values.put("privileges_id", usersPrivileges.getPrivilegesId());
                values.put("is_removable", usersPrivileges.getIsRemovable());
                values.put("creator_id", usersPrivileges.getCreatorId());
                values.put("deleted", usersPrivileges.getDeleted());
                values.put("creation_date", String.valueOf(usersPrivileges.getCreationDate()));
                values.put("updated_date", String.valueOf(usersPrivileges.getUpdatedDate()));

                cnt = db.insertWithOnConflict("Users_privileges", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
