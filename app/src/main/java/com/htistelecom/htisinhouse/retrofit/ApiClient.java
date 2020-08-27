package com.htistelecom.htisinhouse.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiClient {
    private static Retrofit retrofit;
   // private static final String BASE_URL = "http://htistestapi.htistelecom.in/api/";

    private static final String BASE_URL = "http://wfmapi.htistelecom.in/api/";

    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60000, TimeUnit.SECONDS)
            .readTimeout(60000, TimeUnit.SECONDS)
            .writeTimeout(60000, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}