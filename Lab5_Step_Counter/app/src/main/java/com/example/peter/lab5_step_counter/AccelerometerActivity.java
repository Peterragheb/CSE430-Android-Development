package com.example.peter.lab5_step_counter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity {
        TextView tv_x,tv_y,tv_z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        tv_x=findViewById(R.id.tv_x);
        tv_y=findViewById(R.id.tv_y);
        tv_z=findViewById(R.id.tv_z);
        SensorManager sensorManager =
                (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener listener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                tv_x.setText(event.values[0]+"");
                tv_y.setText(event.values[1]+"");
                tv_z.setText(event.values[2]+"");
            }
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(listener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}
