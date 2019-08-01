package com.moviecatalogue.submission.myfavoriteapp.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;



public class FavoriteMovieDB implements BaseColumns {

    private static final String TABLE_NAME = "mst_movie_favorite";

    public static final String TITLE = "title";
    public static final String POSTER_PATH = "poster_path";
    public static final String OVERVIEW = "overview";
    private static final String SCHEME = "content";
    private static final String AUTHORITY = "com.dicoding.kailani.submission.moviecatalogue";

    // Base content yang digunakan untuk akses content provider
    public static final Uri CONTENT_URI_FAVORITE = new Uri.Builder().scheme(FavoriteMovieDB.SCHEME)
            .authority(FavoriteMovieDB.AUTHORITY)
            .appendPath(FavoriteMovieDB.TABLE_NAME)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
