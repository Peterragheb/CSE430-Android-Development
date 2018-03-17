package com.games.peter.fragment_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements topFragment.FragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void sendtext(String text) {
        bottom_fragment bottomfragment=(bottom_fragment) getFragmentManager().findFragmentById(R.id.fragment2);
        bottomfragment.ApplyText(text);
    }
}
