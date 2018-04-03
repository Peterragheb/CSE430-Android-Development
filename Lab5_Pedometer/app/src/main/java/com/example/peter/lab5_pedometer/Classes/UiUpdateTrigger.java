package com.example.peter.lab5_pedometer.Classes;

/**
 * Created by Peter on 3/4/2018.
 */

public class UiUpdateTrigger {
    private boolean updated=false;
    private ChangeListener listener;


    public ChangeListener getListener() {
        return listener;
    }
    public boolean isUpdated() {
        return updated;
    }



    public void setListener(ChangeListener listener) {
        this.listener = listener ;
    }


    public void setUpdated(boolean updated) {
        this.updated = updated;
        if (listener != null)
                listener.onChange();
    }

    public interface ChangeListener {
        void onChange();
    }

}
