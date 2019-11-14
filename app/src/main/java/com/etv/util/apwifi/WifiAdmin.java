package com.etv.util.apwifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.etv.util.MyLog;

import java.util.List;

/**
 * Class Name: WifiAdmin.java<br>
 * Function:Wifi连接管理工具类<br>
 * <p>
 * Modifications:<br>
 *
 * @author ZYT DateTime 2014-5-14 下午2:24:14<br>
 * @version 1.0<br>
 *          <br>
 */
public class WifiAdmin {
    // 定义一个WifiManager对象
    private WifiManager mWifiManager;

    public WifiAdmin(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }


    public boolean openWifi() {
        boolean bRet = true;
        if (!mWifiManager.isWifiEnabled()) {
            bRet = mWifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    public boolean connect(String SSID, String Password, WifiConnectUtils.WifiCipherType Type) {
        if (!this.openWifi()) {
            return false;
        }
        while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }
        System.out.println("WifiAdmin#connect==连接结束");
        WifiConfiguration wifiConfig = createWifiInfo(SSID, Password, Type);
        //
        if (wifiConfig == null) {
            return false;
        }
        WifiConfiguration tempConfig = this.isExsits(SSID);

        int tempId = wifiConfig.networkId;
        if (tempConfig != null) {
            tempId = tempConfig.networkId;
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        int netID = mWifiManager.addNetwork(wifiConfig);
        // 断开连接
        mWifiManager.disconnect();
        // 重新连接
        // netID = wifiConfig.networkId;
        // 设置为true,使其他的连接断开
        boolean bRet = mWifiManager.enableNetwork(netID, true);
        boolean isConnection = mWifiManager.reconnect();
        return bRet;
    }

    // 查看以前是否也配置过这个网络
    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password, WifiConnectUtils.WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WifiConnectUtils.WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiConnectUtils.WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiConnectUtils.WifiCipherType.WIFICIPHER_WPA) {
            // 修改之后配置
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

        } else {
            return null;
        }
        return config;
    }


    /**
     * Function:信号强度转换为字符串<br>
     *
     * @param level <br>
     * @author ZYT DateTime 2014-5-14 下午2:14:42<br>
     */
    public static String singlLevToStr(int level) {
        String resuString = "无信号";
        if (Math.abs(level) < 51) { //极强
            resuString = "极强";
        } else if (Math.abs(level) > 50 && Math.abs(level) < 61) {
            resuString = "较强";
        } else if (Math.abs(level) > 60 && Math.abs(level) < 71) {
            resuString = "弱";
        } else if (Math.abs(level) > 70 && Math.abs(level) < 85) {
            resuString = "非常弱";
        } else if (Math.abs(level) > 84) {
            resuString = "极弱";
        } else {
            resuString = "无信号";
        }
        MyLog.e("WifiFragmentFragment", "==当前信号强度==" + level + "//" + resuString);
        return resuString;
    }

    /**
     * 添加到网络
     *
     * @param wcg
     */
    public boolean addNetwork(WifiConfiguration wcg) {
        if (wcg == null) {
            return false;
        }
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        mWifiManager.saveConfiguration();
        System.out.println(b);
        return b;
    }


    /***
     * 自动连接wifi
     * @param scan
     * @return
     */
    public boolean connectNoWifi(String scan) throws Exception {
        boolean connectResult = false;
        WifiConfiguration config = CreateNoWifiInfo(scan);
        connectResult = addNetwork(config);
        return connectResult;
    }

    /***
     * 自动连接wifi
     * @param scan
     * @return
     */
    public WifiConfiguration CreateNoWifiInfo(String scan) {
        WifiConfiguration config = new WifiConfiguration();
        config.hiddenSSID = false;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        config.SSID = "\"" + scan + "\"";
        config.preSharedKey = null;

        return config;
    }
}
