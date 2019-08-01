package com.kosan.ester.tugas.movie.view;


public interface BaseView {
    void showLoading();

    void dismissLoading();

    void loadContentError();

    void memoryRelease();
}
