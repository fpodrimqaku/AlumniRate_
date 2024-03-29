
package com.mindorks.framework.mvvm.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.mindorks.framework.mvvm.MvvmApp;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.di.component.DaggerFragmentComponent;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.di.module.FragmentModule;

import javax.inject.Inject;

import butterknife.ButterKnife;


public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

    private BaseActivity mActivity;
    private View mRootView;
    private T mViewDataBinding;


    @Inject
    protected V mViewModel;


    public abstract int getBindingVariable();

    public abstract
    @LayoutRes
    int getLayoutId();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection(getBuildComponent());
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mViewDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.setLifecycleOwner(this);
       mViewDataBinding.executePendingBindings();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    public void openActivityOnTokenExpire() {
        if (mActivity != null) {
            mActivity.openActivityOnTokenExpire();
        }
    }

    public abstract void performDependencyInjection(FragmentComponent buildComponent);


    private FragmentComponent getBuildComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(((MvvmApp) (getContext().getApplicationContext())).appComponent)
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    //to be overridden but not neccessarily
    public void initialize() {
    }


    public void snackShowLong(String Text ){
        Snackbar snackbar = Snackbar
                .make(getView(), Text, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.login_janablue));
        snackbar.show();
    }

    public void snackShowShort(String Text ){
        Snackbar snackbar = Snackbar
                .make(getView(), Text, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.login_janablue));
        snackbar.show();
    }

    public void snackShowLong_ERROR(String Text ){
        Snackbar snackbar = Snackbar
                .make(getView(), Text, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.salmon_red));
        snackbar.show();
    }

    public void snackShowShort_ERROR(String Text ){
        Snackbar snackbar = Snackbar
                .make(getView(), Text, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.salmon_red));
        snackbar.show();
    }






    public void toastShowLong(String Text ){

        Toast.makeText(getActivity(),Text,Toast.LENGTH_LONG).show();
    }

    public void toastShowShort(String Text ){
        Toast.makeText(getActivity(),Text,Toast.LENGTH_SHORT).show();
    }



}
