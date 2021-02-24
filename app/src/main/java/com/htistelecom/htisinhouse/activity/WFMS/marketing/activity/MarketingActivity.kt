package com.htistelecom.htisinhouse.activity.WFMS.marketing.activity

import android.app.TimePickerDialog
import android.location.Address
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.AddTask
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.marketing.MarketingSingleModel
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.TaskFragment
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.*
import kotlinx.android.synthetic.main.activity_marketing.*
import kotlinx.android.synthetic.main.fragment_add_project.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MarketingActivity : AppCompatActivity(), View.OnClickListener, MyInterface {

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
    lateinit var tinyDB: TinyDB


    private var sZip: String = ""
    private var sAddress: String = ""
    private var sPhone: String = ""
    private var sEmail: String = ""
    private var sRemarks: String = ""
 //   private var sCompanyOwnerDetails: String = ""
  //  private var sCompanyOwnerName: String = ""
 //   private var sWebstite: String = ""
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


    lateinit var listPopupWindow: ListPopupWindow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketing)
        initViews()
        listeners()
        callAPI(ConstantsWFMS.MARKETING_POSITION_WFMS, "");
    }

    private fun initViews() {
        tv_title!!.text = "Task Details"
        ivDrawer!!.visibility = View.GONE
        tinyDB = TinyDB(this)
        rlPositionMarketingActivity.getViewTreeObserver().addOnGlobalLayoutListener({ mWidthPosition = rlPositionMarketingActivity.getMeasuredWidth() })
        rlTurnoverMarketingActivity.getViewTreeObserver().addOnGlobalLayoutListener({ mWidthTurnover = rlTurnoverMarketingActivity.getMeasuredWidth() })
        mAddress = Utilities.getAddressFromLatLong(this, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())
        sAddress = mAddress!!.get(0).getAddressLine(0).toString()
        etAddressMarketingActivity.setText(sAddress)
    }


    private fun listeners() {
        rlStartTimeMarketingActivity.setOnClickListener(this)
        rlEndTimeMarketingActivity.setOnClickListener(this)
        btnSubmitMarketingActivity.setOnClickListener(this)
        rlPositionMarketingActivity.setOnClickListener(this)
        ivEntityTypeMarketingActivity.setOnClickListener(this)
        ivNatureBusinessMarketingActivity.setOnClickListener(this)
        rlTurnoverMarketingActivity.setOnClickListener(this)
        ivStateMarketingActivity.setOnClickListener(this)
        ivCityMarketingActivity.setOnClickListener(this)
        ivCountryMarketingActivity.setOnClickListener(this)
        btnCancelMarketingActivity.setOnClickListener(this)
        ivBack.setOnClickListener { finish()
        }
        rgLeadMarketingActivity.setOnCheckedChangeListener { group, checkedId ->
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
        sCompanyName = etCompanyNameMarketingActivity!!.text.toString()
        sPersonName = etPersonNameMarketingActivity!!.text.toString()
//        sWebstite = etWebstiteMarketingActivity!!.text.toString()
//        sCompanyOwnerName = etCompanyOwnerNameMarketingActivity!!.text.toString()
//        sCompanyOwnerDetails = etCompanyOwnerDetailsMarketingActivity!!.text.toString()
        sRemarks = etRemarksMarketingActivity!!.text.toString()
        sEmail = etEmailMarketingActivity!!.text.toString()
        sPhone = etPhoneMarketingActivity!!.text.toString()
        sAddress = etAddressMarketingActivity!!.text.toString()
        sZip = etZipMarketingActivity!!.text.toString()
        sEndTime = tvOutTimeMarketingActivity.text.toString()
        sStartTime = tvReachTimeMarketingActivity.text.toString();

//        sCompanyName="test"
//        sPersonName="test"
//        sWebstite="test"
//        sCompanyOwnerName="test"
//        sCompanyOwnerDetails="1234567890"
//        sRemarks="test"
//        sEmail="test@gmail.com"
//        sPhone="1234567890"
//        sAddress="test"
//        sPersonName="test"
//        sZip="123456"
//        sEndTime="12:00"
//        sStartTime="11:00"
//        mTurnoverId="1"
//        mBusinessId="1"
//        mStateId="1"
//        mCityId="1"
//        mEntityId="1"
//        mPositionId="1"

//        if (Utilities.isNetConnected(this@MarketingActivity)) {
//            if (TextUtils.isEmpty(sCompanyName)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter  company name")
//                return
//            } else if (TextUtils.isEmpty(sPersonName)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter  person Name")
//                return
//            } else if (mPositionId.equals("")) {
//                Utilities.showToast(this@MarketingActivity, "Please enter  position")
//                return
//            } else if (TextUtils.isEmpty(sWebstite)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter your website ")
//                return
//            } else if (sStartTime.equals("")) {
//                Utilities.showToast(this@MarketingActivity, "Please enter In Time ")
//                return
//            } else if (sEndTime.equals("")) {
//                Utilities.showToast(this@MarketingActivity, "Please enter Out Time ")
//                return
//            } else if (mEntityId.equals("")) {
//                Utilities.showToast(this@MarketingActivity, "Please select Entity Type")
//                return
//            } else if (mBusinessId.equals("")) {
//                Utilities.showToast(this@MarketingActivity, "Please select Nature of Business")
//                return
//            } else if (TextUtils.isEmpty(sCompanyOwnerName)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter  company owner name ")
//                return
//            } else if (TextUtils.isEmpty(sCompanyOwnerDetails)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter  company owner contact details ")
//                return
//            } else if (mTurnoverId.equals("", true)) {
//                Utilities.showToast(this@MarketingActivity, "Please select annual turnover")
//                return
//            } else if (TextUtils.isEmpty(sRemarks)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter your remarks ")
//                return
//            } else if (TextUtils.isEmpty(sEmail)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter your email")
//                return
//            } else if (!Utilities.emailPatterns(sEmail)) {
//                Utilities.showToast(this@MarketingActivity, "Email Id is not valid")
//                return
//            } else if (TextUtils.isEmpty(sPhone)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter your phone")
//                return
//            } else if (sPhone.length < 10) {
//                Utilities.showToast(this@MarketingActivity, "Phone number must be 10 digit")
//                return
//            } else if (TextUtils.isEmpty(sAddress)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter your address")
//                return
//            } else if (mCountryId.equals("")) {
//                Utilities.showToast(this@MarketingActivity, resources.getString(R.string.errSelectCountry))
//                return
//            } else if (mStateId.equals("")) {
//                Utilities.showToast(this@MarketingActivity, "Please select the state")
//                return
//            } else if (mCityId.equals("")) {
//                Utilities.showToast(this@MarketingActivity, "Please select the city")
//                return
//            } else if (TextUtils.isEmpty(sZip)) {
//                Utilities.showToast(this@MarketingActivity, "Please enter your zip")
//                return
//            } else {
//                val date = ConstantKotlin.getCurrentDate()
//                val json = JSONObject()
//                json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
//                json.put("TaskDate", date)
//                json.put("CompanyName", sCompanyName)
//                json.put("CustReachTime", sStartTime)
//                json.put("CustOutTime", sEndTime)
//                json.put("EmployeeName", sPersonName)
//                json.put("Position", mPositionId)
//
//                json.put("WebLink", sWebstite)
//                json.put("EntityID", mEntityId)
//                json.put("BusinessID", mBusinessId)
//                json.put("CompanyOwner", sCompanyOwnerName)
//
//                json.put("CompanyOwnerContact", sCompanyOwnerDetails)
//                json.put("AnnualTurnoverID", mTurnoverId)
//                json.put("BusinessID", mBusinessId)
//                json.put("Remarks", sRemarks)
//
//                json.put("LeadGenerated", sWebstite)
//                json.put("EmailID", sEmail)
//                json.put("StateID", mStateId)
//                json.put("CityId", mCityId)
//
//                //  json.put("CountryId",mCountryId)
//                json.put("Address", sAddress)
//                json.put("Phone", sPhone)
//                json.put("ZipCode", sZip)
//                json.put("Latitude", OreoLocationService.LATITUDE)
//                json.put("longitude", OreoLocationService.LONGITUDE)
//                json.put("TasktypeID", "3")
//                callAPI(MARKETING_TASK_SUBMIT_WFMS, json.toString())
//
//
//            }
//        } else {
//            Utilities.showToast(this@MarketingActivity, resources.getString(R.string.internet_connection))
//        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rlStartTimeMarketingActivity -> {
                val c: Calendar = Calendar.getInstance()
                val mHour = c.get(Calendar.HOUR_OF_DAY)
                val mMinute = c.get(Calendar.MINUTE)


                val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    val tvStartTime = cal.format(cal.parse(hour.toString() + ":" + minute.toString()))
                    tvReachTimeMarketingActivity.text = tvStartTime
                }, mHour, mMinute, false)


                timePickerDialog.show()

            }
            R.id.rlEndTimeMarketingActivity -> {

                sStartTime = tvReachTimeMarketingActivity.text.toString();
                if (sStartTime.equals("")) {
                    Utilities.showToast(this, "Please enter Reach time")
                } else {
                    mStartTime = cal.parse(tvReachTimeMarketingActivity.text.toString())
                    val c: Calendar = Calendar.getInstance()
                    val mHour = c.get(Calendar.HOUR_OF_DAY)
                    val mMinute = c.get(Calendar.MINUTE)


                    val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                        mEndtTime = cal.parse(hour.toString() + ":" + minute)
                        if (mEndtTime!!.before(mStartTime) || mEndtTime!!.equals(mStartTime)) {
                            Utilities.showToast(this, "Out date must be after reach date")
                        } else {
                            val tvEndTime = cal.format(cal.parse(hour.toString() + ":" + minute.toString()))
                            tvOutTimeMarketingActivity.text = tvEndTime
                        }

                    }, mHour, mMinute, false)


                    timePickerDialog.show()
                }

            }

            R.id.rlPositionMarketingActivity -> {


                showDropdown(positionArray, tvPositionMarketingActivity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mPositionName = mName
                        mPositionId = mId
                        tvPositionMarketingActivity.text = mPositionName
                    }
                }, mWidthPosition)
            }
            R.id.ivEntityTypeMarketingActivity -> {

                showDropdown(entityArray, tvEntityMarketingActivity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mEntityName = mName
                        mEntityId = mId
                        tvEntityMarketingActivity.text = mEntityName
                    }
                }, mWidthPosition)
            }
            R.id.ivNatureBusinessMarketingActivity -> {

                showDropdown(natureArray, tvNatureBusinessMarketingActivity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mBusinessName = mName
                        mBusinessId = mId
                        tvNatureBusinessMarketingActivity.text = mBusinessName
                    }
                }, mWidthPosition)

            }


            R.id.ivCountryMarketingActivity -> {
                showDropdown(CountriesClass.countryArray, tvCountryMarketingActivity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mCountryId = mId

                        mStateId = ""
                        mCityId = ""
                        tvStateMarketingActivity.text = "State"
                        tvCityMarketingActivity.text = "City"


                        val jsonObject = JSONObject()
                        jsonObject.put("CountryId", mCountryId)
                        callAPI(STATE_LIST_WFMS, jsonObject.toString())


                    }
                }, mWidthPosition)
            }

            R.id.ivStateMarketingActivity -> {

                if (mCountryId.equals("")) {
                    Utilities.showToast(this, resources.getString(R.string.errSelectCountry))
                } else {
                    showDropdown(CountriesClass.stateArray, tvStateMarketingActivity, object : SpinnerData {
                        override fun getData(mId: String, mName: String) {
                            mStateId = mId

                            mCityId = ""
                            tvCityMarketingActivity.text = "City"
                            val json = JSONObject()
                            json.put("StateId", mStateId)
                            callAPI(CITY_LIST_WFMS, json.toString())
                        }
                    }, mWidthPosition)
                }

            }
            R.id.ivCityMarketingActivity -> {
                if (mStateId.equals("")) {
                    Utilities.showToast(this, resources.getString(R.string.errSelectState))
                } else {
                    showDropdown(CountriesClass.cityArray, tvCityMarketingActivity, object : SpinnerData {
                        override fun getData(mId: String, mName: String) {

                            mCityId = mId
                        }
                    }, mWidthPosition)
                }

            }
            R.id.rlTurnoverMarketingActivity -> {

                showDropdown(turnoverArray, tvTurnoverMarketingActivity, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {
                        mTurnoverName = mName
                        mTurnoverId = mId
                        tvTurnoverMarketingActivity.text = mTurnoverName
                    }
                }, mWidthTurnover)
            }


            R.id.btnSubmitMarketingActivity -> {
                addTask()

            }
            R.id.btnCancelMarketingActivity -> {
                finish()
            }
        }
    }

    fun callAPI(TYPE: Int, params: String) {


        if (TYPE == MARKETING_POSITION_WFMS) {
            ApiData.getMarketingData(MARKETING_POSITION_WFMS, this, this)

        } else if (TYPE == MARKETING_ENTITY_WFMS) {
            ApiData.getMarketingData(MARKETING_ENTITY_WFMS, this, this)
        } else if (TYPE == MARKETING_NATURE_WFMS) {
            ApiData.getMarketingData(MARKETING_NATURE_WFMS, this, this)

        } else if (TYPE == MARKETING_TURNOVER_WFMS) {
            ApiData.getMarketingData(MARKETING_TURNOVER_WFMS, this, this)

        } else if (TYPE == COUNTRY_LIST_WFMS) {
            ApiData.getMarketingData(COUNTRY_LIST_WFMS, this, this)

        } else if (TYPE == STATE_LIST_WFMS) {
            ApiData.getData(params, STATE_LIST_WFMS, this, this)

        } else if (TYPE == CITY_LIST_WFMS) {
            ApiData.getData(params, CITY_LIST_WFMS, this, this)

        } else if (TYPE == MARKETING_TASK_SUBMIT_WFMS) {
            ApiData.getData(params, MARKETING_TASK_SUBMIT_WFMS, this, this)

        }
    }


    override fun sendResponse(response: Any?, TYPE: Int) {
        if (TYPE == COUNTRY_LIST_WFMS || TYPE == STATE_LIST_WFMS || TYPE == CITY_LIST_WFMS || TYPE == MARKETING_TASK_SUBMIT_WFMS)
            Utilities.dismissDialog()
        if (TYPE == MARKETING_ENTITY_WFMS) {
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
            callAPI(MARKETING_NATURE_WFMS, "")
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
            callAPI(MARKETING_ENTITY_WFMS, "")


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
            callAPI(MARKETING_TURNOVER_WFMS, "")


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


        } else if (TYPE == COUNTRY_LIST_WFMS) {
            CountriesClass.commonMethod(response, ConstantsWFMS.COUNTRY_LIST_WFMS)
        } else if (TYPE == STATE_LIST_WFMS) {
            CountriesClass.commonMethod(response, ConstantsWFMS.STATE_LIST_WFMS)

        } else if (TYPE == CITY_LIST_WFMS) {
            CountriesClass.commonMethod(response, ConstantsWFMS.CITY_LIST_WFMS)

        } else if (TYPE == MARKETING_TASK_SUBMIT_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                Utilities.showToast(this, json.getString("Message"))
                finish()
            }
        }
    }


    fun showDropdown(array: Array<String?>, textView: TextView, spinnerData: SpinnerData, width: Int) {
        var name = ""
        listPopupWindow = ListPopupWindow(this)
        listPopupWindow.setAdapter(ArrayAdapter(
                this,
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