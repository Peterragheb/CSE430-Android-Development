package com.games.peter.lab4_voice_calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventCreatorActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_cancel,btn_done;
    EditText et_event_name;
    TextView tv_event_picked_date,tv_event_picked_time ;
    Calendar calendar;
    String event_name;
    int day,year,month, hour,minute;
    CalendarProvider calendarProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);
        //=============================================================
        link_fields();
        //=============================================================
        init_fields();
        //=============================================================
        btn_cancel.setOnClickListener(this);
        tv_event_picked_date.setOnClickListener(this);
        tv_event_picked_time.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        //=============================================================
        calendarProvider=new CalendarProvider(this.getBaseContext());
        //=============================================================
    }
    private void link_fields(){
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_done=findViewById(R.id.btn_done);
        et_event_name=findViewById(R.id.et_event_name);
        tv_event_picked_date=findViewById(R.id.tv_event_picked_date);
        tv_event_picked_time=findViewById(R.id.tv_event_picked_time);
    }
    private void init_fields(){//get values for first creation
        calendar=Calendar.getInstance();
        event_name=getIntent().getStringExtra(MainActivity.MESSAGE_EVENT_NAME);
        day=getIntent().getIntExtra(MainActivity.MESSAGE_EVENT_DATE_DAY,calendar.get(Calendar.DAY_OF_MONTH));
        month=getIntent().getIntExtra(MainActivity.MESSAGE_EVENT_DATE_MONTH,calendar.get(Calendar.MONTH));
        year=getIntent().getIntExtra(MainActivity.MESSAGE_EVENT_DATE_YEAR,calendar.get(Calendar.YEAR));
        hour=getIntent().getIntExtra(MainActivity.MESSAGE_EVENT_TIME_HOUR,calendar.get(Calendar.HOUR_OF_DAY));
        minute=getIntent().getIntExtra(MainActivity.MESSAGE_EVENT_TIME_MINUTE,calendar.get(Calendar.MINUTE));
        populate_fields(day,month,year,hour,minute,event_name);
    }
    private void populate_fields(int day,int month,int year,int hour,int minute,String event_name){//populate fields
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        //=============================================================
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm aaa");
        simpleDateFormat.setCalendar(calendar);
        String date = simpleDateFormat.format(calendar.getTime());
        String time = sdf.format(calendar.getTime());
        //=============================================================
        this.day=calendar.get(Calendar.DAY_OF_MONTH);
        this.month=calendar.get(Calendar.MONTH);
        this.year=calendar.get(Calendar.YEAR);
        this.hour=calendar.get(Calendar.HOUR_OF_DAY);
        this.minute=calendar.get(Calendar.MINUTE);
        //=============================================================
        this.event_name=event_name;
        et_event_name.setText(event_name);
        tv_event_picked_date.setText(date);
        tv_event_picked_time.setText(time);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //=============================================================
            case R.id.btn_cancel://cancel event creation
                finish();
                break;
            //=============================================================
            case R.id.tv_event_picked_date://pick date from dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(EventCreatorActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                            calendar.set(Calendar.MONTH,month);
                            calendar.set(Calendar.YEAR,year);
                            EventCreatorActivity.this.year=calendar.get(Calendar.YEAR);
                            EventCreatorActivity.this.month=calendar.get(Calendar.MONTH);
                            day=calendar.get(Calendar.DAY_OF_MONTH);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                            String date = simpleDateFormat.format(calendar.getTime());
                            tv_event_picked_date.setText(date);
                        }
                    },year,month,day);
                    datePickerDialog.show();
                break;
            //=============================================================
            case R.id.tv_event_picked_time://pick time from dialog
                TimePickerDialog timePickerDialog=new TimePickerDialog(EventCreatorActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        hour=calendar.get(Calendar.HOUR_OF_DAY);
                        EventCreatorActivity.this.minute=calendar.get(Calendar.MINUTE);
                        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm aaa");
                        String time = sdf.format(calendar.getTime());
                        tv_event_picked_time.setText(time);
                    }
                },hour,minute,false);
                timePickerDialog.show();
                break;
            //=============================================================
            case R.id.btn_done://create event
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);
                hour=calendar.get(Calendar.HOUR_OF_DAY);
                minute=calendar.get(Calendar.MINUTE);
                if (et_event_name.getText().toString().isEmpty())
                {
                    Toast.makeText(EventCreatorActivity.this,"Please enter an event title",Toast.LENGTH_SHORT).show();
                    return;
                }
                event_name=et_event_name.getText().toString();
                calendarProvider.CreateEvent(year,month,day,hour,minute,event_name);
                Intent intent =new Intent(EventCreatorActivity.this,UpcomingEventsActivity.class);
                startActivity(intent);
                finish();
                break;
            //=============================================================
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        event_name=et_event_name.getText().toString();
        outState.putInt("day",day);
        outState.putInt("month",month);
        outState.putInt("year",year);
        outState.putInt("hour",hour);
        outState.putInt("minute",minute);
        outState.putString("event_name",event_name);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        day=savedInstanceState.getInt("day");
        month=savedInstanceState.getInt("month");
        year=savedInstanceState.getInt("year");
        hour=savedInstanceState.getInt("hour");
        minute=savedInstanceState.getInt("minute");
        event_name=savedInstanceState.getString("event_name");
        populate_fields(day,month,year,hour,minute,event_name);
    }
}
