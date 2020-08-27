package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.LOGIN_WFMS
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_login_wfms.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response


class LoginNewActivity : Activity(), MyInterface, View.OnClickListener {


    private var passWord: String = ""
    lateinit var dialogForgot: Dialog
    lateinit var tinyDB: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_wfms)
        tinyDB = TinyDB(this)
        listeners()

        btnCustomDomain.setOnClickListener { view ->
            val intent = Intent(this, CustomUrlActivity::class.java)
            startActivity(intent)
        }
        tvForgotPasswordLoginNewActivity.setOnClickListener { view -> dialogForgotPassword() }
        btnLogin.setOnClickListener { view ->
            var email = etUserName.text.toString()
           // email="shobinkumar25@gmail.com"
            passWord = etPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                Utilities.showToast(this, "Please enter email address")

            } else if (!Utilities.emailPatterns(email)) {
                Utilities.showToast(this, "Please enter vaild email address")

            } else if (passWord.length < 8) {
                Utilities.showToast(this, "Password length should be minimum 8 digits")

            }
//            else if (!Utilities.isValidPassword(passWord))  {
//                Utilities.showToast(this, "Password must be 8 chars contains 1 uppercase letter, 1 special character and alphanumeric characters")
//
//            }
            else {


                var json = JSONObject()
                json.put("EmpEmail", email)
                json.put("EmpPassword", passWord)
                json.put("DeviceIMEI", tinyDB.getString(ConstantsWFMS.TINYDB_IMEI_NUMBER))
                json.put("DeviceToken", "")

                hitAPI(json.toString(),LOGIN_WFMS)


            }
        }
    }

    private fun hitAPI(params: String, TYPE: Int) {
        if(TYPE==LOGIN_WFMS)
        {
            ApiData.getData(params, ConstantsWFMS.LOGIN_WFMS, this, this)

        }

    }

    private fun listeners() {
        ivShowPasswordLoginNewActivity.setOnClickListener(this)
        ivHidePasswordLoginNewActivity.setOnClickListener(this)
    }

    override fun sendResponse(response: Any?, TYPE: Int) {


        Utilities.dismissDialog()
        if (TYPE == ConstantsWFMS.LOGIN_WFMS) {
            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {


                //  Utilities.showToast(this, jsonObj.getString("Message"))
                tinyDB.putString(ConstantsWFMS.TINYDB_EMP_ID, jsonObj.getString("EmpId"))
                tinyDB.putString(ConstantsWFMS.TINYDB_EMP_CODE, jsonObj.getString("EmpCode"))
                tinyDB.putString(ConstantsWFMS.TINYDB_EMP_NAME, jsonObj.getString("EmpName"))
                tinyDB.putString(ConstantsWFMS.TINYDB_EMP_DESIGNATION, jsonObj.getString("EmpDesignation"))
                tinyDB.putString(ConstantsWFMS.TINYDB_EMP_DEPT, jsonObj.getString("EmpDepartment"))
                tinyDB.putString(ConstantsWFMS.TINYDB_EMP_MOBILE, jsonObj.getString("EmpMobileNo"))
                tinyDB.putString(ConstantsWFMS.TINYDB_EMAIL, jsonObj.getString("EmpEmail"))
                tinyDB.putString(ConstantsWFMS.TINYDB_PASSWORD, passWord)
                if (!jsonObj.getString("EmpImg").equals(""))
                    tinyDB.putString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE, jsonObj.getString("EmpImgPath") + jsonObj.getString("EmpImg"))
                else
                    tinyDB.putString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE, "")
                tinyDB.putBoolean(ConstantsWFMS.TINYDB_IS_LOGIN, true)
                Utilities.showToast(this, "Login In Successfully")
                val intent = Intent(this, MainActivityNavigation::class.java)
                startActivity(intent)
                finish()
            } else {
                Utilities.showToast(this, jsonObj.getString("Message"))

            }
        } else if (TYPE == ConstantsWFMS.FORGOT_PASSWORD_WFMS) {
            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                Utilities.showToast(this, jsonObj.getString("Message"))
                dialogForgot.dismiss()
            } else {
                Utilities.showToast(this, jsonObj.getString("Message"))

            }
        }
    }

    fun dialogForgotPassword() {
        dialogForgot = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialogForgot.setContentView(R.layout.forgot_password)
        dialogForgot.setCancelable(false)
        val etEmail = dialogForgot.findViewById<EditText>(R.id.etEmail)
        val ivCross = dialogForgot.findViewById<ImageView>(R.id.ivCross)
        val btnRegister = dialogForgot.findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener({ v ->

            val email = etEmail.getText().toString()
            if (TextUtils.isEmpty(email)) {
                Utilities.showToast(this, "Please Enter the Email Address")
                return@setOnClickListener
            } else {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("EmpEmail", email)
                    jsonObject.put("DeviceIMEI", tinyDB.getString(ConstantsWFMS.TINYDB_IMEI_NUMBER))

                    ApiData.getData(jsonObject.toString(), ConstantsWFMS.FORGOT_PASSWORD_WFMS, this, this)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                //callForgotApi(email);
            }

        })
        dialogForgot.show()
        ivCross.setOnClickListener({ v ->
            if (dialogForgot.isShowing()) {
                dialogForgot.dismiss()
            }
        })
    }

    override fun onClick(v: View?) {
        if(v!!.id==R.id.ivShowPasswordLoginNewActivity)
        {
            etPassword.setTransformationMethod(null)
            ivShowPasswordLoginNewActivity.visibility=View.GONE
            ivHidePasswordLoginNewActivity.visibility=VISIBLE
            etPassword.setSelection(etPassword.text.length)

        }
      else  if(v!!.id==R.id.ivHidePasswordLoginNewActivity)
        {
            etPassword.setTransformationMethod(PasswordTransformationMethod())
            ivShowPasswordLoginNewActivity.visibility=View.VISIBLE
            ivHidePasswordLoginNewActivity.visibility=GONE
            etPassword.setSelection(etPassword.text.length)



        }
    }
}