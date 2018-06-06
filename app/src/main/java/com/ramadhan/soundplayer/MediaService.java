package com.ramadhan.soundplayer;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class MediaService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_PACKAGE = "ACTION_PACKAGE";
    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_STOP = "STOP";
    public static final String ACTION_CREATE = "CREATE";
    MediaPlayer mMediaPlayer = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // ketika nilai dari action ACTION_CREATE maka method ini akan dijalankan
    public void init() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetFileDescriptor afd =
                getApplicationContext()
                        .getResources()
                        .openRawResourceFd(R.raw.tesz);

        try {
            mMediaPlayer.setDataSource(
                    afd.getFileDescriptor()
                    , afd.getStartOffset()
                    , afd.getLength());

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(this);

    }

    // ketika method start service pada MainActivity dijalankan
    // maka method ini akan di panggil
    public int onStartCommand(Intent i, int flags, int startId) {
        String action = i.getAction();

        switch (action) {
            case ACTION_CREATE:
                init();
                break;

                // proses load audio
            // secara asynchronous
            // supaya aplikasi tetap responsif
            case ACTION_PLAY:
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.prepareAsync();
                }
                break;

            case ACTION_STOP:
                mMediaPlayer.stop();
                break;

            default:
                break;

        }
        return flags;

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    // buang memori media player yang sudah tidak digunakan lagi
    @Override
    public void onDestroy() {
        if (mMediaPlayer != null)
            mMediaPlayer.release();
    }
}
