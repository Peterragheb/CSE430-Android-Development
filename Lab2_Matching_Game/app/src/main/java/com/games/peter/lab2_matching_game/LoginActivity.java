package com.games.peter.lab2_matching_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class LoginActivity extends AppCompatActivity {
    EditText et_username;
    Button btn_login;
    DatabaseHandler dbHandler;
    public static String USERNAME_MESSAGE="username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username=findViewById(R.id.et_username);
        btn_login=findViewById(R.id.btn_login);
        dbHandler=new DatabaseHandler(this);
        //dbHandler.deleteAll();
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

    }

}
