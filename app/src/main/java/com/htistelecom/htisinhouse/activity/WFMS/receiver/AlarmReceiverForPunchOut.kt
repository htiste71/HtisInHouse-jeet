package com.htistelecom.htisinhouse.activity.WFMS.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication.context
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.UploadDataServerService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.Utilities
import org.json.JSONObject
import java.util.*


class AlarmReceiverForPunchOut : BroadcastReceiver(), MyInterface {

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
               val mTime24Hrs = ConstantKotlin.getCurrentTime24Hrs()

                val mDateTime = DateUtils.currentDate() + " " + mTime24Hrs
                var mAddressStr=""

                var mCountry=""
                var mState=""
                var mCity=""

                val mAddress = Utilities.getAddressFromLatLong(ctx, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())

                if(mAddress==null)
                {
                   mAddressStr=""
                    mCountry=""
                    mState=""
                    mCity=""
                }
                else
                {
                 mAddressStr=mAddress.get(0).getAddressLine(0)
                    mCountry= mAddress.get(0).countryName
                    mState=mAddress.get(0).adminArea
                    mCity=mAddress.get(0).locality
                }


                val jsonObject = JSONObject()
                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                jsonObject.put("StartDate", DateUtils.currentDate())
                jsonObject.put("StartTime", mDateTime)
                jsonObject.put("Latitude", OreoLocationService.LATITUDE)
                jsonObject.put("Longitude", OreoLocationService.LONGITUDE)
                jsonObject.put("LocationAddress", mAddressStr)
                jsonObject.put("DomainId", tinyDB.getString(ConstantsWFMS.TINYDB_DOMAIN_ID))
                jsonObject.put("PunchType", "O")
                jsonObject.put("PunchCountry",mCountry)
                jsonObject.put("PunchState", mState)

                jsonObject.put("PunchCity", mCity)
                ApiData.getDataPunchOut(jsonObject.toString())

                isCheckout = true
            }
            UtilitiesWFMS.showToast(ctx,"Alarm is running")
            try {
                val date = Date()
                var mCurrentTime = format.format(date)
                LocalBroadcastManager.getInstance(ctx).sendBroadcast(Intent("FOR_UPDATE_TIME").putExtra("data", mCurrentTime)
                        .putExtra("IsCheckOut", isCheckout));

            } catch (e: Exception) {
            }
        } else {

        }
        ctx.startService(Intent(ctx, UploadDataServerService::class.java))





    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        TODO("Not yet implemented")
    }
}


