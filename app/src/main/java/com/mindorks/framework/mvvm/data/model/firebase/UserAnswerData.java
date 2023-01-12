package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserAnswerData {

    private Integer questionNum;
    private Map<Integer, Integer> optionsPickedStats;

    public UserAnswerData() {

        optionsPickedStats = new HashMap<>();
        initiateOptionPickedStats();
    }

    public Integer getQuestionId() {
        return questionNum;
    }

    public void setQuestionId(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Map<Integer, Integer> getOptionsPickedStats() {
        return optionsPickedStats;
    }

    public void setOptionsPickedStats(Map<Integer, Integer> optionsPickedStats) {
        this.optionsPickedStats = optionsPickedStats;
    }

    public void initiateOptionPickedStats() {
        optionsPickedStats.put(1, 0);
        optionsPickedStats.put(2, 0);
        optionsPickedStats.put(3, 0);
        optionsPickedStats.put(4, 0);
        optionsPickedStats.put(5, 0);
    }

    public void AddToOption(int optionId, int valueToAdd) {
        int value = optionsPickedStats.get(optionId);
        optionsPickedStats.put(optionId, value + valueToAdd);
    }

    public void AddToOptions(UserAnswerData valuesToAdd) {
        valuesToAdd.getOptionsPickedStats().keySet().stream().forEach(x -> {
            AddToOption(x, valuesToAdd.getOptionsPickedStats().get(x));

        });

    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }
}
