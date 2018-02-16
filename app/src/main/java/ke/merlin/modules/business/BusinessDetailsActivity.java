package ke.merlin.modules.business;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import ke.merlin.R;
import ke.merlin.dao.business.StBusinessCategoriesDao;
import ke.merlin.dao.business.StBusinessCyclesDao;
import ke.merlin.dao.business.StBusinessLocationsDao;
import ke.merlin.dao.business.StBusinessTypesDao;
import ke.merlin.dao.business.StNatureOfEmploymentDao;
import ke.merlin.dao.business.StNoOfEmployeesRangeDao;
import ke.merlin.dao.customers.StChamaCyclesDao;
import ke.merlin.dao.customers.StCreditOrganisationsDao;
import ke.merlin.dao.customers.StEducationLevelsDao;
import ke.merlin.dao.customers.StHouseTypesDao;
import ke.merlin.models.business.StBusinessCategories;
import ke.merlin.models.business.StBusinessCycles;
import ke.merlin.models.business.StBusinessLocations;
import ke.merlin.models.business.StBusinessTypes;
import ke.merlin.models.business.StNatureOfEmployment;
import ke.merlin.models.business.StNoOfEmployeesRange;
import ke.merlin.models.customers.StChamaCycles;
import ke.merlin.models.customers.StCreditOrganisations;
import ke.merlin.models.customers.StEducationLevels;
import ke.merlin.models.customers.StHouseTypes;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.utils.AuthDetails;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.UrlConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BusinessDetailsActivity extends AppCompatActivity {

    TextView bus_category, bus_type, product_trade_date, location_type, location_trading_start_date, business_address, business_cycle,
            stock_value, sales_value, stock_spend, restocking_frequency, income_explanation, business_rent, business_utilities, no_of_employees,
            nature_of_employment, employees_salary, licensing_fee, storage_fee, transport_fee, house_type, home_rent, house_utilities, food_cost,
            school_fees, saving_amount, in_a_chama, chama_cycle, chama_contribution, chama_members, payout_frequency, payout_amount, records_available,
            do_you_have_a_bank_account, account_organisation, have_you_ever_taken_a_loan, loan_organisation, do_you_have_access_to_credit, credit_organisation,
            do_you_have_a_current_loan, current_loan_organisation, loan_balance, average_daily_customers, are_you_emplyed, customer_education_level,
            stock_neat, accurate_ledger_book, evidence_of_sales_activity, evidence_of_permanent_operation, proof_of_ownership, forthcoming_and_transparent,
            known_to_market_authority, sound_reputation, would_i_lend, how_much;

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Business Details");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
        } else {
            id = (String) savedInstanceState.getSerializable("id");
        }

        initViews();

    }

    private void initViews() {
        bus_category = (TextView) findViewById(R.id.bus_category);
        bus_type = (TextView) findViewById(R.id.bus_type);
        product_trade_date = (TextView) findViewById(R.id.product_trade_date);
        location_type = (TextView) findViewById(R.id.location_type);
        location_trading_start_date = (TextView) findViewById(R.id.location_trading_start_date);
        business_address = (TextView) findViewById(R.id.business_address);
        business_cycle = (TextView) findViewById(R.id.business_cycle);
        stock_value = (TextView) findViewById(R.id.stock_value);
        sales_value = (TextView) findViewById(R.id.sales_value);
        stock_spend = (TextView) findViewById(R.id.stock_spend);
        restocking_frequency = (TextView) findViewById(R.id.restocking_frequency);
        income_explanation = (TextView) findViewById(R.id.income_explanation);
        business_rent = (TextView) findViewById(R.id.business_rent);
        business_utilities = (TextView) findViewById(R.id.business_utilities);
        no_of_employees = (TextView) findViewById(R.id.no_of_employees);
        nature_of_employment = (TextView) findViewById(R.id.nature_of_employment);
        employees_salary = (TextView) findViewById(R.id.employees_salary);
        licensing_fee = (TextView) findViewById(R.id.licensing_fee);
        storage_fee = (TextView) findViewById(R.id.storage_fee);
        transport_fee = (TextView) findViewById(R.id.transport_fee);
        house_type = (TextView) findViewById(R.id.house_type);
        home_rent = (TextView) findViewById(R.id.home_rent);
        house_utilities = (TextView) findViewById(R.id.house_utilities);
        food_cost = (TextView) findViewById(R.id.food_cost);
        school_fees = (TextView) findViewById(R.id.school_fees);
        saving_amount = (TextView) findViewById(R.id.saving_amount);
        in_a_chama = (TextView) findViewById(R.id.in_a_chama);
        chama_cycle = (TextView) findViewById(R.id.chama_cycle);
        chama_contribution = (TextView) findViewById(R.id.chama_contribution);
        chama_members = (TextView) findViewById(R.id.chama_members);
        payout_frequency = (TextView) findViewById(R.id.payout_frequency);
        payout_amount = (TextView) findViewById(R.id.payout_amount);
        records_available = (TextView) findViewById(R.id.records_available);
        do_you_have_a_bank_account = (TextView) findViewById(R.id.do_you_have_a_bank_account);
        account_organisation = (TextView) findViewById(R.id.account_organisation);
        have_you_ever_taken_a_loan = (TextView) findViewById(R.id.have_you_ever_taken_a_loan);
        loan_organisation = (TextView) findViewById(R.id.loan_organisation);
        do_you_have_access_to_credit = (TextView) findViewById(R.id.do_you_have_access_to_credit);
        credit_organisation = (TextView) findViewById(R.id.credit_organisation);
        do_you_have_a_current_loan = (TextView) findViewById(R.id.do_you_have_a_current_loan);
        current_loan_organisation = (TextView) findViewById(R.id.current_loan_organisation);
        loan_balance = (TextView) findViewById(R.id.loan_balance);
        average_daily_customers = (TextView) findViewById(R.id.average_daily_customers);
        are_you_emplyed = (TextView) findViewById(R.id.are_you_emplyed);
        customer_education_level = (TextView) findViewById(R.id.customer_education_level);
        stock_neat = (TextView) findViewById(R.id.stock_neat);
        accurate_ledger_book = (TextView) findViewById(R.id.accurate_ledger_book);
        evidence_of_sales_activity = (TextView) findViewById(R.id.evidence_of_sales_activity);
        evidence_of_permanent_operation = (TextView) findViewById(R.id.evidence_of_permanent_operation);
        proof_of_ownership = (TextView) findViewById(R.id.proof_of_ownership);
        forthcoming_and_transparent = (TextView) findViewById(R.id.forthcoming_and_transparent);
        known_to_market_authority = (TextView) findViewById(R.id.known_to_market_authority);
        sound_reputation = (TextView) findViewById(R.id.sound_reputation);
        would_i_lend = (TextView) findViewById(R.id.would_i_lend);
        how_much = (TextView) findViewById(R.id.how_much);

        getAffordability(id);

    }

    private void getAffordability(String id) {

        final SiteVisit[] siteVisit = {null};

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(UrlConstants.SiteVisit_Latest_URL + id)
                .addHeader("authorization", AuthDetails.getAuth())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        String responseBody = response.body().string();
                        Log.i(" responseBody ", responseBody.substring(0, 10));

                        SiteVisit siteVisit = gson.fromJson(responseBody, SiteVisit.class);
                        setResponseData(siteVisit);

                    }
                }
            }
        });

    }

    private void setResponseData(SiteVisit siteVisit) {

        StBusinessCategoriesDao stBusinessCategoriesDao = new StBusinessCategoriesDao();
        StBusinessCategories stBusinessCategories = stBusinessCategoriesDao.getById(siteVisit.getBusinessCategoryId());

        StBusinessTypesDao stBusinessTypesDao = new StBusinessTypesDao();
        StBusinessTypes stBusinessTypes = stBusinessTypesDao.getById(siteVisit.getBusinessTypeId());

        StBusinessLocationsDao stBusinessLocationsDao = new StBusinessLocationsDao();
        StBusinessLocations stBusinessLocations = stBusinessLocationsDao.getById(siteVisit.getBusinessLocationTypesId());

        final String txt_bus_category = (stBusinessCategories != null) ? stBusinessCategories.getName() : "";
        final String txt_bus_type = (stBusinessTypes != null) ? stBusinessTypes.getName() : "";
        final String txt_product_trade_date = siteVisit.getProductTradingStartDate();
        final String txt_location_type = (stBusinessLocations != null) ? stBusinessLocations.getName() : "";
        final String txt_location_trading_start_date = siteVisit.getLocationTradingStartDate();
        final String txt_business_address = siteVisit.getBusinessAddress();

        StBusinessCyclesDao stBusinessCyclesDao = new StBusinessCyclesDao();
        StBusinessCycles stBusinessCycles = stBusinessCyclesDao.getById(siteVisit.getBusinessCycleId());

        final String txt_business_cycle = (stBusinessCycles != null) ? stBusinessCycles.getName() : "";
        final String txt_stock_value = String.valueOf(siteVisit.getCurrentStockValue());
        final String txt_sales_value = String.valueOf(siteVisit.getSalesPerCycle());
        final String txt_stock_spend = String.valueOf(siteVisit.getSpendOnStock());
        final String txt_restocking_frequency = String.valueOf(siteVisit.getCycleRestockingFrequency());
        final String txt_income_explanation = siteVisit.getIncomeExplanation();

        StNoOfEmployeesRangeDao stNoOfEmployeesRangeDao = new StNoOfEmployeesRangeDao();
        StNoOfEmployeesRange stNoOfEmployeesRange = stNoOfEmployeesRangeDao.getById(siteVisit.getNoOfEmployeesTypesId());

        StNatureOfEmploymentDao stNatureOfEmploymentDao = new StNatureOfEmploymentDao();
        StNatureOfEmployment stNatureOfEmployment = stNatureOfEmploymentDao.getById(siteVisit.getNatureOfEmploymentTypesId());

        final String txt_business_rent = String.valueOf(siteVisit.getBusinessRent());
        final String txt_business_utilities = String.valueOf(siteVisit.getBusinessUtilities());
        final String txt_no_of_employees = (stNoOfEmployeesRange != null) ? stNoOfEmployeesRange.getName() : "";
        final String txt_nature_of_employment = (stNatureOfEmployment != null) ? stNatureOfEmployment.getName() : "";
        final String txt_employees_salary = String.valueOf(siteVisit.getEmployeesSalary());
        final String txt_licensing_fee = String.valueOf(siteVisit.getLicensingFee());
        final String txt_storage_fee = String.valueOf(siteVisit.getStorageFee());
        final String txt_transport_fee = String.valueOf(siteVisit.getTransportFee());

        StHouseTypesDao stHouseTypesDao = new StHouseTypesDao();
        StHouseTypes stHouseTypes = stHouseTypesDao.getById(siteVisit.getHouseTypeId());

        final String txt_house_type = (stHouseTypes != null) ? stHouseTypes.getName() : "";
        final String txt_home_rent = String.valueOf(siteVisit.getHomeRent());
        final String txt_house_utilities = String.valueOf(siteVisit.getHouseUtilities());
        final String txt_food_cost = String.valueOf(siteVisit.getFoodCost());
        final String txt_school_fees = String.valueOf(siteVisit.getSchoolFees());
        final String txt_saving_amount = String.valueOf(siteVisit.getSavingAmount());

        StChamaCyclesDao stChamaCyclesDao = new StChamaCyclesDao();
        StChamaCycles stChamaCycles = stChamaCyclesDao.getById(siteVisit.getChamaCycleId());

         String txt_in_a_chama = "";
        if (siteVisit.getIn_a_chama() != null) {
            txt_in_a_chama = (siteVisit.getIn_a_chama() == 1) ? "Yes" : "No";
        }
        final String txt_chama_cycle = (stChamaCycles != null) ? stChamaCycles.getName() : "";
        final String txt_chama_contribution = String.valueOf(siteVisit.getChamaContribution());
        final String txt_chama_members = String.valueOf(siteVisit.getNoOfChamaMembers());
        final String txt_payout_frequency = String.valueOf(siteVisit.getChamaPayoutFrequency());
        final String txt_payout_amount = String.valueOf(siteVisit.getChamaPayoutAmount());
        final String txt_records_available = (siteVisit.getChamaHasRecords() == 1) ? "Yes" : "No";

        StCreditOrganisationsDao stCreditOrganisationsDao = new StCreditOrganisationsDao();
        StCreditOrganisations bn_account_organisation = stCreditOrganisationsDao.getById(siteVisit.getAccountOrganisation());
        StCreditOrganisations bn_loan_organisation = stCreditOrganisationsDao.getById(siteVisit.getLoanOrganisation());
        StCreditOrganisations bn_credit_organisation = stCreditOrganisationsDao.getById(siteVisit.getOtherCreditOrganisation());
        StCreditOrganisations bn_current_loan_organisation = stCreditOrganisationsDao.getById(siteVisit.getCurrentLoanOrganisation());

        StEducationLevelsDao stEducationLevelsDao = new StEducationLevelsDao();
        StEducationLevels stEducationLevels = stEducationLevelsDao.getById(siteVisit.getEducationLevelId());

        final String txt_do_you_have_a_bank_account = (siteVisit.getHaveAnAccount() == 1) ? "Yes" : "No";
        final String txt_account_organisation = (bn_account_organisation != null) ? bn_account_organisation.getName() : "";
        final String txt_have_you_ever_taken_a_loan = (siteVisit.getEverTakenALoan() == 1) ? "Yes" : "No";
        final String txt_loan_organisation = (bn_loan_organisation != null) ? bn_loan_organisation.getName() : "";
        final String txt_do_you_have_access_to_credit = (siteVisit.getOtherAccessToCredit() == 1) ? "Yes" : "No";
        final String txt_credit_organisation = (bn_credit_organisation != null) ? bn_credit_organisation.getName() : "";
        final String txt_do_you_have_a_current_loan = (siteVisit.getAnyCurrentLoan() == 1) ? "Yes" : "No";
        final String txt_current_loan_organisation = (bn_current_loan_organisation != null) ? bn_current_loan_organisation.getName() : "";
        final String txt_loan_balance = String.valueOf(siteVisit.getOutstandingLoanAmount());
        final String txt_average_daily_customers = String.valueOf(siteVisit.getDailyCustomers());
        final String txt_are_you_emplyed = (siteVisit.getEmployed() == 1) ? "Yes" : "No";
        final String txt_customer_education_level = (stEducationLevels != null) ? stEducationLevels.getName() : "";

        final String txt_stock_neat = (siteVisit.getStockNeat() == 1) ? "Yes" : "No";
        final String txt_accurate_ledger_book = (siteVisit.getAccurateLedgerBook() == 1) ? "Yes" : "No";
        final String txt_evidence_of_sales_activity = (siteVisit.getSalesActivity() == 1) ? "Yes" : "No";
        final String txt_evidence_of_permanent_operation = (siteVisit.getPermanentOperation() == 1) ? "Yes" : "No";
        final String txt_proof_of_ownership = (siteVisit.getProofOfOwnership() == 1) ? "Yes" : "No";
        final String txt_forthcoming_and_transparent = (siteVisit.getForthcomingAndTransparent() == 1) ? "Yes" : "No";
        final String txt_known_to_market_authority = (siteVisit.getKnownToMarketAuthorities() == 1) ? "Yes" : "No";
        final String txt_sound_reputation = (siteVisit.getSoundReputation() == 1) ? "Yes" : "No";
        final String txt_would_i_lend = (siteVisit.getWouldILend() == 1) ? "Yes" : "No";
        final String txt_how_much = String.valueOf(siteVisit.getLendAmount());

        final String finalTxt_in_a_chama = txt_in_a_chama;
        BusinessDetailsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bus_category.setText(txt_bus_category);
                bus_type.setText(txt_bus_type);
                product_trade_date.setText(txt_product_trade_date);
                location_type.setText(txt_location_type);
                location_trading_start_date.setText(txt_location_trading_start_date);
                business_address.setText(txt_business_address);
                business_cycle.setText(txt_business_cycle);
                stock_value.setText(txt_stock_value);
                sales_value.setText(txt_sales_value);
                stock_spend.setText(txt_stock_spend);
                restocking_frequency.setText(txt_restocking_frequency);
                income_explanation.setText(txt_income_explanation);
                business_rent.setText(txt_business_rent);
                business_utilities.setText(txt_business_utilities);
                no_of_employees.setText(txt_no_of_employees);
                nature_of_employment.setText(txt_nature_of_employment);
                employees_salary.setText(txt_employees_salary);
                licensing_fee.setText(txt_licensing_fee);
                storage_fee.setText(txt_storage_fee);
                transport_fee.setText(txt_transport_fee);
                house_type.setText(txt_house_type);
                home_rent.setText(txt_home_rent);
                house_utilities.setText(txt_house_utilities);
                food_cost.setText(txt_food_cost);
                school_fees.setText(txt_school_fees);
                saving_amount.setText(txt_saving_amount);
                in_a_chama.setText(finalTxt_in_a_chama);
                chama_cycle.setText(txt_chama_cycle);
                chama_contribution.setText(txt_chama_contribution);
                chama_members.setText(txt_chama_members);
                payout_frequency.setText(txt_payout_frequency);
                payout_amount.setText(txt_payout_amount);
                records_available.setText(txt_records_available);
                do_you_have_a_bank_account.setText(txt_do_you_have_a_bank_account);
                account_organisation.setText(txt_account_organisation);
                have_you_ever_taken_a_loan.setText(txt_have_you_ever_taken_a_loan);
                loan_organisation.setText(txt_loan_organisation);
                do_you_have_access_to_credit.setText(txt_do_you_have_access_to_credit);
                credit_organisation.setText(txt_credit_organisation);
                do_you_have_a_current_loan.setText(txt_do_you_have_a_current_loan);
                current_loan_organisation.setText(txt_current_loan_organisation);
                loan_balance.setText(txt_loan_balance);
                average_daily_customers.setText(txt_average_daily_customers);
                are_you_emplyed.setText(txt_are_you_emplyed);
                customer_education_level.setText(txt_customer_education_level);
                stock_neat.setText(txt_stock_neat);
                accurate_ledger_book.setText(txt_accurate_ledger_book);
                evidence_of_sales_activity.setText(txt_evidence_of_sales_activity);
                evidence_of_permanent_operation.setText(txt_evidence_of_permanent_operation);
                proof_of_ownership.setText(txt_proof_of_ownership);
                forthcoming_and_transparent.setText(txt_forthcoming_and_transparent);
                known_to_market_authority.setText(txt_known_to_market_authority);
                sound_reputation.setText(txt_sound_reputation);
                would_i_lend.setText(txt_would_i_lend);
                how_much.setText(txt_how_much);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("id", id);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id = savedInstanceState.getString("id");
    }

}
