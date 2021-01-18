package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.SEARCH_TEAM_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.FilterActivity
import com.htistelecom.htisinhouse.activity.WFMS.activity.TasksWeekDetailActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.TeamMapActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.TeamAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_team_wfms.*
import org.json.JSONObject
import retrofit2.Response


class TeamFragmentWFMS : Fragment(), MyInterface, View.OnClickListener {

    lateinit var model: MyTeamModel
    private var ivProfileHeader: ImageView? = null
    private var myTeamList = ArrayList<MyTeamModel>()
    lateinit var teamAdapter: TeamAdapterWFMS
    lateinit var tinyDB: TinyDB
    var mStatusId = ""
    var mBranchId = ""
    var mSearchText = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team_wfms, null)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()

        FilterActivity.filterStatusList
    }

    private fun listeners() {
        llFilterFragmentTeamWFMS.setOnClickListener(this)
        llMapFragmentTeamWFMS.setOnClickListener(this)
        ivProfileHeader!!.setOnClickListener(this)
        etSearchFragmentTeamWFMS.addTextChangedListener(
                object : TextWatcher {
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        handler.removeCallbacks(input_finish_checker);

                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                    override fun afterTextChanged(s: Editable) {
                        if (s.length > 0) {
                            last_text_edit = System.currentTimeMillis();
                            handler.postDelayed(input_finish_checker, delay);
                            mSearchText = s.toString()
                        } else {
                            last_text_edit = System.currentTimeMillis();
                            handler.postDelayed(input_finish_checker, delay);
                            mSearchText = ""
                        }
                        //  searchMethod(s.toString())

                    }
                }
        )
    }

    private fun searchMethod(text: String) {
//         var timer: Timer = Timer()
//         val DELAY: Long = 1000 // milliseconds
//        timer.cancel()
//        timer = Timer()
//        timer.schedule(
//                object : TimerTask() {
//                    override fun run() {
//
//                        (activity as Activity).runOnUiThread {
//                            mSearchText = text
//                            //mSearchText = "sho"
//                            Utilities.showToast(activity!!, mSearchText)
//                            callMethod()                        }
//
//
//
//
//
//                    }
//                },
//                DELAY
//        )


        Handler(Looper.getMainLooper()).postDelayed({
            //Do something after 100ms
            mSearchText = text
            //mSearchText = "sho"
            //   Utilities.showToast(activity!!, mSearchText)
            callMethod()
        }, 1000)

    }

    var delay: Long = 1000 // 1 seconds after user stops typing

    var last_text_edit: Long = 0
    var handler = Handler()

    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > last_text_edit + delay - 500) {
            //  mSearchText = text
            //mSearchText = "sho"
            //  Utilities.showToast(activity!!, mSearchText)
            callMethod()
        }
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
                startActivityForResult(intent, 1)

                //
                //  startActivity(intent)
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
        callMethod()

    }


    private fun hitAPI(params: String, TYPE: Int) {
        when (TYPE) {
            SEARCH_TEAM_LIST_WFMS -> {
                ApiData.getData(params, SEARCH_TEAM_LIST_WFMS, this, activity!!)
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

            if (TYPE == SEARCH_TEAM_LIST_WFMS) {
                try {
                    val json = JSONObject((response as Response<*>).body()!!.toString())
                    if (json.getString("Status").equals("Success")) {

                        myTeamList = Gson().fromJson<java.util.ArrayList<MyTeamModel>>(json.getJSONArray("Output").toString(), object : TypeToken<ArrayList<MyTeamModel>>() {
                        }.type)


                        if (myTeamList.size == 1 && myTeamList.get(0).empId.equals(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            mStatusId = data!!.getStringExtra("status")
            mBranchId = data!!.getStringExtra("branch")
        }
    }

    private fun callMethod() {

        val json = JSONObject()
        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        json.put("SearchText", mSearchText)
        json.put("AvailabilityStatus", mStatusId)
        json.put("Branch", mBranchId)

        hitAPI(json.toString(), ConstantsWFMS.SEARCH_TEAM_LIST_WFMS)


    }

}