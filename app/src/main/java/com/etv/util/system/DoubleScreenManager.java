//package com.etv.util.system;
//
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.display.DisplayManager;
//import android.view.Display;
//
//import com.etv.util.MyLog;
//
//import java.io.IOException;
//
//
///***
// * 双频异县工具类
// */
//
//public class DoubleScreenManager {
//    private static DoubleScreenManager manager;
//    public static final String MAIN_SCREEN_PATH = "persist.sys.displayrot";
//    public static final String SECONDARY_SCREEN_PATH = "persist.sys.hdmirot";
//    public static final String SECONDARY_SCREEN_SCALER = "persist.sys.hdmiscaler";
//    private Context context;
//    public static final String LVDS_PATH = "/sys/devices/fb.8/graphics/fb0/pwr_bl";
//    public static final String HDMI_PATH = "/sys/class/display/HDMI/enable";
//
//    private DoubleScreenManager(Context context) {
//        this.context = context;
//    }
//
//    public static synchronized DoubleScreenManager getInstance(Context context) {
//        if (manager == null) {
//            manager = new DoubleScreenManager(context);
//        }
//
//        return manager;
//    }
//
//    public void openMainScreenLight() {
//        try {
//            Utils.writeIOFile("1", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
//        } catch (IOException var2) {
//            var2.printStackTrace();
//        } catch (InterruptedException var3) {
//            var3.printStackTrace();
//        }
//
//    }
//
//    public void closeMainScreenLight() {
//        try {
//            Utils.writeIOFile("0", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
//        } catch (IOException var2) {
//            var2.printStackTrace();
//        } catch (InterruptedException var3) {
//            var3.printStackTrace();
//        }
//
//    }
//
//    public void openSecondaryScreenLight() {
//        try {
//            Utils.writeIOFile("1", "/sys/class/display/HDMI/enable");
//        } catch (IOException var2) {
//            var2.printStackTrace();
//        } catch (InterruptedException var3) {
//            var3.printStackTrace();
//        }
//
//    }
//
//    public void closeSecondaryScreenLight() {
//        try {
//            Utils.writeIOFile("0", "/sys/class/display/HDMI/enable");
//        } catch (IOException var2) {
//            var2.printStackTrace();
//        } catch (InterruptedException var3) {
//            var3.printStackTrace();
//        }
//
//    }
//
//    public boolean isMainScreenOpen() {
//        return "1".equals(Utils.readGpioPG("/sys/devices/fb.8/graphics/fb0/pwr_bl"));
//    }
//
//    public boolean isSecondaryScreenOpen() {
//        return "1".equals(Utils.readGpioPG("/sys/class/display/HDMI/enable"));
//    }
//
//    public void rotateMainScreen(String degree) {
//        Utils.setValueToProp("persist.sys.displayrot", degree);
//        this.reboot();
//    }
//
//    public void rotateSecondaryScreen(String degree) {
//        if ("90".equals(degree) || "270".equals(degree)) {
//            Utils.setValueToProp("persist.sys.hdmiscaler", "1");
//        }
//
//        Utils.setValueToProp("persist.sys.hdmirot", degree);
//        this.reboot();
//    }
//
//    private void reboot() {
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.reboot");
//        this.context.sendBroadcast(intent);
//    }
//}
