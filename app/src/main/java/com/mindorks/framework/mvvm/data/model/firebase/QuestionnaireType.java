package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.List;

public class QuestionnaireType {


    private Boolean active;

    private String createdByUser;

    private String creationDate;

    private String name;

    private String id;

    List<String> generalOptions;

    private List<Question> questions;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGeneralOptions() {
        return generalOptions;
    }

    public void setGeneralOptions(List<String> generalOptions) {
        this.generalOptions = generalOptions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
