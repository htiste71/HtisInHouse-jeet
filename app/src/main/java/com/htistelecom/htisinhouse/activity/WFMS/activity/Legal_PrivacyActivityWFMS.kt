package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.htistelecom.htisinhouse.R
import kotlinx.android.synthetic.main.toolbar.*

class Legal_PrivacyActivityWFMS : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal_privacy_wfms)
        tv_title.text = "Legal & Privacy"
        ivDrawer.visibility= View.GONE

        ivBack.setOnClickListener { view -> finish() }
    }
}