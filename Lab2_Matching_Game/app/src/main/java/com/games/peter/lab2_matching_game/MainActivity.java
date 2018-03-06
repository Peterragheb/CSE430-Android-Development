package com.games.peter.lab2_matching_game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String SCORE_MESSAGE = "Score";
    public final static String UNIT_MESSAGE = "Unit";
    private ArrayList<ImageButton> imgbtns = new ArrayList<>();
    private ArrayList<Integer> randimges = new ArrayList<>();
    private ArrayList<ImageButton> clicked = new ArrayList<>();
    private int Available_Buttons = 8;
    private Intent AudioIntent;
    private Date Startime;
    private Date Finishtime;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=this.getIntent();
        if (intent.getStringExtra(LoginActivity.USERNAME_MESSAGE)==null) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        username=intent.getStringExtra(LoginActivity.USERNAME_MESSAGE);
        imgbtns.add((ImageButton) findViewById(R.id.ibtn_1));
        imgbtns.add((ImageButton) findViewById(R.id.ibtn_2));
        imgbtns.add((ImageButton) findViewById(R.id.ibtn_3));
        imgbtns.add((ImageButton) findViewById(R.id.ibtn_4));
        imgbtns.add((ImageButton) findViewById(R.id.ibtn_5));
        imgbtns.add((ImageButton) findViewById(R.id.ibtn_6));
        imgbtns.add((ImageButton) findViewById(R.id.ibtn_7));
        imgbtns.add((ImageButton) findViewById(R.id.ibtn_8));

        for (int i = 0; i < imgbtns.size(); i++) {
            imgbtns.get(i).setOnClickListener(this);
            imgbtns.get(i).setImageResource(R.drawable.transparent);
            imgbtns.get(i).setTag(R.drawable.transparent);
        }
        for (int i = 1; i <= 8; i++)
            randimges.add((i % 4) + 1);
        Collections.shuffle(randimges);
        String imgs = "";
        for (int i = 0; i < randimges.size(); i++) {
            imgs += randimges.get(i).toString() + " ";
        }
        AudioIntent = new Intent(MainActivity.this, SoundService.class);
        Startime = new Date(System.currentTimeMillis());
        Finishtime = new Date(System.currentTimeMillis());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("randimges", randimges);
        for (ImageButton imbtn : imgbtns) {
            // outState.putInt("BUTTON_"+GetBtnIndex(imbtn.getId())+"_ID",imbtn.getId());
            outState.putInt("BUTTON_" + GetBtnIndex(imbtn.getId()) + "_VISIBILITY", imbtn.getVisibility());
            outState.putBoolean("BUTTON_" + GetBtnIndex(imbtn.getId()) + "_CLICKABLE", imbtn.isClickable());
            outState.putInt("BUTTON_" + GetBtnIndex(imbtn.getId()) + "_IMAGE", (Integer) imbtn.getTag());
        }
        outState.putInt("AVAILABLE_BUTTONS", Available_Buttons);
        outState.putInt("CLICKED_SIZE", clicked.size());
        for (int i = 0; i < clicked.size(); i++) {
            outState.putInt("CLICKED_BUTTON_" + i, clicked.get(i).getId());
        }
        outState.putLong("START_TIME", Startime.getTime());
        outState.putLong("FINISH_TIME", Finishtime.getTime());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        randimges = savedInstanceState.getIntegerArrayList("randimges");
        for (ImageButton imbtn : imgbtns) {
            // outState.putInt("BUTTON_"+GetBtnIndex(imbtn.getId())+"_ID",imbtn.getId());
            imbtn.setVisibility(savedInstanceState.getInt("BUTTON_" + GetBtnIndex(imbtn.getId()) + "_VISIBILITY"));
            imbtn.setClickable(savedInstanceState.getBoolean("BUTTON_" + GetBtnIndex(imbtn.getId()) + "_CLICKABLE"));
            imbtn.setImageResource(savedInstanceState.getInt("BUTTON_" + GetBtnIndex(imbtn.getId()) + "_IMAGE"));
            imbtn.setTag(savedInstanceState.getInt("BUTTON_" + GetBtnIndex(imbtn.getId()) + "_IMAGE"));
        }
        Available_Buttons = savedInstanceState.getInt("AVAILABLE_BUTTONS");
        int clicked_size = savedInstanceState.getInt("CLICKED_SIZE");
        for (int i = 0; i < clicked_size; i++) {
            int id = savedInstanceState.getInt("CLICKED_BUTTON_" + i);
            clicked.add(findImageButton(id));
        }
        Startime.setTime(savedInstanceState.getLong("START_TIME"));
        Finishtime.setTime(savedInstanceState.getLong("FINISH_TIME"));
        // System.out.println("CLICKED_SIZE: "+clicked_size);
    }

    @Override
    public void onClick(View view) {
        final ImageButton im = (ImageButton) view;
        GameLogic(im);
    }

    private void GameLogic(final ImageButton im) {
        if (clicked.size() < 2) {
            if (clicked.size() == 1 && (im.getId() != clicked.get(0).getId())) {
                im.setImageResource(GetImageID(randimges.get(GetBtnIndex(im.getId()))));
                im.setTag(GetImageID(randimges.get(GetBtnIndex(im.getId()))));
                if ((randimges.get(GetBtnIndex(im.getId())) == randimges.get(GetBtnIndex(clicked.get(0).getId())))) {//IF a match
                    for (ImageButton imbtn : imgbtns) {
                        imbtn.setClickable(false);
                    }
                    AudioIntent.putExtra("audio_id", GetAudioID(randimges.get(GetBtnIndex(im.getId()))));
                    startService(AudioIntent);
                    AudioIntent.putExtra("audio_id", R.raw.correct);
                    startService(AudioIntent);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                    Handler handler = new Handler();
                    Available_Buttons -= 2;
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            for (ImageButton imbtn : imgbtns) {
                                imbtn.setClickable(true);
                            }
                            imgbtns.get(GetBtnIndex(im.getId())).setVisibility(View.INVISIBLE);
                            imgbtns.get(GetBtnIndex(clicked.get(0).getId())).setVisibility(View.INVISIBLE);
                            imgbtns.get(GetBtnIndex(im.getId())).setClickable(false);
                            imgbtns.get(GetBtnIndex(clicked.get(0).getId())).setClickable(false);
                            clicked.clear();
                            stopService(AudioIntent);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            if (Available_Buttons == 0) {//no remaining unmatched buttons
                                Finishtime = new Date(System.currentTimeMillis());
                                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                                long time = Finishtime.getTime() - Startime.getTime();
                                if (time < 60000)//less than a  minute
                                {
                                    intent.putExtra(UNIT_MESSAGE, "Seconds");
                                    intent.putExtra(SCORE_MESSAGE, (int) Math.ceil(time / (1000)));
                                    intent.putExtra(LoginActivity.USERNAME_MESSAGE,username);
                                } else if (time > 60000 && time < 3600000)//less than an hour
                                {
                                    intent.putExtra(UNIT_MESSAGE, "Minutes");
                                    intent.putExtra(SCORE_MESSAGE, (int) Math.ceil(time / (1000 * 60)));
                                    intent.putExtra(LoginActivity.USERNAME_MESSAGE,username);
                                } else if (time > 3600000)//more than an hour
                                {
                                    intent.putExtra(UNIT_MESSAGE, "Hours");
                                    intent.putExtra(SCORE_MESSAGE, (int) Math.ceil(time / (1000 * 60 * 60)));
                                    intent.putExtra(LoginActivity.USERNAME_MESSAGE,username);
                                }
                                startActivity(intent);
                                finish();
                            }
                        }
                    }, 1000);
                    return;
                } else if (randimges.get(GetBtnIndex(im.getId())) != randimges.get(GetBtnIndex(clicked.get(0).getId()))) {//if not at match
                    for (ImageButton imbtn : imgbtns) {
                        imbtn.setClickable(false);
                    }
                    AudioIntent.putExtra("audio_id", GetAudioID(randimges.get(GetBtnIndex(im.getId()))));
                    startService(AudioIntent);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            for (ImageButton imbtn : imgbtns) {
                                imbtn.setClickable(true);
                            }
                            im.setImageResource(R.drawable.transparent);
                            im.setTag(R.drawable.transparent);
                            imgbtns.get(GetBtnIndex(clicked.get(0).getId())).setImageResource(R.drawable.transparent);
                            imgbtns.get(GetBtnIndex(clicked.get(0).getId())).setTag(R.drawable.transparent);
                            clicked.clear();
                            stopService(AudioIntent);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                        }
                    }, 1000);
                    return;
                }
            }
            if (clicked.size() == 0) {//one button is clicked
                AudioIntent.putExtra("audio_id", GetAudioID(randimges.get(GetBtnIndex(im.getId()))));
                startService(AudioIntent);
                for (ImageButton imbtn : imgbtns) {
                    imbtn.setClickable(false);
                }
                im.setImageResource(GetImageID(randimges.get(GetBtnIndex(im.getId()))));
                im.setTag(GetImageID(randimges.get(GetBtnIndex(im.getId()))));
                clicked.add(im);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        for (ImageButton imbtn : imgbtns) {
                            imbtn.setClickable(true);
                        }
                        im.setClickable(false);
                        stopService(AudioIntent);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                }, 1000);
                return;
            }

        }
    }

    ImageButton findImageButton(int id) {
        for (ImageButton imgbtn : imgbtns) {
            if (imgbtn.getId() == id) {
                return imgbtn;
            }
        }
        return null;
    }

    int GetBtnID(int index) {
        switch (index) {
            case 0:
                return R.id.ibtn_1;
            case 1:
                return R.id.ibtn_2;
            case 2:
                return R.id.ibtn_3;
            case 3:
                return R.id.ibtn_4;
            case 4:
                return R.id.ibtn_5;
            case 5:
                return R.id.ibtn_6;
            case 6:
                return R.id.ibtn_7;
            case 7:
                return R.id.ibtn_8;
        }
        return -1;
    }

    int GetBtnIndex(int id) {
        switch (id) {
            case R.id.ibtn_1:
                return 0;
            case R.id.ibtn_2:
                return 1;
            case R.id.ibtn_3:
                return 2;
            case R.id.ibtn_4:
                return 3;
            case R.id.ibtn_5:
                return 4;
            case R.id.ibtn_6:
                return 5;
            case R.id.ibtn_7:
                return 6;
            case R.id.ibtn_8:
                return 7;
        }
        return -1;
    }

    int GetImageID(int randomnumber) {
        switch (randomnumber) {
            case 1:
                return R.drawable.horse;
            case 2:
                return R.drawable.cat;
            case 3:
                return R.drawable.elephant_clipart_transparent_background_10;
            case 4:
                return R.drawable.dog;
        }
        return -1;
    }

    int GetAudioID(int randomnumber) {
        switch (randomnumber) {
            case 1:
                return R.raw.horse;
            case 2:
                return R.raw.cat;
            case 3:
                return R.raw.elephant;
            case 4:
                return R.raw.dog;
        }
        return -1;
    }
}
