package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel

class ChatDetailAdapterWFMS(val mContext: Activity) : RecyclerView.Adapter<ChatDetailAdapterWFMS.MyHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_chat_detail_adapter_wfms, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return 13
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        //  holder.bind(mContext,claimModelsList.get(position))
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, model: TaskListModel) {


        }


    }
}