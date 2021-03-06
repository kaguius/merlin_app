package ke.merlin.models.loans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 10/05/2017.
 */
public class LoansOverpayments implements Serializable {
    private String id;
    private String customerId;
    private String fromLoansId;
    private String toLoansId;
    private double amount;
    private byte to_create;
    private byte to_update;
    private String creatorId;
    private byte deleted;
    private Timestamp creationDate;
    private Timestamp updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFromLoansId() {
        return fromLoansId;
    }

    public void setFromLoansId(String fromLoansId) {
        this.fromLoansId = fromLoansId;
    }

    public String getToLoansId() {
        return toLoansId;
    }

    public void setToLoansId(String toLoansId) {
        this.toLoansId = toLoansId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public byte getTo_create() {
        return to_create;
    }

    public void setTo_create(byte to_create) {
        this.to_create = to_create;
    }

    public byte getTo_update() {
        return to_update;
    }

    public void setTo_update(byte to_update) {
        this.to_update = to_update;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public byte getDeleted() {
        return deleted;
    }

    public void setDeleted(byte deleted) {
        this.deleted = deleted;
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
