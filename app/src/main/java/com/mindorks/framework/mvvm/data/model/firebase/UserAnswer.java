package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.Objects;

public class UserAnswer {
    private String questionId;
    private Integer optionPicked;

    public UserAnswer() {


    }


    public UserAnswer(String questionId) {
        this.questionId = questionId;

    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Integer getOptionPicked() {
        return optionPicked;
    }

    public void setOptionPicked(Integer optionPicked) {
        this.optionPicked = optionPicked;
    }

    @Override
    public boolean equals(Object o) {
       if(o == null)
           return  false;
       UserAnswer userAnswerConverted = ((UserAnswer)o);
       if(this.getOptionPicked() == userAnswerConverted.getOptionPicked() && this.getQuestionId() == userAnswerConverted.getQuestionId())
           return true;
       return false;
    }


}
