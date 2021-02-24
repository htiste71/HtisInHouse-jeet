package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PUNCH_IN_OUT_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PUNCH_STATUS_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.add_project.AddDetailsFragment
import com.htistelecom.htisinhouse.activity.WFMS.claims.ClaimFragmentWFMS
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.DocumentDirectoryFragmentWFMS
import com.htistelecom.htisinhouse.activity.WFMS.fragments.*
import com.htistelecom.htisinhouse.activity.WFMS.leave_managment.LeaveType_OutdoorDutyFragment
import com.htistelecom.htisinhouse.activity.WFMS.receiver.AlarmReceiverForPunchOut
import com.htistelecom.htisinhouse.activity.WFMS.receiver.CheckPunchStatusReceiver
import com.htistelecom.htisinhouse.activity.WFMS.receiver.PunchStatusCheckout
import com.htistelecom.htisinhouse.activity.WFMS.service.JobServiceToUplodData
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.PreOreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.UploadDataServerService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.Utilities
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.drawer_items.*
import kotlinx.android.synthetic.main.nav_header_main_activity_navigation.*
import kotlinx.android.synthetic.main.toolbar_drawer.*
import org.json.JSONObject
import retrofit2.Response
import java.util.*


class MainActivityNavigation : AppCompatActivity(), View.OnClickListener, MyInterface {

    lateinit var receiver: BroadcastReceiver
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private var mTimeStr: String = ""
    private var mDateStr: String = ""

    private var mTime24Hrs: String = ""
    private var mPunchInTime: String = ""
    private var mPunchOutTime: String = ""
    private var mPunchInOutTime: String = ""

    lateinit var fragmentImage: Fragment
    lateinit var locationManager: LocationManager

    companion object {
        lateinit var obj: MainActivityNavigation
        var isBackHome = false
        var token = ""


    }


    var CHECK_TYPE = -1
    val CHECK_IN = 1
    val CHECK_OUT = 2

