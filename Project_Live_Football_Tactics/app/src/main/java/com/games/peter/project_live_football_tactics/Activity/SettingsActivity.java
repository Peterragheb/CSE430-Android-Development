package com.games.peter.project_live_football_tactics.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;
import com.games.peter.project_live_football_tactics.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    //===============================================================================================================
    CircleImageView civ_settings_profile_picture;
    TextView tv_settings_item_username, tv_settings_item_email, tv_settings_item_favorite_team;
    LinearLayout ll_settings_logout,ll_settings_about;
    //ListView lv_settings;
    private FirebaseAuth mAuth; // responsible of authenticating users
    //===============================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUIComponents();
        initComponents();
    }
    //======================================================
    private void initUIComponents(){
        civ_settings_profile_picture = findViewById(R.id.civ_settings_profile_picture);
        tv_settings_item_username = findViewById(R.id.tv_settings_item_username);
        tv_settings_item_email = findViewById(R.id.tv_settings_item_email);
        tv_settings_item_favorite_team = findViewById(R.id.tv_settings_item_favorite_team);
        ll_settings_logout = findViewById(R.id.ll_settings_logout);
        ll_settings_about = findViewById(R.id.ll_settings_about);
        ll_settings_logout.setOnClickListener(this);
        ll_settings_about.setOnClickListener(this);
       // lv_settings = findViewById(R.id.lv_settings);
//        ArrayList<SettingsItem> settingsItems = new ArrayList<>();
//        settingsItems.add(new SettingsItem("About",R.drawable.ic_about));
//        settingsItems.add(new SettingsItem("Logout",R.drawable.ic_logout));
//        ListViewAdapter listViewAdapter = new ListViewAdapter(this,R.layout.settings_item,settingsItems);
//        lv_settings.setAdapter(listViewAdapter);
//        lv_settings.setOnItemClickListener(this);
    }
    //======================================================
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
    }
    //======================================================
    @Override
    public void onStart() {//checks on start of application if user is already signed in
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth!=null){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            //update UI ->redirect to home screen with username for example
            updateUI(currentUser);
        }
        else
        {
            showErrorDialog(null);
        }

    }
    //======================================================
    private void updateUI(FirebaseUser user){
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(civ_settings_profile_picture);
            }
            if (name != null) {
                tv_settings_item_username.setText(name);
            }
            if (email != null) {
                tv_settings_item_email.setText(email);
            }
            if ((user.getPhotoUrl() == null)|| (name == null)||(email == null)){//incase user info were null
                Toast.makeText(this,"Some info couldn't be displayed",Toast.LENGTH_SHORT).show();
            }
            //get user's favorite team from database
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            if (mDatabase!=null){
                DatabaseReference userdata = mDatabase.getReference().child("users").child(user.getUid());
                if (userdata!=null){
                    if (userdata.child(StaticStringsMethods.FAVORITE_TEAM)!=null){
                        userdata.child(StaticStringsMethods.FAVORITE_TEAM).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.v("Fav Team ","     Team Name :     "+dataSnapshot.getValue(String.class));
                                String team = dataSnapshot.getValue(String.class);
                                tv_settings_item_favorite_team.setText(team);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    { // for team name
                        Toast.makeText(this,"Some info couldn't be displayed",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {   // for team name
                    Toast.makeText(this,"Some info couldn't be displayed",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {   // for team name
                Toast.makeText(this,"Some info couldn't be displayed",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            showErrorDialog(null);
        }

    }
    //======================================================
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (position==0){
//
//        }
//        else if (position==1){
//            mAuth.signOut();
//            //startActivity(new Intent(SettingsActivity.this,MainActivity.class));
//            finish();
//        }
//    }
    //======================================================
    @Override
    public void onClick(View v) {
        if (v == ll_settings_about){
            showAboutDialog();
        }
        else if (v == ll_settings_logout){
            mAuth.signOut();
            finish();
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
    private void showAboutDialog(){

        LinearLayout.LayoutParams params = (new LinearLayout.LayoutParams(90, 90));
        params.gravity = Gravity.CENTER;
        new LovelyInfoDialog(this)
                .setTopColorRes(R.color.Grey_blue)
                .setIcon(R.drawable.ic_info_white)
                .configureView(rootView -> rootView.findViewById(R.id.ld_icon).setLayoutParams(params))
                .setTitle("About")
                .setMessage("Live scores and push notifications, for the top leagues around the world.\nAs well as being a live score app, Foottalks is changing the face of football by liberating and sharing the collective opinion of fans from all over the world using real-time chats.\n\nThis is an ALPHA version if you encountered any bug or crash please don't hesitate to contact the application developer.\n\nNOTE:This Application is for football addicts only.")
                .show();
    }
    //======================================================
}
