package com.kosan.ester.tugas.movie.model;

import com.kosan.ester.tugas.movie.network.response.ResponseGenres;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Genres {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    private int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static List<Genres> findGenre(int[] value) {

        List<Genres> output = new ArrayList<>();
        for (int aValue : value) {
            for (Genres genres : getDataSetGenre()) {
                if (genres.getId() == aValue) {
                    output.add(genres);
                    break;
                }
            }
        }
        return output;
    }

    private static List<Genres> getDataSetGenre() {
        String json = "{\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":12,\"name\":\"Adventure\"},{\"id\":16,\"name\":\"Animation\"},{\"id\":35,\"name\":\"Comedy\"},{\"id\":80,\"name\":\"Crime\"},{\"id\":99,\"name\":\"Documentary\"},{\"id\":18,\"name\":\"Drama\"},{\"id\":10751,\"name\":\"Family\"},{\"id\":14,\"name\":\"Fantasy\"},{\"id\":36,\"name\":\"History\"},{\"id\":27,\"name\":\"Horror\"},{\"id\":10402,\"name\":\"Music\"},{\"id\":9648,\"name\":\"Mystery\"},{\"id\":10749,\"name\":\"Romance\"},{\"id\":878,\"name\":\"Science Fiction\"},{\"id\":10770,\"name\":\"TV ResponseMovie\"},{\"id\":53,\"name\":\"Thriller\"},{\"id\":10752,\"name\":\"War\"},{\"id\":37,\"name\":\"Western\"}]}";
        ResponseGenres objectsList = new Gson().fromJson(json, ResponseGenres.class);

        return objectsList.getGenres();
    }

}
