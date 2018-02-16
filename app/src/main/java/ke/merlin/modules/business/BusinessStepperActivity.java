package ke.merlin.modules.business;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TabStepper;

import ke.merlin.R;
import ke.merlin.MainActivity;
import ke.merlin.modules.business.siteVisitSteppers.AssessmentStep;
import ke.merlin.modules.business.siteVisitSteppers.BusinessStep;
import ke.merlin.modules.business.siteVisitSteppers.ChamaStep;
import ke.merlin.modules.business.siteVisitSteppers.ExpensesStep;
import ke.merlin.modules.business.siteVisitSteppers.IncomeStep;
import ke.merlin.modules.business.siteVisitSteppers.PersonalStep;
import ke.merlin.modules.business.siteVisitSteppers.SocialStep;

public class BusinessStepperActivity extends TabStepper {

    String id, sitevisitid;
    int isnew = 0;
    boolean linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
            sitevisitid = extras.getString("sitevisitid");
            linear = extras.getBoolean("linear");

        } else {
            id = (String) savedInstanceState.getSerializable("id");
            sitevisitid = (String) savedInstanceState.getSerializable("sitevisitid");
            linear = savedInstanceState.getBoolean("linear");
        }

        setErrorTimeout(1500);
        setLinear(linear);
        setTitle("Site Visit");
        setAlternativeTab(true);
        setDisabledTouch();
        setDarkPrimaryColor(R.color.lg_login);

        addStep(createFragment(new BusinessStep()));
        addStep(createFragment(new IncomeStep()));
        addStep(createFragment(new ExpensesStep()));
        addStep(createFragment(new PersonalStep()));
        addStep(createFragment(new ChamaStep()));
        addStep(createFragment(new SocialStep()));
        addStep(createFragment(new AssessmentStep()));

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("id", id);
        state.putString("sitevisitid", sitevisitid);
        state.putBoolean("linear", linear);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id = savedInstanceState.getString("id");
        sitevisitid = savedInstanceState.getString("sitevisitid");
        linear = savedInstanceState.getBoolean("linear");
    }

    private AbstractStep createFragment(AbstractStep fragment) {

        Bundle b = new Bundle();
        b.putString("sitevisitid", sitevisitid);
        b.putInt("isnew", isnew);
        b.putString("customers_id", id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onComplete() {
        Toast.makeText(BusinessStepperActivity.this, "Business details saved successfully.", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(BusinessStepperActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
