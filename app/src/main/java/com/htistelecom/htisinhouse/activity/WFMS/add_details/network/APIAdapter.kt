package com.htistelecom.htisinhouse.activity.WFMS.add_details.network

import com.htistelecom.htisinhouse.activity.WFMS.activity.MainActivityNavigation.Companion.token
import com.htistelecom.htisinhouse.retrofit.ApiClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiAdapter {
    val apiClient: APIClient = Retrofit.Builder()
            .baseUrl("http://wfmtestapi.htistelecom.in/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIClient::class.java)


}

private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60000, TimeUnit.SECONDS)
        .readTimeout(60000, TimeUnit.SECONDS)
        .writeTimeout(60000, TimeUnit.SECONDS)
        .addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build()
                return chain.proceed(request)
            }
        })
        .build()