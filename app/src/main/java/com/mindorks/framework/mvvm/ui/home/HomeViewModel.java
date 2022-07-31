package com.mindorks.framework.mvvm.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.firebase.FirebaseHelperImpl;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireAnswers;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

public class HomeViewModel extends BaseViewModel<QuestionnaireListNavigator> {

    private MutableLiveData<String> mText;
    private MutableLiveData<Dictionary<Integer, String>> questions;

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

    public void saveMyRatingAnswers() {
        getDataManager().insertEntityIntoSet(questionnaireAnswers, FirebaseHelperImpl.FirebaseReferences.QUESTIONNAIRE_ANSWERS);
    }

    public QuestionnaireAnswers getQuestionnaireAswers() {
        return questionnaireAnswers;
    }

}