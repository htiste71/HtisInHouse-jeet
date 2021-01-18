package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.ATTENDANCE_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.LeaveDetailActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.AttendanceModel
import com.htistelecom.htisinhouse.activity.WFMS.models.MyAttendanceModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Utilities
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import kotlinx.android.synthetic.main.fragment_attendance_wfms.*
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendanceFragmentWFMS : BaseFragment(), MyInterface, View.OnClickListener {
    lateinit var tinyDB: TinyDB
    var calendarList = ArrayList<MyAttendanceModel>()
    var model: AttendanceModel? = null
    var disable = ArrayList<String>()

    // AttendanceListPresenter presenter;
    var caldroidFragment: CaldroidFragment? = null
    var present = 0
    var absent: Int = 0
    var leave: Int = 0
    var holiday: Int = 0
    var pendingApproval: Int = 0
    var rejection: Int = 0
    var month: Int = 0
    var year: Int = 0
    private var formatter: SimpleDateFormat? = null
    var startDate = ""
    var endDate = ""
    var isFirst = false
    lateinit var calc: Calendar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attendance_wfms, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()


    }

    private fun listeners() {
        btnLeaveDetail.setOnClickListener(this)
    }

    private fun initViews() {
        tinyDB = TinyDB(activity!!)

        calc = Calendar.getInstance()
        month = calc.get(Calendar.MONTH) + 1
        year = calc.get(Calendar.YEAR)

        calc.set(year, month - 1, 1)
        endDate = calc.getActualMaximum(Calendar.DAY_OF_MONTH).toString() + "-" + Utilities.getMonthFormat(month) + "-" + year
        startDate = "01-" + Utilities.getMonthFormat(month) + "-" + year
        formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        caldroidFragment = CaldroidFragment()
    }

    fun attendanceList(attendanceList: ArrayList<MyAttendanceModel>) {
        calendarList = attendanceList
        for (i in calendarList.indices) {
            if (calendarList[i].dayStatus.equals("WW", ignoreCase = true)) {
                val curDate = calendarList[i].attendanceDate
                caldroidFragment!!.setBackgroundDrawableForDate(ColorDrawable(resources.getColor(R.color.grey_color)), getDateFormatted(curDate))
                caldroidFragment!!.setTextColorForDate(R.color.white, getDateFormatted(curDate))
                // holiday++;
            } else if (calendarList[i].dayStatus.equals("PP", ignoreCase = true)) {
                val curDate = calendarList[i].attendanceDate
                caldroidFragment!!.setBackgroundDrawableForDate(ColorDrawable(resources.getColor(R.color.green)), getDateFormatted(curDate))
                caldroidFragment!!.setTextColorForDate(R.color.white, getDateFormatted(curDate))
                present++
            } else if (calendarList[i].dayStatus.equals("AA", ignoreCase = true)) {
                val curDate = calendarList[i].attendanceDate
                caldroidFragment!!.setBackgroundDrawableForDate(ColorDrawable(resources.getColor(R.color.red)), getDateFormatted(curDate))
                caldroidFragment!!.setTextColorForDate(R.color.white, getDateFormatted(curDate))
                ++absent
            } else if (calendarList[i].dayStatus.equals("HH", ignoreCase = true)) {
                val curDate = calendarList[i].attendanceDate
                caldroidFragment!!.setBackgroundDrawableForDate(ColorDrawable(resources.getColor(R.color.grey_color)), getDateFormatted(curDate))
                caldroidFragment!!.setTextColorForDate(R.color.white, getDateFormatted(curDate))
                holiday++
            } else if (calendarList[i].dayStatus.equals("LL", ignoreCase = true)) {
                val curDate = calendarList[i].attendanceDate
                caldroidFragment!!.setBackgroundDrawableForDate(ColorDrawable(resources.getColor(R.color.blue)), getDateFormatted(curDate))
                caldroidFragment!!.setTextColorForDate(R.color.white, getDateFormatted(curDate))
                leave++
            } else if (calendarList[i].dayStatus.equals("PA", ignoreCase = true)) {
                val curDate = calendarList[i].attendanceDate
                caldroidFragment!!.setBackgroundDrawableForDate(ColorDrawable(resources.getColor(R.color.yellow)), getDateFormatted(curDate))
                caldroidFragment!!.setTextColorForDate(R.color.black, getDateFormatted(curDate))
                pendingApproval++
            } else if (calendarList[i].dayStatus.equals("RR", ignoreCase = true)) {
                val curDate = calendarList[i].attendanceDate
                caldroidFragment!!.setBackgroundDrawableForDate(ColorDrawable(resources.getColor(R.color.brown)), getDateFormatted(curDate))
                caldroidFragment!!.setTextColorForDate(R.color.white, getDateFormatted(curDate))
                rejection++
            }

        }

        tvPresentAttendanceCountWFMS.setText("" + present)
        tvAbsentAttendanceCountWFMS.setText("" + absent)
        tvLeaveAttendanceCountWFMS.setText("" + leave)
        tvPendingAttendanceCountWFMS.setText("" + pendingApproval)
        tvRejectionAttendanceCountWFMS.setText("" + rejection)
        tvHolidayAttendanceCountWFMS.setText("" + holiday)
        setAdapterCalendarList()
        clearValues()
    }


    fun getDateFormatted(date: String): Date? {
        val dateTemp = date.split("-".toRegex()).toTypedArray()
        val month = Utilities.getMonthFromString(dateTemp[1])
        val year = dateTemp[2].toInt() - 1900
        val currentDate = Date()
        currentDate.date = dateTemp[0].toInt()
        currentDate.month = month
        currentDate.year = year
        return currentDate
    }


    private fun clearValues() {
        if (caldroidFragment != null) {
            caldroidFragment!!.clearDisableDates()
        }
        absent = 0
        present = 0
        leave = 0
        holiday = 0
        pendingApproval = 0
        rejection = 0
    }


    private fun setCustomResourceForDates() {
        if (caldroidFragment != null) {
            val grey = ColorDrawable(resources.getColor(R.color.grey_color))
            if (disable != null) {
                //this is for sundays
                for (j in disable.indices) {
                    Log.e("tag", "disable" + disable[j])
                    caldroidFragment!!.setBackgroundDrawableForDate(grey, getDateFormatted(disable[j]))
                    caldroidFragment!!.setTextColorForDate(R.color.white, getDateFormatted(disable[j]))
                }
            }
            caldroidFragment!!.refreshView()
        }
    }


    private fun setAdapterCalendarList() {
        setCustomResourceForDates()
        // Attach to the activity
        val t: FragmentTransaction = activity!!.getSupportFragmentManager().beginTransaction()
        t.replace(R.id.calendar1, caldroidFragment!!)
        t.commitAllowingStateLoss()
        // Setup listener
        val listener: CaldroidListener = object : CaldroidListener() {
            override fun onSelectDate(date: Date, view: View) {
                for (i in calendarList.indices) {
                    if (calendarList[i].dayStatus.equals("HH", ignoreCase = true) || calendarList[i].dayStatus.equals("WW", ignoreCase = true)) {
                        if (calendarList[i].attendanceDate.equals(formatter!!.format(date), ignoreCase = true)) {
                            customdialog(calendarList[i].title)
                        }
                    }
                }
            }

            @SuppressLint("WrongConstant")
            override fun onChangeMonth(month: Int, year: Int) {
//                caldroidFragment.clearBackgroundDrawableForDate();
                val text = "month: $month year: $year"
                if (isFirst) {
                    calc[year, month - 1] = 1
                    endDate = calc.getActualMaximum(Calendar.DAY_OF_MONTH).toString() + "-" + Utilities.getMonthFormat(month) + "-" + year
                    startDate = "01-" + Utilities.getMonthFormat(month) + "-" + year
                    clearData()
                    calledMethod()
                }
                isFirst = true
            }

            override fun onLongClickDate(date: Date, view: View) {
                //UtilitiesWFMS.showToast(MyAttendanceActivity.this, "Long click ");
            }
        }

        // Setup Caldroid
        caldroidFragment!!.caldroidListener = listener
    }

    private fun calledMethod() {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
            jsonObject.put("FromDate", startDate)
            jsonObject.put("ToDate", endDate)
            ApiData.getData(jsonObject.toString(), ATTENDANCE_LIST_WFMS, this, activity)
        } catch (e: Exception) {
            Log.e("Error", e.message)
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity!!.getIntent().getExtras() != null) {
            caldroidFragment!!.restoreStatesFromKey(activity!!.getIntent().getExtras(),
                    "CALDROID_SAVED_STATE")
        } else {
            val args = Bundle()
            args.putInt(CaldroidFragment.MONTH, calc[Calendar.MONTH] + 1)
            args.putInt(CaldroidFragment.YEAR, calc[Calendar.YEAR])
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true)
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true)
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true)
            caldroidFragment!!.arguments = args
        }


        setAdapterCalendarList()
        calledMethod()
    }


    fun customdialog(message: String?) {
        AlertDialog.Builder(activity)
                .setTitle("Holiday Details")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes) { dialog, which -> dialog.cancel() }
                .show()
    }


    override fun sendResponse(response: Any, TYPE: Int) {
        Utilities.dismissDialog()


        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(activity!!, tinyDB)
        } else {
            if (TYPE == ATTENDANCE_LIST_WFMS) {
                try {
                    val jsonObject = JSONObject((response as Response<*>).body().toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        val attendanceListData = Gson().fromJson<java.util.ArrayList<MyAttendanceModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<ArrayList<MyAttendanceModel>>() {}.type)
                        attendanceList(attendanceListData)
                    } else {
                        Utilities.showToast(activity, "No Details found.")
                        tvPresentAttendanceCountWFMS.setText("" + present)
                        tvAbsentAttendanceCountWFMS.setText("" + absent)
                        tvLeaveAttendanceCountWFMS.setText("" + leave)
                        tvPendingAttendanceCountWFMS.setText("" + pendingApproval)
                        tvRejectionAttendanceCountWFMS.setText("" + rejection)
                        tvHolidayAttendanceCountWFMS.setText("" + holiday)
                    }
                } catch (e: java.lang.Exception) {
                    Log.e("Error", e.message)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLeaveDetail -> {
                val intent = Intent(activity, LeaveDetailActivityWFMS::class.java)
                startActivity(intent)
            }


        }

    }

    fun clearData() {
        present = 0
        absent = 0
        leave = 0
        holiday = 0
        pendingApproval = 0
        rejection = 0
    }
}