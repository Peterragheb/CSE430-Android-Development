package com.games.peter.project_live_football_tactics.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.games.peter.project_live_football_tactics.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //===============================================================================================================
    private EditText et_login_email,et_login_password;
    private TextView tv_login_signup;
    private Button btn_login_login;
    private FirebaseAuth mAuth; // responsible of authenticating users
    private ProgressDialog progressDialog;
    //Static StaticStringsMethods
    private final String ERROR_EMPTY_EMAIL = "Email can't be left blank";
    private final String ERROR_EMPTY_PASSWORD = "Password can't be left blank";
    private final String LOGIN_USER = "Logging In...";
    private final String SUCCESSFULL_LOGIN = "Successfully logged in";
    private final String ERROR_LOGIN = "Error logging in , try again later";
    private final String ERROR_USER_NOTFOUND = "This accound doesn't exist";
    private final String ERROR_INVALID_EMAIL = "Invalid e-mail address";
    private final String ERROR_INCORRECT_PASSWORD = "Password is incorrect";
    private final String ERROR_EMAIL_ALREADY_USED = "This email address is already in use by another account";
    private final String ERROR_WEAK_PASSWORD = "The password is invalid it must be 6 characters at least";

    //===============================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        initUIComponents();

    }
    //======================================================
    private void initUIComponents(){
        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);
        tv_login_signup = findViewById(R.id.tv_login_signup);
        btn_login_login = findViewById(R.id.btn_login_login);
        btn_login_login.setOnClickListener(this);
        tv_login_signup.setOnClickListener(this);
    }
    //======================================================
    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_login_login){
            perform_login();
        }
        else if (v.getId()==R.id.tv_login_signup){
            startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            finish();
        }
    }
    //======================================================
    private void perform_login(){
        //===========================================================
        String email = et_login_email.getText().toString().trim();
        String password = et_login_password.getText().toString().trim();
        //===========================================================
        if (email.isEmpty()) {
            et_login_email.setError(ERROR_EMPTY_EMAIL);
            et_login_email.requestFocus();
            return;
        }
        //===========================================================
        else if (password.isEmpty()) {
            et_login_password.setError(ERROR_EMPTY_PASSWORD);
            et_login_password.requestFocus();
            return;
        }
        //===========================================================
        else {
            progressDialog.setMessage(LOGIN_USER);
            progressDialog.show();
            //===========================================================
            if (mAuth!=null){
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//try to login in with email and password
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//if logged in successfully
                            progressDialog.cancel();
                            //Toast.makeText(LoginActivity.this, SUCCESSFULL_LOGIN, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        //===========================================================
                        else {
                            if (task.getException().getClass().equals(FirebaseAuthException.class)){
                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                progressDialog.cancel();
                                //===========================================================
                                if (!error_handler(e)) {
                                    Toast.makeText(LoginActivity.this, ERROR_LOGIN, Toast.LENGTH_SHORT).show();//in case error was not catched a default error handler is inserted
                                    Log.e("Login", "Failed Log-in", e);//unhandled error logged
                                }
                                //===========================================================
                            }
                            else if (task.getException().getClass().equals(FirebaseNetworkException.class)) {
                                progressDialog.cancel();
                                showErrorDialog();
                            }
                            else
                                showErrorDialog();
                        }
                    }
                });
            }
            else
                showErrorDialog();
        }
    }
    //======================================================
    private Boolean error_handler(FirebaseAuthException e) {//Handles all Firebase Authentication errors
        String errorCode = e.getErrorCode();
        Boolean entered = false;
        switch (errorCode) {
            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(LoginActivity.this, "The custom token format is incorrect. Please contact the app developer", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(LoginActivity.this, "The custom token corresponds to a different audience.Please contact the app developer", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(LoginActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_INVALID_EMAIL":
                et_login_email.setError(ERROR_INVALID_EMAIL);
                et_login_email.requestFocus();
                entered = true;
                break;
            //===========================================================
            case "ERROR_WRONG_PASSWORD":
                et_login_password.setError(ERROR_INCORRECT_PASSWORD);
                et_login_password.requestFocus();
                et_login_password.setText("");
                entered = true;
                break;
            //===========================================================
            case "ERROR_USER_MISMATCH":
                Toast.makeText(LoginActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(LoginActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(LoginActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_EMAIL_ALREADY_IN_USE":
                et_login_email.setError(ERROR_EMAIL_ALREADY_USED);
                et_login_email.requestFocus();
                entered = true;
                break;
            //===========================================================
            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(LoginActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_USER_DISABLED":
                Toast.makeText(LoginActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(LoginActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(LoginActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(LoginActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(LoginActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                entered = true;
                break;
            //===========================================================
            case "ERROR_WEAK_PASSWORD":
                et_login_password.setError(ERROR_WEAK_PASSWORD);
                et_login_password.requestFocus();
                entered = true;
                break;
            //===========================================================
        }
        return entered;
    }
    //======================================================
    private void showErrorDialog(){
        LinearLayout.LayoutParams params = (new LinearLayout.LayoutParams(90, 90));
        params.gravity = Gravity.CENTER;
        new LovelyInfoDialog(this)
                .setTopColorRes(R.color.Grey_blue)
                .setIcon(R.drawable.ic_info_white)
                .configureView(rootView -> rootView.findViewById(R.id.ld_icon).setLayoutParams(params))
                .setTitle("Error")
                .setMessage("Please check your internet connection.\nif you are connected to the internet and this message keeps appearing contact the app developer")
                .show();
    }
    //======================================================
}
