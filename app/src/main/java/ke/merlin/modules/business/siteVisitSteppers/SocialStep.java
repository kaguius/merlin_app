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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.merlin.R;
import ke.merlin.dao.customers.StCreditOrganisationsTypesDao;
import ke.merlin.dao.customers.StEducationLevelsDao;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.utils.StringWithTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialStep extends AbstractStep {

    String sitevisitid, customers_id;
    private boolean validated = false;
    int isnew;

    SiteVisitDao siteVisitDao;
    SiteVisit siteVisit;

    TextInputLayout lytLoanBalance, lytAverageDailyCustomers;
    EditText tvLoanBalance, tvAverageDailyCustomers;
    TextView tvAccountOrg_W, tv_evertaken_loan, tv_other_creditors, tv_current_loan_org, tv_education_level;
    Spinner spAccountOrg_W, sp_loan_organisation, sp_other_creditors, sp_current_loan_org, sp_education_level;
    CheckBox has_account, ever_taken_loan, have_credit_access, ch_current_loan, ch_employed;

    String accountOrg_W, loan_organisation, other_creditors, loaning_org, educationlevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social_step, container, false);

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
        spAccountOrg_W.setOnItemSelectedListener(onItemSelectedListener());
        sp_loan_organisation.setOnItemSelectedListener(onItemSelectedListener());
        sp_other_creditors.setOnItemSelectedListener(onItemSelectedListener());
        sp_current_loan_org.setOnItemSelectedListener(onItemSelectedListener());
        sp_education_level.setOnItemSelectedListener(onItemSelectedListener());
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.spAccountOrg_W:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            accountOrg_W = (String) swt.tag;
                        }
                        break;

                    case R.id.sp_loan_organisation:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            loan_organisation = (String) swt.tag;
                        }
                        break;

                    case R.id.sp_other_creditors:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            other_creditors = (String) swt.tag;
                        }
                        break;

                    case R.id.sp_current_loan_org:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            loaning_org = (String) swt.tag;
                        }
                        break;

                    case R.id.sp_education_level:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            educationlevel = (String) swt.tag;
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

        lytLoanBalance = (TextInputLayout) v.findViewById(R.id.lytLoanBalance);
        lytAverageDailyCustomers = (TextInputLayout) v.findViewById(R.id.lytAverageDailyCustomers);

        tvLoanBalance = (EditText) v.findViewById(R.id.tvLoanBalance);
        tvAverageDailyCustomers = (EditText) v.findViewById(R.id.tvAverageDailyCustomers);

        tvAccountOrg_W = (TextView) v.findViewById(R.id.tvAccountOrg_W);
        tv_evertaken_loan = (TextView) v.findViewById(R.id.tv_evertaken_loan);
        tv_other_creditors = (TextView) v.findViewById(R.id.tv_other_creditors);
        tv_current_loan_org = (TextView) v.findViewById(R.id.tv_current_loan_org);
        tv_education_level = (TextView) v.findViewById(R.id.tv_education_level);

        spAccountOrg_W = (Spinner) v.findViewById(R.id.spAccountOrg_W);
        sp_loan_organisation = (Spinner) v.findViewById(R.id.sp_loan_organisation);
        sp_other_creditors = (Spinner) v.findViewById(R.id.sp_other_creditors);
        sp_current_loan_org = (Spinner) v.findViewById(R.id.sp_current_loan_org);
        sp_education_level = (Spinner) v.findViewById(R.id.sp_education_level);

        has_account = (CheckBox) v.findViewById(R.id.has_account);
        ever_taken_loan = (CheckBox) v.findViewById(R.id.ever_taken_loan);
        have_credit_access = (CheckBox) v.findViewById(R.id.have_credit_access);
        ch_current_loan = (CheckBox) v.findViewById(R.id.ch_current_loan);
        ch_employed = (CheckBox) v.findViewById(R.id.ch_employed);

        try {
            siteVisitDao = new SiteVisitDao();
            siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);
            Log.i("siteVisit create", "social " + siteVisit);

            if (siteVisit != null) {

                if (siteVisit.getOutstandingLoanAmount() != null) {
                    String loanbalance = String.valueOf(siteVisit.getOutstandingLoanAmount());
                    tvLoanBalance.setText(loanbalance);
                }

                if (siteVisit.getDailyCustomers() != null) {
                    String dailycustomers = String.valueOf(siteVisit.getDailyCustomers());
                    tvAverageDailyCustomers.setText(dailycustomers);
                }

                if (siteVisit.getAccountOrganisation() != null) {
                    accountOrg_W = String.valueOf(siteVisit.getAccountOrganisation());
                }

                if (siteVisit.getLoanOrganisation() != null) {
                    loan_organisation = String.valueOf(siteVisit.getLoanOrganisation());
                }

                if (siteVisit.getOtherCreditOrganisation() != null) {
                    other_creditors = String.valueOf(siteVisit.getOtherCreditOrganisation());
                }

                if (siteVisit.getCurrentLoanOrganisation() != null) {
                    loaning_org = String.valueOf(siteVisit.getCurrentLoanOrganisation());
                }

                if (siteVisit.getEducationLevelId() != null) {
                    educationlevel = String.valueOf(siteVisit.getEducationLevelId());
                }

                if (siteVisit.getHaveAnAccount() != null) {
                    long inachama = siteVisit.getHaveAnAccount();
                    has_account.setChecked(inachama == 1);
                }

                if (siteVisit.getEverTakenALoan() != null) {
                    long inachama = siteVisit.getEverTakenALoan();
                    ever_taken_loan.setChecked(inachama == 1);
                }

                if (siteVisit.getOtherAccessToCredit() != null) {
                    long inachama = siteVisit.getOtherAccessToCredit();
                    have_credit_access.setChecked(inachama == 1);
                }

                if (siteVisit.getAnyCurrentLoan() != null) {
                    long inachama = siteVisit.getAnyCurrentLoan();
                    ch_current_loan.setChecked(inachama == 1);
                }

                if (siteVisit.getEmployed() != null) {
                    long inachama = siteVisit.getEmployed();
                    Log.i("employed ", "inachama " + inachama);
                    ch_employed.setChecked(inachama == 1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StringWithTag> orgTypesList = populateCreditOrganisationTypesSpinner();

        ArrayAdapter<StringWithTag> accountOrgAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, orgTypesList);
        accountOrgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAccountOrg_W.setAdapter(accountOrgAdapter);

        if (accountOrg_W != null && !accountOrg_W.isEmpty()) {
            int position = -1;
            for (int i = 0; i < orgTypesList.size(); i++) {
                if (orgTypesList.get(i).tag.toString().trim().equals(accountOrg_W)) position = i;
            }
            spAccountOrg_W.setSelection(position);
        }

        ArrayAdapter<StringWithTag> loan_organisationAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, orgTypesList);
        loan_organisationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_loan_organisation.setAdapter(loan_organisationAdapter);

        if (loan_organisation != null && !loan_organisation.isEmpty()) {
            int position = -1;
            for (int i = 0; i < orgTypesList.size(); i++) {
                if (orgTypesList.get(i).tag.toString().trim().equals(loan_organisation))
                    position = i;
            }
            sp_loan_organisation.setSelection(position);
        }

        ArrayAdapter<StringWithTag> other_creditorsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, orgTypesList);
        other_creditorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_other_creditors.setAdapter(other_creditorsAdapter);

        if (other_creditors != null && !other_creditors.isEmpty()) {
            int position = -1;
            for (int i = 0; i < orgTypesList.size(); i++) {
                if (orgTypesList.get(i).tag.toString().trim().equals(other_creditors)) position = i;
            }
            sp_other_creditors.setSelection(position);
        }

        ArrayAdapter<StringWithTag> loaning_orgAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, orgTypesList);
        loaning_orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_current_loan_org.setAdapter(loaning_orgAdapter);

        if (loaning_org != null && !loaning_org.isEmpty()) {
            int position = -1;
            for (int i = 0; i < orgTypesList.size(); i++) {
                if (orgTypesList.get(i).tag.toString().trim().equals(loaning_org)) position = i;
            }
            sp_current_loan_org.setSelection(position);
        }

        List<StringWithTag> educationLevelList = populateEducationLevelSpinner();

        ArrayAdapter<StringWithTag> educationlevelAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, educationLevelList);
        educationlevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_education_level.setAdapter(educationlevelAdapter);

        if (educationlevel != null && !educationlevel.isEmpty()) {
            int position = -1;
            for (int i = 0; i < educationLevelList.size(); i++) {
                if (educationLevelList.get(i).tag.toString().trim().equals(educationlevel))
                    position = i;
            }
            sp_education_level.setSelection(position);
        }

    }

    private List<StringWithTag> populateEducationLevelSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StEducationLevelsDao dao = new StEducationLevelsDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateCreditOrganisationTypesSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StCreditOrganisationsTypesDao dao = new StCreditOrganisationsTypesDao();
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
        return "Social";
    }

    @Override
    public boolean nextIf() {

        boolean cancel = false;
        View focusView = null;

        int have_account = 0, evertaken_loan = 0, other_credit_access = 0, any_current_loan = 0, employed = 0;

        if (has_account.isChecked()) {
            have_account = 1;
        }

        if (ever_taken_loan.isChecked()) {
            evertaken_loan = 1;
        }

        if (have_credit_access.isChecked()) {
            other_credit_access = 1;
        }

        if (ch_current_loan.isChecked()) {
            any_current_loan = 1;
        }

        if (ch_employed.isChecked()) {
            employed = 1;
        }

        String loan_balance = tvLoanBalance.getText().toString().trim();
        String daily_customers = tvAverageDailyCustomers.getText().toString().trim();

        if (TextUtils.isEmpty(accountOrg_W)) {
            tvAccountOrg_W.setError("");
            focusView = tvAccountOrg_W;
            cancel = true;
        } else {
            tvAccountOrg_W.setError(null);
        }



        if (TextUtils.isEmpty(loan_balance)) {
            lytLoanBalance.setError("Please enter a value");
            focusView = lytLoanBalance;
            cancel = true;
        } else {
            lytLoanBalance.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(daily_customers)) {
            lytAverageDailyCustomers.setError("Please enter a value");
            focusView = lytAverageDailyCustomers;
            cancel = true;
        } else {
            lytAverageDailyCustomers.setErrorEnabled(false);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            siteVisitDao = new SiteVisitDao();

            siteVisit = new SiteVisit();
            siteVisit.setId(sitevisitid);
            siteVisit.setOutstandingLoanAmount(Integer.valueOf(loan_balance));
            siteVisit.setDailyCustomers(Integer.valueOf(daily_customers));
            siteVisit.setHaveAnAccount((byte) have_account);
            siteVisit.setEverTakenALoan((byte) evertaken_loan);
            siteVisit.setOtherAccessToCredit((byte) other_credit_access);
            siteVisit.setAnyCurrentLoan((byte) any_current_loan);
            siteVisit.setEmployed((byte) employed);
            siteVisit.setAccountOrganisation(accountOrg_W);
            siteVisit.setLoanOrganisation(loan_organisation);
            siteVisit.setOtherCreditOrganisation(other_creditors);
            siteVisit.setCurrentLoanOrganisation(loaning_org);
            siteVisit.setEducationLevelId(educationlevel);

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
