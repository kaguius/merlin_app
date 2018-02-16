package ke.merlin.modules.business.siteVisitSteppers;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import ke.merlin.dao.customers.StChamaCyclesDao;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.utils.StringWithTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChamaStep extends AbstractStep {
    String sitevisitid, customers_id;
    private boolean validated = false;
    int isnew;

    SiteVisitDao siteVisitDao;
    SiteVisit siteVisit;

    TextInputLayout lytChamaContribution, lytChamaMembers, lytPayoutFrequency, lytPayoutAmount;
    EditText tvChamaContribution, tvChamaMembers, tvPayoutFrequency, tvPayoutAmount;
    TextView tvChamaCycle_W;
    Spinner spChamaCycle_W;
    CheckBox chInaChama_W, chHasRecords_W;

    String chamacycle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chama_step, container, false);

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
        spChamaCycle_W.setOnItemSelectedListener(onItemSelectedListener());

    }

    private void initViews(View v, Bundle savedInstanceState) {

        lytChamaContribution = (TextInputLayout) v.findViewById(R.id.lytChamaContribution);
        lytChamaMembers = (TextInputLayout) v.findViewById(R.id.lytChamaMembers);
        lytPayoutFrequency = (TextInputLayout) v.findViewById(R.id.lytPayoutFrequency);
        lytPayoutAmount = (TextInputLayout) v.findViewById(R.id.lytPayoutAmount);

        tvChamaContribution = (EditText) v.findViewById(R.id.tvChamaContribution);
        tvChamaMembers = (EditText) v.findViewById(R.id.tvChamaMembers);
        tvPayoutFrequency = (EditText) v.findViewById(R.id.tvPayoutFrequency);
        tvPayoutAmount = (EditText) v.findViewById(R.id.tvPayoutAmount);

        tvChamaCycle_W = (TextView) v.findViewById(R.id.tvChamaCycle_W);

        spChamaCycle_W = (Spinner) v.findViewById(R.id.spChamaCycle_W);

        chInaChama_W = (CheckBox) v.findViewById(R.id.chInaChama_W);
        chHasRecords_W = (CheckBox) v.findViewById(R.id.chHasRecords_W);

        try {
            siteVisitDao = new SiteVisitDao();
            siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);
            Log.i("siteVisit create", "chama " + siteVisit);

            if (siteVisit != null) {

                if (siteVisit.getChamaContribution() != null) {
                    String chamacontrib = String.valueOf(siteVisit.getChamaContribution());
                    tvChamaContribution.setText(chamacontrib);
                }

                if (siteVisit.getNoOfChamaMembers() != null) {
                    String chamamembers = String.valueOf(siteVisit.getNoOfChamaMembers());
                    tvChamaMembers.setText(chamamembers);
                }

                if (siteVisit.getChamaPayoutFrequency() != null) {
                    String payoutfreq = String.valueOf(siteVisit.getChamaPayoutFrequency());
                    tvPayoutFrequency.setText(payoutfreq);
                }

                if (siteVisit.getChamaPayoutAmount() != null) {
                    String payoutamount = String.valueOf(siteVisit.getChamaPayoutAmount());
                    tvPayoutAmount.setText(payoutamount);
                }

                if (siteVisit.getChamaCycleId() != null) {
                    chamacycle = String.valueOf(siteVisit.getChamaCycleId());
                }

                if (siteVisit.getIn_a_chama() != null) {
                    long inachama = siteVisit.getIn_a_chama();
                    chInaChama_W.setChecked(inachama == 1);
                }

                if (siteVisit.getChamaHasRecords() != null) {
                    long hasrecords = siteVisit.getChamaHasRecords();
                    chHasRecords_W.setChecked(hasrecords == 1);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        List<StringWithTag> chamaCycleList = populateChamaCycleSpinner();
        ArrayAdapter<StringWithTag> chamaCycleAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, chamaCycleList);
        chamaCycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChamaCycle_W.setAdapter(chamaCycleAdapter);

        if (chamacycle != null && !chamacycle.isEmpty()) {
            int position = -1;
            for (int i = 0; i < chamaCycleList.size(); i++) {
                if (chamaCycleList.get(i).tag.toString().trim().equals(chamacycle)) position = i;
            }
            spChamaCycle_W.setSelection(position);
        }


    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.spChamaCycle_W:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            chamacycle = (String) swt.tag;
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
    }

    private List<StringWithTag> populateChamaCycleSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StChamaCyclesDao dao = new StChamaCyclesDao();
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
        return "Chama";
    }

    @Override
    public boolean nextIf() {

        int inachama = 0, hasrecords = 0;
        String chamacontrib = tvChamaContribution.getText().toString().trim();
        String chamamemebers = tvChamaMembers.getText().toString().trim();
        String payoutfreq = tvPayoutFrequency.getText().toString().trim();
        String payoutamount = tvPayoutAmount.getText().toString().trim();

        if (chInaChama_W.isChecked()) {
            inachama = 1;
        }
        if (chHasRecords_W.isChecked()) {
            hasrecords = 1;
        }

        siteVisitDao = new SiteVisitDao();

        siteVisit = new SiteVisit();
        siteVisit.setId(sitevisitid);
        siteVisit.setIn_a_chama((byte) inachama);
        siteVisit.setChamaHasRecords((byte) hasrecords);
        siteVisit.setChamaCycleId(chamacycle);
        siteVisit.setChamaContribution(Integer.valueOf(String.valueOf(isInteger(chamacontrib) ? chamacontrib : 0)));
        siteVisit.setNoOfChamaMembers(Integer.valueOf(String.valueOf(isInteger(chamamemebers) ? chamamemebers : 0)));
        siteVisit.setChamaPayoutFrequency(Integer.valueOf(String.valueOf(isInteger(payoutfreq) ? payoutfreq : 0)));
        siteVisit.setChamaPayoutAmount(Integer.valueOf(String.valueOf(isInteger(payoutamount) ? payoutamount : 0)));

            siteVisitDao.update(siteVisit);


        validated = true;

        return validated;
    }

    @Override
    public String error() {
        return "<b>There' an error!</b> <small>please check!</small>";
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
