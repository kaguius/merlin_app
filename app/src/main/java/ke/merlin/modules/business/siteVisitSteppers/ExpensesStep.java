package ke.merlin.modules.business.siteVisitSteppers;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ke.merlin.dao.business.StNatureOfEmploymentDao;
import ke.merlin.dao.business.StNoOfEmployeesRangeDao;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.utils.StringWithTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesStep extends AbstractStep {

    String sitevisitid, customers_id;
    private boolean validated = false;
    int isnew;

    SiteVisitDao siteVisitDao;
    SiteVisit siteVisit;

    TextInputLayout lytBusinessRent, lytBusinessUtilities, lytEmployeeSalary, lytLicensingFee, lytStorageFee, lytTransportFee;
    EditText tvBusinessRent, tvBusinessUtilities, tvEmployeeSalary, tvLicensingFee, tvStorageFee, tvTransportFee;
    TextView tvNoOfEmployees_W, tvNatureOffEmployment_W;
    Spinner spNoOfEmployees_W, spNatureOffEmployment_W;

    String noofemployees, natureofemployees;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expenses_step, container, false);

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
        spNoOfEmployees_W.setOnItemSelectedListener(onItemSelectedListener());
        spNatureOffEmployment_W.setOnItemSelectedListener(onItemSelectedListener());

    }

    private void initViews(View v, Bundle savedInstanceState) {
        lytBusinessRent = (TextInputLayout) v.findViewById(R.id.lytBusinessRent);
        lytBusinessUtilities = (TextInputLayout) v.findViewById(R.id.lytBusinessUtilities);
        lytEmployeeSalary = (TextInputLayout) v.findViewById(R.id.lytEmployeeSalary);
        lytLicensingFee = (TextInputLayout) v.findViewById(R.id.lytLicensingFee);
        lytStorageFee = (TextInputLayout) v.findViewById(R.id.lytStorageFee);
        lytTransportFee = (TextInputLayout) v.findViewById(R.id.lytTransportFee);

        tvBusinessRent = (EditText) v.findViewById(R.id.tvBusinessRent);
        tvBusinessUtilities = (EditText) v.findViewById(R.id.tvBusinessUtilities);
        tvEmployeeSalary = (EditText) v.findViewById(R.id.tvEmployeeSalary);
        tvLicensingFee = (EditText) v.findViewById(R.id.tvLicensingFee);
        tvStorageFee = (EditText) v.findViewById(R.id.tvStorageFee);
        tvTransportFee = (EditText) v.findViewById(R.id.tvTransportFee);

        tvNoOfEmployees_W = (TextView) v.findViewById(R.id.tvNoOfEmployees_W);
        tvNatureOffEmployment_W = (TextView) v.findViewById(R.id.tvNatureOffEmployment_W);

        spNoOfEmployees_W = (Spinner) v.findViewById(R.id.spNoOfEmployees_W);
        spNatureOffEmployment_W = (Spinner) v.findViewById(R.id.spNatureOffEmployment_W);

        try {
            siteVisitDao = new SiteVisitDao();
            siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);
            Log.i("siteVisit create", "expenses " + siteVisit);

            if (siteVisit != null) {

                if (siteVisit.getBusinessRent() != null) {
                    String busrent = String.valueOf(siteVisit.getBusinessRent());
                    tvBusinessRent.setText(busrent);
                }

                if (siteVisit.getBusinessUtilities() != null) {
                    String busutility = String.valueOf(siteVisit.getBusinessUtilities());
                    tvBusinessUtilities.setText(busutility);
                }

                if (siteVisit.getEmployeesSalary() != null) {
                    String employeesal = String.valueOf(siteVisit.getEmployeesSalary());
                    tvEmployeeSalary.setText(employeesal);
                }

                if (siteVisit.getLicensingFee() != null) {
                    String licensingfee = String.valueOf(siteVisit.getLicensingFee());
                    tvLicensingFee.setText(licensingfee);
                }

                if (siteVisit.getStorageFee() != null) {
                    String storagefee = String.valueOf(siteVisit.getStorageFee());
                    tvStorageFee.setText(storagefee);
                }

                if (siteVisit.getTransportFee() != null) {
                    String transportfee = String.valueOf(siteVisit.getTransportFee());
                    tvTransportFee.setText(transportfee);
                }

                if (siteVisit.getNoOfEmployeesTypesId() != null) {
                    noofemployees = String.valueOf(siteVisit.getNoOfEmployeesTypesId());
                }

                if (siteVisit.getNatureOfEmploymentTypesId() != null) {
                    natureofemployees = String.valueOf(siteVisit.getNatureOfEmploymentTypesId());
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StringWithTag> noofEmployeesList = populateNoofEmployeesSpinner();
        ArrayAdapter<StringWithTag> noofEmployeesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, noofEmployeesList);
        noofEmployeesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNoOfEmployees_W.setAdapter(noofEmployeesAdapter);

        if (noofemployees != null && !noofemployees.isEmpty()) {
            int position = -1;
            for (int i = 0; i < noofEmployeesList.size(); i++) {
                if (noofEmployeesList.get(i).tag.toString().trim().equals(noofemployees))
                    position = i;
            }
            spNoOfEmployees_W.setSelection(position);
        }

        List<StringWithTag> natureOffEmploymentList = populateNatureOffEmploymentSpinner();
        ArrayAdapter<StringWithTag> natureOffEmploymentAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, natureOffEmploymentList);
        natureOffEmploymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNatureOffEmployment_W.setAdapter(natureOffEmploymentAdapter);

        if (natureofemployees != null && !natureofemployees.isEmpty()) {
            int position = -1;
            for (int i = 0; i < natureOffEmploymentList.size(); i++) {
                if (natureOffEmploymentList.get(i).tag.toString().trim().equals(natureofemployees))
                    position = i;
            }
            spNatureOffEmployment_W.setSelection(position);
        }

    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.spNoOfEmployees_W:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            noofemployees = (String) swt.tag;
                        }
                        break;

                    case R.id.spNatureOffEmployment_W:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            natureofemployees = (String) swt.tag;
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
    }

    private List<StringWithTag> populateNatureOffEmploymentSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StNatureOfEmploymentDao dao = new StNatureOfEmploymentDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateNoofEmployeesSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StNoOfEmployeesRangeDao dao = new StNoOfEmployeesRangeDao();
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
        return "Expenses";
    }

    @Override
    public boolean nextIf() {

        boolean cancel = false;
        View focusView = null;

        String busrent = tvBusinessRent.getText().toString().trim();
        String busutility = tvBusinessUtilities.getText().toString().trim();
        String employeesalary = tvEmployeeSalary.getText().toString().trim();
        String licensingfee = tvLicensingFee.getText().toString().trim();
        String storagefee = tvStorageFee.getText().toString().trim();
        String transportfee = tvTransportFee.getText().toString().trim();

        if (TextUtils.isEmpty(noofemployees)) {
            tvNoOfEmployees_W.setError("");
            focusView = tvNoOfEmployees_W;
            cancel = true;
        } else {
            tvNoOfEmployees_W.setError(null);
        }

        if (TextUtils.isEmpty(natureofemployees)) {
            tvNatureOffEmployment_W.setError("");
            focusView = tvNatureOffEmployment_W;
            cancel = true;
        } else {
            tvNatureOffEmployment_W.setError(null);
        }

        if (TextUtils.isEmpty(busrent)) {
            lytBusinessRent.setError("Please enter a value");
            focusView = lytBusinessRent;
            cancel = true;
        } else {
            lytBusinessRent.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(busutility)) {
            lytBusinessUtilities.setError("Please enter a value");
            focusView = lytBusinessUtilities;
            cancel = true;
        } else {
            lytBusinessUtilities.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(employeesalary)) {
            lytEmployeeSalary.setError("Please enter a value");
            focusView = lytEmployeeSalary;
            cancel = true;
        } else {
            lytEmployeeSalary.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(licensingfee)) {
            lytLicensingFee.setError("Please enter a value");
            focusView = lytLicensingFee;
            cancel = true;
        } else {
            lytLicensingFee.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(storagefee)) {
            lytStorageFee.setError("Please enter a value");
            focusView = lytStorageFee;
            cancel = true;
        } else {
            lytStorageFee.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(transportfee)) {
            lytTransportFee.setError("Please enter a value");
            focusView = lytTransportFee;
            cancel = true;
        } else {
            lytTransportFee.setErrorEnabled(false);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            siteVisitDao = new SiteVisitDao();

            siteVisit = new SiteVisit();
            siteVisit.setId(sitevisitid);
            siteVisit.setBusinessRent(Integer.valueOf(busrent));
            siteVisit.setBusinessUtilities(Integer.valueOf(busutility));
            siteVisit.setEmployeesSalary(Integer.valueOf(employeesalary));
            siteVisit.setLicensingFee(Integer.valueOf(licensingfee));
            siteVisit.setStorageFee(Integer.valueOf(storagefee));
            siteVisit.setTransportFee(Integer.valueOf(transportfee));
            siteVisit.setNoOfEmployeesTypesId(noofemployees);
            siteVisit.setNatureOfEmploymentTypesId(natureofemployees);

            siteVisitDao.update(siteVisit);

            validated = true;

        }

        return validated;
    }

    @Override
    public String error() {
        return "<b>There' an error!</b> <small>please check!</small>";
    }

}
