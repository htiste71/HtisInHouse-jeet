package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_verify_email.*
import org.json.JSONObject
import retrofit2.Response

class VerifyEmailActivity : Activity(), MyInterface {


    lateinit var tinyDB: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)
        tinyDB = TinyDB(this)

        btnBackVerifyEmail.setOnClickListener { view ->
            val intent = Intent(this, CustomUrlActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnSubmitVerifyEmail.setOnClickListener { view ->
            var email = etEmailVerifyEmail.text.toString()
          //  email="shobinkumar25@gmail.com"
          //  email="kuldeep.singh@horizontelecom.in"

            if (TextUtils.isEmpty(email)) run {
                Utilities.showToast(this, "Please enter email address")

            } else if (!Utilities.emailPatterns(email)) run {
                Utilities.showToast(this, "Please enter valid email address")

            } else {
               // email="shobinjindal91@gmail.com"
                var json = JSONObject()
                json.put("DomainId", tinyDB.getString(ConstantsWFMS.TINYDB_DOMAIN_ID))
                json.put("EmpEmail", email)
                json.put("DeviceIMEI", tinyDB.getString(ConstantsWFMS.TINYDB_IMEI_NUMBER))

                ApiData.getData(json.toString(), ConstantsWFMS.EMAIL_VERIFICATION_WFMS, this, this)


            }
        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == ConstantsWFMS.EMAIL_VERIFICATION_WFMS) {
            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {


                Utilities.showToast(this, jsonObj.getString("Message"))
                tinyDB.putString(ConstantsWFMS.TINYDB_DOMAIN_ID, jsonObj.getString("DomainId"))
                tinyDB.putString(ConstantsWFMS.TINYDB_EMAIL, jsonObj.getString("Email"))

                tinyDB.putBoolean(ConstantsWFMS.TINYDB_EMAIL_VERIFIED, true)
                Utilities.showToast(this,jsonObj.getString("Message"))
                val intent = Intent(this, LoginNewActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                Utilities.showToast(this, jsonObj.getString("Message"))

            }
        }
    }
}