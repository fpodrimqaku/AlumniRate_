/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.mindorks.framework.mvvm.data;

import android.content.Context;
import android.net.Uri;

import androidx.core.util.Consumer;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.mindorks.framework.mvvm.MvvmApp;
import com.mindorks.framework.mvvm.data.firebase.FirebaseHelper;
import com.mindorks.framework.mvvm.data.firebase.FirebaseHelperImpl;
import com.mindorks.framework.mvvm.data.local.db.DbHelper;
import com.mindorks.framework.mvvm.data.local.prefs.PreferencesHelper;
import com.mindorks.framework.mvvm.data.model.api.BlogResponse;
import com.mindorks.framework.mvvm.data.model.api.LoginRequest;
import com.mindorks.framework.mvvm.data.model.api.LoginResponse;
import com.mindorks.framework.mvvm.data.model.api.LogoutResponse;
import com.mindorks.framework.mvvm.data.model.api.OpenSourceResponse;
import com.mindorks.framework.mvvm.data.model.db.Option;
import com.mindorks.framework.mvvm.data.model.db.Question;
import com.mindorks.framework.mvvm.data.model.db.User;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireAnswers;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.data.model.firebase.RateeRankingsData;
import com.mindorks.framework.mvvm.data.model.others.QuestionCardData;
import com.mindorks.framework.mvvm.data.remote.ApiHeader;
import com.mindorks.framework.mvvm.data.remote.ApiHelper;
import com.mindorks.framework.mvvm.utils.Action;
import com.mindorks.framework.mvvm.utils.AppConstants;
import com.mindorks.framework.mvvm.utils.CommonUtils;
import com.mindorks.framework.mvvm.utils.ConsumerAction;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;


