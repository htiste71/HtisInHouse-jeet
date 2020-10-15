package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantKotlin
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.LeaveTypeModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_leave_detail_wfms.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import retrofit2.Response

class LeaveDetailActivityWFMS : AppCompatActivity(), MyInterface, View.OnClickListener {
    lateinit var activity: MainActivityNavigation
    private var leaveTypeList = ArrayList<LeaveTypeModel>()
    lateinit var leaveTypeArray: Array<String?>
    lateinit var leaveTypeAdapter: ArrayAdapter<String?>
    lateinit var tinyDB: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_detail_wfms)
        initViews()
        listeners()

    }

    private fun listeners() {
        ivBack.setOnClickListener(this)
        btnApplyLeaveDetail.setOnClickListener(this)
    }

    private fun initViews() {
        tinyDB = TinyDB(this)
        tv_title.text = "Leave Details"
        ivDrawer.visibility = View.GONE


        val jsonObj = JSONObject()
        jsonObj.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        hitAPI(ConstantsWFMS.LEAVE_TYPE_WFMS, jsonObj.toString())
    }

    private fun hitAPI(TYPE: Int, params: String) {
        if (TYPE == ConstantsWFMS.LEAVE_TYPE_WFMS) {
            ApiData.getData(params, ConstantsWFMS.LEAVE_TYPE_WFMS, this, this)

        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == ConstantsWFMS.LEAVE_TYPE_WFMS) {
            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObject.getString("Status").equals("Success")) {
                leaveTypeList = Gson().fromJson<java.util.ArrayList<LeaveTypeModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<LeaveTypeModel>>() {

                }.type)
                leaveTypeArray = arrayOfNulls<String>(leaveTypeList.size)
                for (i in leaveTypeList.indices) {
                    //Storing names to string array
                    leaveTypeArray[i] = leaveTypeList.get(i).leaveTypeName
                }
            } else {
                Utilities.showToast(this, "No Details found.")
            }
            leaveTypeAdapter = ArrayAdapter<String?>(this, R.layout.spinner_item, leaveTypeArray)
            spnrLeaveTypeLeaveDetail.setAdapter(leaveTypeAdapter)


            spnrLeaveTypeLeaveDetail!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val model = leaveTypeList.get(position)
                    tvBalanceLeaveDetail.text = model.totalBalance
                    tvUnderApprovalLeaveDetail.text = model.unApproved
                    tvAvailedLeaveDetail.text = model.availed
                    tvCurrentBalanceLeaveDetail.text = model.currentBalance


                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            })


        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivBack -> {
                backToHome()
            }
            R.id.btnApplyLeaveDetail -> {
                startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "OD"))
                finish()


            }
        }
    }

    private fun backToHome() {
        startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Attendance"))
        finish()

    }

    override fun onBackPressed() {
        backToHome()
    }
}