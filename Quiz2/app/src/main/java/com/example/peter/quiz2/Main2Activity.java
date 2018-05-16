package com.example.peter.quiz2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button btn_signin;
    EditText et_email,et_password;
    TextView tv_message;
    final String TAG="Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_message = findViewById(R.id.tv_message);
        btn_signin = findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_email.getText().toString().trim().isEmpty()){
                    if (!et_password.getText().toString().trim().isEmpty()){
                        String email= et_email.getText().toString().trim();
                        String password = et_password.getText().toString().trim();
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            tv_message.setText("Login complete");
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            tv_message.setText("Login Failed");
                                        }
                                    }
                                });
                    }
                }

            }
        });

    }
}
