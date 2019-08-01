package com.kosan.ester.tugas.movie.view.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.kosan.ester.tugas.movie.App;
import com.kosan.ester.tugas.movie.R;
import com.kosan.ester.tugas.movie.database.QueryHelper;
import com.kosan.ester.tugas.movie.model.Genres;
import com.kosan.ester.tugas.movie.model.Movie;
import com.kosan.ester.tugas.movie.utils.Utils;
import com.kosan.ester.tugas.movie.view.activity.iview.DetailMovieView;
import com.kosan.ester.tugas.movie.view.adapter.GenreAdapter;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.kosan.ester.tugas.movie.database.QueryHelper.CONTENT_URI_FAVORITE;


public class DetailMoviesActivity extends BaseActivity implements DetailMovieView, SwipeRefreshLayout.OnRefreshListener {
    // TAG
    private static final String TAG = DetailMoviesActivity.class.getSimpleName();

    private static final String EXTRA_IS_LOADED_VIEW = "extra:is_loaded_view";
    private static final String EXTRA_IS_LOADING = "extra:is_loading";
    public static final String EXTRA_MOVIE_DETAIL = "extra:movie_detail";
    public static final String EXTRA_TITLE = "extra:title";

    @BindView(R.id.ll_global_wrapper)
    protected LinearLayout llGlobalWrapper;

    @BindView(R.id.img_item_photo)
    protected CircleImageView ivItemPhoto;

    @BindView(R.id.tv_title)
    protected TextView tvTitle;

    @BindView(R.id.tv_description)
    protected TextView tvDesc;

    @BindView(R.id.tv_rating)
    protected TextView tvRating;

    @BindView(R.id.tv_duration)
    protected TextView tvDuration;

    @BindView(R.id.tv_language)
    protected TextView tvLanguage;

    @BindView(R.id.tv_release)
    protected TextView tvRelease;

    @BindView(R.id.tv_overview)
    protected TextView tvOverview;

    @BindView(R.id.tv_no_genre)
    protected TextView tvNoGenre;

    @BindView(R.id.rv_genre)
    protected RecyclerView rvGenre;

    @BindView(R.id.srl_main_wrapper)
    protected SwipeRefreshLayout srlRefresh;

    @BindView(R.id.sv_wrapper)
    protected ScrollView svWrapper;

    @BindView(R.id.top_toolbar)
    protected Toolbar toolbar;

