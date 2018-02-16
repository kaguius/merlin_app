package ke.merlin.modules.leads;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import ke.merlin.R;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.customers.LeadsOutcomesDao;
import ke.merlin.dao.customers.StInfoSourcesDao;
import ke.merlin.dao.customers.StLeadOutcomesDao;
import ke.merlin.dao.users.UsersDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.models.customers.LeadsOutcomes;
import ke.merlin.modules.customers.CustomerDataAdapter;
import ke.merlin.modules.leads.outcomes.LeadOutcomesActivity;
import ke.merlin.services.IntentServiceReceiver;
import ke.merlin.services.LeadsIntentService;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.SessionManager;
import ke.merlin.utils.StringWithTag;

public class LeadActivity extends AppCompatActivity implements IntentServiceReceiver.Receiver {

    ProgressDialog pDialog;
    String lo, co, infosource, outcome;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
    SessionManager session;
    private RecyclerView mRecyclerView;
    private ArrayList<Customers> mArrayList;
    private LeadDataAdapter mAdapter;
    private IntentServiceReceiver mReceiver;

    String id, station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Leads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
         id = user.get(SessionManager.KEY_ID);
         station = user.get(SessionManager.KEY_STATION);

        initViews();

        if (savedInstanceState == null) {
            initializeService();
        } else {
            String values = savedInstanceState.getString("savedArray");
            mArrayList = gson.fromJson(values, new TypeToken<List<Customers>>() {
            }.getType());
            mAdapter = new LeadDataAdapter(this, mArrayList);
            mRecyclerView.setAdapter(mAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                Log.i("Dimensions: ", width + " " + height);

                final Dialog dialog = new Dialog(LeadActivity.this);
                dialog.setContentView(R.layout.leads_add); //layout for dialog
                dialog.getWindow().setLayout((7 * width)/8, (5 * height)/6);
                dialog.setTitle("Add a lead");
                dialog.setCancelable(false); //none-dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                EditText txt_phone_lds = (EditText) dialog.findViewById(R.id.txt_phone_lds);
                EditText txt_first_name_lds = (EditText) dialog.findViewById(R.id.txt_first_name_lds);
                EditText txt_last_name_lds = (EditText) dialog.findViewById(R.id.txt_last_name_lds);
                EditText etOtherSources = (EditText) dialog.findViewById(R.id.etOtherSources);
                final EditText txt_next_visit = (EditText) dialog.findViewById(R.id.leads_txt_next_visit);
                EditText txt_outcome_explanation = (EditText) dialog.findViewById(R.id.leads_txt_outcome_explanation);

                TextInputLayout lyt_phone_lds = (TextInputLayout) dialog.findViewById(R.id.lyt_phone_lds);
                TextInputLayout lyt_first_name_lds = (TextInputLayout) dialog.findViewById(R.id.lyt_first_name_lds);
                TextInputLayout lyt_last_name_lds = (TextInputLayout) dialog.findViewById(R.id.lyt_last_name_lds);
                TextInputLayout lyt_OtherSources_lds = (TextInputLayout) dialog.findViewById(R.id.lyt_OtherSources_lds);
                TextInputLayout lyt_next_visit_lds = (TextInputLayout) dialog.findViewById(R.id.lyt_next_visit_lds);
                TextInputLayout lyt_outcome_explanation_lds = (TextInputLayout) dialog.findViewById(R.id.lyt_outcome_explanation_lds);

                Spinner spInfoSources = (Spinner) dialog.findViewById(R.id.spInfoSources);
                Spinner spLoanOfficer = (Spinner) dialog.findViewById(R.id.spLoanOfficer);
                final Spinner spCollectionsOfficer = (Spinner) dialog.findViewById(R.id.spCollectionsOfficer);
                Spinner leads_sp_outcome = (Spinner) dialog.findViewById(R.id.leads_sp_outcome);


                TextView info_spinner = (TextView) dialog.findViewById(R.id.info_spinner);
                TextView lo_spinner = (TextView) dialog.findViewById(R.id.lo_spinner);
                TextView co_spinner = (TextView) dialog.findViewById(R.id.co_spinner);
                TextView leads_outcome_spinner = (TextView) dialog.findViewById(R.id.leads_outcome_spinner);

                View btn_save = dialog.findViewById(R.id.btn_save);
                View btnCancel = dialog.findViewById(R.id.btn_cancel);

                txt_phone_lds.addTextChangedListener(new MyTextWatcher(txt_phone_lds,lyt_phone_lds ));
                txt_first_name_lds.addTextChangedListener(new MyTextWatcher(txt_first_name_lds, lyt_first_name_lds));
                txt_last_name_lds.addTextChangedListener(new MyTextWatcher(txt_last_name_lds, lyt_last_name_lds));
                etOtherSources.addTextChangedListener(new MyTextWatcher(etOtherSources, lyt_OtherSources_lds));
                txt_next_visit.addTextChangedListener(new MyTextWatcher(txt_next_visit, lyt_next_visit_lds));
                txt_outcome_explanation.addTextChangedListener(new MyTextWatcher(txt_outcome_explanation, lyt_outcome_explanation_lds));

                List<StringWithTag> infosourceList = populateInfoTypesSpinner();
                ArrayAdapter<StringWithTag> infoAdapter = new ArrayAdapter<>(LeadActivity.this, android.R.layout.simple_spinner_item, infosourceList);
                infoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spInfoSources.setAdapter(infoAdapter);

                List<StringWithTag> loList = populateOfficerSpinner(station, "10");
                ArrayAdapter<StringWithTag> loAdapter = new ArrayAdapter<>(LeadActivity.this, android.R.layout.simple_spinner_item, loList);
                loAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLoanOfficer.setAdapter(loAdapter);

                List<StringWithTag> coList = populateOfficerSpinner(station, "6");
                ArrayAdapter<StringWithTag> coAdapter = new ArrayAdapter<>(LeadActivity.this, android.R.layout.simple_spinner_item, coList);
                coAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCollectionsOfficer.setAdapter(coAdapter);

                List<StringWithTag> outcomeList = populateOutcomeSpinner();
                ArrayAdapter<StringWithTag> outcomeAdapter = new ArrayAdapter<>(LeadActivity.this, android.R.layout.simple_spinner_item, outcomeList);
                outcomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                leads_sp_outcome.setAdapter(outcomeAdapter);

                //set handling event for 2 buttons and spinner
                spInfoSources.setOnItemSelectedListener(onItemSelectedListener());
                spLoanOfficer.setOnItemSelectedListener(onItemSelectedListener());
                spCollectionsOfficer.setOnItemSelectedListener(onItemSelectedListener());
                leads_sp_outcome.setOnItemSelectedListener(onItemSelectedListener());

                final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                final DatePickerDialog[] nextVisitDatePickerDialog = new DatePickerDialog[1];

                txt_next_visit.setInputType(InputType.TYPE_NULL);
                txt_next_visit.requestFocus();
                txt_next_visit.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            Calendar newCalendar = Calendar.getInstance();
                            nextVisitDatePickerDialog[0] = new DatePickerDialog(LeadActivity.this, new DatePickerDialog.OnDateSetListener() {

                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    Calendar newDate = Calendar.getInstance();
                                    newDate.set(year, monthOfYear, dayOfMonth);
                                    txt_next_visit.setText(dateFormatter.format(newDate.getTime()));
                                }

                            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                            nextVisitDatePickerDialog[0].getDatePicker().setMinDate(newCalendar.getTimeInMillis());
                            nextVisitDatePickerDialog[0].show();
                        }

                        return true;
                    }
                });


                btn_save.setOnClickListener(onConfirmListener(txt_phone_lds, txt_first_name_lds, txt_last_name_lds, etOtherSources, txt_next_visit, txt_outcome_explanation, lyt_phone_lds, lyt_first_name_lds, lyt_last_name_lds, lyt_OtherSources_lds, lyt_next_visit_lds, lyt_outcome_explanation_lds, spInfoSources, spLoanOfficer, spCollectionsOfficer, leads_sp_outcome, info_spinner, lo_spinner, co_spinner, leads_outcome_spinner, dialog));
                btnCancel.setOnClickListener(onCancelListener(dialog));

                dialog.show();

            }
        });
    }


    private List<StringWithTag> populateOutcomeSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StLeadOutcomesDao stLeadOutcomesDao = new StLeadOutcomesDao();
        HashMap<String, String> spInfosourcesMap = stLeadOutcomesDao.getSpinnerItems();

        for (Map.Entry<String, String> entry : spInfosourcesMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }


    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeadActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);

        mReceiver = new IntentServiceReceiver(new Handler());
        mReceiver.setReceiver(LeadActivity.this);
    }

    private void initializeService() {

        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(SessionManager.KEY_ID);
        String station = user.get(SessionManager.KEY_STATION);
        String role = user.get(SessionManager.KEY_ROLES);

        Intent intent = new Intent(Intent.ACTION_SYNC, null, LeadActivity.this, LeadsIntentService.class);
        intent.putExtra("id", id);
        intent.putExtra("station", station);
        intent.putExtra("role", role);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 102);
        startService(intent);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        if (mArrayList != null) {
            String values = gson.toJson(mArrayList);
            savedState.putString("savedArray", values);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mAdapter != null) mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver.setReceiver(LeadActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReceiver.setReceiver(null);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case LeadsIntentService.STATUS_RUNNING:
                pDialog = new ProgressDialog(LeadActivity.this);
                pDialog.setMessage("Processing...");
                pDialog.show();

                break;
            case LeadsIntentService.STATUS_FINISHED:
                pDialog.hide();
                String results = resultData.getString("result");
                if (results.equals("Failed")) {
                    Toast.makeText(LeadActivity.this, "Failed to get data.", Toast.LENGTH_LONG).show();
                } else if (results.equals("No contents")) {
                    Toast.makeText(LeadActivity.this, "No leads found.", Toast.LENGTH_LONG).show();
                } else {
                    mArrayList = gson.fromJson(results, new TypeToken<List<Customers>>() {}.getType());
                    mAdapter = new LeadDataAdapter(this, mArrayList);
                    mRecyclerView.setAdapter(mAdapter);
                }
                break;
            case LeadsIntentService.STATUS_ERROR:
                pDialog.hide();
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(LeadActivity.this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.spInfoSources:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            infosource = (String) swt.tag;
                           // Toast.makeText(parent.getContext(), infosource, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case R.id.leads_sp_outcome:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            outcome = (String) swt.tag;
                        }
                        break;

                    case R.id.spLoanOfficer:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            lo = (String) swt.tag;
                        }
                        break;

                    case R.id.spCollectionsOfficer:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            co = (String) swt.tag;
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
    }

    private View.OnClickListener onConfirmListener(
            final EditText txt_phone_lds, final EditText txt_first_name_lds, final EditText txt_last_name_lds, final EditText etOtherSources,
            final EditText txt_next_visit, final EditText txt_outcome_explanation,
            final TextInputLayout lyt_phone_lds, final TextInputLayout lyt_first_name_lds, final TextInputLayout lyt_last_name_lds,
            final TextInputLayout lyt_otherSources_lds, final TextInputLayout lyt_next_visit_lds, final TextInputLayout lyt_outcome_explanation_lds,
            final Spinner spInfoSources, final Spinner spLoanOfficer, final Spinner spCollectionsOfficer, final Spinner leads_sp_outcome,
            final TextView info_spinner, final TextView lo_spinner, final TextView co_spinner, final TextView leads_outcome_spinner,
            final Dialog dialog) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean cancel = false;
                View focusView = null;

                Date d1 = new Date();

                if (!validatePhone(txt_phone_lds, lyt_phone_lds)) {
                    return;
                }

                if (!validateFirstName(txt_first_name_lds, lyt_first_name_lds)) {
                    return;
                }

                if (!validateLastName(txt_last_name_lds, lyt_last_name_lds)) {
                    return;
                }

                if (!validateOutcomeExplanation(txt_outcome_explanation, lyt_outcome_explanation_lds)) {
                    return;
                }

                if (!validateNextVisitDate(txt_next_visit, lyt_next_visit_lds)) {
                    return;
                }

                if (TextUtils.isEmpty(infosource)) {
                    info_spinner.setError("");
                    focusView = info_spinner;
                    cancel = true;
                }else{
                    info_spinner.setError(null);
                }

                if (TextUtils.isEmpty(lo)) {
                    lo_spinner.setError("");
                    focusView = lo_spinner;
                    cancel = true;
                }else{
                    lo_spinner.setError(null);
                }

                if (TextUtils.isEmpty(co)) {
                    co_spinner.setError("");
                    focusView = co_spinner;
                    cancel = true;
                }else{
                    co_spinner.setError(null);
                }

                if (TextUtils.isEmpty(outcome)) {
                    leads_outcome_spinner.setError("");
                    focusView = leads_outcome_spinner;
                    cancel = true;
                }else{
                    leads_outcome_spinner.setError(null);
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    String primaryphone = txt_phone_lds.getText().toString().trim();
                    String first_name = txt_first_name_lds.getText().toString().trim();
                    String last_name = txt_last_name_lds.getText().toString().trim();
                    String other_sources = etOtherSources.getText().toString().trim();
                    String outcome_explanation = txt_outcome_explanation.getText().toString().trim();

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        d1 = df.parse(txt_next_visit.getText().toString().trim());
                    } catch (ParseException e) {}

                    String customerId = UUID.randomUUID().toString();
                    Customers leads = new Customers();
                    leads.setId(customerId);
                    leads.setFirstName(first_name);
                    leads.setLastName(last_name);
                    leads.setPrimaryPhone(Long.parseLong(primaryphone));
                    leads.setInfoSourcesId(infosource);
                    leads.setLoanOfficerId(lo);
                    leads.setCollectionsOfficerId(co);
                    leads.setOtherSources(other_sources);
                    leads.setStationsId(station);
                    leads.setCreatorId(id);
                    leads.setDeleted((byte) 0);
                    leads.setTo_create((byte) 1);
                    leads.setCreationDate(new Timestamp(new Date().getTime()));
                    leads.setUpdatedDate(new Timestamp(new Date().getTime()));

                    CustomersDao customersDao = new CustomersDao();
                    customersDao.insert(leads);

                    LeadsOutcomes leadsOutcomes = new LeadsOutcomes();
                    leadsOutcomes.setId(String.valueOf(UUID.randomUUID()));
                    leadsOutcomes.setCreatorId(id);
                    leadsOutcomes.setDeleted((byte) 0);
                    leadsOutcomes.setCreationDate(new Timestamp(new Date().getTime()));
                    leadsOutcomes.setCustomersId(customerId);
                    leadsOutcomes.setTo_create((byte) 1);
                    leadsOutcomes.setNextVisitDate(txt_next_visit.getText().toString().trim());
                    leadsOutcomes.setOutcomeExplanation(outcome_explanation);
                    leadsOutcomes.setOutcomesId(outcome);
                    leadsOutcomes.setUpdatedDate(new Timestamp(new Date().getTime()));

                    LeadsOutcomesDao leadsOutcomesDao = new LeadsOutcomesDao();
                    leadsOutcomesDao.insert(leadsOutcomes);

                    mArrayList.add(0, leads);
                    mAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                }
            }
        };
    }

    private boolean validateNextVisitDate(EditText txt_phone_lds, TextInputLayout lyt_phone_lds) {
        Date d1 = new Date();
        if(txt_phone_lds.getText().toString().trim().isEmpty()
                || txt_phone_lds.getText().toString().trim().length() < 3){
            lyt_phone_lds.setError("Enter a valid date");
            requestFocus(txt_phone_lds);
            return false;
        }else{
            String next_visit = txt_phone_lds.getText().toString().trim();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                d1 = df.parse(next_visit);
            } catch (ParseException e) {
                lyt_phone_lds.setError("Date invalid");
                requestFocus(txt_phone_lds);
                return false;
            }

            lyt_phone_lds.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateOutcomeExplanation(EditText txt_phone_lds, TextInputLayout lyt_phone_lds) {
        if(txt_phone_lds.getText().toString().trim().isEmpty()
                || txt_phone_lds.getText().toString().trim().length() < 3){
            lyt_phone_lds.setError("Enter a valid explanation");
            requestFocus(txt_phone_lds);
            return false;
        }else{
            lyt_phone_lds.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLastName(EditText txt_phone_lds, TextInputLayout lyt_phone_lds) {
        if(txt_phone_lds.getText().toString().trim().isEmpty()
                || txt_phone_lds.getText().toString().trim().length() < 3){
            lyt_phone_lds.setError("Enter a valid name");
            requestFocus(txt_phone_lds);
            return false;
        }else{
            lyt_phone_lds.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateFirstName(EditText txt_phone_lds, TextInputLayout lyt_phone_lds) {
        if(txt_phone_lds.getText().toString().trim().isEmpty()
                || txt_phone_lds.getText().toString().trim().length() < 3){
            lyt_phone_lds.setError("Enter a valid name");
            requestFocus(txt_phone_lds);
            return false;
        }else{
            lyt_phone_lds.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone(EditText txt_phone_lds, TextInputLayout lyt_phone_lds) {
        if (txt_phone_lds.getText().toString().trim().isEmpty()
                || txt_phone_lds.getText().toString().trim().length() < 12
                || txt_phone_lds.getText().toString().trim().length() > 12
                || !txt_phone_lds.getText().toString().trim().startsWith("254")) {
            lyt_phone_lds.setError("Enter a valid phone number");
            requestFocus(txt_phone_lds);
            return false;
        } else {
            CustomersDao customersDao = new CustomersDao();
            Customers leads = customersDao.getByPhone(txt_phone_lds.getText().toString().trim());
            if (leads != null){
                lyt_phone_lds.setError("Phone number already exists.");
            }else{
                lyt_phone_lds.setErrorEnabled(false);
            }
        }

        return true;
    }

    private View.OnClickListener onCancelListener(final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
    }

    private List<StringWithTag> populateInfoTypesSpinner() {

        List<StringWithTag> itemList = new ArrayList();
        itemList.add(new StringWithTag("", ""));


        StInfoSourcesDao stInfoSourcesDao = new StInfoSourcesDao();
        HashMap<String, String> spInfosourcesMap = stInfoSourcesDao.getSpinnerInfoSourcesTypes();

        for (Map.Entry<String, String> entry : spInfosourcesMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateOfficerSpinner(String station, String role) {

        List<StringWithTag> itemList = new ArrayList();
        itemList.add(new StringWithTag("", ""));

        UsersDao usersDao = new UsersDao();
        HashMap<String, String> loData = usersDao.getSpinnerData(station, role);

        for (Map.Entry<String, String> entry : loData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private EditText editText;
        private TextInputLayout inputLayout;

        private MyTextWatcher(EditText editText, TextInputLayout inputLayout) {
            this.editText = editText;
            this.inputLayout = inputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (editText.getId()) {
                case R.id.txt_phone_lds:
                    validatePhone(editText, inputLayout);
                    break;

                case R.id.txt_first_name_lds:
                    validateFirstName(editText, inputLayout);
                    break;

                case R.id.txt_last_name_lds:
                    validateLastName(editText, inputLayout);
                    break;

                case R.id.leads_txt_outcome_explanation:
                    validateOutcomeExplanation(editText, inputLayout);
                    break;
            }
        }
    }
}
