package com.mindorks.framework.mvvm.di.component;

import com.mindorks.framework.mvvm.di.module.FragmentModule;
import com.mindorks.framework.mvvm.di.scope.FragmentScope;
import com.mindorks.framework.mvvm.ui.about.AboutFragment;
import com.mindorks.framework.mvvm.ui.dashboard.DashboardFragment;
import com.mindorks.framework.mvvm.ui.feed.blogs.BlogFragment;
import com.mindorks.framework.mvvm.ui.feed.opensource.OpenSourceFragment;
import com.mindorks.framework.mvvm.ui.home.HomeFragment;
import com.mindorks.framework.mvvm.ui.notifications.NotificationsFragment;

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
}
