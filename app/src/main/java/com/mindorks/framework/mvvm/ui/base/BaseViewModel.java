

package com.mindorks.framework.mvvm.ui.base;

import androidx.lifecycle.ViewModel;
import androidx.databinding.ObservableBoolean;
import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import java.lang.ref.WeakReference;


public abstract class BaseViewModel<N> extends ViewModel {

    private final DataManager mDataManager;

    private final ObservableBoolean mIsLoading = new ObservableBoolean();
    private final ObservableBoolean secondaryLoading = new ObservableBoolean();

    private final SchedulerProvider mSchedulerProvider;

    private CompositeDisposable mCompositeDisposable;

    private WeakReference<N> mNavigator;

    public BaseViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider; 
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {

        mIsLoading.set(isLoading);
    }


    public ObservableBoolean getIsLoadingSecondary() {
        return secondaryLoading;
    }

    public void setIsLoadingSecondary(boolean secondaryLoading) {

        this.secondaryLoading.set(secondaryLoading);
    }




    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }
}
