package com.mindorks.framework.mvvm.data.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireAnswers;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.utils.Action;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseHelperImpl implements FirebaseHelper {

    private static class FirebaseReferences {

        final public static String QUESTIONNAIRE_TYPE = "questionnaireTypes";
        final public static String QUESTIONNAIRE_ANSWERS = "questionnaireAnswers";

    }

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Inject
    public FirebaseHelperImpl(FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase, DatabaseReference databaseReference) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = firebaseDatabase;
        this.databaseReference = databaseReference;
    }

    public FirebaseUser getCurrentLoggedInUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signInWithEmailAndPassword(String email, String password, Action onSuccess, Action onFailure) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

    public void signUpWithNewUser(String email, String password, Action onSuccess, Action onFailure) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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


    public QuestionnaireType createQuestionnaireType(QuestionnaireType questionnaireType) {
        //todo what is questionnaire model is not valid
        //todo what if child is not added
        databaseReference.child(FirebaseReferences.QUESTIONNAIRE_TYPE).push().setValue(questionnaireType);
        return questionnaireType;
    }


    public QuestionnaireAnswers insertUserAnswers(QuestionnaireAnswers questionnaireAnswers) {
        //todo what is questionnaire model is not valid
        //todo what if child is not added
        databaseReference.child(FirebaseReferences.QUESTIONNAIRE_ANSWERS).push().setValue(questionnaireAnswers);
        return questionnaireAnswers;
    }




    public QuestionnaireAnswers initiateQuestionnaire(QuestionnaireAnswers questionnaireAnswers) {
        //todo what is questionnaire model is not valid
        //todo what if child is not added
        databaseReference.child(FirebaseReferences.QUESTIONNAIRE_ANSWERS).push().setValue(questionnaireAnswers);
        return questionnaireAnswers;
    }
}