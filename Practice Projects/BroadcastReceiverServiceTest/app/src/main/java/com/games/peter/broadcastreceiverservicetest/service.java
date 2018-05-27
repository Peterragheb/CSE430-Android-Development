package com.games.peter.broadcastreceiverservicetest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class service extends Service{
    MediaPlayer player;
    int audio_id = R.raw.bleep;
    BroadcastReceiver tickReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();
        tickReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    Intent intent1= new Intent(service.this,SoundService.class);
                    intent1.putExtra("audio_id",R.raw.bleep);
                    startService(intent1);

                    Toast.makeText(context, "1 min has passed ", Toast.LENGTH_SHORT).show();
                    Log.v("Karl", "tick tock tick tock...");
//                    player = MediaPlayer.create(service.this, audio_id); //select music file
//                    player.setLooping(false); //set looping
//                    player.start();
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(tickReceiver, filter); // register the broadcast receiver to receive TIME_TICK
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }
    public void onDestroy() {
        unregisterReceiver(tickReceiver);
        stopSelf();
        super.onDestroy();
    }
}
