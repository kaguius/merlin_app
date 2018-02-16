package ke.merlin.modules.customers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ke.merlin.R;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.customers.StCustomerActiveStatusDao;
import ke.merlin.dao.customers.StCustomerApprovalStatusDao;
import ke.merlin.dao.customers.StCustomerStateDao;
import ke.merlin.dao.customers.StCustomerTitlesDao;
import ke.merlin.dao.customers.StGendersDao;
import ke.merlin.dao.customers.StHomeOwnershipsDao;
import ke.merlin.dao.customers.StLanguagesDao;
import ke.merlin.dao.customers.StMaritalStatusDao;
import ke.merlin.dao.stations.StationsDao;
import ke.merlin.dao.stations.StationsMarketsDao;
import ke.merlin.dao.users.UsersDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.models.customers.StCustomerActiveStatus;
import ke.merlin.models.customers.StCustomerApprovalStatus;
import ke.merlin.models.customers.StCustomerState;
import ke.merlin.models.customers.StCustomerTitles;
import ke.merlin.models.customers.StGenders;
import ke.merlin.models.customers.StHomeOwnerships;
import ke.merlin.models.customers.StLanguages;
import ke.merlin.models.customers.StMaritalStatus;
import ke.merlin.models.stations.Stations;
import ke.merlin.models.stations.StationsMarkets;
import ke.merlin.models.users.Users;
import ke.merlin.utils.UrlConstants;

public class CustomerDetailsActivity extends AppCompatActivity {

    CustomersDao customersDao;

    String id;

