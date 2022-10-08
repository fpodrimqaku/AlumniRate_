

package com.mindorks.framework.mvvm.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import android.text.TextUtils;
import android.util.Log;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.data.model.others.QuestionCardData;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;
import java.util.List;


public class MainViewModel extends BaseViewModel<MainNavigator> {

    private static final String TAG = "MainViewModel";

    public static final int NO_ACTION = -1, ACTION_ADD_ALL = 0, ACTION_DELETE_SINGLE = 1;

    private final ObservableField<String> appVersion = new ObservableField<>();

    private final MutableLiveData<List<QuestionCardData>> questionCardData;

    private final ObservableList<QuestionCardData> questionDataList = new ObservableArrayList<>();

    private final ObservableField<String> userEmail = new ObservableField<>();

    private final ObservableField<String> userName = new ObservableField<>();

    private final ObservableField<String> userProfilePicUrl = new ObservableField<>();

    private int action = NO_ACTION;



    private MutableLiveData<String> mText;

    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        questionCardData = new MutableLiveData<>();
        loadQuestionCards();
    }

    public int getAction() {
        return action;
    }

    public ObservableField<String> getAppVersion() {
        return appVersion;
    }

    public LiveData<List<QuestionCardData>> getQuestionCardData() {
        return questionCardData;
    }

    public ObservableList<QuestionCardData> getQuestionDataList() {
        return questionDataList;
    }

    public void setQuestionDataList(List<QuestionCardData> questionCardDatas) {
        action = ACTION_ADD_ALL;
        questionDataList.clear();
        questionDataList.addAll(questionCardDatas);
    }

    public ObservableField<String> getUserEmail() {
        return userEmail;
    }

    public ObservableField<String> getUserName() {
        return userName;
    }

    public ObservableField<String> getUserProfilePicUrl() {
        return userProfilePicUrl;
    }

    public void loadQuestionCards() {
        getCompositeDisposable().add(getDataManager()
                .getQuestionCardData()
                .doOnNext(list -> Log.d(TAG, "loadQuestionCards: " + list.size()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(questionList -> {
                    if (questionList != null) {
                        Log.d(TAG, "loadQuestionCards: " + questionList.size());
                        action = ACTION_ADD_ALL;
                        questionCardData.setValue(questionList);
                    }
                }, throwable -> {
                    Log.d(TAG, "loadQuestionCards: " + throwable);
                }));
    }

    public void logout() {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager().doLogoutApiCall()
                .doOnSuccess(response -> getDataManager().setUserAsLoggedOut())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    getNavigator().openLoginActivity();
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }

    public void onNavMenuCreated() {
        final String currentUserName = getDataManager().getCurrentUserName();
        if (!TextUtils.isEmpty(currentUserName)) {
            userName.set(currentUserName);
        }

        final String currentUserEmail = getDataManager().getCurrentUserEmail();
        if (!TextUtils.isEmpty(currentUserEmail)) {
            userEmail.set(currentUserEmail);
        }

        final String profilePicUrl = getDataManager().getCurrentUserProfilePicUrl();
        if (!TextUtils.isEmpty(profilePicUrl)) {
            userProfilePicUrl.set(profilePicUrl);
        }
    }

    public void removeQuestionCard() {
        action = ACTION_DELETE_SINGLE;
        questionCardData.getValue().remove(0);
    }

    public void updateAppVersion(String version) {
        appVersion.set(version);
    }


    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<QuestionnaireOrganization> CheckIfOrganizedQestionnaireExists(String qrCode) {
        MutableLiveData<QuestionnaireOrganization> questionnaireOrganization = getDataManager().fetchQuestionnaireByQrCode(qrCode);
        return questionnaireOrganization;
    }

    public void setCurrentFormScannedUID(String currentFormUID){
        getDataManager().setCurrentFormUID(currentFormUID);
    }
}
