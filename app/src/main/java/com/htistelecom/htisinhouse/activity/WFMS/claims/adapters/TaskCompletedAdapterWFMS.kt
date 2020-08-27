package com.htistelecom.htisinhouse.activity.WFMS.claims.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.claims.AddClaimActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.claims.SingleTaskClaimDetailActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.claims.models.ClaimSummaryModel
import com.htistelecom.htisinhouse.activity.WFMS.models.CompletedTaskModel
import kotlinx.android.synthetic.main.row_task_completed_adapter_wfms.view.*


class TaskCompletedAdapterWFMS(private val mContext: Context, val taskList: ArrayList<CompletedTaskModel>,val claimSummaryModel: ClaimSummaryModel) : RecyclerView.Adapter<TaskCompletedAdapterWFMS.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        // create a new view
        val itemLayoutView = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_task_completed_adapter_wfms, viewGroup, false)
        // create ViewHolder
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mContext,taskList.get(position),claimSummaryModel)

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, model: CompletedTaskModel,summaryModel:ClaimSummaryModel) {
            itemView.tvProjectNameRowTaskCompletedAdapterWFMS.text=model.projectName
            itemView.tvSiteNameRowTaskCompletedAdapterWFMS.text=model.siteName
            itemView.tvTaskNameRowTaskCompletedAdapterWFMS.text=model.activityText
            itemView.tvTotalAmountRowTaskCompletedAdapterWFMS.text=model.claimedAmount
            itemView.tvApprovedAmountRowTaskCompletedAdapterWFMS.text=model.approvedAmount
            itemView.ivAddClaimRowTaskCompletedAdapterWFMS.setOnClickListener { view ->
                mContext.startActivity(Intent(mContext, AddClaimActivityWFMS::class.java).putExtra("TaskId",model.siteUploadedId))
                (mContext as Activity).finish()

            }
            itemView.ivDetailClaimRowTaskCompletedAdapterWFMS.setOnClickListener { view ->
                mContext.startActivity(Intent(mContext, SingleTaskClaimDetailActivityWFMS::class.java).putExtra("TaskId",model.siteUploadedId).putExtra("Date",model.workDate).putExtra("data",summaryModel))
                (mContext as Activity).finish()


            }


        }


    }


}