    ImageView cst_image, expanded_image;
    ImageButton idfront, idback;
    TextView cst_name, gender, marital, market, station, lo, co, primary, disbursement, alternative,
            national_id, dob, ownership, lived_since, longitudes, latitudes, homeaddress, others,
            customerstate, activestatus, approvalstatus, language, assetlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Customer Details");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
        } else {
            id = (String) savedInstanceState.getSerializable("id");
        }

        initViews();
    }

    private void initViews() {

        cst_image = (ImageView) findViewById(R.id.cst_image);
        expanded_image = (ImageView) findViewById(R.id.expanded_image);

        idfront = (ImageButton) findViewById(R.id.idfront);
        idback = (ImageButton) findViewById(R.id.idback);

        cst_name = (TextView) findViewById(R.id.cst_name);
        gender = (TextView) findViewById(R.id.gender);
        marital = (TextView) findViewById(R.id.marital);
        market = (TextView) findViewById(R.id.market);
        station = (TextView) findViewById(R.id.station);
        lo = (TextView) findViewById(R.id.lo);
        co = (TextView) findViewById(R.id.co);
        primary = (TextView) findViewById(R.id.primary);
        disbursement = (TextView) findViewById(R.id.disbursement);
        alternative = (TextView) findViewById(R.id.alternative);
        national_id = (TextView) findViewById(R.id.national_id);
        dob = (TextView) findViewById(R.id.dob);
        ownership = (TextView) findViewById(R.id.ownership);
        lived_since = (TextView) findViewById(R.id.lived_since);
        longitudes = (TextView) findViewById(R.id.longitudes);
        latitudes = (TextView) findViewById(R.id.latitudes);
        homeaddress = (TextView) findViewById(R.id.homeaddress);
        others = (TextView) findViewById(R.id.others);
        customerstate = (TextView) findViewById(R.id.customerstate);
        activestatus = (TextView) findViewById(R.id.activestatus);
        approvalstatus = (TextView) findViewById(R.id.approvalstatus);
        language = (TextView) findViewById(R.id.language);
        assetlist = (TextView) findViewById(R.id.assetlist);

        customersDao = new CustomersDao();
        Customers customers = customersDao.getCustomerById(id);

        StCustomerTitlesDao stCustomerTitlesDao = new StCustomerTitlesDao();
        StGendersDao stGendersDao = new StGendersDao();
        StMaritalStatusDao stMaritalStatusDao = new StMaritalStatusDao();
        StationsMarketsDao stationsMarketsDao = new StationsMarketsDao();
        StationsDao stationsDao = new StationsDao();
        UsersDao usersDao = new UsersDao();

        Users users_lo = usersDao.getUserById(customers.getLoanOfficerId());
        Users users_co = usersDao.getUserById(customers.getCollectionsOfficerId());

        StHomeOwnershipsDao stHomeOwnershipsDao = new StHomeOwnershipsDao();
        StCustomerStateDao stCustomerStateDao = new StCustomerStateDao();
        StCustomerActiveStatusDao stCustomerActiveStatusDao = new StCustomerActiveStatusDao();
        StCustomerApprovalStatusDao stCustomerApprovalStatusDao = new StCustomerApprovalStatusDao();
        StLanguagesDao stLanguagesDao = new StLanguagesDao();

        StCustomerTitles stCustomerTitles = stCustomerTitlesDao.getCustomerTitleById(customers.getTitleId());
        StGenders stGenders = stGendersDao.getCustomerGenderById(customers.getGenderId());
        StMaritalStatus stMaritalStatus = stMaritalStatusDao.getById(customers.getMaritalStatusId());
        StationsMarkets stationsMarkets = stationsMarketsDao.getById(customers.getMarketId());
        Stations stations = stationsDao.getStationById(customers.getStationsId());
        StHomeOwnerships stHomeOwnerships = stHomeOwnershipsDao.getById(customers.getHomeOwnershipId());
        StCustomerState stCustomerState = stCustomerStateDao.getById(customers.getCustomerStateId());
        StCustomerActiveStatus stCustomerActiveStatus = stCustomerActiveStatusDao.getById(customers.getActiveStatusId());
        StCustomerApprovalStatus stCustomerApprovalStatus = stCustomerApprovalStatusDao.getById(customers.getApprovalStatusId());
        StLanguages stLanguages = stLanguagesDao.getById(customers.getLanguageId());

        String txt_title = (stCustomerTitles == null) ? "" : stCustomerTitles.getName();
        String txt_firstname = customers.getFirstName();
        String txt_lastname = customers.getLastName();
        String txt_aka = customers.getAlsoKnownAs();
        String txt_gender = (stGenders == null) ? "" : stGenders.getName();
        String txt_marital = (stMaritalStatus == null) ? "" : stMaritalStatus.getName();
        String txt_market = (stationsMarkets == null) ? "" : stationsMarkets.getName();
        String txt_station = (stations == null) ? "" : stations.getName();
        String txt_lo = users_lo.getFirstName() + " " + users_lo.getLastName();
        String txt_co = users_co.getFirstName() + " " + users_co.getLastName();
        String txt_primary = String.valueOf(customers.getPrimaryPhone());
        String txt_disbursement = String.valueOf(customers.getDisbursePhone());
        String txt_alternative = String.valueOf(customers.getAlternativePhone());
        String txt_national_id = customers.getNationalId();
        String txt_dob = (customers.getDateOfBirth() == null) ? "" : customers.getDateOfBirth();
        String txt_ownership = (stHomeOwnerships == null) ? "" : stHomeOwnerships.getName();
        String txt_lived_since = customers.getLivedThereSince();
        String txt_longitudes = String.valueOf(customers.getLongitudes());
        String txt_latitudes = String.valueOf(customers.getLatitudes());
        String txt_homeaddress = customers.getHomeAddress();
        String txt_others = customers.getOtherSources();
        String txt_customerstate = (stCustomerState == null) ? "" : stCustomerState.getName();
        String txt_activestatus = (stCustomerActiveStatus == null) ? "" : stCustomerActiveStatus.getName();
        String txt_approvalstatus = (stCustomerApprovalStatus == null) ? "" : stCustomerApprovalStatus.getName();
        String txt_language = (stLanguages == null) ? "" : stLanguages.getName();
        String txt_assetlist = (customers.getAssetList() == 0) ? "No" : "Yes";


        cst_name.setText(txt_title + " " + txt_firstname + " " + txt_lastname + " aka " + txt_aka);
        gender.setText(txt_gender);
        marital.setText(txt_marital);
        market.setText(txt_market + " market, ");
        station.setText(txt_station);
        lo.setText(txt_lo);
        co.setText(txt_co);
        primary.setText(txt_primary);
        disbursement.setText(txt_disbursement);
        alternative.setText(txt_alternative);
        national_id.setText("National ID: " + txt_national_id);
        dob.setText("DOB: " + txt_dob);
        ownership.setText(txt_ownership);
        lived_since.setText(txt_lived_since);
        longitudes.setText(txt_longitudes);
        latitudes.setText(txt_latitudes);
        homeaddress.setText(txt_homeaddress);
        others.setText(txt_others);
        customerstate.setText(txt_customerstate);
        activestatus.setText(txt_activestatus);
        approvalstatus.setText(txt_approvalstatus);
        language.setText(txt_language);
        assetlist.setText(txt_assetlist);

        String path = customers.getPhotoPath();
        String front = customers.getIdFrontPath();
        String back = customers.getIdBackPath();

        String photopath = "", frontpath = "", backpath = "";

        if (path != null) {
            photopath = Base64.encodeToString(path.getBytes(), Base64.NO_WRAP);
        }
        if (front != null) {
            frontpath = Base64.encodeToString(front.getBytes(), Base64.NO_WRAP);
        }
        if (back != null) {
            backpath = Base64.encodeToString(back.getBytes(), Base64.NO_WRAP);
        }

        Picasso.with(this)
                .load(UrlConstants.LoadPhoto_URL + photopath).fit().centerCrop()
                .placeholder(R.drawable.leads)
                .error(R.drawable.leads)
                .into(cst_image);

        Picasso.with(this)
                .load(UrlConstants.LoadPhoto_URL + frontpath).fit().centerCrop()
                .placeholder(R.drawable.leads)
                .error(R.drawable.leads)
                .into(idfront);

        Picasso.with(this)
                .load(UrlConstants.LoadPhoto_URL + backpath).fit().centerCrop()
                .placeholder(R.drawable.leads)
                .error(R.drawable.leads)
                .into(idback);


    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("id", id);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id = savedInstanceState.getString("id");
    }

}
