package com.etv.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.etv.util.MyLog;
import com.ys.etv.R;

@SuppressLint("ResourceAsColor")
public class MyToastView {

    private static MyToastView instance = null;

    public static MyToastView getInstance() {
        if (instance == null) {
            instance = new MyToastView();
        }
        return instance;
    }

    /**
     * 调用方法 MyToast.getInstence.toast(this,info);
     *
     * @param context
     * @param info
     */
    Toast toast;

    public void Toast(Context context, String info) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            if (info == null || TextUtils.isEmpty(info)) {
                return;
            }
            View layout = LayoutInflater.from(context).inflate(R.layout.mytoast_view, null);
            TextView text = (TextView) layout.findViewById(R.id.text);
            if (info == null || info.length() < 2) {
                text.setText("   ");
            }
            text.setText(info);
            text.setTextColor(0xff000000);
            toast = Toast.makeText(context, "Custom location Toast", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 50);
            toast.setView(layout);
            toast.show();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}