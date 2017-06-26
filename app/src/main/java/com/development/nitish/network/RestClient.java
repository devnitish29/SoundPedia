package com.development.nitish.network;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Nitish Singh Rathore on 25/6/17.
 */

public class RestClient {

    private static OkHttpClient mClient;


    private RestClient() {
        mClient = new OkHttpClient();
    }

    public static OkHttpClient getClientInstance(final Context context) {
        final int cacheSize = 30 * 1024 * 1024; //30MB


        //creating request Interceptor
        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl orginalUrl = original.url();
                HttpUrl url = orginalUrl.newBuilder()
                        .build();
                Request newRequest;
                newRequest = original.newBuilder()
                        .url(url)
                        .header("Accept", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        //creating log Interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        if (mClient == null) {
            synchronized (RestClient.class) {
                if (mClient == null) {
                    mClient = new OkHttpClient().newBuilder()
                            .cache(new Cache(context.getApplicationContext().getCacheDir(), cacheSize))
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)

                            .addInterceptor(logging)

                            .addInterceptor(requestInterceptor)
                            .build();


                }
            }
        }
        return mClient;
    }
}
