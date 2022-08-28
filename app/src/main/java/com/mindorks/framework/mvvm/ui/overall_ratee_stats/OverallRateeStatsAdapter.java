package com.mindorks.framework.mvvm.ui.overall_ratee_stats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.RateeRankingsData;

import java.util.List;

public class OverallRateeStatsAdapter extends RecyclerView.Adapter<OverallRateeStatsViewHolder> {
    List<RateeRankingsData> items;
    public OverallRateeStatsViewModel viewModel;

    public OverallRateeStatsAdapter( List<RateeRankingsData> items, OverallRateeStatsViewModel viewModel) {
        this.items = items;
        this.viewModel = viewModel;
    }


    @Override
    public OverallRateeStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.ratee_overall_ratings,
                        parent,
                        false);


        return new OverallRateeStatsViewHolder(parent.getContext(), itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull OverallRateeStatsViewHolder holder, int position) {
        RateeRankingsData item = items.get(position);
        holder.initiateItem(item);
    }

    public void updateData(List<RateeRankingsData> newItems) {

        items.clear();
        items.addAll(newItems);
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
