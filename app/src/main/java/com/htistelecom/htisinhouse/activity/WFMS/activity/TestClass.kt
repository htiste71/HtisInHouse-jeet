package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.htistelecom.htisinhouse.R

class TestClass : AppCompatActivity() {
    var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_company)
        callmethod()
        //  initializeFirebase();
    } //    public void initializeFirebase() {
    //        if (FirebaseApp.getApps(this).isEmpty()) {
    //            FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
    //        }
    //          mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    //        // [END get_remote_config_instance]
    //
    //        // Create a Remote Config Setting to enable developer mode, which you can use to increase
    //        // the number of fetches available per hour during development. See Best Practices in the
    //        // README for more information.
    //        // [START enable_dev_mode]
    //        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
    //                .setMinimumFetchIntervalInSeconds(0)
    //                .build();
    //        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    //        // [END enable_dev_mode]
    //
    //        // Set default Remote Config parameter values. An app uses the in-app default values, and
    //        // when you need to adjust those defaults, you set an updated value for only the values you
    //        // want to change in the Firebase console. See Best Practices in the README for more
    //        // information.
    //        // [START set_default_values]
    //        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
    //        // [END set_default_values]
    //        fetch();
    //    }
    //
    //    void fetch() {
    //        long cacheExpiration = 3600; // 1 hour in seconds.
    //        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
    //        // retrieve values from the service.
    ////        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
    ////            cacheExpiration = 0;
    ////        }
    //        mFirebaseRemoteConfig.fetch(cacheExpiration)
    //                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
    //                    @Override
    //                    public void onComplete(@NonNull Task<Void> task) {
    //                        if (task.isSuccessful()) {
    //                            Toast.makeText(TestClass.this, "Fetch Succeeded",
    //                                    Toast.LENGTH_SHORT).show();
    //                            //isFetched = true;
    //                            // After config data is successfully fetched, it must be activated before newly fetched
    //                            // values are returned.
    //                            //   mFirebaseRemoteConfig.activateFetched();
    //                        } else {
    ////                            fetch();
    ////                            Toast.makeText(SplashScreenActivity.this, "Fetch Failed",
    ////                                    Toast.LENGTH_SHORT).show();
    //                        }
    //                        // displayWelcomeMessage();
    //                    }
    //                });
    //    }
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