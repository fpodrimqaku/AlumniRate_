

package com.mindorks.framework.mvvm.di.module;

import android.app.Application;
import androidx.room.Room;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mindorks.framework.mvvm.BuildConfig;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.AppDataManager;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.firebase.FirebaseHelperImpl;
import com.mindorks.framework.mvvm.data.firebase.FirebaseHelper;
import com.mindorks.framework.mvvm.data.local.db.AppDatabase;
import com.mindorks.framework.mvvm.data.local.db.AppDbHelper;
import com.mindorks.framework.mvvm.data.local.db.DbHelper;
import com.mindorks.framework.mvvm.data.local.prefs.AppPreferencesHelper;
import com.mindorks.framework.mvvm.data.local.prefs.PreferencesHelper;
import com.mindorks.framework.mvvm.data.remote.ApiHeader;
import com.mindorks.framework.mvvm.data.remote.ApiHelper;
import com.mindorks.framework.mvvm.data.remote.AppApiHelper;
import com.mindorks.framework.mvvm.di.ApiInfo;
import com.mindorks.framework.mvvm.di.DatabaseInfo;
import com.mindorks.framework.mvvm.di.PreferenceInfo;
import com.mindorks.framework.mvvm.utils.AppConstants;
import com.mindorks.framework.mvvm.utils.rx.AppSchedulerProvider;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

import io.github.inflationx.calligraphy3.CalligraphyConfig;


@Module
public class AppModule {

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, dbName).fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {

        return application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@ApiInfo String apiKey,
                                                           PreferencesHelper preferencesHelper) {
        return new ApiHeader.ProtectedApiHeader(
                apiKey,
                preferencesHelper.getCurrentUserId(),
                preferencesHelper.getAccessToken());
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {


        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    FirebaseStorage provideStorageReference(FirebaseStorage firebaseStorage) {
        return firebaseStorage;
    }

    @Provides
    @Singleton
    FirebaseHelper provideFirebaseHelper(FirebaseHelperImpl firebaseAccess) {
        return firebaseAccess;
    }

    @Provides
    @Singleton
    FirebaseDatabase provideFirebaseDatabase() {
        return  FirebaseDatabase.getInstance();
    }


    @Provides
    @Singleton
    DatabaseReference provideDatabaseReference(FirebaseDatabase firebaseDatabase) {
        return  firebaseDatabase.getReference();
    }


}
