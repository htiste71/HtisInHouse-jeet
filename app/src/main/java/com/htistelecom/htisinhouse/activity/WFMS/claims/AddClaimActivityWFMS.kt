package com.htistelecom.htisinhouse.activity.WFMS.claims

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_SUBMIT_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.TRANSPORT_MODE_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS.Companion.dateToString
import com.htistelecom.htisinhouse.activity.WFMS.activity.BaseActivityCamera
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_add_claim_wfms.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddClaimActivityWFMS : BaseActivityCamera(), MyInterface, View.OnClickListener {
    private var fileToUploadImage1: MultipartBody.Part? = null
    var fileToUploadImage2: MultipartBody.Part? = null
    private var fileToUploadImage3: MultipartBody.Part? = null
    private var fileToUploadImage4: MultipartBody.Part? = null

    var mImage1PathFile: File? = null
    var mImage2PathFile: File? = null
    var mImage3PathFile: File? = null
    var mImage4PathFile: File? = null

    private var mTransportModeId: String = "1"
    lateinit var transportModeAdapter: ArrayAdapter<String?>
    lateinit var transportModeArray: Array<String?>
    private val transportModeList = ArrayList<TwoParameterModel>()
    var imagesAl = java.util.ArrayList<MultipartBody.Part>()

    var IMAGE_TYPE = -1
    val IMAGE_1 = 1
    val IMAGE_2 = 2
    val IMAGE_3 = 3
    val IMAGE_4 = 4

    var mFromLocation = ""
    var mToLocation = ""
    var mDistance = ""
    var mAmount = ""
    var mRemarks = ""
    var mTaskId = ""

    val dialog: Dialog? = null

   lateinit var tinyDB: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_claim_wfms)
        initViews()
        listeners()
        api()
    }

    private fun api() {
        callAPIMethod(TRANSPORT_MODE_WFMS, "")
    }

    private fun initViews() {


        tv_title.setText(R.string.new_claim)
        ivDrawer.setVisibility(View.GONE)
        mTaskId = intent.getStringExtra("TaskId")
        tinyDB = TinyDB(this)
        // callAPIMethod(TRANSPORT_MODE_WFMS, "")

    }

    private fun listeners() {
        imgView1AddClaimActivityWFMS.setOnClickListener(this)
        imgView2AddClaimActivityWFMS.setOnClickListener(this)
        imgView3AddClaimActivityWFMS.setOnClickListener(this)
        imgView4AddClaimActivityWFMS.setOnClickListener(this)
        btnSubmitAddClaimActivityWFMS.setOnClickListener(this)
        btnCancelAddClaimActivityWFMS.setOnClickListener(this)
        ivBack.setOnClickListener(this)

    }

    private fun callAPIMethod(TYPE: Int, params: String) {
        when (TYPE) {
            ConstantsWFMS.TRANSPORT_MODE_WFMS -> {
                ApiData.getGetData(TRANSPORT_MODE_WFMS, this, this)
            }


        }

    }


    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()

        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            finish()
            ConstantKotlin.logout(this, tinyDB)
        } else {

            when (TYPE) {
                TRANSPORT_MODE_WFMS -> {
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        val jsonArray = jsonObject.getJSONArray("Output")



                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)

                            val model = TwoParameterModel()
                            model.id = item.getString("ModeId")
                            model.name = item.getString("ModeName")
                            transportModeList.add(model)
                        }


                        transportModeArray = arrayOfNulls<String>(transportModeList.size)
                        for (i in transportModeList.indices) {
                            transportModeArray[i] = transportModeList.get(i).name
                        }




                        transportModeAdapter = ArrayAdapter(this, R.layout.spinner_item, transportModeArray)

                        transportModeSpnrAddClaimActivityWFMS.setAdapter(transportModeAdapter)
                        transportModeSpnrAddClaimActivityWFMS.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                mTransportModeId = transportModeList.get(position).id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // TODO Auto-generated method stub
                            }
                        })
                    } else {
                        Utilities.showToast(this, "Please try again")
                    }
                }
                CLAIM_SUBMIT_WFMS -> {
                    val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                    if (jsonObject.getString("Status").equals("Success")) {
                        Utilities.showToast(this, jsonObject.getString("Message"))
                        backToHome()
                    }
                }

            }
        }
    }

    fun backToHome() {
        // startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Claim"))
        finish()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgView1AddClaimActivityWFMS -> {
                IMAGE_TYPE = IMAGE_1
                callImageMethod()
            }
            R.id.imgView2AddClaimActivityWFMS -> {
                IMAGE_TYPE = IMAGE_2
                callImageMethod()

            }
            R.id.imgView3AddClaimActivityWFMS -> {
                IMAGE_TYPE = IMAGE_3
                callImageMethod()
            }
            R.id.imgView4AddClaimActivityWFMS -> {
                IMAGE_TYPE = IMAGE_4




                callImageMethod()


            }
            R.id.ivBack -> {
                backToHome()
            }
            R.id.btnCancelAddClaimActivityWFMS -> {
                backToHome()
            }
            R.id.btnSubmitAddClaimActivityWFMS -> {
                mFromLocation = etFromLocAddClaimActivityWFMS.text.toString()
                mToLocation = etToLocAddClaimActivityWFMS.text.toString()
                mDistance = etDistanceAddClaimActivityWFMS.text.toString()
                mAmount = etAmountAddClaimActivityWFMS.text.toString()
                mRemarks = etRemarksAddClaimActivityWFMS.text.toString()
                if (mFromLocation.equals(""))
                    UtilitiesWFMS.showToast(this, resources.getString(R.string.errFromLocation))
                else if (mToLocation.equals(""))
                    UtilitiesWFMS.showToast(this, resources.getString(R.string.errToLocation))
                else if (mDistance.equals(""))
                    UtilitiesWFMS.showToast(this, resources.getString(R.string.errDistanceTravelled))
                else if (mAmount.equals(""))
                    UtilitiesWFMS.showToast(this, resources.getString(R.string.errAmount))
                else if (mRemarks.equals(""))
                    UtilitiesWFMS.showToast(this, resources.getString(R.string.errRemarks))
                else {

                    if (mImage1PathFile != null) {
                        val mFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), mImage1PathFile!!)
                        fileToUploadImage1 = MultipartBody.Part.createFormData("file", mImage1PathFile!!.name, mFile)
                        imagesAl.add(fileToUploadImage1!!)

                    }
                    if (mImage2PathFile != null) {
                        val mFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), mImage2PathFile!!)
                        fileToUploadImage2 = MultipartBody.Part.createFormData("file", mImage2PathFile!!.name, mFile)
                        imagesAl.add(fileToUploadImage2!!)

                    }
                    if (mImage3PathFile != null) {
                        val mFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), mImage3PathFile!!)
                        fileToUploadImage3 = MultipartBody.Part.createFormData("file", mImage3PathFile!!.name, mFile)
                        imagesAl.add(fileToUploadImage3!!)

                    }
                    if (mImage4PathFile != null) {
                        val mFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), mImage4PathFile!!)
                        fileToUploadImage4 = MultipartBody.Part.createFormData("file", mImage4PathFile!!.name, mFile)
                        imagesAl.add(fileToUploadImage4!!)

                    }
                    var parts: Array<MultipartBody.Part?>? = null
                    if (imagesAl.size > 0) {
                        parts = arrayOfNulls(imagesAl.size)
                        imagesAl.toArray(parts)
                    }
                    // RequestBody mId = RequestBody.create(MediaType.parse("text/plain"), listActivity.get(i).getId());
                    // model.setId(mId);
                    //  model.setPart(fileToUpload);
                    // RequestBody mId = RequestBody.create(MediaType.parse("text/plain"), listActivity.get(i).getId());
                    // model.setId(mId);
                    //  model.setPart(fileToUpload);
                    val rightNow: Calendar = Calendar.getInstance()

                    val jsonObject = JSONObject()
                    jsonObject.put("TaskId", mTaskId)
                    jsonObject.put("Date", dateToString(Calendar.getInstance().time))
                    jsonObject.put("EmpId", tinyDB!!.getString(ConstantsWFMS.TINYDB_EMP_ID))
                    jsonObject.put("FromLoc", mFromLocation)
                    jsonObject.put("ToLoc", mToLocation)
                    jsonObject.put("Remarks", mRemarks)
                    jsonObject.put("Amount", mAmount)
                    jsonObject.put("DistanceTraveled", mDistance)
                    jsonObject.put("TransportMode", mTransportModeId)
                    val mediaType = "application/json; charset=utf-8".toMediaType()
                    val requestBody = jsonObject.toString().toRequestBody(mediaType)
                    ApiData.forImageDataArray(parts, requestBody, CLAIM_SUBMIT_WFMS, this, this)


                }


            }

        }
    }

    private fun callImageMethod() {
        checkPermissions("com.htistelecom.htisinhouse.activity.WFMS.claims.AddClaimActivityWFMS")
    }

    fun imagePath(imgPath: File) {
        if (IMAGE_TYPE == IMAGE_1) {
            mImage1PathFile = imgPath
            Glide.with(this).load(imgPath).into(imgView1AddClaimActivityWFMS);

        } else if (IMAGE_TYPE == IMAGE_2) {
            mImage2PathFile = imgPath

            Glide.with(this).load(imgPath).into(imgView2AddClaimActivityWFMS);

        } else if (IMAGE_TYPE == IMAGE_3) {
            mImage3PathFile = imgPath

            Glide.with(this).load(imgPath).into(imgView3AddClaimActivityWFMS);

        } else if (IMAGE_TYPE == IMAGE_4) {
            mImage4PathFile = imgPath

            Glide.with(this).load(imgPath).into(imgView4AddClaimActivityWFMS);

        }
        // fileToUpload = MultipartBody.Part.createFormData("ActivityImage", imgFile.name, mFile)

    }
}

