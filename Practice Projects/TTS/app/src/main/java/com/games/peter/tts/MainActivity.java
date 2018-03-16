package com.games.peter.tts;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;
    Button btn_read;
    Button btn_speak;
    EditText et_text;
    TextView tv_spokentext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_read=findViewById(R.id.btn_read);
        btn_speak=findViewById(R.id.btn_speak);
        tv_spokentext=findViewById(R.id.tv_spokentext);
        et_text=findViewById(R.id.et_text);
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=et_text.getText().toString();
                text.trim();
                if (!text.isEmpty()){
                    tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
                }
            }
        });
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.ENGLISH);
                    if (result==TextToSpeech.LANG_MISSING_DATA ||result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak up");
                startActivityForResult(i, 1);
            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //if user says the phrase "poptarts"
            //call function poptart

            //if user says the phrase "banana"
            //call function banana
            ArrayList< String > result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            tv_spokentext.setText(result.get(0).toString());

            Toast.makeText(getApplicationContext(), "sound detected", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts!=null)
        {
            tts.stop();
            tts.shutdown();
        }
    }
}
