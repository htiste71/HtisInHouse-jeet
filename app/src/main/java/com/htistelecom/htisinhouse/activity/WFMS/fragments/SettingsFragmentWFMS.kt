package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.ChangePasswordActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.service.UploadDataServerService
import com.htistelecom.htisinhouse.config.TinyDB
import kotlinx.android.synthetic.main.fragment_settings_wfms.*
import java.util.*

class SettingsFragmentWFMS : Fragment(), View.OnClickListener {
    lateinit var tinyDB: TinyDB
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_wfms, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()


    }

    private fun listeners() {
        btnSendLogReportSettingsWFMS.setOnClickListener(this)
        llChangePasswordWFMS.setOnClickListener(this)
        llHelpSettingsWFMS.setOnClickListener(this)
        llTermsOfUseSettingsWFMS.setOnClickListener(this)
        llLegalPrivacySettingsWFMS.setOnClickListener(this)
    }

    private fun initViews() {
        tinyDB = TinyDB(activity)
        if (tinyDB.getString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE).equals(""))
            Glide.with(activity!!).load(R.drawable.icon_man).into(ivProfileSettingsWFMS);
        else
            Glide.with(activity!!).load(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE)).into(ivProfileSettingsWFMS);
        tvProfileNameSettingsWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_NAME)
        tvDeptSettingsWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_DEPT)
        tvDesignationSettingsWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_DESIGNATION)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.llChangePasswordWFMS) {
            val intent = Intent(activity!!, ChangePasswordActivityWFMS::class.java)
            startActivity(intent)
        } else if (v.id == R.id.btnSendLogReportSettingsWFMS) {
            val format = java.text.SimpleDateFormat("hh:mm aa dd MMM yyyy ", Locale.ENGLISH)

            activity!!.startService(Intent(activity, UploadDataServerService::class.java))
            UtilitiesWFMS.showToast(activity!!, "Syncing data")
            val date = Date()
            var mCurrentTime = format.format(date)
            LocalBroadcastManager.getInstance(MyApplication.context!!).sendBroadcast(Intent("FOR_UPDATE_TIME").putExtra("data", mCurrentTime)
                    .putExtra("IsCheckOut", false));

        } else if (v.id == R.id.llHelpSettingsWFMS) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://wfms.htistelecom.in/home/softwaresupport"))
            startActivity(browserIntent)
        } else if (v.id == R.id.llTermsOfUseSettingsWFMS) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://wfms.htistelecom.in/home/terms"))
            startActivity(browserIntent)
        } else if (v.id == R.id.llLegalPrivacySettingsWFMS) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://wfms.htistelecom.in/Home/privacy"))
            startActivity(browserIntent)
        }
    }

}