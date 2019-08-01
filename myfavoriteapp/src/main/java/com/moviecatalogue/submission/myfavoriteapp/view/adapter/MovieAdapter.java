package com.moviecatalogue.submission.myfavoriteapp.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviecatalogue.submission.myfavoriteapp.R;
import com.moviecatalogue.submission.myfavoriteapp.database.FavoriteMovieDB;
import com.moviecatalogue.submission.myfavoriteapp.utils.Utils;



public class MovieAdapter extends CursorAdapter {


    public MovieAdapter(Context context, Cursor c){
        this(context, c, true);
    }

    private MovieAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.row_item_movie, viewGroup, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null){

            ImageView ivThumbnails = view.findViewById(R.id.iv_thumbnails);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            TextView tvDescription = view.findViewById(R.id.tv_description);

            tvTitle.setText(FavoriteMovieDB.getColumnString(cursor,FavoriteMovieDB.TITLE));
            tvDescription.setText(FavoriteMovieDB.getColumnString(cursor,FavoriteMovieDB.OVERVIEW));
            Utils.loadImage(ivThumbnails, FavoriteMovieDB.getColumnString(cursor,FavoriteMovieDB.POSTER_PATH));
        }
    }
}