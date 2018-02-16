package ke.merlin.modules.business.siteVisitSteppers;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.merlin.R;
import ke.merlin.dao.business.StBusinessCyclesDao;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.utils.StringWithTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeStep extends AbstractStep {

    String sitevisitid, customers_id;
    private boolean validated = false;
    int isnew;

    TextInputLayout lytCurrentStockValue, lytSalesValue, lytStockValue, lytIncomeExplanation, lytRestockingFrequency;
    EditText tvCurrentStockValue, tvSalesValue, tvStockValue, tvIncomeExplanation, tvRestockingFrequency;
    TextView tvBusinessCycle_W;
    Spinner spBusinessCycle_W;

    SiteVisitDao siteVisitDao;
    SiteVisit siteVisit;

    String busCycle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_income_step, container, false);

        if (savedInstanceState == null) {
            sitevisitid = getArguments().getString("sitevisitid");
            customers_id = getArguments().getString("customers_id");
            isnew = getArguments().getInt("isnew");
        } else {
            sitevisitid = (String) savedInstanceState.getSerializable("sitevisitid");
            customers_id = (String) savedInstanceState.getSerializable("customers_id");
            isnew = (int) savedInstanceState.getSerializable("isnew");
        }

        initViews(v, savedInstanceState);

        setListeners();

        return v;
    }

    private void setListeners() {
        spBusinessCycle_W.setOnItemSelectedListener(onItemSelectedListener());

        tvIncomeExplanation.addTextChangedListener(new MyTextWatcher(tvIncomeExplanation));

    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.spBusinessCycle_W:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            busCycle = (String) swt.tag;
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
    }

    private void initViews(View v, Bundle savedInstanceState) {
        lytCurrentStockValue = (TextInputLayout) v.findViewById(R.id.lytCurrentStockValue);
        lytSalesValue = (TextInputLayout) v.findViewById(R.id.lytSalesValue);
        lytStockValue = (TextInputLayout) v.findViewById(R.id.lytStockValue);
        lytIncomeExplanation = (TextInputLayout) v.findViewById(R.id.lytIncomeExplanation);
        lytRestockingFrequency = (TextInputLayout) v.findViewById(R.id.lytRestockingFrequency);

        tvCurrentStockValue = (EditText) v.findViewById(R.id.tvCurrentStockValue);
        tvSalesValue = (EditText) v.findViewById(R.id.tvSalesValue);
        tvStockValue = (EditText) v.findViewById(R.id.tvStockValue);
        tvIncomeExplanation = (EditText) v.findViewById(R.id.tvIncomeExplanation);
        tvRestockingFrequency = (EditText) v.findViewById(R.id.tvRestockingFrequency);

        tvBusinessCycle_W = (TextView) v.findViewById(R.id.tvBusinessCycle_W);

        spBusinessCycle_W = (Spinner) v.findViewById(R.id.spBusinessCycle_W);

        try {
            siteVisitDao = new SiteVisitDao();
            siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);
            Log.i("siteVisit create", "income " + siteVisit);

            if (siteVisit != null) {

                if (siteVisit.getCurrentStockValue() != null) {
                    String stockvalue = String.valueOf(siteVisit.getCurrentStockValue());
                    tvCurrentStockValue.setText(stockvalue);
                }

                if (siteVisit.getSalesPerCycle() != null) {
                    String salesvalue = String.valueOf(siteVisit.getSalesPerCycle());
                    tvSalesValue.setText(salesvalue);
                }

                if (siteVisit.getSpendOnStock() != null) {
                    String spendonstock = String.valueOf(siteVisit.getSpendOnStock());
                    tvStockValue.setText(spendonstock);
                }

                if (siteVisit.getIncomeExplanation() != null) {
                    String incomeexplanation = String.valueOf(siteVisit.getIncomeExplanation());
                    tvIncomeExplanation.setText(incomeexplanation);
                }

                if (siteVisit.getCycleRestockingFrequency() != null) {
                    String restockingfreq = String.valueOf(siteVisit.getCycleRestockingFrequency());
                    tvRestockingFrequency.setText(restockingfreq);
                }

                busCycle = siteVisit.getBusinessCycleId();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        List<StringWithTag> busCycleList = populateBusinessCycleSpinner();
        ArrayAdapter<StringWithTag> busCycleAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, busCycleList);
        busCycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBusinessCycle_W.setAdapter(busCycleAdapter);

        if (busCycle != null && !busCycle.isEmpty()) {
            int position = -1;
            for (int i = 0; i < busCycleList.size(); i++) {
                if (busCycleList.get(i).tag.toString().trim().equals(busCycle)) position = i;
            }
            spBusinessCycle_W.setSelection(position);
        }

    }

    private List<StringWithTag> populateBusinessCycleSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StBusinessCyclesDao dao = new StBusinessCyclesDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

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
        state.putString("sitevisitid", sitevisitid);
        state.putString("customers_id", customers_id);
        state.putInt("isnew", isnew);
    }

    @Override
    public void onStepVisible() {
        sitevisitid = getArguments().getString("sitevisitid");
        customers_id = getArguments().getString("customers_id");
        isnew = getArguments().getInt("isnew");
    }

    @Override
    public String name() {
        return "Income";
    }

    @Override
    public boolean nextIf() {

        boolean cancel = false;
        View focusView = null;

        String stockvalue = tvCurrentStockValue.getText().toString().trim();
        String salesvalue = tvSalesValue.getText().toString().trim();
        String spendonstock = tvStockValue.getText().toString().trim();
        String incomeexplanation = tvIncomeExplanation.getText().toString().trim();
        String restockingfreq = tvRestockingFrequency.getText().toString().trim();

        if (!validatetvIncomeExplanation()) {
            return false;
        }

        if (TextUtils.isEmpty(busCycle)) {
            tvBusinessCycle_W.setError("");
            focusView = tvBusinessCycle_W;
            cancel = true;
        } else {
            tvBusinessCycle_W.setError(null);
        }

        if (TextUtils.isEmpty(stockvalue)) {
            lytCurrentStockValue.setError("");
            focusView = tvCurrentStockValue;
            cancel = true;
        } else {
            lytCurrentStockValue.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(salesvalue)) {
            tvSalesValue.setError("");
            focusView = tvSalesValue;
            cancel = true;
        } else {
            tvSalesValue.setError(null);
        }

        if (TextUtils.isEmpty(spendonstock)) {
            tvStockValue.setError("");
            focusView = tvStockValue;
            cancel = true;
        } else {
            tvStockValue.setError(null);
        }

        if (TextUtils.isEmpty(restockingfreq)) {
            tvRestockingFrequency.setError("");
            focusView = tvRestockingFrequency;
            cancel = true;
        } else {
            tvRestockingFrequency.setError(null);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            siteVisitDao = new SiteVisitDao();

            siteVisit = new SiteVisit();
            siteVisit.setId(sitevisitid);
            siteVisit.setBusinessCycleId(busCycle);
            siteVisit.setCurrentStockValue(Integer.valueOf(stockvalue));
            siteVisit.setSalesPerCycle(Integer.valueOf(salesvalue));
            siteVisit.setSpendOnStock(Integer.valueOf(spendonstock));
            siteVisit.setIncomeExplanation(incomeexplanation);
            siteVisit.setCycleRestockingFrequency(Integer.valueOf(restockingfreq));

            siteVisitDao.update(siteVisit);

            validated = true;
        }

        return validated;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
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
                case R.id.tvIncomeExplanation:
                    validatetvIncomeExplanation();
                    break;
            }
        }
    }

    private boolean validatetvIncomeExplanation() {
        if (tvIncomeExplanation.getText().toString().trim().isEmpty()
                || tvIncomeExplanation.getText().toString().trim().length() < 40) {
            lytIncomeExplanation.setError("Address must have a minimum of 40 characters.");
            requestFocus(tvIncomeExplanation);
            return false;
        } else {
            lytIncomeExplanation.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
