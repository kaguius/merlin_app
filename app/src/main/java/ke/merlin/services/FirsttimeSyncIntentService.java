package ke.merlin.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import ke.merlin.models.Last_sync;
import ke.merlin.models.RegistrationsApi;
import ke.merlin.utils.UrlConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by mecmurimi on 31/07/2017.
 */

public class FirsttimeSyncIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "Firsttime Service";

    OkHttpClient client;

    Last_syncDao last_syncDao;

    SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");

    public FirsttimeSyncIntentService() {
        super(FirsttimeSyncIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Login Sync Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        Bundle bundle = new Bundle();

        /* Update UI: Download Service is Running */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);

        try {

            if (checkIfHasNetwork()) {
                RegistrationsApiDao registrationsApiDao = new RegistrationsApiDao();
                RegistrationsApi registrationsApi = registrationsApiDao.getRegistration();
                String regid = registrationsApi.getId();
                String code = registrationsApi.getVerificationCode();

                String credentials = regid + ":" + code;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                //Static Business
                syncBusinessCategories(auth);
                syncBusinessCycles(auth);
                syncStBusinessLocations(auth);
                syncBusinessTypes(auth);
                syncNatureofEmployment(auth);
                syncNoofEmployeesrange(auth);

                //Static Customers
                syncChamaCycles(auth);
                syncCreditOrganisation(auth);
                syncCreditOrganisationTypes(auth);
                syncCustomerActiveStatus(auth);
                syncCustomerApprovalStatus(auth);
                syncCustomerState(auth);
                syncCustomerTitles(auth);
                syncEducationLevels(auth);
                syncGender(auth);
                syncHomeOwnerships(auth);
                syncHouseTypes(auth);
                syncInfoSources(auth);
                syncLanguages(auth);
                syncStLeadOutcomes(auth);
                syncMaritalStatus(auth);
                syncRefereeRelationships(auth);


                //Static Loans
                syncProducts(auth);
                syncInteractionsCategories(auth);
                syncInteractionCallOutcomes(auth);
                syncLoanApprovalStatus(auth);
                syncLoanArrearsStatus(auth);
                syncLoanAssignmentStatus(auth);
                syncReasonforDefault(auth);
                syncTelcos(auth);
                syncTelcosShortcodes(auth);

                //static Stations
                syncSectors(auth);
                syncStations(auth);
                syncStationMarkets(auth);
                syncStationsTargets(auth);


                //static Users
                syncRoles(auth);
                syncUserStatus(auth);
                syncUserPrivileges(auth);
                syncPrivileges(auth);

                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH);

                for (int yr = 2013; yr <= year; yr++) {
                    int maxMonth = 12;
                    if(yr == year){
                        maxMonth = month;
                    }
                    for (int mn = 1; mn <= maxMonth; mn++) {
                        int febMax = 28;
                        if (((yr % 4 == 0) && (yr % 100 != 0)) || (yr % 400 == 0)) {
                            febMax = 29;
                        }

                        String fromDate = yr + "-01-01", toDate = yr + "01-31";

                        if (mn == 1) {
                            fromDate = yr + "-01-01";
                            toDate = yr + "-01-31";
                        } else if (mn == 2) {
                            fromDate = yr + "-02-01";
                            toDate = yr + "-02-" + febMax;
                        } else if (mn == 3) {
                            fromDate = yr + "-03-01";
                            toDate = yr + "-03-31";
                        } else if (mn == 4) {
                            fromDate = yr + "-04-01";
                            toDate = yr + "-04-30";
                        } else if (mn == 5) {
                            fromDate = yr + "-05-01";
                            toDate = yr + "-05-31";
                        } else if (mn == 6) {
                            fromDate = yr + "-06-01";
                            toDate = yr + "-06-30";
                        } else if (mn == 7) {
                            fromDate = yr + "-07-01";
                            toDate = yr + "-07-31";
                        } else if (mn == 8) {
                            fromDate = yr + "-08-01";
                            toDate = yr + "-08-31";
                        } else if (mn == 9) {
                            fromDate = yr + "-09-01";
                            toDate = yr + "-09-30";
                        } else if (mn == 10) {
                            fromDate = yr + "-10-01";
                            toDate = yr + "-10-31";
                        } else if (mn == 11) {
                            fromDate = yr + "-11-01";
                            toDate = yr + "-11-30";
                        } else if (mn == 12) {
                            fromDate = yr + "-12-01";
                            toDate = yr + "-12-31";
                        }

                        Log.i("Start & end date", fromDate + " " + toDate);

                        //sitevisit
                        //syncSiteVisit(auth, fromDate, toDate);

                        //business
                       // syncBusiness(auth, fromDate, toDate);
                       // syncBusinessAssessment(auth, fromDate, toDate);
                       // syncBusinessExpenses(auth, fromDate, toDate);
                       // syncBusinessIncomes(auth, fromDate, toDate);
                      //  syncBusinessLocations(auth, fromDate, toDate);

                        //Customers
                        syncCustomers(auth, fromDate, toDate);
                        //syncCustomerChamas(auth, fromDate, toDate);
                        //syncCustomerDependants(auth, fromDate, toDate);
                       // syncCustomerExpenses(auth, fromDate, toDate);
                        //syncCustomersReferees(auth, fromDate, toDate);
                        //syncCustomersSocial(auth, fromDate, toDate);
                        syncLeadOutcomes(auth, fromDate, toDate);
                       // syncCustomersLoanLimit(auth, fromDate, toDate);

                        //Loans
                        //syncLoans(auth, fromDate, toDate);
                       // syncLoansApplications(auth, fromDate, toDate);
                       // syncLoansOverpayments(auth, fromDate, toDate);
                       // syncLoansPenalty(auth, fromDate, toDate);
                       // syncLoansWaiver(auth, fromDate, toDate);
                       // syncLoansRepayments(auth, fromDate, toDate);
                       // syncLoansBfc(auth, fromDate, toDate);
                       // syncLoansInteractions(auth, fromDate, toDate);

                        //Sms
                       // syncSmsOutgoing(auth, fromDate, toDate);
                       // syncSmsIncoming(auth, fromDate, toDate);

                        //Suspense
                        //syncSuspensePayments(auth, fromDate, toDate);

                    }
                }

                //Users
                syncUsers(auth);

                Log.d(TAG, "Service Completed!");

                bundle.putString("result", "success");
                receiver.send(STATUS_FINISHED, bundle);

            } else {
                intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                if (isCallable(intent)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent1 = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
            }
        } catch (Exception e) {
            /* Sending error message back to activity */
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, "An error occurred");
            receiver.send(STATUS_ERROR, bundle);
        }

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();

    }

    private void syncSuspensePayments(String auth, String fromDate, String toDate) {
        String url = UrlConstants.SuspensePayments_URL + fromDate + "/" + toDate ;
        String table_name = "suspense_payments";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

         client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                SuspensePaymentsDao suspensePaymentsDao = new SuspensePaymentsDao();
                suspensePaymentsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncCustomersLoanLimit(String auth, String fromDate, String toDate) {
        String url = UrlConstants.CustomersLoanLimit_URL + fromDate + "/" + toDate ;
        String table_name = "customers_loan_limit";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                CustomersLoanLimitDao customersLoanLimitDao = new CustomersLoanLimitDao();
                customersLoanLimitDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncSiteVisit(String auth, String fromDate, String toDate) {
        String url = UrlConstants.SiteVisit_URL + fromDate + "/" + toDate ;
        String table_name = "site_visit";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                SiteVisitDao siteVisitDao = new SiteVisitDao();
                siteVisitDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncPrivileges(String auth) {
        String url = UrlConstants.Privileges_URL;
        final String table_name = "privileges";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                PrivilegesDao privilegesDao = new PrivilegesDao();
                privilegesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncStationsTargets(String auth) {
        String url = UrlConstants.StStationsTargets_URL;
        final String table_name = "stations_targets";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StationsTargetsDao stationsTargetsDao = new StationsTargetsDao();
                stationsTargetsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncTelcosShortcodes(String auth) {
        String url = UrlConstants.StTelcosShortcodes_URL;
        final String table_name = "telcos_shortcodes";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

         client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                    Log.i(table_name, responseBody.substring(0, 20));

                TelcosShortcodeDao telcosShortcodeDao = new TelcosShortcodeDao();
                telcosShortcodeDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncSmsIncoming(String auth, String fromDate, String toDate) {
        String url = UrlConstants.SmsIncoming_URL + fromDate + "/" + toDate ;
        String table_name = "sms_incoming";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                SmsIncomingDao smsIncomingDao = new SmsIncomingDao();
                smsIncomingDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncSmsOutgoing(String auth, String fromDate, String toDate) {
        String url = UrlConstants.SmsOutgoing_URL + fromDate + "/" + toDate ;
        String table_name = "sms_outgoing";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                SmsOutgoingDao smsOutgoingDao = new SmsOutgoingDao();
                smsOutgoingDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncLoansInteractions(String auth, String fromDate, String toDate) {
        String url = UrlConstants.LoansInteractions_URL + fromDate + "/" + toDate ;
        String table_name = "loans_interactions";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LoansInteractionsDao loansInteractionsDao = new LoansInteractionsDao();
                loansInteractionsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncLoansBfc(String auth, String fromDate, String toDate) {
        String url = UrlConstants.LoansBfc_URL + fromDate + "/" + toDate ;
        String table_name = "loans_bfc";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LoansBfcDao loansBfcDao = new LoansBfcDao();
                loansBfcDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncLoansRepayments(String auth, String fromDate, String toDate) {
        String url = UrlConstants.LoansRepayments_URL + fromDate + "/" + toDate ;
        String table_name = "loans_repayments";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LoansRepaymentsDao loansRepaymentsDao = new LoansRepaymentsDao();
                loansRepaymentsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void syncLoansWaiver(String auth, String fromDate, String toDate) {
        String url = UrlConstants.LoansWaiver_URL + fromDate + "/" + toDate ;
        String table_name = "loans_waivers";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LoansWaiversDao loansWaiversDao = new LoansWaiversDao();
                loansWaiversDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncLoansPenalty(String auth, String fromDate, String toDate) {
        String url = UrlConstants.LoansPenalty_URL + fromDate + "/" + toDate ;
        String table_name = "loans_penalties";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LoansPenaltiesDao loansPenaltiesDao = new LoansPenaltiesDao();
                loansPenaltiesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncLoansOverpayments(String auth, String fromDate, String toDate) {
        String url = UrlConstants.LoansOverpayments_URL + fromDate + "/" + toDate ;
        String table_name = "loans_overpayments";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LoansOverpaymentsDao loansOverpaymentsDao = new LoansOverpaymentsDao();
                loansOverpaymentsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncLoansApplications(String auth, String fromDate, String toDate) {
        String url = UrlConstants.LoansApplications_URL + fromDate + "/" + toDate ;
        final String table_name = "loans_applications";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();



        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LoansApplicationsDao loansApplicationsDao = new LoansApplicationsDao();
                loansApplicationsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncLoans(String auth, String fromDate, String toDate) {
        String url = UrlConstants.Loans_URL + fromDate + "/" + toDate ;
        final String table_name = "loans";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();


        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LoansDao loansDao = new LoansDao();
                loansDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncBusinessLocations(String auth, String fromDate, String toDate) {
        String url = UrlConstants.BusinessLocations_URL + fromDate + "/" + toDate ;
        final String table_name = "business_location";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);


        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                BusinessLocationDao businessLocationDao = new BusinessLocationDao();
                businessLocationDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncBusinessIncomes(String auth, String fromDate, String toDate) {
        String url = UrlConstants.BusinessIncomes_URL + fromDate + "/" + toDate ;
        final String table_name = "business_incomes";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                BusinessIncomesDao businessIncomesDao = new BusinessIncomesDao();
                businessIncomesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncBusinessExpenses(String auth, String fromDate, String toDate) {
        String url = UrlConstants.BusinessExpenses_URL + fromDate + "/" + toDate ;
        final String table_name = "business_expenses";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                BusinessExpensesDao businessExpensesDao = new BusinessExpensesDao();
                businessExpensesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncBusinessAssessment(String auth, String fromDate, String toDate) {
        String url = UrlConstants.BusinessAssessment_URL + fromDate + "/" + toDate ;
        final String table_name = "business_assessment";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                BusinessAssessmentDao businessAssessmentDao = new BusinessAssessmentDao();
                businessAssessmentDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncBusiness(String auth, String fromDate, String toDate) {
        String url = UrlConstants.Business_URL + fromDate + "/" + toDate ;
        final String table_name = "business";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                BusinessDao businessDao = new BusinessDao();
                businessDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void syncCustomersSocial(String auth, String fromDate, String toDate) {
        String url = UrlConstants.CustomersSocial_URL + fromDate + "/" + toDate ;
        final String table_name = "customers_socials";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                CustomersSocialsDao customersSocialsDao = new CustomersSocialsDao();
                customersSocialsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncCustomersReferees(String auth, String fromDate, String toDate) {
        String url = UrlConstants.CustomersReferees_URL + fromDate + "/" + toDate ;
        final String table_name = "customers_referees";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                CustomersRefereesDao customersRefereesDao = new CustomersRefereesDao();
                customersRefereesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncCustomerExpenses(String auth, String fromDate, String toDate) {
        String url = UrlConstants.CustomerExpenses_URL + fromDate + "/" + toDate ;
        final String table_name = "customers_expenses";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                CustomersExpensesDao customersExpensesDao = new CustomersExpensesDao();
                customersExpensesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncCustomerDependants(String auth, String fromDate, String toDate) {
        String url = UrlConstants.CustomerDependants_URL + fromDate + "/" + toDate ;
        final String table_name = "customers_dependants";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                CustomersDependantsDao customersDependantsDao = new CustomersDependantsDao();
                customersDependantsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncCustomerChamas(String auth, String fromDate, String toDate) {
        String url = UrlConstants.CustomerChamas_URL + fromDate + "/" + toDate ;
        final String table_name = "customers_chamas";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                CustomersChamasDao customersChamasDao = new CustomersChamasDao();
                customersChamasDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncCustomers(String auth, String fromDate, String toDate) {
        String url = UrlConstants.Customers_URL + fromDate + "/" + toDate ;
        final String table_name = "customers";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                CustomersDao customersDao = new CustomersDao();
                customersDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncLeadOutcomes(String auth, String fromDate, String toDate) {
        String url = UrlConstants.LeadOutcomes_URL + fromDate + "/" + toDate ;
        final String table_name = "leads_outcomes";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, fromDate, toDate, 1);


        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                Log.i(table_name, String.valueOf(response.code()));
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                LeadsOutcomesDao leadsOutcomesDao = new LeadsOutcomesDao();
                leadsOutcomesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void syncUserPrivileges(String auth) {
        String url = UrlConstants.UserPrivileges_URL;
        final String table_name = "users_privileges";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                UsersPrivilegesDao usersPrivilegesDao = new UsersPrivilegesDao();
                usersPrivilegesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncUsers(String auth) {
        String url = UrlConstants.Users_URL;
        final String table_name = "users";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                UsersDao usersDao = new UsersDao();
                usersDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncLanguages(final String auth) {
        String url = UrlConstants.StLanguages_URL;
        final String table_name = "st_languages";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));
                StLanguagesDao stLanguagesDao = new StLanguagesDao();
                stLanguagesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncBusinessCategories(final String auth) {
        String url = UrlConstants.StBusinessCategories_URL;
        final String table_name = "st_business_categories";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                    String responseBody = response.body().string();
                    Log.i(table_name, responseBody.substring(0, 20));
                    StBusinessCategoriesDao stBusinessCategoriesDao = new StBusinessCategoriesDao();
                    stBusinessCategoriesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncBusinessTypes(final String auth) {
        String url = UrlConstants.StBusinessTypes_URL;
        final String table_name = "st_business_types";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StBusinessTypesDao stBusinessTypesDao = new StBusinessTypesDao();
                stBusinessTypesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncNatureofEmployment(final String auth) {
        String url = UrlConstants.StNatureofEmployment_URL;
        final String table_name = "st_nature_of_employment";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StNatureOfEmploymentDao stNatureOfEmploymentDao = new StNatureOfEmploymentDao();
                stNatureOfEmploymentDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncNoofEmployeesrange(final String auth) {
        String url = UrlConstants.StNoofEmployeesrange_URL;
        final String table_name = "st_no_of_employees";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StNoOfEmployeesRangeDao stNoOfEmployeesRangeDao = new StNoOfEmployeesRangeDao();
                stNoOfEmployeesRangeDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncBusinessCycles(final String auth) {
        String url = UrlConstants.StBusinessCycles_URL;
        final String table_name = "st_business_cycles";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                    String responseBody = response.body().string();
                    Log.i(table_name, responseBody.substring(0, 20));

                    StBusinessCyclesDao stBusinessCyclesDao = new StBusinessCyclesDao();
                    stBusinessCyclesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncStBusinessLocations(final String auth) {
        String url = UrlConstants.StBusinessLocations_URL;
        final String table_name = "st_business_locations";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StBusinessLocationsDao stBusinessLocationsDao = new StBusinessLocationsDao();
                stBusinessLocationsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncGender(final String auth) {
        String url = UrlConstants.StGender_URL;
        final String table_name = "st_genders";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StGendersDao stGendersDao = new StGendersDao();
                stGendersDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncCustomerTitles(final String auth) {
        String url = UrlConstants.StCustomerTitles_URL;
        final String table_name = "st_customer_titles";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 50));

                StCustomerTitlesDao stCustomerTitlesDao = new StCustomerTitlesDao();
                stCustomerTitlesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncChamaCycles(final String auth) {
        String url = UrlConstants.StChamaCycles_URL;
        final String table_name = "st_chama_cycles";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 50));

                StChamaCyclesDao stChamaCyclesDao = new StChamaCyclesDao();
                stChamaCyclesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncHouseTypes(final String auth) {
        String url = UrlConstants.StHouseTypes_URL;
        final String table_name = "st_house_types";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StHouseTypesDao stHouseTypesDao = new StHouseTypesDao();
                stHouseTypesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncHomeOwnerships(final String auth) {
        String url = UrlConstants.StHomeOwnerships_URL;
        final String table_name = "st_home_ownerships";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StHomeOwnershipsDao stHomeOwnershipsDao = new StHomeOwnershipsDao();
                stHomeOwnershipsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncRefereeRelationships(final String auth) {
        String url = UrlConstants.StRefereeRelationships_URL;
        final String table_name = "st_referees_relationship";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StRefereesRelationshipDao stRefereesRelationshipDao = new StRefereesRelationshipDao();
                stRefereesRelationshipDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncCreditOrganisationTypes(final String auth) {
        String url = UrlConstants.StCreditOrganisationTypes_URL;
        final String table_name = "st_credit_organisations_types";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StCreditOrganisationsTypesDao stCreditOrganisationsTypesDao = new StCreditOrganisationsTypesDao();
                stCreditOrganisationsTypesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncCreditOrganisation(final String auth) {
        String url = UrlConstants.StCreditOrganisation_URL;
        final String table_name = "st_credit_organisations";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StCreditOrganisationsDao stCreditOrganisationsDao = new StCreditOrganisationsDao();
                stCreditOrganisationsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncEducationLevels(final String auth) {
        String url = UrlConstants.StEducationLevels_URL;
        final String table_name = "st_education_levels";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StEducationLevelsDao stEducationLevelsDao = new StEducationLevelsDao();
                stEducationLevelsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncCustomerActiveStatus(final String auth) {
        String url = UrlConstants.StCustomerActiveStatus_URL;
        final String table_name = "st_customer_active_status";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StCustomerActiveStatusDao stCustomerActiveStatusDao = new StCustomerActiveStatusDao();
                stCustomerActiveStatusDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncCustomerApprovalStatus(final String auth) {
        String url = UrlConstants.StCustomerApprovalStatus_URL;
        final String table_name = "st_customer_approval_status";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StCustomerApprovalStatusDao stCustomerApprovalStatusDao = new StCustomerApprovalStatusDao();
                stCustomerApprovalStatusDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncCustomerState(final String auth) {
        String url = UrlConstants.StCustomerState_URL;
        final String table_name = "st_customer_state";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StCustomerStateDao stCustomerStateDao = new StCustomerStateDao();
                stCustomerStateDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncMaritalStatus(final String auth) {
        String url = UrlConstants.StMaritalStatus_URL;
        final String table_name = "st_marital_status";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StMaritalStatusDao stMaritalStatusDao = new StMaritalStatusDao();
                stMaritalStatusDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncStationMarkets(final String auth) {
        String url = UrlConstants.StStationMarkets_URL;
        final String table_name = "stations_markets";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StationsMarketsDao stationsMarketsDao = new StationsMarketsDao();
                stationsMarketsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncStations(final String auth) {
        String url = UrlConstants.StStations_URL;
        final String table_name = "stations";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StationsDao stationsDao = new StationsDao();
                stationsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncInfoSources(final String auth) {
        String url = UrlConstants.StInfoSources_URL;
        final String table_name = "st_info_sources";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StInfoSourcesDao stInfoSourcesDao = new StInfoSourcesDao();
                stInfoSourcesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncStLeadOutcomes(final String auth) {
        String url = UrlConstants.StLeadOutcomes_URL;
        final String table_name = "st_lead_outcomes";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StLeadOutcomesDao stLeadOutcomesDao = new StLeadOutcomesDao();
                stLeadOutcomesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncLoanApprovalStatus(final String auth) {
        String url = UrlConstants.StLoanApprovalStatus_URL;
        final String table_name = "st_loans_approval_status";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StLoansApprovalStatusDao stLoansApprovalStatusDao = new StLoansApprovalStatusDao();
                stLoansApprovalStatusDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncLoanArrearsStatus(final String auth) {
        String url = UrlConstants.StLoanArrearsStatus_URL;
        final String table_name = "st_loans_arrears_status";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StLoansArrearsStatusDao stLoansArrearsStatusDao = new StLoansArrearsStatusDao();
                stLoansArrearsStatusDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncLoanAssignmentStatus(final String auth) {
        String url = UrlConstants.StLoanAssignmentStatus_URL;
        final String table_name = "st_loans_assignment_status";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StLoansAssignmentStatusDao stLoansAssignmentStatusDao = new StLoansAssignmentStatusDao();
                stLoansAssignmentStatusDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncTelcos(final String auth) {
        String url = UrlConstants.StTelcos_URL;
        final String table_name = "telcos";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                TelcosDao telcosDao = new TelcosDao();
                telcosDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncProducts(String auth) {
        String url = UrlConstants.StProducts_URL;
        final String table_name = "products";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                ProductsDao productsDao = new ProductsDao();
                productsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncReasonforDefault(String auth) {
        String url = UrlConstants.StReasonforDefault_URL;
        final String table_name = "st_reason_for_default";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StReasonForDefaultDao stReasonForDefaultDao = new StReasonForDefaultDao();
                stReasonForDefaultDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncInteractionsCategories(String auth) {
        String url = UrlConstants.StInteractionsCategories_URL;
        final String table_name = "st_interactions_categories";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StInteractionsCategoriesDao stInteractionsCategoriesDao = new StInteractionsCategoriesDao();
                stInteractionsCategoriesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncInteractionCallOutcomes(String auth) {
        String url = UrlConstants.StInteractionCallOutcomes_URL;
        final String table_name = "st_interactions_call_outcomes";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StInteractionsCallOutcomessDao stInteractionsCallOutcomessDao = new StInteractionsCallOutcomessDao();
                stInteractionsCallOutcomessDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncRoles(String auth) {
        String url = UrlConstants.Roles_URL;
        final String table_name = "roles";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                RolesDao rolesDao = new RolesDao();
                rolesDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncUserStatus(String auth) {
        String url = UrlConstants.StUserStatus_URL;
        final String table_name = "st_users_status";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                StUsersStatusDao stUsersStatusDao = new StUsersStatusDao();
                stUsersStatusDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncSectors(String auth) {
        String url = UrlConstants.StSectors_URL;
        final String table_name = "sectors";

        last_syncDao = new Last_syncDao();
        Last_sync last_sync = new Last_sync(table_name, "2013-01-01", sdt.format(new Date()), 1);

        client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("authorization", auth).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 204) {
                if (response.code() == 200) {
                String responseBody = response.body().string();
                Log.i(table_name, responseBody.substring(0, 20));

                SectorsDao sectorsDao = new SectorsDao();
                sectorsDao.insertList(responseBody);
                }
                last_syncDao.insert(last_sync);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}