package com.mindorks.framework.mvvm.ui.home;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.databinding.FragmentHomeBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.generated.callback.OnClickListener;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.main.MainActivity;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;
import com.mindorks.placeholderview.annotations.Click;

import java.util.ArrayList;

import butterknife.OnClick;
//Author of below saying->LAURIT HAFIZI
/*
 * pjesa e viti i ri kinezt-bomba berthamore
 */

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements QuestionnaireListNavigator {


    private RecyclerView questionnaireRecyclerView;
    private MutableLiveData<String> mText;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(getLayoutId(), container, false);
root.findViewById(R.id.submitRatingAnswers).setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(),"loop daddy",Toast.LENGTH_SHORT).show();
        mViewModel.saveMyRatingAnswers();
    }
});
        return root; }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initiateQuestionnaireRecyclerView(view);

    }

    private void initiateQuestionnaireRecyclerView(View parentView) {

        questionnaireRecyclerView
                = parentView.findViewById(R.id.rv_questionnaire_types);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this.getActivity());

        // RecyclerViewLayoutManager = linearLayoutManager;

        questionnaireRecyclerView.setLayoutManager(
                linearLayoutManager);
        QuestionnaireQuestionsAdapter adapter = new QuestionnaireQuestionsAdapter(new ArrayList<>(), mViewModel);


        //TODO INVESTIGATE THE ABOVE ROWS LATER

        adapter.updateData(super.mViewModel.getDataManager().getQuestions());
        questionnaireRecyclerView.setLayoutManager(linearLayoutManager);

        questionnaireRecyclerView.setAdapter(adapter);

    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void goBack() {
    }


    public void saveMyRatingAnswers(){
        Toast.makeText(getContext(),"loop daddy",Toast.LENGTH_SHORT).show();
        mViewModel.saveMyRatingAnswers();

    }


    public void startScanner(){
        IntentIntegrator cameraScanner  = new IntentIntegrator(this.getActivity());
        cameraScanner
                //.forSupportFragment(this)
                .setPrompt("Scan the QR code!")
                .setCameraId(0)
                //.setCaptureActivity(this.getActivity().getClass())
                .setOrientationLocked(true)
                .initiateScan();

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
                MutableLiveData<QuestionnaireOrganization> questionnaireOrganizationMutableLiveData = mViewModel.CheckIfOrganizedQestionnaireExists(intentResult.getContents());
                questionnaireOrganizationMutableLiveData.observe((LifecycleOwner) getContext(), (x) -> {

                    if (x != null && x.get_QRCode() != null) {

                        MainActivity homeActivity  = (MainActivity) this.getContext();
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





    //qr scanner
}
