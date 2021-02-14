package com.mindorks.framework.mvvm.data.model.firebase;

public class QuestionnaireOrganization {

    private String  _QRCode;
    private Boolean locationRequired;
    private String location;
    private String fromDateTime;
    private String toDateTime;
    private String questionnaireId;


    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

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

    public String getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }
}
