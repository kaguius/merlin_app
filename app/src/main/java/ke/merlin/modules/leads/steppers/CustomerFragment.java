package ke.merlin.modules.leads.steppers;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.merlin.R;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.customers.StCustomerTitlesDao;
import ke.merlin.dao.customers.StGendersDao;
import ke.merlin.dao.customers.StLeadOutcomesDao;
import ke.merlin.dao.stations.StationsMarketsDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.models.customers.StCustomerTitles;
import ke.merlin.modules.leads.LeadActivity;
import ke.merlin.utils.MyDateTypeAdapter;
import ke.merlin.utils.SessionManager;
import ke.merlin.utils.StringWithTag;

/**
 * Created by mecmurimi on 29/08/2017.
 */

public class CustomerFragment extends AbstractStep {

    TextInputLayout lyt_idno_cv, lyt_firstname_cv, lyt_lastname_cv, lyt_alsoknownas_cv,
            lyt_prim_phone_cv, lyt_dis_phone_cv, lyt_alt_phone_cv;

    EditText txt_idno_cv, txt_firstname_cv, txt_lastname_cv, txt_alsoknownas_cv,
            txt_prim_phone_cv, txt_dis_phone_cv, txt_alt_phone_cv;

    TextView tv_gender_cv, tv_title_cv, tv_market_cv;
    Spinner sp_gender_cv, sp_title_cv, sp_market_cv;
    CheckBox cb_asset_list_cv;
    String id;
    SessionManager session;
    String gender, title, market;
    String user_id, station;
    int asset = 0;
    CustomersDao customersDao = new CustomersDao();
    private boolean validated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.steps_customer, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(SessionManager.KEY_ID);
        station = user.get(SessionManager.KEY_STATION);

        if (savedInstanceState == null) {
            id = getArguments().getString("id");
        } else {
            id = (String) savedInstanceState.getSerializable("id");
        }

        initViews(v);

        Log.i("customer id: ", id);

        setListeners();

