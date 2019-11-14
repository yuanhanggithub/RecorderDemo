package com.etv.util.apwifi;

import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ys.etv.R;


/**
 * Created by jsjm on 2018/5/11.
 */

public class WifiUpdateView {


    /***
     * 更新是否需要密碼的圖標
     * @param scanResult
     * @param imgLock
     */
    public static void updateLock(ScanResult scanResult, ImageView imgLock) {
        String isLock = "";
        String descOri = scanResult.capabilities;
        if (descOri.toUpperCase().contains("WPA-PSK")) {
            isLock = "WPA";
        }
        if (descOri.toUpperCase().contains("WPA2-PSK")) {
            isLock = "WPA2";
        }
        if (descOri.toUpperCase().contains("WPA-PSK")
                && descOri.toUpperCase().contains("WPA2-PSK")) {
            isLock = "WPA/WPA2";
        }
        if (TextUtils.isEmpty(isLock)) {
            imgLock.setVisibility(View.GONE);
        } else {
            imgLock.setVisibility(View.VISIBLE);
        }
    }

    /***
     * 更新wifi图标
     * @param level
     * @param wifiImage
     */
    public static void updateWifiImg(int level, ImageView wifiImage) {
        int imgId = R.drawable.wifi_4;
        if (Math.abs(level) > 100) {
            imgId = R.drawable.wifi_1;
        } else if (Math.abs(level) > 80) {
            imgId = R.drawable.wifi_2;
        } else if (Math.abs(level) > 70) {
            imgId = R.drawable.wifi_3;
        } else if (Math.abs(level) > 60) {
            imgId = R.drawable.wifi_3;
        } else if (Math.abs(level) > 50) {
            imgId = R.drawable.wifi_4;
        } else {
            imgId = R.drawable.wifi_4;
        }
        wifiImage.setImageResource(imgId);
    }


    /***
     * 刷新状态
     * @param wifi
     * @param txtWifiDesc
     */
    public static void updateWifiState(NetworkInfo.State wifi, TextView txtWifiDesc) {
        String lineState = "";
        if (wifi == NetworkInfo.State.CONNECTING) {
            lineState = "连接中...";
        } else if (wifi == NetworkInfo.State.DISCONNECTED) {
            lineState = "网络切换中";
        } else if (wifi == NetworkInfo.State.DISCONNECTING) {
            lineState = "网络切换中";
        } else if (wifi == NetworkInfo.State.SUSPENDED) {
            lineState = "连接中...";
        } else if (wifi == NetworkInfo.State.CONNECTED) {
            lineState = "已连接";
        } else if (wifi == NetworkInfo.State.UNKNOWN) {
            lineState = "网络异常,请在高级设置，进入高级系统设置";
        }
        txtWifiDesc.setText(lineState);
    }
}
