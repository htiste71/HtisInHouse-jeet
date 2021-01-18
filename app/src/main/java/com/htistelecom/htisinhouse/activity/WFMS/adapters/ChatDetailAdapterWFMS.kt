package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.chat.MessageModel
import kotlinx.android.synthetic.main.layout_chat_me.view.*
import kotlinx.android.synthetic.main.layout_chat_other.view.*

class ChatDetailAdapterWFMS(val mContext: Activity, val msgList: ArrayList<MessageModel>) : RecyclerView.Adapter<ChatDetailAdapterWFMS.MyHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_chat_detail_adapter_wfms, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(mContext, msgList.get(position))
    }

    fun addNewMessage(myId: String, otherId: String, message: String, isMe: Boolean) {
        msgList.add(MessageModel(myId, otherId, message, isMe))
        notifyDataSetChanged()


    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, model: MessageModel) {
            if (model.isMe!!)
                itemView.tvMessageMeChatDetailAdapterWFMS.text = model.message
            else
                itemView.tvMessageOtherChatDetailAdapterWFMS.text = model.message

        }


    }
}