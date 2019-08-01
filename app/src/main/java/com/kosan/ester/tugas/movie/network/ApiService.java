package com.kosan.ester.tugas.movie.network;


import com.kosan.ester.tugas.movie.network.response.ResponseMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiService {
    @GET("search/movie")
    Call<ResponseMovie> doSearchMovies(@Query("api_key") String apiKey,
                                       @Query("language") String language,
                                       @Query("page") int page,
                                       @Query("query") String searchText);

    @GET("discover/movie")
    Call<ResponseMovie> getAllMovies(@Query("api_key") String apiKey,
                                     @Query("language") String language,
                                     @Query("page") int page,
                                     @Query("primary_release_date.gte") String afterDateRelease,
                                     @Query("primary_release_date.lte") String beforeDateRelease);



    @GET("movie/now_playing")
    Call<ResponseMovie> getNowPlayingMovies(@Query("api_key") String apiKey,
                                            @Query("language") String language,
                                            @Query("page") int page);

    @GET("movie/upcoming")
    Call<ResponseMovie> getUpCommingMovies(@Query("api_key") String apiKey,
                                     @Query("language") String language,
                                     @Query("page") int page);
}
