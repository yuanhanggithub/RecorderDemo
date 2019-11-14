package com.etv.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.etv.config.AppInfo;
import com.etv.service.ScreenViewService;
import com.etv.util.MyLog;
import com.etv.util.SharedPerManager;

public class PhoneBroadcastReceiver extends BroadcastReceiver {

    TelephonyManager telMgr;
    private Handler handler = new Handler();
    @Override
    public void onReceive(final Context context, Intent intent) {
        telMgr = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        switch (telMgr.getCallState()) {
            //来电
            case TelephonyManager.CALL_STATE_RINGING:
                MyLog.cdl("=======================000来电");
                //通知到Service 撑开界面
                Intent intentCall = new Intent();
                intentCall.setAction(AppInfo.CALL_FROM_THER);
                context.sendBroadcast(intentCall);
                break;
            //响铃
            case TelephonyManager.CALL_STATE_OFFHOOK:
                MyLog.cdl("=======================000响铃");
                Intent intentCallTo = new Intent();
//                SharedPerManager.setTime(System.currentTimeMillis());
//                Log.i("yuanhang","time=="+SharedPerManager.getTime());
                intentCallTo.setAction(AppInfo.CALL_TO_PHONE_RING);
                context.sendBroadcast(intentCallTo);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                MyLog.cdl("=======================000挂断");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (telMgr.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
//                            long endtime = System.currentTimeMillis()-SharedPerManager.getTime();
//                            Log.i("yuanhang","starttime=="+System.currentTimeMillis()+"/time = "+SharedPerManager.getTime());
//                            Log.i("yuanhang","endtime=="+endtime);
//                            if (endtime>1000) {
                                Intent intent1 = new Intent();
                                intent1.setAction(AppInfo.HAND_UP_PHONE);
                                context.sendBroadcast(intent1);
//                            }
                        }
                    }
                }, 1000);
                break;
        }
    }
}
