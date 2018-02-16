package ke.merlin.dao.users;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import ke.merlin.models.users.StUsersStatus;
import ke.merlin.utils.database.DatabaseManager;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 25/07/2017.
 */

public class StUsersStatusDao {

    private StUsersStatus stUsersStatus;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    public StUsersStatusDao(){
        stUsersStatus = new StUsersStatus();
    }

    /**
     *
     * @return
     */
    public static String createUserStatusTable(){
        return "CREATE TABLE 'st_users_status' (\n" +
                "  'id' varchar(150) NOT NULL,\n" +
                "  'name' varchar(100) DEFAULT NULL,\n" +
                "  'description' varchar(150) DEFAULT NULL,\n" +
                " 'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                " 'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'deleted' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                ")";
    }

    public void insert(StUsersStatus stUsersStatus) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", stUsersStatus.getId());
        values.put("name", stUsersStatus.getName());
        values.put("creator_id", stUsersStatus.getCreatorId());
        values.put("deleted", stUsersStatus.getDeleted());
        values.put("creation_date", String.valueOf(stUsersStatus.getCreationDate()));
        values.put("updated_date", String.valueOf(stUsersStatus.getUpdatedDate()));

        // Inserting Row
        db.insert("st_users_status", null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<StUsersStatus> list = gson.fromJson(response, new TypeToken<List<StUsersStatus>>() {}.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (StUsersStatus stUsersStatus: list) {

                ContentValues values = new ContentValues();
                values.put("id", stUsersStatus.getId());
                values.put("name", stUsersStatus.getName());
                values.put("creator_id", stUsersStatus.getCreatorId());
                values.put("deleted", stUsersStatus.getDeleted());
                values.put("creation_date", String.valueOf(stUsersStatus.getCreationDate()));
                values.put("updated_date", String.valueOf(stUsersStatus.getUpdatedDate()));

                cnt = db.insertWithOnConflict("st_users_status", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }
}
