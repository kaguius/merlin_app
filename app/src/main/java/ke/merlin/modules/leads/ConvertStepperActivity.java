package ke.merlin.modules.leads;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TabStepper;

import ke.merlin.R;
import ke.merlin.MainActivity;
import ke.merlin.modules.leads.steppers.CustomerFragment;
import ke.merlin.modules.leads.steppers.HomesFragment;
import ke.merlin.modules.leads.steppers.IdentityFragment;

public class ConvertStepperActivity extends TabStepper {

    String id;
    boolean linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
            linear = extras.getBoolean("linear");

        } else {
            id = (String) savedInstanceState.getSerializable("id");
            linear = savedInstanceState.getBoolean("linear");
        }

        Log.i("customer id: ", id);

        setErrorTimeout(1500);
        setLinear(linear);
        setTitle("Convert Lead");
        setAlternativeTab(true);
        setDisabledTouch();
        setDarkPrimaryColor(R.color.lg_login);

        addStep(createFragment(new CustomerFragment()));
        addStep(createFragment(new HomesFragment()));
        addStep(createFragment(new IdentityFragment()));

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("id", id);
        state.putBoolean("linear", linear);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id = savedInstanceState.getString("id");
        linear = savedInstanceState.getBoolean("linear");
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        b.putString("id", id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onComplete() {
        Toast.makeText(ConvertStepperActivity.this, "Lead converted successfully.", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ConvertStepperActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}
