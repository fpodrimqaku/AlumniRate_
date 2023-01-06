package com.mindorks.framework.mvvm.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.mindorks.framework.mvvm.BR;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.databinding.ActivityLoginBinding;
import com.mindorks.framework.mvvm.databinding.LayoutSignUpBinding;
import com.mindorks.framework.mvvm.di.component.ActivityComponent;
import com.mindorks.framework.mvvm.ui.base.BaseActivity;
import com.mindorks.framework.mvvm.ui.main.MainActivity;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class SignUpActivity extends BaseActivity<LayoutSignUpBinding, SignUpViewModel> implements LoginNavigator {

    private ActivityLoginBinding mActivityLoginBinding;

    MutableLiveData<User> user = new MutableLiveData<User>(new User());
    private static int RESULT_OK_GALLERY_PHOTO_UPLOADED = 111;
    private static int RESULT_OK_GALLERY_PHOTO_CROPPED = 112;


    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_sign_up;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void login(boolean loginAsProfessor) {

    }


    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.newIntent(SignUpActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);

        mViewModel.getUserSaved().observe(this, (x) -> {
            if (x == null) {
            } else if (x.equals(true)) {
                Toast.makeText(this, "Përdoruesi u regjistrua me sukses!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Regjistrimi i Përdoruesit Dështoi!", Toast.LENGTH_SHORT).show();

            }

        });
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }


    public void createAccount(View view) {
        mViewModel.validateUser();
        if (!mViewModel.getUserValidationErrors().isEmpty() && mViewModel.getUserValidationErrors().size() == 1) {
            Map.Entry<String, String> entry = mViewModel.getUserValidationErrors().entrySet().iterator().next();
            String key = entry.getKey();
            String value = entry.getValue();

            super.snackShowLong_ERROR(view, "".concat(key).concat(" ").concat(value).concat("!"));
            return;
        } else if (!mViewModel.getUserValidationErrors().isEmpty() && mViewModel.getUserValidationErrors().size() > 1) {
            super.snackShowLong_ERROR(view, "Plotësoni të gjitha fushat!");
            return;
        }

        mViewModel.createAccount();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == -1 && reqCode == RESULT_OK_GALLERY_PHOTO_UPLOADED) {
            try {
                final Uri imageUri = data.getData();
                File imageToUpload = new File(imageUri.getPath());

                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //  ((ImageView) findViewById(R.id.user_image_view)).setImageBitmap(selectedImage);
                mViewModel.setIsLoadingSecondary(true);
                mViewModel.storeUserImage(imageUri, (storedImageUri) -> {
                    mViewModel.getUser().get().setPhotoUrl(storedImageUri);
                    Glide.with(this).load(mViewModel.getUser().get().getPhotoUrl()).into((ImageView) findViewById(R.id.user_image_view));
                    mViewModel.setIsLoadingSecondary(false);
                }, () -> {
                    snackShowLong_ERROR(findViewById(R.id.user_image_view), "Ngarkimi i fotos së profilit dështoi!");
                    mViewModel.getUser().get().setPhotoUrl(null);
                    mViewModel.setIsLoadingSecondary(false);
                });

            } catch (Exception e) {
                e.printStackTrace();
                //hideLoading();
                mViewModel.setIsLoadingSecondary(false);

            }

        } else {
            snackShowLong_ERROR(findViewById(R.id.user_image_view), "Ngarkimi i fotos së profilit dështoi!");
        }

    }


    public void addPhotoFromGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_OK_GALLERY_PHOTO_UPLOADED);
    }

}
