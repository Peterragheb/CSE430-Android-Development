package com.games.peter.animationdrawabletest;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
        start = findViewById(R.id.start);
        final AnimationDrawable anim = new AnimationDrawable();
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_01), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_02), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_03), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_04), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_05), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_06), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_07), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_09), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_10), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_11), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_12), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_13), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_14), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_15), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_16), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_17), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_18), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_19), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_20), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_21), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_22), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_23), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_24), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_25), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_26), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_27), 50);
        anim.addFrame(getResources().getDrawable(R.drawable.dude_animation_sheet_28), 50);
        //set ImageView to AnimatedDrawable
        image.setImageDrawable(anim);
        //if you want the animation to loop, set false
        anim.setOneShot(true);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anim.isRunning())
                    anim.stop();
                anim.start();
                //anim.
            }
        });


    }
}
