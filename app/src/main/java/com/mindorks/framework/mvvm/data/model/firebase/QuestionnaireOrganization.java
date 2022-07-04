package com.mindorks.framework.mvvm.data.model.firebase;

import java.util.Date;

public class QuestionnaireOrganization {

    private String  _QRCode;
    private Boolean locationRequired;
    private String location;
    private Date fromDateTime;
    private Date toDateTime;
    private String questionnaireName;

    public String getQuestionnaireName() {
        return questionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        questionnaireName = questionnaireName;
    }


    //private String questionnaireId;

  //  public String getQuestionnaireId() {
  //      return questionnaireId;
  //  }

  //  public void setQuestionnaireId(String questionnaireId) {
   //     this.questionnaireId = questionnaireId;
   // }

    public String get_QRCode() {
        return _QRCode;
    }

    public void set_QRCode(String _QRCode) {
        this._QRCode = _QRCode;
    }

    public Boolean getLocationRequired() {
        return locationRequired;
    }

    public void setLocationRequired(Boolean locationRequired) {
        this.locationRequired = locationRequired;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(Date fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public Date getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(Date toDateTime) {
        this.toDateTime = toDateTime;
    }
}
