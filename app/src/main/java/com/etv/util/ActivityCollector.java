package com.etv.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActivityCollector {
    public static final String TAG = "ActivityCollector";
    private static List<Activity> activities = new ArrayList();

    public static void addActivity(Activity paramActivity) {
        activities.add(paramActivity);
        Log.i("ActivityCollector", "添加activity = " + paramActivity.getClass());
    }


    /**
     * 判断是app是否处于后台
     *
     * @return
     */
    public static boolean isAppRunBackGround(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


    public static void finishAll() {
        Iterator localIterator = activities.iterator();
        while (localIterator.hasNext()) {
            Activity localActivity = (Activity) localIterator.next();
            if (!localActivity.isFinishing()) {
                localActivity.finish();
            }
        }
        Log.i("ActivityCollector", "移除所有的activity");
    }


    /***
     * 判断当前界面是否在前台
     * @param context
     * @param listClassName
     * @return
     */
    public static boolean isForegroundList(Context context, List<String> listClassName) {
        try {
            List<Boolean> listFirst = new ArrayList<Boolean>();
            boolean isForrground = false;
            if (listClassName.size() < 1) {
                return isForrground;
            }
            for (int i = 0; i < listClassName.size(); i++) {
                String className = listClassName.get(i);
                listFirst.add(isForeground(context, className));
            }
            for (int k = 0; k < listFirst.size(); k++) {
                boolean isForst = listFirst.get(k);
                if (isForst) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            MyLog.e("", "====界面在前台异常==" + e.toString());
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            Log.i("activityName---", cpn.getClassName());
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }


    public static void removeActivity(Activity paramActivity) {
        activities.remove(paramActivity);
        Log.i("ActivityCollector", "移除activity = " + paramActivity.getClass());
    }
}
