package com.mindorks.framework.mvvm.ui.questionnaire.scan_form;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.databinding.FragmentScanFormQrBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScanFragment extends BaseFragment<FragmentScanFormQrBinding, ScanViewModel> implements ScanNavigator {

    NavController navController;
    int PERMISSION_ID = 101;
    Double currentLatitude = 0.0;
    Double currentLongitude = 0.0;
    WindowManager manager;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_scan_form_qr;
    }


    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = ((MainActivity) this.getActivity()).getNavController();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startScanningActivity(view, savedInstanceState);
        checkIfAppHasCameraPermissionAndRequestIt();
        checkIfAppHasLocationPermissionAndRequestIt();
        listenForFormUIDFromScanner();
    }


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            qrCodeScannedCheck(result.getText());
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    List<String> blackListedQrs = new ArrayList<String>();

    public void qrCodeScannedCheck(String qrCodeScanned) {
        suspendScannerForTimeSpecified(3);
        MutableLiveData<QuestionnaireOrganization> questionnaireOrganizationMutableLiveData = mViewModel.CheckIfOrganizedQestionnaireExists(qrCodeScanned);
        questionnaireOrganizationMutableLiveData.observe((LifecycleOwner) this, (x) -> {

            if (x != null && x.get_QRCode() != null) {

                if (qrCodeScanned != null && !qrCodeScanned.equals(x.get_QRCode())) {
                    return;
                }
                Date currentDateTime  = new Date();

                if(mViewModel.UserHasFilledThequestionnaireBefore(x.get_QRCode())){
                    snackShowLong(getResources().getString(R.string.questionnaire_filled_before));
                    return;
                }


                if(!(currentDateTime.after(x.getFromDateTime()) && currentDateTime.before(x.getToDateTime()))){

                    snackShowLong(getResources().getString(R.string.questionnaire_org_out_of_time));
                    return;
                }

                 if (x.getLocationRequired() != null && x.getLocationRequired() == true) {
                    String[] coo = x.getLocation().split(",");
                    double latitude = Double.parseDouble(coo[0]);
                    double longitude = Double.parseDouble(coo[1]);


                    if (getDistanceBetweenTwoCordinates(currentLatitude, latitude, currentLongitude, longitude) > 300) {
                        snackShowLong(getResources().getString(R.string.questionnaire_far_from_spot));
                    } else {
                        mViewModel.getDataManager().setCurrentFormUID(qrCodeScanned);

                        navController.navigate(R.id.navigation_home);
                        Toast.makeText(getActivity(), R.string.questionnaire_found, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mViewModel.getDataManager().setCurrentFormUID(qrCodeScanned);

                    navController.navigate(R.id.navigation_home);
                    Toast.makeText(getActivity(), R.string.questionnaire_found, Toast.LENGTH_SHORT).show();

                }


            } else {
                Toast.makeText(getActivity(), "Questionnaire Not Found", Toast.LENGTH_SHORT).show();

            }
            decoratedBarcodeView.decodeSingle(callback);
        });
    }


    public void listenForFormUIDFromScanner() {

        mViewModel.getDataManager().getCurrentBarcodeScanned().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (mViewModel.getDataManager().getCurrentFormUID() == null)
                    qrCodeScannedCheck(s);
            }
        });

    }

    DecoratedBarcodeView decoratedBarcodeView;

    public void startScanningActivity(View view, Bundle savedInstanceState) {
        decoratedBarcodeView = view.findViewById(R.id.zxing_barcode_scanner);
        decoratedBarcodeView.setStatusText(getResources().getString(R.string.scan_to_go));
        decoratedBarcodeView.decodeSingle(callback);
    }

    public void scannerPause(boolean pause) {
        if (pause) {
            decoratedBarcodeView.pause();
        } else if(checkLocationPermissions()){
            decoratedBarcodeView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.getDataManager().setCurrentFormUID(null);
    }

    @Override
    public void onResume() {
        scannerPause(false);
        super.onResume();
    }

    @Override
    public void onPause() {

        scannerPause(true);
        super.onPause();
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if(checkLocationPermissions())
                    scannerPause(true);
                    else scannerPause(false);
                } else {
                    scannerPause(true);
                    snackShowLong(getResources().getString(R.string.permission_camera_request));

                }
            });


    public void checkIfAppHasCameraPermissionAndRequestIt() {

        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            if(checkLocationPermissions()==false){
                checkIfAppHasLocationPermissionAndRequestIt();
            }

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA);
        }

    }


    //location permissions and retrieving current location


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
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();


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
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

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
           // Toast.makeText(getActivity(), locationStringBuilder.toString(), Toast.LENGTH_SHORT).show();
        }
    };


    private ActivityResultLauncher<String> locationRequestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getLastLocation();

                } else {

                    snackShowLong(getResources().getString(R.string.permission_location_request));

                }
            });




    public void checkIfAppHasLocationPermissionAndRequestIt() {

        if (checkLocationPermissions()) {

            getLastLocation();
            scannerPause(false);

        } else if (shouldShowRequestPermissionRationale(getResources().getString(R.string.permission_location_request))) {
            scannerPause(true);
        } else {
            locationRequestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
            scannerPause(true);
        }

    }

    public void  suspendScannerForTimeSpecified(int seconds){
        scannerPause(true);


        Observable.just("")
                .delay(3, TimeUnit.SECONDS)
                /*for doing at background*/
                .subscribeOn(Schedulers.io())
                /*for responsing at maint thread*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    scannerPause(false);
                });




    }


    public static double getDistanceBetweenTwoCordinates(double lat1, double lat2, double lon1,
                                                         double lon2) {
        double el1 = 0;
        double el2 = 0;

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


}
