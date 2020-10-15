package com.htistelecom.htisinhouse.activity.WFMS.leave_managment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.models.ODListModel
import kotlinx.android.synthetic.main.row_comp_off.view.*

class ODListAdapter(val ctx: Context, val alData: ArrayList<ODListModel>, val TYPE: Int) : RecyclerView.Adapter<ODListAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_comp_off, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return alData.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(alData.get(pos), ctx, TYPE)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: ODListModel, activity: Context, type: Int) {
            val FOR_SUBMITTED = 1
            val FOR_APPROVED = 2
            val FOR_REJECTED = 3
            if (model.odType.equals("F", true)) {
                itemView.llDateODListAdapter.visibility=View.GONE
                itemView.llFromDateODListAdapter.visibility=View.VISIBLE
                itemView.llToDateODListAdapter.visibility=View.VISIBLE
                itemView.llFromTimeODListAdapter.visibility=View.GONE
                itemView.llToTimeODListAdapter.visibility=View.GONE
                itemView.tvFromDateODStatus.text = model.fromDate
                itemView.tvToDateODStatus.text = model.uptoDate
            } else {
                itemView.llDateODListAdapter.visibility=View.VISIBLE
                itemView.llFromDateODListAdapter.visibility=View.GONE
                itemView.llToDateODListAdapter.visibility=View.GONE
                itemView.llFromTimeODListAdapter.visibility=View.VISIBLE
                itemView.llToTimeODListAdapter.visibility=View.VISIBLE
                itemView.tvDateODStatus.text=model.fromDate
                itemView.tvInTimeODStatus.text = model.fromTime
                itemView.tvOutTimeODStatus.text = model.uptoTime
            }


            itemView.tvLeaveReasonODStatus.text = model.odPurpose
            if (model.odType.equals("F"))
                itemView.tvTypeODStatus.text = "Full Day"
            else
                itemView.tvTypeODStatus.text = "Short Time"

            if (type == FOR_SUBMITTED) {

                itemView.tvLeaveStatusODStatus.text = "Pending"
            } else if (type == FOR_APPROVED) {
                itemView.tvLeaveStatusODStatus.text = "Approved"

            } else if (type == FOR_REJECTED) {
                itemView.tvLeaveStatusODStatus.text = "Rejected"

            }


        }


    }
}