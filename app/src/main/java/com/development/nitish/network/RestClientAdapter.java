package com.development.nitish.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nitish Singh Rathore on 25/6/17.
 */

public class RestClientAdapter {

    private RestClientAdapter() {
    }


    public static <A> A createRestAdapter(Class<A> AdapterClass, String baseUrl, Context context) {


        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(RestClient.getClientInstance(context))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return restAdapter.create(AdapterClass);


    }
}
