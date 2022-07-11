package com.mindorks.framework.mvvm.ui.home;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.Question;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.ui.base.BaseViewHolder;

public class QuestionnaireListItemViewHolder extends BaseViewHolder {

    TextView questionnaireName;
    RadioGroup radioGroup_finalAnswer;
    RadioButton a1;
    RadioButton a2;
    RadioButton a3;
    RadioButton a4;
    RadioButton a5;


    public QuestionnaireListItemViewHolder(View view) {
        super(view);
        questionnaireName = view.findViewById(R.id.question_text);
        radioGroup_finalAnswer = view.findViewById(R.id.radio_group_pick_answer);
        a1 = view.findViewById(R.id.question_answer_1);
        a2 = view.findViewById(R.id.question_answer_2);
        a3 = view.findViewById(R.id.question_answer_3);
        a4 = view.findViewById(R.id.question_answer_4);
        a5 = view.findViewById(R.id.question_answer_5);

    }


    @Override
    public void onBind(int position) {


    }


    public void initiateItem(Question item){
        questionnaireName.setText(item.getQuestion());

    }

}
