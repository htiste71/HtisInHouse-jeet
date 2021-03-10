package com.htistelecom.htisinhouse.activity.WFMS.claims

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_DETAIL_DATE_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.MainActivityNavigation
import com.htistelecom.htisinhouse.activity.WFMS.claims.adapters.TaskCompletedAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel
import com.htistelecom.htisinhouse.activity.WFMS.models.CompletedTaskModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_task_completed_activity_wfms.*
import kotlinx.android.synthetic.main.layout_claim_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskCompletedActivityWFMS : AppCompatActivity(), View.OnClickListener, MyInterface {
    private var completedTaskList = ArrayList<CompletedTaskModel>()
    lateinit var tinyDB: TinyDB
    var mDate: String = ""
    lateinit var model: ClaimSummaryModel
    lateinit var cal: Calendar
    private var formatter: SimpleDateFormat? = null
    var startDate: String? = null
    var endDate: kotlin.String? = null
    var month = 0
    var year: Int = 0
    lateinit var calc: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_completed_activity_wfms)

    }

    override fun onResume() {
        super.onResume()
        initViews()
        clickListeners()
        api()
    }

    private fun clickListeners() {
        ivBack.setOnClickListener(this)
    }

    private fun initViews() {
        //set the title

        tv_title.setText(getString(R.string.myClaim))
        ivDrawer.visibility = View.GONE
        tinyDB = TinyDB(this)
        mDate = intent.getStringExtra("date")
        model = intent.getSerializableExtra("data") as ClaimSummaryModel
        try {
            tvTaskDateTaskCompletedActivityWFMS.text = "Task Date:" + mDate
            tvTotalClaimFragmentWFMS.text = ":" + model.claimedAmount
            tvApprovedClaimFragmentWFMS.text = ": " + model.approvedAmount
            tvAdvanceClaimFragmentWFMS.text = ": " + model.advancePaid
            var mPending = model.claimedAmount.toDouble() - model.approvedAmount.toDouble() - model.rejectedAmount.toDouble()

            tvPendingClaimFragmentWFMS.text = ": " + mPending
        } catch (e: Exception) {

        }



        rvDescriptionClaimDetailActivityWFMS.layoutManager = LinearLayoutManager(this, VERTICAL, false)
    }

    private fun api() {

        var json = JSONObject()


        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        json.put("WorkDate", mDate)
        callAPI(ConstantsWFMS.CLAIM_DETAIL_DATE_WFMS, json.toString())
    }

    private fun callAPI(TYPE: Int, params: String) {
        if (TYPE == ConstantsWFMS.CLAIM_DETAIL_DATE_WFMS) {
            ApiData.getData(params, ConstantsWFMS.CLAIM_DETAIL_DATE_WFMS, this, this)

        }
        else if (TYPE == ConstantsWFMS.CLAIM_SUMMARY_WFMS) {
            try {
                calc = Calendar.getInstance()
                month = Calendar.getInstance()[Calendar.MONTH] + 1
                year = Calendar.getInstance()[Calendar.YEAR]
                formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                calc.set(year, month - 1, 1)
                endDate = calc.getActualMaximum(Calendar.DAY_OF_MONTH).toString() + "-" + Utilities.getMonthFormat(month) + "-" + year
                startDate = "01-" + Utilities.getMonthFormat(month) + "-" + year
                val jsonObject = JSONObject()
                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                jsonObject.put("FromDate", startDate)
                jsonObject.put("ToDate", endDate)
                ApiData.getData(jsonObject.toString(), ConstantsWFMS.CLAIM_SUMMARY_WFMS, this, this)
            } catch (e: Exception) {
                Log.e("Error", e.message)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivBack -> {
                backToHome()
            }
            R.id.ivDrawer -> {

            }
        }
    }

    fun backToHome() {
        startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Claim"))
        finish()
    }

    override fun sendResponse(response: Any?, TYPE: Int) {

        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            finish()
            ConstantKotlin.logout(this, tinyDB)
        } else {
            Utilities.dismissDialog()
            if (TYPE == CLAIM_DETAIL_DATE_WFMS) {
                var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObj.getString("Status").equals("Success"))
                {
                    rvDescriptionClaimDetailActivityWFMS.visibility = View.VISIBLE
                    tvNoTaskClaimDetailActivityWFMS.visibility = View.GONE

                    completedTaskList = Gson().fromJson<java.util.ArrayList<CompletedTaskModel>>(jsonObj.getJSONArray("Output").toString(), object : TypeToken<ArrayList<CompletedTaskModel>>() {}.type)
                    rvDescriptionClaimDetailActivityWFMS.adapter = TaskCompletedAdapterWFMS(this, completedTaskList, model)


                } else {
                    rvDescriptionClaimDetailActivityWFMS.visibility = View.GONE
                    tvNoTaskClaimDetailActivityWFMS.visibility = View.VISIBLE
                }
                callAPI(ConstantsWFMS.CLAIM_SUMMARY_WFMS,"")
            }
            else if (TYPE == ConstantsWFMS.CLAIM_SUMMARY_WFMS) {
                try {
                    val jsonObject = JSONObject((response as Response<*>).body().toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                       val claimSummaryList = Gson().fromJson<ArrayList<com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<ArrayList<ClaimSummaryModel?>?>() {}.type)
                       val model=claimSummaryList.get(0)
                        try {
                            tvTotalClaimFragmentWFMS.text = ":" + model.claimedAmount
                            tvApprovedClaimFragmentWFMS.text = ": " + model.approvedAmount
                            tvAdvanceClaimFragmentWFMS.text = ": " + model.advancePaid
                            var mPending = model.claimedAmount.toDouble() - model.approvedAmount.toDouble() - model.rejectedAmount.toDouble()

                            tvPendingClaimFragmentWFMS.text = ": " + mPending
                        } catch (e: Exception) {

                        }
                    } else {
                        tvTotalClaimFragmentWFMS.setText(": 0.00")
                        tvApprovedClaimFragmentWFMS.setText(": 0.00")
                        tvAdvanceClaimFragmentWFMS.setText(":  0.00")
                        tvPendingClaimFragmentWFMS.setText(": 0.00")
                        var model = com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel()
                        model.advancePaid = "0.00"
                        model.approvedAmount = "0.00"
                        model.claimedAmount = "0.00"


                    }
                } catch (e: Exception) {
                    Log.e("Error", e.message)
                }
            }
        }
    }

    override fun onBackPressed() {
        backToHome()
    }
}