package com.mindorks.framework.mvvm.di.component;

import com.mindorks.framework.mvvm.di.module.FragmentModule;
import com.mindorks.framework.mvvm.di.scope.FragmentScope;
import com.mindorks.framework.mvvm.ui.profile.AboutFragment;
import com.mindorks.framework.mvvm.ui.questionnaire_creation.DashboardFragment;
import com.mindorks.framework.mvvm.ui.feed.blogs.BlogFragment;
import com.mindorks.framework.mvvm.ui.feed.opensource.OpenSourceFragment;
import com.mindorks.framework.mvvm.ui.questionnaire.HomeFragment;
import com.mindorks.framework.mvvm.ui.questionnaire.scan_form.ScanFragment;
import com.mindorks.framework.mvvm.ui.notifications.NotificationsFragment;
import com.mindorks.framework.mvvm.ui.overall_ratee_stats.OverallRateeStatsFragment;
import com.mindorks.framework.mvvm.ui.personal_ratings.PersonalRatingsFragment;

import dagger.Component;


@FragmentScope
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(BlogFragment fragment);
    void inject(OpenSourceFragment fragment);
    void inject(AboutFragment fragment);
    void inject(HomeFragment fragment);
    void inject(DashboardFragment fragment);
    void inject(NotificationsFragment fragment);
    void inject(PersonalRatingsFragment fragment);
    void inject(OverallRateeStatsFragment fragment);
    void inject(ScanFragment fragment);
}
