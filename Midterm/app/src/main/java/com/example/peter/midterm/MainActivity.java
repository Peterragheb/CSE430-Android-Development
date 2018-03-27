package com.example.peter.midterm;

import android.content.Intent;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String CORRECT_ANSWER="B - startActivityForResult()";
    public static String MESSAGE="result";
    RadioGroup rg_radio_group;
    Button btn_submit;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_radio_group=(RadioGroup)findViewById(R.id.rg_radio_group);
        btn_submit=(Button)findViewById(R.id.btn_submit);

        ttsinit();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rg_radio_group.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(),"Please choose an answer!",Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton rb_checked_button=(RadioButton)findViewById(rg_radio_group.getCheckedRadioButtonId());
                Intent intent=new Intent(MainActivity.this,ResultActivity.class);
                if (rb_checked_button.getText().toString().equals(CORRECT_ANSWER))
                {
                    intent.putExtra(MESSAGE,1);
                }
                else
                    intent.putExtra(MESSAGE,2);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (tts!=null)
        {
            tts.stop();
            tts.shutdown();
        }
        finish();
    }
    private void ttsinit(){
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.ENGLISH);
                    if (result==TextToSpeech.LANG_MISSING_DATA ||result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_LONG).show();
                        return;
                    }
                    tts.speak("Please Choose the most correct answer", TextToSpeech.QUEUE_FLUSH,null,null);
                }
            }
        });
    }
}
