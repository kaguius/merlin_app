package ke.merlin.models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by mecmurimi on 24/07/2017.
 */

public class Last_sync {

    private String id;
    private String table_name;
    private String from_date;
    private String to_date;
    private byte is_synced;
    private Timestamp creationDate;
    private Timestamp updatedDate;

    public Last_sync(String table_name, String from_date, String to_date, int is_synced) {
        this.id = UUID.randomUUID().toString();
        this.table_name = table_name;
        this.from_date = from_date;
        this.to_date = to_date;
        this.is_synced = (byte) is_synced;
        this.creationDate = new Timestamp(new Date().getTime());
        this.updatedDate = new Timestamp(new Date().getTime());

    }

    public Last_sync() {

    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public byte getIs_synced() {
        return is_synced;
    }

    public void setIs_synced(byte is_synced) {
        this.is_synced = is_synced;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
