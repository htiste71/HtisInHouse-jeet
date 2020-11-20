package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_custom_domain_form.*
import org.json.JSONObject
import retrofit2.Response

class CustomUrlActivity : Activity(), MyInterface {


    private var mDomainName: String = ""
    lateinit var tinyDB: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_domain_form)
        tinyDB = TinyDB(this)
        btnBack.setOnClickListener { view -> finish() }
        btnContinueCustomUrl.setOnClickListener { view ->
             mDomainName = etCustomUrl.text.toString()+".htistelecom.in"
           // mDomainName = etCustomUrl.text.toString()

            // mDomainName="htis"
            if (mDomainName.equals("")) {
                Utilities.showToast(this, resources.getString(R.string.errCustomUrl))
            } else {
                // mDomainName="aditya"
                var json = JSONObject()
                json.put("DomainName", mDomainName)

                ApiData.getData(json.toString(), ConstantsWFMS.DOMAIN_VERIFICATION_WFMS, this, this)

            }
        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == ConstantsWFMS.DOMAIN_VERIFICATION_WFMS) {
            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                Utilities.showToast(this, jsonObj.getString("Message"))
                tinyDB.putString(ConstantsWFMS.TINYDB_DOMAIN_ID, jsonObj.getString("DomainId"))

                tinyDB.putBoolean(ConstantsWFMS.TINYDB_URL_VERIFIED, true)
                Utilities.showToast(this, jsonObj.getString("Message"))

                val intent = Intent(this, VerifyEmailActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Utilities.showToast(this, jsonObj.getString("Message"))

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}