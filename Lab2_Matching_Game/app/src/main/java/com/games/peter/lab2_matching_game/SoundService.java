package com.games.peter.lab2_matching_game;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class SoundService extends Service {
    //=====================================

    MediaPlayer player;
    int audio_id;

    //=====================================

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    //=====================================


    public void onCreate() {

    }
    //=====================================

    public int onStartCommand(Intent intent, int flags, int startId) {
        audio_id= intent.getIntExtra("audio_id", -1);
        player = MediaPlayer.create(SoundService.this, audio_id); //select music file
        player.setLooping(false); //set looping
        player.start();
        return Service.START_NOT_STICKY;
    }
    //=====================================


    public void onDestroy() {
        player.stop();
        player.reset();
        player.release();
        stopSelf();
        super.onDestroy();
    }
    //=====================================

}