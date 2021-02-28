
package com.mindorks.framework.mvvm.ui.about;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mindorks.framework.mvvm.BR;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.databinding.FragmentAboutBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;


public class AboutFragment extends BaseFragment<FragmentAboutBinding, AboutViewModel> implements AboutNavigator {

    public static final String TAG = "AboutFragment";

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
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
}
