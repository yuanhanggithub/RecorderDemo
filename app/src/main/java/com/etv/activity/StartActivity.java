package com.etv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.etv.util.MyLog;
import com.etv.util.SharedPerManager;

public class StartActivity extends BaseActivity {

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getScreenSize();
        initView();
    }

    private void initView() {
        String phoneNum = SharedPerManager.getPhoneNum();
        Intent intent = new Intent();
        if (phoneNum == null || phoneNum.length() < 3) {
            intent.setClass(StartActivity.this, SettingActivity.class);
        } else {
            intent.setClass(StartActivity.this, PoliceActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void getScreenSize() {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int width = localDisplayMetrics.widthPixels;
        int height = localDisplayMetrics.heightPixels;

//        WindowManager manager = this.getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        int width = outMetrics.widthPixels;
//        int height = outMetrics.heightPixels;
        MyLog.diff("start width=" + width + "  / height =" + height);
        SharedPerManager.setScreenWidth(width);
        SharedPerManager.setScreenHeight(height);
    }


}
