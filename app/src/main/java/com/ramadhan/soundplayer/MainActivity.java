package com.ramadhan.soundplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSound, btnMedia, btnStop;
    SoundPool sp;
    boolean spLoaded = false;
    int wav;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSound = findViewById(R.id.btn_play);
        btnMedia = findViewById(R.id.btn_playyy);
        btnStop = findViewById(R.id.btn_stop);
        btnSound.setOnClickListener(this);
        btnMedia.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder()
                    // jumlah maksimal streams yang didukung oleh soundpool
                    .setMaxStreams(10)
                    .build();

        } else {
            sp = new SoundPool(10,
                    AudioManager.STREAM_MUSIC,
                    1);

        }

        // soundpool hanya bisa memainkan berkas yang telah dimuat sempurna
        // maka nya perlu method ini untuk memastikan bahwa proses pemuatan telah selesai
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                spLoaded = true;

            }
        });
        wav = sp.load(this,
                R.raw.tesz,
                1);

        intent = new Intent(this, MediaService.class);
        intent.setAction(MediaService.ACTION_CREATE);
        intent.setPackage(MediaService.ACTION_PACKAGE);
        startService(intent);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_play:

                // memberikan handle ketika action button ditekan
                // apakah laod file sound sudah selesai atau belum
                // jika sudah baru dapat diputar
                if (spLoaded) {
                    sp.play(wav, 1, 1,
                            0, 0, 1);
                }
                break;

            case R.id.btn_playyy:
                intent.setAction(MediaService.ACTION_PLAY);
                intent.setPackage(MediaService.ACTION_PACKAGE);
                startService(intent);
                break;

            case R.id.btn_stop:
                intent.setAction(MediaService.ACTION_STOP);
                intent.setPackage(MediaService.ACTION_PACKAGE);
                startService(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}
