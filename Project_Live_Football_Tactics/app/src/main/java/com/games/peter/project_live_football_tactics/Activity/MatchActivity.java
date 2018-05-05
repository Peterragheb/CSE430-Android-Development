package com.games.peter.project_live_football_tactics.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.games.peter.project_live_football_tactics.Adapter.SectionPageAdapter;
import com.games.peter.project_live_football_tactics.Fragment.tab_Lineup_fragment;
import com.games.peter.project_live_football_tactics.Fragment.tab_chat_fragment;
import com.games.peter.project_live_football_tactics.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MatchActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    //===============================================================================================================
    ListView lv_chat;
    private SectionPageAdapter sectionPageAdapter;
    protected ViewPager viewPager;
    tab_chat_fragment mtab_chat_fragment;
    public tab_Lineup_fragment mtab_Lineup_fragment;
    ImageView iv_go_to_chat_tab,iv_go_to_lineup_tab;
    Animation anim_scale_up,anim_scale_down;
    private FirebaseAuth mAuth; // responsible of authenticating users
    FirebaseUser currentUser;
    //===============================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        initComponents();

        initUIComponents();
    }
    //======================================================
    private void initUIComponents(){
        sectionPageAdapter=new SectionPageAdapter(getSupportFragmentManager());
        iv_go_to_chat_tab=findViewById(R.id.iv_go_to_chat_tab);
        iv_go_to_lineup_tab=findViewById(R.id.iv_go_to_lineup_tab);
        viewPager=findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(2);
        setupViewPage(viewPager);
        TabLayout tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        anim_scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        anim_scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
    }
    //======================================================
    private void setupViewPage(ViewPager viewPager){
        SectionPageAdapter sectionPageAdapter=new SectionPageAdapter(getSupportFragmentManager());
        mtab_Lineup_fragment=new tab_Lineup_fragment();
        sectionPageAdapter.addfragment(mtab_Lineup_fragment,"Lineup");

        if (getIntent().hasExtra("team_id")){
            int team_id = getIntent().getIntExtra("team_id",0);
            if(team_id!=0){
                Log.v("SETUP_VIEW","fav team : "+team_id);
                int away_team_id =getIntent().getIntExtra("away_team_id",0);
                int home_team_id =getIntent().getIntExtra("home_team_id",0);
                Log.v("SETUP_VIEW","fav team : "+team_id+"  away team :"+away_team_id+"   home team : "+home_team_id);
                if (away_team_id!=0 &&home_team_id!=0){
                    if ((team_id==away_team_id)||(team_id==home_team_id)){
                        Log.v("SETUP_VIEW","fav team : MATCHHH!!");
                        mtab_chat_fragment=new tab_chat_fragment();
                        sectionPageAdapter.addfragment(mtab_chat_fragment,"Chat");
                        iv_go_to_chat_tab.setVisibility(View.VISIBLE);
                        iv_go_to_lineup_tab.setVisibility(View.INVISIBLE);
                    }
                    else {
                        iv_go_to_chat_tab.setVisibility(View.GONE);
                        iv_go_to_lineup_tab.setVisibility(View.GONE);
                    }
                }
                else {
                    iv_go_to_chat_tab.setVisibility(View.GONE);
                    iv_go_to_lineup_tab.setVisibility(View.GONE);
                }
            }
            else {
                iv_go_to_chat_tab.setVisibility(View.GONE);
                iv_go_to_lineup_tab.setVisibility(View.GONE);
            }
        }
        else {
            iv_go_to_chat_tab.setVisibility(View.GONE);
            iv_go_to_lineup_tab.setVisibility(View.GONE);
        }
        iv_go_to_chat_tab.setOnClickListener(this);
        iv_go_to_lineup_tab.setOnClickListener(this);

        viewPager.setAdapter(sectionPageAdapter);
    }
    //======================================================

    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
        if (mAuth!=null)
            currentUser = mAuth.getCurrentUser();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition()==0){
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            iv_go_to_lineup_tab.startAnimation(anim_scale_down);
            iv_go_to_lineup_tab.setVisibility(View.INVISIBLE);
            iv_go_to_chat_tab.startAnimation(anim_scale_up);
            iv_go_to_chat_tab.setVisibility(View.VISIBLE);
        }
        if (tab.getPosition()==1) {
            iv_go_to_chat_tab.startAnimation(anim_scale_down);
            iv_go_to_chat_tab.setVisibility(View.INVISIBLE);
            iv_go_to_lineup_tab.startAnimation(anim_scale_up);
            iv_go_to_lineup_tab.setVisibility(View.VISIBLE);
        }
    }
    //======================================================
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }
    //======================================================
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    //======================================================
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_go_to_chat_tab){
            viewPager.setCurrentItem(1);
        }
        else if (v.getId()==R.id.iv_go_to_lineup_tab){
            viewPager.setCurrentItem(0);
        }
    }
    //======================================================
}
