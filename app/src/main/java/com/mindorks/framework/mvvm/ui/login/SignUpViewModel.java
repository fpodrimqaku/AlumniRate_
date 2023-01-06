package com.mindorks.framework.mvvm.ui.login;

import android.net.Uri;
import android.view.View;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.Action;
import com.mindorks.framework.mvvm.utils.ConsumerAction;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.HashMap;
import java.util.function.Consumer;

public class SignUpViewModel extends BaseViewModel<LoginNavigator> {
    HashMap<String, String> userValidationErrors = new HashMap<>();
    ObservableField<User> user = new ObservableField<User>(new User());
    ObservableField<String> password = new ObservableField<>("");

    MutableLiveData<Boolean> userSaved = new MutableLiveData<>(null);

    public SignUpViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

    }

    public void closeSignupAndRedirectToLoggedInActivity(){


    }

    public void createAccount() {
        setIsLoading(true);

        getDataManager().signUpWithNewUser(user.get(), password.get(), () -> {
            setIsLoading(false);
            userSaved.setValue(true);
        }, () -> {
            setIsLoading(false);
            userSaved.setValue(false);
        });
    }

    public void storeUserImage(Uri imageUriToStore, ConsumerAction<String> onSuccessConsumer, Action onFailureAction) {
        getDataManager().storeImage(imageUriToStore, onSuccessConsumer, onFailureAction);
    }


    public void validateUser() {
        setUserValidationErrors(user.get().validateFields());
        if(getPassword().get() == null || getPassword().get().equals(""))
            getUserValidationErrors().put("Plotësoni fushën","Fjalëkalimi");
    }

    public HashMap<String, String> getUserValidationErrors() {
        return userValidationErrors;

    }

    public void setUserValidationErrors(HashMap<String, String> userValidationErrors) {
        this.userValidationErrors = userValidationErrors;
    }


    public ObservableField<String> getPassword() {
        return password;
    }

    public void setPassword(ObservableField<String> password) {
        this.password = password;
    }


    public ObservableField<User> getUser() {
        return user;
    }

    public MutableLiveData<Boolean> getUserSaved() {
        return userSaved;
    }
}
