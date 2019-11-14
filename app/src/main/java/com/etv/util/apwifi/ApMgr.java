package com.etv.util.apwifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;

public class ApMgr {

    //check whether wifi hotspot on or off
    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        } catch (Throwable ignored) {
        }
        return false;
    }

    //close wifi hotspot
    public static boolean disableAp(Context context) {
        boolean isDealSuccess = false;
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            isDealSuccess = (boolean) method.invoke(wifimanager, null, false);
            Log.e("apState", "===========关闭热点===：" + isDealSuccess);
            return isDealSuccess;
        } catch (Throwable ignored) {
            Log.e("apState", "===========关闭热点Error===：" + ignored.toString());

        }
        return isDealSuccess;
    }

    // toggle wifi hotspot on or off
    public static boolean configApState(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            if (isApOn(context)) {
                wifimanager.setWifiEnabled(false);
                // if ap is on and then disable ap
                disableAp(context);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // toggle wifi hotspot on or off, and specify the hotspot name
    public static boolean configApState(Context context, String apName) {
        boolean isDealState = false;
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            wificonfiguration = new WifiConfiguration();
            wificonfiguration.SSID = apName;
            // if WiFi is on, turn it off
            if (isApOn(context)) {
                wifimanager.setWifiEnabled(false);
                // if ap is on and then disable ap
                disableAp(context);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            isDealState = (boolean) method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            return isDealState;
        } catch (Exception e) {
            Log.i("wifiLog", "=======" + e.toString());
            e.printStackTrace();
        }
        return isDealState;
    }

    /**
     * 创建热点
     *
     * @param mSSID   热点名称
     * @param mPasswd 热点密码
     * @param isOpen  是否是开放热点
     */
    public static void configApState(Context context, String mSSID, String mPasswd, boolean isOpen) {
        Method method1 = null;
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            if (isOpen) {
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            } else {
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            }
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            method1.invoke(mWifiManager, netConfig, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
