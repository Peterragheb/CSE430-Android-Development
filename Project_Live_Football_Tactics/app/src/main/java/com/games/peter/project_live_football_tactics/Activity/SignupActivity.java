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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.games.peter.project_live_football_tactics.Class.User;
import com.games.peter.project_live_football_tactics.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    //===============================================================================================================
    EditText et_signup_username,et_signup_email,et_signup_password,et_signup_confirm_password;
    CircleImageView civ_signup_profile_picture;
    Button btn_signup_signup;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorageRef;

    private Uri uriProfileImage;
    private String profileImageUrl;
    //Static StaticStringsMethods
    private static final int CHOOSE_IMAGE = 101;
    private static final String ERROR_EMPTY_USERNAME = "Username can't be left blank";
    private static final String ERROR_EMPTY_EMAIL = "Email can't be left blank";
    private static final String ERROR_INVALID_EMAIL = "Invalid e-mail address";
    private static final String ERROR_EMPTY_PASSWORD = "Password can't be left blank";
    private static final String ERROR_WEAK_PASSWORD = "The password is invalid it must be 6 characters at least";
    private static final String ERROR_PASSWORDS_MISMATCH = "Passwords do not match";
    private static final String SIGNUP_USER = "Signing Up...";
    private static final String SUCCESSFULL_SIGNUP = "Registered Successfully";
    private static final String ERROR_SIGNUP = "Error Registering user , try again later";
    private final String ERROR_EMAIL_ALREADY_USED = "This email address is already taken by another account";
    private static final String ERROR_INCORRECT_PASSWORD = "Password is incorrect";
    //===============================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initUIComponents();
        initComponents();
    }
    //======================================================
    private void initUIComponents(){
        et_signup_username = findViewById(R.id.et_signup_username);
        et_signup_email = findViewById(R.id.et_signup_email);
        et_signup_password = findViewById(R.id.et_signup_password);
        et_signup_confirm_password = findViewById(R.id.et_signup_confirm_password);
        civ_signup_profile_picture = findViewById(R.id.civ_signup_profile_picture);
        btn_signup_signup = findViewById(R.id.btn_signup_signup);
        civ_signup_profile_picture.setOnClickListener(this);
        btn_signup_signup.setOnClickListener(this);
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
        if (v.getId()==R.id.btn_signup_signup){
            //startActivity(new Intent(SignupActivity.this,TeamChooserActivity.class));
            //finish();
            perform_Signup();
        }
        else if (v.getId()==R.id.civ_signup_profile_picture){
            if (isStoragePermissionGranted())
                ShowImageChooser();

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
    private void perform_Signup(){
        final String username = et_signup_username.getText().toString().trim();
        String email = et_signup_email.getText().toString().trim();
        String password = et_signup_password.getText().toString().trim();
        String confirm_password = et_signup_confirm_password.getText().toString().trim();
        //===========================================================
        if (username.isEmpty()) {
            et_signup_username.setError(ERROR_EMPTY_USERNAME);
            et_signup_username.requestFocus();
            return;
        }
        //===========================================================
        else if (email.isEmpty()) {
            et_signup_email.setError(ERROR_EMPTY_EMAIL);
            et_signup_email.requestFocus();
            return;
        }
        //===========================================================
        else if (password.isEmpty()) {
            et_signup_password.setError(ERROR_EMPTY_PASSWORD);
            et_signup_password.requestFocus();
            return;
        }
        //===========================================================
        else if (!password.equals(confirm_password)) {
            et_signup_password.setError(ERROR_PASSWORDS_MISMATCH);
            et_signup_password.requestFocus();
            et_signup_password.setText("");
            et_signup_confirm_password.setText("");
            return;
        }
        else if (uriProfileImage==null){
            Toast.makeText(SignupActivity.this,"Please choose a profile picture",Toast.LENGTH_SHORT).show();
            return;
        }
        //===========================================================
        else{
            if (mAuth !=null){
                progressDialog.setMessage(SIGNUP_USER);
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//account creation
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//if account created successfully

                            //Add user to firebase
                            AddUserDataToFirebase(username);

                        }
                        //===========================================================
                        else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            }catch (FirebaseNetworkException e){
                                Log.v("SIGNUP_ERROR","NETWORK");
                                progressDialog.cancel();
                                showErrorDialog("Please check your internet connection.\nif you are connected to the internet and this message keeps appearing contact the app developer");
                            }catch (FirebaseException e){
                                progressDialog.cancel();
                                try {
                                    Log.v("SIGNUP_ERROR","AUTH");
                                    if (!error_handler((FirebaseAuthException) e)) {
                                        Toast.makeText(SignupActivity.this, ERROR_SIGNUP, Toast.LENGTH_SHORT).show();//in case error was not catched a default error handler is inserted
                                        Log.e("Signup", "Failed Signup", e);//unhandled error logged
                                    }
                                }
                                catch (ClassCastException ex){
                                    Log.v("Error",ex.getMessage());
                                }
                            }
                            catch (Exception e) {
                                Log.v("SIGNUP_ERROR","ELSE");
                                progressDialog.cancel();
                                Toast.makeText(SignupActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                        //===========================================================
                    }
                });
            }
            else
                showErrorDialog(null);
            }

    }
    //======================================================
    private Boolean error_handler(FirebaseAuthException e) {//Handles all Firebase Authentication errors
        String errorCode = e.getErrorCode();
        Boolean entered = false;
        switch (errorCode) {
            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(SignupActivity.this, "The custom token format is incorrect. Please contact the app developer", Toast.LENGTH_LONG).show();
                entered = true;
            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(SignupActivity.this, "The custom token corresponds to a different audience.Please contact the app developer", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(SignupActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_INVALID_EMAIL":
                et_signup_email.setError(ERROR_INVALID_EMAIL);
                et_signup_email.requestFocus();
                entered = true;
                break;
            case "ERROR_WRONG_PASSWORD":
                et_signup_password.setError(ERROR_INCORRECT_PASSWORD);
                et_signup_password.requestFocus();
                et_signup_password.setText("");
                entered = true;
                break;
            case "ERROR_USER_MISMATCH":
                Toast.makeText(SignupActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(SignupActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(SignupActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_EMAIL_ALREADY_IN_USE":
                et_signup_email.setError(ERROR_EMAIL_ALREADY_USED);
                et_signup_email.requestFocus();
                entered = true;
                break;
            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(SignupActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_USER_DISABLED":
                Toast.makeText(SignupActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(SignupActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(SignupActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(SignupActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(SignupActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            case "ERROR_WEAK_PASSWORD":
                et_signup_password.setError(ERROR_WEAK_PASSWORD);
                et_signup_password.requestFocus();
                entered = true;
                break;
        }
        return entered;
    }
    //======================================================
    private void AddUserDataToFirebase(String username){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null){
            //add user profile pic to storage then add user to database
            UploadUserProfilePicToStorage(firebaseUser,username);
        }
    }
    //======================================================
    private void addUsertoDatabase(FirebaseUser firebaseUser, String username){
        if (firebaseUser!=null){
            User user = new User(firebaseUser.getUid(),username,"profilepictures/"+firebaseUser.getUid());
            mDatabase.getReference().child("users").child(firebaseUser.getUid()).setValue(user);
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
                                    progressDialog.cancel();
                                    //MainActivity.full_data = true;
                                    Toast.makeText(SignupActivity.this, SUCCESSFULL_SIGNUP, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignupActivity.this, TeamChooserActivity.class));
                                    finish();
                                }
                                else{
                                    progressDialog.cancel();
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
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
    private void UploadUserProfilePicToStorage(final FirebaseUser firebaseUser, final String username){
        if (mStorageRef!=null){
            StorageReference profileImageRef =
                    mStorageRef.child("profilepictures/" + firebaseUser.getUid() + ".jpg");

            if (uriProfileImage != null) {
                progressDialog.setMessage("Uploading Profile Picture...");//update progress dialog message
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
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                civ_signup_profile_picture.setImageBitmap(bitmap);

                //uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
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
    //======================================================
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
