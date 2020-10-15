package com.htistelecom.htisinhouse.activity.WFMS.leave_managment.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.models.LeaveListModel
import kotlinx.android.synthetic.main.row_leave_management.view.*
import kotlin.collections.ArrayList

class LeaveListAdapter(val ctx: Context, val alData: ArrayList<LeaveListModel>, val TYPE: Int) : RecyclerView.Adapter<LeaveListAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_leave_management, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return alData.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(alData.get(pos), ctx, TYPE)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val FOR_SUBMITTED = 1
        val FOR_APPROVED = 2
        val FOR_REJECTED = 3
        fun bind(model: LeaveListModel, activity: Context, type: Int) {
          //  val (mFromDate, mToDate)=  UtilitiesWFMS.leaveDate(model.leaveFromTo)
            val mFromDate=model.leaveFrom
            val mToDate=model.leaveTo
            itemView.tvFromDateLeaveManagement.text=mFromDate
            itemView.tvToDateLeaveManagement.text=mToDate
            itemView.tvLeaveTypeLeaveManagement.text = model.leaveType
            itemView.tvLeaveReasonLeaveManagement.text = model.leaveRemarks
            if (type == FOR_SUBMITTED) {
                itemView.tvLeaveStatusLeaveManagement.text = "Pending"
            } else if (type == FOR_APPROVED) {
                itemView.tvLeaveStatusLeaveManagement.text = "Approved"

            } else if (type == FOR_REJECTED) {
                itemView.tvLeaveStatusLeaveManagement.text = "Rejected"

            }

        }


    }
}