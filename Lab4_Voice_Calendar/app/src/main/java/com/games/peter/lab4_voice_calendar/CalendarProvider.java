package com.games.peter.lab4_voice_calendar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Peter on 24/3/2018.
 */

public class CalendarProvider {
    private static final String DEBUG_TAG = "MyActivity";
    private Context context;
    private String MY_ACCOUNT_NAME = "Voice_Calendar_Account";

    //=============================================================
    CalendarProvider(Context context) {
        this.context = context;
        createCalendar();
    }
    //=============================================================
    public long CreateEvent(int year,int month,int day,int hour, int minute ,String name ) {//create new event with sent data
        long calID = getCalendarId();
        long startMillis = 0;
        long endMillis = 0;
        //=============================================================
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, hour, minute);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day, hour+1, minute);
        endMillis = endTime.getTimeInMillis();
        //=============================================================
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, name);
        values.put(CalendarContract.Events.DESCRIPTION, name);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, beginTime.getTimeZone().getID());
        //=============================================================
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return -1;
        }
        //=============================================================
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        //=============================================================
        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        Log.v("Event Created ","ID:"+String.valueOf(eventID)+" Title :"+ name+" hour "+ hour+" minute "+minute);
        return eventID;
        // ... do something with event ID
        //=============================================================
    }

    public void DeleteEvent(long eventID) {//delete event by id
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = cr.delete(deleteUri, null, null);
        //=============================================================
        Log.i(DEBUG_TAG, "Rows deleted: " + rows);
        //=============================================================
    }

    public void deleteAllEvents() {//delete all events
        ArrayList<Event> events = GetAllEvents();
        for (int i = 0; i < events.size(); i++) {
            long eventId = events.get(i).getId();
            DeleteEvent(eventId);
        }
    }

    public ArrayList<Event> GetAllEvents(){//return all events in calendar
        Cursor cursor;
        ArrayList<Event> events = new ArrayList<Event>();
        //=============================================================
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return events;
        }
        //=============================================================
        cursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, CalendarContract.Events.CALENDAR_ID + " = ?",new String[]{String.valueOf(getCalendarId())}, null);
        //=============================================================
        if (cursor.moveToFirst()) {
            do {
                int deletedIDX = cursor.getColumnIndex(CalendarContract.Events.DELETED);
                int idIDX = cursor.getColumnIndex(CalendarContract.Events._ID);
                int titleIDX = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                int startIDX = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                int culidIDX = cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID);
                //=============================================================
                long id = cursor.getLong(idIDX);
                String title =  cursor.getString(titleIDX);
                int deleted = cursor.getInt(deletedIDX);
                String start = cursor.getString(startIDX);
                int culid= cursor.getInt(culidIDX);
                SimpleDateFormat sdf=new SimpleDateFormat("hh:mm aaa");
                String details = "";
                String time = "";
                //=============================================================
                Log.e("Calendar ID ", String.valueOf(culid));
                //=============================================================
                try {
                    long s = Long.parseLong(start);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(s);
                    details = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+ " "+
                            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+ " "+
                            calendar.get(Calendar.DAY_OF_MONTH)+" "+
                            calendar.get(Calendar.YEAR);

                    time = sdf.format(calendar.getTime());
                    //time =calendar.get(Calendar.HOUR_OF_DAY) +":"+calendar.get(Calendar.MINUTE);
                }catch (Exception e){}
                //=============================================================
                if (deleted != 1 )
                {
                    Event e = new Event(id, title, details, time);
                    events.add(e);
                    Log.d("Cursor", "Title: " + title + "\tDescription: " + details );
                }
                //=============================================================
            }while (cursor.moveToNext());
        }
        //=============================================================
        else
            Log.d("Cursor empty", "True" );
        //=============================================================
        cursor.close();
        return events;
    }

    public void createCalendar() {//create calendar for the app
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, MY_ACCOUNT_NAME);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, "Peter Calendar");
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Peter Calendar");
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xffff0000);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, "some.account@googlemail.com");
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, Calendar.getInstance().getTimeZone().getID());
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        //=============================================================
        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "com.peter");
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");
        //=============================================================
        Uri uri = this.context.getContentResolver().insert(builder.build(), values);
    }

    private long getCalendarId() {//return the calendar id to insert in it
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection = CalendarContract.Calendars.ACCOUNT_NAME + " = ? AND " + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? ";
        // use the same values as above:
        String[] selArgs = new String[]{MY_ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL};
        //=============================================================
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return -1;
        }
        //=============================================================
        Cursor cursor = this.context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection, selection, selArgs, null);
        //=============================================================
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        //=============================================================
        cursor.close();
        return -1;
    }


}
