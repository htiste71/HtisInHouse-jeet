package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Dialog
import android.content.Intent

import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.view.Window
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.TASK_STATUS_LIST_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.TASK_STATUS_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS.Companion.showToast
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_perform_wfms.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File

class PerformActivityWFMS : BaseActivityCamera(), MyInterface, View.OnClickListener {
    private var mStatus: String = ""
    var fileToUpload: MultipartBody.Part? = null
    lateinit var taskStatusListArray: Array<String?>
    private val listTaskStatus = ArrayList<TwoParameterModel>()
    lateinit var imgFile: File
    var latitude: String? = null
    var longitute: String? = null
    var message: String? = null
    var status_id = ""
    var fromHomeFragment = false
    var _siteUploadedId = "0"
    var lat: String? = null
    var lang: String? = null
    var lat1: String? = null
    var lang1: String? = null
    var origin: String? = null
    var destination: String? = null
    var _data = ""
    var mImagePath = ""
    var destLat = 0.0
    var destLong = 0.0
    var result = 0.0

    // lateinit var statusData: Array<String?>
    lateinit var dialog: Dialog
    var tinyDB: TinyDB? = null
    var statusAdapter: ArrayAdapter<String?>? = null

    var status: String? = null
    var isStart = false
    var startTask = false

    // var gpsTracker: GPSTracker? = null
    //  var commonFunctions: CommonFunctions? = null
    var _address: List<Address>? = ArrayList()
    var isCompleted = false
    lateinit var siteListModel: TaskListModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perform_wfms)

        init()
        dataFromIntent()
        clickListener()


        //implicit type declaration
        //hitAPI("", Con     stantsWFMS.TASK_STATUS_LIST_WFMS)

    }


    private fun clickListener() {
        ivActivity.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        btnStart.setOnClickListener(this)

        btnStatus.setOnClickListener(this)

        tvViewMap.setOnClickListener(this)


    }


    private fun dataFromIntent() {
        siteListModel = intent.getSerializableExtra("data") as TaskListModel
        //  siteListModel = Gson().fromJson(intent.getStringExtra("data"), TaskListModel::class.java)
        // mSiteId = siteListModel.getSiteUploadedId()
        tinyDB!!.putString("DestLat", siteListModel.getLatitude())
        tinyDB!!.putString("DestLong", siteListModel.getLongitude())
        tinyDB!!.putString("siteId", siteListModel.getSiteId())
        tinyDB!!.putString("ProjectId", siteListModel.getProjectId())
        tvProjectName.setText(siteListModel.getProjectName())
        tvSiteId.setText(siteListModel.getSiteId())
        tvSiteName.setText(siteListModel.getSiteName())
        tvLatitude.setText(siteListModel.getLatitude())
        tvLongitude.setText(siteListModel.getLongitude())
        tvActivity.text = siteListModel.activityName
        tvWorkDate.setText(siteListModel.getWorkDate())
        tvStatus.setText(siteListModel.getStatus())
        tvRemarks.text = siteListModel.taskRemarks
        mStatus = siteListModel.status

        if (!siteListModel.activityImage.equals("")) {
            if (!mStatus.equals("Pending")) {
                mImagePath = siteListModel.activityImagePath + siteListModel.activityImage
                Glide.with(this).load(mImagePath).into(ivActivity);
            }


        }
        if (!siteListModel.activityDemoImage.equals("")) {
            Glide.with(this).load(siteListModel.activityDemoImagePath + siteListModel.activityDemoImage).into(ivActivityDemo);

        }



        if (!siteListModel.getLatitude().equals("", ignoreCase = true) || !siteListModel.getLongitude().equals("", ignoreCase = true)) {
            _address = Utilities.getAddressFromLatLong(this@PerformActivityWFMS, siteListModel.getLatitude().toDouble(), siteListModel.getLongitude().toDouble())
        } else {
        }
        if (_address == null || _address!!.isEmpty()) {
            tvAddress.setText("Your current address is not found")
        } else {
            tvAddress.setText(_address!![0].getAddressLine(0) + "(google)")
        }
        tinyDB!!.putString("Status", tvStatus.getText().toString())
        isStart = intent.getBooleanExtra("isStarted", false)
        /* ShowButtons=getIntent().getStringExtra("ShowButtons");*/
        val status = siteListModel.getStatus().toLowerCase()
        // _siteUploadedId = siteListModel.siteUploadedId().toLowerCase()
        if (status.equals("Started", ignoreCase = true) || status.equals("Pending", ignoreCase = true)) {
            btnStatus.setVisibility(View.VISIBLE)
            btnStart.setVisibility(View.GONE)
            tvViewMap.setVisibility(View.GONE)



            isCompleted = false
        } else if (!isStart && (status.equals("Assigned", ignoreCase = true) || status.equals("none", ignoreCase = true) || status.equals("", ignoreCase = true))) {
            btnStatus.setVisibility(View.GONE)
            tvViewMap.setVisibility(View.VISIBLE)
            btnStart.setVisibility(View.VISIBLE)
            isCompleted = false
        } else if (isStart && (status.equals("Assigned", ignoreCase = true) || status.equals("none", ignoreCase = true) || status.equals("", ignoreCase = true))) {
            llButton.setVisibility(View.INVISIBLE)
            tvViewMap.setVisibility(View.GONE)
            isCompleted = false
        } else if (status.equals("Completed", ignoreCase = true) || status.equals("Rejected", ignoreCase = true) || status.equals("Approved", ignoreCase = true)) {
            llButton.setVisibility(View.INVISIBLE)
            tvViewMap.setVisibility(View.GONE)
            isCompleted = true
        }
    }

    fun init() {
        tv_title.setText("Site Details")
        ivDrawer.setVisibility(View.GONE)
        tinyDB = TinyDB(this@PerformActivityWFMS)
        fromHomeFragment = intent.getBooleanExtra("fromHome", false)
//        gpsTracker = GPSTracker(this)
//        commonFunctions = CommonFunctions()
        val model = TwoParameterModel()
        model.id = "0"
        model.name = "Select the status"
        listTaskStatus.add(model)
        val model1 = TwoParameterModel()
        model1.id = "1"
        model1.name = "Pending"
        listTaskStatus.add(model1)

        val model5 = TwoParameterModel()
        model5.id = "5"
        model5.name = "Completed"
        listTaskStatus.add(model5)


        taskStatusListArray = arrayOf("Select the status", "Pending", "Completed")

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivBack -> backToHome()
            R.id.btnCancel -> backToHome()
            R.id.btnStart -> {
//                destLat = tinyDB!!.getString("DestLat").toDouble()
//                destLong = tinyDB!!.getString("DestLong").toDouble()
//                result = commonFunctions!!.distBetweenLatLng(gpsTracker!!.latitude, gpsTracker!!.longitude, destLat, destLong)
//                if (result > 300) {
//                    Utilities.showToast(this@PerformActivityWFMS, "You aren't near to your destination")
//                } else {

                if (isPunchInMethod()) {
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("TaskId", siteListModel!!.taskId)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    hitAPI(jsonObject.toString(), ConstantsWFMS.START_TASK_WFMS)
                } else {
                    Utilities.showToast(this, resources.getString(R.string.errPunchIn))
                }


            }
            R.id.btnStatus -> {
                startTask = false
                show_statusPopup()
            }
            R.id.tvViewMap -> {
                lat = OreoLocationService.LATITUDE
                lang = OreoLocationService.LONGITUDE
                origin = "$lat,$lang"
                lat1 = tinyDB!!.getString("DestLat")
                lang1 = tinyDB!!.getString("DestLong")
                destination = "$lat1,$lang1"
                callMapData(origin!!, destination!!)
            }
            R.id.ivActivity -> {
                if (isPunchInMethod()) {
                    if (!isCompleted)
                        checkPermissions("com.htistelecom.htisinhouse.activity.WFMS.activity.PerformActivityWFMS")
                    else
                        Utilities.showToast(this, resources.getString(R.string.strTaskCompleted))
                } else {
                    UtilitiesWFMS.showToast(this, resources.getString(R.string.errPunchIn))
                }

            }

        }
    }

    private fun isPunchInMethod(): Boolean {
        return tinyDB!!.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)


    }

