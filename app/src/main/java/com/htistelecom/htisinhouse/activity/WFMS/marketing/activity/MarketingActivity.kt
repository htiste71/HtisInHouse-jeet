package com.htistelecom.htisinhouse.activity.WFMS.marketing.activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MARKETING_ENTITY_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.marketing.MarketingSingleModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Constants
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_marketing.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MarketingActivity : AppCompatActivity(), View.OnClickListener, MyInterface {

    private var sZip: String = ""
    private var sAddress: String = ""
    private var sPhone: String = ""
    private var sEmail: String = ""
    private var sRemarks: String = ""
    private var sCompanyOwnerDetails: String = ""
    private var sCompanyOwnerName: String = ""
    private var sWebstite: String = ""
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


    private var mWidthTurnover: Int = 0
    private var mWidthPosition: Int = 0
    val cal = SimpleDateFormat("HH:mm")
    var mStartTime: Date? = null
    var mEndtTime: Date? = null

    var sStartTime = ""
    var sEndTime = ""

    var alPosition = ArrayList<MarketingSingleModel>()
    var alEntityType = ArrayList<MarketingSingleModel>()
    var alBusiness = ArrayList<MarketingSingleModel>()
    var alTurnover = ArrayList<MarketingSingleModel>()
    var alState = ArrayList<MarketingSingleModel>()
    var alCity = ArrayList<MarketingSingleModel>()
    var projects: Array<String>? = null
    lateinit var listPopupWindow: ListPopupWindow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketing)
        initViews()
        listeners()
        callAPI(ConstantsWFMS.MARKETING_ENTITY_WFMS,"");
    }

    private fun initViews() {
//        tv_title!!.text = "Task Details"
//        ivDrawer!!.visibility = View.GONE
        rlPosition.getViewTreeObserver().addOnGlobalLayoutListener({ mWidthPosition = rlPosition.getMeasuredWidth() })
        rlTurnover.getViewTreeObserver().addOnGlobalLayoutListener({ mWidthTurnover = rlTurnover.getMeasuredWidth() })

    }


    private fun listeners() {
        rlStartTime.setOnClickListener(this)
        rlEndTime.setOnClickListener(this)
        submit_btn.setOnClickListener(this)
        rlPosition.setOnClickListener(this)
        ivEntityType.setOnClickListener(this)
        ivNatureBusiness.setOnClickListener(this)
        rlTurnover.setOnClickListener(this)
        ivState.setOnClickListener(this)
        ivCity.setOnClickListener(this)
        cancel_btn.setOnClickListener(this)

        rgLead.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)

            if (radio.text.equals("Yes,true")) {
                mLead = "Y"
            } else {
                mLead = "N"
            }

        }

        //ivBack!!.setOnClickListener { v -> finish() }


    }

    private fun addTask() {
        sCompanyName = etCompanyName!!.text.toString()
        sPersonName = etpersonName!!.text.toString()
        sWebstite = etWebstite!!.text.toString()
        sCompanyOwnerName = etCompanyOwnerName!!.text.toString()
        sCompanyOwnerDetails = etCompanyOwnerDetails!!.text.toString()
        sRemarks = etRemarks!!.text.toString()
        sEmail = etEmail!!.text.toString()
        sPhone = etPhone!!.text.toString()
        sAddress = etAddress!!.text.toString()
        sZip = etZip!!.text.toString()
        sEndTime = tvOutTime.text.toString()
        if (Utilities.isNetConnected(this@MarketingActivity)) {
            if (TextUtils.isEmpty(sCompanyName)) {
                Utilities.showToast(this@MarketingActivity, "Please enter  company name")
                return
            } else if (TextUtils.isEmpty(sPersonName)) {
                Utilities.showToast(this@MarketingActivity, "Please enter  person Name")
                return
            } else if (mPositionId.equals("")) {
                Utilities.showToast(this@MarketingActivity, "Please enter  position")
                return
            } else if (TextUtils.isEmpty(sWebstite)) {
                Utilities.showToast(this@MarketingActivity, "Please enter your website ")
                return
            } else if (sStartTime.equals("")) {
                Utilities.showToast(this@MarketingActivity, "Please enter In Time ")
                return
            } else if (sEndTime.equals("")) {
                Utilities.showToast(this@MarketingActivity, "Please enter Out Time ")
                return
            } else if (mEntityId.equals("")) {
                Utilities.showToast(this@MarketingActivity, "Please select Entity Type")
                return
            } else if (mBusinessId.equals("")) {
                Utilities.showToast(this@MarketingActivity, "Please select Nature of Business")
                return
            } else if (TextUtils.isEmpty(sCompanyOwnerName)) {
                Utilities.showToast(this@MarketingActivity, "Please enter  company owner name ")
                return
            } else if (TextUtils.isEmpty(sCompanyOwnerDetails)) {
                Utilities.showToast(this@MarketingActivity, "Please enter  company owner contact details ")
                return
            } else if (mTurnoverId.equals("", true)) {
                Utilities.showToast(this@MarketingActivity, "Please select annual turnover")
                return
            } else if (TextUtils.isEmpty(sRemarks)) {
                Utilities.showToast(this@MarketingActivity, "Please enter your remarks ")
                return
            } else if (TextUtils.isEmpty(sEmail)) {
                Utilities.showToast(this@MarketingActivity, "Please enter your email")
                return
            } else if (!Utilities.emailPatterns(sEmail)) {
                Utilities.showToast(this@MarketingActivity, "Email Id is not valid")
                return
            } else if (TextUtils.isEmpty(sPhone)) {
                Utilities.showToast(this@MarketingActivity, "Please enter your phone")
                return
            } else if (sPhone.length < 10) {
                Utilities.showToast(this@MarketingActivity, "Phone number must be 10 digit")
                return
            } else if (TextUtils.isEmpty(sAddress)) {
                Utilities.showToast(this@MarketingActivity, "Please enter your address")
                return
            } else if (mStateId.equals("")) {
                Utilities.showToast(this@MarketingActivity, "Please select the state")
                return
            } else if (mCityId.equals("")) {
                Utilities.showToast(this@MarketingActivity, "Please select the city")
                return
            } else if (TextUtils.isEmpty(sZip)) {
                Utilities.showToast(this@MarketingActivity, "Please enter your zip")
                return
            } else {
                callAPI("post")// hit the Api
            }
        } else {
            Utilities.showToast(this@MarketingActivity, resources.getString(R.string.internet_connection))
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rlStartTime -> {
                val c: Calendar = Calendar.getInstance()
                val mHour = c.get(Calendar.HOUR_OF_DAY)
                val mMinute = c.get(Calendar.MINUTE)

                // Launch Time Picker Dialog
                // Launch Time Picker Dialog

                val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    val tvStartTime = cal.format(cal.parse(hour.toString() + ":" + minute.toString()))
                    tvReachTime.text = tvStartTime
                }, mHour, mMinute, false)


                timePickerDialog.show()

            }
            R.id.rlEndTime -> {

                sStartTime = tvReachTime.text.toString();
                if (sStartTime.equals("")) {
                    Utilities.showToast(this, "Please enter Reach time")
                } else {
                    mStartTime = cal.parse(tvReachTime.text.toString())
                    val c: Calendar = Calendar.getInstance()
                    val mHour = c.get(Calendar.HOUR_OF_DAY)
                    val mMinute = c.get(Calendar.MINUTE)

                    // Launch Time Picker Dialog
                    // Launch Time Picker Dialog

                    val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                        mEndtTime = cal.parse(hour.toString() + ":" + minute)
                        if (mEndtTime!!.before(mStartTime) || mEndtTime!!.equals(mStartTime)) {
                            Utilities.showToast(this, "Out date must be after reach date")
                        } else {
                            val tvEndTime = cal.format(cal.parse(hour.toString() + ":" + minute.toString()))
                            tvOutTime.text = tvEndTime
                        }

                    }, mHour, mMinute, false)


                    timePickerDialog.show()
                }

            }

            R.id.rlPosition -> {

                var list = ArrayList<String>()
                // var items: Array<String> = arrayOf()

                for ((index, value) in alPosition.withIndex()) {
                    list.add(alPosition.get(index).name)
                }
                val positionArray = arrayOfNulls<String>(list.size)
                list.toArray(positionArray)
                showDropdown(positionArray, tvPosition, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mPositionName = mName
                        mPositionId = mId
                        tvPosition.text = mPositionName
                    }
                }, mWidthPosition)
            }
            R.id.ivEntityType -> {
                var list = ArrayList<String>()
                // var items: Array<String> = arrayOf()

                for ((index, value) in alEntityType.withIndex()) {
                    list.add(alEntityType.get(index).name)
                }
                val entityArray = arrayOfNulls<String>(list.size)
                list.toArray(entityArray)
                showDropdown(entityArray, tvEntity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mEntityName = mName
                        mEntityId = mId
                        tvEntity.text = mEntityName
                    }
                }, mWidthPosition)
            }
            R.id.ivNatureBusiness -> {
                var list = ArrayList<String>()
                // var items: Array<String> = arrayOf()

                for ((index, value) in alBusiness.withIndex()) {
                    list.add(alBusiness.get(index).name)
                }
                val businessArray = arrayOfNulls<String>(list.size)
                list.toArray(businessArray)
                showDropdown(businessArray, tvNatureBusiness, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mBusinessName = mName
                        mBusinessId = mId
                        tvNatureBusiness.text = mBusinessName
                    }
                }, mWidthPosition)

            }
            R.id.ivState -> {
                var list = ArrayList<String>()
                // var items: Array<String> = arrayOf()

                for ((index, value) in alState.withIndex()) {
                    list.add(alState.get(index).name)
                }
                val stateArray = arrayOfNulls<String>(list.size)
                list.toArray(stateArray)
                showDropdown(stateArray, tvState, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mStateName = mName
                        mStateId = mId
                        tvState.text = mStateName
                        alCity.clear()
                        mCityId = ""
                        mCityName = ""
                        tvCity.text = "City"
                        callAPI("get_city")
                    }
                }, mWidthPosition)
            }
            R.id.ivCity -> {
                var list = ArrayList<String>()
                // var items: Array<String> = arrayOf()

                for ((index, value) in alCity.withIndex()) {
                    list.add(alCity.get(index).name)
                }
                val cityArray = arrayOfNulls<String>(list.size)
                list.toArray(cityArray)
                showDropdown(cityArray, tvCity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mCityName = mName
                        mCityId = mId
                        tvCity.text = mCityName
                    }
                }, mWidthPosition)

            }
            R.id.rlTurnover -> {
                var list = ArrayList<String>()
                // var items: Array<String> = arrayOf()

                for ((index, value) in alTurnover.withIndex()) {
                    list.add(alTurnover.get(index).name)
                }
                val turnoverArray = arrayOfNulls<String>(list.size)
                list.toArray(turnoverArray)
                showDropdown(turnoverArray, tvTurnover, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mTurnoverName = mName
                        mTurnoverId = mId
                        tvTurnover.text = mTurnoverName
                    }
                }, mWidthTurnover)
            }


            R.id.submit_btn -> {
                addTask()

            }
            R.id.cancel_btn -> {
                finish()
            }
        }
    }

    fun callAPI(TYPE: Int,params:String) {
        if (TYPE==MARKETING_ENTITY_WFMS) {
            ApiData.getMarketingData(MARKETING_ENTITY_WFMS, this)
        } else {
            if (type.equals("get_city")) {
                val jsonObject = JSONObject()
                jsonObject.put("fiStateId", mStateId)
                ApiData.getData(jsonObject.toString(), Constants.FOR_CITY_LIST, this, this)
            } else {
                val jsonObject = JSONObject()
                val db = TinyDB(this)
                jsonObject.put("fvEmplyeeId", db.getString("EmpId"))
                jsonObject.put("fvCompanyName", sCompanyName)
                jsonObject.put("fvEmployeeName", sPersonName)
                jsonObject.put("fiPosition", mPositionId)
                jsonObject.put("fdCustReachTime", sStartTime)
                jsonObject.put("fdCustOutTime", sEndTime)
                jsonObject.put("fiEntityID", mEntityId)
                jsonObject.put("fiBusinessID", mBusinessId)
                jsonObject.put("fvCompanyOwner", sCompanyOwnerName)
                jsonObject.put("fvCompanyOwnerContact", sCompanyOwnerDetails)
                jsonObject.put("fiAnnualTurnoverID", mTurnoverId)
                jsonObject.put("fvRemarks", sRemarks)
                jsonObject.put("fvLeadGenerated", mLead)
                jsonObject.put("fvemailid", sEmail)
                jsonObject.put("fvPhone", sPhone)
                jsonObject.put("fvAddress", sAddress)
                jsonObject.put("fiStateID", mStateId)
                jsonObject.put("fiCityId", mCityId)
                jsonObject.put("fvZipCode", sZip)
                ApiData.getData(jsonObject.toString(), Constants.FOR_MARKETIN_TASK, this, this)
            }
        }
    }

    fun showDropdown(array: Array<String?>, textView: TextView, spinnerData: SpinnerData, width: Int) {
        listPopupWindow = ListPopupWindow(this)
        listPopupWindow.setAdapter(ArrayAdapter(
                this,
                R.layout.row_profile_spinner, array))
        listPopupWindow.setAnchorView(textView)
        listPopupWindow.setWidth(width)
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener({ parent, view, position, id ->
            if (textView.id == R.id.tvPosition) {
                spinnerData.getData(alPosition.get(position).id, alPosition.get(position).name)
            } else if (textView.id == R.id.tvEntity) {
                spinnerData.getData(alEntityType.get(position).id, alEntityType.get(position).name)

            } else if (textView.id == R.id.tvNatureBusiness) {
                spinnerData.getData(alBusiness.get(position).id, alBusiness.get(position).name)

            } else if (textView.id == R.id.tvState) {
                spinnerData.getData(alState.get(position).id, alState.get(position).name)

            } else if (textView.id == R.id.tvCity) {
                spinnerData.getData(alCity.get(position).id, alCity.get(position).name)

            } else if (textView.id == R.id.tvTurnover) {
                spinnerData.getData(alTurnover.get(position).id, alTurnover.get(position).name)

            }

            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        try {
            Utilities.dismissDialog()
            if (TYPE == MARKETING_ENTITY_WFMS) {
                val json = JSONObject((response as Response<*>).body()!!.toString())
                if (json.getString("Status").equals("Success")) {
                    val array=json.getJSONArray("Output")
                    for(i in 0 until array.length())
                    {
                        
                    }


                }
            } else if (TYPE == Constants.FOR_CITY_LIST) {
                val result: String = (response as Response<*>).body().toString()
                val array = JSONArray(result)
                for (data in 0 until array.length()) {
                    val item = array.getJSONObject(data)
                    val modelCity = MarketingSingleModel(item.getString("fiCityId"), item.getString("fvCityName"))
                    alCity.add(modelCity)
                }

            } else if (TYPE == Constants.FOR_MARKETIN_TASK) {
                val result: String = (response as Response<*>).body().toString()
                val json = JSONObject(result)
                if (json.getString("Status").equals("Success", true)) {
                    Utilities.showToast(this@MarketingActivity, json.getString("Message"))
                    finish()
                }
            }

        } catch (e: Exception) {
            e.stackTrace
        }
    }




}