package ke.merlin.dao.sitevisit;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import ke.merlin.models.customers.Customers;
import ke.merlin.models.customers.StRefereesRelationship;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.database.DatabaseManager;

/**
 * Created by mecmurimi on 24/09/2017.
 */

public class SiteVisitDao {
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
    private SiteVisit siteVisit;

    public SiteVisitDao() {
        siteVisit = new SiteVisit();
    }

    /**
     * @return
     */
    public static String createSiteVisitTable() {
        return "CREATE TABLE 'site_visit' (\n" +
                "  'id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'customers_id' varchar(150) NOT NULL ,\n" +
                "  'business_category_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'business_type_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'product_trading_start_date' date,\n" +
                "  'is_reset' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'photo_path' text,\n" +
                "  'stock_neat' tinyint(11) NOT NULL DEFAULT '0',\n" +
                "  'accurate_ledger_book' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'sales_activity' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'permanent_operation' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'proof_of_ownership' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'forthcoming_and_transparent' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'known_to_market_authorities' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'sound_reputation' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'would_I_lend' tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  'lend_amount' int(11) DEFAULT '0',\n" +
                "  'business_rent' int(11) DEFAULT '0',\n" +
                "  'business_utilities' int(11) DEFAULT '0',\n" +
                "  'no_of_employees_types_id' varchar(150) DEFAULT NULL,\n" +
                "  'nature_of_employment_types_id' varchar(150) DEFAULT NULL,\n" +
                "  'employees_salary' int(11) DEFAULT '0',\n" +
                "  'licensing_fee' int(11) DEFAULT '0',\n" +
                "  'transport_fee' int(11) DEFAULT '0',\n" +
                "  'storage_fee' int(11) DEFAULT '0',\n" +
                "  'business_cycle_id' varchar(150) DEFAULT NULL,\n" +
                "  'current_stock_value' int(11) DEFAULT '0',\n" +
                "  'sales_per_cycle' int(11) DEFAULT NULL,\n" +
                "  'spend_on_stock' int(11) DEFAULT '0',\n" +
                "  'income_explanation' text,\n" +
                "  'cycle_restocking_frequency' int(11) DEFAULT NULL,\n" +
                "  'business_location_types_id' varchar(150) DEFAULT NULL,\n" +
                "  'location_trading_start_date' date DEFAULT NULL,\n" +
                "  'business_address' text,\n" +
                "  'longitudes' double DEFAULT NULL,\n" +
                "  'latitudes' double DEFAULT NULL,\n" +
                "  'in_a_chama' tinyint(1) DEFAULT 0,\n" +
                "  'chama_cycle_id' varchar(150) DEFAULT NULL,\n" +
                "  'chama_contribution' int(11) DEFAULT NULL,\n" +
                "  'no_of_chama_members' int(11) DEFAULT NULL,\n" +
                "  'chama_payout_frequency' int(11) DEFAULT NULL,\n" +
                "  'chama_payout_amount' int(11) DEFAULT NULL,\n" +
                "  'chama_has_records' tinyint(1) DEFAULT NULL,\n" +
                "  'house_type_id' varchar(150) DEFAULT NULL,\n" +
                "  'home_rent' int(11) DEFAULT NULL,\n" +
                "  'house_utilities' int(11) DEFAULT NULL,\n" +
                "  'food_cost' int(11) DEFAULT NULL,\n" +
                "  'school_fees' int(11) DEFAULT NULL,\n" +
                "  'saving_amount' int(11) DEFAULT NULL,\n" +
                "  'have_an_account' tinyint(1) DEFAULT NULL,\n" +
                "  'account_organisation' varchar(150) DEFAULT NULL,\n" +
                "  'ever_taken_a_loan' tinyint(1) DEFAULT NULL,\n" +
                "  'loan_organisation' varchar(150) DEFAULT NULL,\n" +
                "  'other_access_to_credit' tinyint(1) DEFAULT NULL,\n" +
                "  'other_credit_organisation' varchar(150) DEFAULT NULL,\n" +
                "  'any_current_loan' tinyint(1) DEFAULT NULL,\n" +
                "  'outstanding_loan_amount' int(11) DEFAULT NULL,\n" +
                "  'current_loan_organisation' varchar(150) DEFAULT NULL,\n" +
                "  'daily_customers' int(11) DEFAULT NULL,\n" +
                "  'employed' tinyint(1) DEFAULT NULL,\n" +
                "  'to_create' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'to_update' tinyint(0) NOT NULL DEFAULT '0',\n" +
                "  'education_level_id' varchar(150) DEFAULT NULL,\n" +
                "  'is_completed' tinyint(0) NOT NULL DEFAULT '1',\n" +
                "  'affordability' int(11) DEFAULT '0',\n" +
                "  'creator_id' varchar(150) NOT NULL DEFAULT '',\n" +
                "  'creation_date' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  'updated_date' datetime NOT NULL ,\n" +
                "  PRIMARY KEY ('id')\n" +
                "  CONSTRAINT 'fk_acnt_org_stv' FOREIGN KEY ('account_organisation') REFERENCES 'st_credit_organisations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_bus_category_stv' FOREIGN KEY ('business_category_id') REFERENCES 'st_business_categories' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_bus_location_stv' FOREIGN KEY ('business_location_types_id') REFERENCES 'st_business_locations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_bus_type_atv' FOREIGN KEY ('business_type_id') REFERENCES 'st_business_types' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_business_cycle_stv' FOREIGN KEY ('business_cycle_id') REFERENCES 'st_business_cycles' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_chama_cycle_stv' FOREIGN KEY ('chama_cycle_id') REFERENCES 'st_chama_cycles' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_cur_loan_org_stv' FOREIGN KEY ('current_loan_organisation') REFERENCES 'st_credit_organisations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_customers_id_sv' FOREIGN KEY ('customers_id') REFERENCES 'customers' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_educ_lev_stv' FOREIGN KEY ('education_level_id') REFERENCES 'st_education_levels' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_house_types_stv' FOREIGN KEY ('house_type_id') REFERENCES 'st_house_types' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_loan_org_stv' FOREIGN KEY ('loan_organisation') REFERENCES 'st_credit_organisations' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_nature_of_employment_stv' FOREIGN KEY ('nature_of_employment_types_id') REFERENCES 'st_nature_of_employment' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_no_of_employee_stv' FOREIGN KEY ('no_of_employees_types_id') REFERENCES 'st_no_of_employees_range' ('id') ON UPDATE CASCADE,\n" +
                "  CONSTRAINT 'fk_other_cred_org' FOREIGN KEY ('other_credit_organisation') REFERENCES 'st_credit_organisations' ('id') ON UPDATE CASCADE\n" +
                ")";
    }

    public void insert(SiteVisit siteVisit) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("id", siteVisit.getId());
        values.put("customers_id", siteVisit.getCustomersId());

        if (siteVisit.getBusinessCategoryId() != null)
            values.put("business_category_id", siteVisit.getBusinessCategoryId());

        if (siteVisit.getBusinessTypeId() != null)
            values.put("business_type_id", siteVisit.getBusinessTypeId());

        if (siteVisit.getProductTradingStartDate() != null)
            values.put("product_trading_start_date", siteVisit.getProductTradingStartDate());

        if (siteVisit.getPhotoPath() != null)
            values.put("photo_path", siteVisit.getPhotoPath());

