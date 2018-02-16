package ke.merlin.modules.leads.outcomes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import ke.merlin.dao.customers.LeadsOutcomesDao;
import ke.merlin.dao.RegistrationsApiDao;
import ke.merlin.dao.customers.StLeadOutcomesDao;
import ke.merlin.models.customers.LeadsOutcomes;
import ke.merlin.models.RegistrationsApi;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.SessionManager;
import ke.merlin.utils.StringWithTag;

public class LeadOutcomesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<LeadsOutcomes> mArrayList;
    private LeadOutcomeDataAdapter mAdapter;

    SessionManager session;

    String userId, station;

    String id, first_name, last_name, outcome;

    TextView txt_lead_outcome_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_outcomes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Lead Outcomes");

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        userId = user.get(SessionManager.KEY_ID);
        station = user.get(SessionManager.KEY_STATION);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
            first_name = extras.getString("first_name");
            last_name = extras.getString("last_name");

        } else {
            id = (String) savedInstanceState.getSerializable("id");
            first_name = (String) savedInstanceState.getSerializable("first_name");
            last_name = (String) savedInstanceState.getSerializable("last_name");
        }

        initViews();

        sendRequest();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(LeadOutcomesActivity.this);
                dialog.setContentView(R.layout.leads_outcome_add); //layout for dialog
                dialog.setTitle("Add a lead outcome");
                dialog.setCancelable(false);

                final EditText txt_next_visit = (EditText) dialog.findViewById(R.id.txt_next_visit);
                EditText txt_outcome_explanation = (EditText) dialog.findViewById(R.id.txt_outcome_explanation);
                Spinner sp_outcome = (Spinner) dialog.findViewById(R.id.sp_outcome);

                TextView outcome_spinner = (TextView) dialog.findViewById(R.id.outcome_spinner);

                View btn_save_lo = dialog.findViewById(R.id.btn_save_lo);
                View btn_cancel_lo = dialog.findViewById(R.id.btn_cancel_lo);

                List<StringWithTag> outcomeList = populateOutcomeSpinner();
                ArrayAdapter<StringWithTag> infoAdapter = new ArrayAdapter<>(LeadOutcomesActivity.this, android.R.layout.simple_spinner_item, outcomeList);
                infoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_outcome.setAdapter(infoAdapter);

                sp_outcome.setOnItemSelectedListener(onItemSelectedListener());

                final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                final DatePickerDialog[] nextVisitDatePickerDialog = new DatePickerDialog[1];

                txt_next_visit.setInputType(InputType.TYPE_NULL);
                txt_next_visit.requestFocus();

                txt_next_visit.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            Calendar newCalendar = Calendar.getInstance();
                            nextVisitDatePickerDialog[0] = new DatePickerDialog(LeadOutcomesActivity.this, new DatePickerDialog.OnDateSetListener() {

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

                btn_save_lo.setOnClickListener(onConfirmListener(txt_next_visit, txt_outcome_explanation, outcome_spinner, sp_outcome, id, dialog));
                btn_cancel_lo.setOnClickListener(onCancelListener(dialog));

                dialog.show();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("id", id);
        state.putString("first_name", first_name);
        state.putString("last_name", last_name);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id = savedInstanceState.getString("id");
        first_name = savedInstanceState.getString("first_name");
        last_name = savedInstanceState.getString("last_name");
    }

    private View.OnClickListener onCancelListener(final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
    }

    private View.OnClickListener onConfirmListener(final EditText txt_next_visit, final EditText txt_outcome_explanation, final TextView outcome_spinner, Spinner sp_outcome, final String id, final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetErrors(txt_next_visit, txt_outcome_explanation);

                boolean cancel = false;
                View focusView = null;

                String next_visit = txt_next_visit.getText().toString().trim();
                String outcome_explanation = txt_outcome_explanation.getText().toString().trim();

                Log.i("next_visit: ", next_visit);


                if (TextUtils.isEmpty(next_visit)) {
                    txt_next_visit.setError("Please select next visit date.");
                    focusView = txt_next_visit;
                    cancel = true;
                }

                if (!TextUtils.isEmpty(next_visit)) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date d1 = df.parse(next_visit);
                    } catch (ParseException e) {
                        txt_next_visit.setError(e.getMessage());
                        focusView = txt_next_visit;
                        cancel = true;
                    }
                }

                if (TextUtils.isEmpty(outcome_explanation)) {
                    txt_outcome_explanation.setError("Field is required");
                    focusView = txt_outcome_explanation;
                    cancel = true;
                }else{
                    txt_outcome_explanation.setError(null);
                }

                if (TextUtils.isEmpty(outcome)) {
                    outcome_spinner.setError("");
                    focusView = outcome_spinner;
                    cancel = true;
                }else{
                    outcome_spinner.setError(null);
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {

                    LeadsOutcomes leadsOutcomes = new LeadsOutcomes();
                    leadsOutcomes.setId(String.valueOf(UUID.randomUUID()));
                    leadsOutcomes.setCreatorId(userId);
                    leadsOutcomes.setDeleted((byte) 0);
                    leadsOutcomes.setCreationDate(new Timestamp(new Date().getTime()));
                    leadsOutcomes.setCustomersId(id);
                    leadsOutcomes.setTo_create((byte) 1);
                    leadsOutcomes.setNextVisitDate(next_visit);
                    leadsOutcomes.setOutcomeExplanation(outcome_explanation);
                    leadsOutcomes.setOutcomesId(outcome);
                    leadsOutcomes.setUpdatedDate(new Timestamp(new Date().getTime()));

                    LeadsOutcomesDao leadsOutcomesDao = new LeadsOutcomesDao();
                    leadsOutcomesDao.insert(leadsOutcomes);

                    mArrayList.add(0, leadsOutcomes);
                    mAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                }
            }
        };
    }

    private void resetErrors(EditText txt_next_visit, EditText txt_outcome_explanation) {
        txt_next_visit.setError(null);
        txt_outcome_explanation.setError(null);
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

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.sp_outcome:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            outcome = (String) swt.tag;
                            Toast.makeText(parent.getContext(), outcome, Toast.LENGTH_LONG).show();
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
    }


    private void initViews() {

        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeadOutcomesActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);

        txt_lead_outcome_id = (TextView) findViewById(R.id.txt_lead_outcome_id);
        txt_lead_outcome_id.setText(" Viewing lead outcomes for " + first_name + " " + last_name);
    }

    private void sendRequest() {

        final ProgressDialog pDialog = new ProgressDialog(LeadOutcomesActivity.this);
        pDialog.setMessage("Processing...");
        pDialog.show();

        LeadsOutcomesDao leadsOutcomesDao = new LeadsOutcomesDao();
        mArrayList = leadsOutcomesDao.getOutcomesByCustomerId(id);
        mAdapter = new LeadOutcomeDataAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        pDialog.hide();
    }
}

