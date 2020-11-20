package com.htistelecom.htisinhouse.activity.WFMS.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.PreOreoLocationService
import com.htistelecom.htisinhouse.services.ServiceDetector
import java.util.*


class DeviceRebootReceiver : BroadcastReceiver() {
    private val TAG = "AfterBoot"
    var detector = ServiceDetector()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {


            //check the service is running or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                    if (!detector.isServiceRunning(context, OreoLocationService::class.java)) {
                        OreoLocationService.enqueueWork(context, Intent())
                        //context.startForegroundService(new Intent(context, SrvTrackingHtis.class));
                    } else {
                        Log.i(TAG, "Service is already running reboot")
                    }
                }
            } else {
                if (!detector.isServiceRunning(context, PreOreoLocationService::class.java)) {
                    context.startService(Intent(context, PreOreoLocationService::class.java))
                } else {
                    Log.i(TAG, "Service is already running reboot")
                }
            }




            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 23
            calendar[Calendar.MINUTE] = 0
            val alarmMgr = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager


            val intent = Intent(context, AlarmReceiverForPunchOut::class.java).putExtra("HOUR_OF_DAY", 0)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 24 * 60 * 60 * 1000, pendingIntent)

            Toast.makeText(MyApplication.context, "Alarm Set After Reboot", Toast.LENGTH_SHORT).show()
        }
    }
}