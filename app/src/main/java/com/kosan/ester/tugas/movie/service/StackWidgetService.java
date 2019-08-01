package com.kosan.ester.tugas.movie.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.kosan.ester.tugas.movie.view.widget.StackRemoteViewsFactory;



public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}