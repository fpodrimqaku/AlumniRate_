package com.mindorks.framework.mvvm.ui.personal_ratings;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;
import com.mindorks.framework.mvvm.data.model.firebase.UserAnswer;
import com.mindorks.framework.mvvm.ui.base.BaseViewHolder;

import java.text.SimpleDateFormat;

public class PersonalRatingsListItemViewHolder extends BaseViewHolder {

    QuestionnaireDataCollected questionnaireDataCollected;
    TextView questionnaire_organization_collected_text;
    TextView questionnaire_organization_collected_attendees_num;
    TextView questionnaire_organization_collected_period;


    public PersonalRatingsListItemViewHolder(View view) {
        super(view);
        questionnaire_organization_collected_text = view.findViewById(R.id.questionnaire_organization_collected_text);
        questionnaire_organization_collected_period = view.findViewById(R.id.questionnaire_organization_collected_period);
        questionnaire_organization_collected_attendees_num = view.findViewById(R.id.questionnaire_organization_collected_attendees_num);


    }

    @Override
    public void onBind(int position) {

    }


    public void initiateItem(QuestionnaireDataCollected questionnaireDataCollected) {
        questionnaire_organization_collected_text.setText(questionnaireDataCollected.getQuestionnaireOrganization().getQuestionnaireName());
        questionnaire_organization_collected_attendees_num.setText(""+questionnaireDataCollected.getPeopleParticipated());
        String timePattern = "hh:mm";
        String datePattern ="MM-dd-yyyy";
        SimpleDateFormat simpleDateFormatTimeOnly = new SimpleDateFormat(timePattern);
        SimpleDateFormat simpleDateFormatDateOnly = new SimpleDateFormat(datePattern);
        String fromTime = simpleDateFormatTimeOnly.format(questionnaireDataCollected.getQuestionnaireOrganization().getFromDateTime());
        String toTime = simpleDateFormatTimeOnly.format(questionnaireDataCollected.getQuestionnaireOrganization().getToDateTime());
        String helalDate = simpleDateFormatDateOnly.format(questionnaireDataCollected.getQuestionnaireOrganization().getFromDateTime());

        String period = "("+helalDate + ")Prej "+ fromTime + " deri" + toTime;

        questionnaire_organization_collected_period.setText(period);

    }




}
