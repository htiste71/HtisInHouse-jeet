package com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.marketing.adapter.TravelAdapter
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TravelModel
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService

import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Constants
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_travel.*
import org.json.JSONObject
import retrofit2.Response

class TravelFragment : Fragment(), MyInterface, View.OnClickListener {
    lateinit var adapter: TravelAdapter
    private var travelList = ArrayList<TravelModel>()
    var tinyDB: TinyDB? = null
    lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_travel, null)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)
        ivAddTravelFragment.setOnClickListener(this)
        rvTravelFragment.layoutManager = LinearLayoutManager(activity)

    }


    override fun sendResponse(response: Any, TYPE: Int) {
        Utilities.dismissDialog()


        if (TYPE == ConstantsWFMS.TRAVEL_SUBMIT_WFMS) {
            try {
                val jsonObject = JSONObject((response as Response<*>).body().toString())
                if (jsonObject.getString("Status").equals("Success")) {
                    val date = ConstantKotlin.getCurrentDate()

                    Utilities.showToast(activity, jsonObject.getString("Message"))
                    jsonObject.put("Empid", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                    jsonObject.put("TaskDate", date)
                    hitAPI(ConstantsWFMS.TRAVEL_LIST_WFMS, jsonObject.toString())
                    dialog.dismiss()
                } else {

                    Utilities.showToast(activity, jsonObject.getString("Message"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObject.getString("Status").equals("Success")) {
                travelList = Gson().fromJson<java.util.ArrayList<TravelModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<TravelModel>>() {

                }.type)
                tvNoTravelFragment.visibility = View.GONE
                rvTravelFragment.visibility = View.VISIBLE


                adapter = TravelAdapter(activity!!, travelList)
                rvTravelFragment.adapter = adapter


            } else {
                tvNoTravelFragment.visibility = View.VISIBLE
                rvTravelFragment.visibility = View.GONE
            }
        }

    }


    fun openDialog() {
        dialog = Dialog(activity!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_travel)
        dialog!!.setCancelable(false)
        dialog!!.show()
        val etFrom = dialog.findViewById<EditText>(R.id.etFromDialogTravel)
        val etTo = dialog.findViewById<EditText>(R.id.etToDialogTravel)
        val etRemarks = dialog.findViewById<EditText>(R.id.etRemarksDialogTravel)
        val btnSubmitDialogTravel = dialog.findViewById<Button>(R.id.btnSubmitDialogTravel)

        val btnClearDialogTravel = dialog.findViewById<Button>(R.id.btnClearDialogTravel)
        val tvtitlePop = dialog.findViewById(R.id.tv_title) as Ubuntu
        val ivBackPop = dialog.findViewById(R.id.ivBack) as ImageView
        val ivDrawerPop = dialog.findViewById(R.id.ivDrawer) as ImageView

        tvtitlePop.text = "Travel"
        ivDrawerPop.visibility = View.GONE
        ivBackPop.setOnClickListener {
            dialog.dismiss()
        }


        btnClearDialogTravel.setOnClickListener {
            etFrom.setText("")
            etTo.setText("")
            etRemarks.setText("")
        }
        btnSubmitDialogTravel.setOnClickListener {
            val mFrom = etFrom.text.toString()
            val mTo = etTo.text.toString()
            val mRemarks = etRemarks.text.toString()
            if (mFrom.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errFromLocation))
            } else if (mTo.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errToLocation))
            } else {


                val jsonObject = JSONObject()
                val date = ConstantKotlin.getCurrentDate()
                val time = ConstantKotlin.getCurrentTime24Hrs()
                val mAddress = Utilities.getAddressFromLatLong(activity, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())
                jsonObject.put("Empid", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                jsonObject.put("TaskDate", date)
                jsonObject.put("TaskTime", time)
                jsonObject.put("Latitude", OreoLocationService.LATITUDE)
                jsonObject.put("Longitude", OreoLocationService.LONGITUDE)
                jsonObject.put("Address", mAddress.get(0).getAddressLine(0)
                )
                jsonObject.put("FromLoc", mFrom)
                jsonObject.put("ToLoc", mTo)
                jsonObject.put("Remarks", mRemarks)

                hitAPI(ConstantsWFMS.TRAVEL_SUBMIT_WFMS, jsonObject.toString())


            }


        }


    }

    fun hitAPI(TYPE: Int, params: kotlin.String) {
        if (TYPE == ConstantsWFMS.TRAVEL_SUBMIT_WFMS) {
            ApiData.getData(params, ConstantsWFMS.TRAVEL_SUBMIT_WFMS, this, activity)

        } else {
            ApiData.getData(params, ConstantsWFMS.TRAVEL_LIST_WFMS, this, activity)

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
                    hitAPI(ConstantsWFMS.TRAVEL_LIST_WFMS, jsonObject.toString())
                }
            }, 200)
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onClick(v: View?) {
        openDialog()
    }


}