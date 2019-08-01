package com.kosan.ester.tugas.movie.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.kosan.ester.tugas.movie.R;
import com.kosan.ester.tugas.movie.database.SharedPrefs;
import com.kosan.ester.tugas.movie.presenter.activity.SettingPresenter;
import com.kosan.ester.tugas.movie.utils.PrefsConst;
import com.kosan.ester.tugas.movie.view.activity.iview.SettingView;

import butterknife.BindView;


public class SettingActivity extends BaseActivity implements SettingView {

    @BindView(R.id.ll_wrapper)
    protected LinearLayout ll_wrapper;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.sc_remember_app)
    protected SwitchCompat scRememberApp;

    @BindView(R.id.sc_remember_release_movie)
    protected SwitchCompat scRememberRelease;

    private SettingPresenter mSettingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Init Presenter
        mSettingPresenter = new SettingPresenter(getApplicationContext(), this);

        // Load View
        setupToolbar();
        loadView();
        setListener();
    }

    @Override
    public void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_bar_setting);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return false;
    }

    @Override
    public void loadView() {
        scRememberApp.setChecked(SharedPrefs.getBoolean(PrefsConst.PREFS_REMEMBER_APP,
                true));
        scRememberRelease.setChecked(SharedPrefs.getBoolean(PrefsConst.PREFS_REMEMBER_RELEASE_MOVIE,
                true));
    }

    @Override
    public void setListener() {
        scRememberApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Check
                if (isChecked)
                    mSettingPresenter.setAlarm(SettingPresenter.TypeAlarm.DAILY_REMINDER, true);
                else
                    mSettingPresenter.cancelAlarm(SettingPresenter.TypeAlarm.DAILY_REMINDER, false);

            }
        });


        scRememberRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Check
                if (isChecked)
                    mSettingPresenter.setAlarm(SettingPresenter.TypeAlarm.RELEASE_MOVIE, true);
                else
                    mSettingPresenter.cancelAlarm(SettingPresenter.TypeAlarm.RELEASE_MOVIE, false);

            }
        });

    }

    @Override
    public void showMessage(String text) {
        Snackbar.make(ll_wrapper,
                text,
                Snackbar.LENGTH_SHORT).show();
    }
}
