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
import okhttp3.Response;

/**
 * Created by cdl
 */

public class MyOkHttpUtil {

    String requestUrl;
    OkHttpClient mOkHttpClient;
    int requestMaxNum;//当前请求的最大次数
    int requestNum = 0;  //当前请求的次数
    RequeatListener requeatListener;


    public MyOkHttpUtil(String requestUrl, int requestMaxNum) {
        this.requestUrl = requestUrl;
        this.requestMaxNum = requestMaxNum;
    }

    public void getRequestInfo(RequeatListener listener) {
        this.requeatListener = listener;
        try {
            if (mOkHttpClient == null) {
                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build();
            }
            final Request request = new Request.Builder().url(requestUrl).build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    if (requestNum > requestMaxNum) {
                        requestNum = 0;
                        requeatListener.requestFailed(arg1.toString());
                    } else {
                        getRequestInfo(requeatListener);
                    }
                    requestNum++;
                }

                @Override
                public void onResponse(Call arg0, Response response)
                        throws IOException {
                    String json = response.body().string();
                    requeatListener.requestSuccess(json);
                }
            });
        } catch (Exception e) {
            listener.requestFailed(e.toString());
        }
    }


}
