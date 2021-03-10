package com.htistelecom.htisinhouse.retrofit;

import com.htistelecom.htisinhouse.activity.WFMS.activity.MainActivityNavigation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClients {
    private static Retrofit retrofit;
     private static final String BASE_URL = "http://wfmtestapi.htistelecom.in/api/";
    //private static final String BASE_URL = "http://wfmapi.htistelecom.in/api/";

    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60000, TimeUnit.SECONDS)
            .readTimeout(60000, TimeUnit.SECONDS)
            .writeTimeout(60000, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " +MainActivityNavigation.Companion.getToken()).build();
                    return chain.proceed(request);
                }
            })
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static <S> S createService(
            Class<S> serviceClass) {
        return getClient().create(serviceClass);
    }
}
