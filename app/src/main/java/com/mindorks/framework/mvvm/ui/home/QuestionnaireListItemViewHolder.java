package com.mindorks.framework.mvvm.ui.home;

import android.view.View;
import android.widget.TextView;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireType;
import com.mindorks.framework.mvvm.ui.base.BaseViewHolder;

public class QuestionnaireListItemViewHolder extends BaseViewHolder {

    TextView questionnaireName;
    TextView q_users_subjected;


    public QuestionnaireListItemViewHolder(View view) {
        super(view);
        questionnaireName = view.findViewById(R.id.q_li_name);
        q_users_subjected = view.findViewById(R.id.textView);

    }


    @Override
    public void onBind(int position) {


    }


    public void initiateItem(QuestionnaireType item){
        questionnaireName.setText(item.getName());
        q_users_subjected.setText(item.getName());
    }

}
