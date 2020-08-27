//package com.htistelecom.htisinhouse.reciver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import com.htistelecom.htisinhouse.utilities.Utilities;
//
//public class AlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (Utilities.isNetConnected(context)) {
//            context.startService(new Intent(context, SendDataToServer.class));
//        }
//    }
//}
