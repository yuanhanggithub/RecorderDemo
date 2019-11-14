package com.etv.util.upload;

import android.os.Handler;
import android.util.Log;

import com.etv.util.MyLog;
import com.etv.util.SimpleDateUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateImageRunnable implements Runnable {

    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码

    String requestUtl;
    String filePath;
    UpdateImageListener listener;
    private Handler handler = new Handler();

    public UpdateImageRunnable(String requestUtl, String filePath, UpdateImageListener listener) {
        this.requestUtl = requestUtl;
        this.filePath = filePath;
        this.listener = listener;
    }

    @Override
    public void run() {
        MyLog.e("time", "======开始计时==" + System.currentTimeMillis());
        File file = new File(filePath);
        if (!file.exists() || file == null) {
            updateError("上传文件不存在");
            return;
        }
        long fileLength = file.length();
        long fileSumUpdate = 0;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(requestUtl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                    + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset="
                    + CHARSET + LINE_END);
            sb.append(LINE_END);
            dos.write(sb.toString().getBytes());

            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024000];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                fileSumUpdate += len;
                updateFileProgress(fileSumUpdate, fileLength);
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                    .getBytes();
            dos.write(end_data);
            dos.flush();
            int res = conn.getResponseCode();
            if (res == 200) {
                StringBuffer buffer = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                //处理响应流
                responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((readLine = responseReader.readLine()) != null) {
                    buffer.append(readLine).append("\n");
                }
                responseReader.close();
                MyLog.e("time", "======结束计时==" + System.currentTimeMillis());
                Log.d("http", "===上传succes====" + buffer.toString());
                updateSuucess(buffer.toString());
            } else {
                updateError("上传失败");
                Log.e("http", "=======上传失败==");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateSuucess(final String successDesc) {
        if (listener == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.updateImageSuccess(successDesc);
            }
        });
    }

    public void updateFileProgress(final long fileUpdate, final long fileTotal) {
        if (listener == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                int progress = (int) (fileUpdate * 100 / fileTotal);
                listener.updateImageProgress(progress);
            }
        });
    }


    public void updateError(final String errorDesc) {
        if (listener == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.updateImageFailed(errorDesc);
            }
        });
    }


//    private void uploadMultiFile(String requestUrl, String imagePath) {
//        TcpService.isUpdateMirrorImage = true;
//        MyLog.update("=====开始时间=" + System.currentTimeMillis());
//        String imageType = "multipart/form-data";
//        File file = new File(imagePath);  //imgUrl为图片位置
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", "head_image", fileBody)
//                .addFormDataPart("imagetype", imageType)
//                .build();
//        Request request = new Request.Builder()
//                .url(requestUrl)
//                .post(requestBody)
//                .build();
//        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
//        OkHttpClient okHttpClient = httpBuilder
//                .build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                TcpService.isUpdateMirrorImage = false;
//                MyLog.update("====update failed =" + e.toString());
//                MyLog.update("=====开始时间=" + System.currentTimeMillis());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String htmlStr = response.body().string();
//                TcpService.isUpdateMirrorImage = false;
//                MyLog.update("====update success =" + htmlStr);
//                MyLog.update("=====开始时间=" + System.currentTimeMillis());
//            }
//        });
//    }

}
