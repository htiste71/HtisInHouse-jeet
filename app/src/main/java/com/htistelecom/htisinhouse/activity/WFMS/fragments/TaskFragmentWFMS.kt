package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.AddTask
import com.htistelecom.htisinhouse.activity.WFMS.AddTask.Companion.activityArray
import com.htistelecom.htisinhouse.activity.WFMS.AddTask.Companion.activityList
import com.htistelecom.htisinhouse.activity.WFMS.AddTask.Companion.projectList
import com.htistelecom.htisinhouse.activity.WFMS.AddTask.Companion.siteArray
import com.htistelecom.htisinhouse.activity.WFMS.AddTask.Companion.siteList

import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.TaskAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.font.UbuntuEditText
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.interfaces.GetDateTime
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_task_wfms.*
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class TaskFragmentWFMS : BaseFragment(), MyInterface, View.OnClickListener {
    private var ivAddTaskTaskFragmentWFMS: ImageView? = null
    private var mCurrentDate: String? = ""
    lateinit var tinyDB: TinyDB
    private var taskAL = ArrayList<TaskListModel>()
    lateinit var dialog: Dialog
    lateinit var listPopupWindow: ListPopupWindow
    var mProjectId = ""
    var mSiteId = ""
    var mActivityId = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task_wfms, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()

    }

    private fun listeners() {
        ivAddTaskTaskFragmentWFMS!!.setOnClickListener(this)

    }

    private fun initViews() {
        tinyDB = TinyDB(activity)

        ivAddTaskTaskFragmentWFMS = activity!!.findViewById<ImageView>(R.id.ivAddHeader)

        mCurrentDate = Utilities.getCurrentDateInMonth()
        rvTaskFragmentWFMS!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


    }

    override fun onResume() {
        super.onResume()

        callTaskList()

    }

    private fun callTaskList() {
        var json = JSONObject()
        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        json.put("FromDate", mCurrentDate)
        json.put("ToDate", mCurrentDate)
        json.put("SiteUploadedId", "0")



        hitAPI(MY_TASK_LIST_WFMS, json.toString())
    }


    private fun openDialogAddNewTask() {


        dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_task)
        dialog.setCancelable(false)
        dialog.show()
        var mFromDateAddTask = ""
        var mReasonAddTask = ""

        val rlProjectDialogAddTask = dialog.findViewById(R.id.rlProjectDialogAddTask) as RelativeLayout
        val tvProjectDialogAddTask = dialog.findViewById(R.id.tvProjectDialogAddTask) as Ubuntu

        val rlFromDateDialogAddTask = dialog.findViewById(R.id.rlFromDateDialogAddTask) as RelativeLayout
        val tvFromDateDialogAddTask = dialog.findViewById(R.id.tvFromDateDialogAddTask) as Ubuntu


        val rlSiteDialogAddTask = dialog.findViewById(R.id.rlSiteDialogAddTask) as RelativeLayout
        val tvSiteDialogAddTask = dialog.findViewById(R.id.tvSiteDialogAddTask) as Ubuntu

        val rlActivityDialogAddTask = dialog.findViewById(R.id.rlActivityDialogAddTask) as RelativeLayout
        val tvActivityDialogAddTask = dialog.findViewById(R.id.tvActivityDialogAddTask) as Ubuntu


        val btnSubmitDialogAddTask = dialog.findViewById(R.id.btnSubmitDialogAddTask) as Button
        val btnCancelDialogAddTask = dialog.findViewById(R.id.btnCancelDialogAddTask) as Button


        val etMessageDialogAddTask = dialog.findViewById(R.id.etMessageDialogAddTask) as UbuntuEditText
        hitAPI(PROJECT_LIST_WFMS, "")

        rlProjectDialogAddTask.setOnClickListener { view ->

            showDropdown(AddTask.projectArray, object : SpinnerData {
                override fun getData(mId: String, mName: String) {

                    mProjectId = mId
                    val json = JSONObject()
                    json.put("ProjectId", mProjectId)
                    hitAPI(SITE_LIST_WFMS, json.toString())

                }

            }, tvProjectDialogAddTask, 700)
        }


        rlSiteDialogAddTask.setOnClickListener { view ->
            if (mProjectId.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errProject))
            } else {
                showDropdown(siteArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mSiteId = mId

                        hitAPI(ACTIVITY_LIST_WFMS, JSONObject().put("SiteId", mSiteId).toString())


                    }

                }, tvSiteDialogAddTask, 700)
            }
        }
        rlActivityDialogAddTask.setOnClickListener { view ->


            if (mSiteId.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errSite))
            } else {
                showDropdown(activityArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mActivityId = mId


                    }

                }, tvActivityDialogAddTask, 700)
            }

        }

        btnCancelDialogAddTask.setOnClickListener { view -> dialog.dismiss() }


        mCurrentDate = DateUtils.currentDate();
        rlFromDateDialogAddTask.setOnClickListener { view ->
            val calc = Calendar.getInstance()
            val year = calc.get(Calendar.YEAR)
            val month = calc.get(Calendar.MONTH)
            val day = calc.get(Calendar.DAY_OF_MONTH)
            Utilities.getDate(activity!!,year, month, day, calc.getTimeInMillis(), object : GetDateTime {
                override fun getDateTime(strDate: String, strTime: String) {

                    tvFromDateDialogAddTask.text = strDate


                }
            })


        }






        btnSubmitDialogAddTask.setOnClickListener { view ->

            mFromDateAddTask = tvFromDateDialogAddTask.text.toString()

            mReasonAddTask = etMessageDialogAddTask.text.toString()
            if (mProjectId.equals("")) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errProject))

            } else if (mFromDateAddTask.equals("")) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errFromdate))
            } else if (mSiteId.equals("")) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errSite))
            } else if (mActivityId.equals("")) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errActivity))
            }
