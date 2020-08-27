package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.ChatDetailActivityWFMS

class ChatAdapterWFMS(private val mContext: Activity) : RecyclerView.Adapter<ChatAdapterWFMS.MyHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_chat_adapter_wfms, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return 13
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
         holder.bind(mContext)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context) {

//            itemView.tvProfileNameRowChatAdapterWFMS.text = ""
//            itemView.tvNewMessageRowChatAdapterWFMS.text = ""
//            itemView.tvNewMessageRowChatAdapterWFMS.text = ""

            itemView.setOnClickListener { view ->
                val intent = Intent(mContext, ChatDetailActivityWFMS::class.java)
                mContext!!.startActivity(intent)
            }

        }


    }
}