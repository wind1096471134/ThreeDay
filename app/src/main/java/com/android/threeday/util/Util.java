package com.android.threeday.util;

/**
 * Created by user on 2014/10/31.
 */
public class Util {
    public static final int DAY_NUM = 3;
    public static final int TYPE_YESTERDAY = 0;
    public static final int TYPE_TODAY = 1;
    public static final int TYPE_TOMORROW = 2;

    public static final int EVALUATION_DEFAULT = 0;
    public static final int EVALUATION_GOOD = 1;
    public static final int EVALUATION_MID = 2;
    public static final int EVALUATION_BAD = 3;

    public static final int DONE = 1;
    public static final int UNDONE = 0;

    public static final int REMAIN = 1;
    public static final int UN_REMAIN = 0;

    public static final int REQUEST_ADD_TASK = 0;

    public static final String TASK_INFORMATION = "0";
    public static final String TASK_REMAIN = "1";
    public static final String TASK_REMAIN_TIME = "2";
    public static final String TASK_DONE = "3";
    public static final String TASK_DONE_TIME = "4";
    public static final String TASK_EVALUATION = "5";
    public static final String TASK_DAY_TYPE = "6";

    public static final String PREFERENCE_NAME = "ThreeDay";
    public static final String PREFERENCE_KEY_DAY_EVALUATION = "DayEvaluation";
}
