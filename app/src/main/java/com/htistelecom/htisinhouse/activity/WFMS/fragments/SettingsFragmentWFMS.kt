package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.activity.ChangePasswordActivityWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import kotlinx.android.synthetic.main.fragment_settings_wfms.*

class SettingsFragmentWFMS : Fragment() {
    lateinit var tinyDB: TinyDB
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_wfms, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)
        if (tinyDB.getString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE).equals(""))
            Glide.with(activity!!).load(R.drawable.icon_man).into(ivProfileSettingsWFMS);
        else
            Glide.with(activity!!).load(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_PROFILE_IMAGE)).into(ivProfileSettingsWFMS);
        tvProfileNameSettingsWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_NAME)
        tvDeptSettingsWFMS.text = tinyDB.getString(ConstantsWFMS.TINYDB_EMP_DEPT)



        llChangePasswordWFMS.setOnClickListener { view ->
            val intent = Intent(activity!!, ChangePasswordActivityWFMS::class.java)
            startActivity(intent)
        }

    }
}