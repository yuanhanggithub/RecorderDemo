package com.etv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.etv.config.AppInfo;
import com.etv.service.ScreenViewService;

public class PoliceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        initOther();
    }


    public void initOther() {
        Intent intent = new Intent(PoliceActivity.this, ScreenViewService.class);
        startService(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("cdl", "=============" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_1) {
            Intent intent = new Intent();
            intent.setAction(AppInfo.ONE_KEY_POLICE_BROAD);
            sendBroadcast(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
