package com.mindorks.framework.mvvm.ui.questionnaire_creation;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.ui.questionnaire.QuestionnaireListNavigator;
import com.mindorks.framework.mvvm.utils.Action;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.Date;
import java.util.List;

public class DashboardViewModel extends BaseViewModel<QuestionnaireListNavigator> {

    private MutableLiveData<String> mText;
    private QuestionnaireOrganization questionnaireOrganization;
    private MutableLiveData<Integer> error = new MutableLiveData<>();


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

    public void  insertQuestionnaireOrganization(Action actionOnSuccess, Action actionOnFailure) {
        questionnaireOrganization.setRateeId(getDataManager().getCurrentLoggedInUser().getUid());
        List<Integer> errorList = questionnaireOrganization.isValid();
        if(errorList.stream().count() > 0){
            getError().setValue(errorList.get(0));
            return;
        }
        questionnaireOrganization.setCreationDateTime(new Date());
        getDataManager().insertQuestionnaireOrganization(questionnaireOrganization,
                actionOnSuccess,
                actionOnFailure
                );
    }

public void setQuestionnaireLocation (Boolean isLocationRequired ,String location){
       this.questionnaireOrganization.setLocationRequired(isLocationRequired);
        this.questionnaireOrganization.setLocation(location);

}

    public void setQuestionnaireQrCode (String qrCode){
        this.questionnaireOrganization.set_QRCode(qrCode);


    }

    public void setQuestionnaireName (String Name){
        this.questionnaireOrganization.setQuestionnaireName(Name);


    }

    public void setQuestionnaireDateFrom (Date dateFrom){
        this.questionnaireOrganization.setFromDateTime(dateFrom);


    }

    public void setQuestionnaireDateTo(Date dateTo){
        this.questionnaireOrganization.setToDateTime(dateTo);


    }

    public void setQuestionnaireLocation (String locationRequired){
        this.questionnaireOrganization.setLocation(locationRequired);
    }

    public String getQuestionnaireLocation(){
        return this.questionnaireOrganization.getLocation();


    }

    public void setQuestionnaireLocationRequired (Boolean locationRequired){
        this.questionnaireOrganization.setLocationRequired(locationRequired);
    }

    public Boolean getQuestionnaireLocationRequired(){
        return this.questionnaireOrganization.getLocationRequired();

    }




    public MutableLiveData<Integer> getError() {
        return error;
    }

    public void setError(Integer errorNum) {
        this.error.setValue(errorNum);
    }
}