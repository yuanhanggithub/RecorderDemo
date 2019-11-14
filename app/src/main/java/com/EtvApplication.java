package com;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.etv.util.CrashExceptionHandler;
import com.etv.util.FileUtil;

public class EtvApplication extends Application {

    public static EtvApplication instance;
    private static SharedPreferences mSharedPreferences;
    public static String USER_INFO = "ETV_SHARE";
    static Context context;

    /***
     * 定时开关机的时间
     */
    public static EtvApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
        mSharedPreferences = getSharedPreferences(USER_INFO, 0);
        FileUtil.creatPathNotExcit();
        initOther();
    }

    public static Context getContext() {
        return context;
    }

    private void initOther() {
        CrashExceptionHandler crashExceptionHandler = CrashExceptionHandler.getCrashInstance();
        crashExceptionHandler.init();
    }

    public void saveData(String key, Object data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            if (data instanceof Integer) {
                editor.putInt(key, (Integer) data);
            } else if (data instanceof Boolean) {
                editor.putBoolean(key, (Boolean) data);
            } else if (data instanceof String) {
                editor.putString(key, (String) data);
            } else if (data instanceof Float) {
                editor.putFloat(key, (Float) data);
            } else if (data instanceof Long) {
                editor.putLong(key, (Long) data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isSave = editor.commit();
    }

    public Object getData(String key, Object defaultObject) {
        try {
            if (defaultObject instanceof String) {
                return mSharedPreferences.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return mSharedPreferences.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return mSharedPreferences.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return mSharedPreferences.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return mSharedPreferences.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}