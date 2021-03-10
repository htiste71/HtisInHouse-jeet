package com.htistelecom.htisinhouse.activity.WFMS.leave_managment


import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatSpinner
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.leave_managment.adapters.CompOffAdapter
import com.htistelecom.htisinhouse.activity.WFMS.leave_managment.adapters.LeaveListAdapter
import com.htistelecom.htisinhouse.activity.WFMS.leave_managment.adapters.ODListAdapter
import com.htistelecom.htisinhouse.activity.WFMS.leave_managment.models.CompOffListModel
import com.htistelecom.htisinhouse.activity.WFMS.models.*
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.font.UbuntuEditText
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.interfaces.GetDateTime
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_leave_type_outdoor_duty.*
import org.json.JSONObject
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LeaveType_OutdoorDutyFragment : BaseFragment(), MyInterface, View.OnClickListener {
    private var mCompOffId: String = ""
    private lateinit var dialogOD: Dialog
    lateinit var dialog: Dialog
    lateinit var dialogCompOff: Dialog

    private var ivAddLeave: ImageView? = null
    lateinit var tinyDB: TinyDB
    private var leaveTypeDayList = ArrayList<LeaveTypeDayModel>()
    private var leaveTypeList = ArrayList<LeaveTypeModel>()
    lateinit var leaveTypeArray: Array<String?>
    lateinit var leaveTypeDayArray: Array<String?>

    private var leaveFrom: Int = 0

    private var compOffList = ArrayList<CompOffListModel>()

    private var leaveTypeCompOffList = ArrayList<TwoParameterModel>()
    lateinit var leaveTypeCompOffArray: Array<String?>


    val FROM_DATE = 0
    val TO_DATE = 1
    val COMP_OFF_DATE = 2
    val COMP_OFF_APPLY_DATE = 3

    private var mCurrentDate: String = ""
    var leaveList = ArrayList<LeaveListModel>()
    var ODList = ArrayList<ODListModel>()

    lateinit var leaveTypeAdapter: ArrayAdapter<String?>
    lateinit var leaveTypeDayAdapter: ArrayAdapter<String?>
    lateinit var leaveTypeCompOffAdapter: ArrayAdapter<String?>


    var mMessage = "";
    var mLeaveTypeId = "1";
    var mFromLeaveType = "";
    var mToLeaveType = "";
    var mFromDate = "";
    var mToDate = "";
    var mFromLeaveDayId = "1";
    var mToLeaveDayId = "1";
    var mFromLeaveTypeName = "";
    var mToLeaveTypeName = "";
    var tvFromDateApplyLeave: Ubuntu? = null
    var tvToDateApplyLeave: Ubuntu? = null
    var leaveTypeFromDateSpnrApplyLeave: AppCompatSpinner? = null
    var leaveTypeToDateSpnrApplyLeave: AppCompatSpinner? = null
    var leaveTypeSpnrApplyLeave: AppCompatSpinner? = null

    var mCompOffLeaveDayTypeId = ""
    var mCompOffLeaveTypeId = ""
    var mCompOffDate = ""
    var mCompOffApplyDate = ""

    var tvCompOffDateDialogCompOff: Ubuntu? = null
    var tvApplyDateDialogCompOff: Ubuntu? = null
    var leaveTypeDaySpnrDialogCompOff: AppCompatSpinner? = null
    var leaveTypeSpnrDialogCompOff: AppCompatSpinner? = null


    var LEAVE = 1;


    var OD = 2;

    val COMP_OFF = 3
    var LEAVE_TYPE: Int = LEAVE


    var FOR_TYPE = 0
    val FOR_SUBMITTED = 1
    val FOR_APPROVED = 2
    val FOR_REJECTED = 3

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_leave_type_outdoor_duty, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()


    }

    private fun listeners() {
        tvSubmittedLeaveLeaveTypeOutdoorDutyFragment.setOnClickListener(this)
        tvRejectedLeaveLeaveTypeOutdoorDutyFragment.setOnClickListener(this)
        tvApprovedLeaveLeaveTypeOutdoorDutyFragment.setOnClickListener(this)

        ivAddLeave!!.setOnClickListener(this)
    }

    private fun initViews() {
        ivAddLeave = activity!!.findViewById<ImageView>(R.id.ivAddHeader)

        tinyDB = TinyDB(activity)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        //hit API


        LEAVE_TYPE = LEAVE
        FOR_TYPE = FOR_SUBMITTED
        changeColor(ContextCompat.getColorStateList(activity!!, R.color.colorOrange), ContextCompat.getColorStateList(activity!!, R.color.colorDarkBlue), ContextCompat.getColorStateList(activity!!, R.color.colorDarkBlue))
        calledMethodCommon()
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, id ->

            if (id == R.id.radioLeaveStatusLeaveTypeOutdoorDutyFragment) {

                ivAddLeave!!.visibility = View.VISIBLE

                LEAVE_TYPE = LEAVE
                FOR_TYPE = FOR_SUBMITTED
                changeColor(ContextCompat.getColorStateList(activity!!, R.color.colorOrange), ContextCompat.getColorStateList(activity!!, R.color.colorDarkBlue), ContextCompat.getColorStateList(activity!!, R.color.colorDarkBlue))
                calledMethodCommon()


            } else if (id == R.id.radioCompOffStatusLeaveTypeOutdoorDutyFragment) {
                ivAddLeave!!.visibility = View.GONE

                LEAVE_TYPE = COMP_OFF
                FOR_TYPE = FOR_SUBMITTED
                changeColor(ContextCompat.getColorStateList(activity!!, R.color.colorDarkBlue), ContextCompat.getColorStateList(activity!!, R.color.colorDarkBlue), ContextCompat.getColorStateList(activity!!, R.color.colorOrange))
                calledMethodCommon()


            } else {
                ivAddLeave!!.visibility = View.VISIBLE

                LEAVE_TYPE = OD
                FOR_TYPE = FOR_SUBMITTED
                changeColor(ContextCompat.getColorStateList(activity!!, R.color.colorDarkBlue), ContextCompat.getColorStateList(activity!!, R.color.colorOrange), ContextCompat.getColorStateList(activity!!, R.color.colorDarkBlue))
                calledMethodCommon()

            }
        })
    }

    private fun changeColor(color1: ColorStateList?, color2: ColorStateList?, color3: ColorStateList?) {
        radioLeaveStatusLeaveTypeOutdoorDutyFragment.setTextColor(color1);
        radioODStatusLeaveTypeOutdoorDutyFragment.setTextColor(color2);
        radioCompOffStatusLeaveTypeOutdoorDutyFragment.setTextColor(color3)
        if (FOR_TYPE == FOR_SUBMITTED) {
            tvSubmittedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorOrange))
            tvApprovedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
            tvRejectedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))

        } else if (FOR_TYPE == FOR_APPROVED) {
            tvSubmittedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
            tvApprovedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorOrange))
            tvRejectedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
        } else {
            tvSubmittedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
            tvApprovedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
            tvRejectedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorOrange))
        }


    }


    private fun calledMethodCommon() {


        if (LEAVE_TYPE == LEAVE) {
            val json = JSONObject()
            json.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
            json.put("LeaveStatus", FOR_TYPE.toString())

            calledMethod(LEAVE_LIST_WFMS, json.toString())
        } else if (LEAVE_TYPE == OD) {
            val json = JSONObject()
            json.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
            json.put("ODRequestStatus", FOR_TYPE.toString())

            calledMethod(OUTDOOR_LIST_WFMS, json.toString())

        } else {
            val json = JSONObject()
            json.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
            // json.put("EmpId","24")

            calledMethod(COMP_OFF_STATUS_LIST_WFMS, json.toString())
        }


    }

    private fun setDayType(type: String) {
        mFromDate = tvFromDateApplyLeave!!.text.toString().trim { it <= ' ' }
        mToDate = tvToDateApplyLeave!!.text.toString().trim { it <= ' ' }
        mFromLeaveTypeName = leaveTypeFromDateSpnrApplyLeave!!.selectedItem.toString()
        mToLeaveTypeName = leaveTypeToDateSpnrApplyLeave!!.selectedItem.toString()

        val isEqual = DateUtils.compareDates(mFromDate, mToDate)
        if (isEqual) {
            if (mFromLeaveTypeName.equals("Full Day", ignoreCase = true)) {
                leaveTypeToDateSpnrApplyLeave!!.setSelection(0)
                mToLeaveType = "FD"
                mFromLeaveDayId = "1"

            } else if (mFromLeaveTypeName.equals("Ist Half", ignoreCase = true)) {
                leaveTypeToDateSpnrApplyLeave!!.setSelection(1)
                mToLeaveType = "IH"
                mFromLeaveDayId = "2"
                leaveTypeToDateSpnrApplyLeave!!.isEnabled = false


            } else if (mFromLeaveTypeName.equals("2nd Half", ignoreCase = true)) {
                leaveTypeFromDateSpnrApplyLeave!!.setSelection(2)
                leaveTypeToDateSpnrApplyLeave!!.setSelection(2)
                mFromLeaveType = "SH"
                mToLeaveType = "SH"
                mFromLeaveDayId = "3"
                mToLeaveDayId = "3"
                leaveTypeToDateSpnrApplyLeave!!.isEnabled = false
            }

        } else {

            if (mFromLeaveTypeName.equals("Ist Half", ignoreCase = true)) {

                leaveTypeFromDateSpnrApplyLeave!!.setSelection(0)
                mFromLeaveType = "FD"
                mFromLeaveDayId = "1"

            } else if (mFromLeaveTypeName.equals("2nd Half", ignoreCase = true)) {

                if (mToLeaveTypeName.equals("Ist Half", true) || mToLeaveTypeName.equals("Full Day", true)) {

                } else {
                    leaveTypeToDateSpnrApplyLeave!!.setSelection(0)
                    mToLeaveType = "FD"
                    mToLeaveDayId = "1"
                }


            } else if (mToLeaveTypeName.equals("2nd Half", true)) {

                leaveTypeToDateSpnrApplyLeave!!.setSelection(0)
                mToLeaveType = "FD"
                mToLeaveDayId = "1"
                leaveTypeFromDateSpnrApplyLeave!!.setSelection(0)
                mFromLeaveType = "FD"
                mFromLeaveDayId = "1"
            } else if (mToLeaveTypeName.equals("Ist Half", true)) {
                if (mFromLeaveTypeName.equals("Ist Half")) {
                    leaveTypeFromDateSpnrApplyLeave!!.setSelection(0)
                    mFromLeaveType = "FD"
                    mFromLeaveDayId = "1"
                }

            }
        }


    }


    fun calledMethod(TYPE: Int, params: String) {
        if (TYPE == ConstantsWFMS.LEAVE_TYPE_WFMS) {
            ApiData.getData(params, LEAVE_TYPE_WFMS, this, activity)

        } else if (TYPE == LEAVE_TYPE_DAY_WFMS) {
            ApiData.getGetData(LEAVE_TYPE_DAY_WFMS, this, activity)


        } else if (TYPE == ConstantsWFMS.APPLY_LEAVE_WFMS) {

            try {


                ApiData.getData(params, ConstantsWFMS.APPLY_LEAVE_WFMS, this, activity)

            } catch (e: Exception) {
                Log.e("Error", e.message)
            }

        } else if (TYPE == OUTDOOR_REQUEST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.OUTDOOR_REQUEST_WFMS, this, activity)

        } else if (TYPE == LEAVE_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.LEAVE_LIST_WFMS, this, activity)

        } else if (TYPE == OUTDOOR_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.OUTDOOR_LIST_WFMS, this, activity)

        } else if (TYPE == APPLY_COMP_OFF_WFMS) {
            ApiData.getData(params, ConstantsWFMS.APPLY_COMP_OFF_WFMS, this, activity)

        } else if (TYPE == COMP_OFF_STATUS_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.COMP_OFF_STATUS_LIST_WFMS, this, activity)


        } else if (TYPE == COMP_OFF_LEAVE_TYPE_WFMS) {
            ApiData.getData(params, ConstantsWFMS.COMP_OFF_LEAVE_TYPE_WFMS, this, activity)


        }
    }


    override fun sendResponse(response: Any, TYPE: Int) {

        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(activity!!, tinyDB)
        } else {

            if (TYPE == LEAVE_TYPE_DAY_WFMS)
            else
                Utilities.dismissDialog()
            try {
                if (TYPE == LEAVE_TYPE_WFMS) {
                    leaveTypeList.clear()
                    leaveTypeArray = emptyArray()

                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        leaveTypeList = Gson().fromJson<java.util.ArrayList<LeaveTypeModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<LeaveTypeModel>>() {

                        }.type)

                        var refinedList = ArrayList<LeaveTypeModel>()
                        for (i in leaveTypeList.indices) {
                            //Storing names to string array
                            if (leaveTypeList.get(i).canApply.equals("Y", true))
                                refinedList.add(leaveTypeList.get(i))

                        }
                        leaveTypeArray = arrayOfNulls<String>(refinedList.size)
                        for (i in 0 until refinedList.size) {
                            leaveTypeArray[i] = refinedList.get(i).leaveTypeName
                        }
                        mLeaveTypeId = leaveTypeList.get(0).leaveTypeId
                        leaveTypeAdapter = ArrayAdapter<String?>(activity!!, R.layout.spinner_item, leaveTypeArray)
                        leaveTypeSpnrApplyLeave!!.setAdapter(leaveTypeAdapter)
                    } else {
                        Utilities.showToast(activity, "No Details found.")
                    }


                } else if (TYPE == LEAVE_TYPE_DAY_WFMS) {

                    leaveTypeDayList.clear()
                    leaveTypeDayArray = emptyArray()
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        leaveTypeDayList = Gson().fromJson<java.util.ArrayList<LeaveTypeDayModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<LeaveTypeDayModel>>() {

                        }.type)

                        leaveTypeDayArray = arrayOfNulls<String?>(leaveTypeDayList.size)
                        for (i in leaveTypeDayList.indices) {
                            leaveTypeDayArray[i] = leaveTypeDayList.get(i).dayType
                        }
                        leaveTypeDayAdapter = ArrayAdapter<String?>(activity!!, R.layout.spinner_item, leaveTypeDayArray)


                        mCompOffLeaveDayTypeId = leaveTypeDayList.get(0).dayTypeId

                        if (LEAVE_TYPE == COMP_OFF) {
                            leaveTypeDaySpnrDialogCompOff!!.setAdapter(leaveTypeDayAdapter)

                        } else {
                            leaveTypeFromDateSpnrApplyLeave!!.setAdapter(leaveTypeDayAdapter)
                            leaveTypeToDateSpnrApplyLeave!!.setAdapter(leaveTypeDayAdapter)
                        }


                    } else {
                        Utilities.showToast(activity, "No Details found.")
                    }
                    val jsonObj = JSONObject()
                    jsonObj.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
                    if (LEAVE_TYPE == COMP_OFF)
                        calledMethod(COMP_OFF_LEAVE_TYPE_WFMS, jsonObj.toString())
                    else
                        calledMethod(LEAVE_TYPE_WFMS, jsonObj.toString())


                } else if (TYPE == ConstantsWFMS.APPLY_LEAVE_WFMS) {
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        Utilities.showToast(activity, jsonObject.getString("Message"))
                        dialog.dismiss()
                        calledMethodCommon()

                    } else {
                        Utilities.showToast(activity, jsonObject.getString("Message"))
                    }

                } else if (TYPE == ConstantsWFMS.OUTDOOR_REQUEST_WFMS) {
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        Utilities.showToast(activity, jsonObject.getString("Message"))
                        dialogOD.dismiss()
                        calledMethodCommon()
                    } else {
                        Utilities.showToast(activity, jsonObject.getString("Message"))
                    }

                } else if (TYPE == LEAVE_LIST_WFMS) {
                    leaveList.clear()
                    tvNoRecord.visibility = View.GONE
                    recyclerView.visibility = VISIBLE
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        leaveList = Gson().fromJson<java.util.ArrayList<LeaveListModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<LeaveListModel>>() {

                        }.type)
                        val adapter = LeaveListAdapter(activity!!, leaveList, FOR_TYPE)
                        recyclerView.adapter = adapter


                    } else {
                        tvNoRecord.visibility = View.VISIBLE
                        recyclerView.visibility = GONE
                    }

                } else if (TYPE == OUTDOOR_LIST_WFMS) {

                    ODList.clear()
                    tvNoRecord.visibility = View.GONE
                    recyclerView.visibility = VISIBLE
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        ODList = Gson().fromJson<java.util.ArrayList<ODListModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<ODListModel>>() {

                        }.type)
                        val adapter = ODListAdapter(activity!!, ODList, FOR_TYPE)
                        recyclerView.adapter = adapter


                    } else {
                        tvNoRecord.visibility = View.VISIBLE
                        recyclerView.visibility = GONE
                    }

                } else if (TYPE == COMP_OFF_STATUS_LIST_WFMS) {
                    compOffList.clear()
                    tvNoRecord.visibility = View.GONE
                    recyclerView.visibility = VISIBLE
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        compOffList = Gson().fromJson<java.util.ArrayList<CompOffListModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<CompOffListModel>>() {

                        }.type)

                        var newList = ArrayList<CompOffListModel>()
                        for (i in 0 until compOffList.size) {
                            if (FOR_TYPE == FOR_SUBMITTED) {
                                if (compOffList.get(i).statusId.equals("") || compOffList.get(i).statusId.equals("0") || compOffList.get(i).statusId.equals("1")) {
                                    newList.add(compOffList.get(i))
                                }

                            } else if (FOR_TYPE == FOR_APPROVED) {
                                if (compOffList.get(i).statusId.equals("2")) {
                                    newList.add(compOffList.get(i))
                                }
                            } else {
                                if (compOffList.get(i).statusId.equals("3")) {
                                    newList.add(compOffList.get(i))
                                }
                            }
                        }
                        if (newList.size > 0) {
                            tvNoRecord.visibility = View.GONE
                            recyclerView.visibility = VISIBLE
                            val adapter = CompOffAdapter(activity!!, compOffList, FOR_TYPE, this)
                            recyclerView.adapter = adapter
                        } else {
                            tvNoRecord.visibility = View.VISIBLE
                            recyclerView.visibility = GONE
                        }


                    } else {
                        tvNoRecord.visibility = View.VISIBLE
                        recyclerView.visibility = GONE
                    }

                } else if (TYPE == APPLY_COMP_OFF_WFMS) {
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        Utilities.showToast(activity, jsonObject.getString("Message"))
                        dialogCompOff.dismiss()
                        calledMethodCommon()

                    } else {
                        Utilities.showToast(activity, jsonObject.getString("Message"))
                    }

                } else if (TYPE == COMP_OFF_LEAVE_TYPE_WFMS) {
                    leaveTypeCompOffList.clear()
                    leaveTypeCompOffArray = emptyArray()
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        val jsonArray = jsonObject.getJSONArray("Output")
                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)

                            val model = TwoParameterModel()
                            model.id = item.getString("LeaveTypeId")
                            model.name = item.getString("LeaveType")
                            leaveTypeCompOffList.add(model)
                        }

                    }

                    mCompOffLeaveTypeId = leaveTypeCompOffList.get(0).id
                    leaveTypeCompOffArray = arrayOfNulls<String>(leaveTypeCompOffList.size)
                    for (i in leaveTypeCompOffList.indices) {
                        leaveTypeCompOffArray[i] = leaveTypeCompOffList.get(i).name
                    }
                    leaveTypeCompOffAdapter = ArrayAdapter<String?>(activity!!, R.layout.spinner_item, leaveTypeCompOffArray)
                    leaveTypeSpnrDialogCompOff!!.adapter = leaveTypeCompOffAdapter
                }
            } catch (e: Exception) {
                Log.e("Error", e.message)
            }
        }
    }

    fun getLeaveType(leaveType: String): String {
        return if (leaveType.equals("Ist Half", ignoreCase = true)) {
            "FH"
        } else if (leaveType.equals("2nd Half", ignoreCase = true)) {
            "SH"
        } else {
            "FD"
        }


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvSubmittedLeaveLeaveTypeOutdoorDutyFragment -> {
                tvSubmittedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorOrange))
                tvApprovedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
                tvRejectedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
                FOR_TYPE = FOR_SUBMITTED
                calledMethodCommon()

            }
            R.id.tvApprovedLeaveLeaveTypeOutdoorDutyFragment -> {
                tvSubmittedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
                tvApprovedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorOrange))
                tvRejectedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
                FOR_TYPE = FOR_APPROVED


                calledMethodCommon()
            }


            R.id.tvRejectedLeaveLeaveTypeOutdoorDutyFragment -> {
                tvSubmittedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
                tvApprovedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorDarkBlue))
                tvRejectedLeaveLeaveTypeOutdoorDutyFragment.setTextColor(resources.getColor(R.color.colorOrange))
                FOR_TYPE = FOR_REJECTED

                calledMethodCommon()
            }
            R.id.ivAddHeader -> {
                if (LEAVE_TYPE == LEAVE) {
                    dialogApplyLeave()
                } else if (LEAVE_TYPE == OD) {
                    dialogODStatus()

                }

            }
        }
    }


    private fun setDateTimeField(year: Int, month: Int, day: Int, timeInMillis: Long) {
        Utilities.getDate(activity!!, year, month, day, timeInMillis, object : GetDateTime {
            override fun getDateTime(strDate: String, strTime: String) {

                if (leaveFrom == FROM_DATE) {
                    mFromDate = strDate
                    tvFromDateApplyLeave!!.text = strDate
                    mFromLeaveDayId = "1"
                    leaveTypeFromDateSpnrApplyLeave!!.setSelection(0)

                    mToDate = ""
                    tvToDateApplyLeave!!.text = ""
                    mToLeaveDayId = "1"
                    leaveTypeToDateSpnrApplyLeave!!.setSelection(0)

                    mFromLeaveType = ""
                    mToLeaveType = ""


                } else if (leaveFrom == TO_DATE) {
                    tvToDateApplyLeave!!.text = strDate
                    setDayType("to")
                } else if (leaveFrom == COMP_OFF_DATE) {
                    mCompOffDate = strDate
                    tvCompOffDateDialogCompOff!!.text = strDate
                } else if (leaveFrom == COMP_OFF_APPLY_DATE) {
                    mCompOffApplyDate = strDate
                    tvApplyDateDialogCompOff!!.text = strDate
                }

            }
        })


    }


    fun dialogODStatus() {


        dialogOD = Dialog(activity!!)
        dialogOD.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogOD.setContentView(R.layout.dialog_od_status)
        dialogOD.setCancelable(false)
        dialogOD.show()
        var mFromDateOD = ""
        var mToDateOD = ""
        var mFromTimeOD = "00:00"
        var mToTimeOD = "00:00"
        var mReasonOD = ""
        var mDayType = "F"
        var mNightShift = ""

        val btnApplyOD = dialogOD.findViewById(R.id.btnApplyOD) as Button
        val btnCancelOD = dialogOD.findViewById(R.id.btnCancelOD) as Button
        val rlFromDateOD = dialogOD.findViewById(R.id.rlFromDateOD) as RelativeLayout
        val rlToDateOD = dialogOD.findViewById(R.id.rlToDateOD) as RelativeLayout
        val tvFromDateOD = dialogOD.findViewById(R.id.tvFromDateOD) as Ubuntu
        val tvToDateOD = dialogOD.findViewById(R.id.tvToDateOD) as Ubuntu
        val tvTotalDaysOD = dialogOD.findViewById(R.id.tvTotalDaysOD) as Ubuntu

        val rlInTimeOD = dialogOD.findViewById(R.id.rlInTimeOD) as RelativeLayout
        val rlOutTimeOD = dialogOD.findViewById(R.id.rlOutTimeOD) as RelativeLayout
        val tvInTimeOD = dialogOD.findViewById(R.id.tvInTimeOD) as Ubuntu
        val tvOutTimeOD = dialogOD.findViewById(R.id.tvOutTimeOD) as Ubuntu
        val tvTotalHoursOD = dialogOD.findViewById(R.id.tvTotalHoursOD) as Ubuntu


        val etReasonOD = dialogOD.findViewById(R.id.etReasonOD) as UbuntuEditText
        val rgDay = dialogOD.findViewById(R.id.rgDay) as RadioGroup
        val rbFullDay = dialogOD.findViewById(R.id.rbFullDay) as RadioButton
        val rbHalfDay = dialogOD.findViewById(R.id.rbHalfDay) as RadioButton
        //    val cbNightShift = dialogOD.findViewById(R.id.cbNightShift) as CheckBox

        val llTimeHeaderOD = dialogOD.findViewById(R.id.llTimeHeaderOD) as LinearLayout
        val llTimeOD = dialogOD.findViewById(R.id.llTimeOD) as LinearLayout



        mCurrentDate = DateUtils.currentDate();

        llTimeHeaderOD.visibility = View.GONE
        llTimeOD.visibility = View.GONE
        tvTotalHoursOD.visibility = View.GONE
        rgDay.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.rbFullDay -> {
                        mDayType = "F"
                        llTimeHeaderOD.visibility = View.GONE
                        llTimeOD.visibility = View.GONE
                        tvTotalHoursOD.visibility = View.GONE
                        rlToDateOD.isEnabled = true

                    }
                    R.id.rbHalfDay -> {
                        mDayType = "H"
                        tvFromDateOD.text = ""
                        tvToDateOD.text = ""
                        llTimeHeaderOD.visibility = View.VISIBLE
                        llTimeOD.visibility = View.VISIBLE
                        tvTotalHoursOD.visibility = View.VISIBLE
                        rlToDateOD.isEnabled = false
                        tvTotalDaysOD.text = "Total Days : 0"

                    }
                }
            }
        })

        rlFromDateOD.setOnClickListener { view ->


            leaveFrom = FROM_DATE
            val calc = Calendar.getInstance()
            val year = calc.get(Calendar.YEAR)
            val month = calc.get(Calendar.MONTH)
            val day = calc.get(Calendar.DAY_OF_MONTH)
            Utilities.getDate(activity!!, year, month, day, calc.getTimeInMillis(), object : GetDateTime {
                override fun getDateTime(strDate: String, strTime: String) {
                    if (mDayType.equals("H")) {
                        tvFromDateOD.text = strDate

                        tvToDateOD.text = strDate
                        tvInTimeOD.text = ""
                        tvOutTimeOD.text = ""
                        tvTotalDaysOD.text = "Total Days : 1"
                    } else {
                        tvFromDateOD.text = strDate
                        tvToDateOD.text = ""
                        tvInTimeOD.text = ""
                        tvOutTimeOD.text = ""
                    }


                }
            })


        }
        rlToDateOD.setOnClickListener { view ->

            mFromDateOD = tvFromDateOD.text.toString()
            mFromTimeOD = tvInTimeOD.text.toString()

            if (mFromDateOD.equals("", ignoreCase = true)) {
                Utilities.showToast(activity, resources.getString(R.string.errFromdate))
            } else {
                leaveFrom = TO_DATE
                val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                try {

                    val date = simpleDateFormat.parse(mFromDateOD)
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                    val mMonth = calendar.get(Calendar.MONTH)
                    val mYear = calendar.get(Calendar.YEAR)

                    Utilities.getDate(activity!!, mYear, mMonth, mDay, calendar.getTimeInMillis(), object : GetDateTime {
                        override fun getDateTime(strDate: String, strTime: String) {

                            tvToDateOD.text = strDate
                            tvOutTimeOD.text = ""
                            mToTimeOD = ""

                            var mTotalDays = getCountOfDays(mFromDateOD, strDate)
                            tvTotalDaysOD.text = ""
                            tvTotalDaysOD.text = "Total Days : " + mTotalDays


                        }
                    })
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

            }


        }
        rlInTimeOD.setOnClickListener { view ->

            mFromDateOD = tvFromDateOD.text.toString()
            mToDateOD = tvToDateOD.text.toString()

            if (mFromDateOD.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errFromdate))

            } else {
                Utilities.selectTime(activity, object : GetDateTime {
                    override fun getDateTime(strDate: String, strTime: String) {

                        tvInTimeOD.text = strTime
                        tvOutTimeOD.text = ""
                        mToTimeOD = ""


                    }
                }, 0, "")
            }

        }
        rlOutTimeOD.setOnClickListener { view ->
            mFromDateOD = tvFromDateOD.text.toString()
            mToDateOD = tvToDateOD.text.toString()
            mFromTimeOD = tvInTimeOD.text.toString()

            if (mFromTimeOD.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errInTime))

            } else {
                Utilities.selectTime(activity, object : GetDateTime {
                    override fun getDateTime(strDate: String, strTime: String) {

                        tvOutTimeOD.text = strTime
                        var mTimeDifference = calculateTime(mFromTimeOD, strTime)
                        tvTotalHoursOD.text = "Total Time : " + mTimeDifference


                    }
                }, 1, mFromTimeOD)
            }


        }

        btnCancelOD.setOnClickListener { view -> dialogOD.dismiss() }
        btnApplyOD.setOnClickListener { view ->

            mFromDateOD = tvFromDateOD.text.toString()
            mToDateOD = tvToDateOD.text.toString()
            mFromTimeOD = tvInTimeOD.text.toString()
            mToTimeOD = tvOutTimeOD.text.toString()
            mReasonOD = etReasonOD.text.toString()
//            val isNightShift = cbNightShift.isChecked
//            if (isNightShift) {
//                mNightShift = "Y"
//            } else {
//                mNightShift = "N"
//            }
            if (mFromDateOD.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errFromdate))
            } else if (mToDateOD.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errToDate))
            }
