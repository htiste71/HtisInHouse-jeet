package com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.OnClick
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MARKETING_TASK_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.marketing.MarketingListModel
import com.htistelecom.htisinhouse.activity.WFMS.marketing.activity.MarketingActivity
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TaskModel
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TravelModel
import com.htistelecom.htisinhouse.adapter.MarketingTaskAdapter
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Constants
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_task.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class TaskFragment : Fragment(), MyInterface,View.OnClickListener {
    private var taskList=ArrayList<TaskModel>()
    var tinyDB: TinyDB? = null
    lateinit var adapter:MarketingTaskAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        return view
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)
        rvTaskFragment.layoutManager=LinearLayoutManager(activity)
        ivAddTaskFragment.setOnClickListener(this)

    }


    override fun sendResponse(response: Any, TYPE: Int) {
        Utilities.dismissDialog()
        try {
            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObject.getString("Status").equals("Success")) {
                taskList = Gson().fromJson<java.util.ArrayList<TaskModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<TaskModel>>() {

                }.type)

                rvTaskFragment.visibility=View.VISIBLE
                tvNoTaskFragment.visibility=View.GONE
                adapter=MarketingTaskAdapter(activity!!,taskList)
                rvTaskFragment.adapter=adapter
            }
            else{
                rvTaskFragment.visibility=View.GONE
                tvNoTaskFragment.visibility=View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser)
            Handler().postDelayed({
                if (activity != null) {
                    val date = ConstantKotlin.getCurrentDate()
                    val jsonObject = JSONObject()

                    jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                    jsonObject.put("TaskDate", date)
                    hitAPI(ConstantsWFMS.MARKETING_TASK_LIST_WFMS, jsonObject.toString())
                }
            }, 200)
        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun hitAPI(TYPE: Int, params: String) {
        if(TYPE==ConstantsWFMS.MARKETING_TASK_LIST_WFMS)
        {
            ApiData.getData(params,MARKETING_TASK_LIST_WFMS,this,activity)
        }

    }

    override fun onClick(v: View?) {
        startActivityForResult(Intent(activity, MarketingActivity::class.java),1)
    }
}