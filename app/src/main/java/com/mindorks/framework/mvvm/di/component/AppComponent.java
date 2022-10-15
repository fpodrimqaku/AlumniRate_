
package com.mindorks.framework.mvvm.di.component;

import android.app.Application;
import com.mindorks.framework.mvvm.MvvmApp;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.local.db.AppDatabase;
import com.mindorks.framework.mvvm.data.local.db.DbHelper;
import com.mindorks.framework.mvvm.data.local.prefs.PreferencesHelper;
import com.mindorks.framework.mvvm.data.remote.ApiHelper;

import com.mindorks.framework.mvvm.di.module.AppModule;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import dagger.BindsInstance;
import dagger.Component;


import javax.inject.Singleton;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MvvmApp app);

    DataManager getDataManager();

    SchedulerProvider getSchedulerProvider();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
