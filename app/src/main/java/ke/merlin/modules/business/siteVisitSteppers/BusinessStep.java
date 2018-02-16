package ke.merlin.modules.business.siteVisitSteppers;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ke.merlin.R;
import ke.merlin.dao.business.StBusinessCategoriesDao;
import ke.merlin.dao.business.StBusinessLocationsDao;
import ke.merlin.dao.business.StBusinessTypesDao;
import ke.merlin.dao.sitevisit.SiteVisitDao;
import ke.merlin.dao.stations.StationsDao;
import ke.merlin.models.sitevisit.SiteVisit;
import ke.merlin.models.stations.Stations;
import ke.merlin.utils.SessionManager;
import ke.merlin.utils.StringWithTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessStep extends AbstractStep implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    MapView mMapView;
    private GoogleMap googleMap;
    double latitude = -1.325430;
    double longitudes = 36.718221;
    String stationName = "Business";
    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;

    SiteVisitDao siteVisitDao;
    SiteVisit siteVisit;

    String sitevisitid, customers_id;
    int isnew;

    TextInputLayout lytDateStartedTradingProduct, lytDateStartedTradingLocation, lytBusinessAddress;
    EditText tvDateStartedTradingProduct, tvDateStartedTradingLocation, tvBusinessAddress;
    TextView tvBusinessCategory, tvBusinessType, tvLocationType;
    Spinner spBusinessCategory, spBusinessType, spLocationType;

    SessionManager session;
    String user_id, station;

    String buscategory, bustype, loctype;

    private boolean validated = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_business_step, container, false);

        if (savedInstanceState == null) {
            sitevisitid = getArguments().getString("sitevisitid");
            customers_id = getArguments().getString("customers_id");
            isnew = getArguments().getInt("isnew");
        } else {
            sitevisitid = (String) savedInstanceState.getSerializable("sitevisitid");
            customers_id = (String) savedInstanceState.getSerializable("customers_id");
            isnew = (int) savedInstanceState.getSerializable("isnew");
        }

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(SessionManager.KEY_ID);
        station = user.get(SessionManager.KEY_STATION);

        initViews(v, savedInstanceState);

        setListeners();

        return v;
    }

    private void setListeners() {
        tvBusinessAddress.addTextChangedListener(new MyTextWatcher(tvBusinessAddress));

        spBusinessCategory.setOnItemSelectedListener(onItemSelectedListener());
        spBusinessType.setOnItemSelectedListener(onItemSelectedListener());
        spLocationType.setOnItemSelectedListener(onItemSelectedListener());
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.spBusinessCategory:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            buscategory = (String) swt.tag;
                        }
                        break;

                    case R.id.spBusinessType:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            bustype = (String) swt.tag;
                        }
                        break;

                    case R.id.spLocationType:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            loctype = (String) swt.tag;
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

        lytDateStartedTradingProduct = (TextInputLayout) v.findViewById(R.id.lytDateStartedTradingProduct);
        lytDateStartedTradingLocation = (TextInputLayout) v.findViewById(R.id.lytDateStartedTradingLocation);
        lytBusinessAddress = (TextInputLayout) v.findViewById(R.id.lytBusinessAddress);

        tvDateStartedTradingProduct = (EditText) v.findViewById(R.id.tvDateStartedTradingProduct);
        tvDateStartedTradingLocation = (EditText) v.findViewById(R.id.tvDateStartedTradingLocation);
        tvBusinessAddress = (EditText) v.findViewById(R.id.tvBusinessAddress);

        tvBusinessCategory = (TextView) v.findViewById(R.id.tvBusinessCategory);
        tvBusinessType = (TextView) v.findViewById(R.id.tvBusinessType);
        tvLocationType = (TextView) v.findViewById(R.id.tvLocationType);

        spBusinessCategory = (Spinner) v.findViewById(R.id.spBusinessCategory);
        spBusinessType = (Spinner) v.findViewById(R.id.spBusinessType);
        spLocationType = (Spinner) v.findViewById(R.id.spLocationType);

        StationsDao stationsDao = new StationsDao();
        Stations stations = stationsDao.getStationById(station);
        longitudes = Double.parseDouble(stations.getLongitude());
        latitude = Double.parseDouble(stations.getLatitude());

        try {
            siteVisitDao = new SiteVisitDao();
            siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);
            Log.i("siteVisit create", "bus " + siteVisit);

            if (siteVisit != null) {

                if (siteVisit.getBusinessCategoryId() != null) {
                    buscategory = siteVisit.getBusinessCategoryId();
                }

                if (siteVisit.getBusinessTypeId() != null) {
                    bustype = siteVisit.getBusinessTypeId();
                }

                if (siteVisit.getBusinessLocationTypesId() != null) {
                    loctype = siteVisit.getBusinessLocationTypesId();
                }

                if (siteVisit.getBusinessAddress() != null) {
                    String address = siteVisit.getBusinessAddress();
                    tvBusinessAddress.setText(address);
                }

                if (siteVisit.getProductTradingStartDate() != null) {
                    String proddate = siteVisit.getProductTradingStartDate();
                    tvDateStartedTradingProduct.setText(proddate);
                }

                if (siteVisit.getLocationTradingStartDate() != null) {
                    String locdate = siteVisit.getLocationTradingStartDate();
                    tvDateStartedTradingLocation.setText(locdate);
                }

                if (siteVisit.getLongitudes() != null) {
                    longitudes = siteVisit.getLongitudes();
                }

                if (siteVisit.getLatitudes() != null) {
                    latitude = siteVisit.getLatitudes();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StringWithTag> busCategoryList = populateBusinessCategorySpinner();
        ArrayAdapter<StringWithTag> busCategoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, busCategoryList);
        busCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBusinessCategory.setAdapter(busCategoryAdapter);

        if (buscategory != null && !buscategory.isEmpty()) {
            int position = -1;
            for (int i = 0; i < busCategoryList.size(); i++) {
                if (busCategoryList.get(i).tag.toString().trim().equals(buscategory)) position = i;
            }
            spBusinessCategory.setSelection(position);
        }

        List<StringWithTag> busTypeList = populateBusinessTypeSpinner();
        ArrayAdapter<StringWithTag> busTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, busTypeList);
        busTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBusinessType.setAdapter(busTypeAdapter);

        if (bustype != null && !bustype.isEmpty()) {
            int position = -1;
            for (int i = 0; i < busTypeList.size(); i++) {
                if (busTypeList.get(i).tag.toString().trim().equals(bustype)) position = i;
            }
            spBusinessType.setSelection(position);
        }

        List<StringWithTag> locTypeList = populateLocationTypeSpinner();
        ArrayAdapter<StringWithTag> locTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, locTypeList);
        locTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocationType.setAdapter(locTypeAdapter);

        if (loctype != null && !loctype.isEmpty()) {
            int position = -1;
            for (int i = 0; i < locTypeList.size(); i++) {
                if (locTypeList.get(i).tag.toString().trim().equals(loctype)) position = i;
            }
            spLocationType.setSelection(position);
        }

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final DatePickerDialog[] nextVisitDatePickerDialog = new DatePickerDialog[1];

        tvDateStartedTradingProduct.setInputType(InputType.TYPE_NULL);
        tvDateStartedTradingProduct.requestFocus();
        tvDateStartedTradingProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar newCalendar = Calendar.getInstance();
                    nextVisitDatePickerDialog[0] = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            tvDateStartedTradingProduct.setText(dateFormatter.format(newDate.getTime()));
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    nextVisitDatePickerDialog[0].getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
                    nextVisitDatePickerDialog[0].show();
                }

                return true;
            }
        });


        tvDateStartedTradingLocation.setInputType(InputType.TYPE_NULL);
        tvDateStartedTradingLocation.requestFocus();
        tvDateStartedTradingLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar newCalendar = Calendar.getInstance();
                    nextVisitDatePickerDialog[0] = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            tvDateStartedTradingLocation.setText(dateFormatter.format(newDate.getTime()));
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    nextVisitDatePickerDialog[0].getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
                    nextVisitDatePickerDialog[0].show();
                }

                return true;
            }
        });

        mMapView = (MapView) v.findViewById(R.id.mapBusView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        MapsInitializer.initialize(getActivity().getApplicationContext());
    }

    private List<StringWithTag> populateLocationTypeSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StBusinessLocationsDao dao = new StBusinessLocationsDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateBusinessTypeSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StBusinessTypesDao dao = new StBusinessTypesDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateBusinessCategorySpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StBusinessCategoriesDao dao = new StBusinessCategoriesDao();
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
        return "Business";
    }

    @Override
    public boolean nextIf() {
        int a = 1;
        Log.i("nextIf() ", String.valueOf(a + 1));

        boolean cancel = false;
        View focusView = null;

        if (!validatetvBusinessAddress()) {
            return false;
        }

        if (TextUtils.isEmpty(buscategory)) {
            tvBusinessCategory.setError("");
            focusView = tvBusinessCategory;
            cancel = true;
        } else {
            tvBusinessCategory.setError(null);
        }

        if (TextUtils.isEmpty(bustype)) {
            tvBusinessType.setError("");
            focusView = tvBusinessType;
            cancel = true;
        } else {
            tvBusinessType.setError(null);
        }

        if (TextUtils.isEmpty(loctype)) {
            tvLocationType.setError("");
            focusView = tvLocationType;
            cancel = true;
        } else {
            tvLocationType.setError(null);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String locationDate = tvDateStartedTradingLocation.getText().toString().trim();
            String productDate = tvDateStartedTradingProduct.getText().toString().trim();
            String businessAddress = tvBusinessAddress.getText().toString().trim();


            siteVisit = siteVisitDao.getSiteVisitById(sitevisitid);

            siteVisit = new SiteVisit();
            siteVisit.setId(sitevisitid);
            siteVisit.setLocationTradingStartDate(locationDate);
            siteVisit.setProductTradingStartDate(productDate);
            siteVisit.setBusinessAddress(businessAddress);
            siteVisit.setLongitudes(longitudes);
            siteVisit.setLatitudes(latitude);
            siteVisit.setBusinessCategoryId(buscategory);
            siteVisit.setBusinessTypeId(bustype);
            siteVisit.setBusinessLocationTypesId(loctype);
            siteVisit.setCreatorId(user_id);

                siteVisitDao.update(siteVisit);


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
        longitudes = toPosition.longitude;
        latitude = toPosition.latitude;
        Toast.makeText(getActivity().getApplicationContext(), "Marker " + marker.getTitle() + " dragged from " + fromPosition + " to " + toPosition, Toast.LENGTH_LONG).show();

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
                case R.id.tvBusinessAddress:
                    validatetvBusinessAddress();
                    break;
            }
        }
    }

    private boolean validatetvBusinessAddress() {
        if (tvBusinessAddress.getText().toString().trim().isEmpty()
                || tvBusinessAddress.getText().toString().trim().length() < 80) {
            lytBusinessAddress.setError("Address must have a minimum of 80 characters.");
            tvBusinessAddress.setError("Address must have a minimum of 80 characters.");
            tvBusinessAddress.requestFocus();
            requestFocus(tvBusinessAddress);
            return false;
        } else {
            lytBusinessAddress.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
