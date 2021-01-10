package com.mindorks.framework.mvvm.data.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.mindorks.framework.mvvm.utils.Action;

import java.util.concurrent.Executor;

public interface FirebaseHelper {
    public FirebaseUser getCurrentLoggedInUser();

    public void signInWithEmailAndPassword(String email, String password, Action onSuccess,Action onFailure);
}
