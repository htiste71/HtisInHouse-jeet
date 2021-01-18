package com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.OFFICE_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.OFFICE_SUBMIT_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.OfficeModel
import com.htistelecom.htisinhouse.activity.WFMS.models.LeaveTypeDayModel
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.adapter.OfficeAdapter
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Constants
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_office.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.String

class OfficeFragment : Fragment(), MyInterface, View.OnClickListener {
    private var officeList= ArrayList<OfficeModel>()
    var tinyDB: TinyDB? = null
    lateinit var adapter:OfficeAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_office, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)
        rvOfficeFragment.layoutManager=LinearLayoutManager(activity)
        btnSubmitOfficeFragment.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnSubmitOfficeFragment ->
                marketingPersonInOffice()
        }
    }

    private fun marketingPersonInOffice() {

        val date = ConstantKotlin.getCurrentDate()
        val time = ConstantKotlin.getCurrentTime24Hrs()


        try {
            val jsonObject = JSONObject()
            jsonObject.put("Empid", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
            jsonObject.put("Latitude", String.valueOf(OreoLocationService.LATITUDE))
            jsonObject.put("Longitude", String.valueOf(OreoLocationService.LONGITUDE))
            val mAddress = Utilities.getAddressFromLatLong(activity, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())

            jsonObject.put("TaskDate", date)
            jsonObject.put("TaskTime", time)
            jsonObject.put("Address", mAddress.get(0).getAddressLine(0))


            hitAPI(OFFICE_SUBMIT_WFMS, jsonObject.toString())


        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    fun hitAPI(TYPE: Int, params: kotlin.String) {
        if (TYPE == OFFICE_SUBMIT_WFMS) {
            ApiData.getData(params, ConstantsWFMS.OFFICE_SUBMIT_WFMS, this, activity)

        } else {
            ApiData.getData(params, ConstantsWFMS.OFFICE_LIST_WFMS, this, activity)

        }
    }


    override fun sendResponse(response: Any, TYPE: Int) {
        Utilities.dismissDialog()


        if (TYPE == OFFICE_SUBMIT_WFMS) {
            try {
                val jsonObject = JSONObject((response as Response<*>).body().toString())
                if (jsonObject.getString("Status").equals("Success")) {
                    Utilities.showToast(activity, jsonObject.getString("Message"))
                    val date = ConstantKotlin.getCurrentDate()
                    val jsonObject = JSONObject()

                    jsonObject.put("Empid", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                    jsonObject.put("TaskDate", date)
                    hitAPI(OFFICE_LIST_WFMS, jsonObject.toString())
                } else {
                    Utilities.showToast(activity, jsonObject.getString("Message"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObject.getString("Status").equals("Success")) {
                officeList = Gson().fromJson<java.util.ArrayList<OfficeModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<OfficeModel>>() {

                }.type)
                adapter=OfficeAdapter(activity!!,officeList)
                rvOfficeFragment.adapter=adapter



            }
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser)
            Handler().postDelayed({
                if (activity != null) {
                    val date = ConstantKotlin.getCurrentDate()
                    val jsonObject = JSONObject()

                    jsonObject.put("Empid", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                    jsonObject.put("TaskDate", date)
                    hitAPI(OFFICE_LIST_WFMS, jsonObject.toString())
                }
            }, 200)
        super.setUserVisibleHint(isVisibleToUser)
    }

}