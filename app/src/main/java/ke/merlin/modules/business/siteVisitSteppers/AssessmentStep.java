package ke.merlin.modules.business.siteVisitSteppers;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import ke.merlin.R;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.models.sitevisit.SiteVisit;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssessmentStep extends AbstractStep {

    String sitevisitid, customers_id;
    int isnew;
    SiteVisitDao siteVisitDao;
    SiteVisit siteVisit;
    TextInputLayout lytLendAmount;
    EditText tvLendAmount;
    CheckBox chStockNeat_W, chAccurateLedger_W, chSalesActivity_W, chPermanentOperations_W, chProofOfOwnership_W,
            chFortcoming_W, chKnownAuthority_W, chSoundReputation_W, chWouldLend_W;
    private boolean validated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_assessment_step, container, false);

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

        return v;
    }

    private void initViews(View v, Bundle savedInstanceState) {
        lytLendAmount = (TextInputLayout) v.findViewById(R.id.lytLendAmount);

        tvLendAmount = (EditText) v.findViewById(R.id.tvLendAmount);

        chStockNeat_W = (CheckBox) v.findViewById(R.id.chStockNeat_W);
        chAccurateLedger_W = (CheckBox) v.findViewById(R.id.chAccurateLedger_W);
        chSalesActivity_W = (CheckBox) v.findViewById(R.id.chSalesActivity_W);
        chPermanentOperations_W = (CheckBox) v.findViewById(R.id.chPermanentOperations_W);
        chProofOfOwnership_W = (CheckBox) v.findViewById(R.id.chProofOfOwnership_W);
        chFortcoming_W = (CheckBox) v.findViewById(R.id.chFortcoming_W);
        chKnownAuthority_W = (CheckBox) v.findViewById(R.id.chKnownAuthority_W);
        chSoundReputation_W = (CheckBox) v.findViewById(R.id.chSoundReputation_W);
        chWouldLend_W = (CheckBox) v.findViewById(R.id.chWouldLend_W);

        try {
            siteVisitDao = new SiteVisitDao();
            siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);

            Log.i("siteVisit create", "assess " + siteVisit);

            if (siteVisit != null) {

                if (siteVisit.getLendAmount() != null) {
                    String lendamount = String.valueOf(siteVisit.getLendAmount());
                    tvLendAmount.setText(lendamount);
                }

                if (siteVisit.getStockNeat() != null) {
                    long stockneat = siteVisit.getStockNeat();
                    chStockNeat_W.setChecked(stockneat == 1);
                }

                if (siteVisit.getAccurateLedgerBook() != null) {
                    long stockneat = siteVisit.getAccurateLedgerBook();
                    chAccurateLedger_W.setChecked(stockneat == 1);
                }

                if (siteVisit.getSalesActivity() != null) {
                    long stockneat = siteVisit.getSalesActivity();
                    chSalesActivity_W.setChecked(stockneat == 1);
                }

                if (siteVisit.getPermanentOperation() != null) {
                    long stockneat = siteVisit.getPermanentOperation();
                    chPermanentOperations_W.setChecked(stockneat == 1);
                }

                if (siteVisit.getProofOfOwnership() != null) {
                    long stockneat = siteVisit.getProofOfOwnership();
                    chProofOfOwnership_W.setChecked(stockneat == 1);
                }

                if (siteVisit.getForthcomingAndTransparent() != null) {
                    long stockneat = siteVisit.getForthcomingAndTransparent();
                    chFortcoming_W.setChecked(stockneat == 1);
                }

                if (siteVisit.getKnownToMarketAuthorities() != null) {
                    long stockneat = siteVisit.getKnownToMarketAuthorities();
                    chKnownAuthority_W.setChecked(stockneat == 1);
                }

                if (siteVisit.getSoundReputation() != null) {
                    long stockneat = siteVisit.getSoundReputation();
                    chSoundReputation_W.setChecked(stockneat == 1);
                }

                if (siteVisit.getWouldILend() != null) {
                    long stockneat = siteVisit.getWouldILend();
                    chWouldLend_W.setChecked(stockneat == 1);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return "Assessment";
    }

    @Override
    public boolean nextIf() {

        int stock_neat = 0, ledger_book = 0, sales_activity = 0, permanent_operation = 0,
                ownership_proof = 0, transparent = 0, known_to_authority = 0, sound_reputation = 0, would_lend = 0;

        if (chStockNeat_W.isChecked()) {
            stock_neat = 1;
        }

        if (chAccurateLedger_W.isChecked()) {
            ledger_book = 1;
        }

        if (chSalesActivity_W.isChecked()) {
            sales_activity = 1;
        }

        if (chPermanentOperations_W.isChecked()) {
            permanent_operation = 1;
        }

        if (chProofOfOwnership_W.isChecked()) {
            ownership_proof = 1;
        }

        if (chFortcoming_W.isChecked()) {
            transparent = 1;
        }

        if (chKnownAuthority_W.isChecked()) {
            known_to_authority = 1;
        }

        if (chSoundReputation_W.isChecked()) {
            sound_reputation = 1;
        }

        if (chWouldLend_W.isChecked()) {
            would_lend = 1;
        }

        String lend_amount = tvLendAmount.getText().toString().trim();

        siteVisitDao = new SiteVisitDao();

        siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);

        double restocking_ratio = 0.0, stock_health_multiplier = 0.0, weekly_profit_gross = 0.0, business_expenses = 0.0, affordability = 0.0,
                net_profit = 0.0, cost_of_living = 0.0, other_income = 0.0, min_allowed_personal_expenses = 0.0, cost_of_sales = 0.0;

        if (siteVisit != null) {

            try {
                double stockvalue = siteVisit.getCurrentStockValue();
                double spendstock = siteVisit.getSpendOnStock();
                Log.i("values ", " CurrentStockValue " + stockvalue + " SpendOnStock " + spendstock);
                restocking_ratio = stockvalue / spendstock;

                if (restocking_ratio < 0.5) {
                    stock_health_multiplier = 1.5;
                } else if (restocking_ratio <= 1) {
                    stock_health_multiplier = 1.25;
                } else if (restocking_ratio <= 4) {
                    stock_health_multiplier = 1.1;
                } else {
                    stock_health_multiplier = 0.8;
                }

                double sales = siteVisit.getSalesPerCycle();
                weekly_profit_gross = sales - spendstock;

                double busrent = siteVisit.getBusinessRent();
                double salary = siteVisit.getEmployeesSalary();
                double busutilities = siteVisit.getBusinessUtilities();
                double licensing = siteVisit.getLicensingFee();
                double storage = siteVisit.getStorageFee();
                double transport = siteVisit.getTransportFee();

                business_expenses = busrent + salary + busutilities + licensing + storage + transport;

                net_profit = weekly_profit_gross - business_expenses;

                double homerent = siteVisit.getHomeRent();
                double houseutility = siteVisit.getHouseUtilities();
                double foodcost = siteVisit.getFoodCost();
                double schoolfees = siteVisit.getSchoolFees();
                double chama_contrib = siteVisit.getChamaContribution();

                cost_of_living = homerent + houseutility + foodcost + schoolfees + chama_contrib;

                min_allowed_personal_expenses = weekly_profit_gross * (0.20);

                cost_of_sales = (spendstock / sales) * 100;

                if (min_allowed_personal_expenses == cost_of_living) {
                    affordability = 0.0;
                    System.out.println("Test 1 fail");
                } else if (cost_of_sales < 15 || cost_of_sales > 85) {
                    affordability = 0.0;
                } else if (spendstock < 2500) {
                    affordability = 0.0;
                    System.out.println("Test 3 fail");
                } else {
                    Double max_value = Math.max(min_allowed_personal_expenses, cost_of_living);
                    affordability = (net_profit - Math.max(min_allowed_personal_expenses, cost_of_living)) * (stock_health_multiplier / 1.30);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        siteVisit = new SiteVisit();
        siteVisit.setId(sitevisitid);
        siteVisit.setStockNeat((byte) stock_neat);
        siteVisit.setAccurateLedgerBook((byte) ledger_book);
        siteVisit.setSalesActivity((byte) sales_activity);
        siteVisit.setPermanentOperation((byte) permanent_operation);
        siteVisit.setProofOfOwnership((byte) ownership_proof);
        siteVisit.setForthcomingAndTransparent((byte) transparent);
        siteVisit.setKnownToMarketAuthorities((byte) known_to_authority);
        siteVisit.setSoundReputation((byte) sound_reputation);
        siteVisit.setWouldILend((byte) would_lend);
        siteVisit.setLendAmount(Integer.valueOf(lend_amount));
        siteVisit.setIsCompleted((byte) 1);
        siteVisit.setAffordability((int) affordability);

        Log.i("affordability ", " the value is " + String.valueOf(affordability));

        siteVisitDao.update(siteVisit);


        validated = true;

        return validated;
    }

    @Override
    public String error() {
        return "<b>There' an error!</b> <small>please check!</small>";
    }
}
