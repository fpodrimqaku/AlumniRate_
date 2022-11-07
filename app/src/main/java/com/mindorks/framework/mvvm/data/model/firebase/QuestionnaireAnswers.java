package com.mindorks.framework.mvvm.data.model.firebase;

import com.mindorks.framework.mvvm.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireAnswers {

    private String questionnaireId;
    private String deviceId;
   // private String rateeId; //dont need it
    private List<UserAnswer> answers;

    public QuestionnaireAnswers() {
        answers = new ArrayList<UserAnswer>();
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;

    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String userId) {
        this.deviceId = userId;
    }

    public List<UserAnswer> getAnswers() {
        int o = 10 ;//todo delete later
        return answers;
    }

    public void setAnswers(List<UserAnswer> answers) {
        this.answers = answers;
    }

   // public String getRateeId() {
   //     return rateeId;
 //   }

   // public void setRateeId(String rateeId) {
  //      this.rateeId = rateeId;
 //   }

    public  List<Integer> isValid(){
        List<Integer> errorList = new ArrayList<>();
        if(questionnaireId == null)
        {   errorList.add(R.string.model_qa_no_user_for_form);
        return errorList;}
       // if(rateeId==null)
       // {errorList.add(R.string.model_qa_no_user_for_form);
      //  return errorList;}

        if(answers.stream().anyMatch(item->item.getOptionPicked()==null))
        { errorList.add(R.string.model_qa_please_fill_all_questions_before_submitting);
        return errorList;}
        return errorList;
    }
}
