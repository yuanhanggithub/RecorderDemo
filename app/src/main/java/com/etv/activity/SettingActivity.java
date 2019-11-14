package com.etv.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.etv.util.SharedPerManager;
import com.etv.util.permission.PermissionCallback;
import com.etv.util.permission.PermissionItem;
import com.etv.util.permission.PermissionUtil;
import com.etv.view.MyToastView;
import com.etv.view.dialog.EditTextDialog;
import com.ys.etv.R;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_setting);
        checkAppPermission();
        initView();
    }

    public void checkAppPermission() {
        if (Build.VERSION.SDK_INT >= 23) {   //悬浮窗权限
            if (!Settings.canDrawOverlays(SettingActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
                return;
            } else {
                //执行6.0以上绘制代码
            }
        } else {
            //执行6.0以下绘制代码
        }
    }

    Button btn_setting;
    Button btn_go_to_recorder;
    TextView tv_police_num;

    private void initView() {
        tv_police_num = (TextView) findViewById(R.id.tv_police_num);
        tv_police_num.setText("预留电话: " + SharedPerManager.getPhoneNum());
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_go_to_recorder = (Button) findViewById(R.id.btn_go_to_recorder);
        btn_setting.setOnClickListener(this);
        btn_go_to_recorder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                showEditTextDialog();
                break;
            case R.id.btn_go_to_recorder:
                if (!isPermission) {
                    MyToastView.getInstance().Toast(SettingActivity.this, "权限检查不通过");
                    checkVideoPermission();
                    return;
                }
                startActivity(new Intent(SettingActivity.this, PoliceActivity.class));
                finish();
                break;
        }
    }

    private void showEditTextDialog() {
        EditTextDialog editDialog = new EditTextDialog(SettingActivity.this);
        editDialog.setOnDialogClickListener(new EditTextDialog.EditTextDialogListener() {
            @Override
            public void commit(String content) {
                SharedPerManager.setPhoneNum(content);
                tv_police_num.setText("预留电话: " + content);
            }
        });
        editDialog.show("修改预留电话", SharedPerManager.getPhoneNum(), "提交");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVideoPermission();
    }

    boolean isPermission = false;

    private void checkVideoPermission() {
        List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
        permissonItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "SD卡读写", R.drawable.permission_ic_storage));
        permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD卡读写", R.drawable.permission_ic_storage));
        permissonItems.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "位置", R.drawable.permission_ic_storage));
        permissonItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_storage));
        permissonItems.add(new PermissionItem(Manifest.permission.CAMERA, "摄像机", R.drawable.permission_ic_camera));
        permissonItems.add(new PermissionItem(Manifest.permission.CALL_PHONE, "电话", R.drawable.permission_ic_camera));


        PermissionUtil.create(SettingActivity.this)
                .permissions(permissonItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onFinish() {
                        isPermission = true;
                    }

                    @Override
                    public void notAllow() {
                        isPermission = false;
                    }
                });

    }


}
