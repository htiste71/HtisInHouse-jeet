package com.htistelecom.htisinhouse.activity.WFMS.receiver

import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import com.htistelecom.htisinhouse.activity.WFMS.service.JobServiceToUplodData
import com.htistelecom.htisinhouse.activity.WFMS.service.UploadDataServerService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import java.util.*


class AlarmReceiverForPunchOut : BroadcastReceiver(), MyInterface {

    lateinit var notifManager: NotificationManager
    val format = java.text.SimpleDateFormat("hh:mm aa dd MMM yyyy ", Locale.ENGLISH)

    override fun onReceive(ctx: Context, intent: Intent) {


        val tinyDB = TinyDB(ctx)
//        val setHour = intent.getIntExtra("HOUR_OF_DAY", -1)
//        val rightNow: Calendar = Calendar.getInstance()
//        val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY)
//        //    UtilitiesWFMS.showToast(ctx, "Me alarm bol rha hoon bahr")
//
//        if (currentHour == setHour)
//        {
//            var isCheckout = false
//            if (tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)) {
//                tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN, false)
//               val mTime24Hrs = ConstantKotlin.getCurrentTime24Hrs()
//
//                val mDateTime = DateUtils.currentDate() + " " + mTime24Hrs
//                var mAddressStr=""
//
//                var mCountry=""
//                var mState=""
//                var mCity=""
//
//                val mAddress = Utilities.getAddressFromLatLong(ctx, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())
//
//                if(mAddress==null)
//                {
//                   mAddressStr=""
//                    mCountry=""
//                    mState=""
//                    mCity=""
//                }
//                else
//                {
//                 mAddressStr=mAddress.get(0).getAddressLine(0)
//                    mCountry= mAddress.get(0).countryName
//                    mState=mAddress.get(0).adminArea
//                    mCity=mAddress.get(0).locality
//                }
//
//
//                val jsonObject = JSONObject()
//                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
//                jsonObject.put("StartDate", DateUtils.currentDate())
//                jsonObject.put("StartTime", mDateTime)
//                jsonObject.put("Latitude", OreoLocationService.LATITUDE)
//                jsonObject.put("Longitude", OreoLocationService.LONGITUDE)
//                jsonObject.put("LocationAddress", mAddressStr)
//                jsonObject.put("DomainId", tinyDB.getString(ConstantsWFMS.TINYDB_DOMAIN_ID))
//                jsonObject.put("PunchType", "O")
//                jsonObject.put("PunchCountry",mCountry)
//                jsonObject.put("PunchState", mState)
//
//                jsonObject.put("PunchCity", mCity)
//                ApiData.getDataPunchOut(jsonObject.toString())
//
//                isCheckout = true
//            }
//            UtilitiesWFMS.showToast(ctx,"Alarm is running")
//            try {
//                val date = Date()
//                var mCurrentTime = format.format(date)
//                LocalBroadcastManager.getInstance(ctx).sendBroadcast(Intent("FOR_UPDATE_TIME").putExtra("data", mCurrentTime)
//                        .putExtra("IsCheckOut", isCheckout));
//
//            } catch (e: Exception) {
//            }
//        } else {
//
//        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val componentName = ComponentName(ctx, JobServiceToUplodData::class.java)
            val job: JobInfo.Builder = JobInfo.Builder(0, componentName)
            job.setOverrideDeadline(0)

            val js: JobScheduler = ctx.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            js.schedule(job.setRequiresCharging(false).build())

        } else {
            ctx.startService(Intent(ctx, UploadDataServerService::class.java))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        TODO("Not yet implemented")
    }
}


