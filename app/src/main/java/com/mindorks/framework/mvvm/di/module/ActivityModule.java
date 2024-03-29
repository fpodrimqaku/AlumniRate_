package com.mindorks.framework.mvvm.di.module;


import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.mindorks.framework.mvvm.ViewModelProviderFactory;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.ui.base.BaseActivity;
import com.mindorks.framework.mvvm.ui.feed.FeedPagerAdapter;
import com.mindorks.framework.mvvm.ui.feed.FeedViewModel;
import com.mindorks.framework.mvvm.ui.login.LoginViewModel;
import com.mindorks.framework.mvvm.ui.login.SignUpViewModel;
import com.mindorks.framework.mvvm.ui.main.MainViewModel;
import com.mindorks.framework.mvvm.ui.overall_ratee_stats.OverallRateeStatsViewModel;
import com.mindorks.framework.mvvm.ui.personal_ratings.PersonalRatingsViewModel;
import com.mindorks.framework.mvvm.ui.splash.SplashViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;



@Module
public class ActivityModule {
    private BaseActivity<?, ?> activity;

    public ActivityModule(BaseActivity<?, ?> activity) {
        this.activity = activity;
    }

    @Provides
    FeedPagerAdapter provideFeedPagerAdapter() {
        return new FeedPagerAdapter(activity.getSupportFragmentManager());
    }

    @Provides
    FeedViewModel provideFeedViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<FeedViewModel> supplier = () -> new FeedViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<FeedViewModel> factory = new ViewModelProviderFactory<>(FeedViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(FeedViewModel.class);
    }

    @Provides
    MainViewModel provideMainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MainViewModel> supplier = () -> new MainViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MainViewModel> factory = new ViewModelProviderFactory<>(MainViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MainViewModel.class);
    }

    @Provides
    LoginViewModel provideLoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<LoginViewModel> supplier = () -> new LoginViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<LoginViewModel> factory = new ViewModelProviderFactory<>(LoginViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LoginViewModel.class);
    }

    @Provides
    SplashViewModel provideSplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<SplashViewModel> supplier = () -> new SplashViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<SplashViewModel> factory = new ViewModelProviderFactory<>(SplashViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SplashViewModel.class);
    }

    @Provides
    SignUpViewModel provideSignUpViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<SignUpViewModel> supplier = () -> new SignUpViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<SignUpViewModel> factory = new ViewModelProviderFactory<>(SignUpViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SignUpViewModel.class);
    }


    @Provides
    OverallRateeStatsViewModel provideOverallRateeStatsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<OverallRateeStatsViewModel> supplier = () -> new OverallRateeStatsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<OverallRateeStatsViewModel> factory = new ViewModelProviderFactory<>(OverallRateeStatsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(OverallRateeStatsViewModel.class);
    }


}
