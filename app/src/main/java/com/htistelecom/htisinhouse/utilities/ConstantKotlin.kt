package com.htistelecom.htisinhouse.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import java.text.SimpleDateFormat
import java.util.*


class ConstantKotlin {
    companion object {
        val FOO = "foo"


        @SuppressLint("SimpleDateFormat")
        fun getCurrentDateTime(): Pair<String, String> {
            val sdfDate = SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH)
            val sdfTime = SimpleDateFormat("hh:mm aa",Locale.ENGLISH)

            val currentDate = sdfDate.format(Date())
            val currentTime = sdfTime.format(Date())

            return Pair(currentTime, currentDate)
        }

        @SuppressLint("SimpleDateFormat")
        fun getDateTimeServer(date:String, time:String):Pair<String, String>
        {

            val sdfDateSource = SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH)
            val sdfTimeSource = SimpleDateFormat("hh:mm:ss",Locale.ENGLISH)
            val sdfDateTarget = SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH)
            val sdfTimeTarget = SimpleDateFormat("hh:mm aa",Locale.ENGLISH)

            val mDateOldFormat=sdfDateSource.parse(date)
            val mDateTargetFormat=sdfDateTarget.format(mDateOldFormat)

            val mTimeOldFormat=sdfTimeSource.parse(time)
            val mTimeTargetFormat=sdfTimeTarget.format(mTimeOldFormat)
            return Pair(mDateTargetFormat, mTimeTargetFormat)

        }



        @SuppressLint("SimpleDateFormat")
        fun getCurrentTime24Hrs(): String {
            val sdfTime = SimpleDateFormat("HH:mm",Locale.ENGLISH)

            val currentTime = sdfTime.format(Date())

            return currentTime
        }


        @SuppressLint("SimpleDateFormat")
        fun getCurrentDate(): String {
            val sdfDate = SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH)

            val currentDate = sdfDate.format(Date())

            return currentDate
        }


        fun logout(ctx: Activity, tinyDB: TinyDB) {
            tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_LOGIN,false)
            val intent = Intent("logout_htis")
            // You can also include some extra data.
            // You can also include some extra data.
            LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent)
        }
    }


}