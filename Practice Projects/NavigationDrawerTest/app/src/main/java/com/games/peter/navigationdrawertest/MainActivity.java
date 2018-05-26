package com.games.peter.navigationdrawertest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout dl_main;
    ActionBarDrawerToggle mToggle;
    NavigationView nav_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dl_main = findViewById(R.id.dl_main);
        mToggle = new ActionBarDrawerToggle(this,dl_main,R.string.open,R.string.close);
        dl_main.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav_view =  findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return  true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item1)
            Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.item2)
            Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.item3)
            Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.item4)
            Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
        return false;
    }
}
