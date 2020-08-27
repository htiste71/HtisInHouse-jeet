//package com.htistelecom.htisinhouse.reciver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.util.Log;
//
//import com.htistelecom.htisinhouse.services.ServiceDetector;
//
//public class AutoStartReceiver extends BroadcastReceiver {
//    private final static String TAG = "AfterBoot";
//    ServiceDetector detector = new ServiceDetector();
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        //check the service is running or not
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//                if (!detector.isServiceRunning(context, SrvTrackingHtis.class)) {
//                    SrvTrackingHtis.enqueueWork(context, new Intent());
//                    //context.startForegroundService(new Intent(context, SrvTrackingHtis.class));
//                } else {
//                    Log.i(TAG, "Service is already running reboot");
//                }
//            }
//        } else {
//            if (!detector.isServiceRunning(context, BackgroundServiceClass.class)) {
//                context.startService(new Intent(context, BackgroundServiceClass.class));
//            } else {
//                Log.i(TAG, "Service is already running reboot");
//            }
//        }
//    }
//}
