package com.htistelecom.htisinhouse.activity.WFMS.add_project


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_add_activity.*
import org.json.JSONObject
import retrofit2.Response

class AddActivityFragment:Fragment(), View.OnClickListener, MyInterface {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_activity,null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnSubmitAddActivityFragment.setOnClickListener(this)
    }


    override fun onClick(v: View?) {

        if(etAddActivityFragment.text.toString().equals(""))
        {
            UtilitiesWFMS.showToast(activity!!,activity!!.getString(R.string.errEnterActivity))
        }
        else{
            val jsonObject = JSONObject()

            jsonObject.put("TaskId", "0")
            jsonObject.put("ActivityName",etAddActivityFragment.text.toString())

            ApiData.getData(jsonObject.toString(),ConstantsWFMS.SUBMIT_ACTIVITY_WFMS,this,activity)


        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        val json = JSONObject((response as Response<*>).body()!!.toString())
        if (json.getString("Status").equals("Success")) {
            Utilities.showToast(activity, json.getString("Message"))
            etAddActivityFragment.setText("")
        }
        else
        {
            Utilities.showToast(activity, json.getString("Message"))

        }

    }
}