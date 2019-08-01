package com.kosan.ester.tugas.movie.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;


import com.kosan.ester.tugas.movie.BuildConfig;
import com.kosan.ester.tugas.movie.R;
import com.kosan.ester.tugas.movie.model.Movie;
import com.kosan.ester.tugas.movie.network.RestClient;
import com.kosan.ester.tugas.movie.network.response.ResponseMovie;
import com.kosan.ester.tugas.movie.utils.Contants;
import com.kosan.ester.tugas.movie.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReleaseTodayService extends BroadcastReceiver{

    private static final String TAG = ReleaseTodayService.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        checkUpCommingMovie(context);
    }

    /**
     * The method check Up Comming Movie Release Today
     *
     * @param context used for sending context from onReceive
     */
    private void checkUpCommingMovie(final Context context){

        RestClient mRestClient = new RestClient();

        mRestClient.getApiService().getUpCommingMovies(BuildConfig.API_KEY,"en-US", 1). enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                System.gc();
                // Success Method
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        showIterateNotification(context, response.body().getMovieDetailList());
                    }
                }else{
                    // Fail to Response and recrusive method
                    checkUpCommingMovie(context);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                Log.e(TAG,t.getMessage());
                // Connection Problem and recrusive Method
                checkUpCommingMovie(context);

            }
        });
    }


    /**
     * The Method used for iteration notification
     *
     * @param context used for
     * @param listMovies used for view movie from API upcomming
     */
    private void showIterateNotification(Context context,List<Movie> listMovies){
        int count = Contants.NOTIFICATION_RELEASE_TODAY_REMINDER;

        // Iteration for release date for 07:00 AM
        if(listMovies !=null && listMovies.size() > 0){
            for(Movie movie : listMovies){
                String releaseDate = Utils.toReleaseDate(movie.getReleaseDate()).toString();
                if(!releaseDate.equalsIgnoreCase("Comming Soon") &&
                        isToday(releaseDate)){
                    String titleMovie = movie.getTitle();
                    String descMovie = movie.getTitle() +" "+context.getString(R.string.release_today_content);

                    // Show Notification
                    Utils.createNotification(context,titleMovie,descMovie,
                            count);
                    count++;
                }
            }
        }

    }

    /**
     *  Check  Release Movie Today.
     *
     * @param startDate used for parameter release date from upcomming API
     * @return compare today and upcomming data
     */
    private boolean isToday(String startDate){
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        String today = targetFormat.format(new Date());
        return startDate.equalsIgnoreCase(today);
    }
}
