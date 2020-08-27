package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.FILTER_TYPE_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.StatusFilterAdapter
import com.htistelecom.htisinhouse.activity.WFMS.adapters.TerritoryFilterAdapter
import com.htistelecom.htisinhouse.activity.WFMS.models.ThreeParameterModel
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_filter.*
import org.json.JSONObject
import retrofit2.Response

class FilterActivity : Activity(), MyInterface {
    companion object {
        var filterStatusList = ArrayList<ThreeParameterModel>()
        var lastPosition = -1
    }

    lateinit var statusAdapter: StatusFilterAdapter
    lateinit var territoryAdapter: TerritoryFilterAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        initViews()






        territoryAdapter = TerritoryFilterAdapter(this)
        rvTerritoryFilter.adapter = territoryAdapter
    }

    private fun initViews() {
        rvStatusFilter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTerritoryFilter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        hitAPI(FILTER_TYPE_WFMS, "")
    }

    fun hitAPI(type: Int, params: String) {
        if (type == ConstantsWFMS.FILTER_TYPE_WFMS) {
            ApiData.getGetData(type, this, this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == FILTER_TYPE_WFMS) {
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
                statusAdapter = StatusFilterAdapter(this, filterStatusList)
                rvStatusFilter.adapter = statusAdapter
            }
        }
    }
}