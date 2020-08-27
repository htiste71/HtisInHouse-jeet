package com.htistelecom.htisinhouse.activity.WFMS.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS;
import com.htistelecom.htisinhouse.activity.WFMS.models.FileModel;
import com.htistelecom.htisinhouse.config.TinyDB;
import com.htistelecom.htisinhouse.utilities.Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class PreOreoLocationService extends Service {

    private final String TAG = "PreOreoLocationService";
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private  LocationListener mLocationListener;
    private LocationManager mLocationManager;

    private final int LOCATION_INTERVAL = 5000;
    private final int LOCATION_DISTANCE = 0;

    TinyDB tinyDB;
    private File file;

    private File myFile;
    private String mDate;
    private String mDateTime;
    private String mUserId="";

   /* public PreOreoLocationService(Context applicationContext) {
        super();
        context = applicationContext;

        Log.e("HERE", "here service created!");
    }*/

    private class LocationListener implements android.location.LocationListener {
        private Location lastLocation = null;
        private final String TAG = "LocationListener";
        private Location mLastLocation;

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            OreoLocationService.LATITUDE = location.getLatitude() + "";
            OreoLocationService.LONGITUDE = location.getLongitude() + "";
            Utilities.showToast(PreOreoLocationService.this,OreoLocationService.LATITUDE+" Pre");
            mUserId = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID);

            if(!mUserId.equalsIgnoreCase(""))
                addDataToFile(OreoLocationService.LATITUDE,OreoLocationService.LONGITUDE);



        }



        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            try {
                // Log.e(TAG, "LocationChanged: " + mLastLocation);
                Log.e(TAG, "onStatusChanged: " + status);
               // loadTrackingData();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

  

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        tinyDB = new TinyDB(PreOreoLocationService.this);
        file = new File(Environment.getExternalStorageDirectory(), "MyFiles");
        if (!file.exists()) {
            file.mkdirs();
        }
        currentDateTime();

        try {
            Log.e(TAG, "Tracking ");
            startTracking();


//            handler = new Handler(Looper.getMainLooper());
//            runnable = new Runnable() {
//                public void run() {
//                    // Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
//
//                    // toast("Service is still running");
//
//                    //Log.e("htis", "enque done");
//                    handler.postDelayed(runnable, 1000);
//                }
//            };
//
//            handler.postDelayed(runnable, 2500);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

//    final Handler mHandler = new Handler();
//
//    void toast(final CharSequence text) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(PreOreoLocationService.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        Log.e("", "destroy");
        sendBroadcast(new Intent("YouWillNeverKillMe"));


        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Intent i = new Intent(this, PreOreoLocationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);

        Intent intent = new Intent("com.htistelecom.htisinhouse");
        sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void initializeLocationManager() {
        Log.e(TAG, "service: loc initialized");
        if (mLocationManager == null) {
            Log.e(TAG, "service: loc null");
            mLocationManager = (LocationManager) (PreOreoLocationService.this).getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void startTracking() {
        initializeLocationManager();
        mLocationListener = new PreOreoLocationService.LocationListener(LocationManager.GPS_PROVIDER);
        Log.e(TAG, "service: startTracking");
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener);
            Log.e(TAG, "startTracking: " + mLocationListener.mLastLocation);
        } catch (SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }



    private String getDateTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);//"yyyyMMddHHmmssSS");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.e(TAG, "onTaskRemoved()");
        sendBroadcast(new Intent("YouWillNeverKillMe"));


       /* Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);

        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),3600*1000*24 ,pendingIntent);
*/


        Intent i = new Intent(context, PreOreoLocationService.class);
        context.startService(i);
        super.onTaskRemoved(rootIntent);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "onLowMemory()");
    }
    private void currentDateTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        mDateTime = simpleDateTimeFormat.format(date);
        mDate = simpleDateFormat.format(date);
    }

    private void addDataToFile(String latitude, String longitude) {
        FileWriter fr = null;
        try {
            BufferedWriter br = null;
            boolean isAvailable = false;
            ArrayList<FileModel> al = new Gson().fromJson(tinyDB.getString(ConstantsWFMS.TINYDB_MYFILE), new TypeToken<ArrayList<FileModel>>() {
            }.getType());


            if (al != null && al.size() > 0) {

                for (int i = 0; i < al.size(); i++) {
                    if (!al.get(i).getFileDate().equalsIgnoreCase(mDate)) {

                    } else {
                        isAvailable = true;
                        break;
                    }
                }
                currentDateTime();
                myFile = new File(file, "" + mUserId + "_" + mDate + ".txt");

                if (isAvailable) {
                    fr = new FileWriter(myFile, true);
                    br = new BufferedWriter(fr);
                    br.write(latitude + "#" + longitude + "#" + mDateTime + ",");

//                    FileModel fileModel = new FileModel();
//                    fileModel.setFileDate(mDate);
//                    fileModel.setFilepath(myFile.getAbsolutePath());
//                    fileModel.setId(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID));
//                    al.add(fileModel);
//                    tinyDB.putString(ConstantsWFMS.TINYDB_MYFILE, new Gson().toJson(al));

                } else {

                    myFile.createNewFile();
                    fr = new FileWriter(myFile, true);
                    br = new BufferedWriter(fr);
                    br.write(latitude + "#" + longitude + "#" + mDateTime + ",");

                    FileModel fileModel = new FileModel();
                    fileModel.setFileDate(mDate);
                    fileModel.setFilepath(myFile.getAbsolutePath());
                    fileModel.setId(mUserId);
                    al.add(fileModel);
                    tinyDB.putString(ConstantsWFMS.TINYDB_MYFILE, new Gson().toJson(al));

                }


            } else {
                myFile = new File(file, "" + mUserId + "_" + mDate + ".txt");

                myFile.createNewFile();
                fr = new FileWriter(myFile, true);
                br = new BufferedWriter(fr);
                br.write(latitude + "#" + longitude + "#" + mDateTime + ",");
                if (!isAvailable) {
                    if (al == null)
                        al = new ArrayList<>();
                    FileModel fileModel = new FileModel();
                    fileModel.setFileDate(mDate);
                    fileModel.setFilepath(myFile.getAbsolutePath());
                    fileModel.setId(mUserId);
                    al.add(fileModel);
                    tinyDB.putString(ConstantsWFMS.TINYDB_MYFILE, new Gson().toJson(al));
                }
            }

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//                saveLatLng(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
//                PreOreoLocationService();
    }
}

