package com.mindorks.framework.mvvm.ui.overall_ratee_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.RateeRankingsData;
import com.mindorks.framework.mvvm.data.model.firebase.UserAnswerData;
import com.mindorks.framework.mvvm.databinding.FragmentRateeOverallRatingsBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import butterknife.ButterKnife;

public class OverallRateeStatsFragment extends BaseFragment<FragmentRateeOverallRatingsBinding, OverallRateeStatsViewModel> implements OverallRateeStatsNavigator {

    public RecyclerView rateeOverallStatsRecyclerView;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        // mViewModel.setNavigator(this);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_ratee_overall_ratings, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initiateOverallUserDataStats(view);
    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ratee_overall_ratings;
    }

    @Override
    public void goBack() {

    }

    private void initiateOverallUserDataStats(View parentView) {

        rateeOverallStatsRecyclerView
                = parentView.findViewById(R.id.rv_ratee_overall_ratings);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this.getActivity());

        rateeOverallStatsRecyclerView.setLayoutManager(
                linearLayoutManager);
        OverallRateeStatsAdapter adapter = new OverallRateeStatsAdapter(new ArrayList<>(), mViewModel);


        //TODO INVESTIGATE THE ABOVE ROWS LATER

        super.mViewModel.getRateeRankingsData().observe(this.getActivity(), new Observer<ConcurrentMap<String, RateeRankingsData>>() {
            @Override
            public void onChanged(ConcurrentMap<String, RateeRankingsData> stringQuestionnaireDataCollectedConcurrentMap) {
                adapter.updateData(stringQuestionnaireDataCollectedConcurrentMap.values().stream().collect(Collectors.toList()));

            }
        });

        rateeOverallStatsRecyclerView.setLayoutManager(linearLayoutManager);

        rateeOverallStatsRecyclerView.setAdapter(adapter);

    }

}
