package com.games.peter.project_live_football_tactics.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.games.peter.project_live_football_tactics.Adapter.ApiAdapter;
import com.games.peter.project_live_football_tactics.Adapter.DialogListViewAdapter;
import com.games.peter.project_live_football_tactics.Class.DialogChoiceItem;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;
import com.games.peter.project_live_football_tactics.Class.User;
import com.games.peter.project_live_football_tactics.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePictureTeamChooserActivity extends AppCompatActivity implements View.OnClickListener {
    //===============================================================================================================
    private CircleImageView civ_profilepictureteamchooser_profile_picture;
    private TextView tv_profilepictureteamchooser_league_name,tv_profilepictureteamchooser_team_name;
    private ImageView iv_profilepictureteamchooser_league_icon,iv_profilepictureteamchooser_team_icon;
    private EditText et_profilepictureteamchooser_username;
    private Button btn_profilepictureteamchooser_finish;
    private ArrayList<DialogChoiceItem> team_list;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorageRef;
    private String selected_team;
    private int selected_team_id;

    private Uri uriProfileImage;
    String profileImageUrl;
    //Static StaticStringsMethods
    private static final int CHOOSE_IMAGE = 101;
    private static final String ERROR_EMPTY_USERNAME = "Username can't be left blank";
    //===============================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture_team_chooser);
        initUIComponents();
        initComponents();
    }
    //======================================================
    private void initUIComponents(){
        civ_profilepictureteamchooser_profile_picture = findViewById(R.id.civ_profilepictureteamchooser_profile_picture);
        et_profilepictureteamchooser_username = findViewById(R.id.et_profilepictureteamchooser_username);
        tv_profilepictureteamchooser_league_name = findViewById(R.id.tv_profilepictureteamchooser_league_name);
        tv_profilepictureteamchooser_team_name = findViewById(R.id.tv_profilepictureteamchooser_team_name);
        btn_profilepictureteamchooser_finish = findViewById(R.id.btn_profilepictureteamchooser_finish);
        iv_profilepictureteamchooser_league_icon = findViewById(R.id.iv_profilepictureteamchooser_league_icon);
        iv_profilepictureteamchooser_team_icon = findViewById(R.id.iv_profilepictureteamchooser_team_icon);
        civ_profilepictureteamchooser_profile_picture.setOnClickListener(this);
        tv_profilepictureteamchooser_league_name.setOnClickListener(this);
        tv_profilepictureteamchooser_team_name.setOnClickListener(this);
        btn_profilepictureteamchooser_finish.setOnClickListener(this);
    }
    //======================================================
    private void initComponents(){
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        uriProfileImage = resourceToUri(this.getBaseContext(),R.drawable.ic_profile_picture);
    }
    //======================================================
    @Override
    public void onClick(View v) {
        if (v == civ_profilepictureteamchooser_profile_picture){
            if (isStoragePermissionGranted())
                ShowImageChooser();
        }
        else  if (v == tv_profilepictureteamchooser_league_name){
            ArrayList<DialogChoiceItem> league_list = new ArrayList<>();
            league_list.add(new DialogChoiceItem(R.drawable.ic_spanish_flag, StaticStringsMethods.LA_LIGA));
            league_list.add(new DialogChoiceItem(R.drawable.ic_english_flag, StaticStringsMethods.PREMIER_LEAGUE));
            league_list.add(new DialogChoiceItem(R.drawable.ic_german_flag, StaticStringsMethods.BUNDES_LIGA));
            league_list.add(new DialogChoiceItem(R.drawable.ic_italian_flag, StaticStringsMethods.SERIA_A));
            league_list.add(new DialogChoiceItem(R.drawable.ic_french_flag, StaticStringsMethods.LIGUE_1));
            showLeagueChoiceDialog(league_list);
        }
        else  if (v == tv_profilepictureteamchooser_team_name){
            if (team_list != null && !team_list.isEmpty())
                showTeamChoiceDialog(team_list);
        }
        else  if (v == btn_profilepictureteamchooser_finish){
            if (selected_team!=null) {
                perform_userDataUpdate();
            }
        }
    }
    //======================================================
    private void perform_userDataUpdate(){
        final String username = et_profilepictureteamchooser_username.getText().toString().trim();
        //===========================================================
        if (username.isEmpty()) {
            et_profilepictureteamchooser_username.setError(ERROR_EMPTY_USERNAME);
            et_profilepictureteamchooser_username.requestFocus();
            return;
        }
        //===========================================================
        else if (uriProfileImage==null){
            Toast.makeText(ProfilePictureTeamChooserActivity.this,"Please choose a profile picture",Toast.LENGTH_SHORT).show();
            return;
        }
        //===========================================================
        else{
            progressDialog.setMessage("Uploading Profile Picture");
            progressDialog.show();
            AddUserDataToFirebase(username);
        }
    }
    //======================================================
    private void ShowImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }
    //======================================================
    private void AddUserDataToFirebase(String username){
        if (mAuth!=null){
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if (firebaseUser != null){
                //add user profile pic to storage then add user to database
                UploadUserProfilePicToStorage(firebaseUser,username);
            }
        }
    }
    //======================================================
    private void addUsertoDatabase(FirebaseUser firebaseUser, String username){
        User user = new User(firebaseUser.getUid(),username,"profilepictures/"+firebaseUser.getUid());
        if (mDatabase!=null){
            mDatabase.getReference().child("users").child(firebaseUser.getUid()).setValue(user);
        }
        else
        {
            showErrorDialog(null);
        }
        if (profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            firebaseUser.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                updateUser(firebaseUser);
                                progressDialog.cancel();
                                MainActivity.full_data=true;
                                finish();
                            }
                            else{
                                progressDialog.cancel();
                                Toast.makeText(ProfilePictureTeamChooserActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            showErrorDialog(null);
        }
    }
    //======================================================
    private void UploadUserProfilePicToStorage(final FirebaseUser firebaseUser, final String username){
        if (mStorageRef!=null){
            StorageReference profileImageRef =
                    mStorageRef.child("profilepictures/" + firebaseUser.getUid() + ".jpg");
            if (profileImageRef!=null){
                if (uriProfileImage != null) {
                    profileImageRef.putFile(uriProfileImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                                    //add user to database with details id , name , profilePictureURL
                                    addUsertoDatabase(firebaseUser,username);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(ProfilePictureTeamChooserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(this,"Please choose a profile picture",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                showErrorDialog(null);
            }

        }
        else{
            showErrorDialog(null);
        }
    }
    //======================================================
    private void updateUser(FirebaseUser firebaseUser) {
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
    //======================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                civ_profilepictureteamchooser_profile_picture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //======================================================
    private void showLeagueChoiceDialog(ArrayList<DialogChoiceItem> league_list) {
        LinearLayout.LayoutParams params =(new LinearLayout.LayoutParams(110,110));
        params.gravity = Gravity.CENTER;
        final DialogListViewAdapter adapter = new DialogListViewAdapter(this,R.layout.item_simple_text, league_list);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.Grey_blue)
                .setTitle("League")
                .setIcon(R.drawable.ic_location)
                .setMessage("Choose the league your team plays in")
                .configureView(rootView -> rootView.findViewById(R.id.ld_icon).setLayoutParams(params))
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<DialogChoiceItem>() {
                    @Override
                    public void onItemSelected(int position, DialogChoiceItem item) {
                        tv_profilepictureteamchooser_league_name.setText(item.getName());
                        iv_profilepictureteamchooser_league_icon.setImageResource(item.getImageResource());
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
                        tv_profilepictureteamchooser_team_name.setText(item.getName());
                        iv_profilepictureteamchooser_team_icon.setImageResource(item.getImageResource());
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
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                Log.v("isPermissionGranted","Permission is granted");
                return true;
            } else {

                Log.v("isPermissionGranted","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("isPermissionGranted","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED){
            Log.v("onRequestPermissions","Permission: "+permissions[0]+ " was "+grantResults[0]+" Permission: "+permissions[1]+ "was "+grantResults[1]);
            //resume tasks needing this permission
            ShowImageChooser();
        }
    }
    //======================================================
    public static Uri resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID) );
    }
    //======================================================
}
