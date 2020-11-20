package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.BRANCH_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.FILTER_TYPE_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.BranchFilterAdapter
import com.htistelecom.htisinhouse.activity.WFMS.adapters.StatusFilterAdapter
import com.htistelecom.htisinhouse.activity.WFMS.models.BranchListModel
import com.htistelecom.htisinhouse.activity.WFMS.models.ThreeParameterModel
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.toolbar_filter.*
import org.json.JSONObject
import retrofit2.Response

class FilterActivity : Activity(), MyInterface, View.OnClickListener {
    companion object {

        var filterStatusList = ArrayList<ThreeParameterModel>()
        var lastPosition = -1
        var branchList = ArrayList<BranchListModel>()

    }

    var mStatusId = ""
    var mBranchId = ""

    lateinit var statusAdapter: StatusFilterAdapter
    lateinit var branchAdapter: BranchFilterAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        initViews()
        listeners()


    }

    private fun listeners() {
        tvApplyFilterToolbar.setOnClickListener(this)
        tvClearAllFilterToolbar.setOnClickListener(this)

    }

    private fun initViews() {
        rvStatusFilter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBranchFilter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        if (filterStatusList == null || filterStatusList.size == 0)
            hitAPI(FILTER_TYPE_WFMS, "")
        else {
            statusAdapter = StatusFilterAdapter(this)
            rvStatusFilter.adapter = statusAdapter

            branchAdapter = BranchFilterAdapter(this)
            rvBranchFilter.adapter = branchAdapter
        }

    }

    fun hitAPI(type: Int, params: String) {
        if (type == ConstantsWFMS.FILTER_TYPE_WFMS) {
            ApiData.getGetData(type, this, this)
        } else if (type == BRANCH_LIST_WFMS) {
            ApiData.getGetData(type, this, this)

        }

    }

    override fun onBackPressed() {
        backToHome()
    }

    private fun backToHome() {
        startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Team"))
        finish()
    }

    override fun sendResponse(response: Any?, TYPE: Int)
    {
        if (TYPE != FILTER_TYPE_WFMS)
            Utilities.dismissDialog()
        if (TYPE == FILTER_TYPE_WFMS)
        {
            val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                val jsonArray = jsonObj.getJSONArray("Output")



                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val model = ThreeParameterModel()
                    model.id = item.getString("StatusId")
                    model.name = item.getString("Status")
                    model.isChecked = false
                    filterStatusList.add(model)

                }

                statusAdapter = StatusFilterAdapter(this)
                rvStatusFilter.adapter = statusAdapter

                hitAPI(BRANCH_LIST_WFMS, "")


            }

        } else if (TYPE == BRANCH_LIST_WFMS) {

            val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {

                // branchList.clear()
                // val jsonArray=JSONArray(jsonObj.getString("Output"))
                branchList = Gson().fromJson<java.util.ArrayList<BranchListModel>>(jsonObj.getJSONArray("Output").toString(), object : TypeToken<ArrayList<BranchListModel>>() {

                }.type);

                branchAdapter = BranchFilterAdapter(this)
                rvBranchFilter.adapter = branchAdapter

            } else {

            }


        }
    }

    fun notifyMethod() {
        statusAdapter.notifyDataSetChanged()
    }

    fun notifyMethodBranch() {
        branchAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.tvApplyFilterToolbar) {

            mBranchId = branchAdapter.getBranchId()
            mStatusId = statusAdapter.getStatusId()
            val intent = Intent()
            intent.putExtra("status", mStatusId)
            intent.putExtra("branch", mBranchId)
            setResult(Activity.RESULT_OK, intent)
            finish()

        } else if (v!!.getId() == R.id.tvClearAllFilterToolbar) {
            for (i in 0 until filterStatusList.size) {
                if (filterStatusList.get(i).isChecked) {
                    filterStatusList.get(i).isChecked = false
                    break
                }

            }
            statusAdapter = StatusFilterAdapter(this)
            rvStatusFilter.adapter = statusAdapter

            for (i in 0 until branchList.size) {
                branchList.get(i).isChecked = false
            }
            branchAdapter = BranchFilterAdapter(this)
            rvBranchFilter.adapter = branchAdapter

            mBranchId = ""
            mStatusId = ""
        }
    }
}


