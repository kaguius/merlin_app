package ke.merlin.models.business;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 10/05/2017.
 */

public class BusinessAssessment implements Serializable {

    private String id;
    private byte stockNeat;
    private byte accurateLedgerBook;
    private byte salesActivity;
    private byte permanentOperation;
    private byte proofOfOwnership;
    private byte forthcomingAndTransparent;
    private byte knownToMarketAuthorities;
    private byte soundReputation;
    private byte wouldILend;
    private Double lendAmount;
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

    public byte getStockNeat() {
        return stockNeat;
    }

    public void setStockNeat(byte stockNeat) {
        this.stockNeat = stockNeat;
    }

    public byte getAccurateLedgerBook() {
        return accurateLedgerBook;
    }

    public void setAccurateLedgerBook(byte accurateLedgerBook) {
        this.accurateLedgerBook = accurateLedgerBook;
    }

    public byte getSalesActivity() {
        return salesActivity;
    }

    public void setSalesActivity(byte salesActivity) {
        this.salesActivity = salesActivity;
    }

    public byte getPermanentOperation() {
        return permanentOperation;
    }

    public void setPermanentOperation(byte permanentOperation) {
        this.permanentOperation = permanentOperation;
    }

    public byte getProofOfOwnership() {
        return proofOfOwnership;
    }

    public void setProofOfOwnership(byte proofOfOwnership) {
        this.proofOfOwnership = proofOfOwnership;
    }

    public byte getForthcomingAndTransparent() {
        return forthcomingAndTransparent;
    }

    public void setForthcomingAndTransparent(byte forthcomingAndTransparent) {
        this.forthcomingAndTransparent = forthcomingAndTransparent;
    }

    public byte getKnownToMarketAuthorities() {
        return knownToMarketAuthorities;
    }

    public void setKnownToMarketAuthorities(byte knownToMarketAuthorities) {
        this.knownToMarketAuthorities = knownToMarketAuthorities;
    }

    public byte getSoundReputation() {
        return soundReputation;
    }

    public void setSoundReputation(byte soundReputation) {
        this.soundReputation = soundReputation;
    }

    public byte getWouldILend() {
        return wouldILend;
    }

    public void setWouldILend(byte wouldILend) {
        this.wouldILend = wouldILend;
    }

    public Double getLendAmount() {
        return lendAmount;
    }

    public void setLendAmount(Double lendAmount) {
        this.lendAmount = lendAmount;
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
