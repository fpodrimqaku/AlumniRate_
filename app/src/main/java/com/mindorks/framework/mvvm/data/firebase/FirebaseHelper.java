package com.mindorks.framework.mvvm.data.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.mindorks.framework.mvvm.utils.Action;

public interface FirebaseHelper {
    public FirebaseUser getCurrentLoggedInUser();

    public void signInWithEmailAndPassword(String email, String password, Action onSuccess,Action onFailure);


}
