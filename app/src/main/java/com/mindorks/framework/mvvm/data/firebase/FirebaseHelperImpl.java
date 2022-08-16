package com.mindorks.framework.mvvm.data.firebase;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireAnswers;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.data.model.firebase.UserAnswer;
import com.mindorks.framework.mvvm.data.model.firebase.UserAnswerData;
import com.mindorks.framework.mvvm.utils.Action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        final public static String QUESTIONNAIRE_DATA_COLLECTED = "questionnaireDataCollected";

    }

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Inject
    public FirebaseHelperImpl(FirebaseAuth firebaseAuth,
                              FirebaseDatabase firebaseDatabase,
                              DatabaseReference databaseReference
    ) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = firebaseDatabase;
        this.databaseReference = databaseReference;
this.storageReference = FirebaseStorage.getInstance().getReference();
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


     MutableLiveData <Boolean> passwordResetEmailSentSuccessfully  = new MutableLiveData<>(null);
    public MutableLiveData <Boolean>  sendPasswordResetEmail(String email) {

        firebaseAuth.sendPasswordResetEmail(email).
                addOnCompleteListener(  new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            passwordResetEmailSentSuccessfully.setValue(Boolean.TRUE);
                        } else {
                            passwordResetEmailSentSuccessfully.setValue(Boolean.FALSE);
                        }
                    }


                });
        return passwordResetEmailSentSuccessfully;
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


    public void getQuestionnairesRealtime(Consumer<List<QuestionnaireType>> consumerFunction, Consumer<DatabaseError> consumerOnError) {
        DatabaseReference relativeDatabaseReference = databaseReference.child(FirebaseReferences.QUESTIONNAIRE_TYPE);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, QuestionnaireType>> t = new GenericTypeIndicator<HashMap<String, QuestionnaireType>>() {
                };
                List<QuestionnaireType> items = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
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


    List<Question> questions = new ArrayList<>();


    public void initiatequestions() {

        DatabaseReference relativeDatabaseReference = databaseReference.child(FirebaseReferences.QUESTIONNAIRE_QUESTIONS);
        relativeDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<HashMap<String, Question>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, Question>>() {
                };
                ArrayList<Question> items = new ArrayList<Question>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
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
    }

    ;

    public void insertQuestions() {
        DatabaseReference relativeDatabaseReference = databaseReference.child(FirebaseReferences.QUESTIONNAIRE_QUESTIONS);
        for (int i = 0; i < 10; i++) {
            Question question = new Question();
            question.setQuestion("Pyetja " + i + " ?");
            relativeDatabaseReference.push().setValue(question);
        }


        // relativeDatabaseReference.child(FirebaseReferences.QUESTIONNAIRE_QUESTIONS).push(questions);


    }


    public void insertQuestionnaireOrganization(QuestionnaireOrganization questionnaireOrganization) {
        DatabaseReference relativeDatabaseReference = databaseReference.child(FirebaseReferences.Questionnaire_Organizations);

        // relativeDatabaseReference.push().setValue(questionnaireOrganization);
        relativeDatabaseReference.child(questionnaireOrganization.get_QRCode()).setValue(questionnaireOrganization);
        // relativeDatabaseReference.child(FirebaseReferences.QUESTIONNAIRE_QUESTIONS).push(questions);
    }

    public final <T> boolean insertEntityIntoSet(T entity, String setName) {
        try {
            DatabaseReference relativeDatabaseReference = databaseReference.child(setName);
            relativeDatabaseReference.push().setValue(entity);
        } catch (Exception exception) {
            return false;

        }
        return true;
    }

    MutableLiveData<QuestionnaireOrganization> questionnaireOrganizationMutableLiveData = new MutableLiveData<>();

    public void addListenerToGetQuestionnaireByQrCode(String qrCode) {
        DatabaseReference relativeDatabaseReference = databaseReference.child(FirebaseReferences.Questionnaire_Organizations).child(qrCode);

        // QuestionnaireOrganization questionnaireOrganization =  relativeDatabaseReference.child(qrCode);

        relativeDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                QuestionnaireOrganization questionnaireOrganization = snapshot.getValue(QuestionnaireOrganization.class);
                if (questionnaireOrganization == null) {
                    QuestionnaireOrganization dummyQuestionnaireOrganization = new QuestionnaireOrganization();
                    questionnaireOrganizationMutableLiveData.setValue(dummyQuestionnaireOrganization);
                } else
                    questionnaireOrganizationMutableLiveData.setValue(questionnaireOrganization);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // return questionnaireOrganization;
    }

    public MutableLiveData<QuestionnaireOrganization> fetchQuestionnaireByQrCode(String qrCode) {
        addListenerToGetQuestionnaireByQrCode(qrCode);
        return questionnaireOrganizationMutableLiveData;
    }

   // ConcurrentMap<String, QuestionnaireDataCollected> questionnaireDataCollected = new ConcurrentHashMap<>();
   MutableLiveData<ConcurrentMap<String, QuestionnaireDataCollected>> questionnaireDataCollected =new MutableLiveData<>(new ConcurrentHashMap<>());
    public MutableLiveData<ConcurrentMap<String, QuestionnaireDataCollected>> fetchQuestionnaireDataCollected(String userId) {

        DatabaseReference relativeDatabaseReference_QA = databaseReference.child(FirebaseReferences.QUESTIONNAIRE_ANSWERS);

        DatabaseReference relativeDatabaseReference_QO = databaseReference.child(FirebaseReferences.Questionnaire_Organizations)
                .orderByChild("questionnaireId").getRef();

        relativeDatabaseReference_QO.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionnaireDataCollected.getValue().clear();
                snapshot.getChildren().forEach((x) -> {
                    QuestionnaireDataCollected qdc = new QuestionnaireDataCollected();
                    QuestionnaireOrganization qo = x.getValue(QuestionnaireOrganization.class);
                    qdc.setQuestionnaireOrganization(qo);
                    questionnaireDataCollected.getValue().put(x.getKey(), qdc);

                });


                relativeDatabaseReference_QA.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AtomicInteger childrenNumber = new AtomicInteger();

                        List<DataSnapshot> allquestionnaireAnwersDataSource = StreamSupport.stream(snapshot.getChildren().spliterator(), false)
                                .collect(Collectors.toList());


                        questionnaireDataCollected.getValue().forEach((qdckey, qdc) -> {
                            if (qdc == null)
                                return;
                            List<QuestionnaireAnswers> allQuestionnaireAnswers = allquestionnaireAnwersDataSource
                                    .stream()
                                    .map(x -> x.getValue(QuestionnaireAnswers.class))
                                    .filter(x ->{
                                       Boolean res= qdckey.equals(x.getQuestionnaireId());
                                        return res;
                                    })
                                    .collect(Collectors.toList());

                            qdc.setPeopleParticipated(allQuestionnaireAnswers.size());


                            List<UserAnswer> userAnswers = new ArrayList<>();
                            allQuestionnaireAnswers.stream().map(x -> x.getAnswers()).forEach(x ->
                                    {
                                        userAnswers.addAll(x);
                                    }
                            );
                            questions.forEach(question -> {
                                UserAnswerData UAD1 = new UserAnswerData();
                                UAD1.setQuestionId(question.getQuestion());
                                userAnswers.stream().filter(x -> x.getQuestionId().equals(question.getQuestion()))
                                        .forEach(x -> {
                                            if(x==null || x.getOptionPicked()==null)
                                                return;
                                            UAD1.AddToOption(x.getOptionPicked(), 1);});
                                qdc.getUserAnswerData().put(question.getQuestion(), UAD1);
                            });


                        });

                        questionnaireDataCollected.setValue(questionnaireDataCollected.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return questionnaireDataCollected;

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
