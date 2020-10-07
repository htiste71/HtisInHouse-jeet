package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MY_TEAM_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.FilterActivity
import com.htistelecom.htisinhouse.activity.WFMS.activity.TasksWeekDetailActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.TeamMapActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.TeamAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_team_wfms.*
import kotlinx.android.synthetic.main.nav_header_main_activity_navigation.*
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

class TeamFragmentWFMS : Fragment(), MyInterface, View.OnClickListener {

    lateinit var model: MyTeamModel
    private var ivProfileHeader: ImageView? = null
    private var myTeamList = ArrayList<MyTeamModel>()
    lateinit var teamAdapter: TeamAdapterWFMS
    lateinit var tinyDB: TinyDB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team_wfms, null)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()


    }

    private fun listeners() {
        llFilterFragmentTeamWFMS.setOnClickListener(this)
        llMapFragmentTeamWFMS.setOnClickListener(this)
        ivProfileHeader!!.setOnClickListener(this)
    }

    private fun initViews() {
        ivProfileHeader = activity!!.findViewById<ImageView>(R.id.ivMyProfileHeader)
        tinyDB = TinyDB(activity)
        rvTeamFragmentWFMS.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
        Glide.with(activity!!).load(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE)).into(ivProfileHeader!!);


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.llFilterFragmentTeamWFMS -> {
                val intent = Intent(activity, FilterActivity::class.java)
                startActivity(intent)
            }
            R.id.llMapFragmentTeamWFMS -> {
                val intent = Intent(activity, TeamMapActivityWFMS::class.java)
                intent.putExtra("Data", myTeamList)
                startActivity(intent)
            }
            R.id.ivMyProfileHeader -> {
                val intent = Intent(activity, TasksWeekDetailActivityWFMS::class.java)
                intent.putExtra("data", model)
                intent.putExtra("UserProfile", true)
                activity!!.startActivity(intent)
            }
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
            try {
                val json = JSONObject((response as Response<*>).body()!!.toString())
                if (json.getString("Status").equals("Success")) {

                    myTeamList = Gson().fromJson<java.util.ArrayList<MyTeamModel>>(json.getJSONArray("Output").toString(), object : TypeToken<ArrayList<MyTeamModel>>() {
                    }.type)


                    if (myTeamList.size == 1) {
                        rvTeamFragmentWFMS.visibility = View.GONE
                        tvNoTeamMemberFoundTeamFragmentWFMS.visibility = View.VISIBLE
                        for (i in 0 until myTeamList.size) {
                            if (myTeamList.get(i).empId == tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID)) {
                                model = myTeamList.get(i)
                                myTeamList.remove(model)
                                break

                            }
                        }
                    } else {
                        rvTeamFragmentWFMS.visibility = View.VISIBLE
                        tvNoTeamMemberFoundTeamFragmentWFMS.visibility = View.GONE
                        for (i in 0 until myTeamList.size) {
                            if (myTeamList.get(i).empId == tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID)) {
                                model = myTeamList.get(i)
                                myTeamList.remove(model)
                                break

                            }
                        }
                        teamAdapter = TeamAdapterWFMS(activity, myTeamList, tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
                        rvTeamFragmentWFMS.adapter = teamAdapter
                    }

                } else {
                    rvTeamFragmentWFMS.visibility = View.GONE
                    tvNoTeamMemberFoundTeamFragmentWFMS.visibility = View.VISIBLE
                }
            } catch (e: Exception) {

            }

        } else {

        }
    }


}