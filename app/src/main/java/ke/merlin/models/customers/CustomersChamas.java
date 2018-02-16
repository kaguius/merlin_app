package ke.merlin.models.customers;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 25/05/2017.
 */
public class CustomersChamas implements Serializable {
    private String id;
    private String chamaCycleId;
    private double chamaContribution;
    private int noOfChamaMembers;
    private int chamaPayoutFrequency;
    private int chamaPayoutAmount;
    private byte chamaHasRecords;
    private String customersId;
    private String siteVisitId;
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

    public String getChamaCycleId() {
        return chamaCycleId;
    }

    public void setChamaCycleId(String chamaCycleId) {
        this.chamaCycleId = chamaCycleId;
    }

    public double getChamaContribution() {
        return chamaContribution;
    }

    public void setChamaContribution(double chamaContribution) {
        this.chamaContribution = chamaContribution;
    }

    public int getNoOfChamaMembers() {
        return noOfChamaMembers;
    }

    public void setNoOfChamaMembers(int noOfChamaMembers) {
        this.noOfChamaMembers = noOfChamaMembers;
    }

    public int getChamaPayoutFrequency() {
        return chamaPayoutFrequency;
    }

    public void setChamaPayoutFrequency(int chamaPayoutFrequency) {
        this.chamaPayoutFrequency = chamaPayoutFrequency;
    }

    public int getChamaPayoutAmount() {
        return chamaPayoutAmount;
    }

    public void setChamaPayoutAmount(int chamaPayoutAmount) {
        this.chamaPayoutAmount = chamaPayoutAmount;
    }

    public byte getChamaHasRecords() {
        return chamaHasRecords;
    }

    public void setChamaHasRecords(byte chamaHasRecords) {
        this.chamaHasRecords = chamaHasRecords;
    }

    public String getCustomersId() {
        return customersId;
    }

    public void setCustomersId(String customersId) {
        this.customersId = customersId;
    }

    public String getSiteVisitId() {
        return siteVisitId;
    }

    public void setSiteVisitId(String siteVisitId) {
        this.siteVisitId = siteVisitId;
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
