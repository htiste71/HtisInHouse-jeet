package com.htistelecom.htisinhouse.activity.WFMS.leave_managment.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.leave_managment.LeaveType_OutdoorDutyFragment
import com.htistelecom.htisinhouse.activity.WFMS.leave_managment.models.CompOffListModel
import kotlinx.android.synthetic.main.row_comp_off_adapter.view.*

class CompOffAdapter(val ctx: Context, val alData: ArrayList<CompOffListModel>, val TYPE: Int, val frag: LeaveType_OutdoorDutyFragment) : RecyclerView.Adapter<CompOffAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_comp_off_adapter, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return alData.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(alData.get(pos), ctx, TYPE, frag)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val FOR_SUBMITTED = 1
        val FOR_APPROVED = 2
        val FOR_REJECTED = 3
        fun bind(model: CompOffListModel, activity: Context, type: Int, frag: LeaveType_OutdoorDutyFragment) {
            itemView.tvAttendanceDateCompOffAdapter.text = model.attendanceDate
            if (type == FOR_SUBMITTED) {
                if (model.statusId.equals("") || model.statusId.equals("0")) {
                    itemView.llCompOffDate.visibility = View.GONE
                    itemView.llCompOffApplyDate.visibility = View.GONE
                    itemView.llLeaveTypeDay.visibility = View.GONE
                    itemView.llLeaveType.visibility = View.GONE
                    itemView.llReason.visibility = View.GONE
                    itemView.btnApplyCompOffAdapter.visibility = View.VISIBLE
                    itemView.tvLeaveStatusCompOffAdapter.visibility = View.GONE

                    itemView.btnApplyCompOffAdapter.setOnClickListener { view ->
                        frag.sendCompId(model.compoffId)
                    }
                } else if (model.statusId.equals("1")) {
                    itemView.btnApplyCompOffAdapter.visibility = View.GONE
                    itemView.tvLeaveStatusCompOffAdapter.visibility = View.VISIBLE

                    itemView.llCompOffDate.visibility = View.VISIBLE
                    itemView.llCompOffApplyDate.visibility = View.VISIBLE
                    itemView.llLeaveTypeDay.visibility = View.VISIBLE
                    itemView.llLeaveType.visibility = View.VISIBLE
                    itemView.llReason.visibility = View.VISIBLE

                    itemView.tvDateCompOffAdapter.text = model.leaveFrom
                    itemView.tvApplyDateCompOffAdapter.text = model.requestedOn
                    itemView.tvLeaveTypeDayCompOffAdapter.text = model.attendanceDayType
                    itemView.tvLeaveTypeCompOffAdapter.text = model.leaveFromDayType
                    itemView.tvLeaveReasonCompOffAdapter.text = model.leavePurpose
                    itemView.tvLeaveStatusCompOffAdapter.text = "Pending"

                }
            } else if (type == FOR_APPROVED) {
                if (model.statusId.equals("2")) {
                    itemView.btnApplyCompOffAdapter.visibility = View.GONE
                    itemView.tvLeaveStatusCompOffAdapter.visibility = View.VISIBLE

                    itemView.llCompOffDate.visibility = View.VISIBLE
                    itemView.llCompOffApplyDate.visibility = View.VISIBLE
                    itemView.llLeaveTypeDay.visibility = View.VISIBLE
                    itemView.llLeaveType.visibility = View.VISIBLE
                    itemView.llReason.visibility = View.VISIBLE

                    itemView.tvDateCompOffAdapter.text = model.leaveFrom
                    itemView.tvApplyDateCompOffAdapter.text = model.requestedOn
                    itemView.tvLeaveTypeDayCompOffAdapter.text = model.attendanceDayType
                    itemView.tvLeaveTypeCompOffAdapter.text = model.leaveFromDayType
                    itemView.tvLeaveReasonCompOffAdapter.text = model.leavePurpose
                    itemView.tvLeaveStatusCompOffAdapter.text = "Approved"
                }
            } else if (type == FOR_REJECTED) {
                if (model.statusId.equals("3")) {
                    itemView.btnApplyCompOffAdapter.visibility = View.GONE
                    itemView.tvLeaveStatusCompOffAdapter.visibility = View.VISIBLE

                    itemView.llCompOffDate.visibility = View.VISIBLE
                    itemView.llCompOffApplyDate.visibility = View.VISIBLE
                    itemView.llLeaveTypeDay.visibility = View.VISIBLE
                    itemView.llLeaveType.visibility = View.VISIBLE
                    itemView.llReason.visibility = View.VISIBLE

                    itemView.tvDateCompOffAdapter.text = model.leaveFrom
                    itemView.tvApplyDateCompOffAdapter.text = model.requestedOn
                    itemView.tvLeaveTypeDayCompOffAdapter.text = model.attendanceDayType
                    itemView.tvLeaveTypeCompOffAdapter.text = model.leaveFromDayType
                    itemView.tvLeaveReasonCompOffAdapter.text = model.leavePurpose
                    itemView.tvLeaveStatusCompOffAdapter.text = "Rejected"
                }

            }


        }
    }
}