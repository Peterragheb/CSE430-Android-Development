package com.example.peter.lab5_step_counter;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lv_sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_sensors=findViewById(R.id.lv_list_of_sensors);
        SensorManager sensorMan = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorMan.getSensorList(Sensor.TYPE_ALL);
        ArrayList<String> list_of_sensors_names=new ArrayList<>();
        for (int i=0;i<sensors.size();i++){
            list_of_sensors_names.add(sensors.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_sensors_names);
        lv_sensors.setAdapter(arrayAdapter);


        lv_sensors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    startActivity(new Intent(MainActivity.this,AccelerometerActivity.class));
                }
            }
        });
    }
}
