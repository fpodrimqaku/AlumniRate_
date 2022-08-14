package com.mindorks.framework.mvvm.data.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.utils.Action;

import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public interface FirebaseHelper {
    public FirebaseUser getCurrentLoggedInUser();

    public void signInWithEmailAndPassword(String email, String password, Action onSuccess,Action onFailure);



    //public void signUpWithNewUser(String email, String password, Action onSuccess, Action onFailure);

    //public User getCurrentUserSigned() ;


    public QuestionnaireType createQuestionnaireType(QuestionnaireType questionnaireType);
    public void getQuestionnairesRealtime(Consumer<List<QuestionnaireType>> consumerFunction, Consumer<DatabaseError> consumerOnError);
    public List<Question> getQuestions();
    public void insertQuestionnaireOrganization (QuestionnaireOrganization questionnaireOrganization);
    public <T>boolean insertEntityIntoSet(T entity,String setName);
    public MutableLiveData<QuestionnaireOrganization>  fetchQuestionnaireByQrCode (String qrCode);
    public MutableLiveData<QuestionnaireDataCollected> fetchQuestionnaireDataCollected (String userId);
    public ConcurrentMap<String, QuestionnaireDataCollected> getquestionnaireDataCollected ();
}
