package com.games.peter.project_live_football_tactics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //ListView lv_matches,lv_matches2;
    LinearLayout ll_league_matches,ll_league_matches2,ll_league_matches3,ll_league_matches4,ll_league_matches5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //lv_matches=findViewById(R.id.includedLayout).findViewById(R.id.lv_league_matches);
        //lv_matches2=findViewById(R.id.includedLayout2).findViewById(R.id.lv_league_matches);

        ll_league_matches=findViewById(R.id.cv_league1_matches_list).findViewById(R.id.ll_league_matches);
        ll_league_matches2=findViewById(R.id.cv_league2_matches_list).findViewById(R.id.ll_league_matches);
        ll_league_matches3=findViewById(R.id.cv_league3_matches_list).findViewById(R.id.ll_league_matches);
        ll_league_matches4=findViewById(R.id.cv_league4_matches_list).findViewById(R.id.ll_league_matches);
        ll_league_matches5=findViewById(R.id.cv_league5_matches_list).findViewById(R.id.ll_league_matches);
        ArrayList<Match> matchlist=new ArrayList<>();
        matchlist.add(new Match(10,"Barcelona","Real Madrid",1,0));
        matchlist.add(new Match(60,"Ateltico Madrid","Sevilla",2,1));
        matchlist.add(new Match(25,"Manchester United","Manchester City",2,2));
        matchlist.add(new Match(Calendar.getInstance().getTime(),"Chelsea","Liverpool"));
        matchlist.add(new Match(Calendar.getInstance().getTime(),"Tottenham","Arsenal"));
        CustomAdapter customAdapter=new CustomAdapter(this,R.layout.match_row_details,matchlist);
       // lv_matches.setAdapter(customAdapter);
        //lv_matches2.setAdapter(customAdapter);
        //=============================================================
//        lv_matches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(MainActivity.this,MatchActivity.class));
//            }
//        });
//        lv_matches2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(MainActivity.this,MatchActivity.class));
//            }
//        });
        for(int i = 0 ; i < customAdapter.getCount(); i++){
            ll_league_matches.addView(customAdapter.getView(i, null, ll_league_matches));
//            TypedValue outValue = new TypedValue();
//            MainActivity.this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
//            ll_league_matches.getChildAt(i).setBackgroundResource(outValue.resourceId);
            ll_league_matches.getChildAt(i).setId(i);
            ll_league_matches.getChildAt(i).setOnClickListener(this);
        }

        for(int i = 0 ; i < customAdapter.getCount(); i++){
            ll_league_matches2.addView(customAdapter.getView(i, null, ll_league_matches2));
            ll_league_matches2.getChildAt(i).setId(i);
            ll_league_matches2.getChildAt(i).setOnClickListener(this);
        }

        for(int i = 0 ; i < customAdapter.getCount(); i++){
            ll_league_matches3.addView(customAdapter.getView(i, null, ll_league_matches3));
            ll_league_matches3.getChildAt(i).setId(i);
            ll_league_matches3.getChildAt(i).setOnClickListener(this);
        }

        for(int i = 0 ; i < customAdapter.getCount(); i++){
            ll_league_matches4.addView(customAdapter.getView(i, null, ll_league_matches4));
            ll_league_matches4.getChildAt(i).setId(i);
            ll_league_matches4.getChildAt(i).setOnClickListener(this);
        }

        for(int i = 0 ; i < customAdapter.getCount(); i++){
            ll_league_matches5.addView(customAdapter.getView(i, null, ll_league_matches5));
            ll_league_matches5.getChildAt(i).setId(i);
            ll_league_matches5.getChildAt(i).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        LinearLayout ll_match=findViewById(v.getId());
        TextView home_team = ll_match.findViewById(R.id.tv_match_home_team);
        TextView away_team = ll_match.findViewById(R.id.tv_match_away_team);
        Intent intent=new Intent(MainActivity.this,MatchActivity.class);
        intent.putExtra("HOME_TEAM_NAME",home_team.getText().toString());
        intent.putExtra("AWAY_TEAM_NAME",away_team.getText().toString());
        startActivity(intent);
        //Toast.makeText(MainActivity.this,"Selected Match Home Team :"+home_team.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
