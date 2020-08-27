//package com.htistelecom.htisinhouse.services;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.htistelecom.htisinhouse.config.TinyDB;
//import com.htistelecom.htisinhouse.db.DBHandler;
//import com.htistelecom.htisinhouse.model.FileModel;
//import com.htistelecom.htisinhouse.model.LatLngModal;
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
//public class BackgroundServiceClass extends Service {
//
//    private final String TAG = "BackgroundServiceClass";
//    public Context context = this;
//    public Handler handler = null;
//    public static Runnable runnable = null;
//    private LocationListener mLocationListener;
//    private LocationManager mLocationManager;
//
//    private final int LOCATION_INTERVAL = 1000 * 10;
//    private final int LOCATION_DISTANCE = 0;
//
//    TinyDB tinyDB;
//    private File file;
//
//   /* public BackgroundServiceClass(Context applicationContext) {
//        super();
//        context = applicationContext;
//
//        Log.e("HERE", "here service created!");
//    }*/
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
//            SrvTrackingHtis.LATITUDE = location.getLatitude() + "";
//            SrvTrackingHtis.LONGITUDE = location.getLongitude() + "";
//            //Log.e(TAG, "LocationChanged123123: " + location);
//            // toast("Lat:" + location.getLatitude() + ", Lng:" + location.getLongitude());
//
//           /* if (CommonFunctions.getTime()) {*/
//                if (distanceBetweenLatLng(0, 0, location.getLatitude(), location.getLongitude()) >= 0) {
//                    FileWriter fr = null;
//                    try {
//
//                        Date date = new Date();
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//
//                        String mDateTime=simpleDateTimeFormat.format(date);
//
//                        String mDate = simpleDateFormat.format(date);
//                        String mUserId = tinyDB.getString("EmpId");
//
//                        File gpxfile = new File(file, "" + mUserId + "_" + mDate + ".txt");
//                        fr = new FileWriter(gpxfile, true);
//                        BufferedWriter br = new BufferedWriter(fr);
//
//                        br.write(location.getLatitude() + "#" + location.getLongitude() +"#"+mDateTime+ ",");
//                        //                    Toast.makeText(BackgroundServiceClass.this, "Hit", Toast.LENGTH_SHORT).show();
//                        //   tinyDB.putString("file",gpxfile.getAbsolutePath());
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
//        }
//
//        public void saveLatLng(String lat, String lng) {
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
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.e(TAG, "onProviderEnabled: " + provider);
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
//    public BackgroundServiceClass() {
//        //
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        file = new File(Environment.getExternalStorageDirectory(), "MyLoc");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        //Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
//        try {
//
//
//
//            /*try {
//                Intent intent = new Intent();
//                String manufacturer = android.os.Build.MANUFACTURER;
//                if ("xiaomi".equalsIgnoreCase(manufacturer)) {
//                    intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
//                } else if ("oppo".equalsIgnoreCase(manufacturer)) {
//                    intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
//                } else if ("vivo".equalsIgnoreCase(manufacturer)) {
//                    intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
//                }
//
//                List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                if  (list.size() > 0) {
//                    context.startActivity(intent);
//                }
//            } catch (Exception e) {
//                Log.e("",e.getMessage());
//            }*/
//
//            tinyDB = new TinyDB(BackgroundServiceClass.this);
//            Log.e(TAG, "service: ");
//            startTracking();
//
//
//            handler = new Handler(Looper.getMainLooper());
//            runnable = new Runnable() {
//                public void run() {
//                    // Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
//
//                    // toast("Service is still running");
//
//                    //Log.e("htis", "enque done");
//                    handler.postDelayed(runnable, 3000);
//                }
//            };
//
//            handler.postDelayed(runnable, 4500);
//        } catch (Exception ex) {
//            Log.e(TAG, ex.getMessage());
//        }
//    }
//
//    final Handler mHandler = new Handler();
//
//    void toast(final CharSequence text) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(BackgroundServiceClass.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void onDestroy() {
//        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
//        //handler.removeCallbacks(runnable);
//        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
//        Log.e("", "destroy");
//        sendBroadcast(new Intent("YouWillNeverKillMe"));
//
//
//        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
//        Intent i = new Intent(this, BackgroundServiceClass.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
//        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
//
//        Intent intent = new Intent("com.htistelecom.htisinhouse");
//        sendBroadcast(intent);
//        super.onDestroy();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        return START_STICKY;
//    }
//
//    private void initializeLocationManager() {
//        Log.e(TAG, "service: loc initialized");
//        if (mLocationManager == null) {
//            Log.e(TAG, "service: loc null");
//            mLocationManager = (LocationManager) (BackgroundServiceClass.this).getSystemService(Context.LOCATION_SERVICE);
//        }
//    }
//
//    public void startTracking() {
//        initializeLocationManager();
//        mLocationListener = new BackgroundServiceClass.LocationListener(LocationManager.GPS_PROVIDER);
//        Log.e(TAG, "service: startTracking");
//        try {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener);
//            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener);
//            Log.e(TAG, "startTracking: " + mLocationListener.mLastLocation);
//        } catch (SecurityException ex) {
//            Log.e(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//        }
//    }
//
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
//        if (millisSecond < (6 * 60 * 1000)) {
//            return;
//        } else {
//            DBHandler dbHandler = new DBHandler(this);
//            Cursor cursor = dbHandler.findHandler("col_IsSync", "N");
//            //Cursor cursor = dbHandler.getAllData();
//
//            if (cursor.getCount() == 0) {
//                // show message
//                Log.e("LoadTrackingData", "nothing");
//                return;
//            }
//
//            JSONArray resultSet = new JSONArray();
//            cursor.moveToFirst();
//            int maxId = 0;
//            while (cursor.isAfterLast() == false) {
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
//                        Log.e(TAG, "post submitted to API." + response.body().toString());
//                        Toast.makeText(context, "" + response.body(), Toast.LENGTH_SHORT).show();
//                        DBHandler db = new DBHandler(getApplicationContext());
//                        db.updateHandler(maxIdfinal, "Y");
//                        tinyDB.putString("LastUploadTime", datedate.toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<LatLngModal> call, Throwable t) {
//                        Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, t.getMessage());
//                    }
//                });
//
//            }
//        }
//    }
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
//
//        Log.e(TAG, "onTaskRemoved()");
//        sendBroadcast(new Intent("YouWillNeverKillMe"));
//
//
//       /* Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 12);
//
//        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),3600*1000*24 ,pendingIntent);
//*/
//
//
//        Intent i = new Intent(context, BackgroundServiceClass.class);
//        context.startService(i);
//        super.onTaskRemoved(rootIntent);
//    }
//
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        Log.e(TAG, "onLowMemory()");
//    }
//}
