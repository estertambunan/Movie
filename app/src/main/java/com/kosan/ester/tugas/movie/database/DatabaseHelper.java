package com.kosan.ester.tugas.movie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie_v1.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper dbHelper;
    private final QueryHelper mQueryHelper;


     private DatabaseHelper(Context mContext){
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mQueryHelper = new QueryHelper();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Table
        mQueryHelper.getFavoriteMovie().createTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mQueryHelper.getFavoriteMovie().dropTable(db);
        onCreate(db);
    }

    public synchronized static DatabaseHelper init(Context context) {
        if(dbHelper ==null){
            dbHelper = new DatabaseHelper(context);
        }
        return dbHelper;
    }

}
