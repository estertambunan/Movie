package com.kosan.ester.tugas.movie.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.kosan.ester.tugas.movie.R;
import com.kosan.ester.tugas.movie.model.Movie;
import com.kosan.ester.tugas.movie.network.response.ResponseMovie;
import com.kosan.ester.tugas.movie.presenter.fragment.UpCommingViewPresenter;
import com.kosan.ester.tugas.movie.utils.Utils;
import com.kosan.ester.tugas.movie.view.activity.DetailMoviesActivity;
import com.kosan.ester.tugas.movie.view.activity.MainActivity;
import com.kosan.ester.tugas.movie.view.activity.iview.GeneralView;
import com.kosan.ester.tugas.movie.view.adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UpCommingFragment extends BaseFragment implements GeneralView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = UpCommingFragment.class.getSimpleName();

    private static final String EXTRA_LIST = "extra:list";
    private static final String EXTRA_COUNTER = "extra:counter";
    private static final String EXTRA_LAST_ITEM = "extra:last_item";
    private static final String EXTRA_STATE_SCROLL_POSITION = "extra:state_scroll_position";

    @BindView(R.id.tv_description)
    protected TextView tvDesc;

    @BindView(R.id.rv_movies)
    protected RecyclerView rvMovies;

    @BindView(R.id.srl_main_wrapper)
    protected SwipeRefreshLayout srlRefresh;

    private LinearLayoutManager llManager;
    private UpCommingViewPresenter mUpCommingViewPresenter;
    private MoviesAdapter adapter;
    private Handler mHandler;
    private int page = 1;
    private int counter = 1;
    private int totalItemCount;
    private int lastVisibleItem;
    private int lastItemCounter = 0;
    private boolean isLoading = false;
    private List<Movie> tempList;

    public UpCommingFragment() {
    }

    public static UpCommingFragment newInstance() {

        Bundle args = new Bundle();

        UpCommingFragment fragment = new UpCommingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mUpCommingViewPresenter = new UpCommingViewPresenter(this);
        mHandler = new Handler();

        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup View
        setupToolbar(getString(R.string.title_bar_up_comming));
        setupRecyclerView(savedInstanceState);
        setupListener();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.isVisible()) {
            if (adapter != null) {
                if (adapter.getItemCount() > 0) {
                    adapter.removeProgressBar();
                    outState.putParcelableArrayList(EXTRA_LIST, new ArrayList<>(adapter.getDataset()));
                    outState.putInt(EXTRA_COUNTER, counter);
                    outState.putInt(EXTRA_LAST_ITEM, lastItemCounter);
                    outState.putInt(EXTRA_STATE_SCROLL_POSITION, llManager.findFirstCompletelyVisibleItemPosition());
                } else {
                    if (tempList != null)
                        outState.putParcelableArrayList(EXTRA_LIST, new ArrayList<>(tempList));

                    outState.putInt(EXTRA_COUNTER, 1);
                    outState.putInt(EXTRA_LAST_ITEM, lastItemCounter);
                    outState.putInt(EXTRA_STATE_SCROLL_POSITION, llManager.findFirstCompletelyVisibleItemPosition());
                }
            }
        }
    }

    public void setupRecyclerView(Bundle saveInstance) {
        if (rvMovies != null) {
            llManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false);
            adapter = new MoviesAdapter(this);
            rvMovies.setLayoutManager(llManager);
            rvMovies.setAdapter(adapter);

            if (saveInstance == null) {
                refreshContent();
            } else {
                ArrayList<Movie> list = saveInstance.getParcelableArrayList(EXTRA_LIST);
                if (list != null) {
                    counter = saveInstance.getInt(EXTRA_COUNTER);
                    lastItemCounter = saveInstance.getInt(EXTRA_LAST_ITEM);
                    adapter.addList(list);
                    tvDesc.setVisibility(adapter.getItemCount() == 0 ?
                            View.VISIBLE : View.GONE);
                    llManager.scrollToPosition(saveInstance.getInt(EXTRA_STATE_SCROLL_POSITION));
                }else{
                    refreshContent();
                }

            }

        }
    }


    @Override
    public void showMovie(ResponseMovie responseMovie) {
        if (adapter != null) {

            this.tempList = responseMovie.getMovieDetailList();

            // Check Maximum Item Count
            if (adapter.getItemCount() <= responseMovie.getTotalResult()) {
                adapter.addList(responseMovie.getMovieDetailList());

                if (adapter.getItemCount() >= responseMovie.getTotalResult()) {
                    adapter.removeProgressBar();

                    if (adapter.getItemCount() > 0)
                        Toast.makeText(getActivity(), R.string.message_warning_all_movie_loaded, Toast.LENGTH_LONG).show();
                }
            }

            // Check No Data
            tvDesc.setVisibility(responseMovie.getMovieDetailList().size() == 0 ?
                    View.VISIBLE : View.GONE);

            // Check Maximum Counter Page
            if (counter <= responseMovie.getTotalPage())
                counter = (adapter.getItemCount() / 20) + 1;

            lastItemCounter = adapter.getItemCount();

            // First Load Scroll top
            if (page == 1) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (rvMovies != null)
                            rvMovies.smoothScrollToPosition(0);
                    }
                }, 500);
            }
        }
    }

    @Override
    public void goToNextActivity(Movie movie) {
        Log.i(TAG, "Method Name : " + Utils.getMethodName());

        Intent intent = new Intent(getActivity(), DetailMoviesActivity.class);
        intent.putExtra(DetailMoviesActivity.EXTRA_MOVIE_DETAIL, movie);
        intent.putExtra(DetailMoviesActivity.EXTRA_TITLE, movie.getTitle());
        startActivity(intent);
    }

    @Override
    public void setupToolbar(String text) {
        if (!TextUtils.isEmpty(text) && getActivity() != null)
            ((MainActivity) getActivity()).changeTitleBar(text);
    }

    @Override
    public void setupListener() {
        // Infinite Scrolling Recyclerview
        rvMovies.post(new Runnable() {
            @Override
            public void run() {
                rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        srlRefresh.setRefreshing(llManager.findLastVisibleItemPosition() == 0);
                        totalItemCount = llManager.getItemCount();
                        lastVisibleItem = llManager.findLastVisibleItemPosition();
                        int visibleThreshold = 1;
                        if (lastItemCounter > 19 && isLoaded() && totalItemCount <= lastVisibleItem + visibleThreshold) {
                            page = counter;
                            mUpCommingViewPresenter.getAllUpCommingMovies(page);
                        }
                    }
                });
            }
        });


        // initialize Swipe Refresh
        srlRefresh.setOnRefreshListener(this);

    }

    @Override
    public void refreshContent() {
        if (this.isVisible()) {
            showLoading();
            onRefresh();
        }
    }

    @Override
    public void showLoading() {
        Log.i(TAG, "show loading");

        if (this.isVisible() && srlRefresh != null && page == 1)
            srlRefresh.setRefreshing(true);


        this.isLoading = true;
    }

    @Override
    public void dismissLoading() {
        Log.i(TAG, "dismiss loading");
        if (this.isVisible() && srlRefresh != null && page == 1)
            srlRefresh.setRefreshing(false);


        this.isLoading = false;
    }

    private boolean isLoaded() {
        return !isLoading;
    }

    @Override
    public void loadContentError() {

        Log.i(TAG, "load content error");
        if (this.isVisible() && tvDesc != null)
            tvDesc.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE
                    : View.GONE);
    }

    @Override
    public void memoryRelease() {
        if (llManager != null) llManager = null;
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        if (adapter != null) adapter = null;
        if (llManager != null) llManager = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        memoryRelease();
    }

    private void resetList() {
        this.page = 1;
        this.counter = 1;
        this.lastItemCounter = 0;
        this.totalItemCount = 0;
        this.lastVisibleItem = 0;
        if (adapter != null) {
            adapter.clearList();
        }
    }

    @Override
    public void onRefresh() {
        resetList();
        mUpCommingViewPresenter.getAllUpCommingMovies(page);
    }
}
