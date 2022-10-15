package com.mindorks.framework.mvvm.ui.personal_ratings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;

import java.util.List;

public class PersonalRatingsAdapter extends RecyclerView.Adapter<PersonalRatingsListItemViewHolder> {

    List<QuestionnaireDataCollected> items;
    public PersonalRatingsViewModel viewModel;

    public PersonalRatingsAdapter(List<QuestionnaireDataCollected> items, PersonalRatingsViewModel viewModel) {
        this.items = items;
        this.viewModel = viewModel;
    }


    @Override
    public PersonalRatingsListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.questionnaire_organizations_collected_data_list_item,
                        parent,
                        false);


        return new PersonalRatingsListItemViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull PersonalRatingsListItemViewHolder holder, int position) {
        QuestionnaireDataCollected item = items.get(position);
        holder.initiateItem(item);
    }

    public void updateData(List<QuestionnaireDataCollected> newItems) {

        items.clear();
        items.addAll(newItems);
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
