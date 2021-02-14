package com.mindorks.framework.mvvm.data.model.firebase;

public class UserAnswer {
    private Integer questionId;
    private Integer optionPicked;


    public UserAnswer(Integer questionId, Integer optionPicked) {
        this.questionId = questionId;
        this.optionPicked = optionPicked;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getOptionPicked() {
        return optionPicked;
    }

    public void setOptionPicked(Integer optionPicked) {
        this.optionPicked = optionPicked;
    }
}
