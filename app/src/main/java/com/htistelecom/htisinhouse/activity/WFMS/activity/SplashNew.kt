package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.service.GPSTrackerWFMS
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.PreOreoLocationService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.services.LocationServiceHtis
import com.htistelecom.htisinhouse.services.LocationServiceHtis.MyBinder
import com.htistelecom.htisinhouse.utilities.Utilities

class SplashNew : AppCompatActivity() {

    companion object
    {
         fun provide():Activity
        {
            return this as SplashNew
        }
    }
    var mBoundService: LocationServiceHtis? = null
    var mServiceBound = false
    private var isGPSOn: Boolean = false
    var im: String = ""
    lateinit var locationManager: LocationManager
    lateinit var tinyDB: TinyDB
    private val LOCATION_PERMISSION_REQUEST_CODE = 103
    var PERMISSIONS = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA


    )

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
           // showGPSDisabledAlertToUser()
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

                if (CheckGpsStatus()) {
                    startLocationClass()


                    Handler().postDelayed({


                        val telephonyManager: TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

                        try {
                            im = telephonyManager.getDeviceId()
                            tinyDB.putString(ConstantsWFMS.TINYDB_IMEI_NUMBER, im.toString())

                        } catch (e: Exception) {
                            tinyDB.putString(ConstantsWFMS.TINYDB_IMEI_NUMBER, im.toString())
                            if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_URL_VERIFIED)) {
                                startActivity(Intent(this@SplashNew, CustomUrlActivity::class.java))
                            } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_EMAIL_VERIFIED)) {
                                startActivity(Intent(this@SplashNew, VerifyEmailActivity::class.java))
                            } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_LOGIN)) {
                                startActivity(Intent(this@SplashNew, LoginNewActivity::class.java))
                            } else {
                                startLocationClass()
                                startActivity(Intent(this@SplashNew, MainActivityNavigation::class.java))
                            }
                            finish()
                        }

                        if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_URL_VERIFIED)) {
                            startActivity(Intent(this@SplashNew, CustomUrlActivity::class.java))
                        } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_EMAIL_VERIFIED)) {
                            startActivity(Intent(this@SplashNew, VerifyEmailActivity::class.java))
                        } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_LOGIN)) {
                            startActivity(Intent(this@SplashNew, LoginNewActivity::class.java))
                        } else {
                            startActivity(Intent(this@SplashNew, MainActivityNavigation::class.java))
                        }
                        finish()
                    }, 1000)
                } else {
                    //showGPSDisabledAlertToUser()
                }
            }
        } else {


            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                Handler().postDelayed({
                    if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_URL_VERIFIED)) {
                        startActivity(Intent(this@SplashNew, CustomUrlActivity::class.java))
                    } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_EMAIL_VERIFIED)) {
                        startActivity(Intent(this@SplashNew, VerifyEmailActivity::class.java))
                    } else if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_LOGIN)) {
                        startActivity(Intent(this@SplashNew, LoginNewActivity::class.java))
                    } else {
                        startLocationClass()
                        startActivity(Intent(this@SplashNew, MainActivityNavigation::class.java))
                    }
                    finish()
                }, 1000)
            } else {
               // showGPSDisabledAlertToUser()
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
                && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
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

    fun CheckGpsStatus(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            val providers = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            if (TextUtils.isEmpty(providers)) {
                false
            } else providers.contains(LocationManager.GPS_PROVIDER)
        } else {
            val locationMode: Int
            locationMode = try {
                Settings.Secure.getInt(getContentResolver(),
                        Settings.Secure.LOCATION_MODE)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            when (locationMode) {
                Settings.Secure.LOCATION_MODE_HIGH_ACCURACY, Settings.Secure.LOCATION_MODE_SENSORS_ONLY -> true
                Settings.Secure.LOCATION_MODE_BATTERY_SAVING, Settings.Secure.LOCATION_MODE_OFF -> false
                else -> false
            }
        }
    }


    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBinder
            mBoundService = myBinder.receiveService()
            mServiceBound = true
            myBinder.sendContext(this@SplashNew)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, LocationServiceHtis::class.java)
        startService(intent)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (mServiceBound) {
            unbindService(mServiceConnection)
            mServiceBound = false
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1001 -> when (resultCode) {
                Activity.RESULT_OK ->onResume()
                Activity.RESULT_CANCELED -> showGPSDisabledAlertToUser()
            }
        }
    }
}


