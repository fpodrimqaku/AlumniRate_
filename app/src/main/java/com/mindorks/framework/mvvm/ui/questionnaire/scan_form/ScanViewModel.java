package com.mindorks.framework.mvvm.ui.questionnaire.scan_form;

import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

public class ScanViewModel extends BaseViewModel<ScanNavigator> {

    public ScanViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager,schedulerProvider);
    }

    public MutableLiveData<QuestionnaireOrganization> CheckIfOrganizedQestionnaireExists(String qrCode) {
        MutableLiveData<QuestionnaireOrganization> questionnaireOrganization = getDataManager().fetchQuestionnaireByQrCode(qrCode);
        return questionnaireOrganization;
    }

}
