package com.htistelecom.htisinhouse.activity.WFMS

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.utilities.Utilities
import io.socket.client.IO
import java.net.URISyntaxException
import java.util.*


class MyApplication : Application() {




    override fun onCreate() {
        super.onCreate()



        if (Utilities.isNetConnected(this))
            callmethod()

        //  DeviceUtils.setWindowDimensions(this);
//        Thread.setDefaultUncaughtExceptionHandler {
//            thread, ex -> handleUncaughtException(thread, ex)
//        }
    }

//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(LocaleHelper.setLocale(base))
//    }
//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        LocaleHelper.setLocale(this)
//    }
    companion object socketObj {
        private val instance: MyApplication? = null
        var context: Activity? = null
        var mSocket: io.socket.client.Socket? = null

        fun createSocket() {
            try {
                mSocket = IO.socket("http://chatserver.htistelecom.in")
                mSocket!!.connect()
                mSocket!!.connected()
            } catch (e: URISyntaxException) {
                Log.e("jags", e.message)
            }
        }
    }

    fun handleUncaughtException(thread: Thread?, e: Throwable) {
        val stackTrace = Log.getStackTraceString(e)
        val message = e.message
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("shobinkumar25@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "MyApp Crash log file")
        intent.putExtra(Intent.EXTRA_TEXT, stackTrace)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // required when starting from Application
        startActivity(intent)
    }


    fun callmethod() {
        FirebaseApp.initializeApp(this)
        FirebaseRemoteConfig.getInstance().apply {
            //set this during development
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build()
            setConfigSettingsAsync(configSettings)
            //set this during development

            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate().addOnCompleteListener { task ->
                val updated = task.result
                if (task.isSuccessful) {
                    val updated = task.result
                 //   Log.d("TAG", "Config params updated: $updated")
                } else {
                   // Log.d("TAG", "Config params updated: $updated")
                }
            }
        }
    }


}