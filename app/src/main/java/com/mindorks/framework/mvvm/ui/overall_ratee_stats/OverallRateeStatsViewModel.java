package com.mindorks.framework.mvvm.ui.overall_ratee_stats;

import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.RateeRankingsData;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;


public class OverallRateeStatsViewModel extends BaseViewModel<OverallRateeStatsNavigator> {


    MutableLiveData<ConcurrentMap<String, RateeRankingsData>> rateeRankingsData = new MutableLiveData<>(new ConcurrentHashMap<String, RateeRankingsData>(1));

    @Inject
    public OverallRateeStatsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        rateeRankingsData= getDataManager().fetchRateeRankingsData();
    }

    public MutableLiveData<ConcurrentMap<String, RateeRankingsData>> getRateeRankingsData() {
        return rateeRankingsData;
    }
}
