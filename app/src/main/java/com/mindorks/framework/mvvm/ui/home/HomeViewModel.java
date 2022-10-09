package com.mindorks.framework.mvvm.ui.home;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.bumptech.glide.load.engine.Resource;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.firebase.FirebaseHelperImpl;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireAnswers;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.Dictionary;

public class HomeViewModel extends BaseViewModel<QuestionnaireListNavigator> {

    private MutableLiveData<String> mText;
    private MutableLiveData<Dictionary<Integer, String>> questions;
    private MutableLiveData<Integer> errorTxt = new MutableLiveData<>();

    public LiveData<String> getText() {
        return mText;
    }



    public QuestionnaireAnswers questionnaireAnswers;

    public HomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        this.questionnaireAnswers = new QuestionnaireAnswers();
        this.questionnaireAnswers.setQuestionnaireId(getDataManager().getCurrentFormUID());
        this.questionnaireAnswers.setUserId(getDataManager().getCurrentFormUID());//todo replace value
    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }

    public boolean saveMyRatingAnswers() {
        if (!questionnaireAnswers.isValid()) {
            errorTxt.setValue(R.string.please_fill_all_questions_before_submitting);
            return false;
        }
        boolean successful = getDataManager().insertEntityIntoSet(questionnaireAnswers, FirebaseHelperImpl.FirebaseReferences.QUESTIONNAIRE_ANSWERS);
        if (successful) {
            getDataManager().setCurrentFormUID(null);
            this.questionnaireAnswers = new QuestionnaireAnswers();
            return true;
        } else {
            return false;
        }

    }

    public QuestionnaireAnswers getQuestionnaireAswers() {
        return questionnaireAnswers;
    }


    public MutableLiveData<QuestionnaireOrganization> CheckIfOrganizedQestionnaireExists(String qrCode) {
        MutableLiveData<QuestionnaireOrganization> questionnaireOrganization = getDataManager().fetchQuestionnaireByQrCode(qrCode);
        return questionnaireOrganization;
    }

    public void setCurrentFormScannedUID(String currentFormUID) {
        getDataManager().setCurrentFormUID(currentFormUID);
    }

    public MutableLiveData<Integer> getErrorTxt() {
        return errorTxt;
    }
}