package com.htistelecom.htisinhouse.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TaskModel
import kotlinx.android.synthetic.main.row_marketing_task.view.*

class MarketingTaskAdapter(val activity: Activity, val arrayList: ArrayList<TaskModel>) : RecyclerView.Adapter<MarketingTaskAdapter.MyHolder>() {

    companion object {
        //var data= TaskModel()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_marketing_task, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(arrayList.get(pos),activity)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: TaskModel, activity: Activity) {
            itemView.tvCompanyName.text=" : "+model.CompanyName
            itemView.tvEmail.text=" : "+model.EmailID
            itemView.tvMobielNumber.text=" : "+model.Phone
            itemView.tvLeadGenerated.text=" : "+model.LeadGenerated
            itemView.tvAddressMarketingTaskAdapter.text=model.Address
            itemView.tvDate.text=" : "+model.TaskDate







        }
    }
}