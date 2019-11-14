package com.etv.util;

import com.EtvApplication;
import com.etv.config.AppInfo;

public class SharedPerManager {

    public static String getPhoneNum() {
        return (String) EtvApplication.getInstance().getData("phonrNum", "");
    }

    public static void setPhoneNum(String paramString) {
        EtvApplication.getInstance().saveData("phonrNum", paramString);
    }


    public static String getBaseSdPath() {
        String savePath = ((String) EtvApplication.getInstance().getData("BaseSdPath", AppInfo.BASE_PATH_INNER));
        return savePath;
    }


    public static int getScreenHeight() {
        return ((Integer) EtvApplication.getInstance().getData("screenHeight", 1080));
    }

    public static int getScreenWidth() {
        return ((Integer) EtvApplication.getInstance().getData("screenWidth", 1920));
    }

    public static void setScreenHeight(int screenHeight) {
        EtvApplication.getInstance().saveData("screenHeight", screenHeight);
    }

    public static void setScreenWidth(int screenWidth) {
        EtvApplication.getInstance().saveData("screenWidth", screenWidth);
    }

    public static void setTime(long screenWidth) {
        EtvApplication.getInstance().saveData("time", screenWidth);
    }

    public static long getTime() {
        return ((Long) EtvApplication.getInstance().getData("time", 1000L));
    }

    public static void setNumber(int number) {
        EtvApplication.getInstance().saveData("number", number);
    }

    public static int getNumber() {
        return ((Integer) EtvApplication.getInstance().getData("number", 1));
    }

    /***
     * 获取设备的唯一值
     * @return
     */
    public static String getUniquePsuedoID() {
        String code = (String) EtvApplication.getInstance().getData("onlycode", "");
        return code;
    }

    /***
     * 保存设备的唯一值
     * @param onlycode
     */
    public static void setUniquePsuedoID(String onlycode) {
        EtvApplication.getInstance().saveData("onlycode", onlycode);
    }
}
