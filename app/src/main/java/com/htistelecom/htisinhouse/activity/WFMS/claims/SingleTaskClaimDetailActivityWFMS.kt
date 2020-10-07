package com.htistelecom.htisinhouse.activity.WFMS.claims

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_DETAIL_TASK_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.BaseActivity
import com.htistelecom.htisinhouse.activity.WFMS.activity.MainActivityNavigation
import com.htistelecom.htisinhouse.activity.WFMS.claims.adapters.SingleTaskClaimDetailAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel
import com.htistelecom.htisinhouse.activity.WFMS.claims.models.SingleTaskDetailModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_single_task_claim_detail_wfms.*
import kotlinx.android.synthetic.main.layout_claim_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import retrofit2.Response

class SingleTaskClaimDetailActivityWFMS : BaseActivity(), View.OnClickListener, MyInterface {
    var mTaskId = ""
    var mDate = ""
    lateinit var summaryModel: ClaimSummaryModel
    lateinit var tinyDB: TinyDB
    var taskDetailList = ArrayList<SingleTaskDetailModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_task_claim_detail_wfms)
        initViews()
        clickListeners()
        api()
    }

    private fun api() {

        var json = JSONObject()


        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        json.put("WorkDate", mDate)
        json.put("Role", "0")
        json.put("TaskId", mTaskId)

        hitAPI(ConstantsWFMS.CLAIM_DETAIL_TASK_WFMS, json.toString())
    }

    private fun hitAPI(TYPE: Int, params: String) {
        if (TYPE == CLAIM_DETAIL_TASK_WFMS) {
            ApiData.getData(params, CLAIM_DETAIL_TASK_WFMS, this, this)
        }
    }

    private fun clickListeners() {
        ivBack.setOnClickListener(this)
    }

    private fun initViews() {
        tinyDB = TinyDB(this)
        tv_title.setText(getString(R.string.strClaims))
        ivDrawer.visibility = View.GONE
        mTaskId = intent.getStringExtra("TaskId")
        mDate = intent.getStringExtra("Date")
        summaryModel = intent.getSerializableExtra("data") as ClaimSummaryModel
        tvTaskDateSingleTaskClaimDetailActivityWFMS.text = "Task Date - " + mDate
        try {
            tvTotalClaimFragmentWFMS.text = ":" + summaryModel.claimedAmount
            tvApprovedClaimFragmentWFMS.text = ": " + summaryModel.approvedAmount
            tvAdvanceClaimFragmentWFMS.text = ": " + summaryModel.advancePaid
            var mPending = summaryModel.claimedAmount.toDouble() - summaryModel.approvedAmount.toDouble() - summaryModel.rejectedAmount.toDouble()

            tvPendingClaimFragmentWFMS.text = ": " + mPending
        } catch (e: Exception) {

        }






        rvSingleTaskClaimDetailActivityWFMS.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivBack ->
                backToHome()

        }
    }

    override fun onBackPressed() {
        backToHome()
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == CLAIM_DETAIL_TASK_WFMS) {
            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                taskDetailList.clear()
                tvNoClaimAddedSingleTaskClaimDetailActivityWFMS.visibility = View.GONE
                rvSingleTaskClaimDetailActivityWFMS.visibility = View.VISIBLE
                taskDetailList = Gson().fromJson<java.util.ArrayList<SingleTaskDetailModel>>(jsonObj.getJSONArray("Output").toString(), object : TypeToken<ArrayList<SingleTaskDetailModel>>() {}.type)

                rvSingleTaskClaimDetailActivityWFMS.adapter = SingleTaskClaimDetailAdapterWFMS(this, taskDetailList)
            } else {
                tvNoClaimAddedSingleTaskClaimDetailActivityWFMS.visibility = View.VISIBLE
                rvSingleTaskClaimDetailActivityWFMS.visibility = View.GONE
            }


        }
    }

    fun backToHome() {
        startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Claim"))
        finish()
    }
}