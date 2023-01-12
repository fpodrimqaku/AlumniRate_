

package com.mindorks.framework.mvvm.data.local.db;

import com.mindorks.framework.mvvm.data.model.db.Option;
import com.mindorks.framework.mvvm.data.model.db.Question;
import com.mindorks.framework.mvvm.data.model.db.User;
import io.reactivex.Observable;
import java.util.List;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import javax.inject.Singleton;



@Singleton
public class AppDbHelper implements DbHelper {

    private final AppDatabase mAppDatabase;

    @Inject
    public AppDbHelper(AppDatabase appDatabase) {
        this.mAppDatabase = appDatabase;
    }

    @Override
    public Observable<List<Question>> getAllQuestions() {
        return mAppDatabase.questionDao().loadAll()
                .toObservable();
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return Observable.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return mAppDatabase.userDao().loadAll();
            }
        });
    }

    @Override
    public Observable<List<Option>> getOptionsForQuestionId(final Long questionId) {
        return mAppDatabase.optionDao().loadAllByQuestionId(questionId)
                .toObservable();
    }

    @Override
    public Observable<Boolean> insertUser(final User user) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.userDao().insert(user);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> isOptionEmpty() {
        return mAppDatabase.optionDao().loadAll()
                .flatMapObservable(options -> Observable.just(options.isEmpty()));
    }

    @Override
    public Observable<Boolean> isQuestionEmpty() {
        return mAppDatabase.questionDao().loadAll()
                .flatMapObservable(questions -> Observable.just(questions.isEmpty()));

    }

    @Override
    public Observable<Boolean> saveOption(final Option option) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.optionDao().insert(option);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveOptionList(final List<Option> optionList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.optionDao().insertAll(optionList);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveQuestion(final Question question) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.questionDao().insert(question);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveQuestionList(final List<Question> questionList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.questionDao().insertAll(questionList);
                return true;
            }
        });
    }
}
