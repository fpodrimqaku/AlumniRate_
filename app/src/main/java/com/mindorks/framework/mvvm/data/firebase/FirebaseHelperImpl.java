package com.mindorks.framework.mvvm.data.firebase;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireAnswers;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.utils.Action;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseHelperImpl implements FirebaseHelper {

    public static class FirebaseReferences {

        final public static String QUESTIONNAIRE_TYPE = "questionnaireTypes";
        final public static String QUESTIONNAIRE_ANSWERS = "questionnaireAnswers";
        final public static String QUESTIONNAIRE = "questionnaires";
        final public static String Questionnaire_Organizations = "questionnaireOrganizations";
        final public static String QUESTIONNAIRE_QUESTIONS = "questionnaireQuestions";

        final public static String PERSON_RATEE = "personRatee";
        final public static String ASPECT_TO_RATE = "aspectToRate";
        final public static String RATE_ORGANIZATION = "ratingOrganization";
        final public static String ASPECT_RATED = "aspectRated";

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

    @Override
    public void getQuestionnairesRealtime(java.util.function.Consumer<List<QuestionnaireType>> consumerFunction, java.util.function.Consumer<DatabaseError> consumerOnError) {

    }


    public QuestionnaireAnswers insertUserAnswers(QuestionnaireAnswers questionnaireAnswers) {
        //todo what is questionnaire model is not valid
        //todo what if child is not added
        databaseReference.child(FirebaseReferences.QUESTIONNAIRE_ANSWERS).push().setValue(questionnaireAnswers);
        return questionnaireAnswers;
    }


    public QuestionnaireOrganization initiateQuestionnaire(QuestionnaireOrganization questionnaireOrganization) {
        //todo what is questionnaire model is not valid
        //todo what if child is not added
        databaseReference.child(FirebaseReferences.Questionnaire_Organizations).push().setValue(questionnaireOrganization);
        return questionnaireOrganization;
    }


    public void getQuestionnairesRealtime(Consumer<List<QuestionnaireType>> consumerFunction, Consumer<DatabaseError> consumerOnError){
      DatabaseReference relativeDatabaseReference=  databaseReference.child(FirebaseReferences.QUESTIONNAIRE_TYPE);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              GenericTypeIndicator<HashMap<String,QuestionnaireType>> t=  new GenericTypeIndicator<HashMap<String,QuestionnaireType>>() { };
              List <QuestionnaireType> items=new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    QuestionnaireType animal = ds.getValue(QuestionnaireType.class);
                    items.add(animal);
                }

                consumerFunction.accept(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                consumerOnError.accept(databaseError);
            }

        };
        relativeDatabaseReference.addValueEventListener(postListener);
    }




   List<Question> questions =  new ArrayList<>();


    public void initiatequestions() {

        DatabaseReference relativeDatabaseReference=  databaseReference.child(FirebaseReferences.QUESTIONNAIRE_QUESTIONS);
        relativeDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    GenericTypeIndicator<HashMap<String,Question>> genericTypeIndicator=  new GenericTypeIndicator<HashMap<String,Question>>() { };
                    ArrayList <Question> items =new ArrayList<Question>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        Question item = ds.getValue(Question.class);
                        items.add(item);
                    }
                questions = items;



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public List<Question> getQuestions() {
        initiatequestions();
        return questions;
    };

    public void insertQuestions (){
        DatabaseReference relativeDatabaseReference=  databaseReference.child(FirebaseReferences.QUESTIONNAIRE_QUESTIONS);
        for (int i = 0 ;  i < 10 ; i++)
        {
            Question question = new Question();
            question.setQuestion("Pyetja " + i +" ?");
            relativeDatabaseReference.push().setValue(question);
        }


       // relativeDatabaseReference.child(FirebaseReferences.QUESTIONNAIRE_QUESTIONS).push(questions);


    }


    public void insertQuestionnaireOrganization (QuestionnaireOrganization questionnaireOrganization){
        DatabaseReference relativeDatabaseReference=  databaseReference.child(FirebaseReferences.Questionnaire_Organizations);

            relativeDatabaseReference.push().setValue(questionnaireOrganization);
        // relativeDatabaseReference.child(FirebaseReferences.QUESTIONNAIRE_QUESTIONS).push(questions);
    }

    public final <T>void insertEntityIntoSet(T entity,String setName){

        DatabaseReference relativeDatabaseReference=  databaseReference.child(setName);

        relativeDatabaseReference.push().setValue(entity);
    }



}




/*    private <T>void bindOnChangeValue(DatabaseReference relativeDatabaseReference,Consumer<T> consumerFunction, Consumer<DatabaseError> consumerOnError){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                consumerFunction.accept( dataSnapshot.getValue(Class <List<T>> objectsrEC ));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
             consumerOnError.accept(databaseError);
            }


        };
        relativeDatabaseReference.addValueEventListener(postListener);
    }*/

/*

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            Post post = dataSnapshot.getValue(Post.class);
            // ..
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };
mPostReference.addValueEventListener(postListener);*/
