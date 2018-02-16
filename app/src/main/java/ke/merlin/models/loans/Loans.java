package ke.merlin.models.loans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 10/05/2017.
 */

public class Loans implements Serializable {
    private String id;
    private long mobile;
    private double amount;
    private int fees;
    private int interest;
    private int earlySettlement;
    private String loanDate;
    private String loanDueDate;
    private String transactionCode;
    private String loanApplicationId;
    private byte isFraud;
    private String arrearsStatusId;
    private String assignmentStatusId;
    private String collectorId;
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

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public int getEarlySettlement() {
        return earlySettlement;
    }

    public void setEarlySettlement(int earlySettlement) {
        this.earlySettlement = earlySettlement;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getLoanDueDate() {
        return loanDueDate;
    }

    public void setLoanDueDate(String loanDueDate) {
        this.loanDueDate = loanDueDate;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(String loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public byte getIsFraud() {
        return isFraud;
    }

    public void setIsFraud(byte isFraud) {
        this.isFraud = isFraud;
    }

    public String getArrearsStatusId() {
        return arrearsStatusId;
    }

    public void setArrearsStatusId(String arrearsStatusId) {
        this.arrearsStatusId = arrearsStatusId;
    }

    public String getAssignmentStatusId() {
        return assignmentStatusId;
    }

    public void setAssignmentStatusId(String assignmentStatusId) {
        this.assignmentStatusId = assignmentStatusId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
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
