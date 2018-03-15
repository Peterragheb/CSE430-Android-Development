package com.example.peter.quiz1_petermagdi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CourseActivity extends AppCompatActivity {
    TextView tv_course_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        tv_course_name=(TextView)findViewById(R.id.tv_course_name);
        Intent intent=getIntent();
        if (intent.getStringExtra(MainActivity.COURSE_NAME_MESSAGE)==null){
            Intent intent_activity=new Intent(CourseActivity.this,MainActivity.class);
            startActivity(intent_activity);
            finish();
        }
        else
        {
            String course_name=intent.getStringExtra(MainActivity.COURSE_NAME_MESSAGE);
            tv_course_name.setText(course_name);
        }
    }
}
