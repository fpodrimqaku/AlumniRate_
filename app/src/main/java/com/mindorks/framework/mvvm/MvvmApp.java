
package com.mindorks.framework.mvvm;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.StorageReference;
import com.mindorks.framework.mvvm.di.component.AppComponent;
import com.mindorks.framework.mvvm.di.component.DaggerAppComponent;
import com.mindorks.framework.mvvm.utils.AppLogger;

import javax.inject.Inject;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;



public class MvvmApp extends Application {

    public AppComponent appComponent;


    @Override
    public void onCreate() {

        super.onCreate();


        FirebaseApp.initializeApp(this);


        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();

        appComponent.inject(this);

        AppLogger.init();

        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("1.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


    }
}
