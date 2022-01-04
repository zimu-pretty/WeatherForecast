package com.example.myapplication;

import android.app.Application;
import android.util.Log;


import java.util.List;

public class VariableApp extends Application {
    private static VariableApp instance = null;
    public static VariableApp getInstance(){
        return instance;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }
}
