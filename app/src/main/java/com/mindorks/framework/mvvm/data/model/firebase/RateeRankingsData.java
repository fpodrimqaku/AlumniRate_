package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RateeRankingsData {
    User user ;
    ConcurrentMap<String, QuestionnaireDataCollected> questionnaireDataCollectedList;

    public RateeRankingsData() {
        this.questionnaireDataCollectedList = new ConcurrentHashMap();
        this.user= new User();
    }

    public ConcurrentMap<String, QuestionnaireDataCollected> getQuestionnaireDataCollectedList() {
        return questionnaireDataCollectedList;
    }

    public void setQuestionnaireDataCollectedList(ConcurrentMap<String, QuestionnaireDataCollected> questionnaireDataCollectedList) {
        this.questionnaireDataCollectedList = questionnaireDataCollectedList;
    }

    public double getOverallAverage() {
        if (questionnaireDataCollectedList.values().stream().count() == 0)
            return 1.0;
        return 1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
