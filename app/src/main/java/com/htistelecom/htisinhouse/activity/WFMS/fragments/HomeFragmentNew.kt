package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListPopupWindow
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.MainActivityNavigation
import com.htistelecom.htisinhouse.activity.WFMS.dialog.SheetDialogFragmentNew
import com.htistelecom.htisinhouse.activity.WFMS.marketing.MarketingFullScreenDialog
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.activity.WFMS.service.PreOreoLocationService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.font.UbuntuEditText
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.interfaces.GetDateTime
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_home_wfms.*
import kotlinx.android.synthetic.main.layout_home_top_bar.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HomeFragmentNew : BaseFragment(), OnMapReadyCallback, View.OnClickListener, MyInterface {

    companion object {
        var mTaskCount = "0"
    }

    lateinit var activityContext: MainActivityNavigation
    private var lastTaskId: String = ""
    private var mIsAvailable = true
    var marker: MarkerOptions? = null
    lateinit var bitmap: Bitmap
    private var taskAL = ArrayList<TaskListModel>()
    private var taskNewList = ArrayList<TaskListModel>()

    lateinit var dialog: Dialog
    lateinit var listPopupWindowActivity: ListPopupWindow
    private var mCurrentDate: String? = ""
    lateinit var listPopupWindow: ListPopupWindow
    lateinit var dialogSaveLocation: Dialog
    lateinit var bottomSheetFragment: SheetDialogFragmentNew
    lateinit var v: View;

    private var projectList = ArrayList<TwoParameterModel>()
    lateinit var projectArray: Array<String?>
    var mProjectId = ""

    private var siteList = ArrayList<TwoParameterModel>()
    lateinit var siteArray: Array<String?>
    var mSiteId = ""

    private var activityList = ArrayList<TwoParameterModel>()
    lateinit var activityArray: Array<String?>
    var mActivityId = ""
    lateinit var tinyDB: TinyDB


    private var selectListTapLocation = ArrayList<TwoParameterModel>()
    lateinit var selectListTapLocationArray: Array<String?>
    var mSelectListIdTapLocation = ""

    private var selectIndustryTapLocation = ArrayList<TwoParameterModel>()
    lateinit var selectIndustryTapLocationArray: Array<String?>
    var mSelectIndustryIdTapLocation = ""


    lateinit var projectListAdapter: ArrayAdapter<String?>
    lateinit var siteListAdapter: ArrayAdapter<String?>
    lateinit var activityListAdapter: ArrayAdapter<String?>

    lateinit var selectListTapLocationAdaopter: ArrayAdapter<String?>
    lateinit var selectIndustryTapLocationAdapter: ArrayAdapter<String?>


    val FROM_DATE = 0
    val TO_DATE = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.fragment_home_wfms, null)
        return v
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        activityContext = activity as MainActivityNavigation
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()


    }

    private fun initViews() {
        tinyDB = TinyDB(activity)

        mCurrentDate = Utilities.getCurrentDateInMonth()
        bottomSheetFragment = SheetDialogFragmentNew()
        if (!tinyDB.getBoolean(ConstantsWFMS.TINYDB_MEETING_STATUS)) {
            btnFreeHomeTop.text = "Meeting"
            mIsAvailable = false
        } else {
            btnFreeHomeTop.text = "Free"
            mIsAvailable = true

        }

    }

    private fun listeners() {
        rlUpFragmentHomeWFMS.setOnClickListener(this)
        llAddNewTaskHome.setOnClickListener(this)
        llTapLocationTaskHome.setOnClickListener(this)
        llFreeHomeTop.setOnClickListener(this)

    }

    override fun onMapReady(maps: GoogleMap) {

        try {
            maps.clear()

            //   val latLng = LatLng(OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())
            maps.uiSettings.setZoomControlsEnabled(true);


            if (taskNewList.size > 0) {
                for (i in 0 until taskNewList.size) {

                    val model = taskNewList.get(i)

                    lastTaskId = model.taskId
                    val lati: Double = model.latitude.toDouble()
                    val longLat: Double = model.longitude.toDouble()
                    if (model.status.equals("Completed"))
                        maps.addMarker(MarkerOptions().position(LatLng(lati, longLat)).title(model.siteName).snippet(model.address).icon(BitmapDescriptorFactory.fromBitmap(getBitMap(1))))
                    else
                        maps.addMarker(MarkerOptions().position(LatLng(lati, longLat)).title(model.siteName).snippet(model.address))

                }


            }

            //    UtilitiesWFMS.showToast(activity!!, OreoLocationService.LATITUDE + "");

            val lati: Double = OreoLocationService.LATITUDE.toDouble()
            val longLat: Double = OreoLocationService.LONGITUDE.toDouble()
            val mAddress = Utilities.getAddressFromLatLong(activity, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())

            if (marker != null)
                marker == null
            marker = MarkerOptions().position(LatLng(lati, longLat)).title(mAddress.get(0).getAddressLine(0)).icon(BitmapDescriptorFactory.fromBitmap(getBitMap(5)))
            maps.addMarker(marker)

            maps.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lati, longLat)))
            maps.animateCamera(CameraUpdateFactory.zoomTo(17f))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }



        if (isPunchInMethod()) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activityContext.cancelService()

            } else {
                activity!!.stopService(Intent(activity, PreOreoLocationService::class.java))

            }

        }

    }

    fun getBitMap(value: Int): Bitmap {
        try {
            var drawable: Drawable? = null

            value == 1//for completed
            value == 2//for pending
            value == 3//started
            if (value == 1) {
                drawable = resources.getDrawable(R.drawable.icon_marker_completed_task)
                bitmap = Bitmap.createBitmap(drawable!!.getIntrinsicWidth(), drawable!!.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            } else {
                drawable = resources.getDrawable(R.drawable.icon_location_map)
                bitmap = Bitmap.createBitmap(drawable!!.getIntrinsicWidth(), drawable!!.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            }
            var canvas = Canvas(bitmap);
            drawable!!.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable!!.draw(canvas);

        } catch (ex: Exception) {

        }
        return bitmap;
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rlUpFragmentHomeWFMS -> {
                // ivArrowUpHome.visibility=View.GONE
                // ivArrowDownHome.visibility=View.VISIBLE
                val bundle = Bundle()
                bundle.putSerializable("data", taskAL)
                bundle.putSerializable("data_filter", taskNewList)


                bottomSheetFragment.setArguments(bundle)
                bottomSheetFragment.show(fragmentManager!!, "exampleBottomSheet")

            }
            R.id.llAddNewTaskHome -> {


                if (isPunchInMethod()) {

                    if (tinyDB.getString(ConstantsWFMS.TINYDB_USER_TYPE).equals("1")) {
                        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
                        val newFragment: MarketingFullScreenDialog? = MarketingFullScreenDialog.newInstance()
                        newFragment!!.show(ft, "dialog")

                    } else {
                        openDialogAddNewTask()
                    }

                } else
                    UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errPunchIn))
            }
            R.id.llTapLocationTaskHome -> {
                openDialogSaveLocation()

            }
            R.id.llFreeHomeTop -> {
                val json = JSONObject()
                json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
                json.put("MeetingDate", mCurrentDate)
                if (mIsAvailable) {
                    json.put("IsAvailable", "N")

                } else {
                    json.put("IsAvailable", "Y")

                }


                hitAPI(MEETING_STATUS_WFMS, json.toString())
            }
        }
    }

    private fun isPunchInMethod(): Boolean {
        return tinyDB.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)

    }

    private fun openDialogSaveLocation() {
        dialogSaveLocation = Dialog(activity!!)
        dialogSaveLocation.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSaveLocation.setContentView(R.layout.dialog_save_current_location)
        dialogSaveLocation.setCancelable(false)
        dialogSaveLocation.show()
        hitAPI(SELECT_TYPE_TAP_LOCATION_WFMS, "")

        val rlSelectTypeDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.rlSelectTypeDialogSaveCurrentLocation) as RelativeLayout
        val tvSelectTypeDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.tvSelectTypeDialogSaveCurrentLocation) as Ubuntu

        val rlSelectDistrictDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.rlSelectDistrictDialogSaveCurrentLocation) as RelativeLayout
        val tvSelectDistrictDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.tvSelectDistrictDialogSaveCurrentLocation) as Ubuntu

        val tvAddressDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.tvAddressDialogSaveCurrentLocation) as Ubuntu

        val rlSelectIndustryDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.rlSelectIndustryDialogSaveCurrentLocation) as RelativeLayout
        val tvSelectIndustryDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.tvSelectIndustryDialogSaveCurrentLocation) as Ubuntu
        val btnSubmitDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.btnSubmitDialogSaveCurrentLocation) as Button
        val btnCancelDialogSaveCurrentLocation = dialogSaveLocation.findViewById(R.id.btnCancelDialogSaveCurrentLocation) as Button
        val mAddress = Utilities.getAddressFromLatLong(activity, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())

        tvSelectDistrictDialogSaveCurrentLocation.text = mAddress.get(0).adminArea
        tvAddressDialogSaveCurrentLocation.text = mAddress.get(0).getAddressLine(0)
        rlSelectTypeDialogSaveCurrentLocation.setOnClickListener { view ->

            showDropdown(selectListTapLocationArray, object : SpinnerData {
                override fun getData(mId: String, mName: String) {

                    mSelectListIdTapLocation = mId


                }

            }, tvSelectTypeDialogSaveCurrentLocation, 700)
        }

        rlSelectIndustryDialogSaveCurrentLocation.setOnClickListener { view ->

            showDropdown(selectIndustryTapLocationArray, object : SpinnerData {
                override fun getData(mId: String, mName: String) {

                    mSelectIndustryIdTapLocation = mId


                }

            }, tvSelectIndustryDialogSaveCurrentLocation, 700)
        }
        btnCancelDialogSaveCurrentLocation.setOnClickListener { view ->
            dialogSaveLocation.dismiss()
        }
        btnSubmitDialogSaveCurrentLocation.setOnClickListener { view ->

            if (mSelectListIdTapLocation.equals("")) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errSelectType))
            } else if (mSelectIndustryIdTapLocation.equals("")) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errIndustryType))
            } else {
                val jsonObject = JSONObject()
                jsonObject.put("LocationId", "0")
                jsonObject.put("LocationTypeId", mSelectListIdTapLocation)
                jsonObject.put("CompanyNatureId", mSelectIndustryIdTapLocation)
                jsonObject.put("LocationAddress", mAddress.get(0).getAddressLine(0))
                jsonObject.put("CountryName", mAddress.get(0).countryName)
                jsonObject.put("StateName", mAddress.get(0).adminArea)
                jsonObject.put("CityName", mAddress.get(0).locality)
                jsonObject.put("Latitude", OreoLocationService.LATITUDE)
                jsonObject.put("Longitude", OreoLocationService.LONGITUDE)
                jsonObject.put("LoginId", tinyDB.getString(TINYDB_EMP_ID))

                hitAPI(SUBMIT_TAP_LOCATION_WFMS, jsonObject.toString())
            }


        }


    }

    private fun openDialogAddNewTask() {


        dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_task)
        dialog.setCancelable(false)
        dialog.show()
        hitAPI(PROJECT_LIST_WFMS, "")
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

                        hitAPI(ConstantsWFMS.ACTIVITY_LIST_WFMS, JSONObject().put("SiteId", mSiteId).toString())


                    }

                }, tvSiteDialogAddTask, 700)
            }
        }
        rlActivityDialogAddTask.setOnClickListener { view ->
//            listVOs.clear()
//
//            for (i in activityList.indices) {
//                var model = StateVO()
//                model.isSelected = false
//                model.title = activityList.get(i).name
//                listVOs.add(model)
//            }
//
//
//            listPopupWindowActivity = ListPopupWindow(activity)
//            listPopupWindowActivity!!.setAdapter(MyAdapter(activity, 0, listVOs))
//            // listPopupWindowActivity!!.setBackgroundDrawable(resources.getDrawable(R.drawable.rect_black))
//            listPopupWindowActivity!!.anchorView = tvActivityDialogAddTask
//            listPopupWindowActivity!!.width = 700
//            listPopupWindowActivity!!.height = 700
//            listPopupWindowActivity!!.isModal = true
////            listPopupWindowActivity!!.setOnItemClickListener { parent, view, position, id ->
////                mActivityId = "1,2"
////
////
////                //textView.text = array!![position]
////                listPopupWindow!!.dismiss()
////            }
//            listPopupWindowActivity!!.show()


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
            Utilities.getDate(activity!!, year, month, day, calc.getTimeInMillis(), object : GetDateTime {
                override fun getDateTime(strDate: String, strTime: String) {

                    tvFromDateDialogAddTask.text = strDate


                }
            })


        }
