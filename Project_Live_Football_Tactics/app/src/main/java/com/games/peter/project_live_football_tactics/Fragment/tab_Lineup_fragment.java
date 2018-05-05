package com.games.peter.project_live_football_tactics.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Activity.MainActivity;
import com.games.peter.project_live_football_tactics.Adapter.ApiAdapter;
import com.games.peter.project_live_football_tactics.Adapter.SectionPageAdapter;
import com.games.peter.project_live_football_tactics.Class.Lineup;
import com.games.peter.project_live_football_tactics.Class.Match;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;
import com.games.peter.project_live_football_tactics.R;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;

/**
 * Created by Peter on 19/4/2018.
 */

public class tab_Lineup_fragment extends Fragment implements TabLayout.OnTabSelectedListener,Match.ChangeListener{
    private SectionPageAdapter sectionPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String fixture_id;
    public Match match;
    private ArrayList<LineupListener> listeners = new ArrayList<>();
    private ArrayList<Lineup> lineups;
    tab_home_lineup_fragment mtaTab_home_lineup_fragment;
    tab_away_lineup_fragment mtaTab_away_lineup_fragment;
    LinearLayout ll_matchdetails_scorestats,ll_matchdetails_lineup_tabs;
    TextView tv_hometeam_name,tv_awayteam_name,tv_match_fragment_time,tv_match_fragment_home_team_score,tv_match_fragment_away_team_score;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_lineup_fragment,container,false);
        listeners = new ArrayList<>();
        initUIComponents(view);

        return view;
    }
    //======================================================
    private void initUIComponents(View view){
        sectionPageAdapter=new SectionPageAdapter(getFragmentManager());
        viewPager=view.findViewById(R.id.container2);
        ll_matchdetails_scorestats = view.findViewById(R.id.ll_matchdetails_scorestats);
        ll_matchdetails_lineup_tabs = view.findViewById(R.id.ll_matchdetails_lineup_tabs);
        viewPager.setOffscreenPageLimit(2);
        setupViewPage(viewPager);
        tabLayout=view.findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        tv_hometeam_name=view.findViewById(R.id.tv_match_fragment_home_team_name);
        tv_awayteam_name=view.findViewById(R.id.tv_match_fragment_away_team_name);
        tv_match_fragment_time = view.findViewById(R.id.tv_match_fragment_time);
        tv_match_fragment_home_team_score = view.findViewById(R.id.tv_match_fragment_home_team_score);
        tv_match_fragment_away_team_score = view.findViewById(R.id.tv_match_fragment_away_team_score);
        setupTabIcons();
        Intent intent=getActivity().getIntent();
            if (intent.hasExtra("fixture_id"))
                fixture_id = intent.getStringExtra("fixture_id");
            if (intent.hasExtra("list")){
                listen_to_MatchUpdates(intent,view);//make this fragment a listener for updates of match
        }
        else
            {
                showErrorDialog(null);
            }
    }

    //======================================================
    private void setupViewPage(ViewPager viewPager){
        SectionPageAdapter sectionPageAdapter=new SectionPageAdapter(getFragmentManager());
        mtaTab_home_lineup_fragment=new tab_home_lineup_fragment();
        mtaTab_away_lineup_fragment=new tab_away_lineup_fragment();

        sectionPageAdapter.addfragment(mtaTab_home_lineup_fragment,"");
        sectionPageAdapter.addfragment(mtaTab_away_lineup_fragment,"");
        viewPager.setAdapter(sectionPageAdapter);
        setViewPager_Height();
        this.addListener(mtaTab_away_lineup_fragment);
        this.addListener(mtaTab_home_lineup_fragment);
    }
    //======================================================
    private void setViewPager_Height(){
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int screen_height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int match_details_height = ll_matchdetails_scorestats.getHeight();
        int tablayout_lineup_height = ll_matchdetails_lineup_tabs.getHeight();
        int toolbar_height =  getActivity().findViewById(R.id.toolbar).getHeight();
        int statusbar_height = rect.top;
        int height=screen_height - statusbar_height - toolbar_height - match_details_height - tablayout_lineup_height;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        viewPager.setLayoutParams(params);
    }
    //======================================================
    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.circle,
                R.drawable.circle2,
        };
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }
    //======================================================
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

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
    private void listen_to_MatchUpdates(Intent intent,View view){
        int list_id,match_id;
        list_id = intent.getIntExtra("list",0);
        if (intent.hasExtra("match")){
            match_id = intent.getIntExtra("match",0);
            if (list_id==1){
                match=MainActivity.al_list1_matches.get(match_id);
            }else if (list_id==2){
                match=MainActivity.al_list2_matches.get(match_id);
            }
            else if (list_id==3){
                match=MainActivity.al_list3_matches.get(match_id);
            }
            else if (list_id==4){
                match=MainActivity.al_list4_matches.get(match_id);
            }
            else if (list_id==5){
                match=MainActivity.al_list5_matches.get(match_id);

            }
            if (match!=null){
                match.addListener(this);
                tv_awayteam_name.setText(match.getTeam_season_away_name());
                tv_hometeam_name.setText(match.getTeam_season_home_name());
                tv_match_fragment_away_team_score.setText(match.getNumber_goal_team_away());
                tv_match_fragment_home_team_score.setText(match.getNumber_goal_team_home());
                if (!match.getFixture_status_short().equals(StaticStringsMethods.NOT_STARTED)&&!match.getFixture_status_short().equals(StaticStringsMethods.FINISHED)&&!match.getFixture_status_short().equals(StaticStringsMethods.HALF_TIME))
                    tv_match_fragment_time.setText(match.getElapsed());
                else
                    tv_match_fragment_time.setText(match.getFixture_status_short());
                if (match.getLineup_confirmed().equals("yes")){
                    Log.v("Lineup_confirmed","yes");
                    if (!match.getLineups().isEmpty()){
                        // setLineups(match.getLineups());
                        Log.v("match_lineup_empty","no");
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("lineups", match.getLineups());
                        mtaTab_away_lineup_fragment.setArguments(bundle);
                        mtaTab_home_lineup_fragment.setArguments(bundle);
                        this.lineups = match.getLineups();
                    }
                    else
                    {
                        Log.v("match_lineup_empty","yes");
                        getLineups();
                    }
                }
            }
            else
            {
                showErrorDialog("Something went wrong please restart the application\nIf this keeps happening contact the application developer");
            }

        }
        else
        {
            showErrorDialog("Something went wrong please restart the application\nIf this keeps happening contact the application developer");
        }
    }
    //======================================================
    public void getLineups(){
        if (fixture_id!=null){
            ArrayList<Lineup> lineups = ApiAdapter.getFixtureLineup(StaticStringsMethods.REQUEST_IN_FIXTURES+
                    fixture_id+
                    StaticStringsMethods.REQUEST_LINEUPS+
                    StaticStringsMethods.ACCESS_TOKEN_FIELD);
            if (lineups!=null &&!lineups.isEmpty()){
                match.setLineups(lineups);
                setLineups(lineups);
            }
        }

    }
    //======================================================
    private void setLineups(ArrayList<Lineup> lineups){
        this.lineups = lineups;
//        for (int i=0;i<this.listeners.size();i++){
//            listeners.get(i).onChange(lineups);
//        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("lineups", match.getLineups());
        mtaTab_away_lineup_fragment.setArguments(bundle);
        mtaTab_home_lineup_fragment.setArguments(bundle);
    }
    //======================================================
    @Override
    public void onDestroy() {
        Log.v("onDestroy","tab_lineup");
        match.removeListener(this);
        super.onDestroy();
    }
    //======================================================
    @Override
    public void onChange(String field,String value) {

        Log.v("onChange","triggered!!!!!!!!!!!!!!!!!!!");
        if (field.equals("fixture_id")){ }
        //======================================================
        else if (field.equals("id_season")){ }
        //======================================================
        else if (field.equals("setFixture_status_short")){
            if ((value.equals(StaticStringsMethods.HALF_TIME))||
                    (value.equals(StaticStringsMethods.FINISHED))){
                TextView tv_time = getView().findViewById(R.id.tv_match_fragment_time);
                tv_time.setText(value);
            }
        }
        //======================================================
        else if (field.equals("setId_team_season_away")){ }
        //======================================================
        else if (field.equals("setId_team_season_home")){ }
        //======================================================
        else if (field.equals("number_goal_team_away")){
            TextView tv = getView().findViewById(R.id.tv_match_fragment_away_team_score);
            tv.setText(value);
        }
        //======================================================
        else if (field.equals("number_goal_team_home")){
            TextView tv =getView().findViewById(R.id.tv_match_fragment_home_team_score);
            tv.setText(value);
        }
        //======================================================
        else if (field.equals("schedule_date")){ }
        else if (field.equals("team_season_away_name")){ }
        else if (field.equals("team_season_home_name")){ }
        else if (field.equals("lineup_confirmed")){
            //trigger firebase notification if favorite team
            //dont know how to check if favorite team
           getLineups();
        }
        //======================================================
        else if (field.equals("elapsed")){
            TextView tv_time = getView().findViewById(R.id.tv_match_fragment_time);
            tv_time.setText(value+"'");
        }
        //======================================================
    }
    //======================================================
    public interface LineupListener {
        void onChange(ArrayList<Lineup>  lineups);
    }
    //======================================================
    public ArrayList<LineupListener> getListenerList() {
        return listeners;
    }
    //======================================================
    public LineupListener getListenerList(int i) {
        return listeners.get(i);
    }
    //======================================================
    public void addListener(LineupListener listener) {
        this.listeners.add(listener) ;
    }
    //======================================================
    public void removeListener(LineupListener listener){
        this.listeners.remove(listener);
    }
    //======================================================
    private void showErrorDialog(String message){
        if (message==null){
            message = "Please try again.\nIf this happened before check your internet connection.\nif you are connected to the internet and this message keeps appearing contact the app developer";
        }
        LinearLayout.LayoutParams params = (new LinearLayout.LayoutParams(90, 90));
        params.gravity = Gravity.CENTER;
        new LovelyInfoDialog(this.getContext())
                .setTopColorRes(R.color.Grey_blue)
                .setIcon(R.drawable.ic_info_white)
                .configureView(rootView -> rootView.findViewById(R.id.ld_icon).setLayoutParams(params))
                .setTitle("Error")
                .setMessage(message)
                .show();
    }
    //======================================================
}
