package com.mindorks.framework.mvvm.ui.base;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<I, V extends BaseViewHolder> extends RecyclerView.Adapter<V> {

    private final List<I> list;

    public BaseRecyclerViewAdapter(List<I> items) {
        this.list = items;

    }

    @Override
    public void onBindViewHolder(final V holder,
                                 final int position) {
        holder.onBind(position);
    }

    public void refreshAdapter() {
        this.refreshAdapter();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
