package com.example.peter.lab5_pedometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.lab5_pedometer.Classes.DBAdapter;
import com.example.peter.lab5_pedometer.Classes.Record;
import com.example.peter.lab5_pedometer.Classes.SharedVar;
import com.example.peter.lab5_pedometer.Classes.StepDetector;
import com.example.peter.lab5_pedometer.Classes.StepListener;
import com.example.peter.lab5_pedometer.Classes.UiUpdateTrigger;

import java.util.Calendar;

/**
 * Created by peter on 3/30/18.
 */
public class tab1_fragment extends Fragment implements SensorEventListener, StepListener, View.OnClickListener, Chronometer.OnChronometerTickListener ,SharedVar.ChangeListener{
    private TextView tv_step_counter,tv_burnt_calories_count,tv_distance_count,tv_speed_count;
    Chronometer cm_time_count;
    private View v_between_pause_stop;
    private Button btn_start_counting;
    private ImageButton ibtn_pause_counting,ibtn_stop_counting;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;
    long elapsedTime;
    float distance,calories, TotalDistanceLastMin, speed;
    public static int step_length;
    int numberof_ticks=0;
    public static float bodyMassKg;
    public static boolean STATE_STARTED=false,PAUSED=false;
    public static UiUpdateTrigger uiUpdateTrigger=new UiUpdateTrigger();
    DBAdapter dbAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab1_fragment,container,false);
        initializeUIfields(view);
        openDB();
        resetFields();
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        assignListeners();
        return view;
    }
    private void initializeUIfields(View view){
        tv_step_counter=view.findViewById(R.id.tv_step_counter);
        tv_burnt_calories_count=view.findViewById(R.id.tv_burnt_calories_count);
        cm_time_count=view.findViewById(R.id.cm_time_count);
        tv_distance_count=view.findViewById(R.id.tv_distance_count);
        tv_speed_count=view.findViewById(R.id.tv_speed_count);
        btn_start_counting=view.findViewById(R.id.btn_start_counting);
        ibtn_pause_counting=view.findViewById(R.id.ibtn_pause_counting);
        ibtn_stop_counting=view.findViewById(R.id.ibtn_stop_counting);
        v_between_pause_stop=view.findViewById(R.id.v_between_pause_stop);
    }
    private void resetFields(){
        tv_step_counter.setText("0");
        tv_burnt_calories_count.setText("0");
        tv_distance_count.setText("0.00");
        tv_speed_count.setText("0.00");
        distance=0;
        numSteps = 0;
        calories = 0;
        speed=0;
        TotalDistanceLastMin=0;
        step_length=MainActivity.sharedVar.getStep_length();
        bodyMassKg=MainActivity.sharedVar.getBody_weight();
    }
    private void assignListeners(){
        simpleStepDetector.registerListener(this);
        btn_start_counting.setOnClickListener(this);
        ibtn_pause_counting.setOnClickListener(this);
        ibtn_stop_counting.setOnClickListener(this);
        cm_time_count.setOnChronometerTickListener(this);
        MainActivity.sharedVar.addListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        tv_step_counter.setText(numSteps+"");
        distance+=step_length/1000.0;
        tv_distance_count.setText(String.format("%.2f", distance));
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_start_counting){
            Start();
        }
        else if (v.getId()==R.id.ibtn_pause_counting){
           Pause();
        }
        else if (v.getId()==R.id.ibtn_stop_counting){
            Stop(false);
        }
    }
    public void Start(){
        STATE_STARTED=true;

        if (!PAUSED) {
            cm_time_count.setBase(SystemClock.elapsedRealtime());
            cm_time_count.start();
        }else {
                cm_time_count.setBase(cm_time_count.getBase()+SystemClock.elapsedRealtime()-elapsedTime);
                cm_time_count.start();
                PAUSED=false;
        }
        btn_start_counting.setVisibility(View.GONE);
        ibtn_pause_counting.setVisibility(View.VISIBLE);
        ibtn_stop_counting.setVisibility(View.VISIBLE);
        v_between_pause_stop.setVisibility(View.VISIBLE);
        sensorManager.registerListener(tab1_fragment.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }
    public void Pause(){
        PAUSED=true;
        elapsedTime=SystemClock.elapsedRealtime();
        cm_time_count.stop();
        btn_start_counting.setVisibility(View.VISIBLE);
        ibtn_pause_counting.setVisibility(View.GONE);
        ibtn_stop_counting.setVisibility(View.GONE);
        v_between_pause_stop.setVisibility(View.GONE);
        sensorManager.unregisterListener(tab1_fragment.this);
    }
    public void Stop(boolean cancel){
        cm_time_count.stop();
        PAUSED=false;
        STATE_STARTED=false;
        elapsedTime=0L;
        btn_start_counting.setVisibility(View.VISIBLE);
        ibtn_pause_counting.setVisibility(View.GONE);
        ibtn_stop_counting.setVisibility(View.GONE);
        v_between_pause_stop.setVisibility(View.GONE);
        sensorManager.unregisterListener(tab1_fragment.this);
        if (!cancel){
            Record record;
            if (Integer.valueOf(tv_step_counter.getText().toString())!=0){
                record=new Record(Calendar.getInstance().getTime(),
                        Integer.valueOf(tv_step_counter.getText().toString()),
                        Float.valueOf(tv_burnt_calories_count.getText().toString()),
                        Float.valueOf(tv_distance_count.getText().toString()),
                        Float.valueOf(tv_speed_count.getText().toString()));
                Log.v("RECORD INFO:","DATE : "+record.parseDate(null)+" STEPS: "+record.getSteps()+" CALORIES: "+record.getCalories()+" DISTANCE: "+record.getDistance()+" SPEED: "+record.getSpeed());
                dbAdapter.insertRow(record.parseDate(null),record.getSteps(),record.getCalories(),record.getDistance(),record.getSpeed());
                Toast.makeText(getContext(),"Run added to standing",Toast.LENGTH_SHORT).show();
                uiUpdateTrigger.setUpdated(true);
            }
        }
        numSteps=0;
        calories=0;
        numberof_ticks=0;
        TotalDistanceLastMin=0;
        distance=0;
        speed=0;
        tv_step_counter.setText(numSteps+"");
        tv_burnt_calories_count.setText(calories+"");
        tv_distance_count.setText(distance+"");
        tv_speed_count.setText(speed+"");
        cm_time_count.setText("00:00");
    }

    private void openDB(){
        dbAdapter=new DBAdapter(this.getContext());
        dbAdapter.open();
    }
    private void closeDB(){
        if (dbAdapter!=null)
            dbAdapter.close();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {

        numberof_ticks++;
        Log.v("TICKS",numberof_ticks+"");
        if (numberof_ticks==60){
            numberof_ticks=0;
            float metersWalkedInAMin =distance-TotalDistanceLastMin;
            calories+= 0.0005 * bodyMassKg * metersWalkedInAMin + 0.0035;
            speed=metersWalkedInAMin*60;
            //TotalDistanceLastMin=metersWalkedInAMin;
            TotalDistanceLastMin=distance;
            Log.v("CALORIES",calories+"");
            tv_burnt_calories_count.setText(String.format("%.2f",calories));
            tv_speed_count.setText(String.format("%.2f",speed));
        }
    }

    @Override
    public void onChange() {
        Stop(true);
        step_length= MainActivity.sharedVar.getStep_length();
        bodyMassKg=MainActivity.sharedVar.getBody_weight();
    }
}
