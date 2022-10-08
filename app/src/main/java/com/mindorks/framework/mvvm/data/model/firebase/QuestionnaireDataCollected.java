package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionnaireDataCollected {
    QuestionnaireOrganization questionnaireOrganization ;
    Map<String,UserAnswerData> userAnswerData;
    Integer peopleParticipated ;

public QuestionnaireDataCollected(){
    userAnswerData = new HashMap<>();

}

    public Integer getPeopleParticipated() {
        return peopleParticipated;
    }

    public void setPeopleParticipated(Integer peopleParticipated) {
        this.peopleParticipated = peopleParticipated;
    }

    public QuestionnaireOrganization getQuestionnaireOrganization() {
        return questionnaireOrganization;
    }

    public void setQuestionnaireOrganization(QuestionnaireOrganization questionnaireOrganization) {
        this.questionnaireOrganization = questionnaireOrganization;
    }

    public Map<String,UserAnswerData> getUserAnswerData() {
        return userAnswerData;
    }

    public void setUserAnswerData(Map<String,UserAnswerData> userAnswerData) {
        this.userAnswerData = userAnswerData;
    }


}
