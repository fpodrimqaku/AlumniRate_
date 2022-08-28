
package com.mindorks.framework.mvvm.ui.login;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.api.LoginRequest;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.CommonUtils;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.Observable;

import butterknife.OnClick;


public class LoginViewModel extends BaseViewModel<LoginNavigator> {




    ObservableBoolean loginUIMode = new ObservableBoolean();
    ObservableField<String> emailToResetPasswordTo =new ObservableField<>("");


    MutableLiveData<Boolean> emailSentToResetPwResult = new MutableLiveData<>(null);
    public LoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        loginUIMode.set(true);
    }

    public boolean isEmailAndPasswordValid(String email, String password) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    public void login(String email, String password,boolean loginAsRatee) {

        /*getCompositeDisposable().add(getDataManager()
                .doServerLoginApiCall(new LoginRequest.ServerLoginRequest(email, password))
                .doOnSuccess(response -> getDataManager()
                        .updateUserInfo(
                                response.getAccessToken(),
                                response.getUserId(),
                                DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                                response.getUserName(),
                                response.getUserEmail(),
                                response.getGoogleProfilePicUrl()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    getNavigator().openMainActivity();
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));*/
        setIsLoading(true);
        getDataManager().signInWithEmailAndPassword(email, password, () -> {

            getNavigator().openMainActivity();
            setIsLoading(false);
            getDataManager()
                    .updateUserInfo(
                            null,
                            null,
                            DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                            null,
                            email,
                            email);
            getDataManager().setCurrentLoginUserMode(loginAsRatee);

        }, () -> {

            Log.d("blu3", "here");
            setIsLoading(false);
        });


    }

    public void onFbLoginClick() {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doFacebookLoginApiCall(new LoginRequest.FacebookLoginRequest("test3", "test4"))
                .doOnSuccess(response -> getDataManager()
                        .updateUserInfo(
                                response.getAccessToken(),
                                response.getUserId(),
                                DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
                                response.getUserName(),
                                response.getUserEmail(),
                                response.getGoogleProfilePicUrl()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    getNavigator().openMainActivity();
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }

    public void onGoogleLoginClick() {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doGoogleLoginApiCall(new LoginRequest.GoogleLoginRequest("test1", "test1"))
                .doOnSuccess(response -> getDataManager()
                        .updateUserInfo(
                                response.getAccessToken(),
                                response.getUserId(),
                                DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                                response.getUserName(),
                                response.getUserEmail(),
                                response.getGoogleProfilePicUrl()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    getNavigator().openMainActivity();
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }

    public void onCreateAccountClick() {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doGoogleLoginApiCall(new LoginRequest.GoogleLoginRequest("test1", "test1"))
                .doOnSuccess(response -> getDataManager()
                        .updateUserInfo(
                                response.getAccessToken(),
                                response.getUserId(),
                                DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                                response.getUserName(),
                                response.getUserEmail(),
                                response.getGoogleProfilePicUrl()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    getNavigator().openMainActivity();
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }




    public void onServerLoginClick(boolean loginAsRatee) {
        getNavigator().login(loginAsRatee);
    }


    public void  sendPasswordResetEmail(){
        emailSentToResetPwResult =  getDataManager().sendPasswordResetEmail(getEmailToResetPasswordTo().get());
    }

    public ObservableField<String> getEmailToResetPasswordTo() {
        return emailToResetPasswordTo;
    }

    public void setEmailToResetPasswordTo(String emailToResetPasswordTo) {
        this.emailToResetPasswordTo.set(emailToResetPasswordTo);
    }

    public ObservableBoolean isLoginUIMode() {
        return loginUIMode;
    }


    public MutableLiveData<Boolean> getEmailSentToResetPwResult() {
        return emailSentToResetPwResult;
    }


    public void setLoginUIMode(boolean loginUIMode) {
        this.loginUIMode.set(loginUIMode);
    }


}
