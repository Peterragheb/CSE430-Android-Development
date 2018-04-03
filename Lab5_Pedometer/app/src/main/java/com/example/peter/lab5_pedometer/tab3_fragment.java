package com.example.peter.lab5_pedometer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.peter.lab5_pedometer.Classes.SharedVar;

/**
 * Created by peter on 3/30/18.
 */

public class tab3_fragment extends Fragment implements SharedVar.ChangeListener ,View.OnClickListener {
    EditText et_step_size,et_body_weight;
    Button btn_save_settings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab3_fragment,container,false);
        et_step_size=view.findViewById(R.id.et_step_size);
        et_body_weight=view.findViewById(R.id.et_body_weight);
        btn_save_settings=view.findViewById(R.id.btn_save_settings);
        et_step_size.setText(MainActivity.sharedVar.getStep_length()+"");
        et_body_weight.setText(MainActivity.sharedVar.getBody_weight()+"");
        btn_save_settings.setOnClickListener(this);
        MainActivity.sharedVar.addListener(this);
        return view;
    }

    @Override
    public void onChange() {
        et_step_size.setText(MainActivity.sharedVar.getStep_length()+"");
        et_body_weight.setText(MainActivity.sharedVar.getBody_weight()+"");
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_save_settings){
            Log.v("TEST","1");

            if (!et_step_size.getText().toString().isEmpty()&&!et_body_weight.getText().toString().isEmpty()){
                Log.v("TEST","2");

                int temp_step_length=Integer.valueOf(et_step_size.getText().toString());
                int temp_body_weight=Integer.valueOf(et_body_weight.getText().toString());
                if (temp_step_length!=0 && temp_body_weight!=0){
                    Log.v("TEST","3");
                    Log.v("TEST",temp_step_length+" "+MainActivity.sharedVar.getStep_length()+" "+temp_body_weight+" "+MainActivity.sharedVar.getBody_weight() );
                    if ((temp_step_length!=MainActivity.sharedVar.getStep_length()) || (temp_body_weight!=MainActivity.sharedVar.getBody_weight())){
                        Log.v("TEST","4");


                        MainActivity.sharedVar.setVariables(temp_step_length,temp_body_weight);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences(MainActivity.MY_PREFS_NAME, MainActivity.MODE_PRIVATE).edit();
                        editor.putInt(MainActivity.MY_PREFS_STEP_SIZE, MainActivity.sharedVar.getStep_length());
                        editor.putInt(MainActivity.MY_PREFS_BODY_WEIGHT, MainActivity.sharedVar.getBody_weight());
                        editor.apply();
                        Toast.makeText(getContext(),"Step length and body weight have been updated",Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }
    }
}
