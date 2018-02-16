package ke.merlin.modules.leads.steppers;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ke.merlin.R;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.customers.StGendersDao;
import ke.merlin.dao.customers.StHomeOwnershipsDao;
import ke.merlin.dao.stations.StationsDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.models.stations.Stations;
import ke.merlin.modules.leads.LeadActivity;
import ke.merlin.utils.StringWithTag;

/**
 * Created by mecmurimi on 29/08/2017.
 */

public class HomesFragment extends AbstractStep implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    String id;
    private boolean validated = false;

    MapView mMapView;
    private GoogleMap googleMap;

    CustomersDao customersDao;
    StationsDao stationsDao;
    Stations stations;
    Customers customers;

    double latitude = -1.325430;
    double longitudes = 36.718221;
    String stationName = "Karen HQ";

    TextInputLayout lyt_lived_since_cv, lyt_home_address;
    EditText txt_lived_since_cv, txt_home_address;
    TextView tv_home_ownership;
    Spinner sp_home_ownership;

    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;

    String home_ownership;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.steps_homes, container, false);

        if (savedInstanceState == null) {
            id = getArguments().getString("id");
        } else {
            id = (String) savedInstanceState.getSerializable("id");
        }

        initViews(v, savedInstanceState);

        Log.i("customer id homes: ", "id " + id);

        setListeners();

        return v;
    }

    private void setListeners() {
        txt_lived_since_cv.addTextChangedListener(new MyTextWatcher(txt_lived_since_cv));
        txt_home_address.addTextChangedListener(new MyTextWatcher(txt_home_address));

        sp_home_ownership.setOnItemSelectedListener(onItemSelectedListener());

    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.sp_home_ownership:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            home_ownership = (String) swt.tag;
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
        mMapView = (MapView) v.findViewById(R.id.mapView);

        lyt_lived_since_cv = (TextInputLayout) v.findViewById(R.id.lyt_lived_since_cv);
        lyt_home_address = (TextInputLayout) v.findViewById(R.id.lyt_home_address);

        txt_lived_since_cv = (EditText) v.findViewById(R.id.txt_lived_since_cv);
        txt_home_address = (EditText) v.findViewById(R.id.txt_home_address);

        tv_home_ownership = (TextView) v.findViewById(R.id.tv_home_ownership);

        sp_home_ownership = (Spinner) v.findViewById(R.id.sp_home_ownership);


        List<StringWithTag> ownershipList = populateHomeOwnershipSpinner();
        ArrayAdapter<StringWithTag> genderAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ownershipList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_home_ownership.setAdapter(genderAdapter);

        if (home_ownership != null && !home_ownership.isEmpty()) {
            int position = -1;
            for (int i = 0; i < ownershipList.size(); i++) {
                if (ownershipList.get(i).tag.toString().trim().equals(home_ownership)) position = i;
            }
            sp_home_ownership.setSelection(position);
        }

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final DatePickerDialog[] nextVisitDatePickerDialog = new DatePickerDialog[1];

        txt_lived_since_cv.setInputType(InputType.TYPE_NULL);
        txt_lived_since_cv.requestFocus();
        txt_lived_since_cv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar newCalendar = Calendar.getInstance();
                    nextVisitDatePickerDialog[0] = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            txt_lived_since_cv.setText(dateFormatter.format(newDate.getTime()));
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    nextVisitDatePickerDialog[0].getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
                    nextVisitDatePickerDialog[0].show();
                }

                return true;
            }
        });

        try {
            customersDao = new CustomersDao();
            stationsDao = new StationsDao();
            customers = customersDao.getCustomerById(id);

            String address = customers.getHomeAddress();
            String lived = customers.getLivedThereSince();

            home_ownership = customers.getHomeOwnershipId();
            txt_lived_since_cv.setText(lived);
            txt_home_address.setText(address);

            if (customers.getLongitudes() != null && customers.getLatitudes() != null){
                longitudes = customers.getLongitudes();
                latitude = customers.getLatitudes();
                stationName = "";
            }else {
                stations = stationsDao.getStationById(customers.getStationsId());
                longitudes = Double.parseDouble(stations.getLongitude());
                latitude = Double.parseDouble(stations.getLatitude());
                stationName = stations.getName();

                Log.i("Station Info: ", stationName + " " + longitudes + " " + latitude);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        mMapView.getMapAsync(this);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {e.printStackTrace();}

    }

    private List<StringWithTag> populateHomeOwnershipSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StHomeOwnershipsDao dao = new StHomeOwnershipsDao();
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
        state.putString("id", id);
    }

    @Override
    public void onStepVisible() {
        id = getArguments().getString("id");
        Log.i("customer id: ", "id is " + id);
    }

    @Override
    public String name() {
        return "Home";
    }

    @Override
    public boolean nextIf() {
        boolean cancel = false;
        View focusView = null;

        if (!validatetxt_lived_since_cv()) {
            return false;
        }

        if (!validatetxt_home_address()) {
            return false;
        }

        if (TextUtils.isEmpty(home_ownership)) {
            tv_home_ownership.setError("");
            focusView = tv_home_ownership;
            cancel = true;
        } else {
            tv_home_ownership.setError(null);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String lived_there = txt_lived_since_cv.getText().toString().trim();
            String home_address = txt_home_address.getText().toString().trim();

            customers.setHomeAddress(home_address);
            customers.setLivedThereSince(lived_there);
            customers.setHomeOwnershipId(home_ownership);
            customers.setLongitudes(longitudes);
            customers.setLatitudes(latitude);
            customersDao.update(customers);

            validated = true;
        }

        return validated;
    }

    @Override
    public String error() {
         return "<b>There an error!</b> <small>Check!</small>";
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng station = new LatLng(latitude, longitudes);

        Log.i("Station Info: ", stationName + " " + longitudes + " " + latitude);

        googleMap.addMarker(new MarkerOptions().position(station).title(stationName).snippet("Customer home location").draggable(true));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(station).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Set a listener for marker click.
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer) marker.getTag();
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(getActivity().getApplicationContext(), marker.getTitle() + " has been clicked " + clickCount + " times.", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        fromPosition = marker.getPosition();
        Log.d(getClass().getSimpleName(), "Drag start at: " + fromPosition);

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        toPosition = marker.getPosition();
        latitude = toPosition.latitude;
        longitudes = toPosition.longitude;
        Toast.makeText(getActivity().getApplicationContext(), "Marker " + marker.getTitle() + " dragged from " + fromPosition + " to " + toPosition, Toast.LENGTH_LONG).show();

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
                case R.id.txt_lived_since_cv:
                    validatetxt_lived_since_cv();
                    break;
                case R.id.txt_home_address:
                    validatetxt_home_address();
                    break;
            }
        }
    }

    private boolean validatetxt_home_address() {
            if (txt_home_address.getText().toString().trim().isEmpty()
                    || txt_home_address.getText().toString().trim().length() < 80) {
                lyt_home_address.setError("Address must have a minimum of 80 characters.");
                requestFocus(txt_home_address);
                return false;
            } else {
                lyt_home_address.setErrorEnabled(false);
            }
            return true;
        }

    private boolean validatetxt_lived_since_cv() {
        Date d1 = new Date();
        if(txt_lived_since_cv.getText().toString().trim().isEmpty()
                || txt_lived_since_cv.getText().toString().trim().length() < 3){
            lyt_lived_since_cv.setError("Enter a valid date");
            requestFocus(txt_lived_since_cv);
            return false;
        }else{
            String next_visit = txt_lived_since_cv.getText().toString().trim();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                d1 = df.parse(next_visit);
            } catch (ParseException e) {
                lyt_lived_since_cv.setError("Date invalid");
                requestFocus(txt_lived_since_cv);
                return false;
            }

            lyt_lived_since_cv.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
