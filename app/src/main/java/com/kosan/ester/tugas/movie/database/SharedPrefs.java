package com.kosan.ester.tugas.movie.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefs {
    private static SharedPreferences prefs;

    public static void init(Context context) {
        if(prefs == null){
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static void setBoolean(String key,Boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key,defaultValue);
    }
}
