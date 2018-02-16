package ke.merlin.modules.search;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

import ke.merlin.R;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.utils.SessionManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

    EditText txtPhone, txtFirstName, txtLastName, txtNationalId;
    Button btnsearch;
    String phone, firstName, lastName, nationalId;

    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        getActivity().setTitle("Search Customer");

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        initViews(v);
        setListeners();

        return v;
    }

    private void setListeners() {
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();

            }
        });
    }

    private void initViews(View v) {
        btnsearch = (Button) v.findViewById(R.id.btnSearchCustomer_s);
        txtPhone = (EditText)  v.findViewById(R.id.etPhoneNumber_s);
        txtFirstName = (EditText)  v.findViewById(R.id.etFirstName_s);
        txtLastName = (EditText)  v.findViewById(R.id.etLastName_s);
        txtNationalId = (EditText)  v.findViewById(R.id.etNationalId_s);
    }

    private void search() {
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Processing...");
        pDialog.show();

        resetErrors();
        getFieldsText();

        HashMap<String, String> user = session.getUserDetails();
        final String station = user.get(SessionManager.KEY_STATION);

        Log.i("Search Data: ", station + " " + phone + " " + firstName + " " + lastName + " " + nationalId);

        ArrayList<Customers> list = CustomersDao.searchCustomers(station, phone, firstName, lastName, nationalId);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        pDialog.hide();

        SearchResultFragment searchFragment = new SearchResultFragment();
        searchFragment.setArrayList(list);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, searchFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void resetErrors() {
        txtPhone.setError(null);
        txtFirstName.setError(null);
        txtLastName.setError(null);
        txtNationalId.setError(null);
    }

    private void getFieldsText() {
        phone = String.valueOf(txtPhone.getText()).trim();
        firstName = String.valueOf(txtFirstName.getText()).trim();
        lastName = String.valueOf(txtLastName.getText()).trim();
        nationalId = String.valueOf(txtNationalId.getText()).trim();
    }
}
