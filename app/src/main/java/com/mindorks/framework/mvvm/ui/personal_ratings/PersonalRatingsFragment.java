
package com.mindorks.framework.mvvm.ui.personal_ratings;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.framework.mvvm.BR;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.databinding.PersonalRatingsBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.home.QuestionnaireQuestionsAdapter;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class PersonalRatingsFragment extends BaseFragment<PersonalRatingsBinding, PersonalRatingsViewModel> implements PersonalRatingsNavigator {

    public static final String TAG = "PersonalRatings";
    private RecyclerView questionnaireRecyclerView;

    public static PersonalRatingsFragment newInstance() {
        Bundle args = new Bundle();
        PersonalRatingsFragment fragment = new PersonalRatingsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.personal_ratings;
    }


    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void initiateQuestionnaireRecyclerView(View parentView) {

        questionnaireRecyclerView
                = parentView.findViewById(R.id.rv_personalRatings);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this.getActivity());

        // RecyclerViewLayoutManager = linearLayoutManager;

        questionnaireRecyclerView.setLayoutManager(
                linearLayoutManager);
        PersonalRatingsAdapter adapter = new PersonalRatingsAdapter(new ArrayList<>(), mViewModel);


        //TODO INVESTIGATE THE ABOVE ROWS LATER

        adapter.updateData(super.mViewModel.questionnaireDataCollected.values().stream().collect(Collectors.toList()));
        questionnaireRecyclerView.setLayoutManager(linearLayoutManager);

        questionnaireRecyclerView.setAdapter(adapter);

    }
}
