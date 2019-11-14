package com.etv.util;

import android.util.Log;

public class MyLog {

    public static void i(String tag, String s) {
        Log.i(tag, s);
    }

    public static void e(String tag, String s) {
        Log.e(tag, s);
    }

    /***
     * 用来打印接受的指令
     * @param message
     */
    public static void message(String message) {
        e("message", message);
    }

    public static void http(String s) {
        e("http", s);
    }

    public static void see(String s) {
        i("see", s);
    }

    public static void image(String s) {
        i("image", s);
    }

    public static void update(String json) {
        e("update", json);
    }

    public static void login(String s) {
        e("login", s);
    }

    public static void media(String s) {
        e("video", s);
    }

    public static void powerOnOff(String s) {
        e("powerOnOff", s);
    }

    public static void task(String s) {
        e("task", s);
    }

    public static void playTask(String s) {
        e("playTask", s);
    }

    public static void socket(String s) {
        e("SiteWebsocket", s);
    }

    public static void wifi(String s) {
        e("wifi", s);
    }

    public static void diff(String s) {
        e("diff", s);
    }

    public static void video(String s) {
        e("videoPlay", s);
    }

    public static void cdl(String s) {
        e("cdl", s);
    }

    public static void draw(String s) {
        e("draw", s);
    }

    public static void timer(String s) {
        e("timer", s);
    }

    public static void down(String s) {
//        e("down", s);
    }

    public static void other(String s) {
        e("other", s);
    }

    public static void test(String s) {
        e("testDown", s);
    }
}
