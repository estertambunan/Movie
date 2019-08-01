package com.kosan.ester.tugas.movie.network.response;

import com.kosan.ester.tugas.movie.model.Genres;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ResponseGenres {
    @SerializedName("genres")
    private List<Genres> genres = new ArrayList<>();

    public List<Genres> getGenres() {
        return genres;
    }

}
