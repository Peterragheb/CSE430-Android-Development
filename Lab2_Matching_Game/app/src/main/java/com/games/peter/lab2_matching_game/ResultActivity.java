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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tv_score=(TextView)findViewById(R.id.tv_score);
        btn_play_again=(Button)findViewById(R.id.btn_play_again);
        Intent intent=this.getIntent();
        tv_score.setText(intent.getIntExtra(MainActivity.SCORE_MESSAGE,-1)+" "+intent.getStringExtra(MainActivity.UNIT_MESSAGE));
        btn_play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}
