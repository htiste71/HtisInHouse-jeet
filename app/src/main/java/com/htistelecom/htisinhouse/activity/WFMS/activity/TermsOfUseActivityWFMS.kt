package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.htistelecom.htisinhouse.R
import kotlinx.android.synthetic.main.toolbar.*

class TermsOfUseActivityWFMS : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use_wfms)
        ivDrawer.visibility = View.GONE

        tv_title.text = "Terms of Use"
        ivBack.setOnClickListener { view -> finish() }
    }
}