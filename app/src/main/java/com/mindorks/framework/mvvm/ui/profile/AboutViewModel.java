
package com.mindorks.framework.mvvm.ui.profile;

import androidx.databinding.ObservableField;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;



public class AboutViewModel extends BaseViewModel<AboutNavigator> {

    User user = new User();



    public AboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);


    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }


    public ObservableField<String> getFirst() {
        return new ObservableField<String>(user.getFirst());
    }



    public ObservableField<String> getLast() {
        return new ObservableField<String>(user.getLast());
    }

    public ObservableField<String> getUsername() {
        return new ObservableField<String>(user.getUsername());
    }


    public ObservableField<String> getTitle() {
        return new ObservableField<String>(user.getTitle());
    }

    public ObservableField<String> getPhotoUri() {
        return new ObservableField<String>(user.getPhotoUrl());
    }

    public ObservableField<String> getEmail() {
        return new ObservableField<String>(user.getEmail());
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
         this.user = user;
    }



    public ObservableField<String> getFullName(){
        return new ObservableField<String>(user.getFirst()+" "+user.getLast());

    }

    public void logout(){

            getDataManager().setCurrentLoginUserMode(false);
            getDataManager()
                    .updateUserInfo(
                            null,
                            null,
                            DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT,
                            null,
                            null,
                            null);

    }

}
