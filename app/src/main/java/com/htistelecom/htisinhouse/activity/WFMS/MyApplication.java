package com.htistelecom.htisinhouse.activity.WFMS;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;


public class MyApplication extends Application {

    private static MyApplication instance;
    public static Activity context;


    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override

            public void uncaughtException(Thread thread, Throwable ex) {

                handleUncaughtException(thread, ex);

            }

        });


    }

    public void handleUncaughtException(Thread thread, Throwable e) {

        String stackTrace = Log.getStackTraceString(e);

        String message = e.getMessage();

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("message/rfc822");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"shobinkumar25@gmail.com"});

        intent.putExtra(Intent.EXTRA_SUBJECT, "MyApp Crash log file");

        intent.putExtra(Intent.EXTRA_TEXT, stackTrace);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application

        startActivity(intent);

    }


    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


}
