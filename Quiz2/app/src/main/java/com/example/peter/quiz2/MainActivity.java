package com.example.peter.quiz2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {
    private static final int SHAKE_THRESHOLD = 800;
    private TextView tv_pattern;
    SensorManager sensorMgr;
    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_pattern = findViewById(R.id.tv_pattern);
//        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        final long time=Calendar.getInstance().getTime().getTime();
        long mintime=500;
        final long[] last_shake = new long[1];
        last_shake[0] = time;
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
        int count =0;
            public void onShake() {
                long time_happen= Calendar.getInstance().getTime().getTime();

                if (time_happen-time<10000){
                    if (time_happen-last_shake[0]>1000){
                        count++;
                        Log.v("DIFF_timehap", time_happen-last_shake[0]+"");
                        //Toast.makeText(MainActivity.this, "Shake detected!", Toast.LENGTH_SHORT).show();
                        last_shake[0] = time_happen;
                        Log.v("LASTSHAKE",last_shake[0]+"");
                        tv_pattern.setText(count+"");
                        if (count==6){
                            startActivity(new Intent(MainActivity.this,Main2Activity.class));
                        }
                    }
                }
            }
       });


    }

//    public void onSensorChanged(int sensor, float[] values) {
//        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
//            long curTime = System.currentTimeMillis();
//            // only allow one update every 100ms.
//            if ((curTime - lastUpdate) > 100) {
//                long diffTime = (curTime - lastUpdate);
//                lastUpdate = curTime;
//
//                x = values[SensorManager.DATA_X];
//                y = values[SensorManager.DATA_Y];
//                z = values[SensorManager.DATA_Z];
//
//                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;
//
//                if (speed > SHAKE_THRESHOLD) {
//                    Log.d("sensor", "shake detected w/ speed: " + speed);
//                    Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
//                }
//                last_x = x;
//                last_y = y;
//                last_z = z;
//            }
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(int i, int i1) {
//
//    }
@Override
protected void onResume() {
    super.onResume();
    mSensorManager.registerListener(mSensorListener,
            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI);
}
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
