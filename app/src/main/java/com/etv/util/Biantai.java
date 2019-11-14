package com.etv.util;

import android.util.Log;

public class Biantai {

    private static long lastOtherTime;

    public static boolean otherTimeCheck(long timeDistance) {
        long current = System.currentTimeMillis();
        long distanceTime = current - lastOtherTime;
        if ((0L < distanceTime) && (distanceTime < timeDistance)) {
            return true;
        }
        lastOtherTime = current;
        return false;
    }

    private static long lasThreeTime;

    public static boolean isThreeClick() {
        try {
            long l1 = System.currentTimeMillis();
            long l2 = l1 - lasThreeTime;
            if ((0L < l2) && (l2 < 3000L)) {
                return true;
            }
            lasThreeTime = l1;
        } catch (Exception e) {
        }
        return false;
    }


    private static long lasTwoTime;

    public static boolean isTwoClick() {
        try {
            long l1 = System.currentTimeMillis();
            long l2 = l1 - lasTwoTime;
            if ((0L < l2) && (l2 < 2000L)) {
                return true;
            }
            lasTwoTime = l1;
        } catch (Exception e) {
        }
        return false;
    }

    private static long lasOneTime;

    public static boolean isOneClick() {
        try {
            long l1 = System.currentTimeMillis();
            long l2 = l1 - lasOneTime;
            if ((0L < l2) && (l2 < 1000L)) {
                return true;
            }
            lasOneTime = l1;
        } catch (Exception e) {
        }

        return false;
    }


    private static long lastHeartTime;

    /**
     * 设置心跳的频率
     *
     * @return
     */
    public static boolean checkHeartTime() {
        try {
            long l1 = System.currentTimeMillis();
            long l2 = l1 - lastHeartTime;
            if ((0L < l2) && (l2 < 2000L)) {
                return true;
            }
            lastHeartTime = l1;
        } catch (Exception e) {
        }
        return false;
    }


    private static int number;
    public static boolean isFourClick() {
        try {
            long l1 = System.currentTimeMillis();
            long l2 = l1 - SharedPerManager.getTime();
            if ((0L < l2) && (l2 < 1000L)) {
                number++;
                if (number>=2) {
                    SharedPerManager.setTime(l1);
                    return false;
                }
            }else {
                number = 0;
            }
            SharedPerManager.setTime(l1);
        } catch (Exception e) {
        }
        return true;
    }

}
