package com.games.peter.lab2_matching_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton fst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fst=(ImageButton)findViewById(R.id.ibtn_1);
        fst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fst.setImageResource(R.drawable.horse);
            }
        });
    }
}
