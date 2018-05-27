package com.games.peter.fragmenttransactiontest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn_swap;
    boolean is_a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_swap = findViewById(R.id.btn_swap);
        addDynamicFragment();
        btn_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment;
                if (is_a) {
                    is_a = false;
                    newFragment = new FragmentB();
                }
                else{
                    is_a = true;
                    newFragment = new FragmentA();
                }
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void addDynamicFragment() {
        // TODO Auto-generated method stub
        Fragment fg = new FragmentA();
        // adding fragment to relative layout by using layout id
        getSupportFragmentManager().beginTransaction().add(R.id.container, fg).commit();
        is_a = true;
    }

}
