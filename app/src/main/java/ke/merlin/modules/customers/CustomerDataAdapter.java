package ke.merlin.modules.customers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ke.merlin.R;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.customers.StCustomerTitlesDao;
import ke.merlin.dao.customers.StGendersDao;
import ke.merlin.dao.loans.ProductsDao;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.models.loans.LoansApplications;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.modules.business.BusinessDetailsActivity;
import ke.merlin.modules.business.BusinessStepperActivity;
import ke.merlin.modules.loans.LoansActivity;
import ke.merlin.modules.sms.SmsActivity;
import ke.merlin.utils.AuthDetails;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.SessionManager;
import ke.merlin.utils.StringWithTag;
import ke.merlin.utils.UrlConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by mecmurimi on 24/08/2017.
 */

public class CustomerDataAdapter extends RecyclerView.Adapter<CustomerDataAdapter.ViewHolder> implements Filterable {
    final String[] product = {""};
    final String[] amount = {"0"};
    String sitevisitid;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
    private ArrayList<Customers> mArrayList;
    private ArrayList<Customers> mFilteredList;
    private StCustomerTitlesDao stCustomerTitlesDao;
    private StGendersDao stGendersDao;
    private Activity activity;

    SiteVisitDao siteVisitDao;

    public CustomerDataAdapter(Activity activity, ArrayList<Customers> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        stCustomerTitlesDao = new StCustomerTitlesDao();
        stGendersDao = new StGendersDao();
        this.activity = activity;
    }

