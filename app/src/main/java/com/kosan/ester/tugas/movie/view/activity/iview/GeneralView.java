package com.kosan.ester.tugas.movie.view.activity.iview;

import android.os.Bundle;

import com.kosan.ester.tugas.movie.model.Movie;
import com.kosan.ester.tugas.movie.network.response.ResponseMovie;
import com.kosan.ester.tugas.movie.view.BaseView;


public interface GeneralView extends BaseView {
    void setupRecyclerView(Bundle saveInstance);

    void showMovie(ResponseMovie responseMovie);

    void goToNextActivity(Movie movie);

    void setupToolbar(String text);

    void setupListener();
    void refreshContent();
}
