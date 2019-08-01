package com.kosan.ester.tugas.movie.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosan.ester.tugas.movie.R;
import com.kosan.ester.tugas.movie.model.Movie;
import com.kosan.ester.tugas.movie.utils.Utils;
import com.kosan.ester.tugas.movie.view.activity.iview.GeneralView;
import com.kosan.ester.tugas.movie.view.adapter.viewholder.BaseViewHolder;
import com.kosan.ester.tugas.movie.view.adapter.viewholder.ProgressViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Movie> mDataset = new ArrayList<>();
    private final GeneralView mView;
    private final static int VIEW_PROGRESS = 0;
    private final static int VIEW_ITEM = 1;

    public MoviesAdapter(GeneralView mView) {
        this.mView = mView;
    }

    public void addList(List<Movie> data) {
        removeProgressBar();
        mDataset.addAll(data);
        addProgressBar();
        this.notifyDataSetChanged();
    }

    private void addProgressBar() {
        mDataset.add(null);
    }

    public void removeProgressBar() {
        if (getItemCount() > 0) {
            if (mDataset.get(getItemCount() - 1) == null) {
                mDataset.remove(getItemCount() - 1);
                notifyItemRemoved(getItemCount());
            }
        }
    }

    public void clearList() {
        mDataset.clear();
        this.notifyDataSetChanged();
    }

    public List<Movie> getDataset() {
        return mDataset;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_item_movie, viewGroup, false);
            return new MovieViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_item_progressbar, viewGroup, false);
            return new ProgressViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_ITEM) {
            MovieViewHolder movieVH = (MovieViewHolder) viewHolder;
            Movie movie = mDataset.get(position);

            Utils.loadImage(movieVH.ivThumbnails, movie.getPosterPath());
            movieVH.tvTitle.setText(movie.getTitle());
            movieVH.tvDescription.setText(movie.getOverview());
        } else {
            ProgressViewHolder progressVH = (ProgressViewHolder) viewHolder;
            progressVH.pbLoading.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    class MovieViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_thumbnails)
        ImageView ivThumbnails;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_description)
        TextView tvDescription;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.goToNextActivity(mDataset.get(getAdapterPosition()));
                }
            });
        }
    }

}
