package com.example.peter.midterm;

import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    TextView tv_result;
    Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv_result=(TextView)findViewById(R.id.tv_result);
        btn_back=(Button)findViewById(R.id.btn_back);
        Intent intent=getIntent();

        int result=intent.getIntExtra(MainActivity.MESSAGE,-1);
        if (result==1){
            tv_result.setText("Correct Answer");
            tv_result.setBackgroundColor(Color.GREEN);

        }else if (result==2){
            tv_result.setText("Wrong Answe.Try Again.");
            tv_result.setBackgroundColor(Color.RED);
        }
        else {
            tv_result.setText("Wrong Answe.Try Again.");
            tv_result.setBackgroundColor(Color.RED);
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ResultActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
