package ke.merlin.models.suspense;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 26/06/2017.
 */
public class SuspensePayments implements Serializable {
    private String id;
    private String name;
    private String accountNumber;
    private long mobile;
    private String shortcode;
    private double amount;
    private String paymentDate;
    private byte resolved;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public byte getResolved() {
        return resolved;
    }

    public void setResolved(byte resolved) {
        this.resolved = resolved;
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
