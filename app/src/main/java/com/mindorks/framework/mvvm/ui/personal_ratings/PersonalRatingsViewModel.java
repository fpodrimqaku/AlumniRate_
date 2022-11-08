
package com.mindorks.framework.mvvm.ui.personal_ratings;

import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.List;
import java.util.concurrent.ConcurrentMap;


public class PersonalRatingsViewModel extends BaseViewModel<PersonalRatingsNavigator> {

    MutableLiveData<ConcurrentMap<String, QuestionnaireDataCollected>>  questionnaireDataCollected ;

    public PersonalRatingsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
getDataManager().initiatefetchingQuestionnaireDataCollected("");
        questionnaireDataCollected =getDataManager().fetchQuestionnaireDataCollected();
    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }
}
