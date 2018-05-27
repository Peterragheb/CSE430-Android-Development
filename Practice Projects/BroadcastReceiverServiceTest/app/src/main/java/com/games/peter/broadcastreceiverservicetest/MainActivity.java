package com.games.peter.broadcastreceiverservicetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this line makes the broadcastreceiver
//        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Toast.makeText(context, "I got it "+ intent.getIntExtra("MyValue",0), Toast.LENGTH_SHORT).show();
//            }
//        };
//        //this line register broadcastreceiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("test"));
//
//        //this line calls the broadcastreceiver
//        LocalBroadcastManager.getInstance(this).sendBroadcast(new       Intent("test").putExtra("MyValue",5));
//        BroadcastReceiver tickReceiver = new BroadcastReceiver(){
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
//                    Toast.makeText(context, "1 min has passed ", Toast.LENGTH_SHORT).show();
//                    Log.v("Karl", "tick tock tick tock...");
//                //}
//            }
//        };
//        IntentFilter mTime = new IntentFilter(Intent.ACTION_TIME_TICK);
//        registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK)); // register the broadcast receiver to receive TIME_TICK
        Intent intent = new Intent(MainActivity.this,service.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
