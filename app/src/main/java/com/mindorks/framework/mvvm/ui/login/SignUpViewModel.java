package com.mindorks.framework.mvvm.ui.login;

import android.net.Uri;
import android.view.View;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

public class SignUpViewModel extends BaseViewModel<LoginNavigator> {
    User user = new User();
    ObservableField<String> email = new ObservableField<>("");
    ObservableField<String> password = new ObservableField<>("");
    ObservableField<String> title = new ObservableField<>("");
    ObservableField<String> name = new ObservableField<>("");
    ObservableField<String> lastName = new ObservableField<>("");
    MutableLiveData<String> imageUri = new MutableLiveData<>(null);
    MutableLiveData<Boolean> userSaved = new MutableLiveData<>(null);

    public SignUpViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

    }

    public void createAccount() {

        user.setEmail(email.get());

        user.setTitle(title.get());
        user.setFirst(name.get());
        user.setLast(lastName.get());

        setIsLoading(true);
        getDataManager().signUpWithNewUser(user, password.get(), () -> {
            setIsLoading(false);
            userSaved.setValue(true);
        }, () -> {
            setIsLoading(false);
            userSaved.setValue(false);
        });
    }

    public void storeUserImage(Uri imageUriToStore) {
        imageUri = getDataManager().storeImage(imageUriToStore);

    }

    public ObservableField<String> getEmail() {
        return email;
    }

    public void setEmail(ObservableField<String> email) {
        this.email = email;
    }

    public ObservableField<String> getPassword() {
        return password;
    }

    public void setPassword(ObservableField<String> password) {
        this.password = password;
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public void setTitle(ObservableField<String> title) {
        this.title = title;
    }

    public ObservableField<String> getName() {
        return name;
    }

    public void setName(ObservableField<String> name) {
        this.name = name;
    }

    public ObservableField<String> getLastName() {
        return lastName;
    }

    public void setLastName(ObservableField<String> lastName) {
        this.lastName = lastName;
    }

    public MutableLiveData<String> getImageUri() {
        return imageUri;
    }

    public User getUser() {
        return user;
    }

    public MutableLiveData<Boolean> getUserSaved() {
        return userSaved;
    }
}
