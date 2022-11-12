
package com.mindorks.framework.mvvm.utils;


import android.app.Application;

import com.mindorks.framework.mvvm.R;

import java.util.HashMap;

public final class AppConstants {

    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static final String DB_NAME = "mindorks_mvvm.db";

    public static final long NULL_INDEX = -1L;

    public static final String PREF_NAME = "mindorks_pref";

    public static final String SEED_DATABASE_OPTIONS = "seed/options.json";

    public static final String SEED_DATABASE_QUESTIONS = "seed/questions.json";

    public static final String STATUS_CODE_FAILED = "failed";

    public static final String STATUS_CODE_SUCCESS = "success";

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_kkmmss";

    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int LOCATION_PERMISSION_CODE = 102;
    public static final int LOCAL_STORAGE_PERMISSION_CODE = 103;
    public static HashMap<Integer,Integer> answersWordified= new HashMap();
    static {

        answersWordified.put(1, R.string.question_one_rate);
        answersWordified.put(2, R.string.question_two_rate);
        answersWordified.put(3, R.string.question_three_rate);
        answersWordified.put(4, R.string.question_four_rate);
        answersWordified.put(5, R.string.question_five_rate);

    }

    private AppConstants() {
        // This utility class is not publicly instantiable


    }
}
