package com.mindorks.framework.mvvm.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.databinding.FragmentHomeBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements QuestionnaireListNavigator {

    private HomeViewModel homeViewModel;

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

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void goBack() {

    }


}