@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final ApiHelper mApiHelper;

    private final Context mContext;

    private final DbHelper mDbHelper;

    private final Gson mGson;

    private final PreferencesHelper mPreferencesHelper;

    private final FirebaseHelper firebaseHelper;

    private final MutableLiveData<String> currentBarcodeScanned = new MutableLiveData<>();

    @Inject
    public AppDataManager(Context context, DbHelper dbHelper, PreferencesHelper preferencesHelper, ApiHelper apiHelper, Gson gson, FirebaseHelperImpl firebaseAccess) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mGson = gson;
        firebaseHelper = firebaseAccess;
    }

    @Override
    public Single<LoginResponse> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request) {
        return mApiHelper.doFacebookLoginApiCall(request);
    }

    @Override
    public Single<LoginResponse> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request) {
        return mApiHelper.doGoogleLoginApiCall(request);
    }

    @Override
    public Single<LogoutResponse> doLogoutApiCall() {
        return mApiHelper.doLogoutApiCall();
    }

    @Override
    public Single<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return mApiHelper.doServerLoginApiCall(request);
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
        mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
    }

    @Override
    public Observable<List<Question>> getAllQuestions() {
        return mDbHelper.getAllQuestions();
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return mDbHelper.getAllUsers();
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public Single<BlogResponse> getBlogApiCall() {
        return mApiHelper.getBlogApiCall();
    }

    @Override
    public String getCurrentUserEmail() {
        return mPreferencesHelper.getCurrentUserEmail();
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPreferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public Long getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(Long userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public String getCurrentUserName() {
        return mPreferencesHelper.getCurrentUserName();
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPreferencesHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPreferencesHelper.getCurrentUserProfilePicUrl();
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPreferencesHelper.setCurrentUserProfilePicUrl(profilePicUrl);
    }

    @Override
    public void setCurrentFormUID(String currentFormUID) {
        mPreferencesHelper.setCurrentFormUID(currentFormUID);
    }

    @Override
    public String getCurrentFormUID() {
        return mPreferencesHelper.getCurrentFormUID();
    }

    @Override
    public void setCurrentLoginUserMode(boolean loginAsRatee) {
        mPreferencesHelper.setCurrentLoginUserMode(loginAsRatee);
    }

    @Override
    public Boolean getCurrentLoginUserMode() {
        return mPreferencesHelper.getCurrentLoginUserMode();
    }


    @Override
    public Single<OpenSourceResponse> getOpenSourceApiCall() {
        return mApiHelper.getOpenSourceApiCall();
    }

    @Override
    public Observable<List<Option>> getOptionsForQuestionId(Long questionId) {
        return mDbHelper.getOptionsForQuestionId(questionId);
    }

    @Override
    public Observable<List<QuestionCardData>> getQuestionCardData() {
        return mDbHelper.getAllQuestions()
                .flatMap(questions -> Observable.fromIterable(questions))
                .flatMap(question -> Observable.zip(
                        mDbHelper.getOptionsForQuestionId(question.id),
                        Observable.just(question),
                        (options, question1) -> new QuestionCardData(question1, options)))
                .toList()
                .toObservable();
    }

    @Override
    public Observable<Boolean> insertUser(User user) {
        return mDbHelper.insertUser(user);
    }

    @Override
    public Observable<Boolean> isOptionEmpty() {
        return mDbHelper.isOptionEmpty();
    }

    @Override
    public Observable<Boolean> isQuestionEmpty() {
        return mDbHelper.isQuestionEmpty();
    }

    @Override
    public Observable<Boolean> saveOption(Option option) {
        return mDbHelper.saveOption(option);
    }

    @Override
    public Observable<Boolean> saveOptionList(List<Option> optionList) {
        return mDbHelper.saveOptionList(optionList);
    }

    @Override
    public Observable<Boolean> saveQuestion(Question question) {
        return mDbHelper.saveQuestion(question);
    }

    @Override
    public Observable<Boolean> saveQuestionList(List<Question> questionList) {
        return mDbHelper.saveQuestionList(questionList);
    }

    @Override
    public Observable<Boolean> seedDatabaseOptions() {
        return mDbHelper.isOptionEmpty()
                .concatMap(isEmpty -> {
                    if (isEmpty) {
                        Type type = new TypeToken<List<Option>>() {
                        }.getType();
                        List<Option> optionList = mGson.fromJson(CommonUtils.loadJSONFromAsset(mContext, AppConstants.SEED_DATABASE_OPTIONS), type);
                        return saveOptionList(optionList);
                    }
                    return Observable.just(false);
                });
    }

    @Override
    public Observable<Boolean> seedDatabaseQuestions() {
        return mDbHelper.isQuestionEmpty()
                .concatMap(isEmpty -> {
                    if (isEmpty) {
                        Type type = $Gson$Types.newParameterizedTypeWithOwner(null, List.class, Question.class);
                        List<Question> questionList = mGson
                                .fromJson(CommonUtils.loadJSONFromAsset(mContext, AppConstants.SEED_DATABASE_QUESTIONS), type);
                        return saveQuestionList(questionList);
                    }
                    return Observable.just(false);
                });
    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(
                null,
                null,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT,
                null,
                null,
                null);
    }

    @Override
    public void updateApiHeader(Long userId, String accessToken) {
        mApiHelper.getApiHeader().getProtectedApiHeader().setUserId(userId);
        mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
    }

    @Override
    public void updateUserInfo(
            String accessToken,
            Long userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath) {

        setAccessToken(accessToken);
        setCurrentUserId(userId);
        setCurrentUserLoggedInMode(loggedInMode);
        setCurrentUserName(userName);
        setCurrentUserEmail(email);
        setCurrentUserProfilePicUrl(profilePicPath);

        updateApiHeader(userId, accessToken);
    }

    @Override
    public FirebaseUser getCurrentLoggedInUser() {
        return firebaseHelper.getCurrentLoggedInUser();
    }

    @Override
    public void signInWithEmailAndPassword(String email, String password, ConsumerAction<String> onSuccess, Action onFailure) {
        firebaseHelper.signInWithEmailAndPassword(email, password, onSuccess, onFailure);
    }

    @Override
    public void signUpWithNewUser(com.mindorks.framework.mvvm.data.model.firebase.User user, String password, Action onSuccess, Action onFailure) {
        firebaseHelper.signUpWithNewUser(user, password, onSuccess, onFailure);
    }


    @Override
    public QuestionnaireType createQuestionnaireType(QuestionnaireType questionnaireType){
        return firebaseHelper.createQuestionnaireType(questionnaireType);
    }

    @Override
    public void getQuestionnairesRealtime(java.util.function.Consumer<List<QuestionnaireType>> consumerFunction, java.util.function.Consumer<DatabaseError> consumerOnError) {

    }

    public void getQuestionnairesRealtime(Consumer<List<QuestionnaireType>> consumerFunction, Consumer<DatabaseError> consumerOnError){
      //  firebaseHelper.getQuestionnairesRealtime(consumerFunction, consumerOnError);
    }

    @Override
    public MutableLiveData<List<com.mindorks.framework.mvvm.data.model.firebase.Question>> getQuestions(){
        return firebaseHelper.getQuestions();
    };

    public void insertQuestionnaireOrganization (QuestionnaireOrganization questionnaireOrganization,Action actionOnSuccess,Action actionOnFailure){
        firebaseHelper.insertQuestionnaireOrganization(questionnaireOrganization,actionOnSuccess,actionOnFailure);

    }
    //not for direct use rather here to suppress overriding rules
    public  <T>boolean insertEntityIntoSet(T entity,String setName){
        //nerfed
        return firebaseHelper.<T>insertEntityIntoSet(entity,setName);

    }

    public boolean insertQuestionnaireAnswers (QuestionnaireAnswers questionnaireAnswers){
       return firebaseHelper.<QuestionnaireAnswers>insertEntityIntoSet(questionnaireAnswers,FirebaseHelperImpl.FirebaseReferences.QUESTIONNAIRE_ANSWERS);
    }

    public MutableLiveData<QuestionnaireOrganization>  fetchQuestionnaireByQrCode (String qrCode){
        return firebaseHelper.fetchQuestionnaireByQrCode(qrCode);

    }

    public MutableLiveData<ConcurrentMap<String, QuestionnaireDataCollected>>fetchQuestionnaireDataCollected (){
        return firebaseHelper.fetchQuestionnaireDataCollected();

    }

    public void initiatefetchRateeRankingsData(){
        firebaseHelper.initiatefetchRateeRankingsData();
    }
    public void initiatefetchingQuestionnaireDataCollected(String userId){

        firebaseHelper.initiatefetchingQuestionnaireDataCollected(userId);
    }



    public MutableLiveData <Boolean>  sendPasswordResetEmail(String email){

        return firebaseHelper.sendPasswordResetEmail(email);
    }

    public void createUserWithProfilePic (String email, String password, Uri mImageUri){
         firebaseHelper.createUserWithProfilePic ( email,  password,  mImageUri);
    }
    public MutableLiveData<String> storeImage(Uri mImageUri, ConsumerAction<String> onSuccessConsumer, Action onFailureAction) {
        return  firebaseHelper.storeImage(mImageUri,  onSuccessConsumer,onFailureAction);
    }

    public com.mindorks.framework.mvvm.data.model.firebase.User getCurrentUserSigned(){
        return  firebaseHelper.getCurrentUserSigned();
    }

    public MutableLiveData<ConcurrentMap<String, RateeRankingsData>> fetchRateeRankingsData(){
        return firebaseHelper.fetchRateeRankingsData();
    }

    public MutableLiveData<String> getCurrentBarcodeScanned(){
        return currentBarcodeScanned;
    }

    public MutableLiveData<Map<String,QuestionnaireAnswers>> getQuestionnairesAnsweredByTheCurrentUserIdDevice (){
        return firebaseHelper.getQuestionnairesAnsweredByTheCurrentUserIdDevice();
    };
    public void fetchQuestionnairesFilledByUserPreviously(){
        firebaseHelper.fetchQuestionnairesFilledByUserPreviously();
    }

    public boolean userHasFilledTheQuestionnaireBefore(String QuestionnaireQrId){
        String DeviceIdHashed = MvvmApp.getDeviceIdHashed();
        return getQuestionnairesAnsweredByTheCurrentUserIdDevice().getValue().values().stream().anyMatch(x->
           x.getQuestionnaireId()!=null && x.getDeviceIdHashed()!=null && x.getQuestionnaireId().equals(QuestionnaireQrId) && x.getDeviceIdHashed().equals(DeviceIdHashed)

        );

    }

    public MutableLiveData<com.mindorks.framework.mvvm.data.model.firebase.User> getCurrentLoggedInUserPassive(){
        return firebaseHelper.getCurrentLoggedInUserPassive();

    }
}