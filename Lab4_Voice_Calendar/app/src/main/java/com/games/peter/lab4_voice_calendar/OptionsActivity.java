package com.games.peter.lab4_voice_calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {
    ListView listView;
    CalendarProvider calendarProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        //=============================================================
        listView=findViewById(R.id.lv_options);
        //=============================================================
        if (MainActivity.got_permission(OptionsActivity.this))
        {
            calendarProvider=new CalendarProvider(OptionsActivity.this.getApplicationContext());
        }
        //=============================================================
        String[] lv_items={"Delete All Events"};
        //=============================================================
        ArrayAdapter arrayAdapter=new ArrayAdapter(OptionsActivity.this,android.R.layout.simple_list_item_1,lv_items);
        //=============================================================
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//delete all events selected
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    if (MainActivity.GOT_PERMISSION)
                        showalert(OptionsActivity.this,OptionsActivity.this);
                    else
                        showalert2(OptionsActivity.this,OptionsActivity.this);
                }
            }
        });
        //=============================================================
    }

    public void showalert(final Context context , final Activity activity){//confirmation dialog to delete all events
        AlertDialog.Builder builder;
        //=============================================================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        //=============================================================
        builder.setTitle("Delete All")
                .setMessage("Are you sure you want to delete all scheduled events ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        calendarProvider.deleteAllEvents();
                        Toast.makeText(OptionsActivity.this,"All events have been deleted",Toast.LENGTH_SHORT).show();
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
    public void showalert2(final Context context , final Activity activity){
        AlertDialog.Builder builder;
        //=============================================================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//show alert that the app doesnt have permission to access calendar
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        //=============================================================
        builder.setTitle("Permission Needed")
                .setMessage("To use this feature , you have to give the application permission to access the calendar. \nOpen permission dialog?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.requestPermission(context,activity);
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}
