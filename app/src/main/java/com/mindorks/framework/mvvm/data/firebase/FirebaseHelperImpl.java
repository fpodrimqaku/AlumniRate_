package com.mindorks.framework.mvvm.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.utils.Action;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseHelperImpl implements FirebaseHelper {

    private static class FirebaseReferences {

        final public static String QUESTIONNAIRE_TYPE = "questionnaireTypes";
        final public static String QUESTIONNAIRE_ANSWERS = "questionnaireTypes";


    }

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Inject
    public FirebaseHelperImpl(FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = firebaseDatabase;
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
        DatabaseReference databaseReference = firebaseDatabase.getReference("vbeat");
        String value=databaseReference.getKey();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            Log.e("TAG", "" + snapshot.getValue()); // your name values you will get here
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", " it's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                int k=0;
            }
        });



        //todo what if child is not added
        return questionnaireType;
    }


}
