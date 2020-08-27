package com.htistelecom.htisinhouse.activity.WFMS.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.util.Log
import com.htistelecom.htisinhouse.activity.AlertDialogActivity
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.PreOreoLocationService
import com.htistelecom.htisinhouse.services.ServiceDetector

class NetworkChangeReceiver:BroadcastReceiver() {
    private val TAG = "LocationProviderChanged"
    val REQUEST_CODE = 12345
    var detector = ServiceDetector()
    override fun onReceive(context: Context, intent: Intent) {
        if (!isLocationEnabled(context)) {
            context.startActivity(Intent(context, AlertDialogActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            //check the service is running or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!detector.isServiceRunning(context, OreoLocationService::class.java)) {
                    OreoLocationService.enqueueWork(context, Intent())
                } else {
                    Log.i(TAG, "Service is already running reboot")
                }
            } else {
                if (!detector.isServiceRunning(context, PreOreoLocationService::class.java)) {
                    context.startService(Intent(context, PreOreoLocationService::class.java))
                } else {
                    Log.i(TAG, "Service is already running reboot")
                }
            }
        }
    }
    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            !TextUtils.isEmpty(locationProviders)
        }
    }
}