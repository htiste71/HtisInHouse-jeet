package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CompoundButton
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.AlertDialogActivity
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PUNCH_IN_OUT_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.claims.ClaimFragmentWFMS
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.DocumentDirectoryFragmentWFMS
import com.htistelecom.htisinhouse.activity.WFMS.fragments.*
import com.htistelecom.htisinhouse.activity.WFMS.leave_managment.LeaveType_OutdoorDutyFragment
import com.htistelecom.htisinhouse.activity.WFMS.receiver.AlarmReceiverForPunchOut
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.UploadDataServerService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.drawer_items.*
import kotlinx.android.synthetic.main.nav_header_main_activity_navigation.*
import kotlinx.android.synthetic.main.toolbar_drawer.*
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class MainActivityNavigation : AppCompatActivity(), View.OnClickListener, MyInterface {

    private var mTimeStr: String = ""
    private var mDateStr: String = ""

    private var mTime24Hrs: String = ""
    private var mPunchInTime: String = ""
    private var mPunchOutTime: String = ""

    companion object {
        lateinit var obj: MainActivityNavigation
        var isBackHome = false
    }

    var CHECK_TYPE = -1
    val CHECK_IN = 1
    val CHECK_OUT = 2

    lateinit var tinyDB: TinyDB

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        initViews()

        listeneres()
        openDefaultFragment()
        setDefautData()


        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(p0: View, p1: Float) {
            }

            override fun onDrawerClosed(p0: View) {
            }


            override fun onDrawerOpened(p0: View) {
                tvTaskCountDrawer.text = HomeFragmentWFMS.mTaskCount
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

    private fun listeneres() {
        ivDrawerHeader.setOnClickListener(this)
        llInnerHomeDrawer.setOnClickListener(this)
        llInnerProfileDrawer.setOnClickListener(this)
        llInnerTeamDrawer.setOnClickListener(this)
        llInnerTaskDrawer.setOnClickListener(this)
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

    val mOnCheckChangedListener = CompoundButton.OnCheckedChangeListener { compoundButton, b ->
        val mAddress = Utilities.getAddressFromLatLong(this, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())
        val (mTime, mDate) = ConstantKotlin.getCurrentDateTime()
        mTimeStr = mTime
        mDateStr = mDate

        mTime24Hrs = ConstantKotlin.getCurrentTime24Hrs()

        val mDateTime = DateUtils.currentDate() + " " + mTime24Hrs

        if (b) {

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
        }
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

            tvPunchedInTimeHeader.text = tinyDB.getString(ConstantsWFMS.TINYDB_PUNCH_IN_TIME)
        } else {
            btnSwitchHeader.setOnCheckedChangeListener(null)

            btnSwitchHeader.isChecked = false
            btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)

            if (tinyDB.getString(ConstantsWFMS.TINYDB_PUNCH_OUT_TIME).equals("")) {
                tvPunchedInTimeHeader.text = "Not Logged In"
            } else {
                tvPunchedInTimeHeader.text = tinyDB.getString(ConstantsWFMS.TINYDB_PUNCH_OUT_TIME)

            }


        }
        if (!tinyDB.getString(ConstantsWFMS.TINYDB_SYNC_TIME).equals(""))
            tvSyncTimeDrawer.text = tinyDB.getString(ConstantsWFMS.TINYDB_SYNC_TIME)
        else
            tvSyncTimeDrawer.text = "Not Sync"

    }


    private fun initViews() {
        obj = this
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        tinyDB = TinyDB(this)
        registerAlarmPunchOut()
        startService(Intent(this, UploadDataServerService::class.java))


    }

    private fun openDefaultFragment() {
        homeFragment()

    }

    private fun isPunchInMethod(): Boolean {
        return tinyDB!!.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)


    }

    private fun registerAlarmPunchOut() {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        val alarmMgr = this.getSystemService(ALARM_SERVICE) as AlarmManager


        val intent = Intent(this, AlarmReceiverForPunchOut::class.java).putExtra("HOUR_OF_DAY", 0)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_UPDATE_CURRENT)
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 24 * 60 * 60 * 1000, pendingIntent)


    }

    private fun homeFragment() {
        hideShowHeader_FragmentHeading(resources.getString(R.string.strHome), GONE, GONE)


        changeColor(resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))


        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragmentWFMS())
                .commit()
    }

    private fun hitAPI(TYPE: Int, params: String) {
        if (TYPE == PUNCH_IN_OUT_WFMS) {
            ApiData.getData(params, PUNCH_IN_OUT_WFMS, this, this)
        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == PUNCH_IN_OUT_WFMS) {
            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObject.getString("Status").equals("Success")) {
                if (CHECK_TYPE == CHECK_IN) {
                    mPunchInTime = "Punched In: " + mTimeStr + ", " + mDateStr
                    tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_TIME, mPunchInTime)

                    tvPunchedInTimeHeader.text = mPunchInTime


                    //btnSwitchHeader.isChecked = true
                    tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN, true)
                    tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_IN_TIME, mPunchInTime)
                } else {
                    mPunchOutTime = "Punched Out: " + mTimeStr + ", " + mDateStr
                    tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_OUT_TIME, mPunchOutTime)
                    // btnSwitchHeader.isChecked = false

                    tvPunchedInTimeHeader.text = mPunchOutTime
                    tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN, false)
                    tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_OUT_TIME, mPunchOutTime)

                }

                Utilities.showToast(this, jsonObject.getString("Message"))
            } else {
                if (CHECK_TYPE == CHECK_IN) {
                    btnSwitchHeader.setOnCheckedChangeListener(null)

                    btnSwitchHeader.isChecked = false
                    btnSwitchHeader.setOnCheckedChangeListener(mOnCheckChangedListener)
                    tvPunchedInTimeHeader.text = tinyDB.getString(ConstantsWFMS.TINYDB_PUNCH_OUT_TIME)

                } else {

                }
                Utilities.showToast(this, jsonObject.getString("Message"))

            }
        }


    }


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


                homeFragment()
                drawerLayout!!.closeDrawer(Gravity.START)

            }
            R.id.llInnerProfileDrawer -> {

                hideShowHeader_FragmentHeading("Profile", GONE, GONE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))
                openFragment(ProfileFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)

            }
            R.id.llInnerTeamDrawer -> {
                hideShowHeader_FragmentHeading(resources.getString(R.string.strTeam), GONE, VISIBLE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))
                openFragment(TeamFragmentWFMS())


                drawerLayout!!.closeDrawer(Gravity.START)
            }
            R.id.llInnerTaskDrawer -> {

                hideShowHeader_FragmentHeading(resources.getString(R.string.strTask), VISIBLE, GONE)


                openFragment(TaskFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

            }
            R.id.llInnerLeaveODDrawer -> {
                hideShowHeader_FragmentHeading(resources.getString(R.string.strLeaveOD), VISIBLE, GONE)

                openFragment(LeaveType_OutdoorDutyFragment())

                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

            }
            R.id.llInnerAttendanceDrawer -> {


                hideShowHeader_FragmentHeading(resources.getString(R.string.strAttendance), GONE, GONE)

                openFragment(AttendanceFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                "TEst"
            }
            R.id.llInnerDocumentDirectoryDrawer -> {
                hideShowHeader_FragmentHeading(resources.getString(R.string.strDocumentDirectory), VISIBLE, GONE)

                openFragment(DocumentDirectoryFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                "TEst"
            }
            R.id.llInnerSalaryDrawer -> {

                hideShowHeader_FragmentHeading(resources.getString(R.string.strSalary), GONE, GONE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))
                openFragment(SalaryFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)
                "TEst"
            }
            R.id.llInnerClaimDrawer -> {
                hideShowHeader_FragmentHeading(resources.getString(R.string.strClaims), GONE, GONE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                openFragment(ClaimFragmentWFMS())

                drawerLayout!!.closeDrawer(Gravity.START)
                "TEst"
            }
            R.id.llInnerChatDrawer -> {
                hideShowHeader_FragmentHeading(resources.getString(R.string.strChat), GONE, GONE)


                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ChatFragmentWFMS())
                        .commit()
                drawerLayout!!.closeDrawer(Gravity.START)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

            }
            R.id.llInnerFormDrawer -> {
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                "TEst"
            }
            R.id.llInnerHelpDrawer -> {

                hideShowHeader_FragmentHeading(resources.getString(R.string.strHelp), GONE, GONE)
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://wfms.htistelecom.in/home/softwaresupport"))
                startActivity(browserIntent)
                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

                "TEst"
            }
            R.id.llInnerFeedbackDrawer -> {

                hideShowHeader_FragmentHeading(resources.getString(R.string.strFeedback), GONE, GONE)

                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite))

                openFragment(FeedbackFragmentWFMS())


                drawerLayout!!.closeDrawer(Gravity.START)


                "TEst"
            }
            R.id.llInnerSettingDrawer -> {

                hideShowHeader_FragmentHeading(resources.getString(R.string.strSetting), GONE, GONE)

                openFragment(SettingsFragmentWFMS())


                changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange))
                drawerLayout!!.closeDrawer(Gravity.START
                )

                "TEst"
            }


        }
    }

    private fun hideShowHeader_FragmentHeading(heading: String, isShowAdd: Int, isShowProfile: Int) {
        tvHeadingHeader.text = heading
        ivAddHeader.visibility = isShowAdd
        ivMyProfileHeader.visibility = isShowProfile

    }


    fun changeColor(colorHome: Int, colorProfile: Int, colorTeam: Int, colorTask: Int, colorLeave: Int, colorAttendance: Int, colorDocument: Int, colorSalary: Int, colorClaim: Int, colorChat: Int,
                    colorForm: Int, colorHelp: Int, colorFeedback: Int, colorSetting: Int) {
        ivHomeDrawer.setColorFilter(colorHome)
        tvHomeDrawer.setTextColor(colorHome)

        ivProfileDrawer.setColorFilter(colorProfile)
        tvProfileDrawer.setTextColor(colorProfile)

        ivTeamDrawer.setColorFilter(colorTeam)
        tvTeamDrawer.setTextColor(colorTeam)

        ivTaskDrawer.setColorFilter(colorTask)
        tvTaskDrawer.setTextColor(colorTask)

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
            tvHeadingHeader.text = "Leave & OD"
            ivAddHeader.visibility = VISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, LeaveType_OutdoorDutyFragment())
                    .commit()
            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Claim")) {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ClaimFragmentWFMS())
                    .commit()
            tvHeadingHeader.text = "Claims"
            ivAddHeader.visibility = GONE
            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Team")) {
            hideShowHeader_FragmentHeading(resources.getString(R.string.strTeam), GONE, VISIBLE)

            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))
            openFragment(TeamFragmentWFMS())

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Settings")) {
            hideShowHeader_FragmentHeading(resources.getString(R.string.strSetting), GONE, GONE)

            openFragment(SettingsFragmentWFMS())


            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange))

        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Home")) {
            homeFragment()
        } else if (intent!!.getStringExtra("fragment") != null && intent!!.getStringExtra("fragment").equals("Task")) {
            hideShowHeader_FragmentHeading(resources.getString(R.string.strTask), VISIBLE, GONE)


            openFragment(TaskFragmentWFMS())

            changeColor(resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite), resources.getColor(R.color.colorWhite))

        }


    }

    fun openFragment(frag: Fragment) {

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frag)

                .commit()
    }

    override fun onBackPressed() {
        var f: Fragment? = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (f is HomeFragmentWFMS) {
//do smth
            finish()
        } else {
          homeFragment()
        }

    }

    private fun checkNavigationMenuItem(): Int {
        val menu: Menu = navigationView.menu
        for (i in 0 until menu.size()) {
            if (menu.getItem(i).isChecked()) return i
        }
        return -1
    }

    override fun onResume() {
        super.onResume()

        val receiver: BroadcastReceiver = AlarmReceiverForPunchOut()
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
                        tinyDB.putString(ConstantsWFMS.TINYDB_PUNCH_OUT_TIME, mPunchOutTime)

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
                    // Handle Location turned OFF
                    context.startActivity(Intent(context, AlertDialogActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
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

        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        //Creating dialog box
        val alert = builder.create()
        //Setting the title manually
        alert.show();

    }


    override fun onDestroy() {
        super.onDestroy()
        // unregisterReceiver(broadcastReceiver);

    }


}

