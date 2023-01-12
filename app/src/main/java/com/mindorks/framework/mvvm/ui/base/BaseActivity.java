
package com.mindorks.framework.mvvm.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mindorks.framework.mvvm.MvvmApp;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.di.component.ActivityComponent;
import com.mindorks.framework.mvvm.di.component.DaggerActivityComponent;
import com.mindorks.framework.mvvm.di.module.ActivityModule;
import com.mindorks.framework.mvvm.ui.login.LoginActivity;
import com.mindorks.framework.mvvm.utils.CommonUtils;
import com.mindorks.framework.mvvm.utils.NetworkUtils;

import javax.inject.Inject;




public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity
        implements BaseFragment.Callback {


    private ProgressDialog mProgressDialog;

    private T mViewDataBinding;

    @Inject
    protected V mViewModel;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();


    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection(getBuildComponent());
        super.onCreate(savedInstanceState);
        performDataBinding();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    public void openActivityOnTokenExpire() {
        startActivity(LoginActivity.newIntent(this));
        finish();
    }

    private ActivityComponent getBuildComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(((MvvmApp)getApplication()).appComponent)
                .activityModule(new ActivityModule(this))
                .build();
    }

    public abstract void performDependencyInjection(ActivityComponent buildComponent);

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    private void performDataBinding() {
        int layoutId  = getLayoutId();
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId);
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }


    public void snackShowLong(View view ,String Text ){
        Snackbar snackbar = Snackbar
                .make(view, Text, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.login_janablue));
        snackbar.show();
    }

    public void snackShowShort(View view ,String Text ){
        Snackbar snackbar = Snackbar
                .make(view, Text, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.login_janablue));
        snackbar.show();
    }



    public void snackShowLong_ERROR(View view ,String Text ){
        Snackbar snackbar = Snackbar
                .make(view, Text, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.salmon_red));
        snackbar.show();
    }

    public void snackShowShort_ERROR(View view ,String Text ){
        Snackbar snackbar = Snackbar
                .make(view, Text, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.salmon_red));
        snackbar.show();
    }



    public void toastShowLong(String Text ){

        Toast.makeText(this,Text,Toast.LENGTH_LONG).show();
    }

    public void toastShowShort(String Text ){
        Toast.makeText(this,Text,Toast.LENGTH_SHORT).show();
    }

}