    lateinit var tinyDB: TinyDB

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_drawer)
        initViews()

        listeneres()
        //openDefaultFragment()
        setDefautData()
        checkForUpdate()
        punchReminderIn()
        punchReminderOut()

        val json = JSONObject()
        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        hitAPI(ConstantsWFMS.PUNCH_STATUS_WFMS, json.toString())

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                IntentFilter("logout_htis"));


        //  initRemoteConfig()


        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(p0: View, p1: Float) {
            }

            override fun onDrawerClosed(p0: View) {
            }


            override fun onDrawerOpened(p0: View) {

                tvTaskCountDrawer.text = HomeFragmentNew.mTaskCount
                Glide.with(this@MainActivityNavigation).load(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE)).into(ivUserImageHeaderWFMS);
                try {
                    val pInfo: PackageInfo = getPackageManager().getPackageInfo(packageName, 0)
                    val version: String = pInfo.versionName
                    tvAppVersionDrawer.text = "v" + version
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        })


    }

    private fun punchReminderIn() {
        try {

            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 10
            calendar[Calendar.MINUTE] = 0
            val alarmMgr = this.getSystemService(ALARM_SERVICE) as AlarmManager


            val intent = Intent(this, CheckPunchStatusReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 12345, intent, FLAG_UPDATE_CURRENT)
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 24 * 60 * 60 * 1000, pendingIntent)

        } catch (e: Exception) {
        }
    }
    private fun punchReminderOut() {
        try {

            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 19
            calendar[Calendar.MINUTE] = 0
            val alarmMgr = this.getSystemService(ALARM_SERVICE) as AlarmManager


            val intent = Intent(this, PunchStatusCheckout::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 123456, intent, FLAG_UPDATE_CURRENT)
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 24 * 60 * 60 * 1000, pendingIntent)

        } catch (e: Exception) {
        }
    }


    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent


            val intent = Intent(context, LoginNewActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun listeneres() {
        ivDrawerHeader.setOnClickListener(this)
        llInnerHomeDrawer.setOnClickListener(this)
        llInnerProfileDrawer.setOnClickListener(this)
        llInnerTeamDrawer.setOnClickListener(this)
        llInnerTaskDrawer.setOnClickListener(this)
        llInnerAddProjectDrawer.setOnClickListener(this)

        llInnerLeaveODDrawer.setOnClickListener(this)
        llInnerAttendanceDrawer.setOnClickListener(this)
        llInnerDocumentDirectoryDrawer.setOnClickListener(this)
        llInnerSalaryDrawer.setOnClickListener(this)
        llInnerClaimDrawer.setOnClickListener(this)
        llInnerChatDrawer.setOnClickListener(this)
        llInnerFormDrawer.setOnClickListener(this)
        llInnerHelpDrawer.setOnClickListener(this)
        llInnerFeedbackDrawer.setOnClickListener(this)
        llInnerSettingDrawer.setOnClickListener(this)
        btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)

    }

    var mOnCheckChangedListener = CompoundButton.OnCheckedChangeListener { compoundButton, b ->
        if(compoundButton.isPressed)
        {
            val mAddress = Utilities.getAddressFromLatLong(this, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())
            val (mTime, mDate) = ConstantKotlin.getCurrentDateTime()
            mTimeStr = mTime
            mDateStr = mDate

            mTime24Hrs = ConstantKotlin.getCurrentTime24Hrs()

            val mDateTime = DateUtils.currentDate() + " " + mTime24Hrs


            try {

                if (mAddress.get(0) == null) {
                    Utilities.showToast(this, resources.getString(R.string.errLocationNotFetched))
                } else {

                    tinyDB.putDouble(ConstantsWFMS.CURRENT_SAVED_LATITUDE, OreoLocationService.LATITUDE.toDouble())
                    tinyDB.putDouble(ConstantsWFMS.CURRENT_SAVED_LONGITUDE, OreoLocationService.LONGITUDE.toDouble())

                    if (b) {

                        if (Utilities.isNetConnected(this)) {
                            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                CHECK_TYPE = CHECK_IN

                                val jsonObject = JSONObject()
                                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                                jsonObject.put("StartDate", DateUtils.currentDate())

                                jsonObject.put("StartTime", mDateTime)
                                jsonObject.put("Latitude", OreoLocationService.LATITUDE)
                                jsonObject.put("Longitude", OreoLocationService.LONGITUDE)
                                jsonObject.put("LocationAddress", mAddress.get(0).getAddressLine(0))
                                jsonObject.put("DomainId", tinyDB.getString(ConstantsWFMS.TINYDB_DOMAIN_ID))
                                jsonObject.put("PunchType", "I")

                                jsonObject.put("PunchCountry", mAddress.get(0).countryName)
                                jsonObject.put("PunchState", mAddress.get(0).adminArea)
                                jsonObject.put("PunchCity", mAddress.get(0).locality)
                                hitAPI(ConstantsWFMS.PUNCH_IN_OUT_WFMS, jsonObject.toString())
                            } else {

                                btnSwitchHeader.setOnCheckedChangeListener(null)

                                btnSwitchHeader.isChecked = false
                                callSwitcherChageListener()


                            }

                        } else {
                            Utilities.showToast(this, resources.getString(R.string.internet_connection))
                            btnSwitchHeader.setOnCheckedChangeListener(null)

                            btnSwitchHeader.isChecked = false
                            callSwitcherChageListener()
                        }


                    } else {

                        if (Utilities.isNetConnected(this)) {
                            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                CHECK_TYPE = CHECK_OUT
                                //   btnSwitchHeader.isChecked=true
                                val jsonObject = JSONObject()
                                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                                jsonObject.put("StartDate", DateUtils.currentDate())
                                jsonObject.put("StartTime", mDateTime)
                                jsonObject.put("Latitude", OreoLocationService.LATITUDE)
                                jsonObject.put("Longitude", OreoLocationService.LONGITUDE)
                                jsonObject.put("LocationAddress", mAddress.get(0).getAddressLine(0))
                                jsonObject.put("DomainId", tinyDB.getString(ConstantsWFMS.TINYDB_DOMAIN_ID))
                                jsonObject.put("PunchType", "O")
                                jsonObject.put("PunchCountry", mAddress.get(0).countryName)
                                jsonObject.put("PunchState", mAddress.get(0).adminArea)
                                jsonObject.put("PunchCity", mAddress.get(0).locality)
                                // hitAPI(ConstantsWFMS.PUNCH_IN_OUT_WFMS, jsonObject.toString())

                                showAlertDialogForPunchOut(jsonObject.toString())
                            } else {
                                btnSwitchHeader.setOnCheckedChangeListener(null)

                                btnSwitchHeader.isChecked = true
                                callSwitcherChageListener()
                            }
                        } else {
                            Utilities.showToast(this, resources.getString(R.string.internet_connection))
                            btnSwitchHeader.setOnCheckedChangeListener(null)

                            btnSwitchHeader.isChecked = true
                            callSwitcherChageListener()
                        }


                    }
                }
            } catch (e: Exception) {

            }
        }



    }

    private fun callSwitcherChageListener() {
        btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)
        openDialog()
        //  startActivity(Intent(this@MainActivityNavigation, AlertDialogActivity::class.java))

    }


    fun openDialog() {


        val Builder = AlertDialog.Builder(this)
                .setMessage(R.string.location_settings_desktop)
                .setTitle(R.string.location_warning)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)

                .setPositiveButton("Cancel") { dialogInterface, which ->
                    dialogInterface.dismiss();
                }

                .setNegativeButton("Settings") { dialogInterface, which ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }


        val alertDialog: AlertDialog = Builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    private fun setDefautData() {
        tvNameHeaderWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMAIL)
        tvEmailHeaderWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_NAME)
        if (tinyDB.getString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE).equals("")) {
            Glide.with(this).load(R.drawable.icon_man).into(ivUserImageHeaderWFMS);

        } else {
            Glide.with(this).load(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE)).into(ivUserImageHeaderWFMS);

        }
        if (isPunchInMethod()) {
            btnSwitchHeader.setOnCheckedChangeListener(null)

            btnSwitchHeader.isChecked = true
            btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)

            tvPunchedInTimeHeader.text = tinyDB.getString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME)
        } else if(!isPunchInMethod())
        {
            btnSwitchHeader.setOnCheckedChangeListener(null)

            btnSwitchHeader.isChecked = false
            btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)


            if (tinyDB.getString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME).equals("")) {
                tvPunchedInTimeHeader.text = "Not Logged In"
            } else {
                tvPunchedInTimeHeader.text = tinyDB.getString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME)

            }


