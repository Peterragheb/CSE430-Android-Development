package com.games.peter.lab2_matching_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView tv_score;
    Button btn_play_again;
    Button btn_leaderboard;
    private Intent AudioIntent;
    DatabaseHandler dbHandler;
    static boolean checkFirstTime=true;
    private String username;
    private String score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent=this.getIntent();
        if (intent.getStringExtra(LoginActivity.USERNAME_MESSAGE)==null) {
            startActivity(new Intent(ResultActivity.this,LoginActivity.class));
            finish();
        }
        username=intent.getStringExtra(LoginActivity.USERNAME_MESSAGE);
        dbHandler=new DatabaseHandler(this);
        tv_score=(TextView)findViewById(R.id.tv_score);
        btn_play_again=(Button)findViewById(R.id.btn_play_again);
        btn_leaderboard=(Button)findViewById(R.id.btn_leaderboard);
        score=intent.getIntExtra(MainActivity.SCORE_MESSAGE,-1)+" "+intent.getStringExtra(MainActivity.UNIT_MESSAGE);
        tv_score.setText(score);
        AudioIntent=new Intent(ResultActivity.this, SoundService.class);
        AudioIntent.putExtra("audio_id",R.raw.victory);
        if (checkFirstTime)
            startService(AudioIntent);
        checkFirstTime=false;
        dbHandler.insertUser(username,score);
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
        btn_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFirstTime=true;
                stopService(AudioIntent);
                Intent intent=new Intent(ResultActivity.this,LeaderboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
