package com.htistelecom.htisinhouse.activity.WFMS.claims

import android.content.Intent
import android.os.Bundle
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
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_task_completed_activity_wfms.*
import kotlinx.android.synthetic.main.layout_claim_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import retrofit2.Response

class TaskCompletedActivityWFMS : AppCompatActivity(), View.OnClickListener, MyInterface {
    private var completedTaskList = ArrayList<CompletedTaskModel>()
    lateinit var tinyDB: TinyDB
    var mDate: String = ""
    lateinit var model: ClaimSummaryModel
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
        Utilities.dismissDialog()
        if (TYPE == CLAIM_DETAIL_DATE_WFMS) {
            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                rvDescriptionClaimDetailActivityWFMS.visibility = View.VISIBLE
                tvNoTaskClaimDetailActivityWFMS.visibility = View.GONE

                completedTaskList = Gson().fromJson<java.util.ArrayList<CompletedTaskModel>>(jsonObj.getJSONArray("Output").toString(), object : TypeToken<ArrayList<CompletedTaskModel>>() {}.type)
                rvDescriptionClaimDetailActivityWFMS.adapter = TaskCompletedAdapterWFMS(this, completedTaskList, model)


            } else {
                rvDescriptionClaimDetailActivityWFMS.visibility = View.GONE
                tvNoTaskClaimDetailActivityWFMS.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        backToHome()
    }
}