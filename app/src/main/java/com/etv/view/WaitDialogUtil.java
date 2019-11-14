package com.etv.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ys.etv.R;

import java.util.Timer;
import java.util.TimerTask;


public class WaitDialogUtil {

    Dialog waitDialog;
    Context context;
    public static final String TAG = WaitDialogUtil.class.getName();
    private final TextView mTv;

    public WaitDialogUtil(Context context) {
        this.context = context;
        waitDialog = new Dialog(context, R.style.MyDialog);
        View recdialog = View.inflate(context, R.layout.dialog_wait, null);
        mTv = (TextView) recdialog.findViewById(R.id.tv_dialog_wait);
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitDialog.setContentView(recdialog);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.setCancelable(true);
        waitDialog.getWindow().setGravity(Gravity.CENTER);
    }

    public void show(String text_dialog) {
        try {
            dismiss();
            mTv.setText(text_dialog);
            waitDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void showOnTime(String text_dialog, long showTime) {
//        dismiss();
//        this.showTime = showTime;
//        mTv.setText(text_dialog);
//        waitDialog.show();
//        startTimer();
//    }

//    public void startTimer() {
//        cacelTime();
//        timer = new Timer(true);
//        task = new MyTask();
//        timer.schedule(task, showTime);
//    }

//    private void cacelTime() {
//        if (timer != null) {
//            timer.cancel();
//        }
//        if (task != null) {
//            task.cancel();
//        }
//    }


//    Timer timer;
//    MyTask task;
//    long showTime = 5000;
//
//    class MyTask extends TimerTask {
//        @Override
//        public void run() {
//            handler.sendEmptyMessage(MESSAGE_TIME_CLODE);
//        }
//    }
//
//    private static final int MESSAGE_TIME_CLODE = 8976;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case MESSAGE_TIME_CLODE:
//                    dismiss();
//                    break;
//            }
//        }
//    };

    public void dismiss() {
        try {
            if (waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        cacelTime();
    }

    public boolean isShowing() {
        if (waitDialog != null && waitDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public void show(String text_dialog, int time) {
        mTv.setText(text_dialog);
        waitDialog.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                waitDialog.dismiss();
                t.cancel();
            }
        }, time);
    }

}
