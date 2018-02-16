package ke.merlin.models.customers;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 25/05/2017.
 */
public class Customers implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String alsoKnownAs;
    private Long primaryPhone;
    private Long alternativePhone;
    private Long disbursePhone;
    private String infoSourcesId;
    private String otherSources;
    private String nationalId;
    private String dateOfBirth;
    private String genderId;
    private Byte assetList;
    private String titleId;
    private String languageId;
    private String maritalStatusId;
    private String activeStatusId;
    private String customerStateId;
    private String approvalStatusId;
    private String loanOfficerId;
    private String collectionsOfficerId;
    private String stationsId;
    private String marketId;
    private String photoPath;
    private String idFrontPath;
    private String idBackPath;
    private String creatorId;
    private byte to_create;
    private byte to_update;
    private byte deleted;
    private Timestamp creationDate;
    private Timestamp updatedDate;
    private String livedThereSince;
    private String homeOwnershipId;
    private String homeAddress;
    private Double longitudes;
    private Double latitudes;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAs(String alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
    }

    public Long getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(Long primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public Long getAlternativePhone() {
        return alternativePhone;
    }

    public void setAlternativePhone(Long alternativePhone) {
        this.alternativePhone = alternativePhone;
    }

    public Long getDisbursePhone() {
        return disbursePhone;
    }

    public void setDisbursePhone(Long disbursePhone) {
        this.disbursePhone = disbursePhone;
    }

    public String getInfoSourcesId() {
        return infoSourcesId;
    }

    public void setInfoSourcesId(String infoSourcesId) {
        this.infoSourcesId = infoSourcesId;
    }

    public String getOtherSources() {
        return otherSources;
    }

    public void setOtherSources(String otherSources) {
        this.otherSources = otherSources;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGenderId() {
        return genderId;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }

    public Byte getAssetList() {
        return assetList;
    }

    public void setAssetList(Byte assetList) {
        this.assetList = assetList;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getMaritalStatusId() {
        return maritalStatusId;
    }

    public void setMaritalStatusId(String maritalStatusId) {
        this.maritalStatusId = maritalStatusId;
    }

    public String getActiveStatusId() {
        return activeStatusId;
    }

    public void setActiveStatusId(String activeStatusId) {
        this.activeStatusId = activeStatusId;
    }

    public String getCustomerStateId() {
        return customerStateId;
    }

    public void setCustomerStateId(String customerStateId) {
        this.customerStateId = customerStateId;
    }

    public String getApprovalStatusId() {
        return approvalStatusId;
    }

    public void setApprovalStatusId(String approvalStatusId) {
        this.approvalStatusId = approvalStatusId;
    }

    public String getLoanOfficerId() {
        return loanOfficerId;
    }

    public void setLoanOfficerId(String loanOfficerId) {
        this.loanOfficerId = loanOfficerId;
    }

    public String getCollectionsOfficerId() {
        return collectionsOfficerId;
    }

    public void setCollectionsOfficerId(String collectionsOfficerId) {
        this.collectionsOfficerId = collectionsOfficerId;
    }

    public String getStationsId() {
        return stationsId;
    }

    public void setStationsId(String stationsId) {
        this.stationsId = stationsId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getIdFrontPath() {
        return idFrontPath;
    }

    public void setIdFrontPath(String idFrontPath) {
        this.idFrontPath = idFrontPath;
    }

    public String getIdBackPath() {
        return idBackPath;
    }

    public void setIdBackPath(String idBackPath) {
        this.idBackPath = idBackPath;
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

    public String getLivedThereSince() {
        return livedThereSince;
    }

    public void setLivedThereSince(String livedThereSince) {
        this.livedThereSince = livedThereSince;
    }

    public String getHomeOwnershipId() {
        return homeOwnershipId;
    }

    public void setHomeOwnershipId(String homeOwnershipId) {
        this.homeOwnershipId = homeOwnershipId;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Double getLongitudes() {
        return longitudes;
    }

    public void setLongitudes(Double longitudes) {
        this.longitudes = longitudes;
    }

    public Double getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(Double latitudes) {
        this.latitudes = latitudes;
    }
}
