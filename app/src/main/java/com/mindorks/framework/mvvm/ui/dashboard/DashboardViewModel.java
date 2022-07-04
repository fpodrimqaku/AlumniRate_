package com.mindorks.framework.mvvm.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.ui.home.QuestionnaireListNavigator;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import butterknife.OnClick;

public class DashboardViewModel extends BaseViewModel<QuestionnaireListNavigator> {

    private MutableLiveData<String> mText;
    private QuestionnaireOrganization questionnaireOrganization;

    public DashboardViewModel(DataManager dataManager,
                              SchedulerProvider schedulerProvider) {
        super(dataManager,schedulerProvider);
        questionnaireOrganization = new QuestionnaireOrganization();
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
        getDataManager().getQuestions();

    }

    public LiveData<String> getText() {
        return mText;
    }


    public QuestionnaireOrganization getQuestionnaireOrganization() {
        return questionnaireOrganization;
    }

    public void  insertQuestionnaireOrganization() {
        getDataManager().insertQuestionnaireOrganization(questionnaireOrganization);
    }

public void setQuestionnaireLocation (boolean isLocationRequired ,String location){
       this.questionnaireOrganization.setLocationRequired(isLocationRequired);
        this.questionnaireOrganization.setLocation(location);

}

    public void setQuestionnaireQrCode (String qrCode){
        this.questionnaireOrganization.set_QRCode(qrCode);


    }

    public void setQuestionnaireName (String Name){
        this.questionnaireOrganization.setQuestionnaireName(Name);


    }


}