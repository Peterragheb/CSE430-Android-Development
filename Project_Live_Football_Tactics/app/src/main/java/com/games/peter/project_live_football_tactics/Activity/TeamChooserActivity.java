package com.games.peter.project_live_football_tactics.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Adapter.ApiAdapter;
import com.games.peter.project_live_football_tactics.Adapter.DialogListViewAdapter;
import com.games.peter.project_live_football_tactics.Class.DialogChoiceItem;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;
import com.games.peter.project_live_football_tactics.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamChooserActivity extends AppCompatActivity implements View.OnClickListener {
    //===============================================================================================================
    private TextView tv_teamchooser_league_name, tv_teamchooser_team_name;
    private ImageView iv_teamchooser_league_icon, iv_teamchooser_team_icon;
    private Button btn_teamchooser_finish;
    private ArrayList<DialogChoiceItem> team_list;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String selected_team;
    private int selected_team_id;


    //===============================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_chooser);
        initUIComponents();
        initComponents();
    }

    //======================================================
    private void initUIComponents() {
        tv_teamchooser_league_name = findViewById(R.id.tv_teamchooser_league_name);
        tv_teamchooser_team_name = findViewById(R.id.tv_teamchooser_team_name);
        btn_teamchooser_finish = findViewById(R.id.btn_teamchooser_finish);
        iv_teamchooser_league_icon = findViewById(R.id.iv_teamchooser_league_icon);
        iv_teamchooser_team_icon = findViewById(R.id.iv_teamchooser_team_icon);
        tv_teamchooser_league_name.setOnClickListener(this);
        tv_teamchooser_team_name.setOnClickListener(this);
        btn_teamchooser_finish.setOnClickListener(this);
    }
    //======================================================
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }
    //======================================================
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_teamchooser_league_name) {
            ArrayList<DialogChoiceItem> league_list = new ArrayList<>();
            league_list.add(new DialogChoiceItem(R.drawable.ic_spanish_flag, StaticStringsMethods.LA_LIGA));
            league_list.add(new DialogChoiceItem(R.drawable.ic_english_flag, StaticStringsMethods.PREMIER_LEAGUE));
            league_list.add(new DialogChoiceItem(R.drawable.ic_german_flag, StaticStringsMethods.BUNDES_LIGA));
            league_list.add(new DialogChoiceItem(R.drawable.ic_italian_flag, StaticStringsMethods.SERIA_A));
            league_list.add(new DialogChoiceItem(R.drawable.ic_french_flag, StaticStringsMethods.LIGUE_1));
            showLeagueChoiceDialog(league_list);
        } else if (v.getId() == R.id.tv_teamchooser_team_name) {
            if (team_list != null && !team_list.isEmpty())
                showTeamChoiceDialog(team_list);
        } else if (v.getId() == R.id.btn_teamchooser_finish) {
            if (selected_team!=null){
                updateUser();
                finish();
            }
        }
    }

    //======================================================
    private void showLeagueChoiceDialog(ArrayList<DialogChoiceItem> league_list) {
        LinearLayout.LayoutParams params = (new LinearLayout.LayoutParams(110, 110));
        params.gravity = Gravity.CENTER;
        final DialogListViewAdapter adapter = new DialogListViewAdapter(this, R.layout.item_simple_text, league_list);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.Grey_blue)
                .setTitle("League")
                .setIcon(R.drawable.ic_location)
                .setMessage("Choose the league your team plays in")
                .configureView(rootView -> rootView.findViewById(R.id.ld_icon).setLayoutParams(params))
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<DialogChoiceItem>() {
                    @Override
                    public void onItemSelected(int position, DialogChoiceItem item) {
                        tv_teamchooser_league_name.setText(item.getName());
                        iv_teamchooser_league_icon.setImageResource(item.getImageResource());
                        generateTeamList(item.getName());
                    }
                })
                .show();
    }
    //======================================================
    private void showTeamChoiceDialog(ArrayList<DialogChoiceItem> league_list) {
        LinearLayout.LayoutParams params = (new LinearLayout.LayoutParams(110, 110));
        params.gravity = Gravity.CENTER;
        final DialogListViewAdapter adapter = new DialogListViewAdapter(this, R.layout.item_simple_text, league_list);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.Grey_blue)
                .setTitle("Team")
                .setIcon(R.drawable.ic_team_logo)
                .setMessage("Choose your team")
                .configureView(rootView -> rootView.findViewById(R.id.ld_icon).setLayoutParams(params))
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<DialogChoiceItem>() {
                    @Override
                    public void onItemSelected(int position, DialogChoiceItem item) {
                        tv_teamchooser_team_name.setText(item.getName());
                        iv_teamchooser_team_icon.setImageResource(item.getImageResource());
                        selected_team = item.getName();
                        selected_team_id = item.getFavorite_team_id();
                    }
                })
                .show();
    }
    //======================================================
    private void generateTeamList(String league_name){
        String seasonid = ApiAdapter.getSeasonId(StaticStringsMethods.REQUEST_IN_LEAGUES +
                StaticStringsMethods.getLeagueId(league_name) +
                StaticStringsMethods.REQUEST_ALL_SEASONS +
                StaticStringsMethods.ACCESS_TOKEN_FIELD);
        if (seasonid!=null){
            team_list = ApiAdapter.getSeasonTeams(StaticStringsMethods.getLeagueIcon(league_name),
                    StaticStringsMethods.REQUEST_IN_SEASONS +
                            seasonid +
                            StaticStringsMethods.REQUEST_ALL_TEAMS +
                            StaticStringsMethods.ACCESS_TOKEN_FIELD);
        }
    }
    //======================================================
    private void updateUser() {
        if (mAuth!=null){
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if (mDatabase!=null){
                mDatabase.getReference().child("users").child(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> postValues = new HashMap<String,Object>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    postValues.put(snapshot.getKey(),snapshot.getValue());
                                }
                                postValues.put(StaticStringsMethods.FAVORITE_TEAM, selected_team);
                                postValues.put(StaticStringsMethods.FAVORITE_TEAM_ID, selected_team_id);
                                mDatabase.getReference().child("users").child(firebaseUser.getUid()).updateChildren(postValues);
                                MainActivity.full_data=true; //to tell the main activity that the user data is complete in the database
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
            }
            else
            {
                showErrorDialog(null);
            }
        }
        else
        {
            showErrorDialog(null);
        }
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

