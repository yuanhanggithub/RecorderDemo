package com.etv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.etv.util.ActivityCollector;
import com.etv.util.FileUtil;

public class BaseActivity extends Activity {

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }

    private void init() {
        ActivityCollector.addActivity(this);
        FileUtil.creatPathNotExcit();
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