//    override fun startDayStatus(stausModelList: List<StatusModel>) {
//        statusModelList = stausModelList
//    }

    //status  pop up
    var submit: Button? = null
    var cancel: Button? = null
    var tvtitlePop: TextView? = null
    var ivDrawerPop: ImageView? = null
    var ivBackPop: ImageView? = null
    var etMessage: EditText? = null


    private fun show_statusPopup() {
        dialog = Dialog(this@PerformActivityWFMS)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.status_dialog)
        dialog!!.setCancelable(false)
        dialog!!.show()
        val spnrStatus = dialog!!.findViewById<Spinner>(R.id.spnrStatus)
        submit = dialog!!.findViewById(R.id.btnSubmit)
        cancel = dialog!!.findViewById(R.id.cancel_btn)
        tvtitlePop = dialog!!.findViewById(R.id.tv_title)
        ivBackPop = dialog!!.findViewById(R.id.ivBack)
        ivDrawerPop = dialog!!.findViewById(R.id.ivDrawer)
        etMessage = dialog!!.findViewById(R.id.etMessage)

        ivDrawerPop!!.setVisibility(View.GONE)
        tvtitlePop!!.text = "Task Status"
        ivBackPop!!.visibility = GONE
        // set the data in spinner
        //  statusData = arrayOfNulls(statusModelList!!.size + 1)


        statusAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskStatusListArray)
        //setting adapter to spinner
        spnrStatus.adapter = statusAdapter
        spnrStatus.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                status = parent.getItemAtPosition(position).toString()
                if (position != 0) {
                    status_id = listTaskStatus!![position].id.toString()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
        cancel!!.setOnClickListener(View.OnClickListener { v: View? -> dialog!!.dismiss() })
        submit!!.setOnClickListener(View.OnClickListener { v: View? ->
            try {
                if (isPunchInMethod()) {
                    Utilities.hideKeyboard(this@PerformActivityWFMS, v)
                    message = etMessage!!.getText().toString()


                    if (status.equals("Select the status")) {
                        Utilities.showToast(this@PerformActivityWFMS, "Select the status")
                        return@OnClickListener
                    } else if (TextUtils.isDigitsOnly(message)) {
                        Utilities.showToast(this@PerformActivityWFMS, "Please enter activity message")
                    } else {

                        if (status.equals("Pending")) {

                            val jsonObject = JSONObject()
                            try {
                                jsonObject.put("TaskId", siteListModel.taskId)
                                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                                jsonObject.put("Status", status_id)
                                jsonObject.put("ActivityId", siteListModel.activityId)
                                _data = jsonObject.toString()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            hitAPI(_data, TASK_STATUS_WFMS)


                        }
                        else if (status.equals("Completed")) {
                            if (fileToUpload == null) {
                                UtilitiesWFMS.showToast(this, resources.getString(R.string.errChangePendingFile))

                            }
                            else {
                                val jsonObject = JSONObject()
                                try {
                                    jsonObject.put("TaskId", siteListModel.taskId)
                                    jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                                    jsonObject.put("Status", status_id)
                                    jsonObject.put("ActivityId", siteListModel.activityId)
                                    _data = jsonObject.toString()
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }


                                hitAPI(_data, TASK_STATUS_WFMS)
                            }
                        }


                    }
                } else {
                    Utilities.showToast(this, resources.getString(R.string.errPunchIn))
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }


    private fun callMapData(origin: String, destination: String) {
        val uri = "http://maps.google.com/maps?saddr=$origin&daddr=$destination"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }

    var position = 0


    override fun onBackPressed() {
        backToHome()
    }

    private fun backToHome() {
        if (fromHomeFragment)

            startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Home"))
        else
            startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Task"))

        finish()
    }

    fun hitAPI(params: String?, TYPE: Int) {
        if (TYPE == ConstantsWFMS.START_TASK_WFMS) {
            ApiData.getData(params, ConstantsWFMS.START_TASK_WFMS, this, this)
        } else if (TYPE == TASK_STATUS_LIST_WFMS) {
            ApiData.getGetData(ConstantsWFMS.TASK_STATUS_LIST_WFMS, this, this)

        } else if (TYPE == TASK_STATUS_WFMS) {
            //val activityDataBody: RequestBody =  RequestBody.create("text/plain".toMediaTypeOrNull(), params!!)

            ApiData.forImageData(fileToUpload, params, TASK_STATUS_WFMS, this, this)
        }
    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utilities.dismissDialog()

        if (TYPE == ConstantsWFMS.START_TASK_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                Utilities.showToast(this, json.getString("Message"))
                btnStatus.setVisibility(View.VISIBLE)
                tvStatus.text = "Started"
                btnStart.setVisibility(View.GONE)
                tvViewMap.setVisibility(View.GONE)
            } else {
                Utilities.showToast(this, json.getString("Message"))

            }
        } else if (TYPE == ConstantsWFMS.TASK_STATUS_LIST_WFMS) {
            val jsonArray = JSONArray((response as Response<*>).body()!!.toString())

            val model = TwoParameterModel()
            model.name = "Select a Status"
            model.id = "-1"
            listTaskStatus.add(model)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)

                val model = TwoParameterModel()
                model.id = item.getString("CompanyNatureId")
                model.name = item.getString("TapLocationTypeName")
                listTaskStatus.add(model)
            }


            taskStatusListArray = arrayOfNulls<String>(listTaskStatus.size)
            for (i in listTaskStatus.indices) {
                taskStatusListArray[i] = listTaskStatus.get(i).name
            }


        } else if (TYPE == ConstantsWFMS.TASK_STATUS_WFMS) {
            val json = JSONObject((response as Response<*>).body()!!.toString())
            if (json.getString("Status").equals("Success")) {
                Utilities.showToast(this, json.getString("Message"))
                dialog.dismiss()
                finish()
            } else {
                Utilities.showToast(this, json.getString("Message"))

            }

        }
    }


    fun imagePath(imgPath: File) {
        imgFile = imgPath
        Glide.with(this).load(imgPath).into(ivActivity);
        val mFile = RequestBody.create("image/*".toMediaTypeOrNull(), imgFile)
        fileToUpload = MultipartBody.Part.createFormData("ActivityImage", imgFile.name, mFile)

    }
}