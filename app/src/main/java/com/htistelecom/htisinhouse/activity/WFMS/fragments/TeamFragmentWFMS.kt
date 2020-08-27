package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MY_TEAM_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.FilterActivity
import com.htistelecom.htisinhouse.activity.WFMS.activity.TeamMapActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.TeamAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_team_wfms.*
import org.json.JSONObject
import retrofit2.Response

class TeamFragmentWFMS : Fragment(), MyInterface {

    private var myTeamList = ArrayList<MyTeamModel>()
    lateinit var teamAdapter: TeamAdapterWFMS
    lateinit var tinyDB: TinyDB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team_wfms, null)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)
        rvTeamFragmentWFMS.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
        llFilterFragmentTeamWFMS.setOnClickListener { view ->
            val intent = Intent(activity, FilterActivity::class.java)
            startActivity(intent)

        }
        llMapFragmentTeamWFMS.setOnClickListener { view ->
            val intent = Intent(activity, TeamMapActivityWFMS::class.java)
            intent.putExtra("Data", myTeamList)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        val json = JSONObject()
        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        hitAPI(json.toString(), ConstantsWFMS.MY_TEAM_WFMS)
    }

    private fun hitAPI(params: String, TYPE: Int) {
        when (TYPE) {
            MY_TEAM_WFMS -> {
                ApiData.getData(params, MY_TEAM_WFMS, this, activity!!)
            }
        }

    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == MY_TEAM_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                rvTeamFragmentWFMS.visibility = View.VISIBLE
                tvNoTeamMemberFoundTeamFragmentWFMS.visibility = View.GONE
                myTeamList = Gson().fromJson<java.util.ArrayList<MyTeamModel>>(json.getJSONArray("Output").toString(), object : TypeToken<ArrayList<MyTeamModel>>() {
                }.type);
                teamAdapter = TeamAdapterWFMS(activity, myTeamList)
                rvTeamFragmentWFMS.adapter = teamAdapter
            } else {
                rvTeamFragmentWFMS.visibility = View.GONE
                tvNoTeamMemberFoundTeamFragmentWFMS.visibility = View.VISIBLE
            }
        } else {

        }
    }

}