package com.htistelecom.htisinhouse.activity.WFMS.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htistelecom.htisinhouse.R;
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS;
import com.htistelecom.htisinhouse.activity.WFMS.activity.SplashActivityWFMS;
import com.htistelecom.htisinhouse.activity.WFMS.models.FileModel;
import com.htistelecom.htisinhouse.config.TinyDB;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class OreoLocationService extends JobIntentService {

    private final String TAG = "SrvTrackingHtis";
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private NotificationManager notificationManager;


    private final int LOCATION_INTERVAL = 5000;
    private final int LOCATION_DISTANCE = 0;

    static final int JOB_ID = 1000;
    public Context context = this;
  //  public Handler handler = null;
    public static Runnable runnable = null;
    Boolean isTrackingOn = false;

    private File file;
    private TinyDB tinyDB;
    private File myFile;
    private String mDate;
    private String mDateTime;
    private String mUserId="";
    public static String LATITUDE = "", LONGITUDE = "";


    @Override
    public void onCreate() {
       initViews();

        Log.e(TAG, "onCreate");
        try {
            startForeground(12345678, getNotification());
            startTracking();
        } catch (Exception ex) {
            Log.e("htis", ex.getMessage());
            toast(ex.getMessage());
        }

//        handler = new Handler(Looper.getMainLooper());
//        runnable = new Runnable() {
//            public void run() {
//
//                handler.postDelayed(runnable, 1000);
//            }
//        };
//
//        handler.postDelayed(runnable, 2500);
    }

    private void initViews() {
        tinyDB = new TinyDB(this);
        file = new File(Environment.getExternalStorageDirectory(), "MyFiles");
        if (!file.exists()) {
            file.mkdirs();
        }
        currentDateTime();

        mUserId = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
            LATITUDE = location.getLatitude() + "";
            LONGITUDE = location.getLongitude() + "";
          //  Utilities.showToast(OreoLocationService.this,LATITUDE+" Oreo");
            mUserId = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID);

            if(!mUserId.equalsIgnoreCase(""))
            addDataToFile(LATITUDE, LONGITUDE);

//                if (distanceBetweenLatLng(0, 0, location.getLatitude(), location.getLongitude()) >= 0) {
//                    FileWriter fr = null;
//                    try {
//
//                        Date date = new Date();
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//
//                        String mDateTime=simpleDateTimeFormat.format(date);
//                        String mDate = simpleDateFormat.format(date);
//                        String mUserId = tinyDB.getString("EmpId");
//
//                        File gpxfile = new File(file, "" + mUserId + "_" + mDate + ".txt");
//                        fr = new FileWriter(gpxfile, true);
//                        BufferedWriter br = new BufferedWriter(fr);
//                        br.write(location.getLatitude() + "#" + location.getLongitude() +"#"+ mDateTime+",");
//                        //  tinyDB.putString("file",gpxfile.getAbsolutePath());
//
//                        boolean isAvailable = false;
//                        ArrayList<FileModel> al = new Gson().fromJson(tinyDB.getString("my_file"), new TypeToken<ArrayList<FileModel>>() {
//                        }.getType());
//                        if (al != null) {
//
//                            for (int i = 0; i < al.size(); i++) {
//                                if (!al.get(i).getFileDate().equalsIgnoreCase(mDate)) {
//
//                                } else {
//                                    isAvailable = true;
//                                    break;
//                                }
//                            }
//                            if (!isAvailable) {
//                                if (al == null)
//                                    al = new ArrayList<>();
//                                FileModel fileModel = new FileModel();
//                                fileModel.setFileDate(mDate);
//                                fileModel.setFilepath(gpxfile.getAbsolutePath());
//                                fileModel.setId(tinyDB.getString("EmpId"));
//                                al.add(fileModel);
//                                tinyDB.putString("my_file", new Gson().toJson(al));
//                            }
//                        } else {
//                            if (!isAvailable) {
//                                if (al == null)
//                                    al = new ArrayList<>();
//                                FileModel fileModel = new FileModel();
//                                fileModel.setFileDate(mDate);
//                                fileModel.setFilepath(gpxfile.getAbsolutePath());
//                                fileModel.setId(tinyDB.getString("EmpId"));
//                                al.add(fileModel);
//                                tinyDB.putString("my_file", new Gson().toJson(al));
//                            }
//                        }
//
//                        br.close();
//                        fr.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
////                saveLatLng(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
////                PreOreoLocationService();
//                }



//            Log.i(TAG, "LocationChanged: " + location);
//            Toast.makeText(SrvTrackingHtis.this, "" + location.getLatitude() + location.getLongitude(), Toast.LENGTH_SHORT).show();
//
//            FileWriter fr = null;
//            try {
//                File gpxfile = new File(file, "myloc.txt");
//                fr = new FileWriter(gpxfile, true);
//                BufferedWriter br = new BufferedWriter(fr);
//                br.write(location.getLatitude() + "#" + location.getLongitude() + ",");
//
//                br.close();
//                fr.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            // Toast.makeText(SrvTrackingHtis.this, "Lat:"+location.getLatitude()+", Lng:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
            // toast("Lat:"+location.getLatitude()+", Lng:"+location.getLongitude());
//            if (distanceBetweenLatLng(0, 0, location.getLatitude(), location.getLongitude()) >= 2) {
//                saveLatLng(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
//                PreOreoLocationService();
//            }


        }



        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            notificationUpdater("Tracking paused");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
            notificationUpdater("Tracking On");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            try {
                // Log.e(TAG, "LocationChanged: " + mLastLocation);
                Log.e(TAG, "onStatusChanged: " + status);
                //PreOreoLocationService();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, OreoLocationService.class, JOB_ID, work);
        Log.e("htis", "enque done");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }

    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
              //  Toast.makeText(OreoLocationService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("htis", "onTaskRemoved");
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mLocationListener);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listners, ignore", ex);
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void startTracking() {
        initializeLocationManager();
        mLocationListener = new LocationListener(LocationManager.GPS_PROVIDER);

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener);
            Log.e(TAG, "startTracking: " + mLocationListener.mLastLocation);
        } catch (SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }


    public void stopTracking() {
        this.onDestroy();
    }

    Notification.Builder builder = null;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private Notification getNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = null;
            channel = new NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(getApplicationContext(), "channel_01")
                    .setAutoCancel(false)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setVibrate(new long[]{1000, 1000})
                    .setSubText("Do not close this app!")
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentTitle("WFMS");

        } else {
            builder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Do not close this app")
                    .setContentTitle("WFMS")
                    .setAutoCancel(false);
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(111, builder.build());

        }
        isTrackingOn = true;
        return builder.build();
    }

    private static final int NOTIF_ID = 12345678;


    private void notificationUpdater(String txt) {
        if (isTrackingOn) {
            builder.setContentText(txt);
            Notification notification = builder.getNotification();
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(NOTIF_ID, builder.build());
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

//    private double distanceBetweenLatLng(double lat1, double lng1, double lat2, double lng2) {
//        tinyDB = new TinyDB(getApplicationContext());
//
//        String lat = tinyDB.getString("LastLatitude");
//        String lng = tinyDB.getString("LastLongitude");
//
//        if (!lat.isEmpty() && lat != null && !lng.isEmpty() && lng != null) {
//            lat1 = Double.parseDouble(lat);
//            lng1 = Double.parseDouble(lng);
//        } else {
//            tinyDB.putString("LastLatitude", String.valueOf(lat2));
//            tinyDB.putString("LastLongitude", String.valueOf(lng2));
//            return 100;
//        }
//        Location startPoint = new Location("locationA");
//        startPoint.setLatitude(lat1);
//        startPoint.setLongitude(lng1);
//
//        Location endPoint = new Location("locationA");
//        endPoint.setLatitude(lat2);
//        endPoint.setLongitude(lng2);
//
//        double distance = startPoint.distanceTo(endPoint);
//        if (distance >= 2) {
//            tinyDB.putString("LastLatitude", String.valueOf(lat2));
//            tinyDB.putString("LastLongitude", String.valueOf(lng2));
//        }
//        return distance;
//    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("htis", "onTaskRemoved");
        Intent i = new Intent(context, SplashActivityWFMS.class);
        context.startService(i);


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
