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

class CheckPunchStatusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val notificationClass = NotificationChannelClass(context)
        val currentTime = DateUtils.getCurrentTime()
        val calendarMorning = Calendar.getInstance()
        calendarMorning[Calendar.HOUR_OF_DAY] = 10
        calendarMorning[Calendar.MINUTE] = 0

        val morningTimeTarget=SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendarMorning.time))
        val currentTimeTarget=SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(SimpleDateFormat("HH:mm", Locale.ENGLISH).format(currentTime.time))



        val calendarMorningBefore11 = Calendar.getInstance()
        calendarMorningBefore11[Calendar.HOUR_OF_DAY] = 11
        calendarMorningBefore11[Calendar.MINUTE] = 0
        val morningTimeTargetBefore11=SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendarMorningBefore11.time))





        val tinyDB = TinyDB(context)




        if (currentTimeTarget.equals(morningTimeTarget) || ((currentTimeTarget.after(morningTimeTarget) && currentTimeTarget.before(morningTimeTargetBefore11)))) {

            if(!tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN))
            notificationClass.showNotification("Please Punch In")


        }
    }
}