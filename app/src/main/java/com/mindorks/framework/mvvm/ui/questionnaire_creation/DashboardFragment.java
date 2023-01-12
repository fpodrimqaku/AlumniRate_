package com.mindorks.framework.mvvm.ui.questionnaire_creation;

import static android.content.Context.WINDOW_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.WriterException;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.databinding.FragmentQuestionnaireCreationBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.questionnaire.QuestionnaireListNavigator;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends BaseFragment<FragmentQuestionnaireCreationBinding, DashboardViewModel> implements QuestionnaireListNavigator {


    private DashboardViewModel dashboardViewModel;

    UUID uuid = UUID.randomUUID();
    WindowManager manager;
    FusedLocationProviderClient mFusedLocationClient;
    Bitmap imageBitmapTemp;
    String qrCode = uuid.toString();
    int defaultHourOnPicker = 12;
    int defaultMinuteOnPicker = 0;

    int PERMISSION_ID = 101;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  mViewModel.setNavigator(this);
        //mViewModel.setNavigator(this);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.manager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_questionnaire_creation, container, false);

        final EditText fvalue = (EditText) root.findViewById(R.id.editQuestionnaireOrganizationName);

        fvalue.addTextChangedListener(new TextWatcher() {
                                          public void afterTextChanged(Editable s) {
                                          }

                                          public void beforeTextChanged(CharSequence s, int start,
                                                                        int count, int after) {
                                          }


                                          @Override
                                          public void onTextChanged(CharSequence s, int start, int before, int count) {
                                              getViewDataBinding().getViewModel().setQuestionnaireName(s.toString());

                                          }
                                      }

        );

        mViewModel.getError().observe(this.getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer value) {
                if (value != null) {
                    Toast.makeText(getContext(), getResources().getString(value), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    public void initiateQrCode(View root) {
        final ImageView qrCode_image = (ImageView) root.findViewById(R.id.imageView_qr_code);
        String fvalue_string = "";

        fvalue_string = uuid.toString();
        getViewDataBinding().getViewModel().setQuestionnaireQrCode(fvalue_string);

        Display display = manager.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(fvalue_string, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCode_image.setImageBitmap(bitmap);
            imageBitmapTemp = bitmap;

            //shareQrCode(imageToShareUri);

        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initiateQrCode(view);

        initVariablesAndEvents(view);
        try {
            ClearCacheImprovised();

        } catch (Exception exe) {
        }
    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_questionnaire_creation;
    }


    @Override
    public void goBack() {

    }


    @OnClick(R.id.buttonSaveQuestionnaireOrganization)
    public void buttonSaveQuestionnaireOrganization_clicked() {
        dashboardViewModel.insertQuestionnaireOrganization(
                () -> {
                    snackShowLong(getString(R.string.questionnaire_inserted_successfully));
                },
                () -> {
                    snackShowLong_ERROR(getString(R.string.questionnaire_inserted_UNsuccessfully));
                }
        );
    }


    public void initVariablesAndEvents(View rootView) {
        CheckBox locationRequired = rootView.findViewById(R.id.questionnaireOrganizationLocationRequired);
        locationRequired.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)//mViewModel.getQuestionnaireLocationRequired() !=null &&  mViewModel.getQuestionnaireLocationRequired() == true)
                {
                    checkIfAppHasLocationPermissionAndRequestIt();
                } else //mViewModel.getQuestionnaireLocationRequired() !=null && mViewModel.getQuestionnaireLocationRequired() == false)
                {
                    mViewModel.setQuestionnaireLocation(null, null);
                }
            }
        });


    }


    @OnClick({R.id.questionnaireTimeFrom, R.id.qestionnaireTimeTo})
    public void initiateTimeFromOrTimeTo(View view) {


        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                setDefaultHourOnPicker(selectedHour);
                setDefaultMinuteOnPicker(selectedMinute);


                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int year = localDate.getYear();
                int month = localDate.getMonthValue();
                int day = localDate.getDayOfMonth();

                Calendar calendarDate = Calendar.getInstance();

                calendarDate.set(Calendar.DATE, day);
                calendarDate.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarDate.set(Calendar.MINUTE, selectedMinute);
                calendarDate.set(Calendar.SECOND, 0);
                calendarDate.set(Calendar.MILLISECOND, 0);
                calendarDate.set(Calendar.MONTH, month - 1);//se ata mretat qe morin rrog per ket pune ->JANARIN E KAN LAN 0
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.setTimeZone(TimeZone.getDefault());


                if (view.getId() == R.id.questionnaireTimeFrom)
                    dashboardViewModel.setQuestionnaireDateFrom(calendarDate.getTime());
                else
                    dashboardViewModel.setQuestionnaireDateTo(calendarDate.getTime());

                String time = "";
                time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute);
                ((TextView) view).setText(time);

            }
        };

        //   int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), /*style,*/ onTimeSetListener, getDefaultHourOnPicker(), getDefaultMinuteOnPicker(), true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        requestNewLocationData();
        if (checkLocationPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            StringBuilder locationStringBuilder = new StringBuilder("");
                            locationStringBuilder.append(location.getLatitude());
                            locationStringBuilder.append(",");
                            locationStringBuilder.append(location.getLongitude());
                            setQuestionnaireLocation(true, locationStringBuilder.toString());

                            locationStringBuilder.append("Latitude: " + location.getLatitude() + "      ");
                            locationStringBuilder.append("Longitude: " + location.getLongitude() + "");

                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private boolean checkLocationPermissions() {
        return getContext().checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{

                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            StringBuilder locationStringBuilder = new StringBuilder("");
            locationStringBuilder.append("Latitude: " + mLastLocation.getLatitude() + "      ");
            locationStringBuilder.append("Longitude: " + mLastLocation.getLongitude() + "");
            if (locationStringBuilder != null)
                Toast.makeText(getActivity(), locationStringBuilder.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void setQuestionnaireLocation(boolean isLocationRequired, String location) {
        getViewDataBinding().getViewModel().setQuestionnaireLocation(isLocationRequired, location);
    }


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getLastLocation();

                } else {

                    snackShowLong_ERROR(getResources().getString(R.string.permission_location_request));
                    mViewModel.setQuestionnaireLocationRequired(false);
                    ((CheckBox) getView().findViewById(R.id.questionnaireOrganizationLocationRequired)).setChecked(false);
                }
            });


    public void checkIfAppHasLocationPermissionAndRequestIt() {

        if (getActivity().checkSelfPermission(
                 Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            getLastLocation();

        } else if (shouldShowRequestPermissionRationale(getResources().getString(R.string.permission_location_request))) {

        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        // ClearCacheImprovised();
                    }
                }
            });


    public void shareQrCode(Uri ImageUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, ImageUri);
        shareIntent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/jpeg");
        // startActivity(Intent.createChooser(shareIntent, null));


        someActivityResultLauncher.launch(shareIntent);
    }

    @OnClick(R.id.questionnaireCreatedShare)
    public void shareImage() {
        if (imageBitmapTemp != null && qrCode != null)
            storeImageIntoCacheAndShareIt(imageBitmapTemp, qrCode, "jpeg");

    }


    public Uri storeImageIntoCacheAndShareIt(Bitmap bitmapToStore, String nameToStoreWith, String extension) {
        File sd = getContext().getCacheDir();
        File folder = new File(sd, "/edurate/");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                folder.mkdirs();
            }
        }

        File fileName = new File(folder, nameToStoreWith + "." + extension);

        try (FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName))) {
            bitmapToStore.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            Uri uri = FileProvider.getUriForFile(this.getContext(), "com.mindorks.framework.mvvm", fileName);
            // Uri tempUri  = getImageUri(getActivity(), bitmapToStore);
            shareQrCode(uri);
            return uri;
        } catch (Exception e) {
            return null;
        }

    }


    public void ClearCacheImprovised() {
        try {
            File sd = getContext().getCacheDir();
            File folder = new File(sd, "/edurate");

            for (File c : folder.listFiles()) {
                c.delete();
            }


        } catch (Exception exe) {

        }
    }

    public int getDefaultHourOnPicker() {
        return defaultHourOnPicker;
    }

    public void setDefaultHourOnPicker(int defaultHourOnPicker) {
        this.defaultHourOnPicker = defaultHourOnPicker;
    }

    public int getDefaultMinuteOnPicker() {
        return defaultMinuteOnPicker;
    }

    public void setDefaultMinuteOnPicker(int defaultMinuteOnPicker) {
        this.defaultMinuteOnPicker = defaultMinuteOnPicker;
    }

}
