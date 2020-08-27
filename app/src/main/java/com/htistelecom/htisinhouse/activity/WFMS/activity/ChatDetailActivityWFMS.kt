package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.adapters.ChatDetailAdapterWFMS
import kotlinx.android.synthetic.main.activity_chat_detail_wfms.*
import kotlinx.android.synthetic.main.toolbar_chat_detail.*

class ChatDetailActivityWFMS : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail_wfms)
        rvChatDetailActivityWFMS.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        rvChatDetailActivityWFMS.adapter = ChatDetailAdapterWFMS(this)
        ivBackChatDetailWFMS.setOnClickListener { view -> finish() }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}