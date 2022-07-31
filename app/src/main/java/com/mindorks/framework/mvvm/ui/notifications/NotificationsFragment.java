package com.mindorks.framework.mvvm.ui.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LifecycleOwner;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.framework.mvvm.HomeActivity;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.databinding.FragmentNotificationsBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.home.QuestionnaireListNavigator;

import java.io.DataOutputStream;

public class NotificationsFragment extends BaseFragment<FragmentNotificationsBinding, NotificationsViewModel> implements QuestionnaireListNavigator {

    private NotificationsViewModel notificationsViewModel;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notifications;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IntentIntegrator.forSupportFragment(this).setOrientationLocked(false).initiateScan();

    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {

                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                MutableLiveData<QuestionnaireOrganization> questionnaireOrganizationMutableLiveData = notificationsViewModel.CheckIfOrganizedQestionnaireExists(intentResult.getContents());
                questionnaireOrganizationMutableLiveData.observe((LifecycleOwner) getContext(), (x) -> {

                    if (x != null && x.get_QRCode() != null) {

                        HomeActivity homeActivity  = (HomeActivity) this.getContext();
                        mViewModel.setCurrentFormScannedUID(intentResult.getContents());

                        BottomNavigationView bottomNavigationView;
                        bottomNavigationView = (BottomNavigationView) homeActivity.findViewById(R.id.nav_view);
                        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);


                    } else {
                        Toast.makeText(getContext(), "Questionnaire Not Found", Toast.LENGTH_SHORT).show();

                    }


                });
                Toast.makeText(getContext(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public void goBack() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}