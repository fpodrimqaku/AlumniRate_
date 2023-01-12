
package com.mindorks.framework.mvvm.ui.profile;

import androidx.databinding.ObservableField;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;



public class AboutViewModel extends BaseViewModel<AboutNavigator> {

    ObservableField<User> user = new ObservableField<User>(new User());



    public AboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);


    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }


   public ObservableField<User> getUser() {
       return user;
   }
    public void setUser(User user) {
        this.user.set(user);
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
