package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class RateeRankingsData {
    User user;
    ConcurrentMap<String, QuestionnaireDataCollected> questionnaireDataCollectedList;

    public RateeRankingsData() {
        this.questionnaireDataCollectedList = new ConcurrentHashMap();
        this.user = new User();
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


    public Map<String, UserAnswerData> getUserAnswerDataCollectedForUser() {

        Map<String, UserAnswerData> userAnswerData = new HashMap<>();

        List<UserAnswerData> userAnswerDataList = questionnaireDataCollectedList.values().stream().map((x) -> {
            return x.getUserAnswerData();

        }).map((x) -> {
            return x.values();
        }).collect(ArrayList::new, List::addAll, List::addAll);
        ;


        userAnswerDataList.stream().forEach((x) -> {
            UserAnswerData uad = userAnswerData.get(x.getQuestionId());
            if (uad == null) {
                uad = new UserAnswerData();
                uad.setQuestionId(x.getQuestionId());
                userAnswerData.put(uad.getQuestionId(), new UserAnswerData());
            }
            userAnswerData.get(x.getQuestionId()).AddToOptions(x);

        });

        return userAnswerData;

    }

    ;
}

/*
    UserAnswerData uad = new UserAnswerData();
            if (x.getQuestionId().equals(y.getQuestionId())) {

                    for (int i = 1; i <= 5; i++) {
                    uad.AddToOption(i, x.getOptionsPickedStats().get(i));
                    uad.AddToOption(i, y.getOptionsPickedStats().get(y));
                    }

                    }
                    ;*/