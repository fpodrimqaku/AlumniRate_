package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.List;

public class QuestionnaireAnswers {

    private String questionnaireId;
    private String userId;
    private List<UserAnswer> answers;


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
        return answers;
    }

    public void setAnswers(List<UserAnswer> answers) {
        this.answers = answers;
    }
}
