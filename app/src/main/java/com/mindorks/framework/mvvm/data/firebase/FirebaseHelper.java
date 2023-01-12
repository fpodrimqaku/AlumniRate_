package com.mindorks.framework.mvvm.data.firebase;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireAnswers;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.data.model.firebase.RateeRankingsData;
import com.mindorks.framework.mvvm.data.model.firebase.User;
import com.mindorks.framework.mvvm.utils.Action;
import com.mindorks.framework.mvvm.utils.ConsumerAction;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public interface FirebaseHelper {
    public FirebaseUser getCurrentLoggedInUser();

    public void signInWithEmailAndPassword(String email, String password, ConsumerAction<String> onSuccess,Action onFailure);



    public void signUpWithNewUser(User user, String password, Action onSuccess, Action onFailure);

    public User getCurrentUserSigned() ;


    public QuestionnaireType createQuestionnaireType(QuestionnaireType questionnaireType);
    public void getQuestionnairesRealtime(Consumer<List<QuestionnaireType>> consumerFunction, Consumer<DatabaseError> consumerOnError);
    public MutableLiveData<List<Question>> getQuestions();
    public void insertQuestionnaireOrganization (QuestionnaireOrganization questionnaireOrganization, Action actionOnSuccess, Action actionOnFailure);
    public <T>boolean insertEntityIntoSet(T entity,String setName);
    public MutableLiveData<QuestionnaireOrganization>  fetchQuestionnaireByQrCode (String qrCode);
    public MutableLiveData<ConcurrentMap<String, QuestionnaireDataCollected>> fetchQuestionnaireDataCollected ();
    public MutableLiveData <Boolean>  sendPasswordResetEmail(String email);
    public void createUserWithProfilePic (String email, String password, Uri mImageUri);
    public MutableLiveData<String> storeImage(Uri mImageUri, ConsumerAction<String> onSuccessConsumer, Action onFailureAction);
    public MutableLiveData<ConcurrentMap<String, RateeRankingsData>> fetchRateeRankingsData();
    public void initiatefetchingQuestionnaireDataCollected(String userId);
    public void initiatefetchRateeRankingsData();
    public MutableLiveData<Map<String,QuestionnaireAnswers>> getQuestionnairesAnsweredByTheCurrentUserIdDevice ();
    public void fetchQuestionnairesFilledByUserPreviously();
    public MutableLiveData<User> getCurrentLoggedInUserPassive();
}
