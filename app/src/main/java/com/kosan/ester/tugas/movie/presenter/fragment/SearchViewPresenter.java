package com.kosan.ester.tugas.movie.presenter.fragment;

import android.support.annotation.NonNull;

import com.kosan.ester.tugas.movie.BuildConfig;
import com.kosan.ester.tugas.movie.network.RestClient;
import com.kosan.ester.tugas.movie.network.response.ResponseMovie;
import com.kosan.ester.tugas.movie.view.activity.iview.GeneralView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchViewPresenter {
    private final GeneralView mView;
    private final RestClient mRestClient;
    private final static String API = BuildConfig.API_KEY;
    private final static String LANGUGAGE = "en-US";

    public SearchViewPresenter(GeneralView view) {
        this.mView = view;
        this.mRestClient = new RestClient();
    }

    public void getAllMovies(int page) {
        mView.showLoading();
        mRestClient.getApiService().getAllMovies(API, LANGUGAGE, page,null,null).enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                System.gc();
                mView.dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mView.showMovie(response.body());

                    }
                } else {
                    mView.loadContentError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                mView.dismissLoading();
                mView.loadContentError();
            }
        });

    }

    public void searchMovie(int page, String textSearch) {

        mView.showLoading();
        mRestClient.getApiService().doSearchMovies(API, LANGUGAGE, page, textSearch).enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                System.gc();
                mView.dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mView.showMovie(response.body());
                    }
                } else {
                    mView.loadContentError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                mView.dismissLoading();
                mView.loadContentError();
            }
        });

    }
}
