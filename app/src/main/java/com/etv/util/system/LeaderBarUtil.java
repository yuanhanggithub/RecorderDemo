package com.etv.util.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.ys.rkapi.Utils.VersionUtils;

import java.util.List;


public class LeaderBarUtil {

    /**
     * 判断APP是否运行在前台
     * true 后台
     * false 前台
     *
     * @param context
     * @return
     */
    public static boolean isAppRunBackground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                    if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        Log.i("NavBar", "处于后台" + appProcess.processName);
                        return true;
                    } else {
                        Log.i("NavBar", "处于前台" + appProcess.processName);
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * 调用系统层，关闭隐藏导航栏
     * @param context
     */
    public static void hiddleLeaderBar(Activity context) {
        try {
            boolean isShowNavbar = getNavBarHideState(context);
            if (isShowNavbar) {
//                Log.i("NavBar", "=========hiddle");
            } else {
//                Log.i("NavBar", "=========show");
                hideNavBar(false, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hideNavBar(boolean hide, Activity context) {
        Intent intent = new Intent();
        if (hide) {
            intent.setAction("android.action.adtv.showNavigationBar");
            context.sendBroadcast(intent);
        } else {
            intent.setAction("android.action.adtv.hideNavigationBar");
            context.sendBroadcast(intent);
        }

    }


    private static boolean getNavBarHideState(Activity context) {
        String mode = VersionUtils.getAndroidModle();
        return !"rk3368".equals(mode) && !"rk3328".equals(mode) ? ("rk3399".equals(mode) ? Utils.getValueFromProp("persist.sys.statebarstate").equals("0") : Settings.System.getInt(context.getContentResolver(), "hidden_state_bar", 0) == 1) : Utils.getValueFromProp("persist.sys.sb.hide").equals("1");
    }

//    public static void hideBottomUIMenu(Activity activity) {
//        //隐藏虚拟按键，并且全屏
//        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = activity.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            View decorView = activity.getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//    }

}
