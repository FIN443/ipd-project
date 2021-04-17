package com.example.tab3;

import android.app.Application;

public class MyPPM extends Application {
    private int temp_ppm;

    public int getPPM() {
        return temp_ppm;
    }

    public void setPPM(int temp) {
        this.temp_ppm = temp;
    }
}
