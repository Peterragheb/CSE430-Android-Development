package com.games.peter.lab4_voice_calendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpcomingEventsActivity extends AppCompatActivity {
    ListView listView;
    CalendarProvider calendarProvider;
    long mLastClickTime=0;
    int previous_item=-1;
    ArrayList<Event> events=new ArrayList<>();
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        //=============================================================
        listView = findViewById(R.id.lv_event_list);
        calendarProvider = new CalendarProvider(this);
        //=============================================================
        events = calendarProvider.GetAllEvents();
        //=============================================================
        if (events.isEmpty())
            MainActivity.requestPermission(this,this);
        //=============================================================
        customAdapter = new CustomAdapter(this, R.layout.event_entry, events);
        listView.setAdapter(customAdapter);
        //=============================================================
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long currTime = System.currentTimeMillis();
                if (currTime - mLastClickTime < ViewConfiguration.getDoubleTapTimeout()&&previous_item==position) {
                    onItemDoubleClick(parent, view, position, id);
                }
                previous_item=position;
                mLastClickTime = currTime;
            }
        });
    }

    public void onItemDoubleClick(AdapterView<?> adapterView, View view, int position, long l) {//if double clicked delete event
        Toast.makeText(UpcomingEventsActivity.this,"Deleted Event "+ events.get(position).getName(),Toast.LENGTH_SHORT).show();
        calendarProvider.DeleteEvent(events.get(position).getId());
        events = calendarProvider.GetAllEvents();
        customAdapter.clear();
        customAdapter.addAll(events);
        customAdapter.notifyDataSetChanged();
    }

}
