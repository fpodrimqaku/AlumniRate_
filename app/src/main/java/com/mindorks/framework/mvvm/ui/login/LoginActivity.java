

package com.mindorks.framework.mvvm.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mindorks.framework.mvvm.BR;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.databinding.ActivityLoginBinding;
import com.mindorks.framework.mvvm.di.component.ActivityComponent;
import com.mindorks.framework.mvvm.ui.base.BaseActivity;
import com.mindorks.framework.mvvm.ui.main.MainActivity;


public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    private ActivityLoginBinding mActivityLoginBinding;

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {

        return R.layout.activity_login;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void login(boolean loginAsRatee) {
        String email = "blu3samurai@outlook.com";//mActivityLoginBinding.etEmail.getText().toString();
        String password = "Default123?";//mActivityLoginBinding.etPassword.getText().toString();
        //email = mActivityLoginBinding.etEmail.getText().toString();
       // password = mActivityLoginBinding.etPassword.getText().toString();
        if (mViewModel.isEmailAndPasswordValid(email, password)) {
            hideKeyboard();
            mViewModel.login(email, password, loginAsRatee);
        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = getViewDataBinding();
        mViewModel.setNavigator(this);
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    public void sendResetPasswordEmail(View view) {
        mViewModel.sendPasswordResetEmail();
        mViewModel.getEmailSentToResetPwResult().observe(this, (res) -> {
            if (res == Boolean.TRUE) {
                Toast.makeText(this, "Request sent successuflly!", Toast.LENGTH_SHORT).show();
                mViewModel.setEmailToResetPasswordTo("");
                mViewModel.setLoginUIMode(true);
            } else if (res == Boolean.FALSE) {
                Toast.makeText(this, "Request failed!", Toast.LENGTH_SHORT).show();

            } else {


            }

        });

    }

    public void goToCreateAccount(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }

}
