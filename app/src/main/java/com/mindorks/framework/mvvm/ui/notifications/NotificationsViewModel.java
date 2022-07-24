package com.mindorks.framework.mvvm.ui.notifications;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.ui.home.QuestionnaireListNavigator;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

public class NotificationsViewModel extends BaseViewModel<QuestionnaireListNavigator> {

    private MutableLiveData<String> mText;
    DataManager dataManager;
    SchedulerProvider schedulerProvider;

    public NotificationsViewModel(DataManager dataManager,
                                  SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<QuestionnaireOrganization> CheckIfOrganizedQestionnaireExists(String qrCode) {
        MutableLiveData<QuestionnaireOrganization> questionnaireOrganization = dataManager.fetchQuestionnaireByQrCode(qrCode);
        return questionnaireOrganization;
    }

}