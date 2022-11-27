
package com.mindorks.framework.mvvm.data;

import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.firebase.FirebaseHelper;
import com.mindorks.framework.mvvm.data.local.db.DbHelper;
import com.mindorks.framework.mvvm.data.local.prefs.PreferencesHelper;
import com.mindorks.framework.mvvm.data.model.others.QuestionCardData;
import com.mindorks.framework.mvvm.data.remote.ApiHelper;
import io.reactivex.Observable;
import java.util.List;



public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper, FirebaseHelper {

    Observable<List<QuestionCardData>> getQuestionCardData();

    Observable<Boolean> seedDatabaseOptions();

    Observable<Boolean> seedDatabaseQuestions();

    void setUserAsLoggedOut();

    void updateApiHeader(Long userId, String accessToken);

    void updateUserInfo(
            String accessToken,
            Long userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath);

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }
    public MutableLiveData<String> getCurrentBarcodeScanned();
    public boolean userHasFilledTheQuestionnaireBefore(String QuestionnaireQrId);
}
