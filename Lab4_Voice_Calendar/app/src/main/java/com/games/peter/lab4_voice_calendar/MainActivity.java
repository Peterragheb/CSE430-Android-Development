package com.games.peter.lab4_voice_calendar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_display_events, btn_options, btn_help, btn_exit;
    ImageView iv_create_event;
    CalendarProvider calendarProvider;
    static boolean GOT_PERMISSION = false;
    static boolean help_clicked=false;
    private final int VOICE_RECOGNITION = 1;
    final public static String MESSAGE_EVENT_NAME = "event_name";
    final public static String MESSAGE_EVENT_DATE_YEAR = "event_year";
    final public static String MESSAGE_EVENT_DATE_MONTH = "event_month";
    final public static String MESSAGE_EVENT_DATE_DAY = "event_day";
    final public static String MESSAGE_EVENT_DATE_DAY_NAME = "event_day_name";
    final public static String MESSAGE_EVENT_TIME_HOUR = "event_hour";
    final public static String MESSAGE_EVENT_TIME_MINUTE = "event_minute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //=============================================================
        if (got_permission(MainActivity.this))
            GOT_PERMISSION=true;
        //=============================================================
        btn_display_events = findViewById(R.id.btn_display_events);
        btn_options = findViewById(R.id.btn_options);
        btn_help = findViewById(R.id.btn_help);
        btn_exit = findViewById(R.id.btn_exit);
        iv_create_event = findViewById(R.id.iv_create_event);
        //=============================================================
        btn_display_events.setOnClickListener(this);
        iv_create_event.setOnClickListener(this);
        btn_options.setOnClickListener(this);
        btn_help.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        //=============================================================
    }
    public static boolean got_permission(Context context){//check if user granted permission to access calendar
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED))
            return false;
        //=============================================================
        return true;
    }

    public static void requestPermission(Context context, Activity activity) {//request calendar permission
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, 1);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            //=============================================================
            case 1: {//code of calendar permission
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GOT_PERMISSION = true;
                    calendarProvider = new CalendarProvider(this.getApplicationContext());
                } else {
                    GOT_PERMISSION = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            //=============================================================
        }
    }

    public void showalert(final Context context, final Activity activity) {//show alert that the app doesnt have permission to access calendar
        AlertDialog.Builder builder;
        //=============================================================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        //=============================================================
        builder.setTitle("Permission Needed")
                .setMessage("To use this feature , you have to give the application permission to access the calendar. \nOpen permission dialog?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission(context, activity);
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }


    private void startVoiceRecognition() {//start voice recognition intent
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak!");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, VOICE_RECOGNITION);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String result = results.get(0);//get the result string
                String DayofWeek = checkContainsDay_getIt(result);//first get the day of week
                //=============================================================
                if (!DayofWeek.isEmpty()) {//check if the returned dayofweek is empty
                    String pattern = "(Sunday | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday)";
                    String[] split_result = result.split(pattern);//split on the dayofweek
                    //=============================================================
                    String event_name = split_result[0];//first string is the event name
                    if (event_name.isEmpty()) {//if event name is empty display toast
                        Toast.makeText(MainActivity.this, "Please try again ,specify an event title", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //=============================================================
                    if (split_result.length < 2) {//if no more strings in array
                        Toast.makeText(MainActivity.this, "Please try again , and make sure you are using the right format", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //=============================================================
                    int day_number = get_day_number(split_result[1]);//get the day number
                    if (day_number == -1)//if no day number returned return
                        return;
                    //=============================================================
                    String month = get_month(split_result[1]);
                    if (month.isEmpty())//if no month name returned return
                        return;
                    //=============================================================
                    int year = get_year(split_result[1]);//if no year returned return
                    if (year == -1)
                        return;
                    //=============================================================
                    int index_of_year = split_result[1].indexOf(year + "");
                    String after_year = split_result[1].substring(index_of_year + 4);//string remaining of the result string after trimming till year
                    if (after_year.length() < 10) {//if remaining string isnt long enough to contain hour , minutes and format return
                        Toast.makeText(MainActivity.this, "Please try again ,specify an hour", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //=============================================================
                    int hour = get_hour(after_year);
                    if (hour == -1)//if no hour returned return
                        return;
                    //=============================================================
                    int index_of_hour = after_year.indexOf(hour + "");
                    String after_hour = after_year.substring(index_of_hour + 2);
                    //=============================================================
                    int minute = get_minute(after_hour);
                    if (minute == -1)//if no day number returned consider it a 0
                        minute = 0;
                    //=============================================================
                    String day_night = day_night(after_hour);//get hour format
                    //=============================================================
                    String date = DayofWeek.trim() + " " + month + " " + day_number + " " + year + " " + hour + ":" + minute + " " + day_night;
                    if (!isValidDate(date)) {//check if date is valid
                        Toast.makeText(MainActivity.this, "Invalid date , Please try again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //=============================================================
                    int month_no = Calendar.getInstance().get(Calendar.MONTH);
                    //=============================================================
                    try {//get month number
                        Date month2 = new SimpleDateFormat("MMMM").parse(month);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(month2);
                        month_no = cal.get(Calendar.MONTH);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //=============================================================
                    if (day_night.equals("PM")){//convert hour to hour_of_day
                        hour+=12;
                    }
                    //=============================================================
                    //create new intent and send data to this intent to create an event
                    Intent intent = new Intent(MainActivity.this, EventCreatorActivity.class);
                    intent.putExtra(MESSAGE_EVENT_NAME, event_name);
                    intent.putExtra(MESSAGE_EVENT_DATE_YEAR, year);
                    intent.putExtra(MESSAGE_EVENT_DATE_MONTH, month_no);
                    intent.putExtra(MESSAGE_EVENT_DATE_DAY_NAME, DayofWeek);
                    intent.putExtra(MESSAGE_EVENT_DATE_DAY, day_number);
                    intent.putExtra(MESSAGE_EVENT_TIME_HOUR, hour);
                    intent.putExtra(MESSAGE_EVENT_TIME_MINUTE, minute);
                    //intent.putExtra(MESSAGE_EVENT_TIME_FORMAT, day_night);
                    startActivity(intent);
                    //=============================================================
                    Log.v("RESULT", "String :" + result);
                    Log.v("RESULT", "Event name :" + event_name + " Day : " + DayofWeek + " Day no : " + day_number + " Month : " + month + " Year : " + year + " Hour :" + hour + " Minute : " + minute + " Format: " + day_night);
                    Log.v("DATE SPLIT ", date);
                } else
                    Toast.makeText(MainActivity.this, "Please try again , and make sure you are using the right format", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    private String checkContainsDay_getIt(String result) {//check for dayname
        if (result.contains("Saturday")) {
            return "saturday";
        } else if (result.contains("Sunday")) {
            return "sunday";
        } else if (result.contains("Monday")) {
            return "monday";
        } else if (result.contains("Tuesday")) {
            return "tuesday";
        } else if (result.contains("Wednesday")) {
            return "wednesday";
        } else if (result.contains("Thursday")) {
            return "thursday";
        } else if (result.contains("Friday")) {
            return "friday";
        } else return "";
    }

    private int get_day_number(String result) {//return day number
        Pattern pattern = Pattern.compile("(?<=\\s|\\/|-)([1-9]|0[1-9]|[1-2][0-9]|3[0-1])(?:st|nd|rd|th)?(?=\\b|\\/|-)");
        Matcher matcher = pattern.matcher(result);
        //=============================================================
        if (matcher.find()) {
            try {
                String[] trim_number;
                //=============================================================
                if (matcher.group(0).contains("st") || matcher.group(0).contains("nd") || matcher.group(0).contains("rd") || matcher.group(0).contains("th")) {
                    trim_number = matcher.group(0).split("(st|nd|rd|th)");
                    return Integer.valueOf(trim_number[0]);
                }
                //=============================================================
                else
                    return Integer.valueOf(matcher.group(0));
            } catch (Exception ex) {
                Log.e("ERROR IN GET_DAY_NUMBER", ex.getMessage());
            }
        }
        //=============================================================
        Toast.makeText(MainActivity.this, "Didn't find a day number ,Please try again using the proper format", Toast.LENGTH_SHORT).show();
        return -1;
        //=============================================================
    }

    private String get_month(String result) {//return month
        Pattern pattern = Pattern.compile("(January|February|March|April|May|June|July|August|September|October|November|December)");
        Matcher matcher = pattern.matcher(result);
        //=============================================================
        if (matcher.find())
            return matcher.group(0);
        //=============================================================
        Toast.makeText(MainActivity.this, "Didn't find a month ,Please try again using the proper format", Toast.LENGTH_SHORT).show();
        return "";
        //=============================================================
    }

    private int get_year(String result) {//return year
        Pattern pattern = Pattern.compile("((20)\\d{2})");
        Matcher matcher = pattern.matcher(result);
        //=============================================================
        if (matcher.find()) {
            try {
                return Integer.valueOf(matcher.group(0));
            } catch (Exception ex) {
                Log.e("ERROR IN GET_YEAR", ex.getMessage());
            }
        }
        //=============================================================
        Toast.makeText(MainActivity.this, "Didn't find a year ,Please try again using the proper format", Toast.LENGTH_SHORT).show();
        return -1;
        //=============================================================
    }

    private int get_hour(String result) {//return hour
        Pattern pattern = Pattern.compile("(?:([01]?\\d|2[0-3]))");
        Matcher matcher = pattern.matcher(result);
        //=============================================================
        if (matcher.find()) {
            try {
                return Integer.valueOf(matcher.group(0));
            } catch (Exception ex) {
                Log.e("ERROR IN GET_HOUR", ex.getMessage());
            }
        }
        //=============================================================
        Toast.makeText(MainActivity.this, "Didn't find an hour ,Please try again using the proper format", Toast.LENGTH_SHORT).show();
        return -1;
        //=============================================================
    }

    private int get_minute(String result) {//return minute
        Pattern pattern = Pattern.compile("([0-5]?\\d)");
        Matcher matcher = pattern.matcher(result);
        //=============================================================
        if (matcher.find()) {
            try {
                return Integer.valueOf(matcher.group(0));
            } catch (Exception ex) {
                Log.e("ERROR IN GET_MINUTE", ex.getMessage());
            }
        }
        //=============================================================
        return -1;
        //=============================================================
    }

    private String day_night(String result) {//return hour format
        if (result.contains("p.m") || result.contains("pm") || result.contains("P.M") || result.contains("PM"))
            return "PM";
        //=============================================================
        else if (result.contains("a.m") || result.contains("am") || result.contains("A.M") || result.contains("AM"))
            return "AM";
        //=============================================================
        else return "";
        //=============================================================
    }

    public static boolean isValidDate(String inDate) {//check date validity
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd yyyy hh:mm a");
        dateFormat.setLenient(false);
        //=============================================================
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        //=============================================================
        return true;
        //=============================================================
    }

    @Override
    public void onBackPressed() {//make android back do nothing
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //=============================================================
            case R.id.btn_display_events: {//show events
                if (GOT_PERMISSION) {
                    Intent intent = new Intent(MainActivity.this, UpcomingEventsActivity.class);
                    startActivity(intent);
                } else
                    showalert(MainActivity.this, MainActivity.this);
                break;
            }
            //=============================================================
            case R.id.btn_options: {//display options
                if (GOT_PERMISSION) {
                    Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
                    startActivity(intent);
                } else
                    showalert(MainActivity.this, MainActivity.this);
                break;
            }
            //=============================================================
            case R.id.btn_help: {//display help
                help_clicked=true;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Voice Calendar v1.0 ");
                builder.setMessage("Input format example:\n\"team lunch, Monday, March 26, 2018 at 7:30 pm.\"\n____________________\n\nYou can contact the app developer on the following email:\npete.ragheb@gmail.com")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                help_clicked=false;
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }
            //=============================================================
            case R.id.btn_exit: {//exit app
                finish();
                break;
            }
            //=============================================================
            case R.id.iv_create_event: {//create new event
                if (GOT_PERMISSION) {
                    startVoiceRecognition();
                } else
                    showalert(MainActivity.this, MainActivity.this);
                break;
            }
            //=============================================================
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //=============================================================
        outState.putBoolean("help_clicked",help_clicked);//if help is opened
        //=============================================================
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //=============================================================
        help_clicked=savedInstanceState.getBoolean("help_clicked");
        if (help_clicked==true){//open help after rotation
            btn_help.callOnClick();
        }
        //=============================================================
    }
}
