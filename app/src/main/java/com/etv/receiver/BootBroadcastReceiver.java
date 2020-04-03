package com.etv.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.etv.activity.StartActivity;
import com.etv.config.AppInfo;
import com.etv.service.ScreenViewService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.WIFI_SERVICE;
import static android.os.Looper.getMainLooper;


public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    private  static final String SD_PULLOUT ="android.intent.action.MEDIA_EJECT";
    private  static final String SD_INSERT ="android.intent.action.MEDIA_MOUNTED";
    private WifiManager wifiManager;
    private Handler handler;
    private ResultReceiver mResultReceiver;
    public void onReceive(final Context context, Intent intent) {


        String str1 = intent.getAction();
        String str2 = "开机广播接收的广播==" + str1;
        Log.i("yuanhang",str2);
//        setWifiApEnabled(context,true);
        if (str1.equals(ACTION_BOOT)) {
            Intent localIntent1 = new Intent(context, StartActivity.class);
            localIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(localIntent1);
        }else if (str1.equals(AppInfo.EXIT_APP)){
//            context.stopService(new Intent(context, ScreenViewService.class));
//            ((Service) context).stopSelf();
        }
//        else if (str1.equals(SD_PULLOUT)){
//            Log.i("BootBroadcastReceiver","action========"+SD_PULLOUT);
//            context.stopService(new Intent(context, ScreenViewService.class));
//            ((Service) context).stopSelf();
//        }else if (str1.equals(SD_INSERT)){
//
//        }
    }


    public boolean setWifiApEnabled(Context mContext,boolean enabled) {
        wifiManager = (WifiManager)mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (enabled) { // disable WiFi in any case
            wifiManager.setWifiEnabled(false);
        }
        handler = new Handler(getMainLooper());
        try {
            mResultReceiver = new ResultReceiver(handler);
            WifiConfiguration apConfig = new WifiConfiguration();
            apConfig.SSID = "asdf";
            apConfig.preSharedKey = "12121212";
            apConfig.allowedKeyManagement.set(4);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                Method mMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
                mMethod.invoke(wifiManager,apConfig);
                ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                Field mField = connectivityManager.getClass().getDeclaredField("TETHERING_WIFI");
                mField.setAccessible(true);
                writeToLog("field.setAccessible ");
                int mTETHERING_WIFI = (int)mField.get(connectivityManager);
                Log.v("mTETHERING_WIFI:",String.valueOf(mTETHERING_WIFI));
                Field iConnMgrField = connectivityManager.getClass().getDeclaredField("mService");
                iConnMgrField.setAccessible(true);
                Object iConnMgr = iConnMgrField.get(connectivityManager);
                Class<?> iConnMgrClass = Class.forName(iConnMgr.getClass().getName());
                Method mStartTethering1 = iConnMgrClass.getMethod("startTethering", int.class,ResultReceiver.class,boolean.class);
                mStartTethering1.setAccessible(true);
                mStartTethering1.invoke(iConnMgr, mTETHERING_WIFI,new ResultReceiver(handler){
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        super.onReceiveResult(resultCode, resultData);
                    }
                },true);
                return true;
            } else {
                Method method = wifiManager.getClass().getMethod(
                        "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
                return (Boolean) method.invoke(wifiManager, apConfig, enabled);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            writeToLog("setWifiApEnabled Exception1: " + e.getMessage());
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            writeToLog("setWifiApEnabled Exception2: " + e.getMessage());
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            writeToLog("setWifiApEnabled Exception3: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            writeToLog("setWifiApEnabled Exception4: " + e.getMessage());
            return false;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            writeToLog("setWifiApEnabled Exception5: " + e.getMessage());
            return false;
        }
    }

    private void writeToLog(String content){
        Log.i("BootBroadcastReceiver",content);
    }
}