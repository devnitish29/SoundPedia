package com.development.nitish.restapi;

import com.development.nitish.model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nitish Singh Rathore on 25/6/17.
 */

public interface SongApi {

    //https://itunes.apple.com/search?term=ade


    @GET("search")
    Call<Song> getSongs(@Query(value = "term", encoded = true) String artistName);
}
