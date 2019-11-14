package com.etv.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.ys.etv.R;

public class PlayUtil implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    Context context;
    PlayMusicListener listener;
    MediaPlayer mp;

    public PlayUtil(Context paramContext) {
        this.context = paramContext;
        MediaPlayer localMediaPlayer = new MediaPlayer();
        this.mp = localMediaPlayer;
    }

    public void onCompletion(MediaPlayer paramMediaPlayer) {
        int i = Log.e("music", "=====播放完毕====");
        if (this.listener == null)
            return;
        this.listener.playCompletOver();
    }

    public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2) {
        String str = "=====播放异常====" + paramInt1 + " / " + paramInt2;
        int i = Log.e("music", str);
        return false;
    }

    public void playMusic() {
        try {
            this.mp.reset();
            MediaPlayer localMediaPlayer = MediaPlayer.create(this.context, R.raw.notify_noice);
            this.mp = localMediaPlayer;
            this.mp.start();
            this.mp.setOnCompletionListener(this);
            this.mp.setOnErrorListener(this);
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public void setOnPlayMusicListener(PlayMusicListener paramPlayMusicListener) {
        this.listener = paramPlayMusicListener;
    }

    public void stopMusic() {
        if (this.mp == null)
            return;
        if (!this.mp.isPlaying())
            return;
        this.mp.stop();
    }

    public static abstract interface PlayMusicListener {
        public abstract void playCompletOver();
    }
}