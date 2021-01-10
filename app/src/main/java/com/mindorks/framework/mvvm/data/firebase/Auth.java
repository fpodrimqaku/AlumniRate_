package com.mindorks.framework.mvvm.data.firebase;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.data.remote.ApiHeader;
import com.mindorks.framework.mvvm.utils.Action;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Auth  implements FirebaseHelper {
    FirebaseAuth firebaseAuth;

    @Inject
    public Auth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseUser getCurrentLoggedInUser() {
        return firebaseAuth.getCurrentUser();
    }


    public void signInWithEmailAndPassword(String email, String password, Action onSuccess, Action onFailure) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onSuccess.takeAction();
                        } else {
                            onFailure.takeAction();
                        }
                    }
                });
    }

    public void signUpWithNewUser(String email, String password,Action onSuccess, Action onFailure) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onSuccess.takeAction();
                        } else {
                            onFailure.takeAction();
                        }
                    }
                });
    }


    public User getCurrentUserSigned() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User();
        if (firebaseUser != null) {
            user.setUsername(firebaseUser.getDisplayName());
            user.setEmail(firebaseUser.getEmail());
            user.setPhotoUrl(firebaseUser.getPhotoUrl());
            user.setEmailVerified(firebaseUser.isEmailVerified());
            user.setUID(firebaseUser.getUid());
        }
        return user;
    }


}
