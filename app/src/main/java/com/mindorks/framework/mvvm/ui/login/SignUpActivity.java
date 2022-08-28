package com.mindorks.framework.mvvm.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

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
            if(x==null){}

            else if (x.equals(true) ) {
                Toast.makeText(this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed To Store User!", Toast.LENGTH_SHORT).show();

            }

        });
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }


    public void createAccount(View view) {
        mViewModel.createAccount();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == -1 && reqCode == RESULT_OK_GALLERY_PHOTO_UPLOADED) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ((ImageView) findViewById(R.id.user_image_view)).setImageBitmap(selectedImage);
                mViewModel.storeUserImage(imageUri);
                mViewModel.getImageUri().observe(this, (x) -> {
                    if (x != null)
                        mViewModel.getUser().setPhotoUrl(x);
                });

            } catch (Exception e) {
                e.printStackTrace();

            }

        } else {
            //Toast.makeText(PostImage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }


    }

    public void startImageCropper(String ImagePath) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setClassName("com.android.camera", "com.android.camera.CropImage");
        File file = new File(ImagePath);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 96);
        intent.putExtra("outputY", 96);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_OK_GALLERY_PHOTO_CROPPED);
    }


    public void addPhotoFromGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_OK_GALLERY_PHOTO_UPLOADED);
    }

}