        return v;
    }

    private void setListeners() {
        txt_firstname_cv.addTextChangedListener(new MyTextWatcher(txt_firstname_cv));
        txt_lastname_cv.addTextChangedListener(new MyTextWatcher(txt_lastname_cv));
        txt_alsoknownas_cv.addTextChangedListener(new MyTextWatcher(txt_alsoknownas_cv));
        txt_dis_phone_cv.addTextChangedListener(new MyTextWatcher(txt_dis_phone_cv));
        txt_alt_phone_cv.addTextChangedListener(new MyTextWatcher(txt_alt_phone_cv));

        //set handling event for 2 buttons and spinner
        sp_gender_cv.setOnItemSelectedListener(onItemSelectedListener());
        sp_title_cv.setOnItemSelectedListener(onItemSelectedListener());
        sp_market_cv.setOnItemSelectedListener(onItemSelectedListener());

    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.sp_gender_cv:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            gender = (String) swt.tag;
                        }
                        break;

                    case R.id.sp_title_cv:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            title = (String) swt.tag;
                        }
                        break;

                    case R.id.sp_market_cv:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            market = (String) swt.tag;
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
    }

    private void initViews(View v) {
        lyt_firstname_cv = (TextInputLayout) v.findViewById(R.id.lyt_firstname_cv);
        lyt_lastname_cv = (TextInputLayout) v.findViewById(R.id.lyt_lastname_cv);
        lyt_alsoknownas_cv = (TextInputLayout) v.findViewById(R.id.lyt_alsoknownas_cv);
        lyt_prim_phone_cv = (TextInputLayout) v.findViewById(R.id.lyt_prim_phone_cv);
        lyt_dis_phone_cv = (TextInputLayout) v.findViewById(R.id.lyt_dis_phone_cv);
        lyt_alt_phone_cv = (TextInputLayout) v.findViewById(R.id.lyt_alt_phone_cv);

        txt_firstname_cv = (EditText) v.findViewById(R.id.txt_firstname_cv);
        txt_lastname_cv = (EditText) v.findViewById(R.id.txt_lastname_cv);
        txt_alsoknownas_cv = (EditText) v.findViewById(R.id.txt_alsoknownas_cv);
        txt_prim_phone_cv = (EditText) v.findViewById(R.id.txt_prim_phone_cv);
        txt_dis_phone_cv = (EditText) v.findViewById(R.id.txt_dis_phone_cv);
        txt_alt_phone_cv = (EditText) v.findViewById(R.id.txt_alt_phone_cv);

        tv_gender_cv = (TextView) v.findViewById(R.id.tv_gender_cv);
        tv_title_cv = (TextView) v.findViewById(R.id.tv_title_cv);
        tv_market_cv = (TextView) v.findViewById(R.id.tv_market_cv);

        sp_gender_cv = (Spinner) v.findViewById(R.id.sp_gender_cv);
        sp_title_cv = (Spinner) v.findViewById(R.id.sp_title_cv);
        sp_market_cv = (Spinner) v.findViewById(R.id.sp_market_cv);

        cb_asset_list_cv = (CheckBox) v.findViewById(R.id.cb_asset_list_cv);

        txt_prim_phone_cv.setEnabled(false);

        try {
            Customers customers = customersDao.getCustomerById(id);

            gender = customers.getGenderId();
            title = customers.getTitleId();
            market = customers.getMarketId();

            String firstName = customers.getFirstName();
            String lastName = customers.getLastName();
            String phone = String.valueOf(customers.getPrimaryPhone());
            String alsoKnownAs = customers.getAlsoKnownAs();
            if (customers.getAlternativePhone() != null) {
                String alternativePhone = String.valueOf(customers.getAlternativePhone());
                txt_alt_phone_cv.setText(alternativePhone);
            }
            if (customers.getDisbursePhone() != null) {
                String disbursePhone = String.valueOf(customers.getDisbursePhone());
                txt_dis_phone_cv.setText(disbursePhone);
            }

            if (customers.getAssetList() != null) {
                long assetList = customers.getAssetList();
                cb_asset_list_cv.setChecked(assetList == 1);
            }

            txt_firstname_cv.setText(firstName);
            txt_lastname_cv.setText(lastName);
            txt_prim_phone_cv.setText(phone);
            txt_alsoknownas_cv.setText(alsoKnownAs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StringWithTag> genderList = populateGenderSpinner();
        ArrayAdapter<StringWithTag> genderAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gender_cv.setAdapter(genderAdapter);

        if (gender != null && !gender.isEmpty()) {
            int position = -1;
            for (int i = 0; i < genderList.size(); i++) {
                if (genderList.get(i).tag.toString().trim().equals(gender)) position = i;
            }

            sp_gender_cv.setSelection(position);
        }

        List<StringWithTag> titleList = populateTitleSpinner();
        ArrayAdapter<StringWithTag> titleAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, titleList);
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_title_cv.setAdapter(titleAdapter);

        if (title != null && !title.isEmpty()) {
            int position = -1;
            for (int i = 0; i < titleList.size(); i++) {
                if (titleList.get(i).tag.toString().trim().equals(title)) position = i;
            }

            sp_title_cv.setSelection(position);
        }

        List<StringWithTag> marketList = populateMarketSpinner(station);
        ArrayAdapter<StringWithTag> marketAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, marketList);
        marketAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_market_cv.setAdapter(marketAdapter);

        if (market != null && !market.isEmpty()) {
            int position = -1;
            for (int i = 0; i < marketList.size(); i++) {
                if (marketList.get(i).tag.toString().trim().equals(market)) position = i;
            }

            sp_market_cv.setSelection(position);
        }


    }

    private List<StringWithTag> populateGenderSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StGendersDao dao = new StGendersDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateTitleSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StCustomerTitlesDao dao = new StCustomerTitlesDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateMarketSpinner(String station) {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StationsMarketsDao dao = new StationsMarketsDao();
        HashMap<String, String> listMap = dao.getSpinnerItems(station);

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("id", id);
    }

    @Override
    public String name() {
        return "Biodata";
    }

    @Override
    public boolean nextIf() {
        boolean cancel = false;
        View focusView = null;

        if (!validatetxt_firstname_cv()) {
            return false;
        }

        if (!validatetxt_lastname_cv()) {
            return false;
        }

        if (!validatetxt_dis_phone_cv()) {
            return false;
        }

        if (TextUtils.isEmpty(gender)) {
            tv_gender_cv.setError("");
            focusView = tv_gender_cv;
            cancel = true;
        } else {
            tv_gender_cv.setError(null);
        }

        if (TextUtils.isEmpty(title)) {
            tv_title_cv.setError("");
            focusView = tv_title_cv;
            cancel = true;
        } else {
            tv_title_cv.setError(null);
        }

        if (TextUtils.isEmpty(market)) {
            tv_market_cv.setError("");
            focusView = tv_market_cv;
            cancel = true;
        } else {
            tv_market_cv.setError(null);
        }

        if (cb_asset_list_cv.isChecked()) {
            asset = 1;
        }

        Log.i("asset: ", " " + asset);

        if (cancel) {
            focusView.requestFocus();
        } else {

            String first_name = txt_firstname_cv.getText().toString().trim();
            String last_name = txt_lastname_cv.getText().toString().trim();
            String also_known_as = txt_alsoknownas_cv.getText().toString().trim();
            String dis_phone = txt_dis_phone_cv.getText().toString().trim();
            String alt_phone = txt_alt_phone_cv.getText().toString().trim();

            Customers customers = customersDao.getCustomerById(id);
            customers.setGenderId(gender);
            customers.setTitleId(title);
            customers.setMarketId(market);
            customers.setAssetList((byte) asset);
            customers.setFirstName(first_name);
            customers.setLastName(last_name);
            customers.setAlsoKnownAs(also_known_as);
            customers.setDisbursePhone(Long.valueOf(dis_phone));
            if (!alt_phone.isEmpty())
            customers.setAlternativePhone(Long.valueOf(alt_phone));
            customersDao.update(customers);

            validated = true;
        }

        return validated;
    }

    @Override
    public void onStepVisible() {
        id = getArguments().getString("id");
    }

    @Override
    public String error() {
        return "<b>There an error!</b> <small>Check!</small>";
    }

    private boolean validatetxt_alt_phone_cv() {
        if (txt_alt_phone_cv.getText().toString().trim().isEmpty()
                || txt_alt_phone_cv.getText().toString().trim().length() < 12
                || txt_alt_phone_cv.getText().toString().trim().length() > 12
                || !txt_alt_phone_cv.getText().toString().trim().startsWith("254")) {
            lyt_alt_phone_cv.setError("Enter a valid name");
            requestFocus(txt_alt_phone_cv);
            return false;
        } else {
            lyt_alt_phone_cv.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatetxt_dis_phone_cv() {
        if (txt_dis_phone_cv.getText().toString().trim().isEmpty()
                || txt_dis_phone_cv.getText().toString().trim().length() < 12
                || txt_dis_phone_cv.getText().toString().trim().length() > 12
                || !txt_dis_phone_cv.getText().toString().trim().startsWith("254")) {
            lyt_dis_phone_cv.setError("Enter a valid phone number");
            requestFocus(txt_dis_phone_cv);
            return false;
        } else {
            Customers customers = customersDao.getByDisPhone(txt_dis_phone_cv.getText().toString().trim());
            if (customers != null) {
                lyt_dis_phone_cv.setError("Number already exists.");
            }
            lyt_dis_phone_cv.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatetxt_alsoknownas_cv() {
        if (txt_alsoknownas_cv.getText().toString().trim().isEmpty()) {
            lyt_alsoknownas_cv.setError("Enter a valid name");
            requestFocus(txt_alsoknownas_cv);
            return false;
        } else {
            lyt_alsoknownas_cv.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatetxt_lastname_cv() {
        if (txt_lastname_cv.getText().toString().trim().isEmpty()
                || txt_lastname_cv.getText().toString().trim().length() < 3) {
            lyt_lastname_cv.setError("Enter a valid name");
            requestFocus(txt_lastname_cv);
            return false;
        } else {
            lyt_lastname_cv.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatetxt_firstname_cv() {
        if (txt_firstname_cv.getText().toString().trim().isEmpty()
                || txt_firstname_cv.getText().toString().trim().length() < 3) {
            lyt_firstname_cv.setError("Enter a valid name");
            requestFocus(txt_firstname_cv);
            return false;
        } else {
            lyt_firstname_cv.setErrorEnabled(false);
        }

        return true;

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txt_firstname_cv:
                    validatetxt_firstname_cv();
                    break;
                case R.id.txt_lastname_cv:
                    validatetxt_lastname_cv();
                    break;
                case R.id.txt_alsoknownas_cv:
                    validatetxt_alsoknownas_cv();
                    break;
                case R.id.txt_dis_phone_cv:
                    validatetxt_dis_phone_cv();
                    break;
                case R.id.txt_alt_phone_cv:
                    validatetxt_alt_phone_cv();
                    break;
            }
        }
    }
}
