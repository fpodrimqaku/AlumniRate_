package com.mindorks.framework.mvvm.ui.questionnaire_creation;

import static android.content.Context.WINDOW_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.mindorks.framework.mvvm.databinding.FragmentDashboardBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.questionnaire.QuestionnaireListNavigator;

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

public class DashboardFragment extends BaseFragment<FragmentDashboardBinding, DashboardViewModel> implements QuestionnaireListNavigator {


    private DashboardViewModel dashboardViewModel;

    UUID uuid = UUID.randomUUID();
    WindowManager manager;
    FusedLocationProviderClient mFusedLocationClient;
int PERMISSION_ID = 101;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  mViewModel.setNavigator(this);
        mViewModel.setNavigator(this);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.manager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

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
                if(value!=null){
                    Toast.makeText(getContext(),getResources().getString(value), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    public void initiateQrCode (View root){
        final ImageView qrCode_image = (ImageView) root.findViewById(R.id.imageView_qr_code);
        String fvalue_string = "";
                                            /*  if (count > 0)
                                                  fvalue_string = fvalue.getText().toString();
                                              else fvalue_string = "empty";
                                              //  String svalue_string = svalue.getText().toString();*/
        fvalue_string= uuid.toString();
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
        initiateThings(view);
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
        return R.layout.fragment_dashboard;
    }


    public void initiateThings(View root) {
if(hasLocationPermissionsGranted()){
    requestPermissions();
}
}







    @Override
    public void goBack() {

    }


@OnClick(R.id.buttonSaveQuestionnaireOrganization)
    public void buttonSaveQuestionnaireOrganization_clicked() {
        dashboardViewModel.insertQuestionnaireOrganization();
    }

@OnClick({R.id.questionnaireTimeFrom,R.id.qestionnaireTimeTo})
    public void initiateTimeFromOrTimeTo(View view){

        int hour=5;
        int minute=5;
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                //  hour = selectedHour;
                //  minute = selectedMinute;

                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int year  = localDate.getYear();
                int month = localDate.getMonthValue();
                int day   = localDate.getDayOfMonth();

                Calendar calendarDate = Calendar.getInstance();

                calendarDate.set(Calendar.DATE,day);
                calendarDate.set(Calendar.HOUR, selectedHour);
                calendarDate.set(Calendar.MINUTE, selectedMinute);
                calendarDate.set(Calendar.SECOND, 0);
                calendarDate.set(Calendar.MILLISECOND, 0);
                calendarDate.set(Calendar.MONTH, month);
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.setTimeZone(TimeZone.getDefault());



                if(view.getId() == R.id.questionnaireTimeFrom)
                dashboardViewModel.setQuestionnaireDateFrom(calendarDate.getTime());
                else
                    dashboardViewModel.setQuestionnaireDateTo(calendarDate.getTime());

                String time="";
                time = selectedHour+":"+selectedMinute;
                ((TextView)view).setText(time);
              //  timepicker.setHour(selectedHour);
              //  timepicker.setMinute(selectedMinute);
            }
        };

     //   int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
    @OnClick({R.id.questionnaireOrganizationLocationRequired})
    public void locationRequiredCheckboxChanged (View view){
        getLastLocation();

    }


    public boolean hasLocationPermissionsGranted (){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return false;
        }
        else return true;


    }



    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        requestNewLocationData();
        // check if permissions are given
        if (checkLocationPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                          //  latitudeTextView.setText(location.getLatitude() + "");
                          //  longitTextView.setText(location.getLongitude() + "");
                           // Location mLastLocation = locationResult.getLastLocation();

                            StringBuilder locationStringBuilder = new StringBuilder("");
                            locationStringBuilder.append(location.getLatitude());
                            locationStringBuilder.append(",");
                            locationStringBuilder.append(location.getLongitude());
                            setQuestionnaireLocation(true,locationStringBuilder.toString());

                            locationStringBuilder.append("Latitude: " + location.getLatitude() + "      ");
                            locationStringBuilder.append("Longitude: " + location.getLongitude() + "");

                            Toast.makeText( getActivity(),locationStringBuilder.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    private boolean checkLocationPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            StringBuilder locationStringBuilder = new StringBuilder("");
            locationStringBuilder.append("Latitude: " + mLastLocation.getLatitude() + "      ");
            locationStringBuilder.append("Longitude: " + mLastLocation.getLongitude() + "");
            Toast.makeText( getActivity(),locationStringBuilder.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void setQuestionnaireLocation(boolean isLocationRequired, String location){
    getViewDataBinding().getViewModel().setQuestionnaireLocation(isLocationRequired,location);
    }


}
