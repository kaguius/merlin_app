package ke.merlin.modules.leads.steppers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ke.merlin.R;
import ke.merlin.dao.customers.CustomersDao;
import ke.merlin.dao.customers.StCustomerApprovalStatusDao;
import ke.merlin.dao.customers.StHomeOwnershipsDao;
import ke.merlin.dao.customers.StLanguagesDao;
import ke.merlin.dao.customers.StMaritalStatusDao;
import ke.merlin.dao.stations.StationsDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.models.stations.Stations;
import ke.merlin.utils.StringWithTag;
import ke.merlin.utils.Utility;

/**
 * Created by mecmurimi on 29/08/2017.
 */

public class IdentityFragment extends AbstractStep {

    String id;
    TextInputLayout lyt_idno_cv, lyt_dob_cv;
    EditText txt_idno_cv, txt_dob_cv;
    TextView tv_language_cv, tv_marital_cv, tv_approval_status_cv, tv_identity_error;
    Spinner sp_language_cv, sp_marital_cv, sp_approval_status_cv;
    CustomersDao customersDao;
    Customers customers;
    String language, marital, approval;
    String photoPath = "", idFrontPath = "", idBackPath = "", fname;
    boolean isPhoto = false, isIdfront = false, isIdBack = false;
    private boolean validated = false;
    private int REQUEST_CAMERA, SELECT_FILE;
    private String userChoosenTask;
    private Button btnPhoto, btnIdFront, btnIdBack;
    private ImageView ivPhoto, ivIdFront, ivIdBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.steps_identity, container, false);

        if (savedInstanceState == null) {
            id = getArguments().getString("id");
        } else {
            id = (String) savedInstanceState.getSerializable("id");
        }

        initViews(v);

        Log.i("customer id homes: ", "id " + id);

        setListeners();

        return v;
    }

    private void setListeners() {

        txt_idno_cv.addTextChangedListener(new MyTextWatcher(txt_idno_cv));
        txt_dob_cv.addTextChangedListener(new MyTextWatcher(txt_dob_cv));

        sp_language_cv.setOnItemSelectedListener(onItemSelectedListener());
        sp_marital_cv.setOnItemSelectedListener(onItemSelectedListener());
        sp_approval_status_cv.setOnItemSelectedListener(onItemSelectedListener());


        btnPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                REQUEST_CAMERA = 1;
                SELECT_FILE = 2;
                selectImage(REQUEST_CAMERA, SELECT_FILE);
            }
        });

        btnIdFront.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                REQUEST_CAMERA = 3;
                SELECT_FILE = 4;
                selectImage(REQUEST_CAMERA, SELECT_FILE);
            }
        });

        btnIdBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                REQUEST_CAMERA = 5;
                SELECT_FILE = 6;
                selectImage(REQUEST_CAMERA, SELECT_FILE);
            }
        });
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (parent.getId()) {

                    case R.id.sp_language_cv:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            language = (String) swt.tag;
                        }
                        break;

                    case R.id.sp_marital_cv:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            marital = (String) swt.tag;
                        }
                        break;

                    case R.id.sp_approval_status_cv:
                        if (id != 0) {
                            StringWithTag swt = (StringWithTag) parent.getItemAtPosition(pos);
                            approval = (String) swt.tag;
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
    }

    private void initViews(View v) {
        btnPhoto = (Button) v.findViewById(R.id.btnPhoto);
        btnIdFront = (Button) v.findViewById(R.id.btnIdFront);
        btnIdBack = (Button) v.findViewById(R.id.btnIdBack);

        ivPhoto = (ImageView) v.findViewById(R.id.ivPhoto);
        ivIdFront = (ImageView) v.findViewById(R.id.ivIdFront);
        ivIdBack = (ImageView) v.findViewById(R.id.ivIdBack);

        lyt_idno_cv = (TextInputLayout) v.findViewById(R.id.lyt_idno_cv);
        lyt_dob_cv = (TextInputLayout) v.findViewById(R.id.lyt_dob_cv);

        txt_idno_cv = (EditText) v.findViewById(R.id.txt_idno_cv);
        txt_dob_cv = (EditText) v.findViewById(R.id.txt_dob_cv);

        tv_language_cv = (TextView) v.findViewById(R.id.tv_language_cv);
        tv_marital_cv = (TextView) v.findViewById(R.id.tv_marital_cv);
        tv_approval_status_cv = (TextView) v.findViewById(R.id.tv_approval_status_cv);
        tv_identity_error = (TextView) v.findViewById(R.id.tv_identity_error);

        sp_language_cv = (Spinner) v.findViewById(R.id.sp_language_cv);
        sp_marital_cv = (Spinner) v.findViewById(R.id.sp_marital_cv);
        sp_approval_status_cv = (Spinner) v.findViewById(R.id.sp_approval_status_cv);

        List<StringWithTag> languageSpinner = populateLanguageSpinner();
        ArrayAdapter<StringWithTag> languageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, languageSpinner);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_language_cv.setAdapter(languageAdapter);

        if (language != null && !language.isEmpty()) {
            int position = -1;
            for (int i = 0; i < languageSpinner.size(); i++) {
                if (languageSpinner.get(i).tag.toString().trim().equals(language)) position = i;
            }
            sp_language_cv.setSelection(position);
        }


        List<StringWithTag> maritalSpinner = populateMaritalSpinner();
        ArrayAdapter<StringWithTag> maritalAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, maritalSpinner);
        maritalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_marital_cv.setAdapter(maritalAdapter);

        if (marital != null && !marital.isEmpty()) {
            int position = -1;
            for (int i = 0; i < maritalSpinner.size(); i++) {
                if (maritalSpinner.get(i).tag.toString().trim().equals(marital)) position = i;
            }
            sp_marital_cv.setSelection(position);
        }


        List<StringWithTag> approvalSpinner = populateApprovalSpinner();
        ArrayAdapter<StringWithTag> approvalAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, approvalSpinner);
        approvalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_approval_status_cv.setAdapter(approvalAdapter);

        if (approval != null && !approval.isEmpty()) {
            int position = -1;
            for (int i = 0; i < approvalSpinner.size(); i++) {
                if (approvalSpinner.get(i).tag.toString().trim().equals(approval)) position = i;
            }
            sp_approval_status_cv.setSelection(position);
        }


        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final DatePickerDialog[] nextVisitDatePickerDialog = new DatePickerDialog[1];

        txt_dob_cv.setInputType(InputType.TYPE_NULL);
        txt_dob_cv.requestFocus();
        txt_dob_cv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar newCalendar = Calendar.getInstance();
                    nextVisitDatePickerDialog[0] = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            txt_dob_cv.setText(dateFormatter.format(newDate.getTime()));
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    nextVisitDatePickerDialog[0].getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
                    nextVisitDatePickerDialog[0].show();
                }

                return true;
            }
        });

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
        return "Identity";
    }

    @Override
    public boolean nextIf() {
        boolean cancel = false;
        View focusView = null;

        if (!validatetxt_idno_cv()) {
            return false;
        }

        if (!validatetxt_dob_cv()) {
            return false;
        }

        if (TextUtils.isEmpty(language)) {
            tv_language_cv.setError("");
            focusView = tv_language_cv;
            cancel = true;
        } else {
            tv_language_cv.setError(null);
        }

        if (TextUtils.isEmpty(marital)) {
            tv_marital_cv.setError("");
            focusView = tv_marital_cv;
            cancel = true;
        } else {
            tv_marital_cv.setError(null);
        }

        if (TextUtils.isEmpty(approval)) {
            tv_approval_status_cv.setError("");
            focusView = tv_approval_status_cv;
            cancel = true;
        } else {
            tv_approval_status_cv.setError(null);
        }

        if (!isPhoto || !isIdfront || !isIdBack) {
            tv_identity_error.setText("Please upload all images");
            focusView = tv_identity_error;
            cancel = true;
        } else {
            tv_identity_error.setText("");
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            customersDao = new CustomersDao();
            customers = customersDao.getCustomerById(id);

            String dob = txt_dob_cv.getText().toString().trim();
            String idNo = txt_idno_cv.getText().toString().trim();

            customers.setDateOfBirth(dob);
            customers.setNationalId(idNo);
            customers.setMaritalStatusId(marital);
            customers.setApprovalStatusId(approval);
            customers.setLanguageId(language);
            customers.setPhotoPath(photoPath);
            customers.setIdFrontPath(idFrontPath);
            customers.setIdBackPath(idBackPath);
            customersDao.update(customers);

            validated = true;
        }

        return validated;
    }

    private List<StringWithTag> populateMaritalSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StMaritalStatusDao dao = new StMaritalStatusDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateLanguageSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StLanguagesDao dao = new StLanguagesDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    private List<StringWithTag> populateApprovalSpinner() {
        List<StringWithTag> itemList = new ArrayList<>();
        itemList.add(new StringWithTag("", ""));

        StCustomerApprovalStatusDao dao = new StCustomerApprovalStatusDao();
        HashMap<String, String> listMap = dao.getSpinnerItems();

        for (Map.Entry<String, String> entry : listMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            itemList.add(new StringWithTag(value, key));
        }

        return itemList;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent(REQUEST_CAMERA);
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent(SELECT_FILE);
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage(final int REQUEST_CAMERA, final int SELECT_FILE) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent(REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent(SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent(int SELECT_FILE) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent(int REQUEST_CAMERA) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    isPhoto = true;
                    photoPath = onCaptureImageResult(data, ivPhoto);
                    break;

                case 2:
                    isPhoto = true;
                    photoPath = onSelectFromGalleryResult(data, ivPhoto);
                    break;

                case 3:
                    isIdfront = true;
                    idFrontPath = onCaptureImageResult(data, ivIdFront);
                    break;

                case 4:
                    isIdfront = true;
                    idFrontPath = onSelectFromGalleryResult(data, ivIdFront);
                    break;

                case 5:
                    isIdBack = true;
                    idBackPath = onCaptureImageResult(data, ivIdBack);
                    break;

                case 6:
                    isIdBack = true;
                    idBackPath = onSelectFromGalleryResult(data, ivIdBack);
                    break;
            }
        }
    }

    private String onCaptureImageResult(Intent data, ImageView imageView) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(thumbnail);

        Uri fileUri = Uri.fromFile(destination);
        Log.i("fileUri.getPath(): ", fileUri.getPath());
        return fileUri.getPath();
    }

    @SuppressWarnings("deprecation")
    private String onSelectFromGalleryResult(Intent data, ImageView imageView) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bm);

//        imageView.setVisibility(View.VISIBLE);
//        imageView.setImageBitmap(bm);

        Uri fileUri = Uri.fromFile(destination);
        Log.i("fileUri.getPath(): ", fileUri.getPath());
        return fileUri.getPath();
    }

    private boolean validatetxt_idno_cv() {
        if (txt_idno_cv.getText().toString().trim().isEmpty()
                || txt_idno_cv.getText().toString().trim().length() < 6) {
            lyt_idno_cv.setError("Enter a valid ID No.");
            requestFocus(txt_idno_cv);
            return false;
        } else {
            lyt_idno_cv.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatetxt_dob_cv() {
        if (txt_dob_cv.getText().toString().trim().isEmpty()) {
            lyt_dob_cv.setError("Enter a valid DOB");
            requestFocus(txt_dob_cv);
            return false;
        } else {
            lyt_dob_cv.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.txt_idno_cv:
                    validatetxt_idno_cv();
                    break;

                case R.id.txt_dob_cv:
                    validatetxt_dob_cv();
                    break;
            }
        }
    }
}