    private LinearLayoutManager mLinearLayoutManager;
    private Movie mMovie;
    private GenreAdapter mGenreAdapter;
    private Handler mHandler;
    private boolean isFavorite;
    private MenuItem menuItemFav;
    private int movieId;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movies);

        // setup view
        setupToolbar(getIntent().getStringExtra(EXTRA_TITLE));
        setupListener();

        // Check Orientation Change
        if (savedInstanceState == null)
            refreshContent();
        else {
            isLoaded = savedInstanceState.getBoolean(EXTRA_IS_LOADED_VIEW);

            if (savedInstanceState.getBoolean(EXTRA_IS_LOADING)) {
                refreshContent();
            } else {
                setupRecyclerview();
                initView();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MOVIE_DETAIL, mMovie);
        outState.putBoolean(EXTRA_IS_LOADING, srlRefresh.isRefreshing());
        outState.putBoolean(EXTRA_IS_LOADED_VIEW, isLoaded);
    }

    @Override
    public void initView() {
        Log.i(TAG, "Method Name : " + Utils.getMethodName());
        Intent intent = getIntent();
        if (intent != null) {
            mMovie = intent.getParcelableExtra(EXTRA_MOVIE_DETAIL);
            if (mMovie != null) {

                Utils.loadImage(ivItemPhoto, mMovie.getPosterPath());

                tvTitle.setText(mMovie.getTitle());
                tvDesc.setText(mMovie.getOriginalTitle());
                tvRating.setText(toText(mMovie.getVoteAverage()));
                tvDuration.setText(toText(mMovie.getVoteCount()));
                tvLanguage.setText(mMovie.getOriginalLanguage());
                tvRelease.setText(Utils.toReleaseDate(mMovie.getReleaseDate()));
                tvOverview.setText(TextUtils.isEmpty(mMovie.getOverview()) ?
                        getString(R.string.text_no_overview) : mMovie.getOverview());
                List<Genres> data = Genres.findGenre(mMovie.getGenreIds());
                mGenreAdapter.addList(data);
                tvNoGenre.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);

                movieId = mMovie.getId();

                // Check Movie ID
                try {
                    isFavorite = App.getDatabaseOpen().getFavoriteMovie().getFindFavoriteMove(mMovie.getId()).getIsFavorite();
                    selectedFavIcon(isFavorite);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.isLoaded = true;

                if(menuItemFav !=null)
                    menuItemFav.setEnabled(true);

            }
        }
    }

    @Override
    public void setupRecyclerview() {
        Log.i(TAG, "Method Name : " + Utils.getMethodName());
        if (rvGenre != null) {
            mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            mGenreAdapter = new GenreAdapter();
            rvGenre.setLayoutManager(mLinearLayoutManager);
            rvGenre.setAdapter(mGenreAdapter);
        }

    }

    @Override
    public void showLoading() {
        Log.i(TAG, "show loading");
        if (srlRefresh != null)
            srlRefresh.setRefreshing(true);
    }

    @Override
    public void dismissLoading() {
        Log.i(TAG, "dismiss loading");
        if (srlRefresh != null)
            srlRefresh.setRefreshing(false);
    }

    @Override
    public void loadContentError() {
        Log.i(TAG, "Content Error");
    }

    @Override
    public void refreshContent() {
        Log.i(TAG, "Refresh Content");
        showLoading();
        onRefresh();
    }

    @Override
    public void setupListener() {
        if (srlRefresh != null) {
            srlRefresh.setOnRefreshListener(this);
        }
    }

    @Override
    public void setupToolbar(String text) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(text);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public void doActionFavorite() {
        try {
            if (isFavorite) {
                isFavorite = false;
                addFavoriteDb(false);
            } else {
                isFavorite = true;
                addFavoriteDb(true);
            }
            selectedFavIcon(isFavorite);
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void addFavoriteDb(final boolean isFavorite) {
        try {
            Movie movie = App.getDatabaseOpen().getFavoriteMovie().getFindFavoriteMove(movieId);
            if (movie != null) {

                /*
                  Change Value if Exist Favorie Movie Status 1 else to no favorite movie 0
                 */
                ContentValues contentValues = QueryHelper.getIsFavorite(isFavorite);

                // URI URI CONTENT PROVIDER
                Uri uri = Uri.parse(CONTENT_URI_FAVORITE + "/" + movieId);

                int count = getContentResolver().update(uri,
                        contentValues,
                        QueryHelper.FavoriteMovie._ID + "=?",
                        new String[]{String.valueOf(movieId)});

                // Show Message Success / Fail
                doMessageFavorite(count == 0,isFavorite);


            } else {
                /*
                  IF Movie no exist from DB Movie Favorite (Add new movie favorite)
                 */
              ContentValues contentValues = QueryHelper.getAllContentValue(mMovie);
              getContentResolver().insert(CONTENT_URI_FAVORITE, contentValues);

                // Show Message Success / Fail
              doMessageFavorite(false,true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void selectedFavIcon(boolean isSelected) {
        Utils.menuIconColor(this, menuItemFav,
                isSelected ? R.color.colorAccent : android.R.color.white);
    }

    private void doMessageFavorite(boolean isFail, boolean isFavorite) {
        if (!isFail)
            Snackbar.make(llGlobalWrapper,
                    isFavorite ? R.string.add_favorite_message : R.string.remove_favorite_message
                    , Snackbar.LENGTH_SHORT).show();

        else
            Snackbar.make(llGlobalWrapper,
                    isFavorite ? R.string.add_fail_favorite_message : R.string.remove_fail_favorite_message
                    , Snackbar.LENGTH_SHORT).show();

        doNotifyWidgetFavMovie();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_fav, menu);
        menuItemFav = menu.findItem(R.id.action_menu_favorite);
        menuItemFav.setEnabled(isLoaded);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.action_menu_favorite:
                doActionFavorite();
                break;
            default:
                break;
        }

        return true;

    }

    @Override
    public void memoryRelease() {
        if (mLinearLayoutManager != null)
            mLinearLayoutManager = null;

        if (mMovie != null)
            mMovie = null;

        if (mGenreAdapter != null) {
            mGenreAdapter.clearList();
            mGenreAdapter = null;
        }

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        memoryRelease();
    }

    @Override
    public void onRefresh() {
        svWrapper.setVisibility(View.GONE);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                svWrapper.setVisibility(View.VISIBLE);
                dismissLoading();
                setupRecyclerview();
                initView();
            }
        }, 1500);
    }
}
