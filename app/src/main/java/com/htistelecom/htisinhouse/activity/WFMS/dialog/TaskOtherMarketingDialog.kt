package com.htistelecom.htisinhouse.activity.WFMS.dialogOpen

import android.app.Dialog
import android.app.TimePickerDialog
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MARKETING_TASK_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TaskModel
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.adapter.MarketingTaskAdapter
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.font.UbuntuButton
import com.htistelecom.htisinhouse.font.UbuntuEditText
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.CountriesClass
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.dialog_task_other_marketing.*
import kotlinx.android.synthetic.main.dialog_task_other_marketing.ivAddTaskFragment
import kotlinx.android.synthetic.main.dialog_task_other_marketing.rvTaskFragment
import kotlinx.android.synthetic.main.dialog_task_other_marketing.tvNoTaskFragment
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_marketing.ivBack
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TaskOtherMarketingdialogOpen : DialogFragment(),MyInterface {
    var empId:String=""
    private val mWidthPosition: Int = 400
    private var taskList = ArrayList<TaskModel>()
    lateinit var adapter: MarketingTaskAdapter


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
    lateinit var listPopupWindow: ListPopupWindow
        lateinit var dialogOpen:Dialog

    val cal = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    var mStartTime: Date? = null
    var mEndtTime: Date? = null

    var sStartTime = ""
    var sEndTime = ""
    companion object {
        fun newInstance(userId: String): TaskOtherMarketingdialogOpen? {
            var fragment= TaskOtherMarketingdialogOpen()
            val args = Bundle()
            args.putString("id", userId)
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_task_other_marketing, null)
        empId= getArguments()!!.getString("id").toString();

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivDrawer.visibility = View.GONE
        tv_title.text = "Add Task Details"
        ivBack.setOnClickListener { dialog!!.dismiss()
        }
        rvTaskFragment.layoutManager = LinearLayoutManager(activity)
        ivAddTaskFragment.setOnClickListener { openDialog() }
        listeners()
        val date = ConstantKotlin.getCurrentDate()
        val jsonObject = JSONObject()

        jsonObject.put("EmpId", empId)
        jsonObject.put("TaskDate", date)
        callAPI(ConstantsWFMS.MARKETING_TASK_LIST_WFMS, jsonObject.toString())


    }

    private fun listeners() {

    }
    fun openDialog() {

        dialogOpen = Dialog(activity!!)
        dialogOpen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogOpen!!.setContentView(R.layout.activity_marketing)
        dialogOpen!!.setCancelable(false)
        dialogOpen!!.show()

        callAPI(ConstantsWFMS.MARKETING_POSITION_WFMS, "")
        mAddress = Utilities.getAddressFromLatLong(activity, OreoLocationService.LATITUDE.toDouble(), OreoLocationService.LONGITUDE.toDouble())
        sAddress = mAddress!!.get(0).getAddressLine(0).toString()
        val ivBack = dialogOpen.findViewById<ImageView>(R.id.ivBack)
        val tv_title = dialogOpen.findViewById<Ubuntu>(R.id.tv_title)
        val ivDrawer = dialogOpen.findViewById<ImageView>(R.id.ivDrawer)

        ivDrawer.visibility = View.GONE
        tv_title.text = "Add Task Details"
        ivBack.setOnClickListener { dialogOpen.dismiss() }


        val etCompanyNameMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etCompanyNameMarketingActivity)
        val etPersonNameMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etPersonNameMarketingActivity)
        val etRemarksMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etRemarksMarketingActivity)


        val rlPositionMarketingActivity = dialogOpen.findViewById<RelativeLayout>(R.id.rlPositionMarketingActivity)
        val ivPositionMarketingActivity = dialogOpen.findViewById<ImageView>(R.id.ivPositionMarketingActivity)
        val tvPositionMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvPositionMarketingActivity)

      //  val etWebstiteMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etWebstiteMarketingActivity)

        val rlStartTimeMarketingActivity = dialogOpen.findViewById<RelativeLayout>(R.id.rlStartTimeMarketingActivity)
        val tvReachTimeMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvReachTimeMarketingActivity)

        val rlEndTimeMarketingActivity = dialogOpen.findViewById<RelativeLayout>(R.id.rlEndTimeMarketingActivity)
        val tvOutTimeMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvOutTimeMarketingActivity)

        val ivEntityTypeMarketingActivity = dialogOpen.findViewById<ImageView>(R.id.ivEntityTypeMarketingActivity)
        val tvEntityMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvEntityMarketingActivity)

        val ivNatureBusinessMarketingActivity = dialogOpen.findViewById<ImageView>(R.id.ivNatureBusinessMarketingActivity)
        val tvNatureBusinessMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvNatureBusinessMarketingActivity)

