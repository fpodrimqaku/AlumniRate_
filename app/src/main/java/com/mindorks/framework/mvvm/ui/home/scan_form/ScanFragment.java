package com.mindorks.framework.mvvm.ui.home.scan_form;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.databinding.FragmentHomeBinding;
import com.mindorks.framework.mvvm.databinding.FragmentScanFormQrBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.home.HomeViewModel;
import com.mindorks.framework.mvvm.ui.home.QuestionnaireListNavigator;
import com.mindorks.framework.mvvm.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ScanFragment extends BaseFragment<FragmentScanFormQrBinding, ScanViewModel> implements ScanNavigator {

    NavController navController ;
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
         navController = ((MainActivity)this.getActivity()).getNavController();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startScanningActivity(view, savedInstanceState);
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

List<String> blackListedQrs  = new ArrayList<String>();

    public void qrCodeScannedCheck(String qrCodeScanned) {
        MutableLiveData<QuestionnaireOrganization> questionnaireOrganizationMutableLiveData = mViewModel.CheckIfOrganizedQestionnaireExists(qrCodeScanned);
        questionnaireOrganizationMutableLiveData.observe((LifecycleOwner) this, (x) -> {

            if (x != null && x.get_QRCode() != null) {

                if(  qrCodeScanned!=null &&  !qrCodeScanned.equals(x.get_QRCode())){
                    return;
                }

                mViewModel.getDataManager().setCurrentFormUID(qrCodeScanned);
                navController.navigate(R.id.navigation_home);
                Toast.makeText(getActivity(), "Questionnaire Found", Toast.LENGTH_SHORT).show();
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
        decoratedBarcodeView.decodeSingle(callback);
    }

    public void scannerPause(boolean pause) {
        if (pause) {
            decoratedBarcodeView.pause();
        } else {
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

}
