package com.htistelecom.htisinhouse.activity.WFMS.activity


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.ADD_TASK_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MARKETING_TASK_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.TaskAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.dialogOpen.TaskOtherMarketingdialogOpen
import com.htistelecom.htisinhouse.activity.WFMS.fragments.HomeFragmentNew
import com.htistelecom.htisinhouse.activity.WFMS.marketing.MarketingFullScreenDialog
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TaskModel
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import com.htistelecom.htisinhouse.adapter.MarketingTaskAdapter
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.font.UbuntuEditText
import com.htistelecom.htisinhouse.interfaces.GetDateTime
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_tasks_week_detail_wfms.*
import kotlinx.android.synthetic.main.fragment_task_wfms.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class TasksWeekDetailActivityWFMS : AppCompatActivity(), View.OnClickListener, MyInterface {
    private var mCurrentDate: String = ""
    lateinit var dialog: Dialog
    lateinit var dialogTasks: Dialog
    private var taskList = java.util.ArrayList<TaskModel>()

    lateinit var listPopupWindow: ListPopupWindow
    lateinit var model: MyTeamModel
    lateinit var projectListAdapter: ArrayAdapter<String?>
    lateinit var siteListAdapter: ArrayAdapter<String?>
    lateinit var activityListAdapter: ArrayAdapter<String?>
    lateinit var tinyDB: TinyDB

    private var projectList = ArrayList<TwoParameterModel>()
    lateinit var projectArray: Array<String?>
    var mProjectId = ""

    private var siteList = ArrayList<TwoParameterModel>()
    lateinit var siteArray: Array<String?>
    var mSiteId = ""

    private var activityList = ArrayList<TwoParameterModel>()
    lateinit var activityArray: Array<String?>
    var mActivityId = ""

    var tvtitlePop: TextView? = null
    var ivDrawerPop: ImageView? = null
    var ivBackPop: ImageView? = null

    var rvTaskFragment: RecyclerView? = null
    lateinit var adapter: MarketingTaskAdapter
    private var taskAL = ArrayList<TaskListModel>()
    private var taskNewList = ArrayList<TaskListModel>()
    var isMyProfile = false

    companion object {
        var taskCount = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_week_detail_wfms)
        initViews()
        listeners()
    }


    private fun initViews() {
        tinyDB = TinyDB(this)
        isMyProfile = intent.getBooleanExtra("UserProfile", false)
        if (isMyProfile)

            tv_title.text = "Profile Detail"
        else
            tv_title.text = "Member Details"


        ivDrawer.visibility = View.GONE
        model = intent.getSerializableExtra("data") as MyTeamModel

        if (!model.empImg.equals(""))
            Glide.with(this).load(model.empImgPath + model.empImg).into(ivProfileImageTasksWeekDetailActivityWFMS);
        else
            Glide.with(this).load(R.drawable.icon_man).into(ivProfileImageTasksWeekDetailActivityWFMS);



        tvProfileNameTasksWeekDetailActivityWFMS.text = model.empName
        tvDesignationTasksWeekDetailActivityWFMS.text = model.empDesignation
        tvReportsToTasksWeekDetailActivityWFMS.text = "Reports To- " + model.empReportingManager

        if (model.checkInLocation.equals(""))
            tvLocationTasksWeekDetailActivityWFMS.text = "NA"
        else
            tvLocationTasksWeekDetailActivityWFMS.text = model.checkInLocation

        if (model.tasks.equals(""))
            tvTasksWeekDetailActivityWFMS.text = "No Tasks"
        else
            tvTasksWeekDetailActivityWFMS.text = model.tasks

        tvClaimsTasksWeekDetailActivityWFMS.text = "Rs." + model.empClaims
        if (model.checkInLocation.equals(""))
            tvPunchStatusTasksWeekDetailActivityWFMS.text = "Not Punched In"
        else
            tvPunchStatusTasksWeekDetailActivityWFMS.text = "Punched In"

        if (model.empId.equals(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))) {
            llCallTasksWeekDetailActivityWFMS.visibility = View.GONE
            llMessageTasksWeekDetailActivityWFMS.visibility = GONE
        } else {
            llCallTasksWeekDetailActivityWFMS.visibility = View.VISIBLE
            llMessageTasksWeekDetailActivityWFMS.visibility = VISIBLE
        }


        val array: Array<String> = model.weeklyTasks.split(",").toTypedArray()

        callBarChart(array[0], array[1], array[2], array[3], array[4], array[5], array[6])


    }

    private fun callBarChart(s: String, s1: String, s2: String, s3: String, s4: String, s5: String, s6: String) {
        val xAxisLabel = java.util.ArrayList<String>()
        xAxisLabel.add("Mon")
        xAxisLabel.add("Tue")
        xAxisLabel.add("Wed")
        xAxisLabel.add("Thu")
        xAxisLabel.add("Fri")
        xAxisLabel.add("Sat")
        xAxisLabel.add("Sun")

        val weekdays = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun") // Your List / array with String Values For X-axis Labels


        val xAxis: XAxis = chart.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setValueFormatter(IndexAxisValueFormatter(weekdays));

        //xAxis.setValueFormatter(IAxisValueFormatter { value, axis -> xAxisLabel[value.toInt()] })
        // xAxis.valueFormatter = IAxisValueFormatter { value, axis -> xAxisLabel[value.toInt()] }


        chart.setTouchEnabled(false)
        val fMon = s.toFloat()
        val fTue = s1.toFloat()
        val fWed = s2.toFloat()
        val fThu = s3.toFloat()
        val fFri = s4.toFloat()
        val fSat = s5.toFloat()
        val fSun = s6.toFloat()


        val entries: MutableList<BarEntry> = java.util.ArrayList()
        entries.add(BarEntry(0f, fMon))
        entries.add(BarEntry(1f, fTue))
        entries.add(BarEntry(2f, fWed))
        entries.add(BarEntry(3f, fThu))
        entries.add(BarEntry(4f, fFri))
        // gap of 2f
        // gap of 2f
        entries.add(BarEntry(5f, fSat))
        entries.add(BarEntry(6f, fSun))
        chart.getDescription().setEnabled(false)

        val leg: Legend = chart.getLegend()
        leg.isEnabled = false
        chart.getAxisLeft().setDrawGridLines(false)
        chart.getXAxis().setDrawGridLines(false)
        val rightYAxis: YAxis = chart.getAxisRight()
        val y: YAxis = chart.getAxisLeft()
        y.setAxisMaxValue(7f)
        y.setAxisMinValue(0f)



        rightYAxis.isEnabled = false
        val set = BarDataSet(entries, "BarDataSet")
        val data = BarData(set)
        data.setDrawValues(false)

        data.barWidth = 0.9f // set custom bar width

        chart.setData(data)
        chart.setFitBars(true) // make the x-axis fit exactly all bars

        chart.invalidate()
    }

    private fun listeners() {
        ivAddTasksWeekDetailActivityWFMS.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        llCallTasksWeekDetailActivityWFMS.setOnClickListener(this)
        llMessageTasksWeekDetailActivityWFMS.setOnClickListener(this)
        tvTasksWeekDetailActivityWFMS.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.ivAddTasksWeekDetailActivityWFMS) {
            if (isPunchInMethod()) {

                if (tinyDB.getString(ConstantsWFMS.TINYDB_USER_TYPE).equals("1")) {


                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    val newFragment: TaskOtherMarketingdialogOpen? = TaskOtherMarketingdialogOpen.newInstance(model.empId)
                    newFragment!!.show(ft, "dialog")

                } else {
                    openDialogAddNewTask()

                }
            } else {
                UtilitiesWFMS.showToast(this, resources.getString(R.string.errPunchIn))

            }


        } else if (v.id == R.id.ivBack) {
            backToHome()
        } else if (v.id == R.id.llCallTasksWeekDetailActivityWFMS) {
            val mPhoneNumber = model.empMobileNo
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhoneNumber))
            startActivity(intent)
        } else if (v.id == R.id.llMessageTasksWeekDetailActivityWFMS) {
            val mPhoneNumber = model.empMobileNo

            val sms_uri = Uri.parse("smsto:" + mPhoneNumber)
            val sms_intent = Intent(Intent.ACTION_SENDTO, sms_uri)
            startActivity(sms_intent)
        } else if (v.id == R.id.tvTasksWeekDetailActivityWFMS) {
            if (model.tasks.contains("0/0") || model.tasks.equals(""))
            Utilities.showToast(this, "No Task Available ")
            else
                openDialogToShowTasks()

        }
    }

    private fun openDialogToShowTasks() {
        dialogTasks = Dialog(this)
        dialogTasks.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogTasks.setContentView(R.layout.dialog_show_task)
        dialogTasks.setCancelable(false)
        dialogTasks.show()
        tvtitlePop = dialogTasks!!.findViewById(R.id.tv_title) as Ubuntu
        ivBackPop = dialogTasks!!.findViewById(R.id.ivBack) as ImageView
        ivDrawerPop = dialogTasks!!.findViewById(R.id.ivDrawer) as ImageView
        rvTaskFragment = dialogTasks!!.findViewById(R.id.rvTaskFragment) as RecyclerView
        rvTaskFragment!!.layoutManager = LinearLayoutManager(this)
        ivDrawerPop!!.setVisibility(View.GONE)
        tvtitlePop!!.text = "Task"
        ivBackPop!!.visibility = VISIBLE
        ivBackPop!!.setOnClickListener { dialogTasks.dismiss() }


        if (model.empLoginCategory.equals("1")) {
            val date = ConstantKotlin.getCurrentDate()
            val jsonObject = JSONObject()

            jsonObject.put("EmpId", model.empId)
            jsonObject.put("TaskDate", date)
            hitAPI(ConstantsWFMS.MARKETING_TASK_LIST_WFMS, jsonObject.toString())
        } else {
            val date = ConstantKotlin.getCurrentDate()

            var json = JSONObject()
            json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
            json.put("FromDate", date)
            json.put("ToDate", date)
            json.put("SiteUploadedId", "0")
            hitAPI(ConstantsWFMS.MY_TASK_LIST_NEW_WFMS, json.toString())
        }


    }

    private fun isPunchInMethod(): Boolean {
        return tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)
    }

    fun hitAPI(type: Int, params: String) {
        if (type == ConstantsWFMS.PROJECT_LIST_WFMS) {
            ApiData.getGetData(ConstantsWFMS.PROJECT_LIST_WFMS, this, this)
        } else if (type == ConstantsWFMS.SITE_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.SITE_LIST_WFMS, this, this)

        } else if (type == ConstantsWFMS.ACTIVITY_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.ACTIVITY_LIST_WFMS, this, this)

        } else if (type == ConstantsWFMS.ADD_TASK_WFMS) {
            ApiData.getData(params.toString(), ConstantsWFMS.ADD_TASK_WFMS, this, this!!)

        } else if (type == ConstantsWFMS.MARKETING_TASK_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.MARKETING_TASK_LIST_WFMS, this, this)
        } else if (type == ConstantsWFMS.MY_TASK_LIST_NEW_WFMS) {
            ApiData.getData(params.toString(), ConstantsWFMS.MY_TASK_LIST_NEW_WFMS, this, this!!)

        }
    }

    private fun openDialogAddNewTask() {


        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_task)
        dialog.setCancelable(true)
        dialog.show()
        hitAPI(ConstantsWFMS.PROJECT_LIST_WFMS, "")
        var mFromDateAddTask = ""
        var mToDateAddTask = ""
        var mFromTimeAddTask = ""
        var mToTimeAddTask = ""
        var mReasonAddTask = ""

        val rlProjectDialogAddTask = dialog.findViewById(R.id.rlProjectDialogAddTask) as RelativeLayout
        val tvProjectDialogAddTask = dialog.findViewById(R.id.tvProjectDialogAddTask) as Ubuntu

        val rlFromDateDialogAddTask = dialog.findViewById(R.id.rlFromDateDialogAddTask) as RelativeLayout
        val tvFromDateDialogAddTask = dialog.findViewById(R.id.tvFromDateDialogAddTask) as Ubuntu