//        val etCompanyOwnerNameMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etCompanyOwnerNameMarketingActivity)
//        val etCompanyOwnerDetailsMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etCompanyOwnerDetailsMarketingActivity)

        val rlTurnoverMarketingActivity = dialogOpen.findViewById<RelativeLayout>(R.id.rlTurnoverMarketingActivity)
        val tvTurnoverMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvTurnoverMarketingActivity)

        val tvLeadMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvLeadMarketingActivity)

        val rgLeadMarketingActivity = dialogOpen.findViewById<RadioGroup>(R.id.rgLeadMarketingActivity)

        val etEmailMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etEmailMarketingActivity)
        val etPhoneMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etPhoneMarketingActivity)
        val etAddressMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etAddressMarketingActivity)

        val ivCountryMarketingActivity = dialogOpen.findViewById<ImageView>(R.id.ivCountryMarketingActivity)
        val tvCountryMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvCountryMarketingActivity)
        val ivStateMarketingActivity = dialogOpen.findViewById<ImageView>(R.id.ivStateMarketingActivity)
        val tvStateMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvStateMarketingActivity)
        val ivCityMarketingActivity = dialogOpen.findViewById<ImageView>(R.id.ivCityMarketingActivity)
        val tvCityMarketingActivity = dialogOpen.findViewById<Ubuntu>(R.id.tvCityMarketingActivity)
        val etZipMarketingActivity = dialogOpen.findViewById<UbuntuEditText>(R.id.etZipMarketingActivity)
        val btnSubmitMarketingActivity = dialogOpen.findViewById<UbuntuButton>(R.id.btnSubmitMarketingActivity)
        val btnCancelMarketingActivity = dialogOpen.findViewById<UbuntuButton>(R.id.btnCancelMarketingActivity)
        etAddressMarketingActivity.setText(sAddress)
        btnCancelMarketingActivity.setOnClickListener { dialogOpen.dismiss() }
        rgLeadMarketingActivity.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = dialogOpen.findViewById(checkedId)

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


            val timePickerdialogOpen = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                val tvStartTime = cal.format(cal.parse(hour.toString() + ":" + minute.toString()))
                tvReachTimeMarketingActivity.text = tvStartTime
            }, mHour, mMinute, false)


            timePickerdialogOpen.show()

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


                val timePickerdialogOpen = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                    mEndtTime = cal.parse(hour.toString() + ":" + minute)
                    if (mEndtTime!!.before(mStartTime) || mEndtTime!!.equals(mStartTime)) {
                        Utilities.showToast(activity, "Out date must be after reach date")
                    } else {
                        val tvEndTime = cal.format(cal.parse(hour.toString() + ":" + minute.toString()))
                        tvOutTimeMarketingActivity.text = tvEndTime
                    }

                }, mHour, mMinute, false)


                timePickerdialogOpen.show()
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
//            sCompanyOwnerName = etCompanyOwnerNameMarketingActivity!!.text.toString()
//            sCompanyOwnerDetails = etCompanyOwnerDetailsMarketingActivity!!.text.toString()
            sRemarks = etRemarksMarketingActivity!!.text.toString()
            sEmail = etEmailMarketingActivity!!.text.toString()
            sPhone = etPhoneMarketingActivity!!.text.toString()
            sAddress = etAddressMarketingActivity!!.text.toString()
            sZip = etZipMarketingActivity!!.text.toString()
            sEndTime = tvOutTimeMarketingActivity.text.toString()
            sStartTime = tvReachTimeMarketingActivity.text.toString();

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
            json.put("CompanyOwner","")

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

    private fun callAPI(TYPE: Int, params: String) {
        if (TYPE == ConstantsWFMS.MARKETING_TASK_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.MARKETING_TASK_LIST_WFMS, this, activity)
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
                dialogOpen.dismiss()
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
                taskList = Gson().fromJson<java.util.ArrayList<TaskModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<TaskModel>>() {}.type)

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