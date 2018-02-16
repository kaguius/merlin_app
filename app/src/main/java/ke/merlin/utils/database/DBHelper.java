package ke.merlin.utils.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ke.merlin.dao.Last_syncDao;
import ke.merlin.dao.business.BusinessAssessmentDao;
import ke.merlin.dao.business.BusinessDao;
import ke.merlin.dao.business.BusinessExpensesDao;
import ke.merlin.dao.business.BusinessIncomesDao;
import ke.merlin.dao.business.BusinessLocationDao;
import ke.merlin.dao.customers.CustomersChamasDao;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.customers.CustomersDependantsDao;
import ke.merlin.dao.customers.CustomersExpensesDao;
import ke.merlin.dao.customers.CustomersLoanLimitDao;
import ke.merlin.dao.customers.CustomersRefereesDao;
import ke.merlin.dao.customers.CustomersSocialsDao;
import ke.merlin.dao.customers.LeadsOutcomesDao;
import ke.merlin.dao.loans.LoansApplicationsDao;
import ke.merlin.dao.loans.LoansBfcDao;
import ke.merlin.dao.loans.LoansDao;
import ke.merlin.dao.loans.LoansInteractionsDao;
import ke.merlin.dao.loans.LoansOverpaymentsDao;
import ke.merlin.dao.loans.LoansPenaltiesDao;
import ke.merlin.dao.loans.LoansRepaymentsDao;
import ke.merlin.dao.loans.LoansWaiversDao;
import ke.merlin.dao.loans.ProductsDao;
import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.dao.loans.TelcosShortcodeDao;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.dao.stations.StationsTargetsDao;
import ke.merlin.dao.suspense.SuspensePaymentsDao;
import ke.merlin.dao.users.PrivilegesDao;
import ke.merlin.dao.users.RolesDao;
import ke.merlin.dao.stations.SectorsDao;
import ke.merlin.dao.sms.SmsIncomingDao;
import ke.merlin.dao.sms.SmsOutgoingDao;
import ke.merlin.dao.business.StBusinessCategoriesDao;
import ke.merlin.dao.business.StBusinessCyclesDao;
import ke.merlin.dao.business.StBusinessLocationsDao;
import ke.merlin.dao.business.StBusinessTypesDao;
import ke.merlin.dao.customers.StChamaCyclesDao;
import ke.merlin.dao.customers.StCreditOrganisationsDao;
import ke.merlin.dao.customers.StCreditOrganisationsTypesDao;
import ke.merlin.dao.customers.StCustomerActiveStatusDao;
import ke.merlin.dao.customers.StCustomerApprovalStatusDao;
import ke.merlin.dao.customers.StCustomerStateDao;
import ke.merlin.dao.customers.StCustomerTitlesDao;
import ke.merlin.dao.customers.StEducationLevelsDao;
import ke.merlin.dao.customers.StGendersDao;
import ke.merlin.dao.customers.StHomeOwnershipsDao;
import ke.merlin.dao.customers.StHouseTypesDao;
import ke.merlin.dao.customers.StInfoSourcesDao;
import ke.merlin.dao.loans.StInteractionsCallOutcomessDao;
import ke.merlin.dao.loans.StInteractionsCategoriesDao;
import ke.merlin.dao.customers.StLanguagesDao;
import ke.merlin.dao.customers.StLeadOutcomesDao;
import ke.merlin.dao.loans.StLoansApprovalStatusDao;
import ke.merlin.dao.loans.StLoansArrearsStatusDao;
import ke.merlin.dao.loans.StLoansAssignmentStatusDao;
import ke.merlin.dao.customers.StMaritalStatusDao;
import ke.merlin.dao.business.StNatureOfEmploymentDao;
import ke.merlin.dao.business.StNoOfEmployeesRangeDao;
import ke.merlin.dao.loans.StReasonForDefaultDao;
import ke.merlin.dao.customers.StRefereesRelationshipDao;
import ke.merlin.dao.users.StUsersStatusDao;
import ke.merlin.dao.stations.StationsDao;
import ke.merlin.dao.stations.StationsMarketsDao;
import ke.merlin.dao.loans.TelcosDao;
import ke.merlin.dao.users.UsersDao;
import ke.merlin.dao.users.UsersPrivilegesDao;

