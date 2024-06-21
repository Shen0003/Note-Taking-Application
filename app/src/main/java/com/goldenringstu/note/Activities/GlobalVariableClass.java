package com.goldenringstu.note.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalVariableClass {
    private static final String PREF_THEME_CODE = "theme_code";
    private static final int DEFAULT_THEME_CODE = 0; // Set your default theme code here

    private static SharedPreferences sharedPreferences;

    public static void initSP(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("app_theme_prefs", Context.MODE_PRIVATE);
        }
    }

    public static int getTHEME_CODE() {
        return sharedPreferences.getInt(PREF_THEME_CODE, DEFAULT_THEME_CODE);
    }

    public static void setTHEME_CODE(int themeCode) {
        sharedPreferences.edit().putInt(PREF_THEME_CODE, themeCode).apply();
    }
}



    //default is 0, which is default theme

    /*
        0: default black
        1: default white
        2: Tong, The Kitty
        3: ...
     */

