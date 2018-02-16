package ke.merlin.models.loans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 10/05/2017.
 */
public class LoansInteractions implements Serializable {
    private String id;
    private String loanId;
    private String customerId;
    private String interactionTypeId;
    private String interactionCategoryId;
    private String nextInteractionDate;
    private String callbackTime;
    private double amount;
    private String comments;
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

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInteractionTypeId() {
        return interactionTypeId;
    }

    public void setInteractionTypeId(String interactionTypeId) {
        this.interactionTypeId = interactionTypeId;
    }

    public String getInteractionCategoryId() {
        return interactionCategoryId;
    }

    public void setInteractionCategoryId(String interactionCategoryId) {
        this.interactionCategoryId = interactionCategoryId;
    }

    public String getNextInteractionDate() {
        return nextInteractionDate;
    }

    public void setNextInteractionDate(String nextInteractionDate) {
        this.nextInteractionDate = nextInteractionDate;
    }

    public String getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(String callbackTime) {
        this.callbackTime = callbackTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
