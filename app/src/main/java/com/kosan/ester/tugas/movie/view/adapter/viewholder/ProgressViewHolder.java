package com.kosan.ester.tugas.movie.view.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;


import com.kosan.ester.tugas.movie.R;

import butterknife.BindView;


public class ProgressViewHolder extends BaseViewHolder {

    @BindView(R.id.pb_loading)
    public ProgressBar pbLoading;

    public ProgressViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