//        val rlFromTimeDialogAddTask = dialog.findViewById(R.id.rlFromTimeDialogAddTask) as RelativeLayout
//        val tvFromTimeDialogAddTask = dialog.findViewById(R.id.tvFromTimeDialogAddTask) as Ubuntu
//
//        val rlToDateDialogAddTask = dialog.findViewById(R.id.rlToDateDialogAddTask) as RelativeLayout
//        val tvToDateDialogAddTask = dialog.findViewById(R.id.tvToDateDialogAddTask) as Ubuntu
//
//        val rlToTimeDialogAddTask = dialog.findViewById(R.id.rlToTimeDialogAddTask) as RelativeLayout
//        val tvToTimeDialogAddTask = dialog.findViewById(R.id.tvToTimeDialogAddTask) as Ubuntu


        val rlSiteDialogAddTask = dialog.findViewById(R.id.rlSiteDialogAddTask) as RelativeLayout
        val tvSiteDialogAddTask = dialog.findViewById(R.id.tvSiteDialogAddTask) as Ubuntu

        val rlActivityDialogAddTask = dialog.findViewById(R.id.rlActivityDialogAddTask) as RelativeLayout
        val tvActivityDialogAddTask = dialog.findViewById(R.id.tvActivityDialogAddTask) as Ubuntu


        val btnSubmitDialogAddTask = dialog.findViewById(R.id.btnSubmitDialogAddTask) as Button
        val btnCancelDialogAddTask = dialog.findViewById(R.id.btnCancelDialogAddTask) as Button


        val etMessageDialogAddTask = dialog.findViewById(R.id.etMessageDialogAddTask) as UbuntuEditText

        rlProjectDialogAddTask.setOnClickListener { view ->

            showDropdown(projectArray, object : SpinnerData {
                override fun getData(mId: String, mName: String) {

                    mProjectId = mId
                    val json = JSONObject()
                    json.put("ProjectId", mProjectId)
                    hitAPI(ConstantsWFMS.SITE_LIST_WFMS, json.toString())

                }

            }, tvProjectDialogAddTask, 700)
        }


        rlSiteDialogAddTask.setOnClickListener { view ->
            if (mProjectId.equals("")) {
                Utilities.showToast(this, resources.getString(R.string.errProject))
            } else {
                showDropdown(siteArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mSiteId = mId

                        hitAPI(ConstantsWFMS.ACTIVITY_LIST_WFMS, JSONObject().put("SiteId", mSiteId).toString())


                    }

                }, tvSiteDialogAddTask, 700)
            }
        }
        rlActivityDialogAddTask.setOnClickListener { view ->


            if (mSiteId.equals("")) {
                Utilities.showToast(this, resources.getString(R.string.errSite))
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
            Utilities.getDate(this, year, month, day, calc.getTimeInMillis(), object : GetDateTime {
                override fun getDateTime(strDate: String, strTime: String) {

                    tvFromDateDialogAddTask.text = strDate


                }
            })


        }


        btnSubmitDialogAddTask.setOnClickListener { view ->

            mFromDateAddTask = tvFromDateDialogAddTask.text.toString()

            mReasonAddTask = etMessageDialogAddTask.text.toString()
            if (mProjectId.equals("")) {
                UtilitiesWFMS.showToast(this, resources.getString(R.string.errProject))

            } else if (mFromDateAddTask.equals("")) {
                UtilitiesWFMS.showToast(this, resources.getString(R.string.errFromdate))
            } else if (mSiteId.equals("")) {
                UtilitiesWFMS.showToast(this, resources.getString(R.string.errSite))
            } else if (mActivityId.equals("")) {
                UtilitiesWFMS.showToast(this, resources.getString(R.string.errActivity))
            }
//
            else if (mReasonAddTask.equals("")) {
                UtilitiesWFMS.showToast(this, resources.getString(R.string.errReason))
            } else {


                val jsonObject = JSONObject()
                jsonObject.put("EmpId", model.empId)
                jsonObject.put("ProjectId", mProjectId)
                //jsonObject.put("TaskId", "0")
                jsonObject.put("TaskDate", mFromDateAddTask)
//                jsonObject.put("StartTime", mFromTimeAddTask)
//                jsonObject.put("EndDate", mToDateAddTask)
//                jsonObject.put("EndTime", mToTimeAddTask)
                jsonObject.put("SiteId", mSiteId)
                jsonObject.put("ActivityId", mActivityId)
                jsonObject.put("ActivityText", mReasonAddTask)
                hitAPI(ConstantsWFMS.ADD_TASK_WFMS, jsonObject.toString())
            }


        }
    }

    fun showDropdown(array: Array<String?>, spinnerData: SpinnerData, textView: Ubuntu, width: Int) {
        listPopupWindow = ListPopupWindow(this)
        listPopupWindow!!.setAdapter(ArrayAdapter<Any?>(
                this,
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


    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if ((response as Response<*>).code() == 401 || (response as Response<*>).code() == 403) {

            if (Utilities.isShowing())
                Utilities.dismissDialog()
            finish()
            ConstantKotlin.logout(this, tinyDB)
        } else {
            if (TYPE == MARKETING_TASK_LIST_WFMS) {
                val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObject.getString("Status").equals("Success")) {
                    taskList = Gson().fromJson<java.util.ArrayList<TaskModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<TaskModel>>() {

                    }.type)


                    adapter = MarketingTaskAdapter(this, taskList)
                    rvTaskFragment!!.adapter = adapter
                }
            } else if (TYPE == ConstantsWFMS.PROJECT_LIST_WFMS) {

                projectList.clear()
                projectArray = emptyArray()
                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val model = TwoParameterModel()
                    model.id = item.getString("ProjectId")
                    model.name = item.getString("ProjectName")
                    projectList.add(model)
                }
                projectArray = arrayOfNulls<String>(projectList.size)
                for (i in projectList.indices) {
                    projectArray[i] = projectList.get(i).name
                }

                projectListAdapter = ArrayAdapter<String?>(this, R.layout.spinner_item, projectArray)
                // hitAPI(ACTIVITY_LIST_WFMS, "")
            } else if (TYPE == ConstantsWFMS.SITE_LIST_WFMS) {

                siteList.clear()
                siteArray = emptyArray()
                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val model = TwoParameterModel()
                    model.id = item.getString("SiteId")
                    model.name = item.getString("SiteName")
                    siteList.add(model)
                }

                siteArray = arrayOfNulls<String>(siteList.size)
                for (i in siteList.indices) {
                    siteArray[i] = siteList.get(i).name
                }

                siteListAdapter = ArrayAdapter<String?>(this, R.layout.spinner_item, siteArray)

            } else if (TYPE == ConstantsWFMS.ACTIVITY_LIST_WFMS) {
                activityList.clear()
                activityArray = emptyArray()
                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val model = TwoParameterModel()
                    model.id = item.getString("ActivityId")
                    model.name = item.getString("ActivityName")
                    activityList.add(model)
                }
                activityArray = arrayOfNulls<String>(activityList.size)
                for (i in activityList.indices) {
                    activityArray[i] = activityList.get(i).name
                }

                activityListAdapter = ArrayAdapter<String?>(this, R.layout.spinner_item, activityArray)
            } else if (TYPE == ADD_TASK_WFMS) {

                val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObj.getString("Status").equals("Success")) {
                    Utilities.showToast(this, jsonObj.getString("Message"))
                    val array: Array<String> = jsonObj.getString("WeeklyTasks").split(",").toTypedArray()

                    callBarChart(array[0], array[1], array[2], array[3], array[4], array[5], array[6])
                    tvTasksWeekDetailActivityWFMS.text = jsonObj.getString("TotalTasks")
                    dialog.dismiss()

                    // startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Team"))

                    //finish()

                } else {
                    Utilities.showToast(this!!, jsonObj.getString("Message"))
                }
            } else if (TYPE == ConstantsWFMS.MY_TASK_LIST_NEW_WFMS) {
                val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObj.getString("Status").equals("Success")) {
                    rvTaskFragment!!.visibility = View.VISIBLE
                   // tvNoTaskFragmentWFMS.visibility = View.GONE
//                    taskAL.clear()
//                    // val jsonArray=JSONArray(jsonObj.getString("Output"))
//                    taskAL = Gson().fromJson<java.util.ArrayList<TaskListModel>>(jsonObj.getJSONArray("Output").toString(), object : TypeToken<ArrayList<TaskListModel>>() {
//
//                    }.type);


                    taskAL.clear()
                    taskNewList.clear()
                    // val jsonArray=JSONArray(jsonObj.getString("Output"))
                    taskAL = Gson().fromJson<java.util.ArrayList<TaskListModel>>(jsonObj.getJSONArray("Output").toString(), object : TypeToken<ArrayList<TaskListModel>>() {}.type);

                    var lastId = ""
                    for (record in 0 until taskAL.size) {

                        if (lastId.equals(taskAL.get(record).taskId)) {
                        } else {
                            lastId = taskAL.get(record).taskId
                            taskNewList.add(taskAL.get(record));

                        }


                    }

                    rvTaskFragment!!.adapter = TaskAdapterWFMS(this, taskNewList, taskAL)
                }
            }
        }
    }

    override fun onBackPressed() {
        backToHome()
    }

    fun backToHome() {
        // startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Team"))
        finish()
    }

}