package com.kosan.ester.tugas.movie.view.activity.iview;


import com.kosan.ester.tugas.movie.view.BaseView;

public interface DetailMovieView extends BaseView {
    void initView();

    void setupRecyclerview();

    void refreshContent();

    void setupListener();

    void setupToolbar(String text);

    void doActionFavorite();

    void addFavoriteDb(boolean isFavorite);
}
