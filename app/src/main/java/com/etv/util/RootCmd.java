package com.etv.util;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class RootCmd {

    private static boolean mHaveRoot = false;
    public static final String PROOERTY_INFO = "persist.sys.displayrot";   //主屏幕的旋转方向
    public static final String PROOERTY_OTHER_INFO = "persist.orientation.vhinit";   //副屏幕的旋转方向

    //判断设备是否连网线  0：没有连接   1：已经连接
    public static final String JUJLE_ETH_LINE_DEV_ORDER = "cat /sys/class/net/eth0/carrier";

    //    获取和设置SystemProperties属性的代码
    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, defaultValue));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    public static void setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 判断当前设备有没有权限
     * @return
     */
    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
            if (ret != -1) {
                mHaveRoot = true;
            } else {
            }
        } else {
        }
        return mHaveRoot;
    }

    // 执行命令但不关注结果输出
    public static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;
        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 执行命令并且输出结果
     */
    public static String execRootCmdBackInfo(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());
            Log.e("cdl", cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {
                Log.d("result", line);
                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /***
     * @param command
     * @return
     */
    public static boolean exusecmd(String command) {
        Log.e("cdl", "===========" + command);
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            Log.e("cdl", "======000==writeSuccess======");
            process.waitFor();
        } catch (Exception e) {
            Log.e("cdl", "======111=writeError======" + e.toString());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //  RootCmd.writeFileToSystem(srcfile.getPath(), "/system/media/bootanimation.zip");
    public static void writeFileToSystem(String filePath, String sysFilePath) {
        exusecmd("mount -o rw,remount /system");
        exusecmd("rm -rf /system/media/boomAnimal.zip");
        exusecmd("chmod 777 /system/media");
        exusecmd("cp  " + filePath + " " + sysFilePath);
    }

    public static void writeFileToSystemApp(String filePath, String sysFilePath) {
        exusecmd("mount -o rw,remount /system");
        exusecmd("rm -rf /system/app/guardian.apk");
        exusecmd("cp  " + filePath + " " + sysFilePath);
        exusecmd("chmod 777 /system/app/guardian.apk");
    }

    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }

    public static boolean delGuardianApk() {
        exusecmd("mount -o rw,remount /system");
        boolean isdel = exusecmd("rm -rf /system/app/guardian.apk");
        exusecmd("chmod 777 /system/app/guardian.apk");
        return isdel;
    }

}
