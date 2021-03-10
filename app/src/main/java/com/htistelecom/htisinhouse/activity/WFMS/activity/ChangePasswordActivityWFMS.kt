package com.htistelecom.htisinhouse.activity.WFMS.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.TINYDB_EMAIL
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_change_password_wfms.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class ChangePasswordActivityWFMS : Activity(), View.OnClickListener, MyInterface {
    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()

        if ((response as Response<*>).code() == 401 ||  (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()

            finish()
            com.htistelecom.htisinhouse.utilities.ConstantKotlin.logout(this,tinyDB)
        } else {

            var jsonObj = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObj.getString("Status").equals("Success")) {
                Utilities.showToast(this, jsonObj.getString("Message"))
                tinyDB.putString(ConstantsWFMS.TINYDB_PASSWORD, mNewPassword)
                backToHome()
            } else {
                Utilities.showToast(this, jsonObj.getString("Message"))

            }
        }
    }

    private var mNewPassword: String = ""
    lateinit var tinyDB: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_wfms)
        tinyDB = TinyDB(this)
        init()
    }

    private fun init() {
        tv_title.setText("Change Password")
        ivDrawer.setVisibility(View.GONE)
        ivBack.setOnClickListener(this)
        btnChangePassword.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivBack -> {
                backToHome()
            }
            R.id.btnChangePassword -> changePassword()
            R.id.btnCancel -> {
                backToHome()
            }

        }
    }

    private fun backToHome() {
        startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Settings"))
        finish()
    }

    private fun changePassword() {
        val mCurrentPassword = etCurrentPassword.getText().toString().trim({ it <= ' ' })
        mNewPassword = etNewPassword.getText().toString().trim({ it <= ' ' })
        val mConfirmNewPassword = etConfirmNewPassword.getText().toString().trim({ it <= ' ' })


        if (mCurrentPassword.equals("", ignoreCase = true)) {
            Utilities.showToast(this, getString(R.string.err_current_password))
            return
        } else if (!mCurrentPassword.equals(tinyDB.getString(ConstantsWFMS.TINYDB_PASSWORD), ignoreCase = true)) {
            Utilities.showToast(this, getString(R.string.err_current_password_not_correct))
            return
        } else if (mNewPassword.length == 0) {
            Utilities.showToast(this, resources.getString(R.string.err_newpassword_empty))
            return
        } else if (mNewPassword.length < 8) {
            Utilities.showToast(this, "Password length should be minimum 8 digits")
            return
        } else if (!Utilities.isValidPassword(mNewPassword)) {
            Utilities.showToast(this, "Password must be 8 chars contains 1 uppercase letter, 1 special character and alphanumeric characters")
            return
        } else if (mConfirmNewPassword.length == 0) {
            Utilities.showToast(this, resources.getString(R.string.err_confirm_password_empty))
            return
        } else if (mNewPassword != mConfirmNewPassword) {
            Utilities.showToast(this, resources.getString(R.string.err_password_not_match))
            return
        } else {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("EmpPassword", mCurrentPassword)
                jsonObject.put("EmpNewPassword", mNewPassword)
                jsonObject.put("EmpEmail", tinyDB.getString(TINYDB_EMAIL))

                ApiData.getData(jsonObject.toString(), ConstantsWFMS.CHANGE_PASSWORD_WFMS, this, this)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

    }

    override fun onBackPressed() {
        backToHome()
    }
}