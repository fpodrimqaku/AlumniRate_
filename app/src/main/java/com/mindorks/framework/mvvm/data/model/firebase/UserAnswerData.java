package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserAnswerData {
    private String questionId;
    private Map<Integer,Integer> optionsPickedStats;

    public UserAnswerData (){

        optionsPickedStats = new HashMap<>();
        initiateOptionPickedStats();
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Map<Integer, Integer> getOptionsPickedStats() {
        return optionsPickedStats;
    }

    public void setOptionsPickedStats(Map<Integer, Integer> optionsPickedStats) {
        this.optionsPickedStats = optionsPickedStats;
    }
    public void initiateOptionPickedStats (){
        optionsPickedStats.put(1,0);
        optionsPickedStats.put(2,0);
        optionsPickedStats.put(3,0);
        optionsPickedStats.put(4,0);
        optionsPickedStats.put(5,0);    }

    public void AddToOption (int optionId,int valueToAdd){
        int value = optionsPickedStats.get(optionId);
        optionsPickedStats.put(optionId,value+valueToAdd);
    }



}
