package com.games.peter.lab2_matching_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResultActivity extends AppCompatActivity {
    //=====================================
    //fields
    TextView tv_score;
    Button btn_play_again;
    Button btn_leaderboard;
    private Intent AudioIntent;
    DatabaseHandler dbHandler;
    static boolean checkFirstTime=true;
    private static String username;
    private int score;
    private String score_type;
    DatabaseHandler mydb;
    private static final String TAG = "DBAdapter";
    //=====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //=====================================
        //if no username is passed then user need to login first
        Intent intent=this.getIntent();
        if (intent.getStringExtra(LoginActivity.USERNAME_MESSAGE)==null) {
            startActivity(new Intent(ResultActivity.this,LoginActivity.class));
            finish();
        }
        //=====================================
        //assign variables
        tv_score=(TextView)findViewById(R.id.tv_score);
        btn_play_again=(Button)findViewById(R.id.btn_play_again);
        btn_leaderboard=(Button)findViewById(R.id.btn_leaderboard);
        score=intent.getIntExtra(MainActivity.SCORE_MESSAGE,-1);
        score_type=intent.getStringExtra(MainActivity.UNIT_MESSAGE);
        tv_score.setText(score+" "+score_type);
        //=====================================
        //play audio of victory
        AudioIntent=new Intent(ResultActivity.this, SoundService.class);
        AudioIntent.putExtra("audio_id",R.raw.victory);
        //====================================
        if (checkFirstTime) {
            int scoretype=1;
            startService(AudioIntent);
            username=intent.getStringExtra(LoginActivity.USERNAME_MESSAGE);
            openDB();
            DateFormat df = new SimpleDateFormat("d/MM/yyyy, h:mm a");
            String date = df.format(Calendar.getInstance().getTime());
            long indx=mydb.insertRow(username,score,score_type,date);
            //dbHandler=new DatabaseHandler(this);
            //dbHandler.insertUser(username,score);
        }
        //=====================================
        checkFirstTime=false;

        //=====================================
        //play button listener
        btn_play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFirstTime=true;
                stopService(AudioIntent);
                Intent intent=new Intent(ResultActivity.this,MainActivity.class);
                intent.putExtra(LoginActivity.USERNAME_MESSAGE,username);
                startActivity(intent);
                finish();
            }
        });

        //leaderboard button click to display scores
        btn_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(AudioIntent);
                Intent intent=new Intent(ResultActivity.this,LeaderboardActivity.class);
                startActivity(intent);
            }
        });
        //=====================================

    }

    //open database
    private void openDB(){
        mydb=new DatabaseHandler(this);
        mydb.open();
    }

    //close database
    private void closeDB(){
        if (mydb!=null)
            mydb.close();
    }

    //destroy activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }
}
