package com.mindorks.framework.mvvm.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.local.prefs.AppPreferencesHelper;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.data.model.firebase.UserAnswer;
import com.mindorks.framework.mvvm.databinding.FragmentHomeBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.generated.callback.OnClickListener;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.main.MainActivity;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;
import com.mindorks.placeholderview.annotations.Click;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
//Author of below saying->LAURIT HAFIZI
/*
 * pjesa e viti i ri kinezt-bomba berthamore
 */

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements QuestionnaireListNavigator {


    private RecyclerView questionnaireRecyclerView;
    private MutableLiveData<String> mText;
    NavController navController;

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
        navController = ((MainActivity) this.getActivity()).getNavController();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(getLayoutId(), container, false);
        root.findViewById(R.id.submitRatingAnswers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean successful = mViewModel.saveMyRatingAnswers();
                if (successful) {
                    navController.navigate(R.id.navigation_scan_form_fragment);
                }
            }
        });

        mViewModel.getErrorTxt().observe(this.getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer textIdInResources) {
                if (textIdInResources != null)
                    Toast.makeText(getActivity(), getResources().getString(textIdInResources), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

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


        questionnaireRecyclerView.setLayoutManager(
                linearLayoutManager);
        QuestionnaireQuestionsAdapter adapter = new QuestionnaireQuestionsAdapter(new ArrayList<>(), mViewModel);

        super.mViewModel.getDataManager().getQuestions();

        super.mViewModel.getDataManager().getQuestions().observe(getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                mViewModel.getQuestionnaireAswers().getAnswers().clear();
                questions.forEach(question -> {
                    UserAnswer userAnswer = new UserAnswer(question.getQuestion());//todo changelater
                    mViewModel.getQuestionnaireAswers().getAnswers().add(userAnswer);

                });
                adapter.updateData(questions);
            }
        });
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


    public void saveMyRatingAnswers() {

        mViewModel.saveMyRatingAnswers();

    }


    public void startScanner() {
        IntentIntegrator cameraScanner = new IntentIntegrator(this.getActivity());
        cameraScanner
                //.forSupportFragment(this)
                .setPrompt("Scan the QR code!")
                .setCameraId(0)
                //.setCaptureActivity(this.getActivity().getClass())
                .setOrientationLocked(true)
                .initiateScan();

    }

}
