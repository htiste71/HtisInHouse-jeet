package com.htistelecom.htisinhouse.activity.WFMS.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication.context
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.service.UploadDataServerService
import com.htistelecom.htisinhouse.config.TinyDB
import java.util.*


class AlarmReceiverForPunchOut : BroadcastReceiver() {

    lateinit var notifManager: NotificationManager
    val format = java.text.SimpleDateFormat("hh:mm aa dd MMM yyyy ")

    override fun onReceive(ctx: Context, intent: Intent) {


        val tinyDB = TinyDB(ctx)
        val setHour = intent.getIntExtra("HOUR_OF_DAY", -1)
        val rightNow: Calendar = Calendar.getInstance()
        val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY)
        //    UtilitiesWFMS.showToast(ctx, "Me alarm bol rha hoon bahr")

        if (currentHour == setHour) {
            var isCheckout = false
            if (tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)) {
                tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN, false)
                isCheckout = true
            }
            UtilitiesWFMS.showToast(context,"Alarm is running")
            try {
                val date = Date()
                var mCurrentTime = format.format(date)
                LocalBroadcastManager.getInstance(context).sendBroadcast(Intent("FOR_UPDATE_TIME").putExtra("data", mCurrentTime)
                        .putExtra("IsCheckOut", isCheckout));

            } catch (e: Exception) {
            }
        } else {

        }
        ctx.startService(Intent(ctx, UploadDataServerService::class.java))





    }
}


