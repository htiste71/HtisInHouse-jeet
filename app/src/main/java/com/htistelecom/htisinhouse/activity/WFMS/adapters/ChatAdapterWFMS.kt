package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.ChatDetailActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import kotlinx.android.synthetic.main.row_chat_adapter_wfms.view.*
import kotlinx.android.synthetic.main.row_team_adapter_wfms.view.*

class ChatAdapterWFMS(private val mContext: Activity, val list: ArrayList<MyTeamModel>) : RecyclerView.Adapter<ChatAdapterWFMS.MyHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_chat_adapter_wfms, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(mContext, list.get(position))
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, model: MyTeamModel) {

            itemView.tvProfileNameRowChatAdapterWFMS.text = model.empName
            itemView.tvNewMessageRowChatAdapterWFMS.text = "Test"
            itemView.tvMessageDateRowChatAdapterWFMS.text = ""
            if (model.empImg.equals("")) {
                Glide.with(itemView).load(R.drawable.icon_man).into(itemView.ivProfileImageRowChatAdapterWFMS);

            } else {
                Glide.with(itemView).load(model.empImgPath + model.empImg).into(itemView.ivProfileImageRowChatAdapterWFMS);


            }

            itemView.setOnClickListener { view ->
                val intent = Intent(mContext, ChatDetailActivityWFMS::class.java)
                mContext!!.startActivity(intent)
            }
            itemView.setOnClickListener { v ->
                mContext.startActivity(Intent(mContext, ChatDetailActivityWFMS::class.java).putExtra("model",model))
            }
        }


    }
}