package com.mindorks.framework.mvvm.data.model.firebase;

import com.mindorks.framework.mvvm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionnaireOrganization {

    private String  _QRCode;
    private Boolean locationRequired;
    private String location;
    private Date fromDateTime;
    private Date toDateTime;
    private String questionnaireName;
    private String rateeId;
    private Date CreationDateTime;



    public String getQuestionnaireName() {
        return this.questionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
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
    public String getRateeId() {
        return rateeId;
    }

    public void setRateeId(String rateeId) {
        this.rateeId = rateeId;
    }

    public Date getCreationDateTime() {
        return CreationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        CreationDateTime = creationDateTime;
    }



    public List<Integer> isValid(){

        List<Integer> errorList = new ArrayList<>();
        if(_QRCode == null)
        {   errorList.add(R.string.model_qa_no_user_for_form);
            return errorList;}
        if(fromDateTime==null)
        {  errorList.add(R.string.model_qo_pick_date_from);
            return errorList;}
        if(toDateTime==null)
        {errorList.add(R.string.model_qo_pick_date_to);
            return errorList;}
        if(questionnaireName==null)
        {errorList.add(R.string.model_qo_type_questionnaire_org_name);
            return errorList;}
        if(getToDateTime().compareTo(getFromDateTime()) < 0)
        {errorList.add(R.string.model_qo_end_date_smaller_than_startdate);
            return errorList;}
        return errorList;
    }
}
