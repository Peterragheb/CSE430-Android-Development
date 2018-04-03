package com.example.peter.lab5_pedometer.Classes;

import java.util.ArrayList;

/**
 * Created by Peter on 1/4/2018.
 */

public class SharedVar {
    private int step_length;
    private int body_weight;
    private ArrayList<ChangeListener> listeners=new ArrayList<>();


    public ChangeListener getListener(int i) {
        return listeners.get(i);
    }
    public ArrayList<ChangeListener> getAllListeners() {
        return listeners;
    }
    public int getStep_length() {
        return step_length;
    }

    public void setVariables(int step_length,int body_weight) {
        if (step_length == 0 || body_weight == 0){
            this.step_length = 30;
            this.body_weight = 80;
        }else{
            this.step_length = step_length;
            this.body_weight = body_weight;
        }
        if (listeners != null)
            for (int i=0;i<listeners.size();i++)
                listeners.get(i).onChange();
    }

    public void addListener(ChangeListener listener) {
        this.listeners.add(listener);
    }

    public int getBody_weight() {
        return body_weight;
    }

    public interface ChangeListener {
        void onChange();
    }
}