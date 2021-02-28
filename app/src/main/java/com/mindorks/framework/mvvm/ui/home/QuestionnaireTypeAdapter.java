package com.mindorks.framework.mvvm.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;

import java.util.List;

public class QuestionnaireTypeAdapter extends RecyclerView.Adapter<QuestionnaireListItemViewHolder> {

    List<QuestionnaireType> items;

    public QuestionnaireTypeAdapter(List<QuestionnaireType> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public QuestionnaireListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.questionnaire_list_item,
                        parent,
                        false);

        // return itemView
        return new QuestionnaireListItemViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull QuestionnaireListItemViewHolder holder, int position) {
        holder.initiateItem(items.get(position));
    }

    public void updateData(List<QuestionnaireType> newItems){

        items.clear();
        items.addAll(newItems);
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
