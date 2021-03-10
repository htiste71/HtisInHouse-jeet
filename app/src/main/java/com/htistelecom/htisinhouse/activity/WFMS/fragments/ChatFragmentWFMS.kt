package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.ChatAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_chat_wfms.*
import org.json.JSONObject
import retrofit2.Response

class ChatFragmentWFMS : Fragment(), MyInterface {
    lateinit var tinyDB: TinyDB
    private var myTeamList = ArrayList<MyTeamModel>()
    lateinit var model: MyTeamModel
    lateinit var adapter: ChatAdapterWFMS

    //lateinit var adapter:ChatAdapterWFMS
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_wfms, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)

        rvChatFragmentWFMS.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
        callMethod()
        if (MyApplication.mSocket!!.connected()) {
            MyApplication.mSocket!!.emit("user_connected", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_NAME))
        }
        MyApplication.mSocket!!.connect()

    }

    override fun onResume() {
        super.onResume()

    }

    private fun callMethod() {

        val json = JSONObject()
        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_MANAGER_ID))
        json.put("SearchText", "")
        json.put("AvailabilityStatus", "")
        json.put("Branch", "")

        ApiData.getData(json.toString(), ConstantsWFMS.SEARCH_TEAM_LIST_WFMS, this, activity!!)


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()

        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(activity!!, tinyDB)
        } else {

            if (TYPE == ConstantsWFMS.SEARCH_TEAM_LIST_WFMS) {
                try {
                    val json = JSONObject((response as Response<*>).body()!!.toString())
                    if (json.getString("Status").equals("Success")) {

                        myTeamList = Gson().fromJson<java.util.ArrayList<MyTeamModel>>(json.getJSONArray("Output").toString(), object : TypeToken<ArrayList<MyTeamModel>>() {
                        }.type)


                        if (myTeamList.size == 1 && myTeamList.get(0).empId.equals(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))) {
                            rvChatFragmentWFMS.visibility = View.GONE
                            //   tvNoTeamMemberFoundTeamFragmentWFMS.visibility = View.VISIBLE
                            for (i in 0 until myTeamList.size) {
                                if (myTeamList.get(i).empId == tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID)) {
                                    model = myTeamList.get(i)
                                    myTeamList.remove(model)
                                    break

                                }
                            }
                        } else {
                            rvChatFragmentWFMS.visibility = View.VISIBLE
                            //  tvNoTeamMemberFoundTeamFragmentWFMS.visibility = View.GONE
                            for (i in 0 until myTeamList.size) {
                                if (myTeamList.get(i).empId == tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID)) {
                                    model = myTeamList.get(i)
                                    myTeamList.remove(model)
                                    break

                                }
                            }

                            adapter = ChatAdapterWFMS(activity!!, myTeamList)
                            rvChatFragmentWFMS.adapter = adapter
                        }

                    } else {
                        rvChatFragmentWFMS.visibility = View.GONE
                        // tvNoTeamMemberFoundTeamFragmentWFMS.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    UtilitiesWFMS.showToast(activity!!, e.message.toString())
                }

            } else {

            }
        }
    }

}