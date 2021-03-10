package com.htistelecom.htisinhouse.services;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.htistelecom.htisinhouse.R;
import com.htistelecom.htisinhouse.activity.WFMS.activity.SplashNew;
import com.htistelecom.htisinhouse.activity.WFMS.interfaces.ILocationService;
import com.htistelecom.htisinhouse.config.TinyDB;

import org.jetbrains.annotations.NotNull;


public class LocationServiceHtis extends Service {
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private final LocationServiceHtis.LocationServiceBinder binder = new LocationServiceHtis.LocationServiceBinder();
    private IBinder mBinder = new MyBinder();

    LocationManager locationManager;
    private static final String TAG = "LocationServiceHtis";
    private SplashNew  act;



    /*public LocationServiceHtis(){
        tinyDB=new TinyDB(LocationServiceHtis.this);
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initializeLocationManager() {

        startLocationUpdates();
    }


    public class MyBinder extends Binder implements ILocationService {
       public LocationServiceHtis receiveService() {
            return LocationServiceHtis.this;
        }

        @Override
        public void sendContext(@NotNull SplashNew activity) {
            act=activity;
            createLocationRequest();
        }
    }



    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(act, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(act, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(act,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private void startLocationUpdates() {
        LocationRequest mLocationRequest;
        FusedLocationProviderClient mFusedLocationClient;
        LocationCallback locationCallback;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(LocationServiceHtis.this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(20 * 1000);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

            }
        };


    }

    public void startTracking() {
        initializeLocationManager();
    }

    public void stopTracking() {
        this.onDestroy();
       /* if (mFusedLocationClient != null) {
            try {
                mFusedLocationClient.removeLocationUpdates(locationCallback);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listners, ignore", ex);
            }
        }*/
    }


    public class LocationServiceBinder extends Binder {
        public LocationServiceHtis getService() {
            return LocationServiceHtis.this;
        }
    }






}