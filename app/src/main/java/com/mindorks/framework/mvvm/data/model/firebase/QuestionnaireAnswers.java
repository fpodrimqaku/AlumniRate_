package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireAnswers {

    private String questionnaireId;
    private String userId;
    private String rateeId;
    private List<UserAnswer> answers;

    public QuestionnaireAnswers() {
        answers = new ArrayList<UserAnswer>();
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UserAnswer> getAnswers() {
        int o = 10 ;//todo delete later
        return answers;
    }

    public void setAnswers(List<UserAnswer> answers) {
        this.answers = answers;
    }

    public String getRateeId() {
        return rateeId;
    }

    public void setRateeId(String rateeId) {
        this.rateeId = rateeId;
    }
}
