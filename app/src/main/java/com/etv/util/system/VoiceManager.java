package com.etv.util.system;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.etv.util.MyLog;
import com.etv.view.MyToastView;

/**
 * 音量控制管理器
 */
public class VoiceManager {

    private static final String TAG = "VoiceManager";


    public static int getCurrentVoiceNum(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        int mediacurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return mediacurrent;
    }

    public static void dealMediaVoice(Context context, String progress) {
        if (progress == null || progress.length() < 1) {
            return;
        }
        MyLog.cdl("传过来的音量==" + progress);
        try {
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
            int mediamax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int progressInt = Integer.parseInt(progress);
            int progressSet = (progressInt * mediamax / 100);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progressSet, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 处理媒体音量
     * @param b
     * true add
     * false request
     */
    public static void dealMediaVoice(Context context, boolean b) {
        try {
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
            int mediamax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int mediacurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.i(TAG, "===处理前媒体音量====" + mediacurrent + "  /" + mediamax);
            if (b) {
                if (mediacurrent == mediamax) {
                    return;
                }
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediacurrent + 1, 0);
                int currentVoice = mediacurrent + 1;

                MyToastView.getInstance().Toast(context, "增加音量: " + currentVoice * 100 / 15 + "% ");
            } else {
                if (mediacurrent < 1) {
                    return;
                }
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediacurrent - 1, 0);
                int currentVoice = mediacurrent - 1;
                MyToastView.getInstance().Toast(context, "降低音量: " + currentVoice * 100 / 15 + "% ");
            }
        } catch (Exception e) {
        }
    }

    /**
     * 按照比例来修改音量
     *
     * @param context
     * @param progress
     */
    public static void repairDevVoice(Context context, String progress) {
        try {
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
            int progressNum = Integer.parseInt(progress);
            if (progressNum == 0) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                return;
            }
            int voiceNum = (progressNum * 15) / 100;
            MyLog.message("====设置的音量===" + voiceNum);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voiceNum, 0);
        } catch (Exception e) {
            MyLog.message("====设置的音量error===" + e.toString());
            e.printStackTrace();
        }
    }

    /***
     * 静音
     */
    public static void addMediaMaxVoice(Context context) {
        try {
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 15);  //设置媒体音量为 0
        } catch (Exception e) {
        }
    }

    /***
     * 静音
     */
    public static void stopMediaVoice(Context context) {
        try {
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);  //设置媒体音量为 0
        } catch (Exception e) {
        }
    }


}