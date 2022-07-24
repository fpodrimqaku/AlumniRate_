package com.mindorks.framework.mvvm.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.databinding.FragmentHomeBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.placeholderview.annotations.Click;

import java.util.ArrayList;
//Author of below saying->LAURIT HAFIZI
/*
 * pjesa e viti i ri kinezt-bomba berthamore
 * */

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements QuestionnaireListNavigator {


    private RecyclerView questionnaireRecyclerView;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);


    }
/*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        mBlogAdapter.setListener(this);
    }

    @Override
    public void onRetryClick() {
        mViewModel.fetchBlogs();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentBlogBinding = getViewDataBinding();
        setUp();
    }
    */


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(getLayoutId(), container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initiateQuestionnaireRecyclerView(view);

    }

    private void initiateQuestionnaireRecyclerView(View parentView) {

        questionnaireRecyclerView
                = parentView.findViewById(R.id.rv_questionnaire_types);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this.getActivity());

        // RecyclerViewLayoutManager = linearLayoutManager;

        questionnaireRecyclerView.setLayoutManager(
                linearLayoutManager);
        QuestionnaireQuestionsAdapter adapter = new QuestionnaireQuestionsAdapter(new ArrayList<>(),mViewModel);

      /*  super.mViewModel.getDataManager().getQuestions((result) -> {

            adapter.updateData(result);
            // adapter.notifyDataSetChanged();

        }, (error) -> {
            Log.d("blu3", "data set updation failed" + error.getDetails());
        });
*/
        //TODO INVESTIGATE THE ABOVE ROWS LATER

        adapter.updateData(super.mViewModel.getDataManager().getQuestions());
        questionnaireRecyclerView.setLayoutManager(linearLayoutManager);

        questionnaireRecyclerView.setAdapter(adapter);

    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void goBack() {
    }
    @Click(R.id.submitRatingAnswers)
    public void saveMyRatingAnswers(View view){
mViewModel.saveMyRatingAnswers();

    }
}
