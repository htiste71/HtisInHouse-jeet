//package com.htistelecom.htisinhouse.reciver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.location.LocationManager;
//import android.os.Build;
//import android.provider.Settings;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.htistelecom.htisinhouse.activity.AlertDialogActivity;
//import com.htistelecom.htisinhouse.services.ServiceDetector;
//
//import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
//
//public class NetworkReceiver extends BroadcastReceiver {
//    private final static String TAG = "LocationProviderChanged";
//    public static final int REQUEST_CODE = 12345;
//    ServiceDetector detector = new ServiceDetector();
//
//    // START OF onReceive
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        if (!isLocationEnabled(context)) {
//
//            context.startActivity(new Intent(context, AlertDialogActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
//
//        } else {
//            //check the service is running or not
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (!detector.isServiceRunning(context, SrvTrackingHtis.class)) {
//                    SrvTrackingHtis.enqueueWork(context, new Intent());
//                    //context.startForegroundService(new Intent(context, SrvTrackingHtis.class));
//                } else {
//                    Log.i(TAG, "Service is already running reboot");
//                }
//            } else {
//                if (!detector.isServiceRunning(context, SrvTrackingHtis.class)) {
//                    context.startService(new Intent(context, BackgroundServiceClass.class));
//                } else {
//                    Log.i(TAG, "Service is already running reboot");
//                }
//            }
//
//        }
//
//    }
//
//
//    public static boolean isLocationEnabled(Context context) {
//        int locationMode = 0;
//        String locationProviders;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            try {
//                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
//
//        }else{
//            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            return !TextUtils.isEmpty(locationProviders);
//        }
//
//    }
//
//
//}
