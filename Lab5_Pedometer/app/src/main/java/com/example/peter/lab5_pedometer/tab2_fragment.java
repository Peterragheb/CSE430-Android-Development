package com.example.peter.lab5_pedometer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by peter on 3/30/18.
 */

public class tab2_fragment extends Fragment {
    TextView tv_tab2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab2_fragment,container,false);
        tv_tab2=view.findViewById(R.id.tv_tab2);
        return view;
    }
}