/**
 * Created by mecmurimi on 20/03/2017.
 */

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 8;
    // Database Name
    private static final String DATABASE_NAME = "merlin.db";
    private static final String TAG = DBHelper.class.getSimpleName().toString();

    public DBHelper( ) {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Registration Tables
        db.execSQL(RegistrationsApiDao.createRegistrationApiTable());
        db.execSQL(Last_syncDao.createlastSyncedTable());

        //Business Tables
        db.execSQL(BusinessDao.createBusinessTable());
        db.execSQL(BusinessAssessmentDao.createBusinessAssessmentTable());
        db.execSQL(BusinessExpensesDao.createBusinessExpensesTable());
        db.execSQL(BusinessIncomesDao.createBusinessIncomesTable());
        db.execSQL(BusinessLocationDao.createBusinessLocationTable());
        db.execSQL(StBusinessCategoriesDao.createBusinessCategoriesTable());

        db.execSQL(StBusinessCyclesDao.createBusinessCysclesTable());
        db.execSQL(StBusinessLocationsDao.createBusinessStLocationsTable());
        db.execSQL(StBusinessTypesDao.createBusinessTypesTable());
        db.execSQL(StNatureOfEmploymentDao.createNatureofEmploymentTable());
        db.execSQL(StNoOfEmployeesRangeDao.createNoOfEmployeesRangeTable());

        //Sitevisit Tables
        db.execSQL(SiteVisitDao.createSiteVisitTable());

        //SMS Tables
        db.execSQL(SmsIncomingDao.createSmsIncomingTable());
        db.execSQL(SmsOutgoingDao.createSmsOutgoingTable());

        //Suspense Tables
        db.execSQL(SuspensePaymentsDao.createSuspensePaymentsTable());

        //Customers Tables
        db.execSQL(CustomersChamasDao.createCustomersChamasTable());
        db.execSQL(CustomersDao.createCustomersTable());
        db.execSQL(CustomersDependantsDao.createCustomersDependantsTable());
        db.execSQL(CustomersExpensesDao.createCustomersExpensesTable());

        db.execSQL(CustomersLoanLimitDao.createCustomersLoanLimitTable());
        db.execSQL(CustomersRefereesDao.createCustomersRefereesTable());
        db.execSQL(CustomersSocialsDao.createCustomersSocialsTable());
        db.execSQL(LeadsOutcomesDao.createLeadOutcomeTable());
        db.execSQL(StChamaCyclesDao.createChamaCyclesTable());

        db.execSQL(StCreditOrganisationsDao.createCreditOrganisationTable());
        db.execSQL(StCreditOrganisationsTypesDao.createCreditOrganisationTypesTable());
        db.execSQL(StCustomerActiveStatusDao.createCustomerActiveStatusTable());
        db.execSQL(StCustomerApprovalStatusDao.createCustomerApprovalStatusTable());
        db.execSQL(StCustomerStateDao.createCustomerStateTable());

        db.execSQL(StCustomerTitlesDao.createCustomerTitlesTable());
        db.execSQL(StEducationLevelsDao.createEducationLevelsTable());
        db.execSQL(StGendersDao.createGenderTable());
        db.execSQL(StHomeOwnershipsDao.createHomeOwnershipTable());
        db.execSQL(StHouseTypesDao.createHouseTypesTable());

        db.execSQL(StInfoSourcesDao.createInfoSourcesTable());
        db.execSQL(StLanguagesDao.createLanguagesTable());
        db.execSQL(StLeadOutcomesDao.createStLeadOutcomesTable());
        db.execSQL(StMaritalStatusDao.createMaritalStatusTable());
        db.execSQL(StRefereesRelationshipDao.createRefereeRelationshipTable());

        //Loans Tables
        db.execSQL(LoansApplicationsDao.createLoansApplicationTable());
        db.execSQL(LoansBfcDao.createLoansBfcTable());
        db.execSQL(LoansDao.createLoansTable());
        db.execSQL(LoansInteractionsDao.createLoansInteractionsTable());
        db.execSQL(LoansOverpaymentsDao.createLoansOverpaymentTable());

        db.execSQL(LoansPenaltiesDao.createLoansPenaltiesTable());
        db.execSQL(LoansRepaymentsDao.createLoansRepaymentsTable());
        db.execSQL(LoansWaiversDao.createLoansWaiversTable());
        db.execSQL(ProductsDao.createProductsTable());
        db.execSQL(StInteractionsCategoriesDao.createInteractionsCategoriesTable());

        db.execSQL(StInteractionsCallOutcomessDao.createInteractionCallOutcomesTable());
        db.execSQL(StLoansApprovalStatusDao.createLoanApprovalStatusTable());
        db.execSQL(StLoansArrearsStatusDao.createLoanArrearsStatusTable());
        db.execSQL(StLoansAssignmentStatusDao.createLoanAssignmentStatusTable());
        db.execSQL(StReasonForDefaultDao.createReasonForDefaultTable());

        db.execSQL(TelcosDao.createTelcosTable());
        db.execSQL(TelcosShortcodeDao.createTelcosShortcodesTable());

        //Stations Tables
        db.execSQL(SectorsDao.createSectorsTable());
        db.execSQL(StationsDao.createStationsTable());
        db.execSQL(StationsMarketsDao.createStationMarketsTable());
        db.execSQL(StationsTargetsDao.createStationsTargetsTable());

        //Users Tables
        db.execSQL(PrivilegesDao.createPrivilegesTable());
        db.execSQL(RolesDao.createRolesTable());
        db.execSQL(StUsersStatusDao.createUserStatusTable());
        db.execSQL(UsersPrivilegesDao.createUserPrivilegesTable());
        db.execSQL(UsersDao.createUsersTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop table if existed, all data will be gone!!!
//        db.execSQL("DROP TABLE IF EXISTS " + "age_of_dependants_types");

        onCreate(db);
    }

}
