package com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MARKETING_POSITION_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MARKETING_TASK_LIST_WFMS

import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TaskModel
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.adapter.MarketingTaskAdapter
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.font.UbuntuButton
import com.htistelecom.htisinhouse.font.UbuntuEditText
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.CountriesClass
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_task.*
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TaskFragment(var empId: String) : Fragment(), MyInterface {
    private val mWidthPosition: Int = 400
    private var taskList = ArrayList<TaskModel>()
    lateinit var adapter: MarketingTaskAdapter
    lateinit var dialog: Dialog


    private var mAddress: MutableList<Address>? = null
    private val entityList = ArrayList<TwoParameterModel>()
    lateinit var entityArray: Array<String?>

    private val turnoverList = ArrayList<TwoParameterModel>()
    lateinit var turnoverArray: Array<String?>


    private val natureList = ArrayList<TwoParameterModel>()
    lateinit var natureArray: Array<String?>


    private val positionList = ArrayList<TwoParameterModel>()
    lateinit var positionArray: Array<String?>

    private var mCountryId: String = ""
   // lateinit var tinyDB: TinyDB


    private var sZip: String = ""
    private var sAddress: String = ""
    private var sPhone: String = ""
    private var sEmail: String = ""
    private var sRemarks: String = ""
   // private var sCompanyOwnerDetails: String = ""
   // private var sCompanyOwnerName: String = ""
   // private var sWebstite: String = ""
    private var sPersonName: String = ""
    private var sCompanyName: String = ""
    private var mLead: String = "Y"
    private var mTurnoverId: String = ""
    private var mTurnoverName: String = ""
    private var mCityName: String = ""
    private var mCityId: String = ""
    private var mStateId: String = ""
    private var mStateName: String = ""
    private var mBusinessId: String = ""
    private var mBusinessName: String = ""
    private var mEntityId: String = ""
    private var mEntityName: String = ""
    private var mPositionId: String = ""
    private var mPositionName: String = ""


    val cal = SimpleDateFormat("HH:mm")
    var mStartTime: Date? = null
    var mEndtTime: Date? = null

    var sStartTime = ""
    var sEndTime = ""


    lateinit var listPopupWindow: ListPopupWindow


    companion object {
        var isOk = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       // tinyDB = TinyDB(activity)
        rvTaskFragment.layoutManager = LinearLayoutManager(activity)
        ivAddTaskFragment.setOnClickListener { openDialog() }

    }


    override fun sendResponse(response: Any, TYPE: Int) {
        if (TYPE == MARKETING_TASK_LIST_WFMS || TYPE == ConstantsWFMS.COUNTRY_LIST_WFMS || TYPE == ConstantsWFMS.STATE_LIST_WFMS || TYPE == ConstantsWFMS.CITY_LIST_WFMS || TYPE == ConstantsWFMS.MARKETING_TASK_SUBMIT_WFMS)
            Utilities.dismissDialog()
        if (TYPE == ConstantsWFMS.MARKETING_ENTITY_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                val array = json.getJSONArray("Output")
                for (i in 0 until array.length()) {
                    val jsonObjectInner = array.getJSONObject(i)
                    val model = TwoParameterModel()
                    model.id = jsonObjectInner.getString("ID")
                    model.name = jsonObjectInner.getString("EntityName")
                    entityList.add(model)
                }
                entityArray = arrayOfNulls<String>(entityList.size)
                for (i in entityList.indices) {
                    entityArray[i] = entityList.get(i).name
                }


            }
            callAPI(ConstantsWFMS.MARKETING_NATURE_WFMS, "")
        } else if (TYPE == ConstantsWFMS.MARKETING_POSITION_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                val array = json.getJSONArray("Output")
                for (i in 0 until array.length()) {
                    val jsonObjectInner = array.getJSONObject(i)
                    val model = TwoParameterModel()
                    model.id = jsonObjectInner.getString("ID")
                    model.name = jsonObjectInner.getString("PositionName")
                    positionList.add(model)
                }
                positionArray = arrayOfNulls<String>(positionList.size)
                for (i in positionList.indices) {
                    positionArray[i] = positionList.get(i).name
                }
            }
            callAPI(ConstantsWFMS.MARKETING_ENTITY_WFMS, "")


        } else if (TYPE == ConstantsWFMS.MARKETING_NATURE_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                val array = json.getJSONArray("Output")
                for (i in 0 until array.length()) {
                    val jsonObjectInner = array.getJSONObject(i)
                    val model = TwoParameterModel()
                    model.id = jsonObjectInner.getString("ID")
                    model.name = jsonObjectInner.getString("NatureName")
                    natureList.add(model)
                }
                natureArray = arrayOfNulls<String>(natureList.size)
                for (i in natureList.indices) {
                    natureArray[i] = natureList.get(i).name
                }
            }
            callAPI(ConstantsWFMS.MARKETING_TURNOVER_WFMS, "")


        } else if (TYPE == ConstantsWFMS.MARKETING_TURNOVER_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                val array = json.getJSONArray("Output")
                for (i in 0 until array.length()) {
                    val jsonObjectInner = array.getJSONObject(i)
                    val model = TwoParameterModel()
                    model.id = jsonObjectInner.getString("ID")
                    model.name = jsonObjectInner.getString("AnnualTurnover")
                    turnoverList.add(model)
                }
                turnoverArray = arrayOfNulls<String>(turnoverList.size)
                for (i in turnoverList.indices) {
                    turnoverArray[i] = turnoverList.get(i).name
                }
            }


            callAPI(ConstantsWFMS.COUNTRY_LIST_WFMS, "")


        } else if (TYPE == ConstantsWFMS.COUNTRY_LIST_WFMS) {
            CountriesClass.commonMethod(response, ConstantsWFMS.COUNTRY_LIST_WFMS)
        } else if (TYPE == ConstantsWFMS.STATE_LIST_WFMS) {
            CountriesClass.commonMethod(response, ConstantsWFMS.STATE_LIST_WFMS)

        } else if (TYPE == ConstantsWFMS.CITY_LIST_WFMS) {
            CountriesClass.commonMethod(response, ConstantsWFMS.CITY_LIST_WFMS)

        } else if (TYPE == ConstantsWFMS.MARKETING_TASK_SUBMIT_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                Utilities.showToast(activity, json.getString("Message"))
                dialog.dismiss()
                val date = ConstantKotlin.getCurrentDate()
                val jsonObject = JSONObject()

                jsonObject.put("EmpId", empId)
                jsonObject.put("TaskDate", date)
                callAPI(ConstantsWFMS.MARKETING_TASK_LIST_WFMS, jsonObject.toString())

            } else {
                Utilities.showToast(activity, json.getString("Message"))

            }
        } else if (TYPE == MARKETING_TASK_LIST_WFMS) {
            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObject.getString("Status").equals("Success")) {
                taskList = Gson().fromJson<java.util.ArrayList<TaskModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<TaskModel>>() {

                }.type)

                rvTaskFragment.visibility = View.VISIBLE
                tvNoTaskFragment.visibility = View.GONE
                adapter = MarketingTaskAdapter(activity!!, taskList)
                rvTaskFragment.adapter = adapter
            } else {
                rvTaskFragment.visibility = View.GONE
                tvNoTaskFragment.visibility = View.VISIBLE
            }
        }


    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser)
            Handler().postDelayed({
                if (activity != null) {
                    val date = ConstantKotlin.getCurrentDate()
                    val jsonObject = JSONObject()

                    jsonObject.put("EmpId", empId)
                    jsonObject.put("TaskDate", date)
                    callAPI(ConstantsWFMS.MARKETING_TASK_LIST_WFMS, jsonObject.toString())
                }
            }, 200)
        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun callAPI(TYPE: Int, params: String) {
        if (TYPE == ConstantsWFMS.MARKETING_TASK_LIST_WFMS) {
            ApiData.getData(params, MARKETING_TASK_LIST_WFMS, this, activity)
        } else if (TYPE == ConstantsWFMS.MARKETING_POSITION_WFMS) {
            ApiData.getMarketingData(ConstantsWFMS.MARKETING_POSITION_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.MARKETING_ENTITY_WFMS) {
            ApiData.getMarketingData(ConstantsWFMS.MARKETING_ENTITY_WFMS, this, activity)
        } else if (TYPE == ConstantsWFMS.MARKETING_NATURE_WFMS) {
            ApiData.getMarketingData(ConstantsWFMS.MARKETING_NATURE_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.MARKETING_TURNOVER_WFMS) {
            ApiData.getMarketingData(ConstantsWFMS.MARKETING_TURNOVER_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.COUNTRY_LIST_WFMS) {
            ApiData.getMarketingData(ConstantsWFMS.COUNTRY_LIST_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.STATE_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.STATE_LIST_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.CITY_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.CITY_LIST_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.MARKETING_TASK_SUBMIT_WFMS) {
            ApiData.getData(params, ConstantsWFMS.MARKETING_TASK_SUBMIT_WFMS, this, activity)

        }

    }


    fun openDialog() {

        dialog = Dialog(activity!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.activity_marketing)
        dialog!!.setCancelable(false)
        dialog!!.show()

        callAPI(MARKETING_POSITION_WFMS, "")
        mAddress = Utilities.getAddressFromLatLong(activity, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())
        sAddress = mAddress!!.get(0).getAddressLine(0).toString()
        val ivBack = dialog.findViewById<ImageView>(R.id.ivBack)
        val tv_title = dialog.findViewById<Ubuntu>(R.id.tv_title)
        val ivDrawer = dialog.findViewById<ImageView>(R.id.ivDrawer)

        ivDrawer.visibility = View.GONE
        tv_title.text = "Add Task Details"
        ivBack.setOnClickListener { dialog.dismiss() }


        val etCompanyNameMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etCompanyNameMarketingActivity)
        val etPersonNameMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etPersonNameMarketingActivity)
        val etRemarksMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etRemarksMarketingActivity)


        val rlPositionMarketingActivity = dialog.findViewById<RelativeLayout>(R.id.rlPositionMarketingActivity)
        val ivPositionMarketingActivity = dialog.findViewById<ImageView>(R.id.ivPositionMarketingActivity)
        val tvPositionMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvPositionMarketingActivity)

      //  val etWebstiteMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etWebstiteMarketingActivity)

        val rlStartTimeMarketingActivity = dialog.findViewById<RelativeLayout>(R.id.rlStartTimeMarketingActivity)
        val tvReachTimeMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvReachTimeMarketingActivity)

        val rlEndTimeMarketingActivity = dialog.findViewById<RelativeLayout>(R.id.rlEndTimeMarketingActivity)
        val tvOutTimeMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvOutTimeMarketingActivity)

        val ivEntityTypeMarketingActivity = dialog.findViewById<ImageView>(R.id.ivEntityTypeMarketingActivity)
        val tvEntityMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvEntityMarketingActivity)

        val ivNatureBusinessMarketingActivity = dialog.findViewById<ImageView>(R.id.ivNatureBusinessMarketingActivity)
        val tvNatureBusinessMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvNatureBusinessMarketingActivity)

     //   val etCompanyOwnerNameMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etCompanyOwnerNameMarketingActivity)
      //  val etCompanyOwnerDetailsMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etCompanyOwnerDetailsMarketingActivity)

        val rlTurnoverMarketingActivity = dialog.findViewById<RelativeLayout>(R.id.rlTurnoverMarketingActivity)
        val tvTurnoverMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvTurnoverMarketingActivity)

        val tvLeadMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvLeadMarketingActivity)

        val rgLeadMarketingActivity = dialog.findViewById<RadioGroup>(R.id.rgLeadMarketingActivity)

        val etEmailMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etEmailMarketingActivity)
        val etPhoneMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etPhoneMarketingActivity)
        val etAddressMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etAddressMarketingActivity)

        val ivCountryMarketingActivity = dialog.findViewById<ImageView>(R.id.ivCountryMarketingActivity)
        val tvCountryMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvCountryMarketingActivity)
        val ivStateMarketingActivity = dialog.findViewById<ImageView>(R.id.ivStateMarketingActivity)
        val tvStateMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvStateMarketingActivity)
        val ivCityMarketingActivity = dialog.findViewById<ImageView>(R.id.ivCityMarketingActivity)
        val tvCityMarketingActivity = dialog.findViewById<Ubuntu>(R.id.tvCityMarketingActivity)
        val etZipMarketingActivity = dialog.findViewById<UbuntuEditText>(R.id.etZipMarketingActivity)
        val btnSubmitMarketingActivity = dialog.findViewById<UbuntuButton>(R.id.btnSubmitMarketingActivity)
        val btnCancelMarketingActivity = dialog.findViewById<UbuntuButton>(R.id.btnCancelMarketingActivity)
        etAddressMarketingActivity.setText(sAddress)
        btnCancelMarketingActivity.setOnClickListener { dialog.dismiss() }
        rgLeadMarketingActivity.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = dialog.findViewById(checkedId)

            if (radio.text.equals("Yes,true")) {
                mLead = "Y"
            } else {
                mLead = "N"
            }

        }


        rlStartTimeMarketingActivity.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mHour = c.get(Calendar.HOUR_OF_DAY)
            val mMinute = c.get(Calendar.MINUTE)


            val timePickerDialog = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                val tvStartTime = cal.format(cal.parse(hour.toString() + ":" + minute.toString()))
                tvReachTimeMarketingActivity.text = tvStartTime
            }, mHour, mMinute, false)


            timePickerDialog.show()

        }
        rlEndTimeMarketingActivity.setOnClickListener {

            sStartTime = tvReachTimeMarketingActivity.text.toString();
            if (sStartTime.equals("")) {
                Utilities.showToast(activity, "Please enter Reach time")
            } else {
                mStartTime = cal.parse(tvReachTimeMarketingActivity.text.toString())
                val c: Calendar = Calendar.getInstance()
                val mHour = c.get(Calendar.HOUR_OF_DAY)
                val mMinute = c.get(Calendar.MINUTE)


                val timePickerDialog = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                    mEndtTime = cal.parse(hour.toString() + ":" + minute)
                    if (mEndtTime!!.before(mStartTime) || mEndtTime!!.equals(mStartTime)) {
                        Utilities.showToast(activity, "Out date must be after reach date")
                    } else {
                        val tvEndTime = cal.format(cal.parse(hour.toString() + ":" + minute.toString()))
                        tvOutTimeMarketingActivity.text = tvEndTime
                    }

                }, mHour, mMinute, false)


                timePickerDialog.show()
            }

        }

        rlPositionMarketingActivity.setOnClickListener {


            showDropdown(positionArray, tvPositionMarketingActivity, object : SpinnerData {
                override fun getData(mId: String, mName: String) {
                    mPositionName = mName
                    mPositionId = mId
                    tvPositionMarketingActivity.text = mPositionName
                }
            }, mWidthPosition)
        }

        ivEntityTypeMarketingActivity.setOnClickListener {

            showDropdown(entityArray, tvEntityMarketingActivity, object : SpinnerData {
                override fun getData(mId: String, mName: String) {
                    mEntityId = mId
                }
            }, mWidthPosition)
        }
        ivNatureBusinessMarketingActivity.setOnClickListener {

            showDropdown(natureArray, tvNatureBusinessMarketingActivity, object : SpinnerData {
                override fun getData(mId: String, mName: String) {
                    mBusinessId = mId
                }
            }, mWidthPosition)

        }
        ivStateMarketingActivity.setOnClickListener {

            if (mCountryId.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errSelectCountry))
            } else {
                showDropdown(CountriesClass.stateArray, tvStateMarketingActivity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mStateId = mId

                        mCityId = ""
                        tvCityMarketingActivity.text = "City"
                        val json = JSONObject()
                        json.put("StateId", mStateId)
                        callAPI(ConstantsWFMS.CITY_LIST_WFMS, json.toString())
                    }
                }, mWidthPosition)
            }

        }
        ivCountryMarketingActivity.setOnClickListener {
            showDropdown(CountriesClass.countryArray, tvCountryMarketingActivity, object : SpinnerData {
                override fun getData(mId: String, mName: String) {
                    mCountryId = mId

                    mStateId = ""
                    mCityId = ""
                    tvStateMarketingActivity.text = "State"
                    tvCityMarketingActivity.text = "City"


                    val jsonObject = JSONObject()
                    jsonObject.put("CountryId", mCountryId)
                    callAPI(ConstantsWFMS.STATE_LIST_WFMS, jsonObject.toString())


                }
            }, mWidthPosition)
        }
        ivCityMarketingActivity.setOnClickListener {
            if (mStateId.equals("")) {
                Utilities.showToast(activity, resources.getString(R.string.errSelectState))
            } else {
                showDropdown(CountriesClass.cityArray, tvCityMarketingActivity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mCityId = mId
                    }
                }, mWidthPosition)
            }

        }
        rlTurnoverMarketingActivity.setOnClickListener {

            showDropdown(turnoverArray, tvTurnoverMarketingActivity, object : SpinnerData {
                override fun getData(mId: String, mName: String) {
                    mTurnoverName = mName
                    mTurnoverId = mId
                    tvTurnoverMarketingActivity.text = mTurnoverName
                }
            }, 800)
        }

        btnSubmitMarketingActivity.setOnClickListener {

            sCompanyName = etCompanyNameMarketingActivity!!.text.toString()
            sPersonName = etPersonNameMarketingActivity!!.text.toString()
          //  sWebstite = etWebstiteMarketingActivity!!.text.toString()
           // sCompanyOwnerName = etCompanyOwnerNameMarketingActivity!!.text.toString()
           // sCompanyOwnerDetails = etCompanyOwnerDetailsMarketingActivity!!.text.toString()
            sRemarks = etRemarksMarketingActivity!!.text.toString()
            sEmail = etEmailMarketingActivity!!.text.toString()
            sPhone = etPhoneMarketingActivity!!.text.toString()
            sAddress = etAddressMarketingActivity!!.text.toString()
            sZip = etZipMarketingActivity!!.text.toString()
            sEndTime = tvOutTimeMarketingActivity.text.toString()
            sStartTime = tvReachTimeMarketingActivity.text.toString();

//            sCompanyName = "test"
//            sPersonName = "test"
//            sWebstite = "test"
//            sCompanyOwnerName = "test"
//            sCompanyOwnerDetails = "1234567890"
//            sRemarks = "test"
//            sEmail = "test@gmail.com"
//            sPhone = "1234567890"
//            sAddress = "test"
//            sPersonName = "test"
//            sZip = "123456"
//            sEndTime = "12:00"
//            sStartTime = "11:00"
//            mTurnoverId = "1"
//            mBusinessId = "1"
//            mStateId = "1"
//            mCityId = "1"
//            mEntityId = "1"
//            mPositionId = "1"


//            if (TextUtils.isEmpty(sCompanyName)) {
//                Utilities.showToast(activity, "Please enter  company name")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sPersonName)) {
//                Utilities.showToast(activity, "Please enter  person Name")
//                return@setOnClickListener
//            } else if (mPositionId.equals("")) {
//                Utilities.showToast(activity, "Please enter  position")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sWebstite)) {
//                Utilities.showToast(activity, "Please enter your website ")
//                return@setOnClickListener
//            } else if (sStartTime.equals("")) {
//                Utilities.showToast(activity, "Please enter In Time ")
//                return@setOnClickListener
//            } else if (sEndTime.equals("")) {
//                Utilities.showToast(activity, "Please enter Out Time ")
//                return@setOnClickListener
//            } else if (mEntityId.equals("")) {
//                Utilities.showToast(activity, "Please select Entity Type")
//                return@setOnClickListener
//            } else if (mBusinessId.equals("")) {
//                Utilities.showToast(activity, "Please select Nature of Business")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sCompanyOwnerName)) {
//                Utilities.showToast(activity, "Please enter  company owner name ")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sCompanyOwnerDetails)) {
//                Utilities.showToast(activity, "Please enter  company owner contact details ")
//                return@setOnClickListener
//            } else if (mTurnoverId.equals("", true)) {
//                Utilities.showToast(activity, "Please select annual turnover")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sRemarks)) {
//                Utilities.showToast(activity, "Please enter your remarks ")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sEmail)) {
//                Utilities.showToast(activity, "Please enter your email")
//                return@setOnClickListener
//            } else if (!Utilities.emailPatterns(sEmail)) {
//                Utilities.showToast(activity, "Email Id is not valid")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sPhone)) {
//                Utilities.showToast(activity, "Please enter your phone")
//                return@setOnClickListener
//            } else if (sPhone.length < 10) {
//                Utilities.showToast(activity, "Phone number must be 10 digit")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sAddress)) {
//                Utilities.showToast(activity, "Please enter your address")
//                return@setOnClickListener
//            } else if (mCountryId.equals("")) {
//                Utilities.showToast(activity, resources.getString(R.string.errSelectCountry))
//                return@setOnClickListener
//            } else if (mStateId.equals("")) {
//                Utilities.showToast(activity, "Please select the state")
//                return@setOnClickListener
//            } else if (mCityId.equals("")) {
//                Utilities.showToast(activity, "Please select the city")
//                return@setOnClickListener
//            } else if (TextUtils.isEmpty(sZip)) {
//                Utilities.showToast(activity, "Please enter your zip")
//                return@setOnClickListener
//            } else {
                val date = ConstantKotlin.getCurrentDate()
                val json = JSONObject()
                json.put("EmpId", empId)
                json.put("TaskDate", date)
                json.put("CompanyName", sCompanyName)
                json.put("CustReachTime", sStartTime)
                json.put("CustOutTime", sEndTime)
                json.put("EmployeeName", sPersonName)
                json.put("Position", mPositionId)

                json.put("WebLink", "")
                json.put("EntityID", mEntityId)
                json.put("BusinessID", mBusinessId)
                json.put("CompanyOwner", "")

                json.put("CompanyOwnerContact", "")
                json.put("AnnualTurnoverID", mTurnoverId)
                json.put("BusinessID", mBusinessId)
                json.put("Remarks", sRemarks)

                json.put("LeadGenerated", mLead)
                json.put("EmailID", sEmail)
                json.put("StateID", mStateId)
                json.put("CityId", mCityId)

                //  json.put("CountryId",mCountryId)
                json.put("Address", sAddress)
                json.put("Phone", sPhone)
                json.put("ZipCode", sZip)
                json.put("Latitude", OreoLocationService.LATITUDE)
                json.put("longitude", OreoLocationService.LONGITUDE)
                json.put("TasktypeID", "3")
                callAPI(ConstantsWFMS.MARKETING_TASK_SUBMIT_WFMS, json.toString())

         //   }
        }


    }


    fun showDropdown(array: Array<String?>, textView: TextView, spinnerData: SpinnerData, width: Int) {
        var name = ""
        listPopupWindow = ListPopupWindow(activity!!)
        listPopupWindow.setAdapter(ArrayAdapter(
                activity!!,
                R.layout.row_profile_spinner, array))
        listPopupWindow.setAnchorView(textView)
        listPopupWindow.setWidth(width)
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener({ parent, view, position, id ->
            if (textView.id == R.id.tvPositionMarketingActivity) {
                name = positionList.get(position).name
                spinnerData.getData(positionList.get(position).id, positionList.get(position).name)

            } else if (textView.id == R.id.tvEntityMarketingActivity) {
                name = entityList.get(position).name

                spinnerData.getData(entityList.get(position).id, entityList.get(position).name)

            } else if (textView.id == R.id.tvNatureBusinessMarketingActivity) {
                spinnerData.getData(natureList.get(position).id, natureList.get(position).name)
                name = natureList.get(position).name
            } else if (textView.id == R.id.tvCountryMarketingActivity) {
                name = CountriesClass.countryList.get(position).name
                spinnerData.getData(CountriesClass.countryList.get(position).id, CountriesClass.countryList.get(position).name)

            } else if (textView.id == R.id.tvStateMarketingActivity) {
                name = CountriesClass.stateList.get(position).name
                spinnerData.getData(CountriesClass.stateList.get(position).id, CountriesClass.stateList.get(position).name)

            } else if (textView.id == R.id.tvCityMarketingActivity) {
                name = CountriesClass.cityList.get(position).name
                spinnerData.getData(CountriesClass.cityList.get(position).id, CountriesClass.cityList.get(position).name)

            } else if (textView.id == R.id.tvTurnoverMarketingActivity) {
                name = turnoverList.get(position).name
                spinnerData.getData(turnoverList.get(position).id, turnoverList.get(position).name)

            }
            textView.text = name

            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }
}

