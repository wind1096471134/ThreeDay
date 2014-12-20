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
    public static final int REQUEST_RESET_PASSWORD = 1;
    public static final int REQUEST_FIRST_SET_PASSWORD = 2;

    public final static int MORNING_REMAIN_NOTIFICATION_ID = -1;
    public final static int MORNING_REMAIN_PENDING_INTENT_ID = -2;
    public final static int EVENING_CHECK_NOTIFICATION_ID = -3;
    public final static int EVENING_CHECK_PENDING_INTENT_ID = -4;
    public final static int UPDATE_DATA_AT_NEW_DAY_ALARM_ID = -5;

    public static final int MORNING_REMAIN_TIME_DEFAULT_HOUR = 8;
    public static final int MORNING_REMAIN_TIME_DEFAULT_MINUTE = 0;
    public static final int EVENING_CHECK_TIME_DEFAULT_HOUR = 22;
    public static final int EVENING_CHECK_TIME_DEFAULT_MINUTE = 0;
    public static final int NEW_DAY_ALARM_HOUR = 0;
    public static final int NEW_DAY_ALARM_MINUTE = 0;
    public static final long A_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    public static final String TASK_INFORMATION = "0";
    public static final String TASK_REMAIN = "1";
    public static final String TASK_REMAIN_TIME = "2";
    public static final String TASK_DONE = "3";
    public static final String TASK_DONE_TIME = "4";
    public static final String TASK_EVALUATION = "5";
    public static final String TASK_DAY_TYPE = "6";
    public static final String TASK_ID = "7";

    public static final String PREFERENCE_NAME = "ThreeDay";
    public static final String PREFERENCE_KEY_YESTERDAY_EVALUATION = "YesterdayEvaluation";
    public static final String PREFERENCE_KEY_TODAY_EVALUATION = "TodayEvaluation";
    public static final String PREFERENCE_KEY_TODAY_TASKS_CHECK = "TodayTasksCheck";
    public static final String PREFERENCE_KEY_MORNING_REMAIN = "MorningRemain";
    public static final String PREFERENCE_KEY_MORNING_REMAIN_TIME_HOUR = "MorningRemainTimeHour";
    public static final String PREFERENCE_KEY_MORNING_REMAIN_TIME_MINUTE = "MorningRemainTimeMinute";
    public static final String PREFERENCE_KEY_EVENING_CHECK = "EveningCheck";
    public static final String PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR = "EveningCheckTimeHour";
    public static final String PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE = "EveningCheckTimeMinute";
    public static final String PREFERENCE_KEY_REAL_DAY_TIME_1 = "RealDayTime1";
    public static final String PREFERENCE_KEY_REAL_DAY_TIME_2 = "RealDayTime2";
    public static final String PREFERENCE_KEY_LAST_IN_DAY_TIME = "DayTime";
    public static final String PREFERENCE_KEY_FIRST_USING = "FirstUsing";
    public static final String PREFERENCE_KEY_LOCK_SET = "LockSet";
    public static final String PREFERENCE_KEY_LOCK_PASSWORD = "LockPassword";

    public static final String REMAIN_BUNDLE_KEY = "BundleKey";
    public static final String REMAIN_TASKITEM_KEY = "TaskItemKey";
    public static final String ARRANGE_TOMORROW_KEY = "ArrangeTomorrow";

    public static final String EXTRA_KEY_LOCK_ACTIVITY_STATE = "LockActivityState";
    public static final String EXTRA_KEY_LOCK_BACK = "Back";
    public static final String EXTRA_KEY_LOCK_START_ACTIVITY = "StartActivity";

    public static final boolean DEFAULT_MORNING_REMAIN = false;
    public static final boolean DEFAULT_EVENING_CHECK = true;
}