        if (siteVisit.getStockNeat() != null)
            values.put("stock_neat", siteVisit.getStockNeat());

        if (siteVisit.getAccurateLedgerBook() != null)
            values.put("accurate_ledger_book", siteVisit.getAccurateLedgerBook());

        if (siteVisit.getSalesActivity() != null)
            values.put("sales_activity", siteVisit.getSalesActivity());

        if (siteVisit.getPermanentOperation() != null)
            values.put("permanent_operation", siteVisit.getPermanentOperation());

        if (siteVisit.getProofOfOwnership() != null)
            values.put("proof_of_ownership", siteVisit.getProofOfOwnership());

        if (siteVisit.getForthcomingAndTransparent() != null)
            values.put("forthcoming_and_transparent", siteVisit.getForthcomingAndTransparent());

        if (siteVisit.getKnownToMarketAuthorities() != null)
            values.put("known_to_market_authorities", siteVisit.getKnownToMarketAuthorities());

        if (siteVisit.getSoundReputation() != null)
            values.put("sound_reputation", siteVisit.getSoundReputation());

        if (siteVisit.getWouldILend() != null)
            values.put("would_I_lend", siteVisit.getWouldILend());

        if (siteVisit.getLendAmount() != null)
            values.put("lend_amount", siteVisit.getLendAmount());

        if (siteVisit.getBusinessRent() != null)
            values.put("business_rent", siteVisit.getBusinessRent());

        if (siteVisit.getBusinessUtilities() != null)
            values.put("business_utilities", siteVisit.getBusinessUtilities());

        if (siteVisit.getNoOfEmployeesTypesId() != null)
            values.put("no_of_employees_types_id", siteVisit.getNoOfEmployeesTypesId());

        if (siteVisit.getNatureOfEmploymentTypesId() != null)
            values.put("nature_of_employment_types_id", siteVisit.getNatureOfEmploymentTypesId());

        if (siteVisit.getEmployeesSalary() != null)
            values.put("employees_salary", siteVisit.getEmployeesSalary());

        if (siteVisit.getLicensingFee() != null)
            values.put("licensing_fee", siteVisit.getLicensingFee());

        if (siteVisit.getTransportFee() != null)
            values.put("transport_fee", siteVisit.getTransportFee());

        if (siteVisit.getStorageFee() != null)
            values.put("storage_fee", siteVisit.getStorageFee());

        if (siteVisit.getBusinessCycleId() != null)
            values.put("business_cycle_id", siteVisit.getBusinessCycleId());

        if (siteVisit.getCurrentStockValue() != null)
            values.put("current_stock_value", siteVisit.getCurrentStockValue());

        if (siteVisit.getSalesPerCycle() != null)
            values.put("sales_per_cycle", siteVisit.getSalesPerCycle());

        if (siteVisit.getSpendOnStock() != null)
            values.put("spend_on_stock", siteVisit.getSpendOnStock());

        if (siteVisit.getIncomeExplanation() != null)
            values.put("income_explanation", siteVisit.getIncomeExplanation());

        if (siteVisit.getCycleRestockingFrequency() != null)
            values.put("cycle_restocking_frequency", siteVisit.getCycleRestockingFrequency());

        if (siteVisit.getBusinessLocationTypesId() != null)
            values.put("business_location_types_id", siteVisit.getBusinessLocationTypesId());

        if (siteVisit.getLocationTradingStartDate() != null)
            values.put("location_trading_start_date", siteVisit.getLocationTradingStartDate());

        if (siteVisit.getBusinessAddress() != null)
            values.put("business_address", siteVisit.getBusinessAddress());

        if (siteVisit.getLongitudes() != null)
            values.put("longitudes", siteVisit.getLongitudes());

        if (siteVisit.getLatitudes() != null)
            values.put("latitudes", siteVisit.getLatitudes());

        if (siteVisit.getIn_a_chama() != null)
            values.put("in_a_chama", siteVisit.getIn_a_chama());

        if (siteVisit.getChamaCycleId() != null)
            values.put("chama_cycle_id", siteVisit.getChamaCycleId());

        if (siteVisit.getChamaContribution() != null)
            values.put("chama_contribution", siteVisit.getChamaContribution());

        if (siteVisit.getNoOfChamaMembers() != null)
            values.put("no_of_chama_members", siteVisit.getNoOfChamaMembers());

        if (siteVisit.getChamaPayoutFrequency() != null)
            values.put("chama_payout_frequency", siteVisit.getChamaPayoutFrequency());

        if (siteVisit.getChamaPayoutAmount() != null)
            values.put("chama_payout_amount", siteVisit.getChamaPayoutAmount());

        if (siteVisit.getChamaHasRecords() != null)
            values.put("chama_has_records", siteVisit.getChamaHasRecords());

        if (siteVisit.getHouseTypeId() != null)
            values.put("house_type_id", siteVisit.getHouseTypeId());

        if (siteVisit.getHomeRent() != null)
            values.put("home_rent", siteVisit.getHomeRent());

        if (siteVisit.getHouseUtilities() != null)
            values.put("house_utilities", siteVisit.getHouseUtilities());

        if (siteVisit.getFoodCost() != null)
            values.put("food_cost", siteVisit.getFoodCost());

        if (siteVisit.getSchoolFees() != null)
            values.put("school_fees", siteVisit.getSchoolFees());

        if (siteVisit.getSavingAmount() != null)
            values.put("saving_amount", siteVisit.getSavingAmount());

        if (siteVisit.getHaveAnAccount() != null)
            values.put("have_an_account", siteVisit.getHaveAnAccount());

        if (siteVisit.getAccountOrganisation() != null)
            values.put("account_organisation", siteVisit.getAccountOrganisation());

        if (siteVisit.getEverTakenALoan() != null)
            values.put("ever_taken_a_loan", siteVisit.getEverTakenALoan());

        if (siteVisit.getLoanOrganisation() != null)
            values.put("loan_organisation", siteVisit.getLoanOrganisation());

        if (siteVisit.getOtherAccessToCredit() != null)
            values.put("other_access_to_credit", siteVisit.getOtherAccessToCredit());

        if (siteVisit.getOtherCreditOrganisation() != null)
            values.put("other_credit_organisation", siteVisit.getOtherCreditOrganisation());

        if (siteVisit.getAnyCurrentLoan() != null)
            values.put("any_current_loan", siteVisit.getAnyCurrentLoan());

        if (siteVisit.getOutstandingLoanAmount() != null)
            values.put("outstanding_loan_amount", siteVisit.getOutstandingLoanAmount());

        if (siteVisit.getCurrentLoanOrganisation() != null)
            values.put("current_loan_organisation", siteVisit.getCurrentLoanOrganisation());

        if (siteVisit.getDailyCustomers() != null)
            values.put("daily_customers", siteVisit.getDailyCustomers());

        if (siteVisit.getEmployed() != null)
            values.put("employed", siteVisit.getEmployed());

        if (siteVisit.getEducationLevelId() != null)
            values.put("education_level_id", siteVisit.getEducationLevelId());

        values.put("is_completed", siteVisit.getIsCompleted());

        values.put("to_create", siteVisit.getTo_create());

        values.put("to_update", siteVisit.getTo_update());

        values.put("affordability", siteVisit.getAffordability());

