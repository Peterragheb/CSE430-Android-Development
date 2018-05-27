package com.games.peter.multiplefragmenttest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        addDynamicFragment();
    }
    private void addDynamicFragment() {
        // TODO Auto-generated method stub
        final Fragment fg = new Fragment_Details();
        // adding fragment to relative layout by using layout id
        getSupportFragmentManager().beginTransaction().add(R.id.container3, fg).commit();
//        if (getIntent().getExtras().containsKey("title") && getIntent().getExtras().containsKey("description")){
//            ((Fragment_Details)fg).title.setText(getIntent().getStringExtra("title"));
//            ((Fragment_Details)fg).description.setText(getIntent().getStringExtra("description"));
//        }

    }
}
