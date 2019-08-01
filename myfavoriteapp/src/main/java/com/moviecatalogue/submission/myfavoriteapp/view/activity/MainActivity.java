package com.moviecatalogue.submission.myfavoriteapp.view.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.moviecatalogue.submission.myfavoriteapp.R;
import com.moviecatalogue.submission.myfavoriteapp.presenter.MainViewPresenter;
import com.moviecatalogue.submission.myfavoriteapp.view.adapter.MovieAdapter;

import butterknife.BindView;

import static com.moviecatalogue.submission.myfavoriteapp.database.FavoriteMovieDB.CONTENT_URI_FAVORITE;



public class MainActivity extends BaseActivity implements MainView,LoaderManager.LoaderCallbacks<Cursor>{
    // TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tv_description)
    protected TextView tvDesc;

    @BindView(R.id.lv_movies)
    protected ListView lvMovies;

    private MovieAdapter adapter;

    private final int LOAD_MOVIE_FAVORITE_ID = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewPresenter mainViewPresenter = new MainViewPresenter(this);

        setupView();
        setupToolbar();

        mainViewPresenter.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).restartLoader(LOAD_MOVIE_FAVORITE_ID, null, this);
    }

    @Override
    public void setupToolbar() {
        if(getSupportActionBar() !=null)
            getSupportActionBar().setTitle(R.string.fav_title_toolbar);
    }

    @Override
    public void setupView() {
        adapter = new MovieAdapter(this, null);
        lvMovies.setAdapter(adapter);
    }

    @Override
    public void load() {
        LoaderManager.getInstance(this).initLoader(LOAD_MOVIE_FAVORITE_ID, null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.i(TAG,"onCreateLoader : "+bundle);
        return new CursorLoader(this, CONTENT_URI_FAVORITE, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.i(TAG,"onLoadFinished : "+data);
        tvDesc.setVisibility(isCursorEmpty(data) ? View.VISIBLE: View.GONE);
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.i(TAG,"onLoaderReset ");
        adapter.swapCursor(null);
    }

    private boolean isCursorEmpty(Cursor cursor){
        return !cursor.moveToFirst() || cursor.getCount() == 0;
    }
}
