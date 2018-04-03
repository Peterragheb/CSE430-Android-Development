package com.example.peter.lab5_pedometer;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import com.example.peter.lab5_pedometer.Classes.SectionPageAdapter;
import com.example.peter.lab5_pedometer.Classes.SharedVar;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    public static final String MY_PREFS_NAME = "PEDO_PREF";
    public static final String MY_PREFS_STEP_SIZE = "step_size";
    public static final String MY_PREFS_BODY_WEIGHT = "body_weight";
    private SectionPageAdapter sectionPageAdapter;
    private ViewPager viewPager;
    tab1_fragment mtab1_fragment;
    tab2_fragment mtab2_fragment;
    tab3_fragment mtab3_fragment;
    ContentValues contentValues;
    public static SharedPreferences prefs ;
    private int temp_step_length;
    private int temp_body_weight;
    public static SharedVar sharedVar=new SharedVar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sectionPageAdapter=new SectionPageAdapter(getSupportFragmentManager());
        viewPager=findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(3);
        setupViewPage(viewPager);
        TabLayout tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mtab1_fragment =(tab1_fragment) getSupportFragmentManager().getFragment(savedInstanceState, "mtab1_fragment");
            mtab2_fragment =(tab2_fragment) getSupportFragmentManager().getFragment(savedInstanceState, "mtab2_fragment");
            mtab3_fragment =(tab3_fragment) getSupportFragmentManager().getFragment(savedInstanceState, "mtab3_fragment");
        }
        tabLayout.addOnTabSelectedListener(this);
        Log.v("temp_step_length",temp_step_length+"");
        Log.v("temp_body_weight",temp_body_weight+"");
        if (!prefs.contains(MY_PREFS_STEP_SIZE)&&!prefs.contains(MY_PREFS_BODY_WEIGHT))
            display_WelcomeDialog(this);
        temp_step_length=prefs.getInt(MY_PREFS_STEP_SIZE,0) ;
        temp_body_weight=prefs.getInt(MY_PREFS_BODY_WEIGHT,0);
        Log.v("temp_step_length after",temp_step_length+"");
        Log.v("temp_body_weight after",temp_body_weight+"");
        sharedVar.setVariables(temp_step_length,temp_body_weight);
        Log.v("STEP LENGTH",sharedVar.getStep_length()+"");
        Log.v("BODY WEIGHT",sharedVar.getBody_weight()+"");
    }

    private void display_WelcomeDialog(final Context context){
        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.navy_blue)
                .setButtonsColorRes(R.color.darkDeepOrange)
                .setIcon(R.drawable.ic_fire)
                .setTitle("Welcome")
                .setMessage(Html.fromHtml("Would you like to set your Body Weight and Step length? <br><br> <b>Note:</b>Otherwise they will be set to default"))
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displayStep_SetDialog(context);
                    }
                })
                .setNegativeButton(android.R.string.no,  new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displayAfterSetup_Dialog(context,false);
                        temp_step_length=0;
                        temp_body_weight=0;
                        sharedVar.setVariables(temp_step_length,temp_body_weight);
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putInt(MY_PREFS_STEP_SIZE, sharedVar.getStep_length());
                        editor.putInt(MY_PREFS_BODY_WEIGHT, sharedVar.getBody_weight());
                        editor.apply();
                    }
                }).setCancelable(false)
                .show();

    }
    private void displayStep_SetDialog(final Context context){
        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkDeepOrange));
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.navy_blue)
                .setTitle("Set Step Length Value")
                .setConfirmButtonColor(getResources().getColor(R.color.darkDeepOrange))
                .setMessage("Step Length")
                .setIcon(R.drawable.ic_settings)
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .configureEditText(editText -> editText.setHint("Cm"))
                .configureEditText(editText -> editText.setBackgroundTintList(colorStateList))
                .setInputFilter("Please enter a value", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return !text.isEmpty();
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        temp_step_length=Integer.valueOf(text);
                        displayWeight_SetDialog(context);
                    }
                }).setCancelable(false)
                .show();

    }
    private void displayWeight_SetDialog(final Context context){
        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkDeepOrange));
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.navy_blue)
                .setConfirmButtonColor(getResources().getColor(R.color.darkDeepOrange))
                .setTitle("Set Body Weight Value")
                .setMessage("Body Weight")
                .setIcon(R.drawable.ic_settings)
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .configureEditText(editText -> editText.setHint("Kg"))
                .configureEditText(editText -> editText.setBackgroundTintList(colorStateList))
                .setInputFilter("Please enter a value", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return !text.isEmpty();
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        temp_body_weight=Integer.valueOf(text);
                        displayAfterSetup_Dialog(context,true);
                    }
                }).setCancelable(false)
                .show();

    }
    private void displayAfterSetup_Dialog(final Context context,boolean success){
    if (success){
        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.navy_blue)
                .setButtonsColorRes(R.color.darkDeepOrange)
                .setIcon(R.drawable.ic_speed)
                .setTitle("Finish")
                .setMessage("You successfully finished your startup setup.\nTo change the values go to settings.")
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedVar.setVariables(temp_step_length,temp_body_weight);
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putInt(MY_PREFS_STEP_SIZE, sharedVar.getStep_length());
                        editor.putInt(MY_PREFS_BODY_WEIGHT, sharedVar.getBody_weight());
                        editor.apply();
                        Log.v("VARIABLES SET"," STEP L:" +temp_step_length+" BODY W: "+temp_body_weight);
                    }
                }).setCancelable(false)
                .show();
    }
    else {
        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.navy_blue)
                .setButtonsColorRes(R.color.darkDeepOrange)
                .setIcon(R.drawable.ic_speed)
                .setTitle("Finish")
                .setMessage("Values are set to default.\nTo change them go to settings.")
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setCancelable(false)
                .show();
    }

    }
    private void setupViewPage(ViewPager viewPager){
        SectionPageAdapter sectionPageAdapter=new SectionPageAdapter(getSupportFragmentManager());
        mtab1_fragment=new tab1_fragment();
        mtab2_fragment=new tab2_fragment();
        mtab3_fragment=new tab3_fragment();
        sectionPageAdapter.addfragment(mtab1_fragment,"Today");
        sectionPageAdapter.addfragment(mtab2_fragment,"Standing");
        sectionPageAdapter.addfragment(mtab3_fragment,"Settings");
        viewPager.setAdapter(sectionPageAdapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//        if (tab.getPosition()==1){
//            mtab2_fragment.displayRecordSet();
//        }
//        if (tab.getPosition()==0){
//            if (contentValues!=null){
//                mtab1_fragment.restoreState(contentValues);
//                contentValues=null;
//            }
//
//        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab.getPosition()==0){
//            start_State=mtab1_fragment.getStartState();
//            step_count=mtab1_fragment.getNumSteps();
           if (tab1_fragment.STATE_STARTED){
               tab1_fragment.STATE_STARTED=false;
               if (!tab1_fragment.PAUSED) {
                   mtab1_fragment.Pause();
               }
               //contentValues=mtab1_fragment.saveState();
           }
            //contentValues=mtab1_fragment.saveState();
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
//        if (tab.getPosition()==0){
//            if (contentValues!=null){
//                mtab1_fragment.restoreState(contentValues);
//                contentValues=null;
//            }
//
//        }
    }

    @Override
    public void onDestroy() {
        Log.v("MAIN:","onStart()");
        super.onDestroy();
    }
    @Override
    public void onStart() {
        Log.v("MAIN:","onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.v("MAIN:","onStop()");
        super.onStop();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "mtab1_fragment", mtab1_fragment);
        //getSupportFragmentManager().putFragment(outState, "mtab2_fragment", mtab2_fragment);
        //getSupportFragmentManager().putFragment(outState, "mtab3_fragment", mtab3_fragment);
    }

}
