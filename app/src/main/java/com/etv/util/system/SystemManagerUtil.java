package com.etv.util.system;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Surface;
import android.view.WindowManager;


import com.ys.rkapi.MyManager;

import java.io.File;
import java.io.FileReader;


/***
 * 息屏良平
 */
public class SystemManagerUtil {

    private static final String TAG = "PowerManagerUtil";

    public static boolean ishasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }


    /***
     * 获取CPUid
     * @return
     */
    public static String getCpuInfo() {
        String cpuInfo = "";
        try {
            File file = new File("/proc/cpuinfo");
            FileReader reader = new FileReader(file);
            char[] bb = new char[1024];
            int n;
            while ((n = reader.read(bb)) != -1) {
                cpuInfo += new String(bb, 0, n);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(cpuInfo) || cpuInfo.length() > 2) {
            cpuInfo = cpuInfo.substring(cpuInfo.lastIndexOf(":") + 1, cpuInfo.length()).trim();
        }
        return cpuInfo;
    }


    /**
     * 判断屏幕是横屏还是竖屏
     *
     * @param context true 横屏
     *                false 视频
     * @return
     */
    public static boolean isScreenHorOrVer(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            return true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            return false;
        }
        return true;
    }


    public static void do_exec(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            String str = paramString + "\nexit\n";
            localProcess.getOutputStream().write(str.getBytes());
            if (localProcess.waitFor() != 0) {
                System.out.println("cmd=" + paramString + " error!");
                throw new SecurityException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 关机
     * @param context
     */
    public static void shoutDownDev(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.shutdown");
        context.sendBroadcast(intent);
    }

    /***
     * 重启设备
     * @param context
     */
    public static void rebootDev(Context context) {
        try {
            MyManager manager = MyManager.getInstance(context);
            manager.reboot();
        } catch (Exception e) {
        }
    }


    /***
     * 判断屏幕是息屏还是亮的状态
     * 1： on
     * 2: off
     * @return
     */
    public static String isOpenLight() {
        String str = "";
        try {
            File file = new File("/sys/devices/fb.11/graphics/fb0/pwr_bl");
            FileReader reader = new FileReader(file);
            char[] bb = new char[1024];
            int n;
            while ((n = reader.read(bb)) != -1) {
                str += new String(bb, 0, n);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /***
     * 息屏亮屏操作
     * @param paramInt
     */
    public static void openOrClose(int paramInt) {
        try {
            writeFile(paramInt);
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static void writeFile(int paramInt) {
        try {
            File localFile = new File("/sys/devices/fb.11/graphics/fb0/pwr_bl");
            localFile.setExecutable(true);
            localFile.setReadable(true);
            localFile.setWritable(true);
            if (paramInt == 0) {
                do_exec("busybox echo 0 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
            } else if (paramInt == 1) {
                do_exec("busybox echo 1 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }


    /**
     * 设置屏幕的亮度
     */
    public static void setScreenBrightness(Activity context, int process) {
        //设置当前窗口的亮度值.这种方法需要权限android.permission.WRITE_EXTERNAL_STORAGE
        WindowManager.LayoutParams localLayoutParams = context.getWindow().getAttributes();
        float f = process / 255.0F;
        localLayoutParams.screenBrightness = f;
        context.getWindow().setAttributes(localLayoutParams);
        //修改系统的亮度值,以至于退出应用程序亮度保持
        saveBrightness(context.getContentResolver(), process);

    }

    /***
     * 保存屏幕亮度
     * @param resolver
     * @param brightness
     */
    public static void saveBrightness(ContentResolver resolver, int brightness) {
        //改变系统的亮度值
        //这里需要权限android.permission.WRITE_SETTINGS
        //设置为手动调节模式
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //保存到系统中
        Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        resolver.notifyChange(uri, null);
    }


    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    private void setScreenMode(Activity context, int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 设置当前屏幕亮度值  0--255
     */
    private void saveScreenBrightness(Activity context, int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }


    /**
     * 获得当前屏幕亮度值  0--255
     */
    public static int getScreenBrightness(Activity context) {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        return screenBrightness;
    }


    /**
     * 获取屏幕亮度
     *
     * @return
     */
    public static int getSystemBrightness(Context context) {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     * 获取屏幕的旋转角度
     *
     * @param context
     * @return
     */
    public static int getScreenRoate(Context context) {
        try {
            int angle = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).
                    getDefaultDisplay().getRotation();
            switch (angle) {
                case Surface.ROTATION_0:
                    return 0;
                case Surface.ROTATION_90:
                    return 90;
                case Surface.ROTATION_180:
                    return 180;
                case Surface.ROTATION_270:
                    return 270;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
