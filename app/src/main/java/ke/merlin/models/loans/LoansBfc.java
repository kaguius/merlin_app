package ke.merlin.models.loans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 10/05/2017.
 */
public class LoansBfc implements Serializable {
    private String id;
    private String loansId;
    private String customerId;
    private String suspicious;
    private String professional;
    private String firstLoan;
    private String loansHistory;
    private String timelyPayments;
    private String willingToPay;
    private String payFullAmount;
    private String paymentPlan;
    private String customerSector;
    private String notablePerson;
    private String staffAssessment;
    private String defaultReasonId;
    private String otherSources;
    private Integer pointsEarned;
    private String customerState;
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

    public String getLoansId() {
        return loansId;
    }

    public void setLoansId(String loansId) {
        this.loansId = loansId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSuspicious() {
        return suspicious;
    }

    public void setSuspicious(String suspicious) {
        this.suspicious = suspicious;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getFirstLoan() {
        return firstLoan;
    }

    public void setFirstLoan(String firstLoan) {
        this.firstLoan = firstLoan;
    }

    public String getLoansHistory() {
        return loansHistory;
    }

    public void setLoansHistory(String loansHistory) {
        this.loansHistory = loansHistory;
    }

    public String getTimelyPayments() {
        return timelyPayments;
    }

    public void setTimelyPayments(String timelyPayments) {
        this.timelyPayments = timelyPayments;
    }

    public String getWillingToPay() {
        return willingToPay;
    }

    public void setWillingToPay(String willingToPay) {
        this.willingToPay = willingToPay;
    }

    public String getPayFullAmount() {
        return payFullAmount;
    }

    public void setPayFullAmount(String payFullAmount) {
        this.payFullAmount = payFullAmount;
    }

    public String getPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(String paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public String getCustomerSector() {
        return customerSector;
    }

    public void setCustomerSector(String customerSector) {
        this.customerSector = customerSector;
    }

    public String getNotablePerson() {
        return notablePerson;
    }

    public void setNotablePerson(String notablePerson) {
        this.notablePerson = notablePerson;
    }

    public String getStaffAssessment() {
        return staffAssessment;
    }

    public void setStaffAssessment(String staffAssessment) {
        this.staffAssessment = staffAssessment;
    }

    public String getDefaultReasonId() {
        return defaultReasonId;
    }

    public void setDefaultReasonId(String defaultReasonId) {
        this.defaultReasonId = defaultReasonId;
    }

    public String getOtherSources() {
        return otherSources;
    }

    public void setOtherSources(String otherSources) {
        this.otherSources = otherSources;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
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
