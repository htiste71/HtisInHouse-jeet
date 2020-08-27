//package com.htistelecom.htisinhouse.services;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Looper;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.JobIntentService;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.htistelecom.htisinhouse.R;
//import com.htistelecom.htisinhouse.config.TinyDB;
//import com.htistelecom.htisinhouse.db.DBHandler;
//import com.htistelecom.htisinhouse.retrofit.ApiClient;
//import com.htistelecom.htisinhouse.retrofit.ApiInterface;
//import com.htistelecom.htisinhouse.utilities.CommonFunctions;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class SrvTrackingHtis extends JobIntentService {
//
//    private final String TAG = "SrvTrackingHtis";
//    private LocationListener mLocationListener;
//    private LocationManager mLocationManager;
//    private NotificationManager notificationManager;
//
//
//    private final int LOCATION_INTERVAL = 1000 * 10;
//    private final int LOCATION_DISTANCE = 0;
//    TinyDB tinyDB;
//
//    static final int JOB_ID = 1000;
//    public Context context = this;
//    public Handler handler = null;
//    public static Runnable runnable = null;
//    Boolean isTrackingOn = false;
//
//    public static String LATITUDE = "", LONGITUDE = "";
//
//
//    File file;
//
//    @Override
//    public void onCreate() {
//        file = new File(Environment.getExternalStorageDirectory(), "MyLoc");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//
//        Log.i(TAG, "onCreate");
//        try {
//            startForeground(12345678, getNotification());
//            startTracking();
//        } catch (Exception ex) {
//            Log.i("htis", ex.getMessage());
//            toast(ex.getMessage());
//        }
//
//        handler = new Handler(Looper.getMainLooper());
//        runnable = new Runnable() {
//            public void run() {
//                // Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
//
//                //toast("Service is still running");
//                //Log.i("htis", "enque done");
//                handler.postDelayed(runnable, 3000);
//            }
//        };
//
//        handler.postDelayed(runnable, 4500);
//    }
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    private class LocationListener implements android.location.LocationListener {
//        private Location lastLocation = null;
//        private final String TAG = "LocationListener";
//        private Location mLastLocation;
//
//        public LocationListener(String provider) {
//            mLastLocation = new Location(provider);
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            mLastLocation = location;
//            LATITUDE = location.getLatitude() + "";
//            LONGITUDE = location.getLongitude() + "";
//         /*   if (CommonFunctions.getTime()) {*/
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
////                loadTrackingData();
//                }
//           /* } else {
//
//            }*/
//
//
////            Log.i(TAG, "LocationChanged: " + location);
////            Toast.makeText(SrvTrackingHtis.this, "" + location.getLatitude() + location.getLongitude(), Toast.LENGTH_SHORT).show();
////
////            FileWriter fr = null;
////            try {
////                File gpxfile = new File(file, "myloc.txt");
////                fr = new FileWriter(gpxfile, true);
////                BufferedWriter br = new BufferedWriter(fr);
////                br.write(location.getLatitude() + "#" + location.getLongitude() + ",");
////
////                br.close();
////                fr.close();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
//
//            // Toast.makeText(SrvTrackingHtis.this, "Lat:"+location.getLatitude()+", Lng:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
//            // toast("Lat:"+location.getLatitude()+", Lng:"+location.getLongitude());
////            if (distanceBetweenLatLng(0, 0, location.getLatitude(), location.getLongitude()) >= 2) {
////                saveLatLng(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
////                loadTrackingData();
////            }
//
//
//        }
//
//        public void saveLatLng(String lat, String lng) {
//            tinyDB = new TinyDB(getApplicationContext());
//
//            DBHandler dbHandler = new DBHandler(getApplicationContext());
//            LatLngModal latLngModal = new LatLngModal();
//            latLngModal.setId(1);
//            latLngModal.setLatitude(lat);
//            latLngModal.setLongitude(lng);
//            latLngModal.setCreatedBy(tinyDB.getString("EmpId"));
//            latLngModal.setCreatedDate(getDateTime("dd-MMM-yyyy HH:mm:ss"));
//            boolean isInserted = dbHandler.addHandler(latLngModal);
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.e(TAG, "onProviderDisabled: " + provider);
//            notificationUpdater("Tracking paused");
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.e(TAG, "onProviderEnabled: " + provider);
//            notificationUpdater("Tracking On");
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            try {
//                // Log.e(TAG, "LocationChanged: " + mLastLocation);
//                Log.e(TAG, "onStatusChanged: " + status);
//                loadTrackingData();
//            } catch (Exception ex) {
//                Log.e(TAG, ex.getMessage());
//            }
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        return START_NOT_STICKY;
//    }
//
//    public static void enqueueWork(Context context, Intent work) {
//        enqueueWork(context, SrvTrackingHtis.class, JOB_ID, work);
//        Log.i("htis", "enque done");
//    }
//
//    @Override
//    protected void onHandleWork(@NonNull Intent intent) {
//       /* try {
//            startForeground(12345678, getNotification());
//        }
//        catch (Exception ex)
//        {
//            Log.i("htis", ex.getMessage());
//            toast(ex.getMessage());
//        }
//
//        handler = new Handler(Looper.getMainLooper());
//        runnable = new Runnable() {
//            public void run() {
//                // Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
//
//                toast("Service is still running");
//                //Log.i("htis", "enque done");
//                handler.postDelayed(runnable, 1000);
//            }
//        };
//
//        handler.postDelayed(runnable, 1500);*/
//    }
//
//    final Handler mHandler = new Handler();
//
//    // Helper for showing tests
//    void toast(final CharSequence text) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(SrvTrackingHtis.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.e("htis", "onTaskRemoved");
//        if (mLocationManager != null) {
//            try {
//                mLocationManager.removeUpdates(mLocationListener);
//            } catch (Exception ex) {
//                Log.i(TAG, "fail to remove location listners, ignore", ex);
//            }
//        }
//    }
//
//    private void initializeLocationManager() {
//        if (mLocationManager == null) {
//            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        }
//    }
//
//    public void startTracking() {
//        initializeLocationManager();
//        mLocationListener = new LocationListener(LocationManager.GPS_PROVIDER);
//
//        try {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener);
//            Log.i(TAG, "startTracking: " + mLocationListener.mLastLocation);
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//        }
//    }
//
//
//    public void stopTracking() {
//        this.onDestroy();
//    }
//
//    Notification.Builder builder = null;
//
//    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
//    private Notification getNotification() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = null;
//            channel = new NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//            builder = new Notification.Builder(getApplicationContext(), "channel_01")
//                    .setAutoCancel(false)
//                    .setSmallIcon(R.drawable.ic_stat_name)
//                    .setVibrate(new long[]{1000, 1000})
//                    .setSubText("Do not close this app!")
//                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                    .setContentTitle("HTIS Employee Self Care");
//
//        } else {
//            builder = new Notification.Builder(this)
//                    .setSmallIcon(R.drawable.ic_stat_name)
//                    .setContentText("Do not close this app")
//                    .setContentTitle("HTIS Employee Self Care")
//                    .setAutoCancel(false);
//            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            notificationManager.notify(111, builder.build());
//
//        }
//        isTrackingOn = true;
//        return builder.build();
//    }
//
//    private static final int NOTIF_ID = 12345678;
//
//
//    private void notificationUpdater(String txt) {
//        if (isTrackingOn) {
//            builder.setContentText(txt);
//            Notification notification = builder.getNotification();
//            notification.flags = Notification.FLAG_ONGOING_EVENT;
//            notificationManager.notify(NOTIF_ID, builder.build());
//        }
//    }
//
//    /* public class LocationServiceBinder extends Binder {
//        public SrvTrackingHtis getService() {
//            return SrvTrackingHtis.this;
//        }
//    } */
//
//    public void loadTrackingData() {
//
//        tinyDB = new TinyDB(getApplicationContext());
//        String df = "yyyyMMddHHmmssSS";
//        String dNow = getDateTime(df);
//        String dOld = dNow;
//        dOld = tinyDB.getString("LastUploadTime");
//        if (dOld.equals("")) {
//            dOld = dNow;
//            tinyDB.putString("LastUploadTime", dNow);
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat(df);
//        formatter.setLenient(false);
//        Date dateNowDate = new Date();
//        Date dateLastDate = new Date();
//
//        try {
//            dateNowDate = formatter.parse(dNow);
//            dateLastDate = formatter.parse(dOld);
//        } catch (Exception ex) {
//            Log.e("DateError", ex.getMessage());
//        }
//
//        Long millisSecond = (dateNowDate.getTime() - dateLastDate.getTime());// datediff(dateNow,dateLast);
//        //Long currencyMillisSecond = System.currentTimeMillis ();
////            if (millisSecond < (6 * 10 * 1000)) {
////                return;
////            } else {
//        DBHandler dbHandler = new DBHandler(this);
//        Cursor cursor = dbHandler.findHandler("col_IsSync", "N");
//        //Cursor cursor = dbHandler.getAllData();
//
//        if (cursor.getCount() == 0) {
//            // show message
//            Log.i("LoadTrackingData", "nothing");
//            return;
//        }
//
//          /*StringBuffer buffer = new StringBuffer();
//            while (cursor.moveToNext()) {
//                buffer.append("Id :"+ cursor.getString(0)+"\n");
//                buffer.append("Name :"+ cursor.getString(1)+"\n");
//                buffer.append("Surname :"+ cursor.getString(2)+"\n");
//                buffer.append("Marks :"+ cursor.getString(3)+"\n\n");
//            }*/
//        // Show all data
//        //Log.i("Data",buffer.toString());
//
//        JSONArray resultSet = new JSONArray();
//        cursor.moveToFirst();
//        int maxId = 0;
//        while (cursor.isAfterLast() == false) {
//            int totalColumn = cursor.getColumnCount();
//            JSONObject rowObject = new JSONObject();
//            for (int i = 0; i < totalColumn; i++) {
//                if (cursor.getColumnName(i) != null) {
//                    try {
//                        if (cursor.getString(i) != null) {
//                            //Log.d("TAG_NAME", cursor.getString(i));
//                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
//                            if (cursor.getColumnName(i).equals("Id")) {
//                                maxId = Integer.valueOf(cursor.getString(i));
//                            }
//                        } else {
//                            rowObject.put(cursor.getColumnName(i), "");
//                        }
//                    } catch (Exception e) {
//                        Log.d("TAG_NAME", e.getMessage());
//                    }
//                }
//            }
//            resultSet.put(rowObject);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        Log.d("TAG_NAME", resultSet.toString());
//        String empId = tinyDB.getString("EmpId");
//        if (millisSecond >= (1000 * 10) && isNetworkAvailable()) {
//            final int maxIdfinal = maxId;
//            final String datedate = dNow.toString();
//            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//            apiService.LatLongSave(resultSet.toString(), "", empId, "ON", "Auto", "asus").enqueue(new Callback<LatLngModal>() {
//                @Override
//                public void onResponse(Call<LatLngModal> call, Response<LatLngModal> response) {
//                    Log.i(TAG, "post submitted to API." + response.body().toString());
//                    Toast.makeText(context, "" + response.body(), Toast.LENGTH_SHORT).show();
//                    DBHandler db = new DBHandler(getApplicationContext());
//                    db.updateHandler(maxIdfinal, "Y");
//                    tinyDB.putString("LastUploadTime", datedate.toString());
//                }
//
//                @Override
//                public void onFailure(Call<LatLngModal> call, Throwable t) {
//                    Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, t.getMessage());
//                }
//            });
//
//        }
//    }
//    //}
//
//    private String getDateTime(String format) {
//        DateFormat dateFormat = new SimpleDateFormat(format);//"yyyyMMddHHmmssSS");
//        Date date = new Date();
//        return dateFormat.format(date);
//    }
//
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//
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
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        super.onTaskRemoved(rootIntent);
//        Log.e("htis", "onTaskRemoved");
//        Intent i = new Intent(context, SplashActivity.class);
//        context.startService(i);
//
//
//    }
//
//
//}
