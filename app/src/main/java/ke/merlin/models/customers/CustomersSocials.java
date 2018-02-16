package ke.merlin.models.customers;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 25/05/2017.
 */
public class CustomersSocials implements Serializable {
    private String id;
    private byte haveAnAccount;
    private String accountOrganisation;
    private byte everTakenALoan;
    private String loanOrganisation;
    private byte otherAccessToCredit;
    private String otherCreditOrganisation;
    private byte anyCurrentLoan;
    private double outstandingLoanAmount;
    private String currentLoanOrganisation;
    private int dailyCustomers;
    private byte employed;
    private String educationLevelId;
    private String customersId;
    private byte to_create;
    private byte to_update;
    private String siteVisitId;
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

    public byte getHaveAnAccount() {
        return haveAnAccount;
    }

    public void setHaveAnAccount(byte haveAnAccount) {
        this.haveAnAccount = haveAnAccount;
    }

    public String getAccountOrganisation() {
        return accountOrganisation;
    }

    public void setAccountOrganisation(String accountOrganisation) {
        this.accountOrganisation = accountOrganisation;
    }

    public byte getEverTakenALoan() {
        return everTakenALoan;
    }

    public void setEverTakenALoan(byte everTakenALoan) {
        this.everTakenALoan = everTakenALoan;
    }

    public String getLoanOrganisation() {
        return loanOrganisation;
    }

    public void setLoanOrganisation(String loanOrganisation) {
        this.loanOrganisation = loanOrganisation;
    }

    public byte getOtherAccessToCredit() {
        return otherAccessToCredit;
    }

    public void setOtherAccessToCredit(byte otherAccessToCredit) {
        this.otherAccessToCredit = otherAccessToCredit;
    }

    public String getOtherCreditOrganisation() {
        return otherCreditOrganisation;
    }

    public void setOtherCreditOrganisation(String otherCreditOrganisation) {
        this.otherCreditOrganisation = otherCreditOrganisation;
    }

    public byte getAnyCurrentLoan() {
        return anyCurrentLoan;
    }

    public void setAnyCurrentLoan(byte anyCurrentLoan) {
        this.anyCurrentLoan = anyCurrentLoan;
    }

    public double getOutstandingLoanAmount() {
        return outstandingLoanAmount;
    }

    public void setOutstandingLoanAmount(double outstandingLoanAmount) {
        this.outstandingLoanAmount = outstandingLoanAmount;
    }

    public String getCurrentLoanOrganisation() {
        return currentLoanOrganisation;
    }

    public void setCurrentLoanOrganisation(String currentLoanOrganisation) {
        this.currentLoanOrganisation = currentLoanOrganisation;
    }

    public int getDailyCustomers() {
        return dailyCustomers;
    }

    public void setDailyCustomers(int dailyCustomers) {
        this.dailyCustomers = dailyCustomers;
    }

    public byte getEmployed() {
        return employed;
    }

    public void setEmployed(byte employed) {
        this.employed = employed;
    }

    public String getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(String educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public String getCustomersId() {
        return customersId;
    }

    public void setCustomersId(String customersId) {
        this.customersId = customersId;
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

    public String getSiteVisitId() {
        return siteVisitId;
    }

    public void setSiteVisitId(String siteVisitId) {
        this.siteVisitId = siteVisitId;
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