//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                cancelService()
//               // startService(Intent(this, OreoLocationService::class.java))
//
//            } else {
//                stopService(Intent(this, PreOreoLocationService::class.java))
//
//            }


        }
        if (!tinyDB.getString(ConstantsWFMS.TINYDB_SYNC_TIME).equals(""))
            tvSyncTimeDrawer.text = tinyDB.getString(ConstantsWFMS.TINYDB_SYNC_TIME)
        else
            tvSyncTimeDrawer.text = "Not Sync"

    }


    private fun initViews() {
        tinyDB = TinyDB(this)
        token = tinyDB.getString(ConstantsWFMS.TINYDB_TOKEN)
        try {

            if (MyApplication.socketObj.mSocket == null) {
                MyApplication.socketObj.createSocket()
            } else if (!MyApplication.socketObj.mSocket!!.connected()) {
                MyApplication.socketObj.createSocket()

            } else {
                MyApplication.socketObj.mSocket!!.connect()

            }
        } catch (e: Exception) {
            Log.e("", "")
        }
        if (MyApplication.mSocket!!.connected()) {
            MyApplication.mSocket!!.emit("user_connected", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_NAME))
        }





        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        obj = this
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        registerAlarmPunchOut()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            try {
                val componentName = ComponentName(this, JobServiceToUplodData::class.java)
                val job: JobInfo.Builder = JobInfo.Builder(0, componentName)
                job.setRequiresCharging(false)
                job.setOverrideDeadline(0)
                val js: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

                js.schedule(job.build())
            } catch (e: Exception) {
                Log.e("", "")
            }


        } else {
            startService(Intent(this, UploadDataServerService::class.java))
        }



    }

    private fun openDefaultFragment() {
        homeFragment()

    }

    private fun isPunchInMethod(): Boolean {
        return tinyDB!!.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)


    }

    private fun registerAlarmPunchOut() {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 0
        val alarmMgr = this.getSystemService(ALARM_SERVICE) as AlarmManager


        val intent = Intent(this, AlarmReceiverForPunchOut::class.java).putExtra("HOUR_OF_DAY", 0)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_UPDATE_CURRENT)
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 24 * 60 * 60 * 1000, pendingIntent)


    }

    private fun homeFragment() {
        hideShowHeader_FragmentHeading(resources.getString(R.string.strHome), GONE, GONE)


        changeColor(resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))


        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragmentNew())
                .commit()
    }

    private fun hitAPI(TYPE: Int, params: String) {
        if (TYPE == PUNCH_IN_OUT_WFMS) {
            ApiData.getData(params, PUNCH_IN_OUT_WFMS, this, this)
        } else if (TYPE == PUNCH_STATUS_WFMS) {
            ApiData.getData(params, PUNCH_STATUS_WFMS, this, this)

        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        if (Utilities.isShowing())
            Utilities.dismissDialog()
        Log.e("dismiss", TYPE.toString())
        if ((response as Response<*>).code() == 401 || (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(this, tinyDB)
        } else {

            if (TYPE == PUNCH_IN_OUT_WFMS) {
                val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObject.getString("Status").equals("Success"))
                {
                    if (CHECK_TYPE == CHECK_IN) {
                        mPunchInTime = "Punched In: " + mTimeStr + ", " + mDateStr
                     //  tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_TIME, mPunchInTime)

                        tvPunchedInTimeHeader.text = mPunchInTime


                        //btnSwitchHeader.isChecked = true
                        tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN, true)
                        tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME, mPunchInTime)
                        startLocationClass()


                    } else {
                        mPunchOutTime = "Punched Out: " + mTimeStr + ", " + mDateStr
                      //  tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_OUT_TIME, mPunchOutTime)
                        // btnSwitchHeader.isChecked = false

                        tvPunchedInTimeHeader.text = mPunchOutTime
                        tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN, false)
                        tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME, mPunchOutTime)

                    }

                    Utilities.showToast(this, jsonObject.getString("Message"))
                } else {
                    if (CHECK_TYPE == CHECK_IN) {
                        btnSwitchHeader.setOnCheckedChangeListener(null)

                        btnSwitchHeader.isChecked = false
                        btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)
                        tvPunchedInTimeHeader.text = tinyDB.getString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME)

                    } else {

                    }
                    Utilities.showToast(this, jsonObject.getString("Message"))

                }
            } else if (TYPE == PUNCH_STATUS_WFMS)
            {
                val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObject.getString("Status").equals("Success"))
                {
                    val array = jsonObject.getJSONArray("Output")
                    val obj = array.getJSONObject(0)

                    val (mDate,mTime) = ConstantKotlin.getDateTimeServer(obj.getString("PunchDate"),obj.getString("PunchTime"))


                    if(obj.getString("PunchType").equals("I",true))
                    {
                        mPunchInOutTime = "Punched In: " + mTime + ", " + mDate
                        tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME, mPunchOutTime)
                        tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN, true)
                        btnSwitchHeader.isChecked = true
                        btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)

                    }
                    else{
                        btnSwitchHeader.isChecked = false
                        btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)
                        mPunchInOutTime = "Punched Out: " + mTime + ", " + mDate
                        tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN, false)

                        tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME, mPunchOutTime)

                    }

                    tvPunchedInTimeHeader.text = mPunchInOutTime




                }
                else
                {
                    tvPunchedInTimeHeader.text = "Not Logged In"
                    tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME,"")
                    btnSwitchHeader.isChecked = false
                    btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)
                }
            }
            openDefaultFragment()
        }

    }


    @SuppressLint("WrongConstant")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivDrawerHeader -> {
                if (!drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout!!.openDrawer(Gravity.START)

                } else {
                    drawerLayout!!.closeDrawer(Gravity.END)
                }
                "TEst"
            }
            R.id.llInnerHomeDrawer -> {

                clearFilter()
                homeFragment()
                drawerLayout!!.closeDrawer(Gravity.START)

            }
            R.id.llInnerProfileDrawer -> {

                clearFilter()

                fragmentImage = ProfileFragmentWFMS()
                hideShowHeader_FragmentHeading("Profile", GONE, GONE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))
                openFragment(fragmentImage)

                drawerLayout!!.closeDrawer(Gravity.START)

            }
            R.id.llInnerTeamDrawer -> {
                hideShowHeader_FragmentHeading(resources.getString(R.string.strTeam), GONE, VISIBLE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))
                openFragment(TeamFragmentWFMS())


                drawerLayout!!.closeDrawer(Gravity.START)
            }
            R.id.llInnerTaskDrawer -> {
                clearFilter()

                hideShowHeader_FragmentHeading(resources.getString(R.string.strTask), VISIBLE, GONE)


                openFragment(TaskFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

            }

            R.id.llInnerAddProjectDrawer -> {


                clearFilter()

                hideShowHeader_FragmentHeading(resources.getString(R.string.strAddProject), GONE, GONE)

                openFragment(AddDetailsFragment())

                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))


            }


            R.id.llInnerLeaveODDrawer -> {
                clearFilter()

                hideShowHeader_FragmentHeading(resources.getString(R.string.strLeaveOD), VISIBLE, GONE)

                openFragment(LeaveType_OutdoorDutyFragment())

                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

            }
            R.id.llInnerAttendanceDrawer -> {
                clearFilter()


                hideShowHeader_FragmentHeading(resources.getString(R.string.strAttendance), GONE, GONE)

                openFragment(AttendanceFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                "TEst"
            }
            R.id.llInnerDocumentDirectoryDrawer -> {
                clearFilter()

                hideShowHeader_FragmentHeading(resources.getString(R.string.strDocumentDirectory), VISIBLE, GONE)
                fragmentImage = DocumentDirectoryFragmentWFMS()

                openFragment(fragmentImage)

                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                "TEst"
            }
            R.id.llInnerSalaryDrawer -> {
                clearFilter()


                hideShowHeader_FragmentHeading(resources.getString(R.string.strSalary), GONE, GONE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))
                openFragment(SalaryFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)
                "TEst"
            }
            R.id.llInnerClaimDrawer -> {
                clearFilter()

                hideShowHeader_FragmentHeading(resources.getString(R.string.strClaims), GONE, GONE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                openFragment(ClaimFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)
                "TEst"
            }
            R.id.llInnerChatDrawer -> {
                hideShowHeader_FragmentHeading(resources.getString(R.string.strChat), GONE, GONE)


                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ChatFragmentWFMS())
                        .commit()
                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

            }
            R.id.llInnerFormDrawer -> {
                clearFilter()

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                "TEst"
            }
            R.id.llInnerHelpDrawer -> {
                clearFilter()

                hideShowHeader_FragmentHeading(resources.getString(R.string.strHelp), GONE, GONE)
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://wfms.htistelecom.in/home/softwaresupport"))
                startActivity(browserIntent)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                "TEst"
            }
            R.id.llInnerFeedbackDrawer -> {
                clearFilter()

                hideShowHeader_FragmentHeading(resources.getString(R.string.strFeedback), GONE, GONE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite))

                openFragment(FeedbackFragmentWFMS())


                drawerLayout!!.closeDrawer(Gravity.START)


                "TEst"
            }
            R.id.llInnerSettingDrawer -> {
                clearFilter()

                hideShowHeader_FragmentHeading(resources.getString(R.string.strSetting), GONE, GONE)

                openFragment(SettingsFragmentWFMS())


                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange))
                drawerLayout!!.closeDrawer(Gravity.START
                )

                "TEst"
            }


        }
    }

    private fun clearFilter() {
        FilterActivity.filterStatusList.clear()
        FilterActivity.branchList.clear()
    }

    private fun hideShowHeader_FragmentHeading(heading: String, isShowAdd: Int, isShowProfile: Int) {
        tvHeadingHeader.text = heading
        ivAddHeader.visibility = isShowAdd
        ivMyProfileHeader.visibility = isShowProfile

    }


    fun changeColor(colorHome: Int, colorProfile: Int, colorTeam: Int, colorTask: Int, colorAddDetail: Int, colorLeave: Int, colorAttendance: Int, colorDocument: Int, colorSalary: Int, colorClaim: Int, colorChat: Int,
                    colorForm: Int, colorHelp: Int, colorFeedback: Int, colorSetting: Int) {
        ivHomeDrawer.setColorFilter(colorHome)
        tvHomeDrawer.setTextColor(colorHome)

        ivProfileDrawer.setColorFilter(colorProfile)
        tvProfileDrawer.setTextColor(colorProfile)

        ivTeamDrawer.setColorFilter(colorTeam)
        tvTeamDrawer.setTextColor(colorTeam)

        ivTaskDrawer.setColorFilter(colorTask)
        tvTaskDrawer.setTextColor(colorTask)


        ivAddProjectDrawer.setColorFilter(colorAddDetail)
        tvAddProjectDrawer.setTextColor(colorAddDetail)

        ivLeaveODDrawer.setColorFilter(colorLeave)
        tvLeaveODDrawer.setTextColor(colorLeave)

        ivAttendanceDrawer.setColorFilter(colorAttendance)
        tvAttendanceDrawer.setTextColor(colorAttendance)

        ivDocumentDirectoryDrawer.setColorFilter(colorDocument)
        tvDocumentDirectoryDrawer.setTextColor(colorDocument)


        ivSalaryDrawer.setColorFilter(colorSalary)
        tvSalaryDrawer.setTextColor(colorSalary)

        ivClaimDrawer.setColorFilter(colorClaim)
        tvClaimDrawer.setTextColor(colorClaim)
        ivChatDrawer.setColorFilter(colorChat)
        tvChatDrawer.setTextColor(colorChat)
        ivFormDrawer.setColorFilter(colorForm)
        tvFormDrawer.setTextColor(colorForm)
        ivHelpDrawer.setColorFilter(colorHelp)
        tvHelpDrawer.setTextColor(colorHelp)
        ivFeedbackDrawer.setColorFilter(colorFeedback)
        tvFeedbackDrawer.setTextColor(colorFeedback)

        ivSettingDrawer.setColorFilter(colorSetting)
        tvSettingDrawer.setTextColor(colorSetting)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("OD")) {
            tvHeadingHeader.text = "Leave & OD & Comp Off"
            ivAddHeader.visibility = VISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, LeaveType_OutdoorDutyFragment())
                    .commit()
            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Claim")) {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ClaimFragmentWFMS())
                    .commit()
            tvHeadingHeader.text = "Claims"
            ivAddHeader.visibility = GONE
            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Team")) {
            hideShowHeader_FragmentHeading(resources.getString(R.string.strTeam), GONE, VISIBLE)

            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))
            openFragment(TeamFragmentWFMS())

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Settings")) {
            hideShowHeader_FragmentHeading(resources.getString(R.string.strSetting), GONE, GONE)

            openFragment(SettingsFragmentWFMS())


            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange))

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Home")) {
            homeFragment()
        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Task")) {
            hideShowHeader_FragmentHeading(resources.getString(R.string.strTask), VISIBLE, GONE)


            openFragment(TaskFragmentWFMS())

            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Attendance")) {
            hideShowHeader_FragmentHeading(resources.getString(R.string.strAttendance), GONE, GONE)

            openFragment(AttendanceFragmentWFMS())


            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

        }


    }

    fun openFragment(frag: Fragment) {

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frag)

                .commit()
    }

    override fun onBackPressed() {
        var f: Fragment? = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (f is HomeFragmentNew) {
//do smth
            finish()
        } else {
            homeFragment()
        }

    }

