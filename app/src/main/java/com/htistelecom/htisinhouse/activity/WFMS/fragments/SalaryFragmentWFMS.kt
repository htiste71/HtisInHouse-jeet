package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.SalaryAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.SalaryModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.retrofit.RetrofitAPI
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_salary_wfms.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SalaryFragmentWFMS : BaseFragment(), View.OnClickListener, MyInterface {
    private var salaryList = ArrayList<SalaryModel>()
    var calc: Calendar? = null
   lateinit var tinyDB: TinyDB
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
        rvSalary.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
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
        getMonth(monthYear)

        calledMethod()
    }

    fun backMonth() {
        val calc = Calendar.getInstance()
        curmonth -= 1
        calc.add(Calendar.MONTH, curmonth)
        val previousMonthYear = SimpleDateFormat("MM yyyy", Locale.getDefault()).format(calc.time)
        //display the leave data
        val month = SimpleDateFormat("MM", Locale.getDefault()).format(calc.time)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(calc.time).toInt()
        monthYear = month.toString() + year.toString()
        getMonth(monthYear)
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
        getMonth(monthYear)

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

        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin. logout(activity!!, tinyDB)
        } else {
            try {
                salaryList.clear()
                var isDFirst = true
                rvSalary.visibility = View.VISIBLE
                tvSalaryMsgSalaryFragmenmtWFMS.visibility = View.GONE
                val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObj.getString("Status").equals("Success")) {
                    val jsonArray = jsonObj.getJSONArray("Output")
                    val arrayLength = jsonArray.length()

                    for (record in 0 until jsonArray.length()) {
                        val jObject = jsonArray.getJSONObject(record)

                        if (jObject.getString("CTCType").equals("D")) {

                            if (isDFirst) {
                                isDFirst = false
                                val model = SalaryModel()

                                model.charges = "Allowances"
                                model.chargeAmount = jObject.getString("Allowance")
                                model.ctcType = "A"


                                salaryList.add(model)

                                val model1 = SalaryModel()

                                model1.charges = jObject.getString("Charges")

                                model1.chargeAmount = jObject.getString("ChargeAmount")
                                model1.ctcType = "D"

                                salaryList.add(model1)


                                if (record == arrayLength - 1) {
                                    val model = SalaryModel()

                                    model.charges = "Deductions"
                                    model.chargeAmount = jObject.getString("Deduction")
                                    model.ctcType = "D"

                                    salaryList.add(model)


                                    val model2 = SalaryModel()

                                    model2.charges = "Net Payable"

                                    model2.chargeAmount = jObject.getString("NetPayable")
                                    model2.ctcType = "D"

                                    salaryList.add(model2)
                                }

                            } else {
                                if (record == arrayLength - 1) {
                                    val model1 = SalaryModel()

                                    model1.charges = jObject.getString("Charges")
                                    model1.chargeAmount = jObject.getString("ChargeAmount")
                                    model1.ctcType = "D"

                                    salaryList.add(model1)


                                    val model = SalaryModel()

                                    model.charges = "Deductions"
                                    model.chargeAmount = jObject.getString("Deduction")
                                    model.ctcType = "D"

                                    salaryList.add(model)


                                    val model2 = SalaryModel()

                                    model2.charges = "Net Payable"

                                    model2.chargeAmount = jObject.getString("NetPayable")
                                    model2.ctcType = "D"

                                    salaryList.add(model2)
                                } else {
                                    val model = SalaryModel()

                                    model.charges = "Deductions"
                                    model.chargeAmount = jObject.getString("Deduction")
                                    model.ctcType = "D"

                                    salaryList.add(model)
                                }
                            }

                        } else {
                            val model = SalaryModel()

                            model.charges = jObject.getString("Charges")

                            model.chargeAmount = jObject.getString("ChargeAmount")
                            model.ctcType = "A"

                            salaryList.add(model)
                            if (record == arrayLength - 1) {
                                model.charges = "Allowances"
                                model.chargeAmount = jObject.getString("Allowance")
                                model.ctcType = "A"


                                salaryList.add(model)


                                val model2 = SalaryModel()

                                model2.charges = "Net Payable"

                                model2.chargeAmount = jObject.getString("NetPayable")
                                model2.ctcType = "A"

                                salaryList.add(model2)
                            }
                        }

                    }
                    rvSalary.adapter = SalaryAdapterWFMS(activity, salaryList)
                    //checkAllowanceDeduction(salaryList)
                } else {
                    rvSalary.visibility = View.GONE
                    tvSalaryMsgSalaryFragmenmtWFMS.setVisibility(View.VISIBLE)
                }
            } catch (e: Exception) {
                Log.e("Error", e.message)
            }

        }
    }

    private fun checkAllowanceDeduction(msalaryList: ArrayList<SalaryModel>) {

//        for (i in msalaryList.indices) {
//            if (msalaryList[i].getCTCType().equals("A",true) && msalaryList[i].getCharges().equals("BASIC",true)) {
//                //allowDataList.add(msalaryList.get(i));
//                tvBasicSalaryFragmenmtWFMS.setText(msalaryList[i].getChargeAmount().toString())
//            } else if (msalaryList[i].getCTCType().equals("A",true) && msalaryList[i].getCharges().equals("HRA",true)) {
//                // deductDataList.add(msalaryList.get(i));
//                tvHraSalaryFragmenmtWFMS.setText(msalaryList[i].getChargeAmount().toString())
//            } else if (msalaryList[i].getCTCType().equals("A",true) && msalaryList[i].getCharges().equals("CONVEYANCE",true)) {
//                tvConveyanceSalaryFragmenmtWFMS.setText(msalaryList[i].getChargeAmount().toString())
//            } else if (msalaryList[i].getCTCType().equals("A",true) && msalaryList[i].getCharges().equals("MEDICAL",true)) {
//                tvMedicalDeductionSalaryFragmenmtWFMS.setText(msalaryList[i].getChargeAmount().toString())
//            } else if (msalaryList[i].getCTCType().equals("A",true) && msalaryList[i].getCharges().equals("SPECIAL",true)) {
//                tvSpecialSalaryFragmenmtWFMS.setText(msalaryList[i].getChargeAmount().toString())
//            } else if (msalaryList[i].getCTCType().equals("D",true) && msalaryList[i].getCharges().equals("LWF",true)) {
//                tvLwfSalaryFragmenmtWFMS.setText(msalaryList[i].getChargeAmount().toString())
//            } else if (msalaryList[i].getCTCType().equals("D",true) && msalaryList[i].getCharges().equals("MEDICAL",true)) {
//                tvMedicalSalaryFragmenmtWFMS.setText(msalaryList[i].getChargeAmount().toString())
//            } else if (msalaryList[i].getCTCType().equals("D",true) && msalaryList[i].getCharges().equals("DONATE",true)) {
//                tvDonateSalaryFragmenmtWFMS.setText(msalaryList[i].getChargeAmount().toString())
//            }
//        }
//        tvGrossPaySalaryFragmenmtWFMS.setText(msalaryList[0].getAllowance().toString())
//        tvGrossPaySalaryFragmenmtWFMS.setText(msalaryList[0].getDeduction().toString())
//        tvNetPaySalaryFragmenmtWFMS.setText(msalaryList[0].getNetPayable().toString())
    }

    fun getMonth(date: String) {
        val dateFormat = SimpleDateFormat("MMM yyyy")
        val oldDateFormat = SimpleDateFormat("MMyyyy")
        val dOldFormat = oldDateFormat.parse(date)
        val sDate = dateFormat.format(dOldFormat)
        tvCurrentMonthSalaryFragmenmtWFMS.setText(sDate)


    }
}