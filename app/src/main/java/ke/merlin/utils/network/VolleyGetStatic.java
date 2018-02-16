package ke.merlin.utils.network;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ke.merlin.dao.loans.ProductsDao;
import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.dao.users.RolesDao;
import ke.merlin.dao.stations.SectorsDao;
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
import ke.merlin.models.RegistrationsApi;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * Created by mecmurimi on 25/07/2017.
 */

public class VolleyGetStatic extends Thread {

    private static Context mCtx;
    String tag;
    String url;
    String table_name;
    String category;

    public VolleyGetStatic(String tag, String url, String table_name, String category, Context context) {
        this.tag = tag;
        this.url = url;
        this.table_name = table_name;
        this.category = category;
        mCtx = context;
    }

    @Override
    public void run() {

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response " + table_name, response);

                Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();

                if (table_name.equals("st_languages")) {
                    StLanguagesDao stLanguagesDao = new StLanguagesDao();
                    long res = stLanguagesDao.insertList(response);
                    Log.i("st_languages Result: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_business_categories")) {
                    StBusinessCategoriesDao stBusinessCategoriesDao = new StBusinessCategoriesDao();
                    long res = stBusinessCategoriesDao.insertList(response);
                    Log.i("st_business_categories:", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_business_types")) {
                    StBusinessTypesDao stBusinessTypesDao = new StBusinessTypesDao();
                    long res = stBusinessTypesDao.insertList(response);
                    Log.i("st_business_types: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_nature_of_employment")) {
                    StNatureOfEmploymentDao stNatureOfEmploymentDao = new StNatureOfEmploymentDao();
                    long res = stNatureOfEmploymentDao.insertList(response);
                    Log.i("nature_of_employment:", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_no_of_employees_range")) {
                    StNoOfEmployeesRangeDao stNoOfEmployeesRangeDao = new StNoOfEmployeesRangeDao();
                    long res = stNoOfEmployeesRangeDao.insertList(response);
                    Log.i("Leads Result: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_business_cycles")) {
                    StBusinessCyclesDao stBusinessCyclesDao = new StBusinessCyclesDao();
                    long res = stBusinessCyclesDao.insertList(response);
                    Log.i("st_business_cycles: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_business_locations")) {
                    StBusinessLocationsDao stBusinessLocationsDao = new StBusinessLocationsDao();
                    long res = stBusinessLocationsDao.insertList(response);
                    Log.i("st_business_locations: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_genders")) {
                    StGendersDao stGendersDao = new StGendersDao();
                    long res = stGendersDao.insertList(response);
                    Log.i("st_genders: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_customer_titles")) {
                    StCustomerTitlesDao stCustomerTitlesDao = new StCustomerTitlesDao();
                    long res = stCustomerTitlesDao.insertList(response);
                    Log.i("st_customer_titles: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_chama_cycles")) {
                    StChamaCyclesDao stChamaCyclesDao = new StChamaCyclesDao();
                    long res = stChamaCyclesDao.insertList(response);
                    Log.i("st_chama_cycles: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_house_types")) {
                    StHouseTypesDao stHouseTypesDao = new StHouseTypesDao();
                    long res = stHouseTypesDao.insertList(response);
                    Log.i("st_house_types: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_home_ownerships")) {
                    StHomeOwnershipsDao stHomeOwnershipsDao = new StHomeOwnershipsDao();
                    long res = stHomeOwnershipsDao.insertList(response);
                    Log.i("st_home_ownerships: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_referees_relationship")) {
                    StRefereesRelationshipDao stRefereesRelationshipDao = new StRefereesRelationshipDao();
                    long res = stRefereesRelationshipDao.insertList(response);
                    Log.i("referees_relationship: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_credit_organisations_types")) {
                    StCreditOrganisationsTypesDao stCreditOrganisationsTypesDao = new StCreditOrganisationsTypesDao();
                    long res = stCreditOrganisationsTypesDao.insertList(response);
                    Log.i("organisations_types: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_credit_organisations")) {
                    StCreditOrganisationsDao stCreditOrganisationsDao = new StCreditOrganisationsDao();
                    long res = stCreditOrganisationsDao.insertList(response);
                    Log.i("credit_organisations: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_education_levels")) {
                    StEducationLevelsDao stEducationLevelsDao = new StEducationLevelsDao();
                    long res = stEducationLevelsDao.insertList(response);
                    Log.i("st_education_levels: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_customer_active_status")) {
                    StCustomerActiveStatusDao stCustomerActiveStatusDao = new StCustomerActiveStatusDao();
                    long res = stCustomerActiveStatusDao.insertList(response);
                    Log.i("customer_active: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_customer_approval_status")) {
                    StCustomerApprovalStatusDao stCustomerApprovalStatusDao = new StCustomerApprovalStatusDao();
                    long res = stCustomerApprovalStatusDao.insertList(response);
                    Log.i("customer_approval:", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_customer_state")) {
                    StCustomerStateDao stCustomerStateDao = new StCustomerStateDao();
                    long res = stCustomerStateDao.insertList(response);
                    Log.i("st_customer_state: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_marital_status")) {
                    StMaritalStatusDao stMaritalStatusDao = new StMaritalStatusDao();
                    long res = stMaritalStatusDao.insertList(response);
                    Log.i("st_marital_status: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("stations_markets")) {
                    StationsMarketsDao stationsMarketsDao = new StationsMarketsDao();
                    long res = stationsMarketsDao.insertList(response);
                    Log.i("stations_markets: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("stations")) {
                    StationsDao stationsMarketsDao = new StationsDao();
                    long res = stationsMarketsDao.insertList(response);
                    Log.i("stations: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_info_sources")) {
                    StInfoSourcesDao stInfoSourcesDao = new StInfoSourcesDao();
                    long res = stInfoSourcesDao.insertList(response);
                    Log.i("st_info_sources: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_lead_outcomes")) {
                    StLeadOutcomesDao stLeadOutcomesDao = new StLeadOutcomesDao();
                    long res = stLeadOutcomesDao.insertList(response);
                    Log.i("st_lead_outcomes: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_loans_approval_status")) {
                    StLoansApprovalStatusDao stLoansApprovalStatusDao = new StLoansApprovalStatusDao();
                    long res = stLoansApprovalStatusDao.insertList(response);
                    Log.i("loans_approval_status:", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_loans_arrears_status")) {
                    StLoansArrearsStatusDao stLoansArrearsStatusDao = new StLoansArrearsStatusDao();
                    long res = stLoansArrearsStatusDao.insertList(response);
                    Log.i("loans_arrears_status:", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_loans_assignment_status")) {
                    StLoansAssignmentStatusDao stLoansAssignmentStatusDao = new StLoansAssignmentStatusDao();
                    long res = stLoansAssignmentStatusDao.insertList(response);
                    Log.i("loan_assignment_status:", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("telcos")) {
                    TelcosDao telcosDao = new TelcosDao();
                    long res = telcosDao.insertList(response);
                    Log.i("telcos: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("products")) {
                    ProductsDao productsDao = new ProductsDao();
                    long res = productsDao.insertList(response);
                    Log.i("products: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_reason_for_default")) {
                    StReasonForDefaultDao stReasonForDefaultDao = new StReasonForDefaultDao();
                    long res = stReasonForDefaultDao.insertList(response);
                    Log.i("st_reason_for_default: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_interactions_categories")) {
                    StInteractionsCategoriesDao stInteractionsCategoriesDao = new StInteractionsCategoriesDao();
                    long res = stInteractionsCategoriesDao.insertList(response);
                    Log.i("interactions_category:", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_interactions_call_outcomes")) {
                    StInteractionsCallOutcomessDao stInteractionsCallOutcomesDao = new StInteractionsCallOutcomessDao();
                    long res = stInteractionsCallOutcomesDao.insertList(response);
                    Log.i("interactions_outcomes: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("roles")) {
                    RolesDao rolesDao = new RolesDao();
                    long res = rolesDao.insertList(response);
                    Log.i("roles: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("st_users_status")) {
                    StUsersStatusDao stUsersStatusDao = new StUsersStatusDao();
                    long res = stUsersStatusDao.insertList(response);
                    Log.i("st_users_status: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("sectors")) {
                    SectorsDao sectorsDao = new SectorsDao();
                    long res = sectorsDao.insertList(response);
                    Log.i("sectors: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("users")) {
                    UsersDao sectorsDao = new UsersDao();
                    long res = sectorsDao.insertList(response);
                    Log.i("users: ", String.valueOf(res) + " " + url);
                }

                if (table_name.equals("users_privileges")) {
                    UsersPrivilegesDao sectorsDao = new UsersPrivilegesDao();
                    long res = sectorsDao.insertList(response);
                    Log.i("users_privileges: ", String.valueOf(res) + " " + url);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(mCtx.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    mCtx.getApplicationContext().startActivity(intent);
                }
                //Log.e("Error", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                RegistrationsApiDao registrationsApiDao = new RegistrationsApiDao();
                RegistrationsApi registrationsApi = registrationsApiDao.getRegistration();
                String id = registrationsApi.getId();
                String code = registrationsApi.getVerificationCode();

                String credentials = id + ":" + code;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        MySingleton.getInstance(mCtx).addToRequestQueue(strReq);

    }
}
