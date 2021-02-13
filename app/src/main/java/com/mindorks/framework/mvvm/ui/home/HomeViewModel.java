package com.mindorks.framework.mvvm.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mindorks.framework.mvvm.data.DataManager;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.ui.base.BaseViewModel;
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends BaseViewModel<QuestionnaireListNavigator> {

    private MutableLiveData<String> mText;


    public LiveData<String> getText() {
        return mText;
    }


    public HomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }


    public void doStuff() {
        QuestionnaireType qtype = new QuestionnaireType();
        Question questionOne = new Question();
        questionOne.setQuestion("how are you?");
        Question questionTwo = new Question();
        questionTwo.setQuestion("how are you now?");

        List<String> options = new ArrayList();
        options.add("bad");
        options.add("good");
        options.add("pretty good");

        List<Question> questions = new ArrayList();
        questions.add(questionOne);
        questions.add(questionTwo);
        qtype.setActive(true);
        qtype.setCreationDate(new Date().toString());
        qtype.setGeneralOptions(options);
        qtype.setName("testQuestionnaire");
        qtype.setQuestions(questions);
        getDataManager().createQuestionnaireType(qtype);
    }
}