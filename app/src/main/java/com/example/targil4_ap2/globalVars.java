package com.example.targil4_ap2;

import android.app.Application;

import com.example.targil4_ap2.adapters.MessagesListAdapter;

public class globalVars extends Application {
    // Global variable declaration and initialization
    public static boolean lightOn = true;
    public static String username = "";
    public static String password = "";
    public static String server="http://10.0.2.2:5000/api/";
}
