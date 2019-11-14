package com.etv.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint({"SimpleDateFormat"})
public class SimpleDateUtil {
    /***
     * 将String时间转化成long__时间戳
     * @param time
     */
    public static long StringToLongTime(String time) {
        long timeBack = -1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateStart = format.parse(time);
            timeBack = dateStart.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeBack;
    }


    /**
     * 将long转化成时间戳
     *
     * @param time
     * @return
     */
    public static long StringToLongTimePower(String time) {
        long timeBack = -1;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date dateStart = format.parse(time);
            timeBack = dateStart.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeBack;
    }


    /***
     * 获取当前的星期
     * @return
     */
    public static int getCurrentWeekDay() {
        int weekDay = 0;
        Calendar calendar = Calendar.getInstance();
        int currentDayWeek = calendar.get(Calendar.DAY_OF_WEEK);  //今天的工作星期
        switch (currentDayWeek) {
            case 1: //7
                weekDay = 7;
                break;
            case 2: //1
                weekDay = 1;
                break;
            case 3: //2
                weekDay = 2;
                break;
            case 4: //3
                weekDay = 3;
                break;
            case 5: //4
                weekDay = 4;
                break;
            case 6: //5
                weekDay = 5;
                break;
            case 7: //6
                weekDay = 6;
                break;
        }
        return weekDay;
    }


    public static String formatTaskTimeShow(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(time));
    }

    public static String formatCurrentTime() {
        long time = System.currentTimeMillis();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Long.valueOf(time));
    }

    public static String format(String paramString) {
        long l = Long.parseLong(paramString);
        return new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss").format(Long.valueOf(l));
    }

    /***
     * 将时间戳转换成时间数据
     * @param paramLong
     * @return
     */
    public static long formatBig(long paramLong) {
        return Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(Long.valueOf(paramLong)));
    }

    /***
     * 获取日期
     * @return
     */
    public static String getDate() {
        long l = System.currentTimeMillis();
        String str = new SimpleDateFormat("yyyy/MM/dd").format(Long.valueOf(l));
        return str;
    }

    public static String getDateSingle() {
        long l = System.currentTimeMillis();
        String str = new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(l));
        return str;
    }


    public static String getTime() {
        long time = System.currentTimeMillis();
        String str = new SimpleDateFormat("HH:mm").format(Long.valueOf(time));
        return str;
    }

    public static long getCurrentTimelONG() {
        long time = System.currentTimeMillis();
        String timeBack = new SimpleDateFormat("yyyyMMddHHmmss").format(Long.valueOf(time));
        long timeBackLong = Long.parseLong(timeBack);
        return timeBackLong;
    }


    public static int getHour() {
        long l = System.currentTimeMillis();
        return Integer.valueOf(new SimpleDateFormat("HH").format(Long.valueOf(l))).intValue();
    }

    public static String getWeek() {
        Date localDate = new Date();
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(localDate);
        int j = localCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int i = j;
        if (j < 0) {
            i = 0;
        }
        return new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"}[i];
    }

    public static String timeParseMedia(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }


    public static String getCurrentYear(long currentTime) {
        return new SimpleDateFormat("yyyy").format(Long.valueOf(currentTime));
    }

    public static String getCurrentMonth(long currentTime) {
        return new SimpleDateFormat("MM").format(Long.valueOf(currentTime));
    }

    public static String getCurrentDay(long currentTime) {
        return new SimpleDateFormat("dd").format(Long.valueOf(currentTime));
    }

    public static String getCurrentHour(long currentTime) {
        return new SimpleDateFormat("HH").format(Long.valueOf(currentTime));
    }

    public static String getCurrentMin(long currentTime) {
        return new SimpleDateFormat("mm").format(Long.valueOf(currentTime));
    }

    public static String getCurrentSec(long currentTime) {
        return new SimpleDateFormat("ss").format(Long.valueOf(currentTime));
    }


    /***
     * 获取当前的日期
     * @return
     */
    public static long getCurrentDateLong() {
        long current = System.currentTimeMillis();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(current);
        return Long.parseLong(currentDate);
    }

    /**
     * 获取当前的时分
     *
     * @return
     */
    public static String getCurrentHourMin() {
        long current = System.currentTimeMillis();
        return new SimpleDateFormat("HHmm").format(current);
    }

    /***
     * 获取当前的时分秒
     * @return
     */
    public static String getCurrentHourMinSec() {
        long current = System.currentTimeMillis();
        return new SimpleDateFormat("HH:mm:ss").format(current);
    }


    /**
     * 获取当前的时分秒
     *
     * @return
     */
    public static long getCurrentHourMinSecond() {
        long current = System.currentTimeMillis();
        String backTime = new SimpleDateFormat("HHmmss").format(current);
        return Long.parseLong(backTime);
    }

    /***
     * 将2019-01-26
     * 转化成  20190126
     * @param endDate
     * @return
     */
    public static long formatStringtoDate(String endDate) {
        endDate = endDate.replace("-", "");
        long timeBack = Long.parseLong(endDate);
        return timeBack;
    }

    /***
     * 任务
     * 将 00:00:00
     * 转化成 000000
     * @param endTime
     * @return
     */
    public static long formatStringTime(String endTime) {
        endTime = endTime.replace(":", "");
        long timeBack = Long.parseLong(endTime);
        return timeBack;
    }


}