        values.put("creator_id", siteVisit.getCreatorId());
        values.put("creation_date", String.valueOf(siteVisit.getCreationDate()));
        values.put("updated_date", String.valueOf(siteVisit.getUpdatedDate()));

        // Inserting Row
        db.insertWithOnConflict("site_visit", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insertList(String response) {
        long cnt = 0;

        List<SiteVisit> list = gson.fromJson(response, new TypeToken<List<SiteVisit>>() {
        }.getType());
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (SiteVisit siteVisit : list) {

                ContentValues values = new ContentValues();
                values.put("id", siteVisit.getId());
                values.put("customers_id", siteVisit.getCustomersId());

                if (siteVisit.getBusinessCategoryId() != null)
                    values.put("business_category_id", siteVisit.getBusinessCategoryId());

                if (siteVisit.getBusinessTypeId() != null)
                    values.put("business_type_id", siteVisit.getBusinessTypeId());

                if (siteVisit.getProductTradingStartDate() != null)
                    values.put("product_trading_start_date", siteVisit.getProductTradingStartDate());

                if (siteVisit.getPhotoPath() != null)
                    values.put("photo_path", siteVisit.getPhotoPath());

                if (siteVisit.getStockNeat() != null)
                    values.put("stock_neat", siteVisit.getStockNeat());

                if (siteVisit.getAccurateLedgerBook() != null)
                    values.put("accurate_ledger_book", siteVisit.getAccurateLedgerBook());

                if (siteVisit.getSalesActivity() != null)
                    values.put("sales_activity", siteVisit.getSalesActivity());

                if (siteVisit.getPermanentOperation() != null)
                    values.put("permanent_operation", siteVisit.getPermanentOperation());

                if (siteVisit.getProofOfOwnership() != null)
                    values.put("proof_of_ownership", siteVisit.getProofOfOwnership());

                if (siteVisit.getForthcomingAndTransparent() != null)
                    values.put("forthcoming_and_transparent", siteVisit.getForthcomingAndTransparent());

                if (siteVisit.getKnownToMarketAuthorities() != null)
                    values.put("known_to_market_authorities", siteVisit.getKnownToMarketAuthorities());

                if (siteVisit.getSoundReputation() != null)
                    values.put("sound_reputation", siteVisit.getSoundReputation());

                if (siteVisit.getWouldILend() != null)
                    values.put("would_I_lend", siteVisit.getWouldILend());

                if (siteVisit.getLendAmount() != null)
                    values.put("lend_amount", siteVisit.getLendAmount());

                if (siteVisit.getBusinessRent() != null)
                    values.put("business_rent", siteVisit.getBusinessRent());

                if (siteVisit.getBusinessUtilities() != null)
                    values.put("business_utilities", siteVisit.getBusinessUtilities());

                if (siteVisit.getNoOfEmployeesTypesId() != null)
                    values.put("no_of_employees_types_id", siteVisit.getNoOfEmployeesTypesId());

                if (siteVisit.getNatureOfEmploymentTypesId() != null)
                    values.put("nature_of_employment_types_id", siteVisit.getNatureOfEmploymentTypesId());

                if (siteVisit.getEmployeesSalary() != null)
                    values.put("employees_salary", siteVisit.getEmployeesSalary());

                if (siteVisit.getLicensingFee() != null)
                    values.put("licensing_fee", siteVisit.getLicensingFee());

                if (siteVisit.getTransportFee() != null)
                    values.put("transport_fee", siteVisit.getTransportFee());

                if (siteVisit.getStorageFee() != null)
                    values.put("storage_fee", siteVisit.getStorageFee());

                if (siteVisit.getBusinessCycleId() != null)
                    values.put("business_cycle_id", siteVisit.getBusinessCycleId());

                if (siteVisit.getCurrentStockValue() != null)
                    values.put("current_stock_value", siteVisit.getCurrentStockValue());

                if (siteVisit.getSalesPerCycle() != null)
                    values.put("sales_per_cycle", siteVisit.getSalesPerCycle());

                if (siteVisit.getSpendOnStock() != null)
                    values.put("spend_on_stock", siteVisit.getSpendOnStock());

                if (siteVisit.getIncomeExplanation() != null)
                    values.put("income_explanation", siteVisit.getIncomeExplanation());

                if (siteVisit.getCycleRestockingFrequency() != null)
                    values.put("cycle_restocking_frequency", siteVisit.getCycleRestockingFrequency());

                if (siteVisit.getBusinessLocationTypesId() != null)
                    values.put("business_location_types_id", siteVisit.getBusinessLocationTypesId());

                if (siteVisit.getLocationTradingStartDate() != null)
                    values.put("location_trading_start_date", siteVisit.getLocationTradingStartDate());

                if (siteVisit.getBusinessAddress() != null)
                    values.put("business_address", siteVisit.getBusinessAddress());

                if (siteVisit.getLongitudes() != null)
                    values.put("longitudes", siteVisit.getLongitudes());

                if (siteVisit.getLatitudes() != null)
                    values.put("latitudes", siteVisit.getLatitudes());

                if (siteVisit.getIn_a_chama() != null)
                    values.put("in_a_chama", siteVisit.getIn_a_chama());

                if (siteVisit.getChamaCycleId() != null)
                    values.put("chama_cycle_id", siteVisit.getChamaCycleId());

                if (siteVisit.getChamaContribution() != null)
                    values.put("chama_contribution", siteVisit.getChamaContribution());

                if (siteVisit.getNoOfChamaMembers() != null)
                    values.put("no_of_chama_members", siteVisit.getNoOfChamaMembers());

                if (siteVisit.getChamaPayoutFrequency() != null)
                    values.put("chama_payout_frequency", siteVisit.getChamaPayoutFrequency());

                if (siteVisit.getChamaPayoutAmount() != null)
                    values.put("chama_payout_amount", siteVisit.getChamaPayoutAmount());

                if (siteVisit.getChamaHasRecords() != null)
                    values.put("chama_has_records", siteVisit.getChamaHasRecords());

                if (siteVisit.getHouseTypeId() != null)
                    values.put("house_type_id", siteVisit.getHouseTypeId());

                if (siteVisit.getHomeRent() != null)
                    values.put("home_rent", siteVisit.getHomeRent());

                if (siteVisit.getHouseUtilities() != null)
                    values.put("house_utilities", siteVisit.getHouseUtilities());

                if (siteVisit.getFoodCost() != null)
                    values.put("food_cost", siteVisit.getFoodCost());

                if (siteVisit.getSchoolFees() != null)
                    values.put("school_fees", siteVisit.getSchoolFees());

                if (siteVisit.getSavingAmount() != null)
                    values.put("saving_amount", siteVisit.getSavingAmount());

                if (siteVisit.getHaveAnAccount() != null)
                    values.put("have_an_account", siteVisit.getHaveAnAccount());

                if (siteVisit.getAccountOrganisation() != null)
                    values.put("account_organisation", siteVisit.getAccountOrganisation());

                if (siteVisit.getEverTakenALoan() != null)
                    values.put("ever_taken_a_loan", siteVisit.getEverTakenALoan());

                if (siteVisit.getLoanOrganisation() != null)
                    values.put("loan_organisation", siteVisit.getLoanOrganisation());

                if (siteVisit.getOtherAccessToCredit() != null)
                    values.put("other_access_to_credit", siteVisit.getOtherAccessToCredit());

                if (siteVisit.getOtherCreditOrganisation() != null)
                    values.put("other_credit_organisation", siteVisit.getOtherCreditOrganisation());

                if (siteVisit.getAnyCurrentLoan() != null)
                    values.put("any_current_loan", siteVisit.getAnyCurrentLoan());

                if (siteVisit.getOutstandingLoanAmount() != null)
                    values.put("outstanding_loan_amount", siteVisit.getOutstandingLoanAmount());

                if (siteVisit.getCurrentLoanOrganisation() != null)
                    values.put("current_loan_organisation", siteVisit.getCurrentLoanOrganisation());

                if (siteVisit.getDailyCustomers() != null)
                    values.put("daily_customers", siteVisit.getDailyCustomers());

                if (siteVisit.getEmployed() != null)
                    values.put("employed", siteVisit.getEmployed());

                if (siteVisit.getEducationLevelId() != null)
                    values.put("education_level_id", siteVisit.getEducationLevelId());

                values.put("is_completed", siteVisit.getIsCompleted());

                values.put("affordability", siteVisit.getAffordability());

                values.put("to_create", siteVisit.getTo_create());

                values.put("to_update", siteVisit.getTo_update());

                values.put("creator_id", siteVisit.getCreatorId());
                values.put("creation_date", String.valueOf(siteVisit.getCreationDate()));
                values.put("updated_date", String.valueOf(siteVisit.getUpdatedDate()));

                cnt = db.insertWithOnConflict("site_visit", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }

        return cnt;
    }

    public void update(SiteVisit siteVisit) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();

        if (siteVisit.getBusinessCategoryId() != null)
            values.put("business_category_id", siteVisit.getBusinessCategoryId());

        if (siteVisit.getBusinessTypeId() != null)
            values.put("business_type_id", siteVisit.getBusinessTypeId());

        if (siteVisit.getProductTradingStartDate() != null)
            values.put("product_trading_start_date", siteVisit.getProductTradingStartDate());

        if (siteVisit.getPhotoPath() != null)
            values.put("photo_path", siteVisit.getPhotoPath());

        if (siteVisit.getStockNeat() != null)
            values.put("stock_neat", siteVisit.getStockNeat());

        if (siteVisit.getAccurateLedgerBook() != null)
            values.put("accurate_ledger_book", siteVisit.getAccurateLedgerBook());

        if (siteVisit.getSalesActivity() != null)
            values.put("sales_activity", siteVisit.getSalesActivity());

        if (siteVisit.getPermanentOperation() != null)
            values.put("permanent_operation", siteVisit.getPermanentOperation());

        if (siteVisit.getProofOfOwnership() != null)
            values.put("proof_of_ownership", siteVisit.getProofOfOwnership());

        if (siteVisit.getForthcomingAndTransparent() != null)
            values.put("forthcoming_and_transparent", siteVisit.getForthcomingAndTransparent());

        if (siteVisit.getKnownToMarketAuthorities() != null)
            values.put("known_to_market_authorities", siteVisit.getKnownToMarketAuthorities());

        if (siteVisit.getSoundReputation() != null)
            values.put("sound_reputation", siteVisit.getSoundReputation());

        if (siteVisit.getWouldILend() != null)
            values.put("would_I_lend", siteVisit.getWouldILend());

        if (siteVisit.getLendAmount() != null)
            values.put("lend_amount", siteVisit.getLendAmount());

        if (siteVisit.getBusinessRent() != null)
            values.put("business_rent", siteVisit.getBusinessRent());

        if (siteVisit.getBusinessUtilities() != null)
            values.put("business_utilities", siteVisit.getBusinessUtilities());

        if (siteVisit.getNoOfEmployeesTypesId() != null)
            values.put("no_of_employees_types_id", siteVisit.getNoOfEmployeesTypesId());

        if (siteVisit.getNatureOfEmploymentTypesId() != null)
            values.put("nature_of_employment_types_id", siteVisit.getNatureOfEmploymentTypesId());

        if (siteVisit.getEmployeesSalary() != null)
            values.put("employees_salary", siteVisit.getEmployeesSalary());

        if (siteVisit.getLicensingFee() != null)
            values.put("licensing_fee", siteVisit.getLicensingFee());

        if (siteVisit.getTransportFee() != null)
            values.put("transport_fee", siteVisit.getTransportFee());

        if (siteVisit.getStorageFee() != null)
            values.put("storage_fee", siteVisit.getStorageFee());

        if (siteVisit.getBusinessCycleId() != null)
            values.put("business_cycle_id", siteVisit.getBusinessCycleId());

        if (siteVisit.getCurrentStockValue() != null)
            values.put("current_stock_value", siteVisit.getCurrentStockValue());

        if (siteVisit.getSalesPerCycle() != null)
            values.put("sales_per_cycle", siteVisit.getSalesPerCycle());

        if (siteVisit.getSpendOnStock() != null)
            values.put("spend_on_stock", siteVisit.getSpendOnStock());

        if (siteVisit.getIncomeExplanation() != null)
            values.put("income_explanation", siteVisit.getIncomeExplanation());

        if (siteVisit.getCycleRestockingFrequency() != null)
            values.put("cycle_restocking_frequency", siteVisit.getCycleRestockingFrequency());

        if (siteVisit.getBusinessLocationTypesId() != null)
            values.put("business_location_types_id", siteVisit.getBusinessLocationTypesId());

        if (siteVisit.getLocationTradingStartDate() != null)
            values.put("location_trading_start_date", siteVisit.getLocationTradingStartDate());

        if (siteVisit.getBusinessAddress() != null)
            values.put("business_address", siteVisit.getBusinessAddress());

        if (siteVisit.getLongitudes() != null)
            values.put("longitudes", siteVisit.getLongitudes());

        if (siteVisit.getLatitudes() != null)
            values.put("latitudes", siteVisit.getLatitudes());

        if (siteVisit.getIn_a_chama() != null)
            values.put("in_a_chama", siteVisit.getIn_a_chama());

        if (siteVisit.getChamaCycleId() != null)
            values.put("chama_cycle_id", siteVisit.getChamaCycleId());

        if (siteVisit.getChamaContribution() != null)
            values.put("chama_contribution", siteVisit.getChamaContribution());

        if (siteVisit.getNoOfChamaMembers() != null)
            values.put("no_of_chama_members", siteVisit.getNoOfChamaMembers());

        if (siteVisit.getChamaPayoutFrequency() != null)
            values.put("chama_payout_frequency", siteVisit.getChamaPayoutFrequency());

        if (siteVisit.getChamaPayoutAmount() != null)
            values.put("chama_payout_amount", siteVisit.getChamaPayoutAmount());

        if (siteVisit.getChamaHasRecords() != null)
            values.put("chama_has_records", siteVisit.getChamaHasRecords());

        if (siteVisit.getHouseTypeId() != null)
            values.put("house_type_id", siteVisit.getHouseTypeId());

        if (siteVisit.getHomeRent() != null)
            values.put("home_rent", siteVisit.getHomeRent());

        if (siteVisit.getHouseUtilities() != null)
            values.put("house_utilities", siteVisit.getHouseUtilities());

        if (siteVisit.getFoodCost() != null)
            values.put("food_cost", siteVisit.getFoodCost());

        if (siteVisit.getSchoolFees() != null)
            values.put("school_fees", siteVisit.getSchoolFees());

        if (siteVisit.getSavingAmount() != null)
            values.put("saving_amount", siteVisit.getSavingAmount());

        if (siteVisit.getHaveAnAccount() != null)
            values.put("have_an_account", siteVisit.getHaveAnAccount());

        if (siteVisit.getAccountOrganisation() != null)
            values.put("account_organisation", siteVisit.getAccountOrganisation());

        if (siteVisit.getEverTakenALoan() != null)
            values.put("ever_taken_a_loan", siteVisit.getEverTakenALoan());

        if (siteVisit.getLoanOrganisation() != null)
            values.put("loan_organisation", siteVisit.getLoanOrganisation());

        if (siteVisit.getOtherAccessToCredit() != null)
            values.put("other_access_to_credit", siteVisit.getOtherAccessToCredit());

        if (siteVisit.getOtherCreditOrganisation() != null)
            values.put("other_credit_organisation", siteVisit.getOtherCreditOrganisation());

        if (siteVisit.getAnyCurrentLoan() != null)
            values.put("any_current_loan", siteVisit.getAnyCurrentLoan());

        if (siteVisit.getOutstandingLoanAmount() != null)
            values.put("outstanding_loan_amount", siteVisit.getOutstandingLoanAmount());

        if (siteVisit.getCurrentLoanOrganisation() != null)
            values.put("current_loan_organisation", siteVisit.getCurrentLoanOrganisation());

        if (siteVisit.getDailyCustomers() != null)
            values.put("daily_customers", siteVisit.getDailyCustomers());

        if (siteVisit.getEmployed() != null)
            values.put("employed", siteVisit.getEmployed());

        if (siteVisit.getEducationLevelId() != null)
            values.put("education_level_id", siteVisit.getEducationLevelId());

        values.put("is_completed", siteVisit.getIsCompleted());

        values.put("to_create", siteVisit.getTo_create());

        values.put("to_update", siteVisit.getTo_update());

        values.put("affordability", siteVisit.getAffordability());

        if (siteVisit.getUpdatedDate() != null)
            values.put("updated_date", String.valueOf(siteVisit.getUpdatedDate()));

        // Inserting Row
        db.update("site_visit", values, "id = ? ", new String[]{siteVisit.getId()});
        DatabaseManager.getInstance().closeDatabase();
    }

    public SiteVisit getLatestNonCompleteSiteVisit(String customers_id) {
        SiteVisit beans = null;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("site_visit", null, " customers_id = ?", new String[]{customers_id}, null, null, "creation_date desc", "1");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new SiteVisit();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setCustomersId(cursor.getString(cursor.getColumnIndex("customers_id")));
                beans.setBusinessCategoryId(cursor.getString(cursor.getColumnIndex("business_category_id")));
                beans.setBusinessTypeId(cursor.getString(cursor.getColumnIndex("business_type_id")));
                beans.setProductTradingStartDate(cursor.getString(cursor.getColumnIndex("product_trading_start_date")));
                if (!cursor.isNull(cursor.getColumnIndex("is_reset"))) {
                    beans.setIsReset(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_reset"))));
                }
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                if (!cursor.isNull(cursor.getColumnIndex("stock_neat"))) {
                    beans.setStockNeat(Byte.parseByte(cursor.getString(cursor.getColumnIndex("stock_neat"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("accurate_ledger_book"))) {
                    beans.setAccurateLedgerBook(Byte.parseByte(cursor.getString(cursor.getColumnIndex("accurate_ledger_book"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sales_activity"))) {
                    beans.setSalesActivity(Byte.parseByte(cursor.getString(cursor.getColumnIndex("sales_activity"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("permanent_operation"))) {
                    beans.setPermanentOperation(Byte.parseByte(cursor.getString(cursor.getColumnIndex("permanent_operation"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("proof_of_ownership"))) {
                    beans.setProofOfOwnership(Byte.parseByte(cursor.getString(cursor.getColumnIndex("proof_of_ownership"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("forthcoming_and_transparent"))) {
                    beans.setForthcomingAndTransparent(Byte.parseByte(cursor.getString(cursor.getColumnIndex("forthcoming_and_transparent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("known_to_market_authorities"))) {
                    beans.setKnownToMarketAuthorities(Byte.parseByte(cursor.getString(cursor.getColumnIndex("known_to_market_authorities"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sound_reputation"))) {
                    beans.setSoundReputation(Byte.parseByte(cursor.getString(cursor.getColumnIndex("sound_reputation"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("would_I_lend"))) {
                    beans.setWouldILend(Byte.parseByte(cursor.getString(cursor.getColumnIndex("would_I_lend"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("lend_amount"))) {
                    beans.setLendAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("lend_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("business_rent"))) {
                    beans.setBusinessRent(Integer.valueOf(cursor.getString(cursor.getColumnIndex("business_rent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("business_utilities"))) {
                    beans.setBusinessUtilities(Integer.valueOf(cursor.getString(cursor.getColumnIndex("business_utilities"))));
                }
                beans.setNoOfEmployeesTypesId(cursor.getString(cursor.getColumnIndex("no_of_employees_types_id")));
                beans.setNatureOfEmploymentTypesId(cursor.getString(cursor.getColumnIndex("nature_of_employment_types_id")));
                if (!cursor.isNull(cursor.getColumnIndex("employees_salary"))) {
                    beans.setEmployeesSalary(Integer.valueOf(cursor.getString(cursor.getColumnIndex("employees_salary"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("licensing_fee"))) {
                    beans.setLicensingFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("licensing_fee"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("transport_fee"))) {
                    beans.setTransportFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("transport_fee"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("storage_fee"))) {
                    beans.setStorageFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("storage_fee"))));
                }
                beans.setBusinessCycleId(cursor.getString(cursor.getColumnIndex("business_cycle_id")));
                if (!cursor.isNull(cursor.getColumnIndex("current_stock_value"))) {
                    beans.setCurrentStockValue(Integer.valueOf(cursor.getString(cursor.getColumnIndex("current_stock_value"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sales_per_cycle"))) {
                    beans.setSalesPerCycle(Integer.valueOf(cursor.getString(cursor.getColumnIndex("sales_per_cycle"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("spend_on_stock"))) {
                    beans.setSpendOnStock(Integer.valueOf(cursor.getString(cursor.getColumnIndex("spend_on_stock"))));
                }
                beans.setIncomeExplanation(cursor.getString(cursor.getColumnIndex("income_explanation")));
                if (!cursor.isNull(cursor.getColumnIndex("cycle_restocking_frequency"))) {
                    beans.setCycleRestockingFrequency(Integer.valueOf(cursor.getString(cursor.getColumnIndex("cycle_restocking_frequency"))));
                }
                beans.setBusinessLocationTypesId(cursor.getString(cursor.getColumnIndex("business_location_types_id")));
                beans.setLocationTradingStartDate(cursor.getString(cursor.getColumnIndex("location_trading_start_date")));
                beans.setBusinessAddress(cursor.getString(cursor.getColumnIndex("business_address")));
                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("in_a_chama"))) {
                    beans.setIn_a_chama(Byte.valueOf(cursor.getString(cursor.getColumnIndex("in_a_chama"))));
                }
                beans.setChamaCycleId(cursor.getString(cursor.getColumnIndex("chama_cycle_id")));
                if (!cursor.isNull(cursor.getColumnIndex("chama_contribution"))) {
                    beans.setChamaContribution(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_contribution"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("no_of_chama_members"))) {
                    beans.setNoOfChamaMembers(Integer.valueOf(cursor.getString(cursor.getColumnIndex("no_of_chama_members"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_payout_frequency"))) {
                    beans.setChamaPayoutFrequency(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_payout_frequency"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_payout_amount"))) {
                    beans.setChamaPayoutAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_payout_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_has_records"))) {
                    beans.setChamaHasRecords(Byte.valueOf(cursor.getString(cursor.getColumnIndex("chama_has_records"))));
                }
                beans.setHouseTypeId(cursor.getString(cursor.getColumnIndex("house_type_id")));
                if (!cursor.isNull(cursor.getColumnIndex("home_rent"))) {
                    beans.setHomeRent(Integer.valueOf(cursor.getString(cursor.getColumnIndex("home_rent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("house_utilities"))) {
                    beans.setHouseUtilities(Integer.valueOf(cursor.getString(cursor.getColumnIndex("house_utilities"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("food_cost"))) {
                    beans.setFoodCost(Integer.valueOf(cursor.getString(cursor.getColumnIndex("food_cost"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("school_fees"))) {
                    beans.setSchoolFees(Integer.valueOf(cursor.getString(cursor.getColumnIndex("school_fees"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("saving_amount"))) {
                    beans.setSavingAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("saving_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("have_an_account"))) {
                    beans.setHaveAnAccount(Byte.valueOf(cursor.getString(cursor.getColumnIndex("have_an_account"))));
                }
                beans.setAccountOrganisation(cursor.getString(cursor.getColumnIndex("account_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("ever_taken_a_loan"))) {
                    beans.setEverTakenALoan(Byte.valueOf(cursor.getString(cursor.getColumnIndex("ever_taken_a_loan"))));
                }
                beans.setLoanOrganisation(cursor.getString(cursor.getColumnIndex("loan_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("other_access_to_credit"))) {
                    beans.setOtherAccessToCredit(Byte.valueOf(cursor.getString(cursor.getColumnIndex("other_access_to_credit"))));
                }
                beans.setOtherCreditOrganisation(cursor.getString(cursor.getColumnIndex("other_credit_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("any_current_loan"))) {
                    beans.setAnyCurrentLoan(Byte.valueOf(cursor.getString(cursor.getColumnIndex("any_current_loan"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("outstanding_loan_amount"))) {
                    beans.setOutstandingLoanAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("outstanding_loan_amount"))));
                }
                beans.setCurrentLoanOrganisation(cursor.getString(cursor.getColumnIndex("current_loan_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("employed"))) {
                    beans.setEmployed(Byte.valueOf(cursor.getString(cursor.getColumnIndex("employed"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("daily_customers"))) {
                    beans.setDailyCustomers(Integer.valueOf(cursor.getString(cursor.getColumnIndex("daily_customers"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("is_completed"))) {
                    beans.setIsCompleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_completed"))));
                }

                if (!cursor.isNull(cursor.getColumnIndex("affordability"))) {
                    beans.setAffordability(cursor.getInt(cursor.getColumnIndex("affordability")));
                }

                if (!cursor.isNull(cursor.getColumnIndex("to_create"))) {
                    beans.setTo_create(Byte.parseByte(cursor.getString(cursor.getColumnIndex("to_create"))));
                }

                if (!cursor.isNull(cursor.getColumnIndex("to_update"))) {
                    beans.setTo_update(Byte.parseByte(cursor.getString(cursor.getColumnIndex("to_update"))));
                }

                beans.setEducationLevelId(cursor.getString(cursor.getColumnIndex("education_level_id")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return beans;
    }

    public SiteVisit getSiteVisitById(String sitevisitid) {
        SiteVisit beans = null;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("site_visit", null, " id = ? and is_completed = 0", new String[]{sitevisitid}, null, null, "creation_date desc", "1");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new SiteVisit();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setCustomersId(cursor.getString(cursor.getColumnIndex("customers_id")));
                beans.setBusinessCategoryId(cursor.getString(cursor.getColumnIndex("business_category_id")));
                beans.setBusinessTypeId(cursor.getString(cursor.getColumnIndex("business_type_id")));
                beans.setProductTradingStartDate(cursor.getString(cursor.getColumnIndex("product_trading_start_date")));
                if (!cursor.isNull(cursor.getColumnIndex("is_reset"))) {
                    beans.setIsReset(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_reset"))));
                }
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                if (!cursor.isNull(cursor.getColumnIndex("stock_neat"))) {
                    beans.setStockNeat(Byte.parseByte(cursor.getString(cursor.getColumnIndex("stock_neat"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("accurate_ledger_book"))) {
                    beans.setAccurateLedgerBook(Byte.parseByte(cursor.getString(cursor.getColumnIndex("accurate_ledger_book"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sales_activity"))) {
                    beans.setSalesActivity(Byte.parseByte(cursor.getString(cursor.getColumnIndex("sales_activity"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("permanent_operation"))) {
                    beans.setPermanentOperation(Byte.parseByte(cursor.getString(cursor.getColumnIndex("permanent_operation"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("proof_of_ownership"))) {
                    beans.setProofOfOwnership(Byte.parseByte(cursor.getString(cursor.getColumnIndex("proof_of_ownership"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("forthcoming_and_transparent"))) {
                    beans.setForthcomingAndTransparent(Byte.parseByte(cursor.getString(cursor.getColumnIndex("forthcoming_and_transparent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("known_to_market_authorities"))) {
                    beans.setKnownToMarketAuthorities(Byte.parseByte(cursor.getString(cursor.getColumnIndex("known_to_market_authorities"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sound_reputation"))) {
                    beans.setSoundReputation(Byte.parseByte(cursor.getString(cursor.getColumnIndex("sound_reputation"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("would_I_lend"))) {
                    beans.setWouldILend(Byte.parseByte(cursor.getString(cursor.getColumnIndex("would_I_lend"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("lend_amount"))) {
                    beans.setLendAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("lend_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("business_rent"))) {
                    beans.setBusinessRent(Integer.valueOf(cursor.getString(cursor.getColumnIndex("business_rent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("business_utilities"))) {
                    beans.setBusinessUtilities(Integer.valueOf(cursor.getString(cursor.getColumnIndex("business_utilities"))));
                }
                beans.setNoOfEmployeesTypesId(cursor.getString(cursor.getColumnIndex("no_of_employees_types_id")));
                beans.setNatureOfEmploymentTypesId(cursor.getString(cursor.getColumnIndex("nature_of_employment_types_id")));
                if (!cursor.isNull(cursor.getColumnIndex("employees_salary"))) {
                    beans.setEmployeesSalary(Integer.valueOf(cursor.getString(cursor.getColumnIndex("employees_salary"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("licensing_fee"))) {
                    beans.setLicensingFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("licensing_fee"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("transport_fee"))) {
                    beans.setTransportFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("transport_fee"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("storage_fee"))) {
                    beans.setStorageFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("storage_fee"))));
                }
                beans.setBusinessCycleId(cursor.getString(cursor.getColumnIndex("business_cycle_id")));
                if (!cursor.isNull(cursor.getColumnIndex("current_stock_value"))) {
                    beans.setCurrentStockValue(Integer.valueOf(cursor.getString(cursor.getColumnIndex("current_stock_value"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sales_per_cycle"))) {
                    beans.setSalesPerCycle(Integer.valueOf(cursor.getString(cursor.getColumnIndex("sales_per_cycle"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("spend_on_stock"))) {
                    beans.setSpendOnStock(Integer.valueOf(cursor.getString(cursor.getColumnIndex("spend_on_stock"))));
                }
                beans.setIncomeExplanation(cursor.getString(cursor.getColumnIndex("income_explanation")));
                if (!cursor.isNull(cursor.getColumnIndex("cycle_restocking_frequency"))) {
                    beans.setCycleRestockingFrequency(Integer.valueOf(cursor.getString(cursor.getColumnIndex("cycle_restocking_frequency"))));
                }
                beans.setBusinessLocationTypesId(cursor.getString(cursor.getColumnIndex("business_location_types_id")));
                beans.setLocationTradingStartDate(cursor.getString(cursor.getColumnIndex("location_trading_start_date")));
                beans.setBusinessAddress(cursor.getString(cursor.getColumnIndex("business_address")));
                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("in_a_chama"))) {
                    beans.setIn_a_chama(Byte.valueOf(cursor.getString(cursor.getColumnIndex("in_a_chama"))));
                }
                beans.setChamaCycleId(cursor.getString(cursor.getColumnIndex("chama_cycle_id")));
                if (!cursor.isNull(cursor.getColumnIndex("chama_contribution"))) {
                    beans.setChamaContribution(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_contribution"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("no_of_chama_members"))) {
                    beans.setNoOfChamaMembers(Integer.valueOf(cursor.getString(cursor.getColumnIndex("no_of_chama_members"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_payout_frequency"))) {
                    beans.setChamaPayoutFrequency(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_payout_frequency"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_payout_amount"))) {
                    beans.setChamaPayoutAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_payout_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_has_records"))) {
                    beans.setChamaHasRecords(Byte.valueOf(cursor.getString(cursor.getColumnIndex("chama_has_records"))));
                }
                beans.setHouseTypeId(cursor.getString(cursor.getColumnIndex("house_type_id")));
                if (!cursor.isNull(cursor.getColumnIndex("home_rent"))) {
                    beans.setHomeRent(Integer.valueOf(cursor.getString(cursor.getColumnIndex("home_rent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("house_utilities"))) {
                    beans.setHouseUtilities(Integer.valueOf(cursor.getString(cursor.getColumnIndex("house_utilities"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("food_cost"))) {
                    beans.setFoodCost(Integer.valueOf(cursor.getString(cursor.getColumnIndex("food_cost"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("school_fees"))) {
                    beans.setSchoolFees(Integer.valueOf(cursor.getString(cursor.getColumnIndex("school_fees"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("saving_amount"))) {
                    beans.setSavingAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("saving_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("have_an_account"))) {
                    beans.setHaveAnAccount(Byte.valueOf(cursor.getString(cursor.getColumnIndex("have_an_account"))));
                }
                beans.setAccountOrganisation(cursor.getString(cursor.getColumnIndex("account_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("ever_taken_a_loan"))) {
                    beans.setEverTakenALoan(Byte.valueOf(cursor.getString(cursor.getColumnIndex("ever_taken_a_loan"))));
                }
                beans.setLoanOrganisation(cursor.getString(cursor.getColumnIndex("loan_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("other_access_to_credit"))) {
                    beans.setOtherAccessToCredit(Byte.valueOf(cursor.getString(cursor.getColumnIndex("other_access_to_credit"))));
                }
                beans.setOtherCreditOrganisation(cursor.getString(cursor.getColumnIndex("other_credit_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("any_current_loan"))) {
                    beans.setAnyCurrentLoan(Byte.valueOf(cursor.getString(cursor.getColumnIndex("any_current_loan"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("outstanding_loan_amount"))) {
                    beans.setOutstandingLoanAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("outstanding_loan_amount"))));
                }
                beans.setCurrentLoanOrganisation(cursor.getString(cursor.getColumnIndex("current_loan_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("daily_customers"))) {
                    beans.setDailyCustomers(Integer.valueOf(cursor.getString(cursor.getColumnIndex("daily_customers"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("employed"))) {
                    beans.setEmployed(Byte.valueOf(cursor.getString(cursor.getColumnIndex("employed"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("is_completed"))) {
                    beans.setIsCompleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_completed"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("affordability"))) {
                    beans.setAffordability(cursor.getInt(cursor.getColumnIndex("affordability")));
                }
                if (!cursor.isNull(cursor.getColumnIndex("to_create"))) {
                    beans.setTo_create(Byte.parseByte(cursor.getString(cursor.getColumnIndex("to_create"))));
                }

                if (!cursor.isNull(cursor.getColumnIndex("to_update"))) {
                    beans.setTo_update(Byte.parseByte(cursor.getString(cursor.getColumnIndex("to_update"))));
                }
                beans.setEducationLevelId(cursor.getString(cursor.getColumnIndex("education_level_id")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return beans;
    }

    public SiteVisit getLatestCustomerSiteVisit(String id) {
        SiteVisit beans = null;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query("site_visit", null, " customers_id = ? and is_completed = 1", new String[]{id}, null, null, "creation_date desc", "1");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                beans = new SiteVisit();
                beans.setId(cursor.getString(cursor.getColumnIndex("id")));
                beans.setCustomersId(cursor.getString(cursor.getColumnIndex("customers_id")));
                beans.setBusinessCategoryId(cursor.getString(cursor.getColumnIndex("business_category_id")));
                beans.setBusinessTypeId(cursor.getString(cursor.getColumnIndex("business_type_id")));
                beans.setProductTradingStartDate(cursor.getString(cursor.getColumnIndex("product_trading_start_date")));
                if (!cursor.isNull(cursor.getColumnIndex("is_reset"))) {
                    beans.setIsReset(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_reset"))));
                }
                beans.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));
                if (!cursor.isNull(cursor.getColumnIndex("stock_neat"))) {
                    beans.setStockNeat(Byte.parseByte(cursor.getString(cursor.getColumnIndex("stock_neat"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("accurate_ledger_book"))) {
                    beans.setAccurateLedgerBook(Byte.parseByte(cursor.getString(cursor.getColumnIndex("accurate_ledger_book"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sales_activity"))) {
                    beans.setSalesActivity(Byte.parseByte(cursor.getString(cursor.getColumnIndex("sales_activity"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("permanent_operation"))) {
                    beans.setPermanentOperation(Byte.parseByte(cursor.getString(cursor.getColumnIndex("permanent_operation"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("proof_of_ownership"))) {
                    beans.setProofOfOwnership(Byte.parseByte(cursor.getString(cursor.getColumnIndex("proof_of_ownership"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("forthcoming_and_transparent"))) {
                    beans.setForthcomingAndTransparent(Byte.parseByte(cursor.getString(cursor.getColumnIndex("forthcoming_and_transparent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("known_to_market_authorities"))) {
                    beans.setKnownToMarketAuthorities(Byte.parseByte(cursor.getString(cursor.getColumnIndex("known_to_market_authorities"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sound_reputation"))) {
                    beans.setSoundReputation(Byte.parseByte(cursor.getString(cursor.getColumnIndex("sound_reputation"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("would_I_lend"))) {
                    beans.setWouldILend(Byte.parseByte(cursor.getString(cursor.getColumnIndex("would_I_lend"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("lend_amount"))) {
                    beans.setLendAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("lend_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("business_rent"))) {
                    beans.setBusinessRent(Integer.valueOf(cursor.getString(cursor.getColumnIndex("business_rent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("business_utilities"))) {
                    beans.setBusinessUtilities(Integer.valueOf(cursor.getString(cursor.getColumnIndex("business_utilities"))));
                }
                beans.setNoOfEmployeesTypesId(cursor.getString(cursor.getColumnIndex("no_of_employees_types_id")));
                beans.setNatureOfEmploymentTypesId(cursor.getString(cursor.getColumnIndex("nature_of_employment_types_id")));
                if (!cursor.isNull(cursor.getColumnIndex("employees_salary"))) {
                    beans.setEmployeesSalary(Integer.valueOf(cursor.getString(cursor.getColumnIndex("employees_salary"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("licensing_fee"))) {
                    beans.setLicensingFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("licensing_fee"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("transport_fee"))) {
                    beans.setTransportFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("transport_fee"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("storage_fee"))) {
                    beans.setStorageFee(Integer.valueOf(cursor.getString(cursor.getColumnIndex("storage_fee"))));
                }
                beans.setBusinessCycleId(cursor.getString(cursor.getColumnIndex("business_cycle_id")));
                if (!cursor.isNull(cursor.getColumnIndex("current_stock_value"))) {
                    beans.setCurrentStockValue(Integer.valueOf(cursor.getString(cursor.getColumnIndex("current_stock_value"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("sales_per_cycle"))) {
                    beans.setSalesPerCycle(Integer.valueOf(cursor.getString(cursor.getColumnIndex("sales_per_cycle"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("spend_on_stock"))) {
                    beans.setSpendOnStock(Integer.valueOf(cursor.getString(cursor.getColumnIndex("spend_on_stock"))));
                }
                beans.setIncomeExplanation(cursor.getString(cursor.getColumnIndex("income_explanation")));
                if (!cursor.isNull(cursor.getColumnIndex("cycle_restocking_frequency"))) {
                    beans.setCycleRestockingFrequency(Integer.valueOf(cursor.getString(cursor.getColumnIndex("cycle_restocking_frequency"))));
                }
                beans.setBusinessLocationTypesId(cursor.getString(cursor.getColumnIndex("business_location_types_id")));
                beans.setLocationTradingStartDate(cursor.getString(cursor.getColumnIndex("location_trading_start_date")));
                beans.setBusinessAddress(cursor.getString(cursor.getColumnIndex("business_address")));
                if (!cursor.isNull(cursor.getColumnIndex("longitudes"))) {
                    beans.setLongitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("longitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("latitudes"))) {
                    beans.setLatitudes(Double.valueOf(cursor.getString(cursor.getColumnIndex("latitudes"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("in_a_chama"))) {
                    beans.setIn_a_chama(Byte.valueOf(cursor.getString(cursor.getColumnIndex("in_a_chama"))));
                }
                beans.setChamaCycleId(cursor.getString(cursor.getColumnIndex("chama_cycle_id")));
                if (!cursor.isNull(cursor.getColumnIndex("chama_contribution"))) {
                    beans.setChamaContribution(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_contribution"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("no_of_chama_members"))) {
                    beans.setNoOfChamaMembers(Integer.valueOf(cursor.getString(cursor.getColumnIndex("no_of_chama_members"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_payout_frequency"))) {
                    beans.setChamaPayoutFrequency(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_payout_frequency"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_payout_amount"))) {
                    beans.setChamaPayoutAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("chama_payout_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("chama_has_records"))) {
                    beans.setChamaHasRecords(Byte.valueOf(cursor.getString(cursor.getColumnIndex("chama_has_records"))));
                }
                beans.setHouseTypeId(cursor.getString(cursor.getColumnIndex("house_type_id")));
                if (!cursor.isNull(cursor.getColumnIndex("home_rent"))) {
                    beans.setHomeRent(Integer.valueOf(cursor.getString(cursor.getColumnIndex("home_rent"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("house_utilities"))) {
                    beans.setHouseUtilities(Integer.valueOf(cursor.getString(cursor.getColumnIndex("house_utilities"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("food_cost"))) {
                    beans.setFoodCost(Integer.valueOf(cursor.getString(cursor.getColumnIndex("food_cost"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("school_fees"))) {
                    beans.setSchoolFees(Integer.valueOf(cursor.getString(cursor.getColumnIndex("school_fees"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("saving_amount"))) {
                    beans.setSavingAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("saving_amount"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("have_an_account"))) {
                    beans.setHaveAnAccount(Byte.valueOf(cursor.getString(cursor.getColumnIndex("have_an_account"))));
                }
                beans.setAccountOrganisation(cursor.getString(cursor.getColumnIndex("account_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("ever_taken_a_loan"))) {
                    beans.setEverTakenALoan(Byte.valueOf(cursor.getString(cursor.getColumnIndex("ever_taken_a_loan"))));
                }
                beans.setLoanOrganisation(cursor.getString(cursor.getColumnIndex("loan_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("other_access_to_credit"))) {
                    beans.setOtherAccessToCredit(Byte.valueOf(cursor.getString(cursor.getColumnIndex("other_access_to_credit"))));
                }
                beans.setOtherCreditOrganisation(cursor.getString(cursor.getColumnIndex("other_credit_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("any_current_loan"))) {
                    beans.setAnyCurrentLoan(Byte.valueOf(cursor.getString(cursor.getColumnIndex("any_current_loan"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("outstanding_loan_amount"))) {
                    beans.setOutstandingLoanAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex("outstanding_loan_amount"))));
                }
                beans.setCurrentLoanOrganisation(cursor.getString(cursor.getColumnIndex("current_loan_organisation")));
                if (!cursor.isNull(cursor.getColumnIndex("daily_customers"))) {
                    beans.setDailyCustomers(Integer.valueOf(cursor.getString(cursor.getColumnIndex("daily_customers"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("employed"))) {
                    beans.setEmployed(Byte.valueOf(cursor.getString(cursor.getColumnIndex("employed"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("is_completed"))) {
                    beans.setIsCompleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_completed"))));
                }
                if (!cursor.isNull(cursor.getColumnIndex("affordability"))) {
                    beans.setAffordability(cursor.getInt(cursor.getColumnIndex("affordability")));
                }
                if (!cursor.isNull(cursor.getColumnIndex("to_create"))) {
                    beans.setTo_create(Byte.parseByte(cursor.getString(cursor.getColumnIndex("to_create"))));
                }

                if (!cursor.isNull(cursor.getColumnIndex("to_update"))) {
                    beans.setTo_update(Byte.parseByte(cursor.getString(cursor.getColumnIndex("to_update"))));
                }
                beans.setEducationLevelId(cursor.getString(cursor.getColumnIndex("education_level_id")));
                beans.setCreatorId(cursor.getString(cursor.getColumnIndex("creator_id")));
                beans.setCreationDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("creation_date"))));
                beans.setUpdatedDate(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("updated_date"))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return beans;
    }
}
