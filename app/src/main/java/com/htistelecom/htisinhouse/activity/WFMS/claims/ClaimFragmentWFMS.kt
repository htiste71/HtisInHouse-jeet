package com.htistelecom.htisinhouse.activity.WFMS.claims

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.ATTENDANCE_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_SUMMARY_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.advance_claims.activity.AdvanceStatusActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel
import com.htistelecom.htisinhouse.activity.WFMS.models.MyAttendanceModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Utilities
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import kotlinx.android.synthetic.main.fragment_claims_wfms.*
import kotlinx.android.synthetic.main.layout_claim_detail.*
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ClaimFragmentWFMS : BaseFragment(), MyInterface, View.OnClickListener {
    private var ivAddClaim: ImageView? = null
    var month = 0
    var year: Int = 0
    var caldroidFragment: CaldroidFragment? = null
    var isFirst = false
    lateinit var cal: Calendar
    private var formatter: SimpleDateFormat? = null
    var startDate: String? = null
    var endDate: kotlin.String? = null
    var disable = ArrayList<String>()
    var claimDates = ArrayList<String>()
    var claimSummaryList = ArrayList<com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel>()
  lateinit  var tinyDB: TinyDB
    var args: Bundle? = null
    lateinit var calc: Calendar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_claims_wfms, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()
    }

    private fun listeners() {
        tvRequestAdvanceClaim.setOnClickListener(this)
        ivAddClaim!!.setOnClickListener(this)
    }

    private fun initViews() {
        tinyDB = TinyDB(activity)
        ivAddClaim = activity!!.findViewById<ImageView>(R.id.ivAddHeader)

        calc = Calendar.getInstance()
        month = Calendar.getInstance()[Calendar.MONTH] + 1
        year = Calendar.getInstance()[Calendar.YEAR]
        formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        calc.set(year, month - 1, 1)
        endDate = calc.getActualMaximum(Calendar.DAY_OF_MONTH).toString() + "-" + Utilities.getMonthFormat(month) + "-" + year
        startDate = "01-" + Utilities.getMonthFormat(month) + "-" + year
        args = Bundle()
        caldroidFragment = CaldroidFragment()
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

//                val intent = Intent(activity, TaskCompletedActivityWFMS::class.java).putExtra("date", UtilitiesWFMS.dateToString(date)).putExtra("data", claimSummaryList.get(0))
//
//                startActivity(intent)
                // convert the data format
                //   Utilities.showToast(activity, "" + formatter!!.format(date))
                var isValue = false
                if (claimDates != null) {
                    for (i in 0..claimDates.size - 1) {
                        if (claimDates[i].equals(formatter!!.format(date), ignoreCase = true)) {
                            isValue = true
                            break
                        }
                    }
                }
                if (isValue) {
                    val intent = Intent(activity, TaskCompletedActivityWFMS::class.java)
                    intent.putExtra("date", formatter!!.format(date))
                    intent.putExtra("data", claimSummaryList.get(0))
                    startActivity(intent)
                } else {
                    Utilities.showToast(activity, "No Claims")
                }
            }

            @SuppressLint("WrongConstant")
            override fun onChangeMonth(month: Int, year: Int) {
//                caldroidFragment.clearBackgroundDrawableForDate();
                val text = "month: $month year: $year"
                if (isFirst) {
                    val calc = Calendar.getInstance()
                    calc[year, month - 1] = 1
                    endDate = calc.getActualMaximum(Calendar.DAY_OF_MONTH).toString() + "-" + Utilities.getMonthFormat(month) + "-" + year
                    startDate = "01-" + Utilities.getMonthFormat(month) + "-" + year
                    //  weekend(month, year)
                    //setAdapterCalendarList();
                    calledMethod(ATTENDANCE_LIST_WFMS)
                }
                isFirst = true
            }

            override fun onLongClickDate(date: Date, view: View) {
                Utilities.showToast(activity, "Long click " + formatter!!.format(date))
            }
        }

        // Setup Caldroid
        caldroidFragment!!.caldroidListener = listener
    }

    private fun calledMethod(type: Int) {
        if (type == ConstantsWFMS.ATTENDANCE_LIST_WFMS) {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                jsonObject.put("FromDate", startDate)
                jsonObject.put("ToDate", endDate)
                ApiData.getData(jsonObject.toString(), ConstantsWFMS.ATTENDANCE_LIST_WFMS, this, activity)
            } catch (e: Exception) {
                Log.e("Error", e.message)
            }
        } else if (type == ConstantsWFMS.CLAIM_SUMMARY_WFMS) {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                jsonObject.put("FromDate", startDate)
                jsonObject.put("ToDate", endDate)
                ApiData.getData(jsonObject.toString(), CLAIM_SUMMARY_WFMS, this, activity)
            } catch (e: Exception) {
                Log.e("Error", e.message)
            }
        }
    }

    private fun setCustomResourceForDates() {
        if (caldroidFragment != null) {
            if (disable != null) {
                //this is for sundays background color change
                for (j in disable.indices) {
                    Log.e("tag", "disable" + disable[j])
                    caldroidFragment!!.setTextColorForDate(R.color.black, getDateFormatted(disable[j]))
                }
            }
            caldroidFragment!!.refreshView()
        }
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


    fun weekend(currentmonth: Int, year: Int): List<String?>? {
        disable = java.util.ArrayList()
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = 1
        cal[Calendar.MONTH] = currentmonth - 1
        cal[Calendar.YEAR] = year
        val month = currentmonth - 1
        do {
            val dayOfWeek = cal[Calendar.DAY_OF_WEEK]
            if (dayOfWeek == Calendar.SUNDAY) {
                val d = cal.time
                disable.add(formatter!!.format(cal.time))
            }
            cal.add(Calendar.DAY_OF_MONTH, 1)
        } while (cal[Calendar.MONTH] == month)
        return disable
    }

    fun attendanceList(attendanceList: ArrayList<MyAttendanceModel>) {
        var data = 0
        for (i in attendanceList.indices) {
            if (attendanceList[i].dayStatus.equals("PP", ignoreCase = true)) {
                val curDate = attendanceList[i].attendanceDate
                claimDates.add(data, curDate)
                data += 1
                caldroidFragment!!.setBackgroundDrawableForDate(ColorDrawable(resources.getColor(R.color.green)), getDateFormatted(curDate))
                caldroidFragment!!.setTextColorForDate(R.color.white, getDateFormatted(curDate))
            }
        }
        setAdapterCalendarList()
    }


    override fun onResume() {
        super.onResume()
        if (activity!!.getIntent().getExtras() != null) {
            caldroidFragment!!.restoreStatesFromKey(activity!!.getIntent().getExtras(),
                    "CALDROID_SAVED_STATE")
        } else {
            cal = Calendar.getInstance()
            args!!.putInt(CaldroidFragment.MONTH, cal[Calendar.MONTH] + 1)
            args!!.putInt(CaldroidFragment.YEAR, cal[Calendar.YEAR])
            args!!.putBoolean(CaldroidFragment.ENABLE_SWIPE, true)
            args!!.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true)
            args!!.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true)
            caldroidFragment!!.arguments = args
        }

        //calender view
        //  weekend(month, year)
        setAdapterCalendarList()
        // call the attendance presenter
        calledMethod(ATTENDANCE_LIST_WFMS)


        //callClaimSummaryData();
    }


    private fun setTheClaimSummary(model: com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel) {
        try {
            tvTotalClaimFragmentWFMS.text = ":" + model.claimedAmount
            tvApprovedClaimFragmentWFMS.text = ": " + model.approvedAmount
            tvAdvanceClaimFragmentWFMS.text = ": " + model.advancePaid
            var mPending = model.claimedAmount.toDouble() - model.approvedAmount.toDouble() - model.rejectedAmount.toDouble()

            tvPendingClaimFragmentWFMS.text = ": " + mPending
        } catch (e: Exception) {

        }


    }

    override fun sendResponse(response: Any, TYPE: Int) {
        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(activity!!, tinyDB)
        } else {


            if (TYPE != ATTENDANCE_LIST_WFMS)
                Utilities.dismissDialog()
            if (TYPE == ATTENDANCE_LIST_WFMS) {
                try {
                    val jsonObject = JSONObject((response as Response<*>).body().toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        val attendanceListData = Gson().fromJson<java.util.ArrayList<MyAttendanceModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<ArrayList<MyAttendanceModel?>?>() {}.type)
                        attendanceList(attendanceListData)
                        // calledMethod(Constants.FOR_CLAIMS_SUMMARY)
                    } else {
                        UtilitiesWFMS.showToast(activity!!, jsonObject.getString("Message"))
                    }
                } catch (e: Exception) {
                    Log.e("Error", e.message)
                }
                calledMethod(CLAIM_SUMMARY_WFMS)
            } else if (TYPE == ConstantsWFMS.CLAIM_SUMMARY_WFMS) {
                try {
                    val jsonObject = JSONObject((response as Response<*>).body().toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        claimSummaryList = Gson().fromJson<ArrayList<com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<ArrayList<ClaimSummaryModel?>?>() {}.type)
                        setTheClaimSummary(claimSummaryList.get(0))
                    } else {
                        tvTotalClaimFragmentWFMS.setText(": 0.00")
                        tvApprovedClaimFragmentWFMS.setText(": 0.00")
                        tvAdvanceClaimFragmentWFMS.setText(":  0.00")
                        tvPendingClaimFragmentWFMS.setText(": 0.00")
                        var model = com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel()
                        model.advancePaid = "0.00"
                        model.approvedAmount = "0.00"
                        model.claimedAmount = "0.00"

                        claimSummaryList.add(0, model)
                    }
                } catch (e: Exception) {
                    Log.e("Error", e.message)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvRequestAdvanceClaim -> {
                startActivity(Intent(activity, AdvanceStatusActivityWFMS::class.java))

            }
            R.id.ivAddHeader -> {
                startActivity(Intent(activity, AddClaimActivityWFMS::class.java))

            }
        }
    }

}