//
            else if (mReasonAddTask.equals("")) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errReason))
            } else {


                val jsonObject = JSONObject()
                jsonObject.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
                jsonObject.put("ProjectId", mProjectId)
                jsonObject.put("TaskDate", mFromDateAddTask)

                jsonObject.put("SiteId", mSiteId)
                jsonObject.put("ActivityId", mActivityId)
                jsonObject.put("ActivityText", mReasonAddTask)
                hitAPI(ADD_TASK_WFMS, jsonObject.toString())

            }


        }
    }


    fun showDropdown(array: Array<String?>, spinnerData: SpinnerData, textView: Ubuntu, width: Int) {
        listPopupWindow = ListPopupWindow(activity!!)
        listPopupWindow!!.setAdapter(ArrayAdapter<Any?>(
                activity!!,
                R.layout.row_profile_spinner, array))
        listPopupWindow!!.setBackgroundDrawable(resources.getDrawable(R.drawable.rect_white_background_no_radius_border))
        listPopupWindow!!.anchorView = textView
        listPopupWindow!!.width = width
        listPopupWindow!!.height = 700
        listPopupWindow!!.isModal = true
        listPopupWindow!!.setOnItemClickListener { parent, view, position, id ->

            if (textView.id == R.id.tvProjectDialogAddTask) {
                spinnerData.getData(projectList.get(position).id, projectList.get(position).name)
            } else if (textView.id == R.id.tvSiteDialogAddTask) {
                spinnerData.getData(siteList.get(position).id, siteList.get(position).name)

            } else if (textView.id == R.id.tvActivityDialogAddTask) {
                spinnerData.getData(activityList.get(position).id, activityList.get(position).name)
            }
            textView.text = array!![position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }



    fun hitAPI(type: Int, params: String) {
        if (type == MY_TASK_LIST_WFMS) {
            ApiData.getData(params.toString(), ConstantsWFMS.MY_TASK_LIST_WFMS, this, activity!!)

        } else if (type == PROJECT_LIST_WFMS) {
            ApiData.getGetData(PROJECT_LIST_WFMS, this, activity)

        } else if (type == SITE_LIST_WFMS) {
            ApiData.getData(params, SITE_LIST_WFMS, this, activity)

        } else if (type == ACTIVITY_LIST_WFMS) {
            ApiData.getData(params, ACTIVITY_LIST_WFMS, this, activity)

        } else if (type == ADD_TASK_WFMS) {
            ApiData.getData(params.toString(), ConstantsWFMS.ADD_TASK_WFMS, this, activity!!)

        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == MY_TASK_LIST_WFMS) {
            val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                rvTaskFragmentWFMS.visibility = View.VISIBLE
                tvNoTaskFragmentWFMS.visibility = View.GONE
                taskAL.clear()
                // val jsonArray=JSONArray(jsonObj.getString("Output"))
                taskAL = Gson().fromJson<java.util.ArrayList<TaskListModel>>(jsonObj.getJSONArray("Output").toString(), object : TypeToken<ArrayList<TaskListModel>>() {

                }.type);
                rvTaskFragmentWFMS.adapter = TaskAdapterWFMS(activity, taskAL)
                HomeFragmentWFMS.mTaskCount = taskAL.size.toString()
            } else {
                rvTaskFragmentWFMS.visibility = View.GONE
                tvNoTaskFragmentWFMS.visibility = View.VISIBLE
            }
        } else if (TYPE == PROJECT_LIST_WFMS) {
            AddTask.commonMethod(response, PROJECT_LIST_WFMS)


        } else if (TYPE == SITE_LIST_WFMS) {
            AddTask.commonMethod(response, SITE_LIST_WFMS)

        } else if (TYPE == ACTIVITY_LIST_WFMS) {
            AddTask.commonMethod(response, ACTIVITY_LIST_WFMS)

        } else if (TYPE == ADD_TASK_WFMS) {

            val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                Utilities.showToast(activity!!, jsonObj.getString("Message"))
                dialog.dismiss()
                mProjectId=""
                mSiteId=""
                mActivityId=""
                callTaskList()

            } else {
                Utilities.showToast(activity!!, jsonObj.getString("Message"))
            }

        }


    }

    override fun onClick(v: View) {
        if (v.id == R.id.ivAddHeader) {
            if (isPunchedInMethod())
                openDialogAddNewTask()
            else
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errPunchIn))
        }
    }

    private fun isPunchedInMethod(): Boolean {
        return tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)
    }


}