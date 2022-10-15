package com.mindorks.framework.mvvm.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mindorks.framework.mvvm.ViewModelProviderFactory;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.ui.about.AboutViewModel;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.questionnaire_creation.DashboardViewModel;
import com.mindorks.framework.mvvm.ui.feed.blogs.BlogAdapter;
import com.mindorks.framework.mvvm.ui.feed.blogs.BlogViewModel;
import com.mindorks.framework.mvvm.ui.feed.opensource.OpenSourceAdapter;
import com.mindorks.framework.mvvm.ui.feed.opensource.OpenSourceViewModel;
import com.mindorks.framework.mvvm.ui.questionnaire.HomeViewModel;
import com.mindorks.framework.mvvm.ui.questionnaire.scan_form.ScanViewModel;
import com.mindorks.framework.mvvm.ui.notifications.NotificationsViewModel;
import com.mindorks.framework.mvvm.ui.personal_ratings.PersonalRatingsViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;



@Module
public class FragmentModule {

    private BaseFragment<?, ?> fragment;

    public FragmentModule(BaseFragment<?, ?> fragment) {
        this.fragment = fragment;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    AboutViewModel provideAboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AboutViewModel> supplier = () -> new AboutViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AboutViewModel> factory = new ViewModelProviderFactory<>(AboutViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(AboutViewModel.class);
    }

    @Provides
    BlogAdapter provideBlogAdapter() {
        return new BlogAdapter(new ArrayList<>());
    }


    @Provides
    BlogViewModel provideBlogViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<BlogViewModel> supplier = () -> new BlogViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<BlogViewModel> factory = new ViewModelProviderFactory<>(BlogViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(BlogViewModel.class);
    }

    @Provides
    OpenSourceAdapter provideOpenSourceAdapter() {
        return new OpenSourceAdapter();
    }

    @Provides
    OpenSourceViewModel provideOpenSourceViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<OpenSourceViewModel> supplier = () -> new OpenSourceViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<OpenSourceViewModel> factory = new ViewModelProviderFactory<>(OpenSourceViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(OpenSourceViewModel.class);
    }


    @Provides
    HomeViewModel provideHomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<HomeViewModel> supplier = () -> new HomeViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<HomeViewModel> factory = new ViewModelProviderFactory<>(HomeViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(HomeViewModel.class);
    }

    @Provides
    DashboardViewModel provideDashboardViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<DashboardViewModel> supplier = () -> new DashboardViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<DashboardViewModel> factory = new ViewModelProviderFactory<>(DashboardViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(DashboardViewModel.class);
    }

   @Provides
    NotificationsViewModel provideNotificationsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<NotificationsViewModel> supplier = () -> new NotificationsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<NotificationsViewModel> factory = new ViewModelProviderFactory<>(NotificationsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(NotificationsViewModel.class);
    }

    @Provides
    PersonalRatingsViewModel providePersonalRatingsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<PersonalRatingsViewModel> supplier = () -> new PersonalRatingsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<PersonalRatingsViewModel> factory = new ViewModelProviderFactory<>(PersonalRatingsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(PersonalRatingsViewModel.class);
    }

    @Provides
    ScanViewModel provideScanViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ScanViewModel> supplier = () -> new ScanViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ScanViewModel> factory = new ViewModelProviderFactory<>(ScanViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ScanViewModel.class);
    }


}
