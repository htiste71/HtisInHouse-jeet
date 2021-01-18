//package com.htistelecom.htisinhouse.retrofit
//
//import android.content.Context
//import android.net.ConnectivityManager
//import com.htistelecom.htisinhouse.activity.WFMS.exceptions.NetworkException
//import okhttp3.Interceptor
//import okhttp3.Response
//
//class NetworkConnectionInterceptor(context: Context) : Interceptor {
//    private val applicationContext = context.applicationContext
//    override fun intercept(chain: Interceptor.Chain): Response {
//        if (!isInternetAvailable()) {
//            throw NetworkException("No Internet Available")
//            return chain.proceed(chain.request())
//        }
//    }
//
//    fun isInternetAvailable(): Boolean {
//        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        cm.activeNetworkInfo.also { return it != null && it.isConnected }
//    }
//
//}