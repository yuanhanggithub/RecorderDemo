package com.police.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.etv.activity.BaseActivity;
import com.etv.view.MyToastView;
import com.police.adapter.CallPhoneAdapter;
import com.police.entity.CallPhoneEntity;
import com.police.util.PoliceUtil;
import com.ys.etv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CallPoliceActivity extends BaseActivity implements View.OnClickListener, CallPhoneAdapter.ItemClickAdapterListener {

    CallPhoneAdapter adapter;
    List<CallPhoneEntity> lists = new ArrayList<>();
    GridView grid_main;
    private TextView tv_show_num;
    Button btn_del, btn_call_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_police);
        initView();
    }

    String callNum = "";

    private void initView() {
        btn_call_phone = (Button) findViewById(R.id.btn_call_phone);
        btn_call_phone.setOnClickListener(this);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        tv_show_num = (TextView) findViewById(R.id.tv_show_num);
        tv_show_num.setText("110");
        callNum = "110";
        grid_main = (GridView) findViewById(R.id.grid_main);
        lists = PoliceUtil.getData();
        adapter = new CallPhoneAdapter(CallPoliceActivity.this, lists);
        adapter.setOnAdapterItemClick(this);
        grid_main.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_call_phone:
//                if (callNum.trim().length() < 1) {
//                    Toast.makeText(CallPoliceActivity.this, "请输入需要拨打的电话号码", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                Uri data = Uri.parse("tel:" + callNum);
//                intent.setData(data);
//                startActivity(intent);
//                finish();
                break;
            case R.id.btn_del:
                updateTextView();
                if (callNum.length() < 1) {
                    return;
                }
                callNum = callNum.substring(0, callNum.length() - 1);
                updateTextView();
                break;
        }
    }

    @Override
    public void clicktem(int position, CallPhoneEntity entity) {
        String num = entity.getTitle();
        callNum = callNum + num;
        updateTextView();
    }

    private void updateTextView() {
        tv_show_num.setText(callNum);
    }

    public void showToastView(final String toast) {
        MyToastView.getInstance().Toast(CallPoliceActivity.this, toast);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    private Timer timer;
    private MyTask task;

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(MESSAGE_TASK_CLOSE);
        }
    }

    private static final int MESSAGE_TASK_CLOSE = 1564;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_TASK_CLOSE) {
                cacelTimer();
                finish();
            }
        }
    };

    private void startTimer() {
        cacelTimer();
        timer = new Timer(true);
        task = new MyTask();
        timer.schedule(task, 30 * 1000);
    }


    private void cacelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cacelTimer();
    }
}
