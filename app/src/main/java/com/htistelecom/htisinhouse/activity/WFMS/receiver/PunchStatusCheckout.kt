package com.htistelecom.htisinhouse.activity.WFMS.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.NotificationChannelClass
import java.text.SimpleDateFormat
import java.util.*

class PunchStatusCheckout:BroadcastReceiver(){



    override fun onReceive(context: Context, intent: Intent?) {
        val notificationClass = NotificationChannelClass(context)
        val currentTime = DateUtils.getCurrentTime()


        val currentTimeTarget= SimpleDateFormat("HH:mm").parse(SimpleDateFormat("HH:mm").format(currentTime.time))





        val calendarEvening = Calendar.getInstance()
        calendarEvening[Calendar.HOUR_OF_DAY] = 19
        calendarEvening[Calendar.MINUTE] = 0
        val eveningTimeTarget= SimpleDateFormat("HH:mm").parse(SimpleDateFormat("HH:mm").format(calendarEvening.time))



        val tinyDB = TinyDB(context)




       if (currentTimeTarget.equals(eveningTimeTarget) || currentTimeTarget.after(eveningTimeTarget)) {
            if(tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN))
                notificationClass.showNotification(" Please Punch Out")

        }
    }
}