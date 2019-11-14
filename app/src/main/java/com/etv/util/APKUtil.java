package com.etv.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.etv.view.MyToastView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class APKUtil {
    Context context;

    public APKUtil(Context context) {
        this.context = context;
    }


    /***
     * 获取APP的软件版本号
     * @param context
     * @param packname
     * @return
     */
    public static int getOtherAppVersion(Context context, String packname) {
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            versionCode = packinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static void startAppFromService(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void startApp(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 打开文件管理器
     * @param context
     */
    public static void openFileManagerApk(Context context) {
        if (APKUtil.ApkState(context, "com.android.rockchip")) {
            Intent param = new Intent("android.intent.action.MAIN");
            param.addCategory("android.intent.category.LAUNCHER");
            param.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            param.setComponent(new ComponentName("com.android.rockchip", "com.android.rockchip.RockExplorer"));
            context.startActivity(param);
        } else if (APKUtil.ApkState(context, "com.softwinner.TvdFileManager")) {
            Intent param = new Intent("android.intent.action.MAIN");
            param.addCategory("android.intent.category.LAUNCHER");
            param.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            param.setComponent(new ComponentName("com.softwinner.TvdFileManager", "com.softwinner.TvdFileManager.MainUI"));
            context.startActivity(param);
        } else {
            MyToastView.getInstance().Toast(context, "没有找到合适的文件管理器，请联系售后");
        }
    }


    /**
     * 获取所有进程包名
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getAllProcess(Context context) {
        ArrayList<String> list = new ArrayList<String>();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo runningApp : appProcesses) {
            list.add(runningApp.processName);
        }
        return list;
    }

    /***
     * 返回当前前台运行的app
     *
     * @return
     */
    public static String appIsRunForset(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        String packName = appProcesses.get(0).processName;
        return packName;
    }

    /**
     * 安装APK文件
     */
    public void installApk(String filePath) {
        try {
            String authorities = "com.ys.etv.fileprovider";
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            File apkFile = new File(filePath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, authorities, apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            MyLog.cdl("======安装异常==" + e.toString());
            e.printStackTrace();
        }
    }

    /***
     * 判断APK有没有安装
     * @return
     */
    public static boolean ApkState(final Context context, String packageName) {
        boolean isInstall = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            if (packageInfo != null) {
                isInstall = true;
            } else {
                isInstall = false;
            }
        } catch (Exception e) {
            isInstall = false;
        }
        return isInstall;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static void writeFile(String str) throws IOException, InterruptedException {

        File file = new File("/sys/devices/fb.11/graphics/fb0/pwr_bl");
        file.setExecutable(true);
        file.setReadable(true);//设置可读权限
        file.setWritable(true);//设置可写权限
        if (str.equals("0")) {
            do_exec("busybox echo 0 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
        } else {
            do_exec("busybox echo 1 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
        }
    }

    public static void do_exec(String cmd) {
        try {
            /* Missing read/write permission, trying to chmod the file */
            Process su;
            su = Runtime.getRuntime().exec("su");
            String str = cmd + "\n" + "exit\n";
            su.getOutputStream().write(str.getBytes());

            if ((su.waitFor() != 0)) {
                System.out.println("cmd=" + cmd + " error!");
                throw new SecurityException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openOrClose(String cmd) {
        try {
            APKUtil.writeFile(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 静默安装APK
     * @param apkPath
     * @return
     */
    public boolean installApkSlient(String apkPath) {
        boolean result = false;
        File f = new File(apkPath);
        if (f != null && f.exists()) {
            DataOutputStream dataOutputStream = null;
            BufferedReader errorStream = null;
            try {
                // 申请su权限
                Process process = Runtime.getRuntime().exec("su");
                dataOutputStream = new DataOutputStream(process.getOutputStream());
                // 执行pm install命令
                String command = "pm install -r " + apkPath + "\n";
                dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
                dataOutputStream.flush();
                dataOutputStream.writeBytes("exit\n");
                dataOutputStream.flush();
                process.waitFor();
                errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String msg = "";
                String line;
                // 读取命令的执行结果
                while ((line = errorStream.readLine()) != null) {
                    msg += line;
                }
                // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
                if (!msg.contains("Failure")) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dataOutputStream != null) {
                        dataOutputStream.close();
                    }
                    if (errorStream != null) {
                        errorStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        return result;
    }

}
