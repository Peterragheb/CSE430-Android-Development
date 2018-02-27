package com.games.peter.lab2_matching_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<ImageButton> imgbtns=new ArrayList<>();
    ArrayList<imbutton> randFilled=new ArrayList<>();
    ArrayList <Integer> randimges=new ArrayList<>();
    ArrayList<ImageButton> clicked=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgbtns.add((ImageButton)findViewById(R.id.ibtn_1));
        imgbtns.add((ImageButton)findViewById(R.id.ibtn_2));
        imgbtns.add((ImageButton)findViewById(R.id.ibtn_3));
        imgbtns.add((ImageButton)findViewById(R.id.ibtn_4));
        imgbtns.add((ImageButton)findViewById(R.id.ibtn_5));
        imgbtns.add((ImageButton)findViewById(R.id.ibtn_6));
        imgbtns.add((ImageButton)findViewById(R.id.ibtn_7));
        imgbtns.add((ImageButton)findViewById(R.id.ibtn_8));

        for (int i=0;i<imgbtns.size();i++){
            imgbtns.get(i).setOnClickListener(this);
        }
        for (int i=0;i<8;i++)
            randimges.add(i);
        RandomFiller();

    }

    @Override
    public void onClick(View view) {
        ImageButton im=(ImageButton)view;
        imbutton btn=findImageButton(im.getId());
        if (!btn.isClicked()&&clicked.size()<2){
            if (clicked.size()==1){
                im.setImageResource(randFilled.get(GetBtnIndex(im.getId())).getImage());
                if (randimges.get(GetBtnIndex(im.getId()))==randimges.get(GetBtnIndex(clicked.get(0).getId()))){
                    imgbtns.get(GetBtnIndex(im.getId())).setVisibility(View.INVISIBLE);
                    imgbtns.get(GetBtnIndex(clicked.get(0).getId())).setVisibility(View.INVISIBLE);
                    imgbtns.get(GetBtnIndex(im.getId())).setClickable(false);
                    imgbtns.get(GetBtnIndex(clicked.get(0).getId())).setClickable(false);
                    clicked.clear();
                    //Toast.makeText(this,clicked.size()+"",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    im.setImageResource(R.drawable.transparent);
                    imgbtns.get(GetBtnIndex(clicked.get(0).getId())).setImageResource(R.drawable.transparent);
                    clicked.clear();
                    return;
                }

            }
            if (clicked.size()==0){
                im.setImageResource(randFilled.get(GetBtnIndex(im.getId())).getImage());
                clicked.add(btn.getImbtn());
               // Toast.makeText(MainActivity.this,clicked.size()+"",Toast.LENGTH_SHORT).show();
                return;
            }

            }
    }
    private void RandomFiller(){//random.nextInt(max - min + 1) + min
        Collections.shuffle(randimges);
        String imgs="";
        for (int i=0;i<randimges.size();i++){
            imgs+=randimges.get(i).toString()+" ";
        }
        Toast.makeText(MainActivity.this,imgs,Toast.LENGTH_SHORT).show();
        for (int i=0;i<imgbtns.size();i++){
            switch (randimges.get(i)){
                case 1:
                    randFilled.add(new imbutton(imgbtns.get(i),R.drawable.horse,false));
                    break;
                case 2:
                    randFilled.add(new imbutton(imgbtns.get(i),R.drawable.beach,false));
                    break;
                case 3:
                    randFilled.add(new imbutton(imgbtns.get(i),R.drawable.elephant_clipart_transparent_background_10,false));
                    break;
                case 4:
                    randFilled.add(new imbutton(imgbtns.get(i),R.drawable.dice,false));
                    break;
            }
        }
    }
    imbutton findImageButton(int id) {
        for(imbutton imgbtn : randFilled) {
            if(imgbtn.getImbtn().getId()==id) {
                return imgbtn;
            }
        }
        return null;
    }
    imbutton findImageButton2(int id) {
        for(imbutton imgbtn : randFilled) {
            if(imgbtn.getImbtn().getId()==id) {
                return imgbtn;
            }
        }
        return null;
    }

    int GetBtnID(int index) {
        switch (index){
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
        return 0;
    }
    int GetBtnIndex(int id) {
        switch (id){
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
        return 0;
    }
}
