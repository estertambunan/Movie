package com.kosan.ester.tugas.movie.view.activity.iview;

import android.support.v4.app.Fragment;

public interface MainView {
    void setupToolbar();
    void changeTitleBar(String text);
    void replaceFragment(Fragment view, String tag);
    void setupListener();
    void applySetting();
}