//    private fun checkNavigationMenuItem(): Int {
//        val menu: Menu = navigationView.menu
//        for (i in 0 until menu.size()) {
//            if (menu.getItem(i).isChecked()) return i
//        }
//        return -1
//    }

    override fun onResume() {
        super.onResume()

        receiver = AlarmReceiverForPunchOut()
        val intentFilter = IntentFilter("com.htistelecom.htisinhouse.PUNCH_OUT")
        registerReceiver(receiver, intentFilter)


        val broadcastReceiverUpdateTime = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent!!.action.equals("FOR_UPDATE_TIME")) {
                    val syncTime = "Last Sync:" + intent!!.getStringExtra("data")
                    val mPunchOutTime = "Punched Out:" + intent!!.getStringExtra("data")
                    val isCheckOut = intent!!.getBooleanExtra("IsCheckOut", false)
                    tvSyncTimeDrawer.text = syncTime
                    tinyDB.putString(ConstantsWFMS.TINYDB_SYNC_TIME, syncTime)

                    if (isCheckOut) {
                        tvPunchedInTimeHeader.text = mPunchOutTime
                        tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_OUT_TIME, mPunchOutTime)

                    }
                }


            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverUpdateTime, IntentFilter("FOR_UPDATE_TIME"))







        registerReceiver(mGpsSwitchStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

    }

    private val mGpsSwitchStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals("android.location.PROVIDERS_CHANGED")) {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (isGpsEnabled) {

                    // Handle Location turned ON
                } else {
                    openDialog()
                    // Handle Location turned OFF
                    //    context.startActivity(Intent(context, AlertDialogActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }
        }
    }

    private fun onGPSDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.gps_disabled_message)
                .setCancelable(false)
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, id: Int) {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                })
                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        dialog.cancel()
                    }
                })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onPause() {
        super.onPause()

        //  unregisterReceiver(mGpsSwitchStateReceiver)
    }

    fun showAlertDialogForPunchOut(params: String) {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(R.string.strPunchOut)
                .setCancelable(false)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            hitAPI(ConstantsWFMS.PUNCH_IN_OUT_WFMS, params)
            cancelService()

        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
            btnSwitchHeader.setOnCheckedChangeListener(null)

            btnSwitchHeader.isChecked = true
            btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)

        }
        //Creating dialog box
        val alert = builder.create()
        //Setting the title manually
        alert.show();

    }


    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy()

        // unregisterReceiver(broadcastReceiver);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri = CropImage.getPickImageResultUri(this, data)

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                //  mCropImageUri = imageUri
                //  requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            } else {

                if (fragmentImage is ProfileFragmentWFMS) {

                    (fragmentImage as ProfileFragmentWFMS).callMethod(imageUri)

                } else {
                    (fragmentImage as DocumentDirectoryFragmentWFMS).callMethod(imageUri)
                }

                // no permissions required or already grunted, can start crop image activity
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun cancelService() {
        val am: ActivityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses: List<ActivityManager.RunningAppProcessInfo> = am.getRunningAppProcesses()

        val iter: Iterator<ActivityManager.RunningAppProcessInfo> = runningAppProcesses.iterator()

        while (iter.hasNext()) {
            val next: ActivityManager.RunningAppProcessInfo = iter.next()
            val pricessName = "$packageName:service"
            if (next.processName.equals(pricessName)) {
                android.os.Process.killProcess(next.pid)
                break
            }
        }
    }

    private fun startLocationClass() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // startService(Intent(this, OreoLocationService::class.java))
            val it = Intent(this,OreoLocationService::class.java)
            OreoLocationService.enqueueWork(this, it);

        } else {
            startService(Intent(this, PreOreoLocationService::class.java))

        }

    }


    private fun checkForUpdate() {

        val appVersion: String = getAppVersion(this)
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val currentVersion =
                remoteConfig.getString("min_version_of_app")
        val minVersion =
                remoteConfig.getString("latest_version_of_app")
        if (!TextUtils.isEmpty(minVersion) && !TextUtils.isEmpty(appVersion) && checkMandateVersionApplicable(
                        getAppVersionWithoutAlphaNumeric(minVersion),
                        getAppVersionWithoutAlphaNumeric(appVersion)
                )
        ) {
            onUpdateNeeded(true)
        } else if (!TextUtils.isEmpty(currentVersion) && !TextUtils.isEmpty(appVersion) && !TextUtils.equals(
                        currentVersion,
                        appVersion
                )
        ) {
            //   onUpdateNeeded(false)
        } else {
            moveForward()
        }
    }


    private fun checkMandateVersionApplicable(
            minVersion: String,
            appVersion: String
    ): Boolean {
        return try {
            val minVersionInt = minVersion.toInt()
            val appVersionInt = appVersion.toInt()
            minVersionInt > appVersionInt
        } catch (exp: NumberFormatException) {
            false
        }
    }

    private fun getAppVersion(context: Context): String {
        var result: String? = ""
        try {
            result = context.packageManager
                    .getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("TAG", e.message)
        }
        return result ?: ""
    }

    private fun getAppVersionWithoutAlphaNumeric(result: String): String {
        var version_str = ""
        version_str = result.replace(".", "")
        return version_str
    }

    private fun onUpdateNeeded(isMandatoryUpdate: Boolean) {


        val dialogBuilder = AlertDialog.Builder(this)
                .setTitle("New Version Available")
                .setCancelable(false)
                .setMessage(if (isMandatoryUpdate) "A new version is found on Play store, please update for better usage." else "A new version is found on Play store, please update for better usage.")
                .setPositiveButton("now")
                { dialog, which ->
                    openAppOnPlayStore(this, null)
                }


        dialogBuilder.setNegativeButton("Later") { dialog, which ->
            moveForward()
            dialog?.dismiss()
        }.create()

        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()
    }

    private fun moveForward() {
        // Toast.makeText(this, "Next Page Intent", Toast.LENGTH_SHORT).show()
    }

    fun openAppOnPlayStore(ctx: Context, package_name: String?) {
        var package_name = package_name
        if (package_name == null) {
            package_name = ctx.packageName
        }
        val uri = Uri.parse("market://details?id=$package_name")
        openURI(ctx, uri, "Play Store not found in your device")
    }

    fun openURI(
            ctx: Context,
            uri: Uri?,
            error_msg: String?
    ) {
        val i = Intent(Intent.ACTION_VIEW, uri)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        if (ctx.packageManager.queryIntentActivities(i, 0).size > 0) {
            ctx.startActivity(i)
        } else if (error_msg != null) {
            Toast.makeText(this, error_msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

}

