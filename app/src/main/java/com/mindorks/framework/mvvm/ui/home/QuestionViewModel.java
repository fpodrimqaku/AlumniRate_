package com.mindorks.framework.mvvm.ui.home;

import androidx.databinding.ObservableField;

import com.mindorks.framework.mvvm.data.model.firebase.UserAnswer;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableAll;

public class QuestionViewModel {

    public QuestionViewModel(UserAnswer userAnswer) {
        this.userAnswer = userAnswer;
        this.optionPicked.set(userAnswer.getOptionPicked());
    }

    public final ObservableField<Integer> optionPicked = new ObservableField<>();
    UserAnswer userAnswer;

    public ObservableField<Integer> getOptionPicked() {
        return optionPicked;
    }

    public void setOptionPicked(Integer optionPicked) {
        this.optionPicked.set(optionPicked);
    }
}
