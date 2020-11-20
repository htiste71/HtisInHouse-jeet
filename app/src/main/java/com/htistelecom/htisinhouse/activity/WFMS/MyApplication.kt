package com.htistelecom.htisinhouse.activity.WFMS

import android.app.Application
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import android.content.Intent
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication
import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.htistelecom.htisinhouse.R

class MyApplication : Application() {
    private val mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    var isFetched = false
    override fun onCreate() {
        super.onCreate()
        callmethod()

        //  DeviceUtils.setWindowDimensions(this);
        Thread.setDefaultUncaughtExceptionHandler { thread, ex -> handleUncaughtException(thread, ex) }
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

    companion object {
        private val instance: MyApplication? = null
        var context: Activity? = null
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
                    Log.d("TAG", "Config params updated: $updated")
                } else {
                    Log.d("TAG", "Config params updated: $updated")
                }
            }
        }
    }
}