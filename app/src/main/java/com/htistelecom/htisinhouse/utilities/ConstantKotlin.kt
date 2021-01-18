package com.htistelecom.htisinhouse.utilities

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


        fun getCurrentDateTime(): Pair<String, String> {
            val sdfDate = SimpleDateFormat("dd MMM yyyy")
            val sdfTime = SimpleDateFormat("hh:mm aa")

            val currentDate = sdfDate.format(Date())
            val currentTime = sdfTime.format(Date())

            return Pair(currentTime, currentDate)
        }

        fun getCurrentTime24Hrs(): String {
            val sdfTime = SimpleDateFormat("HH:mm")

            val currentTime = sdfTime.format(Date())

            return currentTime
        }


        fun getCurrentDate(): String {
            val sdfDate = SimpleDateFormat("dd-MMM-yyyy")

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