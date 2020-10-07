//package com.htistelecom.htisinhouse.activity.WFMS.activity
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.app.Dialog
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.Address
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.LinearLayoutManager.VERTICAL
//import android.text.TextUtils
//import android.view.View
//import android.view.View.GONE
//import android.view.Window
//import android.widget.*
//import android.widget.AdapterView.OnItemSelectedListener
//import com.htistelecom.htisinhouse.R
//import com.htistelecom.htisinhouse.activity.ApiData
//import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
//import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.NEW_TASK_STATUS_WFMS
//import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.TASK_STATUS_LIST_WFMS
//import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
//import com.htistelecom.htisinhouse.activity.WFMS.adapters.TaskImagesAdapter
//import com.htistelecom.htisinhouse.activity.WFMS.interfaces.iActivity
//import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
//import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
//import com.htistelecom.htisinhouse.activity.WFMS.service.OreoLocationService
//import com.htistelecom.htisinhouse.config.TinyDB
//import com.htistelecom.htisinhouse.retrofit.MyInterface
//import com.htistelecom.htisinhouse.utilities.Utilities
//import com.theartofdev.edmodo.cropper.CropImage
//import com.theartofdev.edmodo.cropper.CropImageView
//import kotlinx.android.synthetic.main.activity_perform_new_wfms.*
//import kotlinx.android.synthetic.main.activity_perform_wfms.btnCancel
//import kotlinx.android.synthetic.main.activity_perform_wfms.btnStart
//import kotlinx.android.synthetic.main.activity_perform_wfms.btnStatus
//import kotlinx.android.synthetic.main.activity_perform_wfms.llButton
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvActivity
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvAddress
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvLatitude
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvLongitude
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvProjectName
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvRemarks
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvSiteId
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvSiteName
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvStatus
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvViewMap
//import kotlinx.android.synthetic.main.activity_perform_wfms.tvWorkDate
//import kotlinx.android.synthetic.main.toolbar.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import org.json.JSONArray
//import org.json.JSONException
//import org.json.JSONObject
//import retrofit2.Response
//import java.io.File
//import java.io.IOException
//
//class PerformActivityNew : BaseActivityCamera(), MyInterface, View.OnClickListener, iActivity {
//    lateinit var mCropImageUri: Uri
//    private var value: Int = 0
//    private var mStatus: String = ""
//    lateinit var taskStatusListArray: Array<String?>
//    private val listTaskStatus = ArrayList<TwoParameterModel>()
//    lateinit var imgFile: File
//    var latitude: String? = null
//    var longitute: String? = null
//    var message: String? = null
//    var status_id = ""
//    var fromHomeFragment = false
//    var _siteUploadedId = "0"
//    var lat: String? = null
//    var lang: String? = null
//    var lat1: String? = null
//    var lang1: String? = null
//    var origin: String? = null
//    var destination: String? = null
//    var _data = ""
//    var mImagePath = ""
//    var destLat = 0.0
//    var destLong = 0.0
//    var result = 0.0
//
//    lateinit var adapter: TaskImagesAdapter
//    var taskList = ArrayList<TaskListModel>()
//    var taskListSelected = ArrayList<TaskListModel>()
//
//    lateinit var dialog: Dialog
//    var tinyDB: TinyDB? = null
//    var statusAdapter: ArrayAdapter<String?>? = null
//
//    var status: String? = null
//    var isStart = false
//    var startTask = false
//
//    // var gpsTracker: GPSTracker? = null
//    //  var commonFunctions: CommonFunctions? = null
//    var _address: List<Address>? = ArrayList()
//    var isCompleted = false
//    lateinit var siteListModel: TaskListModel
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_perform_new_wfms)
//
//        init()
//        dataFromIntent()
//        clickListener()
//
//
//        //implicit type declaration
//        //hitAPI("", Con     stantsWFMS.TASK_STATUS_LIST_WFMS)
//
//    }
//
//
//    private fun clickListener() {
//        //  ivActivity.setOnClickListener(this)
//        ivBack.setOnClickListener(this)
//        btnCancel.setOnClickListener(this)
//        btnStart.setOnClickListener(this)
//
//        btnStatus.setOnClickListener(this)
//
//        tvViewMap.setOnClickListener(this)
//
//
//    }
//
//
//    private fun dataFromIntent() {
//        siteListModel = intent.getSerializableExtra("data") as TaskListModel
//        //  siteListModel = Gson().fromJson(intent.getStringExtra("data"), TaskListModel::class.java)
//        // mSiteId = siteListModel.getSiteUploadedId()
//        tinyDB!!.putString("DestLat", siteListModel.getLatitude())
//        tinyDB!!.putString("DestLong", siteListModel.getLongitude())
//        tinyDB!!.putString("siteId", siteListModel.getSiteId())
//        tinyDB!!.putString("ProjectId", siteListModel.getProjectId())
//        tvProjectName.setText(siteListModel.getProjectName())
//        tvSiteId.setText(siteListModel.getSiteId())
//        tvSiteName.setText(siteListModel.getSiteName())
//        tvLatitude.setText(siteListModel.getLatitude())
//        tvLongitude.setText(siteListModel.getLongitude())
//        tvActivity.text = siteListModel.activityName
//        tvWorkDate.setText(siteListModel.getWorkDate())
//        tvStatus.setText(siteListModel.getStatus())
//        tvRemarks.text = siteListModel.taskRemarks
//        mStatus = siteListModel.status
//
//
//        taskList = intent.getSerializableExtra("list") as ArrayList<TaskListModel>
//        taskListSelected.add(siteListModel)
//        for (record in 0 until taskList.size) {
//            if (siteListModel.taskId.equals(taskList.get(record).taskId) && !siteListModel.subActivityId.equals(taskList.get(record).subActivityId)) {
//                taskListSelected.add(taskList.get(record))
//            }
//        }
//
//
//
//
//
//
//
//        if (!siteListModel.getLatitude().equals("", ignoreCase = true) || !siteListModel.getLongitude().equals("", ignoreCase = true)) {
//            _address = Utilities.getAddressFromLatLong(this@PerformActivityNew, siteListModel.getLatitude().toDouble(), siteListModel.getLongitude().toDouble())
//        } else {
//        }
//        if (_address == null || _address!!.isEmpty()) {
//            tvAddress.setText("Your current address is not found")
//        } else {
//            tvAddress.setText(_address!![0].getAddressLine(0) + "(google)")
//        }
//        tinyDB!!.putString("Status", tvStatus.getText().toString())
//        isStart = intent.getBooleanExtra("isStarted", false)
//        /* ShowButtons=getIntent().getStringExtra("ShowButtons");*/
//        val status = siteListModel.getStatus().toLowerCase()
//        // _siteUploadedId = siteListModel.siteUploadedId().toLowerCase()
//        if (status.equals("Started", ignoreCase = true) || status.equals("Pending", ignoreCase = true)) {
//            btnStatus.setVisibility(View.VISIBLE)
//            btnStart.setVisibility(View.GONE)
//            tvViewMap.setVisibility(View.GONE)
//
//
//
//            isCompleted = false
//        } else if (!isStart && (status.equals("Assigned", ignoreCase = true) || status.equals("none", ignoreCase = true) || status.equals("", ignoreCase = true))) {
//            btnStatus.setVisibility(View.GONE)
//            tvViewMap.setVisibility(View.VISIBLE)
//            btnStart.setVisibility(View.VISIBLE)
//            isCompleted = false
//        } else if (isStart && (status.equals("Assigned", ignoreCase = true) || status.equals("none", ignoreCase = true) || status.equals("", ignoreCase = true))) {
//            llButton.setVisibility(View.INVISIBLE)
//            tvViewMap.setVisibility(View.GONE)
//            isCompleted = false
//        } else if (status.equals("Completed", ignoreCase = true) || status.equals("Rejected", ignoreCase = true) || status.equals("Approved", ignoreCase = true)) {
//            llButton.setVisibility(View.INVISIBLE)
//            tvViewMap.setVisibility(View.GONE)
//            isCompleted = true
//        }
//
//        adapter = TaskImagesAdapter(this, taskListSelected, this)
//        rvActivity.adapter = adapter
//    }
//
//    fun init() {
//        tv_title.setText("Site Details")
//        ivDrawer.setVisibility(View.GONE)
//        tinyDB = TinyDB(this@PerformActivityNew)
//        rvActivity.layoutManager = LinearLayoutManager(this, VERTICAL, false)
//        fromHomeFragment = intent.getBooleanExtra("fromHome", false)
////        gpsTracker = GPSTracker(this)
////        commonFunctions = CommonFunctions()
//        val model = TwoParameterModel()
//        model.id = "0"
//        model.name = "Select the status"
//        listTaskStatus.add(model)
//        val model1 = TwoParameterModel()
//        model1.id = "1"
//        model1.name = "Pending"
//        listTaskStatus.add(model1)
//
//        val model5 = TwoParameterModel()
//        model5.id = "5"
//        model5.name = "Completed"
//        listTaskStatus.add(model5)
//
//
//        taskStatusListArray = arrayOf("Select the status", "Pending", "Completed")
//
//    }
//
//    override fun onClick(view: View) {
//        when (view.id) {
//            R.id.ivBack -> backToHome()
//            R.id.btnCancel -> backToHome()
//            R.id.btnStart -> {
////                destLat = tinyDB!!.getString("DestLat").toDouble()
////                destLong = tinyDB!!.getString("DestLong").toDouble()
////                result = commonFunctions!!.distBetweenLatLng(gpsTracker!!.latitude, gpsTracker!!.longitude, destLat, destLong)
////                if (result > 300) {
////                    Utilities.showToast(this@PerformActivityNew, "You aren't near to your destination")
////                } else {
//
//                if (isPunchInMethod()) {
//                    val jsonObject = JSONObject()
//                    try {
//                        jsonObject.put("TaskId", siteListModel!!.taskId)
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                    hitAPI(jsonObject.toString(), ConstantsWFMS.START_TASK_WFMS)
//                } else {
//                    Utilities.showToast(this, resources.getString(R.string.errPunchIn))
//                }
//
//
//            }
//            R.id.btnStatus -> {
//                startTask = false
//                show_statusPopup()
//            }
//            R.id.tvViewMap -> {
//                lat = OreoLocationService.LATITUDE
//                lang = OreoLocationService.LONGITUDE
//                origin = "$lat,$lang"
//                lat1 = tinyDB!!.getString("DestLat")
//                lang1 = tinyDB!!.getString("DestLong")
//                destination = "$lat1,$lang1"
//                callMapData(origin!!, destination!!)
//            }
////            R.id.ivActivity -> {
////                if (isPunchInMethod()) {
////                    if (!isCompleted)
////                        checkPermissions("com.htistelecom.htisinhouse.activity.NewKnjrkhana.activity.PerformActivityNew")
////                    else
////                        Utilities.showToast(this, resources.getString(R.string.strTaskCompleted))
////                } else {
////                    UtilitiesWFMS.showToast(this, resources.getString(R.string.errPunchIn))
////                }
////
////            }
//
//        }
//    }
//
//    private fun isPunchInMethod(): Boolean {
//        return tinyDB!!.getBoolean(ConstantsWFMS.TINYDB_IS_PUNCH_IN)
//
//
//    }
//
////    override fun startDayStatus(stausModelList: List<StatusModel>) {
////        statusModelList = stausModelList
////    }
//
//    //status  pop up
//    var submit: Button? = null
//    var cancel: Button? = null
//    var tvtitlePop: TextView? = null
//    var ivDrawerPop: ImageView? = null
//    var ivBackPop: ImageView? = null
//    var etMessage: EditText? = null
//
//
//    private fun show_statusPopup() {
//        dialog = Dialog(this@PerformActivityNew)
//        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog!!.setContentView(R.layout.status_dialog)
//        dialog!!.setCancelable(false)
//        dialog!!.show()
//        val spnrStatus = dialog!!.findViewById<Spinner>(R.id.spnrStatus)
//        submit = dialog!!.findViewById(R.id.btnSubmit)
//        cancel = dialog!!.findViewById(R.id.cancel_btn)
//        tvtitlePop = dialog!!.findViewById(R.id.tv_title)
//        ivBackPop = dialog!!.findViewById(R.id.ivBack)
//        ivDrawerPop = dialog!!.findViewById(R.id.ivDrawer)
//        etMessage = dialog!!.findViewById(R.id.etMessage)
//
//        ivDrawerPop!!.setVisibility(View.GONE)
//        tvtitlePop!!.text = "Task Status"
//        ivBackPop!!.visibility = GONE
//        // set the data in spinner
//        //  statusData = arrayOfNulls(statusModelList!!.size + 1)
//
//
//        statusAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskStatusListArray)
//        //setting adapter to spinner
//        spnrStatus.adapter = statusAdapter
//        spnrStatus.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                status = parent.getItemAtPosition(position).toString()
//                if (position != 0) {
//                    status_id = listTaskStatus!![position].id.toString()
//
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // TODO Auto-generated method stub
//            }
//        }
//        cancel!!.setOnClickListener(View.OnClickListener { v: View? -> dialog!!.dismiss() })
//        submit!!.setOnClickListener(View.OnClickListener { v: View? ->
//            try {
//                if (isPunchInMethod()) {
//                    Utilities.hideKeyboard(this@PerformActivityNew, v)
//                    message = etMessage!!.getText().toString()
//
//
//                    if (status.equals("Select the status")) {
//                        Utilities.showToast(this@PerformActivityNew, "Select the status")
//                        return@OnClickListener
//                    } else if (TextUtils.isDigitsOnly(message)) {
//                        Utilities.showToast(this@PerformActivityNew, "Please enter activity message")
//                    } else {
//                        loopImagesToSend()
//                    }
//                } else {
//                    Utilities.showToast(this, resources.getString(R.string.errPunchIn))
//                }
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        })
//    }
//
//    private fun loopImagesToSend() {
//        if (status.equals("Pending")) {
//
//
//            val jsonObject = JSONObject()
//            try {
//                jsonObject.put("TaskId", siteListModel.taskId)
//                jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
//                jsonObject.put("Status", status_id)
//                jsonObject.put("ActivityId", siteListModel.activityId)
//                _data = jsonObject.toString()
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            hitAPI(_data, NEW_TASK_STATUS_WFMS)
//
//
//        } else if (status.equals("Completed")) {
//            var isEmpty = false
//            for (i in 0 until taskListSelected.size) {
//                if (taskListSelected.get(i).activityImage.equals("")) {
//                    isEmpty = true
//                    // UtilitiesWFMS.showToast(this,"Select the image")
//                    break
//                }
//            }
//
//
//            if (isEmpty) {
//                UtilitiesWFMS.showToast(this, resources.getString(R.string.errChangePendingFile))
//
//            } else {
//
//                completeImageListToUpload()
//
//            }
//        }
//
//
//    }
//
//    private fun completeImageListToUpload() {
//
//        val jsonObject = JSONObject()
//        try {
//            jsonObject.put("TaskId", taskListSelected.get(value).taskId)
//            jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
//            jsonObject.put("Status", status_id)
//            jsonObject.put("ActivityId", taskListSelected.get(value).activityId)
//            jsonObject.put("SubActivityId", taskListSelected.get(value).subActivityId)
//
//            _data = jsonObject.toString()
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//
//        hitAPI(_data, NEW_TASK_STATUS_WFMS)
//
//
//    }
//
//
//    private fun callMapData(origin: String, destination: String) {
//        val uri = "http://maps.google.com/maps?saddr=$origin&daddr=$destination"
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
//        startActivity(intent)
//    }
//
//    var position = 0
//
//
//    override fun onBackPressed() {
//        backToHome()
//    }
//
//    private fun backToHome() {
//        if (fromHomeFragment)
//
//            startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Home"))
//        else
//            startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Task"))
//
//        finish()
//    }
//
//    fun hitAPI(params: String?, TYPE: Int) {
//        if (TYPE == ConstantsWFMS.START_TASK_WFMS) {
//            ApiData.getData(params, ConstantsWFMS.START_TASK_WFMS, this, this)
//        } else if (TYPE == TASK_STATUS_LIST_WFMS) {
//            ApiData.getGetData(ConstantsWFMS.TASK_STATUS_LIST_WFMS, this, this)
//
//        } else if (TYPE == NEW_TASK_STATUS_WFMS) {
//
//            imgFile = File(taskListSelected.get(value).activityImage)
//            val mFile = RequestBody.create("image/*".toMediaTypeOrNull(), imgFile)
//            val fileToUpload = MultipartBody.Part.createFormData("ActivityImage", imgFile.name, mFile)
//            //val activityDataBody: RequestBody =  RequestBody.create("text/plain".toMediaTypeOrNull(), params!!)
//
//            ApiData.forImageData(fileToUpload, params, NEW_TASK_STATUS_WFMS, this, this)
//        }
//    }
//
//    override fun sendResponse(response: Any, TYPE: Int) {
//        Utilities.dismissDialog()
//
//        if (TYPE == ConstantsWFMS.START_TASK_WFMS) {
//            val json = JSONObject((response as Response<*>).body()!!.toString())
//            if (json.getString("Status").equals("Success")) {
//                Utilities.showToast(this, json.getString("Message"))
//                btnStatus.setVisibility(View.VISIBLE)
//                tvStatus.text = "Started"
//                btnStart.setVisibility(View.GONE)
//                tvViewMap.setVisibility(View.GONE)
//            } else {
//                Utilities.showToast(this, json.getString("Message"))
//
//            }
//        } else if (TYPE == ConstantsWFMS.TASK_STATUS_LIST_WFMS) {
//            val jsonArray = JSONArray((response as Response<*>).body()!!.toString())
//
//            val model = TwoParameterModel()
//            model.name = "Select a Status"
//            model.id = "-1"
//            listTaskStatus.add(model)
//            for (i in 0 until jsonArray.length()) {
//                val item = jsonArray.getJSONObject(i)
//
//                val model = TwoParameterModel()
//                model.id = item.getString("CompanyNatureId")
//                model.name = item.getString("TapLocationTypeName")
//                listTaskStatus.add(model)
//            }
//
//
//            taskStatusListArray = arrayOfNulls<String>(listTaskStatus.size)
//            for (i in listTaskStatus.indices) {
//                taskStatusListArray[i] = listTaskStatus.get(i).name
//            }
//
//
//        } else if (TYPE == ConstantsWFMS.NEW_TASK_STATUS_WFMS) {
//            val json = JSONObject((response as Response<*>).body()!!.toString())
//            if (json.getString("Status").equals("Success")) {
//                Utilities.showToast(this, json.getString("Message"))
//
//                if (status.equals("Pending")) {
//                    dialog.dismiss()
//
//                } else {
//                    value = +1;
//                    if (value != taskListSelected.size - 1)
//                        completeImageListToUpload()
//                    else {
//                        finish()
//
//                        dialog.dismiss()
//
//                    }
//                }
//
//            } else {
//                Utilities.showToast(this, json.getString("Message"))
//
//            }
//
//        }
//    }
//
//
//    fun imagePath(imgPath: String, imgFrom: Int) {
//        if (imgFrom == 0) {
//            try {
//                val sourceUri: Uri = Uri.parse(imgPath)
//                val file: File? = getImageFile()
//                val destinationUri = Uri.fromFile(file)
//                openCropActivity(sourceUri, destinationUri)
//            } catch (e: java.lang.Exception) {
//                UtilitiesWFMS.showToast(this, "Please select another image")
//            }
//
//        } else {
//            val uri = Uri.parse(imgPath)
//            openCropActivity(uri, uri)
//        }
//
//
//    }
//
//    private fun openCropActivity(sourceUri: Uri, destinationUri: Uri) {
////        UCrop.of(sourceUri, destinationUri)
////                .withMaxResultSize(120, 90)
////                .withAspectRatio(5f, 5f)
////                .start(this)
//    }
//
//    override fun callPosition(pos: Int) {
//        position = pos
//        if (isCompleted) {
//            Utilities.showToast(this@PerformActivityNew, "You can't chnage your activity images")
//        } else {
//            onSelectImageClick()
//            //  checkPermissions("com.htistelecom.htisinhouse.activity.NewKnjrkhana.activity.PerformActivityNew")
//
//        }
//    }
//
//
//    @Throws(IOException::class)
//    private fun getImageFile(): File? {
//        val mDir = File(Environment.getExternalStorageDirectory(), "WFMS")
//        if (!mDir.exists()) {
//            mDir.mkdir()
//        }
//        val imageFileName = "JPEG_" + System.currentTimeMillis() + "_"
//
//        val file = File.createTempFile(
//                imageFileName, ".jpg", mDir
//        )
//        return file
//    }
//
//
//    fun onSelectImageClick() {
//        CropImage.startPickImageActivity(this)
//    }
//
//    @SuppressLint("NewApi")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        // handle result of pick image chooser
//        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val imageUri: Uri = CropImage.getPickImageResultUri(this, data)
//
//            // For API >= 23 we need to check specifically that we have permissions to read external storage.
//            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
//                // request permissions and handle the result in onRequestPermissionsResult()
//                mCropImageUri = imageUri
//                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
//            } else {
//                // no permissions required or already grunted, can start crop image activity
//                startCropImageActivity(imageUri)
//            }
//        }
//
//        // handle result of CropImageActivity
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
//            if (resultCode == Activity.RESULT_OK) {
//
//                var s = result.getUri()
//                //  (findViewById<View>(R.id.quick_start_cropped_image) as ImageButton).setImageURI(File())
//                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show()
//                taskListSelected.get(position).activityImage = File(result.uri.path).absolutePath
//                taskListSelected.get(position).activityImagePath = ""
//                adapter.notifyDataSetChanged()
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (mCropImageUri != null && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            // required permissions granted, start crop image activity
//            startCropImageActivity(mCropImageUri)
//        } else {
//            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show()
//        }
//    }
//
//
//    /**
//     * Start crop image activity for the given image.
//     */
//    private fun startCropImageActivity(imageUri: Uri) {
//        CropImage.activity(imageUri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setMultiTouchEnabled(true)
//                .start(this)
//    }
//
//}
//
//
