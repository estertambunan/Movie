package com.kosan.ester.tugas.movie;

import android.app.Application;
import android.database.sqlite.SQLiteException;

import com.kosan.ester.tugas.movie.database.DatabaseHelper;
import com.kosan.ester.tugas.movie.database.QueryHelper;
import com.kosan.ester.tugas.movie.database.SharedPrefs;

public class App extends Application {

    private static DatabaseHelper mDatabaseHelper;
    private static QueryHelper mQueryHelper;
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        // Init Global Context
        initInstance();

        // Initial Prefs
        SharedPrefs.init(this);

        // Initial Database
        DatabaseHelper.init(this);

    }

    private synchronized void initInstance(){
        if(mInstance == null)
            mInstance  = new App();
    }

    public static QueryHelper getDatabaseOpen() {
        if(mDatabaseHelper == null)
            mDatabaseHelper = DatabaseHelper.init(mInstance);

        if(mQueryHelper == null)
            mQueryHelper = new QueryHelper(mDatabaseHelper.getWritableDatabase());

        return mQueryHelper;
    }

    public static void getDatabaseClose()throws SQLiteException{
        if(mDatabaseHelper != null)
            mDatabaseHelper.close();

        if(mQueryHelper !=null)
            mQueryHelper = null;
    }
}
