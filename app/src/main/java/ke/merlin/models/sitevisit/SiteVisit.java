package ke.merlin.models.sitevisit;

import java.sql.Timestamp;

/**
 * Created by mecmurimi on 24/09/2017.
 */

public class SiteVisit {
    private String id;
    private String customersId;
    private String businessCategoryId;
    private String businessTypeId;
    private String productTradingStartDate;
    private Byte isReset;
    private String photoPath;
    private Byte stockNeat;
    private Byte accurateLedgerBook;
    private Byte salesActivity;
    private Byte permanentOperation;
    private Byte proofOfOwnership;
    private Byte forthcomingAndTransparent;
    private Byte knownToMarketAuthorities;
    private Byte soundReputation;
    private Byte wouldILend;
    private Integer lendAmount;
    private Integer businessRent;
    private Integer businessUtilities;
    private String noOfEmployeesTypesId;
    private String natureOfEmploymentTypesId;
    private Integer employeesSalary;
    private Integer licensingFee;
    private Integer transportFee;
    private Integer storageFee;
    private String businessCycleId;
    private Integer currentStockValue;
    private Integer salesPerCycle;
    private Integer spendOnStock;
    private String incomeExplanation;
    private Integer cycleRestockingFrequency;
    private String businessLocationTypesId;
    private String locationTradingStartDate;
    private String businessAddress;
    private Double longitudes;
    private Double latitudes;
    private Byte in_a_chama;
    private String chamaCycleId;
    private Integer chamaContribution;
    private Integer noOfChamaMembers;
    private Integer chamaPayoutFrequency;
    private Integer chamaPayoutAmount;
    private Byte chamaHasRecords;
    private String houseTypeId;
    private Integer homeRent;
    private Integer houseUtilities;
    private Integer foodCost;
    private Integer schoolFees;
    private Integer savingAmount;
    private Byte haveAnAccount;
    private String accountOrganisation;
    private Byte everTakenALoan;
    private String loanOrganisation;
    private Byte otherAccessToCredit;
    private String otherCreditOrganisation;
    private Byte anyCurrentLoan;
    private Integer outstandingLoanAmount;
    private String currentLoanOrganisation;
    private Integer dailyCustomers;
    private Byte employed;
    private String educationLevelId;
    private byte isCompleted;
    private Integer affordability;
    private byte to_create;
    private byte to_update;
    private String creatorId;
    private Timestamp creationDate;
    private Timestamp updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(byte isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getCustomersId() {
        return customersId;
    }

    public void setCustomersId(String customersId) {
        this.customersId = customersId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getProductTradingStartDate() {
        return productTradingStartDate;
    }

    public void setProductTradingStartDate(String productTradingStartDate) {
        this.productTradingStartDate = productTradingStartDate;
    }

    public byte getIsReset() {
        return isReset;
    }

    public void setIsReset(byte isReset) {
        this.isReset = isReset;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Byte getStockNeat() {
        return stockNeat;
    }

    public void setStockNeat(Byte stockNeat) {
        this.stockNeat = stockNeat;
    }

    public Byte getAccurateLedgerBook() {
        return accurateLedgerBook;
    }

    public void setAccurateLedgerBook(Byte accurateLedgerBook) {
        this.accurateLedgerBook = accurateLedgerBook;
    }

    public Byte getSalesActivity() {
        return salesActivity;
    }

    public void setSalesActivity(Byte salesActivity) {
        this.salesActivity = salesActivity;
    }

    public Byte getPermanentOperation() {
        return permanentOperation;
    }

    public void setPermanentOperation(Byte permanentOperation) {
        this.permanentOperation = permanentOperation;
    }

    public Byte getProofOfOwnership() {
        return proofOfOwnership;
    }

    public void setProofOfOwnership(Byte proofOfOwnership) {
        this.proofOfOwnership = proofOfOwnership;
    }

    public Byte getForthcomingAndTransparent() {
        return forthcomingAndTransparent;
    }

    public void setForthcomingAndTransparent(Byte forthcomingAndTransparent) {
        this.forthcomingAndTransparent = forthcomingAndTransparent;
    }

    public Byte getKnownToMarketAuthorities() {
        return knownToMarketAuthorities;
    }

    public void setKnownToMarketAuthorities(Byte knownToMarketAuthorities) {
        this.knownToMarketAuthorities = knownToMarketAuthorities;
    }

    public Byte getSoundReputation() {
        return soundReputation;
    }

    public void setSoundReputation(Byte soundReputation) {
        this.soundReputation = soundReputation;
    }

    public Byte getWouldILend() {
        return wouldILend;
    }

    public void setWouldILend(Byte wouldILend) {
        this.wouldILend = wouldILend;
    }

    public Integer getLendAmount() {
        return lendAmount;
    }

    public void setLendAmount(Integer lendAmount) {
        this.lendAmount = lendAmount;
    }

    public Integer getBusinessRent() {
        return businessRent;
    }

    public void setBusinessRent(Integer businessRent) {
        this.businessRent = businessRent;
    }

    public Integer getBusinessUtilities() {
        return businessUtilities;
    }

    public void setBusinessUtilities(Integer businessUtilities) {
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

    public Integer getEmployeesSalary() {
        return employeesSalary;
    }

    public void setEmployeesSalary(Integer employeesSalary) {
        this.employeesSalary = employeesSalary;
    }

    public Integer getLicensingFee() {
        return licensingFee;
    }

    public void setLicensingFee(Integer licensingFee) {
        this.licensingFee = licensingFee;
    }

    public Integer getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(Integer transportFee) {
        this.transportFee = transportFee;
    }

    public Integer getStorageFee() {
        return storageFee;
    }

    public void setStorageFee(Integer storageFee) {
        this.storageFee = storageFee;
    }

    public String getBusinessCycleId() {
        return businessCycleId;
    }

    public void setBusinessCycleId(String businessCycleId) {
        this.businessCycleId = businessCycleId;
    }

    public Integer getCurrentStockValue() {
        return currentStockValue;
    }

    public void setCurrentStockValue(Integer currentStockValue) {
        this.currentStockValue = currentStockValue;
    }

    public Integer getSalesPerCycle() {
        return salesPerCycle;
    }

    public void setSalesPerCycle(Integer salesPerCycle) {
        this.salesPerCycle = salesPerCycle;
    }

    public Integer getSpendOnStock() {
        return spendOnStock;
    }

    public void setSpendOnStock(Integer spendOnStock) {
        this.spendOnStock = spendOnStock;
    }

    public String getIncomeExplanation() {
        return incomeExplanation;
    }

    public void setIncomeExplanation(String incomeExplanation) {
        this.incomeExplanation = incomeExplanation;
    }

    public Integer getCycleRestockingFrequency() {
        return cycleRestockingFrequency;
    }

    public void setCycleRestockingFrequency(Integer cycleRestockingFrequency) {
        this.cycleRestockingFrequency = cycleRestockingFrequency;
    }

    public String getBusinessLocationTypesId() {
        return businessLocationTypesId;
    }

    public void setBusinessLocationTypesId(String businessLocationTypesId) {
        this.businessLocationTypesId = businessLocationTypesId;
    }

    public String getLocationTradingStartDate() {
        return locationTradingStartDate;
    }

    public void setLocationTradingStartDate(String locationTradingStartDate) {
        this.locationTradingStartDate = locationTradingStartDate;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
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

    public Byte getIn_a_chama() {
        return in_a_chama;
    }

    public void setIn_a_chama(Byte in_a_chama) {
        this.in_a_chama = in_a_chama;
    }

    public String getChamaCycleId() {
        return chamaCycleId;
    }

    public void setChamaCycleId(String chamaCycleId) {
        this.chamaCycleId = chamaCycleId;
    }

    public Integer getChamaContribution() {
        return chamaContribution;
    }

    public void setChamaContribution(Integer chamaContribution) {
        this.chamaContribution = chamaContribution;
    }

    public Integer getNoOfChamaMembers() {
        return noOfChamaMembers;
    }

    public void setNoOfChamaMembers(Integer noOfChamaMembers) {
        this.noOfChamaMembers = noOfChamaMembers;
    }

    public Integer getChamaPayoutFrequency() {
        return chamaPayoutFrequency;
    }

    public void setChamaPayoutFrequency(Integer chamaPayoutFrequency) {
        this.chamaPayoutFrequency = chamaPayoutFrequency;
    }

    public Integer getChamaPayoutAmount() {
        return chamaPayoutAmount;
    }

    public void setChamaPayoutAmount(Integer chamaPayoutAmount) {
        this.chamaPayoutAmount = chamaPayoutAmount;
    }

    public Byte getChamaHasRecords() {
        return chamaHasRecords;
    }

    public void setChamaHasRecords(Byte chamaHasRecords) {
        this.chamaHasRecords = chamaHasRecords;
    }

    public String getHouseTypeId() {
        return houseTypeId;
    }

    public void setHouseTypeId(String houseTypeId) {
        this.houseTypeId = houseTypeId;
    }

    public Integer getHomeRent() {
        return homeRent;
    }

    public void setHomeRent(Integer homeRent) {
        this.homeRent = homeRent;
    }

    public Integer getHouseUtilities() {
        return houseUtilities;
    }

    public void setHouseUtilities(Integer houseUtilities) {
        this.houseUtilities = houseUtilities;
    }

    public Integer getFoodCost() {
        return foodCost;
    }

    public void setFoodCost(Integer foodCost) {
        this.foodCost = foodCost;
    }

    public Integer getSchoolFees() {
        return schoolFees;
    }

    public void setSchoolFees(Integer schoolFees) {
        this.schoolFees = schoolFees;
    }

    public Integer getSavingAmount() {
        return savingAmount;
    }

    public void setSavingAmount(Integer savingAmount) {
        this.savingAmount = savingAmount;
    }

    public Byte getHaveAnAccount() {
        return haveAnAccount;
    }

    public void setHaveAnAccount(Byte haveAnAccount) {
        this.haveAnAccount = haveAnAccount;
    }

    public String getAccountOrganisation() {
        return accountOrganisation;
    }

    public void setAccountOrganisation(String accountOrganisation) {
        this.accountOrganisation = accountOrganisation;
    }

    public Byte getEverTakenALoan() {
        return everTakenALoan;
    }

    public void setEverTakenALoan(Byte everTakenALoan) {
        this.everTakenALoan = everTakenALoan;
    }

    public String getLoanOrganisation() {
        return loanOrganisation;
    }

    public void setLoanOrganisation(String loanOrganisation) {
        this.loanOrganisation = loanOrganisation;
    }

    public Byte getOtherAccessToCredit() {
        return otherAccessToCredit;
    }

    public void setOtherAccessToCredit(Byte otherAccessToCredit) {
        this.otherAccessToCredit = otherAccessToCredit;
    }

    public String getOtherCreditOrganisation() {
        return otherCreditOrganisation;
    }

    public void setOtherCreditOrganisation(String otherCreditOrganisation) {
        this.otherCreditOrganisation = otherCreditOrganisation;
    }

    public Byte getAnyCurrentLoan() {
        return anyCurrentLoan;
    }

    public void setAnyCurrentLoan(Byte anyCurrentLoan) {
        this.anyCurrentLoan = anyCurrentLoan;
    }

    public Integer getOutstandingLoanAmount() {
        return outstandingLoanAmount;
    }

    public void setOutstandingLoanAmount(Integer outstandingLoanAmount) {
        this.outstandingLoanAmount = outstandingLoanAmount;
    }

    public String getCurrentLoanOrganisation() {
        return currentLoanOrganisation;
    }

    public void setCurrentLoanOrganisation(String currentLoanOrganisation) {
        this.currentLoanOrganisation = currentLoanOrganisation;
    }

    public Integer getDailyCustomers() {
        return dailyCustomers;
    }

    public void setDailyCustomers(Integer dailyCustomers) {
        this.dailyCustomers = dailyCustomers;
    }

    public Byte getEmployed() {
        return employed;
    }

    public void setEmployed(Byte employed) {
        this.employed = employed;
    }

    public String getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(String educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public Integer getAffordability() {
        return affordability;
    }

    public void setAffordability(Integer affordability) {
        this.affordability = affordability;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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
