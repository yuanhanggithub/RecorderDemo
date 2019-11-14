package com.etv.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.etv.activity.PoliceActivity;
import com.etv.activity.SettingActivity;
import com.etv.activity.StartActivity;
import com.etv.config.AppInfo;
import com.etv.service.ScreenViewService;


public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    private  static final String SD_PULLOUT ="android.intent.action.MEDIA_EJECT";
    private  static final String SD_INSERT ="android.intent.action.MEDIA_MOUNTED";
    public void onReceive(final Context context, Intent intent) {


        String str1 = intent.getAction();
        String str2 = "开机广播接收的广播==" + str1;
        Log.i("yuanhang",str2);
        if (str1.equals(ACTION_BOOT)) {
            Intent localIntent1 = new Intent(context, StartActivity.class);
            localIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(localIntent1);
        }else if (str1.equals(AppInfo.EXIT_APP)){
            context.stopService(new Intent(context, ScreenViewService.class));
            ((Service) context).stopSelf();
        }
//        else if (str1.equals(SD_PULLOUT)){
//            Log.i("BootBroadcastReceiver","action========"+SD_PULLOUT);
//            context.stopService(new Intent(context, ScreenViewService.class));
//            ((Service) context).stopSelf();
//        }else if (str1.equals(SD_INSERT)){
//
//        }
    }
}