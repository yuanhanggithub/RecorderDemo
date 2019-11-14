package com.etv.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.etv.config.AppInfo;
import com.etv.service.ScreenViewService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    public static final String TAG = "FileUtil";

    //删除目录或者文件
    public static boolean deleteDirOrFile(String Path) {
        return deleteDirOrFile(new File(Path));
    }

    public static boolean deleteDirOrFile(File file) {
        if (file.isFile()) {
            file.delete();
            return false;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return false;
            }
            for (File f : childFile) {
                deleteDirOrFile(f);
            }
            file.delete();
        }
        FileUtil.creatPathNotExcit();
        return false;
    }


    public static void MKDIRSfILE(String path) {
        File file = null;
        try {
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void creatPathNotExcit() {
        String path = null;
        File file = null;
        try {
            path = AppInfo.BASE_PATH_INNER + "/etv";   //sdcard/etv
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }
            path = AppInfo.BASE_CRASH_LOG();   //sdcard/crashlog
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }

            path = AppInfo.BASE_PATH();   //sdcard/etv
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }
            path = AppInfo.BASE_APK();    //sdcard/etv/apk
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }

            path = AppInfo.BASE_IMAGE();   //sdcard/etv/image
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }
            path = AppInfo.BASE_TASK_URL();   //sdcard/etv/task
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }
            path = AppInfo.BASE_CACHE();   //sdcard/etv/cache
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }
            path = AppInfo.BASE_DEFAULR_PLAY();   //sdcard/etv/default
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }

            path = AppInfo.BASE_IMAGE_RECEIVER();   //sdcard/etv/receiver
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.e("cdl", "==========" + isCreate + "/" + path);
            }
        } catch (Exception e) {
        }
    }


    public static String getTxtInfoFromTxtFile(String filePath) {
        return getTxtInfoFromTxtFile(filePath, TYPE_UTF_8);
    }

    /***
     * 从文本中获取txt信息
     * @param filePath
     * @type type
     * @return
     */
    public static final String TYPE_UTF_8 = "UTF-8";
    public static final String TYPE_GBK = "GBK";

    public static String getTxtInfoFromTxtFile(String filePath, String type) {
        String result = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return result;
            }
            int length = (int) file.length();
            byte[] buff = new byte[length];
            FileInputStream fin = new FileInputStream(file);
            fin.read(buff);
            fin.close();
            result = new String(buff, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


//    /***
//     * 将IP地址写入本地
//     * @param errorDesc
//     * @return
//     */
//    public static void saveDevStatueTxtToSdcard(String errorDesc) {
//        errorDesc = "\n\n" + errorDesc;
//        //删除多余的文件，仅仅保留5个文件
//        File fileDel = new File(AppInfo.BASE_INFO());
//        File[] fileList = fileDel.listFiles();
//        if (fileList != null && fileList.length > 5) {
//            String fileOne = fileList[0].getAbsolutePath();
//            deleteDirOrFile(fileOne);
//        }
//        String data = SimpleDateUtil.getDateOther();
//        String fileDir = AppInfo.BASE_INFO() + "/" + data + ".txt";
//        FileWriteToSdInfoRunnable runnable = new FileWriteToSdInfoRunnable(errorDesc, fileDir);
//        EtvService.getInstance().executor(runnable);
//    }

    public static String FormetFileSize(long paramLong) {
        Object localObject = new DecimalFormat("#.00");
        if (paramLong == 0L) {
            return "0B";
        }
        if (paramLong < 1024L) {
            localObject = ((DecimalFormat) localObject).format(paramLong) + "B";
        }
        if (paramLong < 1048576L) {
            localObject = ((DecimalFormat) localObject).format(paramLong / 1024.0D) + "KB";
        } else if (paramLong < 1073741824L) {
            localObject = ((DecimalFormat) localObject).format(paramLong / 1048576.0D) + "MB";
        } else {
            localObject = ((DecimalFormat) localObject).format(paramLong / 1073741824.0D) + "GB";
        }
        return (String) localObject;
    }

    public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    public static Bitmap ResizeBitmap(Bitmap bitmap, int scale) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(1 / scale, 1 / scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }


    /**
     * @param bitmap
     * @param destPath
     * @param quality
     */
    public static void writeImage(Bitmap bitmap, String destPath, int quality) {
        try {
            deleteDirOrFile(destPath);
            if (createFile(destPath)) {
                FileOutputStream out = new FileOutputStream(destPath);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                    out.flush();
                    out.close();
                    out = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


//    /***
//     * 清理其他无用的文件
//     */
//    public static void delEtvDisonlineTask() {
//        try {
//            String innerSdPath = AppInfo.BASE_PATH_INNER;
//            File fileDir = new File(innerSdPath);
//            if (fileDir == null) {
//                return;
//            }
//            if (!fileDir.exists()) {
//                return;
//            }
//            File[] fileList = fileDir.listFiles();
//            if (fileList == null) {
//                return;
//            }
//            if (fileList.length < 1) {
//                return;
//            }
//            for (File file : fileList) {
//                String fileName = file.getName();
//                String filePath = file.getPath();
//                if (fileName.contains(AppInfo.DISONLINE_TASK_NAME_DIR)) {
//                    MyLog.cdl("===删除的文件path==" + filePath);
//                    FileUtil.deleteDirOrFile(filePath);
//                }
//            }
//        } catch (Exception e) {
//            MyLog.cdl("11111====================999" + e.toString());
//            e.printStackTrace();
//        }
//    }
    /**
     * @param is_removale true 外置
     *                    false 内置
     *                    <p>
     *                    <p>
     *                    获取剩余内存
     */
    public static String readSDCard(String path, Boolean is_removale) {
        DecimalFormat df = new DecimalFormat("0.00");
        StatFs sf = new StatFs(path);
        long blockSize;
        long blockCount;
        long availCount;
        if (Build.VERSION.SDK_INT > 18) {
            blockSize = sf.getBlockSizeLong(); //文件存储时每一个存储块的大小为4KB
            blockCount = sf.getBlockCountLong();//存储区域的存储块的总个数
            availCount = sf.getAvailableBlocksLong();//存储区域中可用的存储块的个数（剩余的存储大小）
        } else {
            blockSize = sf.getBlockSize();
            blockCount = sf.getBlockCount();
            availCount = sf.getAvailableBlocks();
        }
        Log.d("sss","总的存储空间大小:" + blockSize*blockCount/1073741824+"GB" + ",剩余空间:" + availCount * blockSize / 1048576 + "MB"
                +"--存储块的总个数--"+blockCount+"--一个存储块的大小--"+blockSize/1024+"KB");
        return df.format((double) availCount * (double) blockSize / 1048576);
    }

    public static synchronized void delete (String path, Date date, int type){
        File file = new File(path);
        File[] filearray = file.listFiles();
        if (file.exists()){
            if (filearray != null && filearray.length>0) {
                long min = getCreateTime(filearray[0]).getTime();
                int j =0;
                for (int i = 0; i < filearray.length; i++) {
                    if (type== ScreenViewService.TYPE){
                        if (min>getCreateTime(filearray[i]).getTime()){
                            min = getCreateTime(filearray[i]).getTime();
                            j = i;
                        }
                        if (i==(filearray.length-1)){
//                        filearray[j].delete();
                            deleteDirWihtFile(filearray[j]);
                            Log.i("DeleteFolder", "file:=" +j+"/"+ filearray.length);
                        }
                    }else {
                        Log.i("DeleteFolder", "file=" + filearray.length);
                        long time = (date.getTime() - getCreateTime(filearray[i]).getTime()) / (60 * 60 * 1000 * 24);
                        Log.i(TAG,"data="+time);
                        if (time > 6) {
//                        filearray[i].delete();
                            Log.i(TAG,"delete()===");
                            deleteDirWihtFile(filearray[i]);
                        }
                    }
                }
            }
        }
    }
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists())
            return;
        if(!dir.isDirectory()){
            dir.delete();
            Log.i(TAG,"dir.delete()===");
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static Date getCreateTime(File path) {
        File f =path;
        Date date = null;
        if (f.exists()) {
//            FileInputStream fis = null;
            try {
//                fis = new FileInputStream(f);
                SimpleDateFormat simpdate= new SimpleDateFormat("yyyy-MM-dd");
                String time = simpdate.format(new Date(f.lastModified()));
                date = simpdate.parse(time);
                date.getTime();
//                System.out.println("文件文件创建时间" + time);
//                // B,KB,MB,
//                System.out.println("文件大小:" + fis.available() + "B");
//                System.out.println("文件名称：" + f.getName());
//                System.out.println("文件是否存在：" + f.exists());
//                System.out.println("文件的相对路径：" + f.getPath());
//                System.out.println("文件的绝对路径：" + f.getAbsolutePath());
//                System.out.println("文件可以读取：" + f.canRead());
//                System.out.println("文件可以写入：" + f.canWrite());
//                System.out.println("文件上级路径：" + f.getParent());
//                System.out.println("文件大小：" + f.length() + "B");
//                System.out.println("文件最后修改时间：" + new Date(f.lastModified()));
//                System.out.println("是否是文件类型：" + f.isFile());
//                System.out.println("是否是文件夹类型：" + f.isDirectory());
//                Log.i("FuleUtils", "文件文件创建时间:" + time + "\n" + "文件名称："
//                        + f.getName() + "\n" + "文件是否存在：" + f.exists() + "\n"
//                        + "文件的相对路径：" + f.getPath() + "\n" + "文件的绝对路径："
//                        + f.getAbsolutePath() + "\n" + "文件可以写入：" + f.canWrite()
//                        + "\n" + "是否是文件夹类型：" + f.isDirectory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG,"data="+date.getTime());
        return date;
    }

    public static boolean execFor7(String command) {
        Log.d("execFor7", "command = " + command);
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String s = command + "\n";
            dataOutputStream.write(s.getBytes(Charset.forName("utf-8")));
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
            Log.d("execFor7", "execFor7 msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("execFor7", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }


    public static boolean getPath(String path){
        if (path==null)
            return false;
        File file = new File(path);
        if (file.exists())
            return true;
        else
            return false;
    }
}