    @Override
    public CustomerDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_customers, viewGroup, false);
        return new CustomerDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerDataAdapter.ViewHolder viewHolder, int i) {

        final String id = mFilteredList.get(i).getId();
        final String dis_phone = String.valueOf(mFilteredList.get(i).getDisbursePhone());

        viewHolder.txt_customer_lst.setText(stCustomerTitlesDao.getCustomerTitleById(mFilteredList.get(i).getTitleId()).getName() + " " + mFilteredList.get(i).getFirstName() + " " + mFilteredList.get(i).getLastName());
        viewHolder.txt_idNo_lst.setText("IdNo: " + mFilteredList.get(i).getNationalId());
        viewHolder.txt_mobile_lst.setText("Mobile: " + mFilteredList.get(i).getPrimaryPhone());
        viewHolder.txt_gender_lst.setText("Gender: " + stGendersDao.getCustomerGenderById(mFilteredList.get(i).getGenderId()).getName());
        viewHolder.txt_loan_limit_lst.setText("Marital Status: " + mFilteredList.get(i).getMaritalStatusId());

        viewHolder.img_add_bus_lst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sitevisitid;

                siteVisitDao = new SiteVisitDao();
                SiteVisit siteVisit = siteVisitDao.getLatestNonCompleteSiteVisit(id);
                if (siteVisit != null && siteVisit.getIsCompleted() == 0) {
                    sitevisitid = siteVisit.getId();
                } else {
                    sitevisitid = UUID.randomUUID().toString();
                    siteVisit = new SiteVisit();
                    siteVisit.setId(sitevisitid);
                    siteVisit.setTo_create((byte) 1);
                    siteVisit.setIsReset((byte) 0);
                    siteVisit.setCustomersId(id);
                    siteVisit.setIsCompleted((byte) 0);
                    siteVisit.setCreationDate(new Timestamp(new Date().getTime()));
                    siteVisit.setUpdatedDate(new Timestamp(new Date().getTime()));

                    siteVisitDao.insert(siteVisit);
                }

                Intent i = new Intent(activity, BusinessStepperActivity.class);
                i.putExtra("id", id);
                i.putExtra("sitevisitid", sitevisitid);
                activity.startActivity(i);

            }
        });


        viewHolder.lyt_add_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkIfHasNetwork()) {

                    DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;

                    final Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.add_loan);
                    dialog.getWindow().setLayout((7 * width) / 8, (5 * height) / 6);
                    dialog.setTitle("Create Loan");
                    dialog.setCancelable(false);

                    EditText txtdis_phone = (EditText) dialog.findViewById(R.id.txt_dis_phone_l);
                    txtdis_phone.setText(dis_phone);
                    txtdis_phone.setEnabled(false);

                    Spinner spProduct = (Spinner) dialog.findViewById(R.id.spProduct);
                    Spinner spLoanAmount = (Spinner) dialog.findViewById(R.id.spLoanAmount);

                    TextView tvProduct = (TextView) dialog.findViewById(R.id.tvProduct);
                    TextView tvLoanAmount = (TextView) dialog.findViewById(R.id.tvLoanAmount);
                    TextView tvaffordability = (TextView) dialog.findViewById(R.id.tvaffordability);

                    getAffordabilty(id, dialog);

                    List<StringWithTag> productList = populateProductsSpinner();
                    ArrayAdapter<StringWithTag> productAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, productList);
                    productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProduct.setAdapter(productAdapter);

                    spProduct.setOnItemSelectedListener(onItemSelectedListener());
                    spLoanAmount.setOnItemSelectedListener(onItemSelectedListener());

                    Log.i("spinner items ", amount[0] + " " + product[0]);

                    View btn_save = dialog.findViewById(R.id.btn_save_l);
                    View btnCancel = dialog.findViewById(R.id.btn_cancel_l);

                    btn_save.setOnClickListener(onConfirmListener(id, tvProduct, tvLoanAmount, tvaffordability, dialog));
                    btnCancel.setOnClickListener(onCancelListener(dialog));

                    dialog.show();

                } else {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    if (isCallable(intent)) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    } else {
                        Intent intent1 = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent1);
                    }
                }

            }

            private AdapterView.OnItemSelectedListener onItemSelectedListener() {
                return new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                        switch (parent.getId()) {

                            case R.id.spProduct:
                                if (id != 0) {
                                    StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                                    product[0] = (String) swt.tag;
                                }
                                break;

                            case R.id.spLoanAmount:
                                if (id != 0) {
                                    amount[0] = parent.getItemAtPosition(pos).toString();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView parent) {

                    }
                };
            }


        });

        viewHolder.lyt_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, CustomerDetailsActivity.class);
                i.putExtra("id", id);
                activity.startActivity(i);

            }
        });

        viewHolder.img_sitevisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, BusinessDetailsActivity.class);
                i.putExtra("id", id);
                activity.startActivity(i);

            }
        });

        viewHolder.img_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, SmsActivity.class);
                i.putExtra("id", id);
                activity.startActivity(i);

            }
        });

        viewHolder.img_loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, LoansActivity.class);
                i.putExtra("id", id);
                activity.startActivity(i);

            }
        });

    }

    private View.OnClickListener onConfirmListener(final String id, final TextView tvProduct, final TextView tvLoanAmount, final TextView tvaffordability, final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(product[0])) {
                    tvProduct.setError("");
                    focusView = tvProduct;
                    cancel = true;
                } else {
                    tvProduct.setError(null);
                }

                if (TextUtils.isEmpty(amount[0]) || amount[0].equals("0")) {
                    tvLoanAmount.setError("");
                    focusView = tvLoanAmount;
                    cancel = true;
                } else {
                    tvLoanAmount.setError(null);
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    CustomersDao customersDao = new CustomersDao();
                    Customers customers = customersDao.getCustomerById(id);

                    String disphone = String.valueOf(customers.getDisbursePhone());

                    String carrier_code = "1";

                    if (disphone.startsWith("25470") || disphone.startsWith("25471") || disphone.startsWith("25472")
                            || disphone.startsWith("25479") || disphone.startsWith("25474")) {
                        carrier_code = "1";
                    } else if (disphone.startsWith("25473") || disphone.startsWith("25478")) {
                        carrier_code = "2";
                    }

                    SessionManager session = new SessionManager(activity.getApplicationContext());
                    session.checkLogin();
                    HashMap<String, String> user = session.getUserDetails();
                    String userid = user.get(SessionManager.KEY_ID);
                    Log.i("userid ", userid);

                    LoansApplications loansApplications = new LoansApplications();
                    loansApplications.setId(String.valueOf(UUID.randomUUID()));
                    loansApplications.setCustomersId(customers.getId());
                    loansApplications.setMobile(customers.getDisbursePhone());
                    loansApplications.setAmount(Double.parseDouble(amount[0]));
                    loansApplications.setProductId(product[0]);
                    loansApplications.setLoanOfficerId(customers.getLoanOfficerId());
                    loansApplications.setCollectionsOfficerId(customers.getCollectionsOfficerId());
                    loansApplications.setStationId(customers.getStationsId());
                    loansApplications.setTelcosId(carrier_code);
                    loansApplications.setApprovalStatus("1");
                    loansApplications.setSiteVisitId(sitevisitid);
                    loansApplications.setCreatorId(userid);
                    loansApplications.setCreationDate(new Timestamp(new Date().getTime()));
                    loansApplications.setUpdatedDate(new Timestamp(new Date().getTime()));


                    if (checkIfHasNetwork()) {

                        createLoanApplication(loansApplications);

                    } else {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        if (isCallable(intent)) {
                            activity.startActivity(intent);
                        } else {
                            activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        }
                    }

                    amount[0] = "0";
                    product[0] = "";

                    dialog.dismiss();
                }

            }
        };

    }

    private void createLoanApplication(LoansApplications loansApplications) {
        MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();

        final String requestBody = gson.toJson(loansApplications);
        Log.i("requestBody ", requestBody);
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(UrlConstants.Create_LoansApplications_URL)
                .post(body)
                .addHeader("authorization", AuthDetails.getAuth())
                .addHeader("content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,  res, Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, res, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private View.OnClickListener onCancelListener(final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount[0] = "0";
                product[0] = "";
                dialog.dismiss();
            }
        };
    }

    private void getAffordabilty(String id, final Dialog dialog) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(UrlConstants.SiteVisit_Latest_URL + id)
                .addHeader("authorization", AuthDetails.getAuth())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                final List<Integer> list = new ArrayList<>();
                list.add(0);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Spinner spLoanAmount = (Spinner) dialog.findViewById(R.id.spLoanAmount);

                        TextView tvaffordability = (TextView) dialog.findViewById(R.id.tvaffordability);
                        tvaffordability.setText("Customer Affordability:  0");

                        ArrayAdapter<Integer> affordabilityAdaptor = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, list);
                        affordabilityAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spLoanAmount.setAdapter(affordabilityAdaptor);
                    }
                });

                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        String responseBody = response.body().string();
                        Log.i(" responseBody ", responseBody.substring(0, 10));

                        SiteVisit siteVisit = gson.fromJson(responseBody, SiteVisit.class);
                        int affordability = siteVisit.getAffordability();

                        sitevisitid = siteVisit.getId();

                        final List<Integer> list = new ArrayList<>();
                        list.add(0);

                        if (affordability >= 5000) {

                            if (affordability > 50000) {
                                affordability = 50000;
                            }

                            for (int i = 5000; i <= affordability; i += 2500) {
                                list.add(i);
                            }
                        }

                        Log.i("affordability ", " is " + String.valueOf(affordability));

                        final int finalAffordability = affordability;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Spinner spLoanAmount = (Spinner) dialog.findViewById(R.id.spLoanAmount);

                                TextView tvaffordability = (TextView) dialog.findViewById(R.id.tvaffordability);
                                tvaffordability.setText("Customer Affordability: " + finalAffordability);

                                ArrayAdapter<Integer> affordabilityAdaptor = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, list);
                                affordabilityAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spLoanAmount.setAdapter(affordabilityAdaptor);
                            }
                        });

                    } else {

                        final List<Integer> list = new ArrayList<>();
                        list.add(0);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Spinner spLoanAmount = (Spinner) dialog.findViewById(R.id.spLoanAmount);

                                TextView tvaffordability = (TextView) dialog.findViewById(R.id.tvaffordability);
                                tvaffordability.setText("Customer Affordability: 0");

                                ArrayAdapter<Integer> affordabilityAdaptor = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, list);
                                affordabilityAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spLoanAmount.setAdapter(affordabilityAdaptor);
                            }
                        });
                    }
                } else {

                    final List<Integer> list = new ArrayList<>();
                    list.add(0);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Spinner spLoanAmount = (Spinner) dialog.findViewById(R.id.spLoanAmount);

                            TextView tvaffordability = (TextView) dialog.findViewById(R.id.tvaffordability);
                            tvaffordability.setText("Customer Affordability: 0");

                            ArrayAdapter<Integer> affordabilityAdaptor = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, list);
                            affordabilityAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spLoanAmount.setAdapter(affordabilityAdaptor);
                        }
                    });
                }

            }

        });
    }

    private List<StringWithTag> populateProductsSpinner() {

        List<StringWithTag> itemList = new ArrayList();
        itemList.add(new StringWithTag("", ""));


        ProductsDao dao = new ProductsDao();
        HashMap<String, String> listMap = dao.getSpinnerTypes();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Customers> filteredList = new ArrayList<>();

                    for (Customers customers : mArrayList) {

                        if (customers.getNationalId().toLowerCase().contains(charString) || customers.getFirstName().toLowerCase().contains(charString)
                                || customers.getLastName().toLowerCase().contains(charString) || String.valueOf(customers.getPrimaryPhone()).toLowerCase().contains(charString)) {

                            filteredList.add(customers);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Customers>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_add_bus_lst, img_create_loan, img_sitevisit, img_sms, img_loans;
        private TextView txt_customer_lst, txt_idNo_lst, txt_mobile_lst, txt_gender_lst, txt_loan_limit_lst;
        LinearLayout lyt_add_loan, lyt_customer;

        public ViewHolder(View view) {
            super(view);

            txt_customer_lst = (TextView) view.findViewById(R.id.txt_customer_lst);
            txt_idNo_lst = (TextView) view.findViewById(R.id.txt_idNo_lst);
            txt_mobile_lst = (TextView) view.findViewById(R.id.txt_mobile_lst);
            txt_gender_lst = (TextView) view.findViewById(R.id.txt_gender_lst);
            txt_loan_limit_lst = (TextView) view.findViewById(R.id.txt_loan_limit_lst);
            img_add_bus_lst = (ImageView) view.findViewById(R.id.img_add_bus_lst);
            img_create_loan = (ImageView) view.findViewById(R.id.img_create_loan);
            img_sitevisit = (ImageView) view.findViewById(R.id.img_sitevisit);
            img_sms = (ImageView) view.findViewById(R.id.img_sms);
            img_loans = (ImageView) view.findViewById(R.id.img_loans);
            lyt_add_loan = (LinearLayout) view.findViewById(R.id.lyt_add_loan);
            lyt_customer = (LinearLayout) view.findViewById(R.id.lyt_customer);

        }
    }
}