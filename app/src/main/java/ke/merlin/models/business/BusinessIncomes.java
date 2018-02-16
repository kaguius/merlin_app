package ke.merlin.models.business;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 10/05/2017.
 */
public class BusinessIncomes implements Serializable {
    private String id;
    private String businessCycleId;
    private double currentStockValue;
    private double salesPerCycle;
    private double spendOnStock;
    private String incomeExplanation;
    private int cycleRestockingFrequency;
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

    public String getBusinessCycleId() {
        return businessCycleId;
    }

    public void setBusinessCycleId(String businessCycleId) {
        this.businessCycleId = businessCycleId;
    }

    public double getCurrentStockValue() {
        return currentStockValue;
    }

    public void setCurrentStockValue(double currentStockValue) {
        this.currentStockValue = currentStockValue;
    }

    public double getSalesPerCycle() {
        return salesPerCycle;
    }

    public void setSalesPerCycle(double salesPerCycle) {
        this.salesPerCycle = salesPerCycle;
    }

    public double getSpendOnStock() {
        return spendOnStock;
    }

    public void setSpendOnStock(double spendOnStock) {
        this.spendOnStock = spendOnStock;
    }

    public String getIncomeExplanation() {
        return incomeExplanation;
    }

    public void setIncomeExplanation(String incomeExplanation) {
        this.incomeExplanation = incomeExplanation;
    }

    public int getCycleRestockingFrequency() {
        return cycleRestockingFrequency;
    }

    public void setCycleRestockingFrequency(int cycleRestockingFrequency) {
        this.cycleRestockingFrequency = cycleRestockingFrequency;
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
