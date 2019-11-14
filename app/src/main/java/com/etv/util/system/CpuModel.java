package com.etv.util.system;

public class CpuModel {

    /***
     * 获取CPU型号
     * @return
     */
    public static String getMobileType() {
        String cpuModel = android.os.Build.PRODUCT;
        if (cpuModel.contains("_")) {
            cpuModel = cpuModel.substring(0, cpuModel.indexOf("_"));
        }
        return cpuModel;
    }
}
