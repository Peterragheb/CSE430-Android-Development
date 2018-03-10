package com.games.peter.lab2_matching_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText et_username;
    Button btn_login;
    DatabaseHandler mydb;
    public static String USERNAME_MESSAGE="username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //=====================================
        //assign variables
        et_username=findViewById(R.id.et_username);
        btn_login=findViewById(R.id.btn_login);
        //=====================================

        //Delete all users
        //deleteall();

        //=====================================
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_username.getText().toString().isEmpty()){
                    String username=et_username.getText().toString();
                    //dbHandler.insertUser(username,null);
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra(USERNAME_MESSAGE,username);
                    startActivity(intent);
                    finish();
                }
            }
        });
        //======================================

    }


    //delete allusers
    private void deleteall(){
        openDB();
        mydb.deleteAll();
    }


    //open database
    private void openDB(){
        mydb=new DatabaseHandler(this);
        mydb.open();
    }


    //close database
    private void closeDB(){
        if (mydb!=null)
            mydb.close();
    }


    //on activity destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

}
