package com.xxzz.curriculum.index;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundMusic extends Service {
    // TODO: add local music
    private static MediaPlayer mediaPlayer = null;

    public void play(Context context, int resource) {
        stop(context);
        mediaPlayer = MediaPlayer.create(context, resource);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stop(Context context) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        //stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public class PlayBinder extends Binder {

    }
}
