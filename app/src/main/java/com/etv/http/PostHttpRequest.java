package com.etv.http;

import com.etv.config.AppInfo;
import com.etv.util.MyLog;
import com.etv.util.SharedPerManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/***
 * post请求模板
 */
public class PostHttpRequest {

    OkHttpClient mOkHttpClient;
    RequestBody requestBodyPost;
    String url;

    public PostHttpRequest(String url, RequestBody requestBodyPost) {
        MyLog.http("==请求的URL==" + url);
        this.url = url;
        this.requestBodyPost = requestBodyPost;
    }

    public void querybindInfo(final RequeatListener listener) {
        try {
            if (mOkHttpClient == null) {
                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build();
            }
            Request requestPost = new Request.Builder().url(url)
                    .post(requestBodyPost).build();
            mOkHttpClient.newCall(requestPost).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.requestFailed(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    String htmlStr = response.body().string();
                    listener.requestSuccess(htmlStr);
                }
            });
        } catch (Exception e) {
            listener.requestFailed(e.toString());
        }
    }

}
