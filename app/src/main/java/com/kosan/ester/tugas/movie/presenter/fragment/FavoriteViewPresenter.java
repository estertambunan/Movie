package com.kosan.ester.tugas.movie.presenter.fragment;

import com.kosan.ester.tugas.movie.App;
import com.kosan.ester.tugas.movie.model.Movie;
import com.kosan.ester.tugas.movie.network.response.ResponseMovie;
import com.kosan.ester.tugas.movie.view.activity.iview.GeneralView;

import java.util.List;


public class FavoriteViewPresenter {
    private final GeneralView mView;

    public FavoriteViewPresenter(GeneralView view) {
        this.mView = view;
    }

    public void getAllUpFavoriteMovie(int page) {
        mView.showLoading();
        try {
            // Pagging Movies
            int offset = (page - 1) * 20;

            List<Movie> allMovieList = App.getDatabaseOpen().getFavoriteMovie().getAllData(20,offset);

            ResponseMovie responseMovie = new ResponseMovie();
            responseMovie.setPage(page);
            responseMovie.setMovieDetailList(allMovieList);
            responseMovie.setTotalResult(allMovieList.size());
            responseMovie.setTotalPage(page);

            mView.dismissLoading();

            mView.showMovie(responseMovie);

        } catch (Exception e) {
            mView.dismissLoading();
            e.printStackTrace();
        }

    }
}
