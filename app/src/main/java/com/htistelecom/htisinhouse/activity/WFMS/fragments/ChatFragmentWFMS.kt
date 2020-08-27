package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.adapters.ChatAdapterWFMS
import kotlinx.android.synthetic.main.fragment_chat_wfms.*

class ChatFragmentWFMS: Fragment() {
    //lateinit var adapter:ChatAdapterWFMS
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_wfms,null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvChatFragmentWFMS.layoutManager=LinearLayoutManager(activity,VERTICAL,false)
        rvChatFragmentWFMS.adapter=ChatAdapterWFMS(activity!!)


    }
}