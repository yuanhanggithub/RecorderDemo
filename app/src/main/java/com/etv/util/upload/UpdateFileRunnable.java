package com.etv.util.upload;

import com.etv.util.CodeUtil;
import com.etv.util.MyLog;
import com.etv.util.SimpleDateUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.MIME;

import java.io.File;


/***
 * 文件上传线程
 */
public class UpdateFileRunnable implements Runnable {

    String requestUtl;
    String filePath;
    UpdateImageListener listener;

    public static final int TAG_FILE = 1;
    public static final int TAG_IMAGE = 2;
    int updateTag = TAG_FILE;

    public UpdateFileRunnable(String requestUtl, String filePath) {
        this.requestUtl = requestUtl;
        this.filePath = filePath;
    }

    public UpdateFileRunnable(String requestUtl, String filePath, int updateTag, UpdateImageListener listener) {
        this.requestUtl = requestUtl;
        this.filePath = filePath;
        this.listener = listener;
        this.updateTag = updateTag;
    }


    @Override
    public void run() {
        photoUpload();
    }

    public void photoUpload() {
        HttpUtils utils = new HttpUtils(50000); // 设置连接超时
        String fileName = CodeUtil.getUniquePsuedoID();
        MyLog.cdl("=====fineName = " + fileName);
        RequestParams params = new RequestParams();
        String token = SimpleDateUtil.formatBig(System.currentTimeMillis()) + "";
        params.addHeader("token", token);
        if (updateTag == TAG_FILE) {  //文件
            params.addBodyParameter("fileType", "7");
            params.addBodyParameter("fileName", fileName);
        } else if (updateTag == TAG_IMAGE) { //图片

        }
        File file = new File(filePath);
        params.addBodyParameter("file", file, MIME.ENC_BINARY);
        utils.send(HttpRequest.HttpMethod.POST, requestUtl, params,
                new RequestCallBack() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                        int progress = (int) (current * 100 / total);
                        if (listener == null) {
                            return;
                        }
                        listener.updateImageProgress(progress);
                    }

                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        MyLog.update("上传成功===" + arg0.result.toString());
                        if (listener == null) {
                            return;
                        }
                        listener.updateImageSuccess("上传成功");
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        MyLog.update("上传失败==" + arg0 + ":" + arg1);
                        if (listener == null) {
                            return;
                        }
                        listener.updateImageSuccess("上传失败:" + arg0.toString());
                    }
                });
    }

}
