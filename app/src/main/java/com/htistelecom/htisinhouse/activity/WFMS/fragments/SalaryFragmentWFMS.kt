package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.retrofit.RetrofitAPI
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_salary_wfms.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SalaryFragmentWFMS : BaseFragment(), View.OnClickListener, MyInterface {
    var calc: Calendar? = null
    var tinyDB: TinyDB? = null
    var currentMonth: String = ""
    var monthYear: kotlin.String = ""
    var from = 0
    var curmonth: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_salary_wfms, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()
    }

    private fun listeners() {
        ivPreviousMonthSalaryFragmentWFMS.setOnClickListener(this)
        ivNextMonthSalaryFragmentWFMS.setOnClickListener(this)
    }

    private fun initViews() {
        calc = Calendar.getInstance() // Get today

        tinyDB = TinyDB(activity)
    }

    override fun onClick(v: View) {
        when (v!!.id) {
            R.id.ivPreviousMonthSalaryFragmentWFMS -> {
                backMonth()
            }
            R.id.ivNextMonthSalaryFragmentWFMS -> {
                nextMonth()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val month = SimpleDateFormat("MM", Locale.getDefault()).format(calc!!.time)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(calc!!.time).toInt()
        monthYear = month.toString() + year.toString()
        calledMethod()
    }

    fun backMonth() {
        val calc = Calendar.getInstance()
        curmonth -= 1
        calc.add(Calendar.MONTH, curmonth)
        val previousMonthYear = SimpleDateFormat("MM yyyy", Locale.getDefault()).format(calc.time)
        tvCurrentMonthSalaryFragmenmtWFMS.setText(previousMonthYear)
        //display the leave data
        val month = SimpleDateFormat("MM", Locale.getDefault()).format(calc.time)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(calc.time).toInt()
        monthYear = month.toString() + year.toString()
        //getSalaryDetails(MonthYear);
        calledMethod()
    }

    fun nextMonth() {
        val calc = Calendar.getInstance()
        curmonth += 1
        calc.add(Calendar.MONTH, curmonth)
        val nextMonthYear = SimpleDateFormat("MM yyyy", Locale.getDefault()).format(calc.time)
        tvCurrentMonthSalaryFragmenmtWFMS.setText(nextMonthYear)
        //display the leave data
        val month = SimpleDateFormat("MM", Locale.getDefault()).format(calc.time)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(calc.time).toInt()
        monthYear = month.toString() + year.toString()
        // getSalaryDetails(MonthYear);
        calledMethod()
    }

    fun calledMethod() {
        try {
            val json = JSONObject()
            json.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
            json.put("MonthYear", monthYear)
            RetrofitAPI.callAPI(json.toString(), ConstantsWFMS.SALARY_SLIP_WFMS, this)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()



    }
}