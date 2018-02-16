package ke.merlin.models.business;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 10/05/2017.
 */
public class BusinessExpenses implements Serializable {

    private String id;
    private double businessRent;
    private double businessUtilities;
    private String noOfEmployeesTypesId;
    private String natureOfEmploymentTypesId;
    private double employeesSalary;
    private double licensingFee;
    private double transportFee;
    private double storageFee;
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

    public double getBusinessRent() {
        return businessRent;
    }

    public void setBusinessRent(double businessRent) {
        this.businessRent = businessRent;
    }

    public double getBusinessUtilities() {
        return businessUtilities;
    }

    public void setBusinessUtilities(double businessUtilities) {
        this.businessUtilities = businessUtilities;
    }

    public String getNoOfEmployeesTypesId() {
        return noOfEmployeesTypesId;
    }

    public void setNoOfEmployeesTypesId(String noOfEmployeesTypesId) {
        this.noOfEmployeesTypesId = noOfEmployeesTypesId;
    }

    public String getNatureOfEmploymentTypesId() {
        return natureOfEmploymentTypesId;
    }

    public void setNatureOfEmploymentTypesId(String natureOfEmploymentTypesId) {
        this.natureOfEmploymentTypesId = natureOfEmploymentTypesId;
    }

    public double getEmployeesSalary() {
        return employeesSalary;
    }

    public void setEmployeesSalary(double employeesSalary) {
        this.employeesSalary = employeesSalary;
    }

    public double getLicensingFee() {
        return licensingFee;
    }

    public void setLicensingFee(double licensingFee) {
        this.licensingFee = licensingFee;
    }

    public double getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(double transportFee) {
        this.transportFee = transportFee;
    }

    public double getStorageFee() {
        return storageFee;
    }

    public void setStorageFee(double storageFee) {
        this.storageFee = storageFee;
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
