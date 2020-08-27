package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PROFILE_IMAGE_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PROFILE_WFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.LoginNewActivity
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.fragment.BaseFragment
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_profile_wfms.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File

class ProfileFragmentWFMS : BaseFragmentCamera(), MyInterface, View.OnClickListener {


    lateinit var tinyDB: TinyDB
    var mEmpId = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_wfms, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()


        listeners()


    }

    private fun listeners() {
        ivCameraIconProfileFragmentWFMS.setOnClickListener(this)
        btnLogoutProfileFragmentWFMS.setOnClickListener { view ->
            clearCredentials()

            startActivity(Intent(activity, LoginNewActivity::class.java))
            activity!!.finish()

        }
    }

    private fun clearCredentials() {
//        tinyDB.putString(ConstantsWFMS.TINYDB_DOMAIN_ID, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_EMAIL, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_EMP_ID, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_EMP_NAME, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_EMP_CODE, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_EMP_NAME, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_EMP_DESIGNATION, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_EMP_DEPT, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_EMP_MOBILE, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_PASSWORD, "")

//        tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_CHECKIN, false)
//        tinyDB.putString(ConstantsWFMS.TINYDB_CHECKIN_TIME, "")
//        tinyDB.putString(ConstantsWFMS.TINYDB_CHECKOUT_TIME, "")


    }

    private fun initViews() {
        tinyDB = TinyDB(activity)
        mEmpId = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID)
        hitAPI()

    }

    private fun hitAPI() {

        var json = JSONObject()
        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        ApiData.getData(json.toString(), ConstantsWFMS.PROFILE_WFMS, this, activity)

    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()

        if (TYPE == PROFILE_WFMS) {
            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {

                tvFirstNameProfileFragmentWFMS.text = jsonObj.getString("EmpName")
                tvDesignationProfileFragmentWFMS.text = jsonObj.getString("EmpDesignation")
                tvDeptProfileFragmentWFMS.text = jsonObj.getString("EmpDepartment")
                tvEmailProfileFragmentWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMAIL)
                tvContactProfileFragmentWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_MOBILE)
                if (!jsonObj.getString("EmpImg").equals(""))
                    Glide.with(this).load(jsonObj.getString("EmpImgPath") + jsonObj.getString("EmpImg")).into(ivProfileFragmentWFMS);
                else
                    Glide.with(this).load(R.drawable.icon_man).into(ivProfileFragmentWFMS);


            }

        } else if (TYPE == PROFILE_IMAGE_WFMS) {
            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                tinyDB.putString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE,jsonObj.getString("ImageURL")+jsonObj.getString("ImageName"))
                Utilities.showToast(activity, jsonObj.getString("Message"))
            }
        }


    }

    override fun onClick(v: View) {
        if (v.id == R.id.ivCameraIconProfileFragmentWFMS) {
            checkPermissions("com.htistelecom.htisinhouse.activity.NewKnjrkhana.fragments.ProfileFragmentWFMS", activity!!)


        }
    }

    fun imagePath(imgPath: File) {
        //  imgFile = imgPath

        Glide.with(activity!!).load(imgPath).into(ivProfileFragmentWFMS);
        val mFile = RequestBody.create("image/*".toMediaTypeOrNull(), imgPath)
        val fileToUpload = MultipartBody.Part.createFormData("EmpImage", imgPath.name, mFile)
        ApiData.forImageData(fileToUpload, mEmpId, PROFILE_IMAGE_WFMS, this, activity)

    }
}