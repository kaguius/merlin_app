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
import ke.merlin.dao.customers.StHouseTypesDao;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.utils.StringWithTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalStep extends AbstractStep {
    String sitevisitid, customers_id;
    private boolean validated = false;
    int isnew;

    SiteVisitDao siteVisitDao;
    SiteVisit siteVisit;

    TextInputLayout lytHomeRent, lytHouseUtilities, lytFoodCost, lytSchoolFees, lytSavingAmount;
    EditText tvHomeRent, tvHouseUtilities, tvFoodCost, tvSchoolFees, tvSavingAmount;
    TextView tvHouseType;
    Spinner spHouseType;

    String housetype;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expenses_customer_step, container, false);

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
        spHouseType.setOnItemSelectedListener(onItemSelectedListener());
    }

    private void initViews(View v, Bundle savedInstanceState) {
        lytHomeRent = (TextInputLayout) v.findViewById(R.id.lytHomeRent);
        lytHouseUtilities = (TextInputLayout) v.findViewById(R.id.lytHouseUtilities);
        lytFoodCost = (TextInputLayout) v.findViewById(R.id.lytFoodCost);
        lytSchoolFees = (TextInputLayout) v.findViewById(R.id.lytSchoolFees);
        lytSavingAmount = (TextInputLayout) v.findViewById(R.id.lytSavingAmount);

        tvHomeRent = (EditText) v.findViewById(R.id.tvHomeRent);
        tvHouseUtilities = (EditText) v.findViewById(R.id.tvHouseUtilities);
        tvFoodCost = (EditText) v.findViewById(R.id.tvFoodCost);
        tvSchoolFees = (EditText) v.findViewById(R.id.tvSchoolFees);
        tvSavingAmount = (EditText) v.findViewById(R.id.tvSavingAmount);

        tvHouseType = (TextView) v.findViewById(R.id.tvHouseType);

        spHouseType = (Spinner) v.findViewById(R.id.spHouseType);

        try {
            siteVisitDao = new SiteVisitDao();
            siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);
            Log.i("siteVisit create", "personal " + siteVisit);

            if (siteVisit != null) {

                if (siteVisit.getHomeRent() != null) {
                    String homerent = String.valueOf(siteVisit.getHomeRent());
                    tvHomeRent.setText(homerent);
                }

                if (siteVisit.getHouseUtilities() != null) {
                    String houseutility = String.valueOf(siteVisit.getHouseUtilities());
                    tvHouseUtilities.setText(houseutility);
                }

                if (siteVisit.getFoodCost() != null) {
                    String foodcost = String.valueOf(siteVisit.getFoodCost());
                    tvFoodCost.setText(foodcost);
                }

                if (siteVisit.getSchoolFees() != null) {
                    String schoolfees = String.valueOf(siteVisit.getSchoolFees());
                    tvSchoolFees.setText(schoolfees);
                }

                if (siteVisit.getSavingAmount() != null) {
                    String savingamount = String.valueOf(siteVisit.getSavingAmount());
                    tvSavingAmount.setText(savingamount);
                }

                if (siteVisit.getHouseTypeId() != null) {
                    housetype = String.valueOf(siteVisit.getHouseTypeId());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StringWithTag> houseTypeList = populateHouseTypeSpinner();
        ArrayAdapter<StringWithTag> houseTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, houseTypeList);
        houseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHouseType.setAdapter(houseTypeAdapter);

        if (housetype != null && !housetype.isEmpty()) {
            int position = -1;
            for (int i = 0; i < houseTypeList.size(); i++) {
                if (houseTypeList.get(i).tag.toString().trim().equals(housetype)) position = i;
            }
            spHouseType.setSelection(position);
        }

    }

    private List<StringWithTag> populateHouseTypeSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StHouseTypesDao dao = new StHouseTypesDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
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

                    case R.id.spHouseType:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            housetype = (String) swt.tag;
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
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
        return "Personal";
    }

    @Override
    public boolean nextIf() {
        boolean cancel = false;
        View focusView = null;

        String homerent = tvHomeRent.getText().toString().trim();
        String houseutilities = tvHouseUtilities.getText().toString().trim();
        String foodcost = tvFoodCost.getText().toString().trim();
        String schoolfees = tvSchoolFees.getText().toString().trim();
        String savingamount = tvSavingAmount.getText().toString().trim();

        if (TextUtils.isEmpty(homerent)) {
            lytHomeRent.setError("Please enter a value");
            focusView = lytHomeRent;
            cancel = true;
        } else {
            lytHomeRent.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(houseutilities)) {
            lytHouseUtilities.setError("Please enter a value");
            focusView = lytHouseUtilities;
            cancel = true;
        } else {
            lytHouseUtilities.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(foodcost)) {
            lytFoodCost.setError("Please enter a value");
            focusView = lytFoodCost;
            cancel = true;
        } else {
            lytFoodCost.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(schoolfees)) {
            lytSchoolFees.setError("Please enter a value");
            focusView = lytSchoolFees;
            cancel = true;
        } else {
            lytSchoolFees.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(savingamount)) {
            lytSavingAmount.setError("Please enter a value");
            focusView = lytSavingAmount;
            cancel = true;
        } else {
            lytSavingAmount.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(housetype)) {
            tvHouseType.setError("");
            focusView = tvHouseType;
            cancel = true;
        } else {
            tvHouseType.setError(null);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            siteVisitDao = new SiteVisitDao();

            siteVisit = new SiteVisit();
            siteVisit.setId(sitevisitid);
            siteVisit.setHomeRent(Integer.valueOf(homerent));
            siteVisit.setHouseUtilities(Integer.valueOf(houseutilities));
            siteVisit.setFoodCost(Integer.valueOf(foodcost));
            siteVisit.setSchoolFees(Integer.valueOf(schoolfees));
            siteVisit.setSavingAmount(Integer.valueOf(savingamount));
            siteVisit.setHouseTypeId(housetype);

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
