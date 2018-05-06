package com.games.peter.project_live_football_tactics.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Adapter.ApiAdapter;
import com.games.peter.project_live_football_tactics.Adapter.CustomMatchListAdapter;
import com.games.peter.project_live_football_tactics.Class.Match;
import com.games.peter.project_live_football_tactics.Class.MatchListUpdater;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;
import com.games.peter.project_live_football_tactics.R;
import com.games.peter.project_live_football_tactics.View.CustomMatchView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //===============================================================================================================
    private LinearLayout ll_league_matches1,ll_league_matches2,ll_league_matches3,ll_league_matches4,ll_league_matches5;
    private ImageView iv_league_flag1,iv_league_flag2,iv_league_flag3,iv_league_flag4,iv_league_flag5;
    private TextView tv_league_name1,tv_league_name2,tv_league_name3,tv_league_name4,tv_league_name5;
    private TextView tv_main_login;
    private ImageView iv_main_settings;
    private FirebaseAuth mAuth; // responsible of authenticating users
    public static ArrayList<Match> al_list1_matches,al_list2_matches,al_list3_matches,al_list4_matches,al_list5_matches;
    static int team_id;
    static String user_id;
    static String user_name;
    static boolean full_data=false;
    //===============================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUIComponents();
        initComponents();
    }
    //======================================================
    private void initUIComponents(){
        //Intent intent = getIntent();

        tv_main_login = findViewById(R.id.tv_main_login);
        iv_main_settings = findViewById(R.id.iv_main_settings);
        ll_league_matches1=findViewById(R.id.cv_league1_matches_list).findViewById(R.id.ll_league_matches);
        ll_league_matches2=findViewById(R.id.cv_league2_matches_list).findViewById(R.id.ll_league_matches);
        ll_league_matches3=findViewById(R.id.cv_league3_matches_list).findViewById(R.id.ll_league_matches);
        ll_league_matches4=findViewById(R.id.cv_league4_matches_list).findViewById(R.id.ll_league_matches);
        ll_league_matches5=findViewById(R.id.cv_league5_matches_list).findViewById(R.id.ll_league_matches);

        iv_league_flag1=findViewById(R.id.cv_league1_matches_list).findViewById(R.id.iv_league_flag);
        iv_league_flag2=findViewById(R.id.cv_league2_matches_list).findViewById(R.id.iv_league_flag);
        iv_league_flag3=findViewById(R.id.cv_league3_matches_list).findViewById(R.id.iv_league_flag);
        iv_league_flag4=findViewById(R.id.cv_league4_matches_list).findViewById(R.id.iv_league_flag);
        iv_league_flag5=findViewById(R.id.cv_league5_matches_list).findViewById(R.id.iv_league_flag);

        tv_league_name1=findViewById(R.id.cv_league1_matches_list).findViewById(R.id.tv_league_name);
        tv_league_name2=findViewById(R.id.cv_league2_matches_list).findViewById(R.id.tv_league_name);
        tv_league_name3=findViewById(R.id.cv_league3_matches_list).findViewById(R.id.tv_league_name);
        tv_league_name4=findViewById(R.id.cv_league4_matches_list).findViewById(R.id.tv_league_name);
        tv_league_name5=findViewById(R.id.cv_league5_matches_list).findViewById(R.id.tv_league_name);
        tv_main_login.setOnClickListener(this);
        iv_main_settings.setOnClickListener(this);
    }
    //======================================================
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
        al_list1_matches = new ArrayList<>();
        al_list2_matches = new ArrayList<>();
        al_list3_matches = new ArrayList<>();
        al_list4_matches = new ArrayList<>();
        al_list5_matches = new ArrayList<>();

        initRecycleView();
        periodicUpdate();

    }
    //======================================================
    @Override
    public void onStart() {//checks on start of application if user is already signed in
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //update UI ->Remove login and add settings
        updateUI(currentUser);
        getUserFavoriteTeam(currentUser);
    }
    //======================================================
    private void updateUI(FirebaseUser user){
        if (user != null) {
            iv_main_settings.setVisibility(View.VISIBLE);
            tv_main_login.setVisibility(View.GONE);
            user_id = user.getUid();
            user_name = user.getDisplayName();
            //make sure that the user info is in the database
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference userdata = mDatabase.getReference().child("users").child(user.getUid());
            //otherwise ask him to choose a profile pic & favorite team
            userdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        startActivity(new Intent(MainActivity.this,ProfilePictureTeamChooserActivity.class));
                        full_data=false;
                    }
                    else
                    full_data=true;
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        else{
            iv_main_settings.setVisibility(View.GONE);
            tv_main_login.setVisibility(View.VISIBLE);
            team_id = 0;
        }

    }
    //======================================================
    private void initRecycleView(){

        Calendar c= Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        //TODO remove this
        //c.add(Calendar.DATE, -4); //will be removed this adds 5 days only to test on a day that has matches on it
        String datefrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(c.getTime());
        Log.v("DateFrom", datefrom);
        c.add(Calendar.DATE, 1);
        String dateto = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(c.getTime());
        Log.v("DateTo", dateto);
        CustomMatchListAdapter customMatchListAdapter = null;
        al_list1_matches = generateLeagueList(StaticStringsMethods.PREMIER_LEAGUE,datefrom,dateto,customMatchListAdapter,ll_league_matches1,iv_league_flag1,tv_league_name1);
        al_list2_matches = generateLeagueList(StaticStringsMethods.LA_LIGA,datefrom,dateto,customMatchListAdapter,ll_league_matches2,iv_league_flag2,tv_league_name2);
        al_list3_matches = generateLeagueList(StaticStringsMethods.BUNDES_LIGA,datefrom,dateto,customMatchListAdapter,ll_league_matches3,iv_league_flag3,tv_league_name3);
        al_list4_matches = generateLeagueList(StaticStringsMethods.SERIA_A,datefrom,dateto,customMatchListAdapter,ll_league_matches4,iv_league_flag4,tv_league_name4);
        al_list5_matches = generateLeagueList(StaticStringsMethods.LIGUE_1,datefrom,dateto,customMatchListAdapter,ll_league_matches5,iv_league_flag5,tv_league_name5);
        if ((al_list1_matches==null)||(al_list2_matches==null)||(al_list3_matches==null)||(al_list4_matches==null)||(al_list5_matches==null)){
            showErrorDialog("Couldn't fetch data.\nPlease check your internet connection and restart the application");
        }

    }
    //======================================================
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_main_login){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        else  if (v.getId()==R.id.iv_main_settings){
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
        }
        else
        {
            CustomMatchView ll_match=findViewById(v.getId());
            String fixture_id = ll_match.getFixtureid();
            TextView home_team = ll_match.findViewById(R.id.tv_match_home_team);
            TextView away_team = ll_match.findViewById(R.id.tv_match_away_team);
            Intent intent=new Intent(MainActivity.this,MatchActivity.class);
            //intent.putExtra("HOME_TEAM_NAME",home_team.getText().toString());
            //intent.putExtra("AWAY_TEAM_NAME",away_team.getText().toString());
            Match match = null;
            ArrayList<Integer> match_List_id = findMatchFromList(fixture_id);
            if (match_List_id!=null&&!match_List_id.isEmpty()){
                intent.putExtra("list",match_List_id.get(0)); //Matchlist number
                intent.putExtra("match",match_List_id.get(1)); //match index in list
                match =getMatchId(match_List_id.get(0),match_List_id.get(1)); //get match from list using list number and match index
                //if (team_id!=0 && match!=null){//make sure we know the user's favorite team before starting match activity
                    if (match!=null){//make sure we know the user's favorite team before starting match activity
                    intent.putExtra("away_team_id",Integer.valueOf(match.getId_team_season_away())); //send match away team id
                    intent.putExtra("home_team_id",Integer.valueOf(match.getId_team_season_home()));//send match home team id
                    intent.putExtra("away_team_name",match.getTeam_season_away_name()); //send match away team name
                    intent.putExtra("home_team_name",match.getTeam_season_home_name());//send match home team name
                    intent.putExtra("fixture_id",fixture_id); //send fixture id
                    intent.putExtra("user_id",user_id); //send user id  from firebase
                    intent.putExtra("user_name",user_name); //send username from firebase
                    intent.putExtra("team_id",team_id); // send favorite team id from firebase
                    startActivity(intent);
                }

            }
            else
                showErrorDialog(null);
        }

    }
    //======================================================
    private String getSeasonId(String league_name ){
        return ApiAdapter.getSeasonId(StaticStringsMethods.REQUEST_IN_LEAGUES +
                StaticStringsMethods.getLeagueId(league_name) +
                StaticStringsMethods.REQUEST_ALL_SEASONS +
                StaticStringsMethods.ACCESS_TOKEN_FIELD);

    }
    //======================================================
    private ArrayList<Match> getFixtures(String season_id,String datefrom,String dateto){
        return ApiAdapter.getFixtures(StaticStringsMethods.REQUEST_IN_SEASONS+
                season_id+
                StaticStringsMethods.REQUEST_FIXTURES+
                StaticStringsMethods.DATE_FROM_FIELD+
                datefrom+
                "&"+
                StaticStringsMethods.DATE_TO_FIELD+
                dateto+"&"+StaticStringsMethods.ACCESS_TOKEN_FIELD);
    }
    //======================================================
    private ArrayList<Match> generateLeagueList(String league ,String datefrom,String dateto,CustomMatchListAdapter customMatchListAdapter,LinearLayout layout,ImageView iv_league_flag,TextView tv_league_name){
        String league_seasonid = getSeasonId(league);
        if (league_seasonid!=null){
            ArrayList<Match> matches= getFixtures(league_seasonid,datefrom,dateto);
            if (matches!=null){
                matches = Match.sortMatchesBy_ScheduledDate(matches);
                customMatchListAdapter=new CustomMatchListAdapter(this,R.layout.match_row_details,matches);
                iv_league_flag.setImageResource(StaticStringsMethods.getLeagueIcon(league));
                tv_league_name.setText(league);
                //TODO REMOVE BELOW
                //matches.get(0).setLineup_confirmed("no");
                for(int i = 0 ; i < customMatchListAdapter.getCount(); i++){
                    layout.addView(customMatchListAdapter.getView(i, null, layout));
                    layout.getChildAt(i).setId(View.generateViewId());
                    layout.getChildAt(i).setOnClickListener(this);
                    matches.get(i).addListener((CustomMatchView)layout.getChildAt(i));
                    ((CustomMatchView) layout.getChildAt(i)).setFixtureid(matches.get(i).getFixture_id());
                }
                return matches;
            }
        }
      return null;
    }
    //======================================================
    private void compareandUpdateList(ArrayList<Match> original_list,ArrayList<Match> updated_list){
        if ((original_list!=null )&&(updated_list!=null)){//make sure the original list is not null nor the updated one
            Log.v("compareandUpdateList","WILLCOMPARE");
//        if (original_list == al_list2_matches)
//            updated_list.set(2,new Match(al_list2_matches.get(2).getFixture_id(),al_list2_matches.get(2).getId_season(),al_list2_matches.get(2).getFixture_status_short(),al_list2_matches.get(2).getId_team_season_away(),al_list2_matches.get(2).getId_team_season_home(),al_list2_matches.get(2).getNumber_goal_team_away(),"1",al_list2_matches.get(2).getSchedule_date(),al_list2_matches.get(2).getTeam_season_away_name(),al_list2_matches.get(2).getTeam_season_home_name(),al_list2_matches.get(2).getLineup_confirmed(),"20"));
            for (int i=0;i<original_list.size();i++){
                Log.v("compareandUpdateList","updated : "+updated_list.get(i).getElapsed()+" original : "+original_list.get(i).getElapsed());

                //==================================================
                if (!updated_list.get(i).getFixture_id().equals(original_list.get(i).getFixture_id()))
                    original_list.get(i).setFixture_id(updated_list.get(i).getFixture_id());
                //==================================================
                if (!updated_list.get(i).getId_season().equals(original_list.get(i).getId_season()))
                    original_list.get(i).setId_season((updated_list.get(i).getId_season()));
                //==================================================
                if (!updated_list.get(i).getFixture_status_short().equals(original_list.get(i).getFixture_status_short()))
                    original_list.get(i).setFixture_status_short((updated_list.get(i).getFixture_status_short()));
                //==================================================
                if (!updated_list.get(i).getId_team_season_away().equals(original_list.get(i).getId_team_season_away()))
                    original_list.get(i).setId_team_season_away(updated_list.get(i).getId_team_season_away());
                //==================================================
                if (!updated_list.get(i).getId_team_season_home().equals(original_list.get(i).getId_team_season_home()))
                    original_list.get(i).setId_team_season_home(updated_list.get(i).getId_team_season_home());
                //==================================================
                if (!updated_list.get(i).getNumber_goal_team_away().equals(original_list.get(i).getNumber_goal_team_away()))
                    original_list.get(i).setNumber_goal_team_away(updated_list.get(i).getNumber_goal_team_away());
                //==================================================
                if (!updated_list.get(i).getNumber_goal_team_home().equals(original_list.get(i).getNumber_goal_team_home()))
                    original_list.get(i).setNumber_goal_team_home(updated_list.get(i).getNumber_goal_team_home());
                //==================================================
                if (!updated_list.get(i).getSchedule_date().equals(original_list.get(i).getSchedule_date()))
                    original_list.get(i).setSchedule_date(updated_list.get(i).getSchedule_date());
                //==================================================
                if (!updated_list.get(i).getTeam_season_away_name().equals(original_list.get(i).getTeam_season_away_name()))
                    original_list.get(i).setTeam_season_away_name(updated_list.get(i).getTeam_season_away_name());
                //==================================================
                if (!updated_list.get(i).getTeam_season_home_name().equals(original_list.get(i).getTeam_season_home_name()))
                    original_list.get(i).setTeam_season_home_name(updated_list.get(i).getTeam_season_home_name());
                //==================================================
                if (!updated_list.get(i).getLineup_confirmed().equals(original_list.get(i).getLineup_confirmed()))
                    original_list.get(i).setLineup_confirmed(updated_list.get(i).getLineup_confirmed());
                //==================================================
                if (!updated_list.get(i).getElapsed().equals(original_list.get(i).getElapsed())){
                    Log.v("Update_match","UPDATEDDD!!!!!!!");
                    original_list.get(i).setElapsed(updated_list.get(i).getElapsed());
                }

                //==================================================
            }
        }

    }
    //======================================================
    private void periodicUpdate(){
        if ((al_list1_matches!=null )&&
                (al_list2_matches!=null)&&
                (al_list3_matches!=null )&&
                (al_list4_matches!=null )&&
                (al_list5_matches!=null )){//make sure matchlists are not null
            MatchListUpdater matchListUpdater =new MatchListUpdater(al_list1_matches,al_list2_matches,al_list3_matches,al_list4_matches,al_list5_matches);
            Timer timer = new Timer ();
            TimerTask hourlyTask = new TimerTask () {
                @Override
                public void run () {
                    matchListUpdater.run();
                    while (!matchListUpdater.isFnished())
                    {

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            compareandUpdateList(al_list1_matches,matchListUpdater.getAl_list1_matches());
                            compareandUpdateList(al_list2_matches,matchListUpdater.getAl_list2_matches());
                            compareandUpdateList(al_list3_matches,matchListUpdater.getAl_list3_matches());
                            compareandUpdateList(al_list4_matches,matchListUpdater.getAl_list4_matches());
                            compareandUpdateList(al_list5_matches,matchListUpdater.getAl_list5_matches());

                        }
                    });
                }
            };
            //timer.schedule (hourlyTask, 0l, 1000*60*1);//every 1 min
            timer.schedule (hourlyTask, 0l, 1000*30);//every 30 secs
        }

    }
    //======================================================
    public ArrayList<Integer> findMatchFromList(String fixture_id){
        ArrayList<Integer> returnValue=new ArrayList<>();
        if (al_list1_matches!=null){
            for (int i=0;i<al_list1_matches.size();i++){
                if (al_list1_matches.get(i).getFixture_id().equals(fixture_id))
                {
                    returnValue.add(1);
                    returnValue.add(i);
                    return returnValue;
                }
            }
        }
        if (al_list2_matches!=null){
            for (int i=0;i<al_list2_matches.size();i++){
                if (al_list2_matches.get(i).getFixture_id().equals(fixture_id))
                {
                    returnValue.add(2);
                    returnValue.add(i);
                    return returnValue;
                }
            }
        }

        if (al_list3_matches!=null){
            for (int i=0;i<al_list3_matches.size();i++){
                if (al_list3_matches.get(i).getFixture_id().equals(fixture_id))
                {
                    returnValue.add(3);
                    returnValue.add(i);
                    return returnValue;
                }
            }
        }

        if (al_list4_matches!=null){
            for (int i=0;i<al_list4_matches.size();i++){
                if (al_list4_matches.get(i).getFixture_id().equals(fixture_id))
                {
                    returnValue.add(4);
                    returnValue.add(i);
                    return returnValue;
                }
            }
        }

        if (al_list5_matches!=null){
            for (int i=0;i<al_list5_matches.size();i++){
                if (al_list5_matches.get(i).getFixture_id().equals(fixture_id))
                {
                    returnValue.add(5);
                    returnValue.add(i);
                    return returnValue;
                }
            }
        }
        return returnValue;
    }
    //======================================================
    protected void getUserFavoriteTeam(FirebaseUser currentUser){
        //get user's favorite team from database
        if (currentUser!=null&full_data){
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference userdata = mDatabase.getReference().child("users").child(currentUser.getUid());
            if (userdata.child(StaticStringsMethods.FAVORITE_TEAM_ID)!=null){
                userdata.child(StaticStringsMethods.FAVORITE_TEAM_ID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v("Fav_Team_Id ","     Team Name :     "+dataSnapshot.getValue(Integer.class));
                        if (dataSnapshot.getValue()!=null) {
                            team_id = dataSnapshot.getValue(Integer.class);
                        }
                        else
                        {
                            Log.v("dataSnapshot_IS","null");
                            startActivity(new Intent(MainActivity.this,TeamChooserActivity.class));
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else {
                Log.v("NO_FAV_TEAM","YES");
                startActivity(new Intent(MainActivity.this,TeamChooserActivity.class));
            }
        }

    }
    //======================================================
    private Match getMatchId(int list_id,int match_id){
        Match match =null;
            if (list_id==1){
                match=al_list1_matches.get(match_id);
            }else if (list_id==2){
                match=al_list2_matches.get(match_id);
            }
            else if (list_id==3){
                match=al_list3_matches.get(match_id);
            }
            else if (list_id==4){
                match=al_list4_matches.get(match_id);
            }
            else if (list_id==5){
                match=al_list5_matches.get(match_id);

            }
            Log.v("RETURNED_MATCH",match.getId_team_season_away());
        return match;
    }
    //======================================================
    private void showErrorDialog(String message){
        if (message==null){
            message = "Please try again.\nIf this happened before check your internet connection.\nif you are connected to the internet and this message keeps appearing contact the app developer";
        }
        LinearLayout.LayoutParams params = (new LinearLayout.LayoutParams(90, 90));
        params.gravity = Gravity.CENTER;
        new LovelyInfoDialog(this)
                .setTopColorRes(R.color.Grey_blue)
                .setIcon(R.drawable.ic_info_white)
                .configureView(rootView -> rootView.findViewById(R.id.ld_icon).setLayoutParams(params))
                .setTitle("Error")
                .setMessage(message)
                .show();
    }
    //======================================================
}
