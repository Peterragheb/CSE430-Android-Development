package com.games.peter.multiplefragmenttest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    Fragment_Details fragment_details;
    Fragment_List fragment_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Log.v("savedInstanceState","null");
            addDynamicFragment();
            fragment_list = (Fragment_List) getSupportFragmentManager().findFragmentByTag("Fragment_List");
            fragment_details = (Fragment_Details) getSupportFragmentManager().findFragmentByTag("Fragment_Details");
        } else {
            Log.v("savedInstanceState","NOT null");
            fragment_list = (Fragment_List) getSupportFragmentManager().findFragmentByTag("Fragment_List");
            fragment_details = (Fragment_Details) getSupportFragmentManager().findFragmentByTag("Fragment_Details");
            if(fragment_list != null){
                Log.v("fragment_list","NOT null");
                getSupportFragmentManager().beginTransaction().remove(fragment_list).commit();
            }
            else{
                Log.v("fragment_list","IS null");
            }
            if(fragment_details != null){
                Log.v("fragment_details","NOT null");
                getSupportFragmentManager().beginTransaction().remove(fragment_details).commit();
            }
            else{
                Log.v("fragment_details","IS null");
            }
            addDynamicFragment();

            if(fragment_details != null){
                Log.v("fragment_details","NOT null AFTER ADD");
            }
        }
    }
    private void addDynamicFragment() {
        // TODO Auto-generated method stub

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                Log.v("SAME_Activity","TRUE");
                final Fragment fg = new Fragment_List();
                // adding fragment to relative layout by using layout id
                Bundle bundle = new Bundle();
                bundle.putBoolean("SameActivity",true);
                fg.setArguments(bundle);
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.container, fg,"Fragment_List");
                final Fragment fg1 = new Fragment_Details();
                // adding fragment to relative layout by using layout id
                transaction.add(R.id.container2, fg1,"Fragment_Details");
                transaction.commit();
                getSupportFragmentManager().executePendingTransactions();
                fragment_list = (Fragment_List) getSupportFragmentManager().findFragmentByTag("Fragment_List");
                fragment_details = (Fragment_Details) getSupportFragmentManager().findFragmentByTag("Fragment_Details");
            }
            else
            {
                Log.v("SAME_Activity","False_Landscape");
                final Fragment fg = new Fragment_List();
                // adding fragment to relative layout by using layout id
                Bundle bundle = new Bundle();
                bundle.putBoolean("SameActivity",false);
                fg.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.container, fg,"Fragment_List").commit();
            }
        }
        else
        {
             Log.v("SAME_Activity","False_Landscape");
             final Fragment fg = new Fragment_List();
             // adding fragment to relative layout by using layout id
             Bundle bundle = new Bundle();
             bundle.putBoolean("SameActivity",false);
             fg.setArguments(bundle);
             getSupportFragmentManager().beginTransaction().add(R.id.container, fg,"Fragment_List").commit();
        }
    }

}
