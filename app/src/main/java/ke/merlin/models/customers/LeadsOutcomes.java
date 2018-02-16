package ke.merlin.models.customers;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 25/05/2017.
 */
public class LeadsOutcomes implements Serializable {
    private String id;
    private String customersId;
    private String outcomesId;
    private String outcomeExplanation;
    private String nextVisitDate;
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

    public String getCustomersId() {
        return customersId;
    }

    public void setCustomersId(String customersId) {
        this.customersId = customersId;
    }

    public String getOutcomesId() {
        return outcomesId;
    }

    public void setOutcomesId(String outcomesId) {
        this.outcomesId = outcomesId;
    }

    public String getOutcomeExplanation() {
        return outcomeExplanation;
    }

    public void setOutcomeExplanation(String outcomeExplanation) {
        this.outcomeExplanation = outcomeExplanation;
    }

    public String getNextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(String nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
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
