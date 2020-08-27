package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.htistelecom.htisinhouse.R
import kotlinx.android.synthetic.main.toolbar.*

class HelpActivityWFMS : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_wfms)
        tv_title.text="Help"
        ivDrawer.visibility= View.GONE
        ivBack.setOnClickListener { view -> finish() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}