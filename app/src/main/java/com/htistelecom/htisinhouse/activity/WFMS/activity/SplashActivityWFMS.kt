package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.service.GPSTrackerWFMS
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.PreOreoLocationService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.utilities.Utilities

class SplashActivityWFMS : AppCompatActivity() {

    var im: String = ""
    lateinit var locationManager: LocationManager
    lateinit var tinyDB: TinyDB
    private val LOCATION_PERMISSION_REQUEST_CODE = 103
    var PERMISSIONS = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    )

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        MyApplication.context = this
        tinyDB = TinyDB(this)


        initOtherViews()
       // Handler().postDelayed({ checkPermissions() }, 1000)


    }


    private fun checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!checkLocationPermissionGranted()) {
                requestLocationPermission()
            } else {

                checkGPS()
            }
        } else {
            checkGPS()
        }


    }


    private fun initOtherViews() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }


    internal fun checkGPS() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startLocationClass()

        } else {
            showGPSDisabledAlertToUser()
        }
    }


    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS"
                ) { dialog, id ->
                    val callGPSSettingIntent = Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(callGPSSettingIntent)
                }
        alertDialogBuilder.setNegativeButton("Cancel"
        ) { dialog, id -> dialog.cancel() }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!checkLocationPermissionGranted()) {
                checkPermissions()
            } else {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    startLocationClass()


                    Handler().postDelayed({


                        val telephonyManager: TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

                        try {
                            im = telephonyManager.getDeviceId()
                            tinyDB.putString(ConstantsWFMS.TINYDB_IMEI_NUMBER, im.toString())

                        } catch (e: Exception) {
                            tinyDB.putString(ConstantsWFMS.TINYDB_IMEI_NUMBER, im.toString())
                            if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_URL_VERIFIED)) {
                                startActivity(Intent(this@SplashActivityWFMS, CustomUrlActivity::class.java))
                            } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_EMAIL_VERIFIED)) {
                                startActivity(Intent(this@SplashActivityWFMS, VerifyEmailActivity::class.java))
                            } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_LOGIN)) {
                                startActivity(Intent(this@SplashActivityWFMS, LoginNewActivity::class.java))
                            } else {
                                startLocationClass()
                                startActivity(Intent(this@SplashActivityWFMS, MainActivityNavigation::class.java))
                            }
                            finish()
                        }

                        if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_URL_VERIFIED)) {
                            startActivity(Intent(this@SplashActivityWFMS, CustomUrlActivity::class.java))
                        } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_EMAIL_VERIFIED)) {
                            startActivity(Intent(this@SplashActivityWFMS, VerifyEmailActivity::class.java))
                        } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_LOGIN)) {
                            startActivity(Intent(this@SplashActivityWFMS, LoginNewActivity::class.java))
                        } else {
                            startActivity(Intent(this@SplashActivityWFMS, MainActivityNavigation::class.java))
                        }
                        finish()
                    }, 1000)
                }
            }
        } else {


            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                Handler().postDelayed({
                    if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_URL_VERIFIED)) {
                        startActivity(Intent(this@SplashActivityWFMS, CustomUrlActivity::class.java))
                    } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_EMAIL_VERIFIED)) {
                        startActivity(Intent(this@SplashActivityWFMS, VerifyEmailActivity::class.java))
                    } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_LOGIN)) {
                        startActivity(Intent(this@SplashActivityWFMS, LoginNewActivity::class.java))
                    } else {
                        startLocationClass()
                        startActivity(Intent(this@SplashActivityWFMS, MainActivityNavigation::class.java))
                    }
                    finish()
                }, 1000)
            }


        }


    }

    private fun startLocationClass() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(Intent(this, OreoLocationService::class.java))

        } else {
            startService(Intent(this, PreOreoLocationService::class.java))

        }
        startService(Intent(this, GPSTrackerWFMS::class.java))

    }


    fun checkLocationPermissionGranted(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        val result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED&& result3 == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestLocationPermission() {
        requestPermissions(PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (locationAccepted) {
                    Utilities.showToast(this, "Permission Granted, Now you can access location.")
                    checkGPS()
                    //startLocationClass()
                } else {
                    Utilities.showToast(this, "Permission Denied, You cannot access location")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to all the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestLocationPermission()
                                        }
                                    })
                            return
                        }
                    }
                }
            }
        }
    }


    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }


}