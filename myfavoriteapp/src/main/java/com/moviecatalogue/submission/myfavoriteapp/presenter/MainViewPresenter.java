package com.moviecatalogue.submission.myfavoriteapp.presenter;


import com.moviecatalogue.submission.myfavoriteapp.view.activity.MainView;


public class MainViewPresenter {
    private final MainView mView;

    public MainViewPresenter(MainView view) {
        mView = view;
    }
    public void loadData(){
        mView.load();
    }
}
