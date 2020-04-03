package com.etv.config;

import android.os.Environment;

import com.etv.util.CodeUtil;
import com.etv.util.FileUtil;
import com.etv.util.MyLog;
import com.etv.util.SharedPerManager;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class AppInfo {


    public static final String ONE_KEY_POLICE_BROAD = "com.ys.sendEvent";  //一键报警广播
    public static final String CALL_FROM_THER = "com.etv.service.CALL_FROM_THER"; //来电话了
    public static final String CALL_TO_PHONE_RING = "com.etv.service.CALL_TO_PHONE_RING"; //拨电话
    public static final String HAND_UP_PHONE = "com.etv.service.HAND_UP_PHONE";  //挂断电话调用广播
    public static final String HAND_UP_LONG_PHONE = "com.ys.longsendEvent";  //长按广播
    public static final String SD_PULLOUT ="android.intent.action.MEDIA_EJECT";//SD卡拔出
    public static final String SD_INSERT ="android.intent.action.MEDIA_MOUNTED";//SD卡插入
    public static final String EXIT_APP ="com.ys.exit.videoapp";//退出app
    public static final String SHRINK_WINDOW ="com.ys.shrink.window";//退出app
    public static final String START_SETTING ="com.ys.start.setting";//打开设置界面
    public static final String BASE_PATH_INNER = Environment.getExternalStorageDirectory().getPath();

    /***
     * 获取基本路径
     * @return
     */
    public static String BASE_SD_PATH() {
        String basePath = SharedPerManager.getBaseSdPath();
        return basePath;
    }

    /***
     * 这里可能用户频繁插拔SD卡，导致路径切换失败的情况
     * 先取值，如果不存在的话，设置内存路径
     * 再次取值
     * @return
     */
    public static String BASE_PATH() {
        String path = BASE_SD_PATH() + "/etv";
        return path;
    }

    /**
     * 保存log的位置
     *
     * @return
     */
    public static String BASE_CRASH_LOG() {
        String path = BASE_PATH_INNER + "/crashlog";
        return path;
    }

    public static String BASE_APK() {
        FileUtil.MKDIRSfILE("/sdcard/etv");
        FileUtil.MKDIRSfILE("/sdcard/etv/apk");
        return BASE_PATH_INNER + "/etv/apk";
    }

    public static String BASE_IMAGE() {
        return BASE_PATH() + "/image";
    }

    public static String BASE_IMAGE_RECEIVER() {
        return BASE_PATH() + "/receiver";
    }

    /***
     * 用来保存默认播让的图片位置
     * @return
     */
    public static String BASE_DEFAULR_PLAY() {
        return BASE_PATH() + "/default";
    }

    public static String BASE_CACHE() {
        return BASE_PATH() + "/cache";
    }

    public static String BASE_TASK_URL() {
        return BASE_PATH() + "/task";
    }

}