//        rlToDateDialogAddTask.setOnClickListener { view ->
//
//            mFromDateAddTask = tvFromDateDialogAddTask.text.toString()
//            mFromTimeAddTask = tvToTimeDialogAddTask.text.toString()
//
//            if (mFromDateAddTask.equals("", ignoreCase = true)) {
//                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errFromdate))
//            } else {
//                leaveFrom = TO_DATE
//                val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
//                try {
//
//                    val date = simpleDateFormat.parse(mFromDateAddTask)
//                    val calendar = Calendar.getInstance()
//                    calendar.time = date
//                    val mDay = calendar.get(Calendar.DAY_OF_MONTH)
//                    val mMonth = calendar.get(Calendar.MONTH)
//                    val mYear = calendar.get(Calendar.YEAR)
//
//                    getDate(mYear, mMonth, mDay, calendar.getTimeInMillis(), object : GetDateTime {
//                        override fun getDateTime(strDate: String, strTime: String) {
//
//                            tvToDateDialogAddTask.text = strDate
//                            tvToTimeDialogAddTask.text = ""
//
//
//                        }
//                    })
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//
//            }
//
//
//        }
//        rlFromTimeDialogAddTask.setOnClickListener { view ->
//
//            mFromDateAddTask = tvFromDateDialogAddTask.text.toString()
//            mToDateAddTask = tvToDateDialogAddTask.text.toString()
//
//            if (mFromDateAddTask.equals("")) {
//                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errFromdate))
//
//            } else {
//                Utilities.selectTime(activity, object : GetDateTime {
//                    override fun getDateTime(strDate: String, strTime: String) {
//
//                        tvFromTimeDialogAddTask.text = strTime
//                        tvToTimeDialogAddTask.text = ""
//
//
//                    }
//                })
//            }
//
//        }
//        rlToTimeDialogAddTask.setOnClickListener { view ->
//            mFromDateAddTask = tvFromDateDialogAddTask.text.toString()
//            mToDateAddTask = tvToDateDialogAddTask.text.toString()
//            mFromTimeAddTask = tvFromTimeDialogAddTask.text.toString()
//
//            if (mFromTimeAddTask.equals("")) {
//                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errInTime))
//
//            } else {
//                Utilities.selectTime(activity, object : GetDateTime {
//                    override fun getDateTime(strDate: String, strTime: String) {
//
//                        tvToTimeDialogAddTask.text = strTime
//
//
//                    }
//                })
//            }
//
//
//        }


        btnSubmitDialogAddTask.setOnClickListener { view ->

            mFromDateAddTask = tvFromDateDialogAddTask.text.toString()
//            mToDateAddTask = tvToDateDialogAddTask.text.toString()
//            mFromTimeAddTask = tvFromTimeDialogAddTask.text.toString()
//            mToTimeAddTask = tvToTimeDialogAddTask.text.toString()
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
                //jsonObject.put("TaskId", "0")
                jsonObject.put("TaskDate", mFromDateAddTask)
//                jsonObject.put("StartTime", mFromTimeAddTask)
//                jsonObject.put("EndDate", mToDateAddTask)
//                jsonObject.put("EndTime", mToTimeAddTask)
                jsonObject.put("SiteId", mSiteId)
                jsonObject.put("ActivityId", mActivityId)
                jsonObject.put("ActivityText", mReasonAddTask)
                hitAPI(ADD_TASK_WFMS, jsonObject.toString())
            }


        }
    }


    fun hitAPI(type: Int, params: String) {
        if (type == PROJECT_LIST_WFMS) {
            ApiData.getGetData(PROJECT_LIST_WFMS, this, activity)
        } else if (type == SITE_LIST_WFMS) {
            ApiData.getData(params, SITE_LIST_WFMS, this, activity)

        } else if (type == ACTIVITY_LIST_WFMS) {
            ApiData.getData(params, ACTIVITY_LIST_WFMS, this, activity)

        } else if (type == ADD_TASK_WFMS) {
            ApiData.getData(params.toString(), ConstantsWFMS.ADD_TASK_WFMS, this, activity!!)

        } else if (type == SELECT_TYPE_TAP_LOCATION_WFMS) {
            ApiData.getGetData(SELECT_TYPE_TAP_LOCATION_WFMS, this, activity)

        } else if (type == SELECT_INDUSTRY_TAP_LOCATION_WFMS) {
            ApiData.getGetData(SELECT_INDUSTRY_TAP_LOCATION_WFMS, this, activity)

        } else if (type == SUBMIT_TAP_LOCATION_WFMS) {
            ApiData.getData(params.toString(), ConstantsWFMS.SUBMIT_TAP_LOCATION_WFMS, this, activity!!)

        } else if (type == MY_TASK_LIST_NEW_WFMS) {
            ApiData.getData(params.toString(), ConstantsWFMS.MY_TASK_LIST_NEW_WFMS, this, activity!!)

        } else if (type == MEETING_STATUS_WFMS) {


            ApiData.getData(params.toString(), ConstantsWFMS.MEETING_STATUS_WFMS, this, activity!!)

        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        //   bottomSheetFragment.dismiss()


    }

    override fun onResume() {
        super.onResume()
        var json = JSONObject()
        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        json.put("FromDate", mCurrentDate)
        json.put("ToDate", mCurrentDate)
        json.put("SiteUploadedId", "0")


        hitAPI(ConstantsWFMS.MY_TASK_LIST_NEW_WFMS, json.toString())
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        if (TYPE != SELECT_TYPE_TAP_LOCATION_WFMS) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            Log.e("dismiss", TYPE.toString())

        }


        if ((response as Response<*>).code() == 401 || (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(activity!!, tinyDB)
        } else {
            if (TYPE != SELECT_TYPE_TAP_LOCATION_WFMS) {
                if (Utilities.isShowing())
                    Utilities.dismissDialog()
                Log.e("dismiss", TYPE.toString())

            }
            if (TYPE == PROJECT_LIST_WFMS) {

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

                projectListAdapter = ArrayAdapter<String?>(activity!!, R.layout.spinner_item, projectArray)
                // hitAPI(ACTIVITY_LIST_WFMS, "")
            } else if (TYPE == SITE_LIST_WFMS) {


                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())
                siteList.clear()
                siteArray = emptyArray()
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

                siteListAdapter = ArrayAdapter<String?>(activity!!, R.layout.spinner_item, siteArray)

            } else if (TYPE == ACTIVITY_LIST_WFMS) {
                activityArray = emptyArray()
                activityList.clear()
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

                activityListAdapter = ArrayAdapter<String?>(activity!!, R.layout.spinner_item, activityArray)
            } else if (TYPE == ADD_TASK_WFMS) {

                val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObj.getString("Status").equals("Success")) {
                    Utilities.showToast(activity!!, jsonObj.getString("Message"))
                    dialog.dismiss()


                    mProjectId = ""
                    mSiteId = ""
                    mActivityId = ""
                    var json = JSONObject()
                    json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
                    json.put("FromDate", mCurrentDate)
                    json.put("ToDate", mCurrentDate)
                    json.put("SiteUploadedId", "0")
                    // Utilities.getCurrentDateInMonth()

                    hitAPI(ConstantsWFMS.MY_TASK_LIST_NEW_WFMS, json.toString())
                } else {
                    Utilities.showToast(activity!!, jsonObj.getString("Message"))
                }

//            {"Status":"Success","Message":"Record Saved Successfully !!","Output":""}
            } else if (TYPE == SELECT_TYPE_TAP_LOCATION_WFMS) {

                selectListTapLocation.clear()
                selectListTapLocationArray = emptyArray()
                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val model = TwoParameterModel()
                    model.id = item.getString("TapLocationTypeId")
                    model.name = item.getString("TapLocationTypeName")
                    selectListTapLocation.add(model)
                }
                selectListTapLocationArray = arrayOfNulls<String>(selectListTapLocation.size)
                for (i in selectListTapLocation.indices) {
                    selectListTapLocationArray[i] = selectListTapLocation.get(i).name
                }

                selectListTapLocationAdaopter = ArrayAdapter<String?>(activity!!, R.layout.spinner_item, selectListTapLocationArray)

                hitAPI(SELECT_INDUSTRY_TAP_LOCATION_WFMS, "")

            } else if (TYPE == SELECT_INDUSTRY_TAP_LOCATION_WFMS) {

                selectIndustryTapLocation.clear()
                selectIndustryTapLocationArray = emptyArray()
                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val model = TwoParameterModel()
                    model.id = item.getString("CompanyNatureId")
                    model.name = item.getString("CompanyNatureName")
                    selectIndustryTapLocation.add(model)
                }
                selectIndustryTapLocationArray = arrayOfNulls<String>(selectIndustryTapLocation.size)
                for (i in selectIndustryTapLocation.indices) {
                    selectIndustryTapLocationArray[i] = selectIndustryTapLocation.get(i).name
                }

                selectIndustryTapLocationAdapter = ArrayAdapter<String?>(activity!!, R.layout.spinner_item, selectIndustryTapLocationArray)
            } else if (TYPE == SUBMIT_TAP_LOCATION_WFMS) {
                val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObj.getString("Status").equals("Success")) {
                    Utilities.showToast(activity!!, jsonObj.getString("Message"))
                    dialogSaveLocation.dismiss()
                    mSelectListIdTapLocation = ""
                    mSelectIndustryIdTapLocation = ""

                } else {
                    Utilities.showToast(activity!!, jsonObj.getString("Message"))
                }
            } else if (TYPE == MEETING_STATUS_WFMS) {
                val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObj.getString("Status").equals("Success")) {
                    Utilities.showToast(activity!!, jsonObj.getString("Message"))
                    if (mIsAvailable) {
                        mIsAvailable = false
                        btnFreeHomeTop.text = "Meeting"
                        tinyDB.putBoolean(ConstantsWFMS.TINYDB_MEETING_STATUS, false)
                    } else {
                        mIsAvailable = true
                        btnFreeHomeTop.text = "Free"
                        tinyDB.putBoolean(ConstantsWFMS.TINYDB_MEETING_STATUS, true)

                    }

                } else {
                    Utilities.showToast(activity!!, jsonObj.getString("Message"))
                }
            } else if (TYPE == MY_TASK_LIST_NEW_WFMS) {
                val jsonObj = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObj.getString("Status").equals("Success")) {
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




                    tvTasksCountHome.text = taskNewList.size.toString() + " Scheduled Tasks"
                    mTaskCount = taskNewList.size.toString()

                } else {
                    tvTasksCountHome.text = "No Scheduled Tasks"

                }
                val mapFragment = childFragmentManager!!
                        .findFragmentById(com.htistelecom.htisinhouse.R.id.fragment_map) as SupportMapFragment
                mapFragment.getMapAsync(this)
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
            } else if (textView.id == R.id.tvSelectTypeDialogSaveCurrentLocation) {
                spinnerData.getData(selectListTapLocation.get(position).id, selectListTapLocation.get(position).name)
            } else if (textView.id == R.id.tvSelectIndustryDialogSaveCurrentLocation) {
                spinnerData.getData(selectIndustryTapLocation.get(position).id, selectIndustryTapLocation.get(position).name)
            }
            textView.text = array!![position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }


}

