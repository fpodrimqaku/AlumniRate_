
package com.mindorks.framework.mvvm.ui.splash;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.concurrent.TimeUnit;


public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void startSeeding() {
        getCompositeDisposable().add(getDataManager()
                .seedDatabaseQuestions()
                .flatMap(aBoolean -> getDataManager().seedDatabaseOptions())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .delay(3, TimeUnit.SECONDS)
                .subscribe(aBoolean -> {
                    decideNextActivity();
                }, throwable -> {
                    decideNextActivity();
                }));
    }

    private void decideNextActivity() {
        int o = DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType();
        int u =getDataManager().getCurrentUserLoggedInMode();
        if (getDataManager().getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            getNavigator().openLoginActivity();
        }
        else if (getDataManager().getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER.getType() &&
                getDataManager().getCurrentLoginUserMode() == true
        ) {
            getNavigator().openMainActivity();
        }
        else {
            getNavigator().openLoginActivity();
           // getNavigator().openMainActivity();
        }
    }
}
