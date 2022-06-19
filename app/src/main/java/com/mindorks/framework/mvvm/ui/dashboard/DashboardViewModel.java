package com.mindorks.framework.mvvm.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.ui.home.QuestionnaireListNavigator;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

public class DashboardViewModel extends BaseViewModel<QuestionnaireListNavigator> {

    private MutableLiveData<String> mText;

    public DashboardViewModel(DataManager dataManager,
                              SchedulerProvider schedulerProvider) {
        super(dataManager,schedulerProvider);

        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
        getDataManager().getQuestions();
    }

    public LiveData<String> getText() {
        return mText;
    }
}