//            else if (mFromTimeOD.equals("")) {
//                Utilities.showToast(activity, resources.getString(R.string.errInTime))
//            } else if (mToDateOD.equals("")) {
//                Utilities.showToast(activity, resources.getString(R.string.errOutTime))
//            }
            else if (mReasonOD.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errReason))
            } else {

                if (mFromTimeOD.equals("")) {
                    mFromTimeOD = "00:00"
                }
                if (mToTimeOD.equals("")) {
                    mToTimeOD = "00:00"
                }

                val jsonObject = JSONObject()
                jsonObject.put("LeaveId", "0")

                jsonObject.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
                jsonObject.put("FromDate", mFromDateOD)
                jsonObject.put("UptoDate", mToDateOD)
                jsonObject.put("ODType", mDayType)
                jsonObject.put("IsNightShift", "N")
                jsonObject.put("FromTime", mFromTimeOD)
                jsonObject.put("UptoTime", mToTimeOD)

                jsonObject.put("ODPurpose", mReasonOD)
                calledMethod(ConstantsWFMS.OUTDOOR_REQUEST_WFMS, jsonObject.toString())

            }

        }

    }

    private fun calculateTime(mFromTimeOD: String, mToTimeOD: String): String {


        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

        val date1 = simpleDateFormat.parse(mFromTimeOD)
        val date2 = simpleDateFormat.parse(mToTimeOD)
        val millis = date2.time - date1.time
        Log.v("Data1", "" + date1.time)
        Log.v("Data2", "" + date2.time)
        val hours = (millis / (1000 * 60 * 60)).toInt()
        val mins = (millis / (1000 * 60)).toInt() % 60
        var mHours = ""
        var mMinutes = ""
        if (hours < 9) {
            mHours = "0" + hours.toString()
        } else {
            mHours = hours.toString()
        }
        if (mins < 9) {
            mMinutes = "0" + mins.toString()
        } else {
            mMinutes = mins.toString()
        }
        val diff = mHours + " Hours " + mMinutes + " Minutes" // updated value every1 second
        return diff


    }

    private fun dialogApplyLeave() {
        dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_leave_apply)
        dialog.setCancelable(false)
        dialog.show()
        calledMethod(LEAVE_TYPE_DAY_WFMS, "")
        val btnSubmitApplyLeave = dialog.findViewById(R.id.btnSubmitApplyLeave) as Button
        val btnCancelApplyLeave = dialog.findViewById(R.id.btnCancelApplyLeave) as Button
        val rlFromDateApplyLeave = dialog.findViewById(R.id.rlFromDateApplyLeave) as RelativeLayout
        val rlToDateApplyLeave = dialog.findViewById(R.id.rlToDateApplyLeave) as RelativeLayout
        tvFromDateApplyLeave = dialog.findViewById(R.id.tvFromDateApplyLeave) as Ubuntu
        tvToDateApplyLeave = dialog.findViewById(R.id.tvToDateApplyLeave) as Ubuntu
        val etReasonApplyLeave = dialog.findViewById(R.id.etReasonApplyLeave) as UbuntuEditText
        leaveTypeFromDateSpnrApplyLeave = dialog.findViewById(R.id.leaveTypeFromDateSpnrApplyLeave) as AppCompatSpinner
        leaveTypeToDateSpnrApplyLeave = dialog.findViewById(R.id.leaveTypeToDateSpnrApplyLeave) as AppCompatSpinner
        leaveTypeSpnrApplyLeave = dialog.findViewById(R.id.leaveTypeSpnrApplyLeave) as AppCompatSpinner


        btnCancelApplyLeave.setOnClickListener { view -> dialog.dismiss() }

        //get current date
        mCurrentDate = DateUtils.currentDate();

        //setting adapter to spinner


        rlFromDateApplyLeave.setOnClickListener({ v ->
            leaveFrom = FROM_DATE
            val calc = Calendar.getInstance()
            val year = calc.get(Calendar.YEAR)
            val month = calc.get(Calendar.MONTH)
            val day = calc.get(Calendar.DAY_OF_MONTH)
            setDateTimeField(year, month, day, calc.getTimeInMillis())
        })


        rlToDateApplyLeave.setOnClickListener({ v ->
            leaveTypeToDateSpnrApplyLeave!!.setEnabled(true)
            if (mFromDate.equals("", ignoreCase = true)) {
                Utilities.showToast(activity, resources.getString(R.string.errFromdate))
            } else if (mFromLeaveDayId.equals("", ignoreCase = true)) {
                Utilities.showToast(activity, resources.getString(R.string.err_leave_type))

            } else {
                leaveFrom = TO_DATE
                val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                try {

                    val date = simpleDateFormat.parse(mFromDate)
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                    val mMonth = calendar.get(Calendar.MONTH)
                    val mYear = calendar.get(Calendar.YEAR)

                    setDateTimeField(mYear, mMonth, mDay, calendar.timeInMillis)

                } catch (e: ParseException) {
                    e.printStackTrace()
                }

            }


        })




        btnCancelApplyLeave.setOnClickListener({ v -> dialog.dismiss() })

        leaveTypeFromDateSpnrApplyLeave!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                mFromDate = tvFromDateApplyLeave!!.getText().toString().trim()
                if (mFromDate.equals("", ignoreCase = true)) {
                    Utilities.showToast(activity, resources.getString(R.string.errFromdate))
                } else {

                    leaveTypeToDateSpnrApplyLeave!!.setEnabled(true)
                    mFromLeaveType = ""
                    mToLeaveType = ""


//                    if (position == 0) {
//                        leaveTypeToDateSpnrApplyLeave!!.setSelection(0)
//                        mFromLeaveDayId = leaveTypeDayList.get(position).dayTypeId
//                    } else {
                    mFromLeaveType = getLeaveType(parent.getItemAtPosition(position).toString())
                    mFromLeaveDayId = leaveTypeDayList.get(position).dayTypeId
                    mToLeaveType = ""

                    if (!mToDate.equals("", ignoreCase = true)) {
                        setDayType("from")
                    }


                    //   }


                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        leaveTypeToDateSpnrApplyLeave!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mToDate = tvToDateApplyLeave!!.getText().toString().trim()
                if (mFromDate.equals("", ignoreCase = true)) {
                    Utilities.showToast(activity, resources.getString(R.string.fromDate_data))
                } else if (mFromLeaveDayId.equals("", ignoreCase = true)) {
                    Utilities.showToast(activity, resources.getString(R.string.err_leave_type))
                } else if (mToDate.equals("", ignoreCase = true)) {
                    Utilities.showToast(activity, resources.getString(R.string.errToDate))

                } else {
                    if (position == 0) {
                        mToLeaveDayId = "0"
                    } else {
                        mToLeaveType = getLeaveType(parent.getItemAtPosition(position).toString())
                        mToLeaveDayId = leaveTypeDayList.get(position).dayTypeId
                        setDayType("to")
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        leaveTypeSpnrApplyLeave!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mLeaveTypeId = leaveTypeList.get(position).getLeaveTypeId()!!.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        btnSubmitApplyLeave.setOnClickListener({ v ->
            mFromDate = tvFromDateApplyLeave!!.getText().toString().trim()
            mToDate = tvToDateApplyLeave!!.getText().toString().trim()
            mMessage = etReasonApplyLeave.getText().toString().trim({ it <= ' ' })
            if (TextUtils.isEmpty(mFromDate)) {
                Utilities.showToast(activity, resources.getString(R.string.fromDate_data))
                return@setOnClickListener
            } else if (TextUtils.isEmpty(mToDate)) {
                Utilities.showToast(activity, resources.getString(R.string.toDate_data))
                return@setOnClickListener
            } else if (mFromLeaveDayId.equals("", ignoreCase = true) || mFromLeaveDayId.equals("0", ignoreCase = true)) {
                Utilities.showToast(activity, resources.getString(R.string.application_data))
                return@setOnClickListener
            } else if (mToLeaveDayId.equals("", ignoreCase = true) || mToLeaveDayId.equals("0", ignoreCase = true)) {
                Utilities.showToast(activity, resources.getString(R.string.application_data))
                return@setOnClickListener
            } else if (mLeaveTypeId.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errLeaveType))

            } else if (TextUtils.isEmpty(mMessage)) {
                Utilities.showToast(activity, resources.getString(R.string.reason_data))
                return@setOnClickListener
            } else {

                val jsonObject = JSONObject()
                jsonObject.put("LeaveId", "0")

                jsonObject.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
                jsonObject.put("FromDate", mFromDate)
                jsonObject.put("ToDate", mToDate)
                jsonObject.put("LeaveTypeId", mLeaveTypeId)
                jsonObject.put("FromDay", mFromLeaveDayId)
                jsonObject.put("ToDay", mToLeaveDayId)
                jsonObject.put("LeavePurpose", mMessage)
                calledMethod(ConstantsWFMS.APPLY_LEAVE_WFMS, jsonObject.toString())

            }

        })
    }

    fun getCountOfDays(createdDateString: String?, expireDateString: String?): String? {
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        var todayWithZeroTime: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(createdDateString)
            expireCovertedDate = dateFormat.parse(expireDateString)
            val today = Date()
            todayWithZeroTime = dateFormat.parse(dateFormat.format(today))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var cYear = 0
        var cMonth = 0
        var cDay = 0
        if (createdConvertedDate!!.after(todayWithZeroTime)) {
            val cCal = Calendar.getInstance()
            cCal.time = createdConvertedDate
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        } else {
            val cCal = Calendar.getInstance()
            cCal.time = todayWithZeroTime
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        }


        /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */
        val eCal = Calendar.getInstance()
        eCal.time = expireCovertedDate
        val eYear = eCal[Calendar.YEAR]
        val eMonth = eCal[Calendar.MONTH]
        val eDay = eCal[Calendar.DAY_OF_MONTH]
        val date1 = Calendar.getInstance()
        val date2 = Calendar.getInstance()
        date1.clear()
        date1[cYear, cMonth] = cDay
        date2.clear()
        date2[eYear, eMonth] = eDay
        val diff = date2.timeInMillis - date1.timeInMillis
        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
        return "" + (dayCount.toInt() + 1) + " Days"
    }

    private fun dialogCompOff() {
        dialogCompOff = Dialog(activity!!)
        dialogCompOff.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCompOff.setContentView(R.layout.dialog_comp_off)
        dialogCompOff.setCancelable(false)
        dialogCompOff.show()
        calledMethod(LEAVE_TYPE_DAY_WFMS, "")
        val btnSubmitDialogCompOff = dialogCompOff.findViewById(R.id.btnSubmitDialogCompOff) as Button
        val btnCancelDialogCompOff = dialogCompOff.findViewById(R.id.btnCancelDialogCompOff) as Button
        val rlCompOffDateDialogCompOff = dialogCompOff.findViewById(R.id.rlCompOffDateDialogCompOff) as RelativeLayout
        val rlApplyDateDialogCompOff = dialogCompOff.findViewById(R.id.rlApplyDateDialogCompOff) as RelativeLayout
        tvCompOffDateDialogCompOff = dialogCompOff.findViewById(R.id.tvCompOffDateDialogCompOff) as Ubuntu
        tvApplyDateDialogCompOff = dialogCompOff.findViewById(R.id.tvApplyDateDialogCompOff) as Ubuntu
        val etReasonDialogCompOff = dialogCompOff.findViewById(R.id.etReasonDialogCompOff) as UbuntuEditText
        leaveTypeDaySpnrDialogCompOff = dialogCompOff.findViewById(R.id.leaveTypeDaySpnrDialogCompOff) as AppCompatSpinner
        leaveTypeSpnrDialogCompOff = dialogCompOff.findViewById(R.id.leaveTypeSpnrDialogCompOff) as AppCompatSpinner


        btnCancelDialogCompOff.setOnClickListener { view -> dialogCompOff.dismiss() }

        //get current date
        mCurrentDate = DateUtils.currentDate();

        //setting adapter to spinner


        rlCompOffDateDialogCompOff.setOnClickListener(
                { v ->
                    leaveFrom = COMP_OFF_APPLY_DATE
                    val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)


                    leaveFrom = COMP_OFF_DATE

                    val calendar = Calendar.getInstance()
                    calendar.time = calendar.time
                    val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                    val mMonth = calendar.get(Calendar.MONTH)
                    val mYear = calendar.get(Calendar.YEAR)

                    setDateTimeField(mYear, mMonth, mDay, calendar.timeInMillis)
                })


        //  leaveFrom = COMP_OFF_APPLY_DATE
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)


        val calendar = Calendar.getInstance()
        calendar.time = calendar.time
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)

        tvApplyDateDialogCompOff!!.text = Utilities.getCurrentDateInMonth()

        //   setDateTimeField(mYear, mMonth, mDay, calendar.timeInMillis)


        leaveTypeDaySpnrDialogCompOff!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {


                mCompOffLeaveDayTypeId = leaveTypeDayList.get(position).dayTypeId


            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })

        leaveTypeSpnrDialogCompOff!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {


                mCompOffLeaveTypeId = leaveTypeCompOffList.get(position).id


            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })



        btnSubmitDialogCompOff.setOnClickListener({ v ->

            mMessage = etReasonDialogCompOff.getText().toString().trim({ it <= ' ' })
            if (TextUtils.isEmpty(mCompOffDate)) {
                Utilities.showToast(activity, resources.getString(R.string.errCompOffDate))
                return@setOnClickListener
            } else if (TextUtils.isEmpty(mMessage)) {
                Utilities.showToast(activity, resources.getString(R.string.reason_data))
                return@setOnClickListener
            } else {

                val jsonObject = JSONObject()

                jsonObject.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
                jsonObject.put("FromDate", mCompOffDate)
                jsonObject.put("ToDate", mCompOffDate)
                jsonObject.put("LeaveTypeId", mCompOffLeaveTypeId)
                jsonObject.put("FromDay", mCompOffLeaveDayTypeId)
                jsonObject.put("ToDay", mCompOffLeaveDayTypeId)
                jsonObject.put("LeavePurpose", mMessage)
                jsonObject.put("CompoffId", mCompOffId)

                calledMethod(ConstantsWFMS.APPLY_COMP_OFF_WFMS, jsonObject.toString())

            }

        })
    }

    fun sendCompId(compoffId: String) {
        mCompOffId = compoffId
        dialogCompOff()

    }

}