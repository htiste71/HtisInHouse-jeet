//package com.htistelecom.htisinhouse.services;
//
//import android.Manifest;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Binder;
//import android.os.Build;
//import android.os.IBinder;
//import android.support.v4.app.ActivityCompat;
//import android.util.Log;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.htistelecom.htisinhouse.R;
//import com.htistelecom.htisinhouse.config.TinyDB;
//import com.htistelecom.htisinhouse.db.DBHandler;
//import com.htistelecom.htisinhouse.model.LatLngModal;
//import com.htistelecom.htisinhouse.retrofit.ApiClient;
//import com.htistelecom.htisinhouse.retrofit.ApiInterface;
//import com.htistelecom.htisinhouse.utilities.Utilities;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class LocationServiceHtis extends Service {
//    private final LocationServiceHtis.LocationServiceBinder binder = new LocationServiceHtis.LocationServiceBinder();
//    TinyDB tinyDB;
//
//    LocationManager locationManager;
//    Boolean isGpsOn = false;
//    private static final String TAG = "LocationServiceHtis";
//
//
//
//    /*public LocationServiceHtis(){
//        tinyDB=new TinyDB(LocationServiceHtis.this);
//    }*/
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        return START_NOT_STICKY;
//    }
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return binder;
//    }
//
//    @Override
//    public void onCreate() {
//        Log.i(TAG, "onCreate");
//        tinyDB = new TinyDB(LocationServiceHtis.this);
//        startForeground(12345688, getNotification());
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    private void initializeLocationManager() {
//
//        startLocationUpdates();
//    }
//
//    private void startLocationUpdates() {
//        LocationRequest mLocationRequest;
//        FusedLocationProviderClient mFusedLocationClient;
//        LocationCallback locationCallback;
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        checkGPS();
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(LocationServiceHtis.this);
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(20 * 1000);
//
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    //checkGPS();
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    if (location != null) {
//                        //checkGPS();
//                        //getNotification();
//                        SrvTrackingHtis.LATITUDE = String.valueOf(location.getLatitude());
//                        SrvTrackingHtis.LONGITUDE = String.valueOf(location.getLongitude());
//                        Utilities.showToast(LocationServiceHtis.this, "Lat:" + location.getLatitude() + "Lng:" + location.getLongitude());
//                        if (distanceBetweenLatLng(0, 0, location.getLatitude(), location.getLongitude()) >= 100) {
//                            saveLatLng(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
//                            loadTrackingData();
//                        }
//                    } else {
//                        // checkGPS();
//                        //getNotification();
//                    }
//                }
//            }
//        };
//        try {
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                    locationCallback,
//                    null /* Looper */);
//        } catch (Exception ex) {
//            Log.e("location", ex.getMessage());
//        }
//
//    }
//
//    public void startTracking() {
//        initializeLocationManager();
//    }
//
//    public void stopTracking() {
//        this.onDestroy();
//       /* if (mFusedLocationClient != null) {
//            try {
//                mFusedLocationClient.removeLocationUpdates(locationCallback);
//            } catch (Exception ex) {
//                Log.i(TAG, "fail to remove location listners, ignore", ex);
//            }
//        }*/
//    }
//
//    private Notification getNotification() {
//        NotificationManager notificationManager;
//        String trackingMsg = "Tracking On";
//        if (!isGpsOn) {
//            trackingMsg = "Tracking Off";
//        }
//        Notification.Builder builder = null;
//        if (Build.VERSION.SDK_INT >= 23) {
//            NotificationChannel channel = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                channel = new NotificationChannel("htis_not_01", "HTIS Channel", NotificationManager.IMPORTANCE_DEFAULT);
//            }
//
//            notificationManager = getSystemService(NotificationManager.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationManager.createNotificationChannel(channel);
//            }
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                builder = new Notification.Builder(getApplicationContext(), "channel_01")
//                        .setAutoCancel(false)
//                        .setSmallIcon(R.drawable.ic_stat_name)
//                        .setSubText(trackingMsg)
//                        .setContentTitle("HTIS Employee Self Care");
//            }
//        } else {
//            builder = new Notification.Builder(this)
//                    .setSmallIcon(R.drawable.ic_stat_name)
//                    .setContentText(trackingMsg)
//                    .setContentTitle("HTIS Employee Self Care")
//                    .setAutoCancel(false);
//            //notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            //notificationManager.notify(0, builder.build());
//
//        }
//        return builder != null ? builder.build() : null;
//    }
//
//    private Boolean checkGPS() {
//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            isGpsOn = true;
//        } else {
//            isGpsOn = false;
//
//        }
//        return isGpsOn;
//    }
//
//    public class LocationServiceBinder extends Binder {
//        public LocationServiceHtis getService() {
//            return LocationServiceHtis.this;
//        }
//    }
//
//    public void loadTrackingData() {
//
//        tinyDB = new TinyDB(getApplicationContext());
//        String df = "yyyyMMddHHmmssSS";
//        String dNow = getDateTime(df);
//        String dOld = dNow;
//        dOld = tinyDB.getString("LastUploadTime").toString();
//        if (dOld.equals("")) {
//            dOld = dNow.toString();
//            tinyDB.putString("LastUploadTime", dNow.toString());
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat(df, Locale.getDefault());
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
//        if (millisSecond < (6 * 60 * 1000)) {
//            return;
//        } else {
//            DBHandler dbHandler = new DBHandler(this);
//            Cursor cursor = dbHandler.findHandler("col_IsSync", "N");
//            //Cursor cursor = dbHandler.getAllData();
//
//            if (cursor.getCount() == 0) {
//                // show message
//                Log.i("LoadTrackingData", "nothing");
//                return;
//            }
//
//            JSONArray resultSet = new JSONArray();
//            cursor.moveToFirst();
//            int maxId = 0;
//            while (!cursor.isAfterLast()) {
//                int totalColumn = cursor.getColumnCount();
//                JSONObject rowObject = new JSONObject();
//                for (int i = 0; i < totalColumn; i++) {
//                    if (cursor.getColumnName(i) != null) {
//                        try {
//                            if (cursor.getString(i) != null) {
//                                //Log.d("TAG_NAME", cursor.getString(i));
//                                rowObject.put(cursor.getColumnName(i), cursor.getString(i));
//                                if (cursor.getColumnName(i).equals("Id")) {
//                                    maxId = Integer.valueOf(cursor.getString(i));
//                                }
//                            } else {
//                                rowObject.put(cursor.getColumnName(i), "");
//                            }
//                        } catch (Exception e) {
//                            Log.d("TAG_NAME", e.getMessage());
//                        }
//                    }
//                }
//                resultSet.put(rowObject);
//                cursor.moveToNext();
//            }
//            cursor.close();
//            Log.d("TAG_NAME", resultSet.toString());
//            String empId = tinyDB.getString("EmpId").toString();
//            if (millisSecond >= (6 * 60 * 1000) && isNetworkAvailable()) {
//                final int maxIdfinal = maxId;
//                final String datedate = dNow.toString();
//                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//                apiService.LatLongSave(resultSet.toString(), "", empId, "ON", "Auto", "asus").enqueue(new Callback<LatLngModal>() {
//                    @Override
//                    public void onResponse(Call<LatLngModal> call, Response<LatLngModal> response) {
//                        Log.i(TAG, "post submitted to API." + response.body().toString());
//                        DBHandler db = new DBHandler(getApplicationContext());
//                        db.updateHandler(maxIdfinal, "Y");
//                        tinyDB.putString("LastUploadTime", datedate.toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<LatLngModal> call, Throwable t) {
//
//                        Log.e(TAG, t.getMessage());
//                    }
//                });
//
//            }
//        }
//    }
//
//    private String getDateTime(String format) {
//        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());//"yyyyMMddHHmmssSS");
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
//        if (!lat.isEmpty() && !lng.isEmpty() && lng != null) {
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
//        if (distance >= 100) {
//            tinyDB.putString("LastLatitude", String.valueOf(lat2));
//            tinyDB.putString("LastLongitude", String.valueOf(lng2));
//        }
//        return distance;
//    }
//
//    public void saveLatLng(String lat, String lng) {
//        tinyDB = new TinyDB(getApplicationContext());
//
//        DBHandler dbHandler = new DBHandler(getApplicationContext());
//        LatLngModal latLngModal = new LatLngModal();
//        latLngModal.setId(1);
//        latLngModal.setLatitude(lat);
//        latLngModal.setLongitude(lng);
//        latLngModal.setCreatedBy(tinyDB.getString("EmpId"));
//        latLngModal.setCreatedDate(getDateTime("dd-MMM-yyyy HH:mm:ss"));
//        boolean isInserted = dbHandler.addHandler(latLngModal);
//    }
//}