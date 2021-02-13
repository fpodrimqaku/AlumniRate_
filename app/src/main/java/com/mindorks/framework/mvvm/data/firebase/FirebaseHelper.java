package com.mindorks.framework.mvvm.data.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.utils.Action;

public interface FirebaseHelper {
    public FirebaseUser getCurrentLoggedInUser();

    public void signInWithEmailAndPassword(String email, String password, Action onSuccess,Action onFailure);



    //public void signUpWithNewUser(String email, String password, Action onSuccess, Action onFailure);

    //public User getCurrentUserSigned() ;


    public QuestionnaireType createQuestionnaireType(QuestionnaireType questionnaireType);


}
