package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_feedback_wfms.*
import org.json.JSONObject
import retrofit2.Response

class FeedbackFragmentWFMS : BaseFragment(), MyInterface {
    lateinit var tinyDB: TinyDB
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feedback_wfms, null)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)
        btnFeedbackFeedbackFragmentWFMS.setOnClickListener { view ->
            val mFeedback = etFeedbackFragmentFeedbackWFMS.text.toString()
            if (mFeedback.equals("")) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errFeedback))
            } else {
                val jsonObject = JSONObject()
                jsonObject.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
                jsonObject.put("FeedbackforId", "1")
                jsonObject.put("TypeId", "2")
                jsonObject.put("Feedback", mFeedback)
                ApiData.getData(jsonObject.toString(), ConstantsWFMS.USER_FEEDBACK_WFMS, this, activity!!)
            }

        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()

        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(activity!!, tinyDB)
        } else {
            val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                UtilitiesWFMS.showToast(activity!!, jsonObj.getString("Message"))
                etFeedbackFragmentFeedbackWFMS.setText("")

            }
        }
    }
}