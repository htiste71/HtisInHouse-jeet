package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.PerformActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import kotlinx.android.synthetic.main.row_task_adapter_wfms.view.*
import java.util.ArrayList

class TaskAdapterWFMS (private val mContext: Context?, val taskAL: ArrayList<TaskListModel>) : RecyclerView.Adapter<TaskAdapterWFMS.MyHolderBottomSheet>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolderBottomSheet {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_task_adapter_wfms, viewGroup, false)
        return TaskAdapterWFMS.MyHolderBottomSheet(view)
    }

    override fun getItemCount(): Int {
        return taskAL.size
    }

    override fun onBindViewHolder(holder: MyHolderBottomSheet, position: Int) {
        holder.bind(mContext!!, taskAL.get(position))
    }

    class MyHolderBottomSheet(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, model: TaskListModel) {
            itemView.tvSiteNameRowTaskAdapterWFMS!!.text = model.projectName
            itemView.tvSiteAddressRowTaskAdapterWFMS!!.text = model.siteName
            itemView.tvTaskNameRowTaskAdapterWFMS!!.text = model.activityName
            itemView.tvDateRowTaskAdapterWFMS!!.text = model.workDate

            if (model.status.equals("Completed")) {
                itemView.ivArrowRowTaskAdapterWFMS.visibility = View.GONE
            } else {
                itemView.ivArrowRowTaskAdapterWFMS.visibility = View.VISIBLE

                if (model.status.equals("Started")) {
                    val imagename = "icon_arrow_green"
                    val res: Int = mContext.resources.getIdentifier(imagename, "drawable", mContext.getPackageName())
                    itemView.ivArrowRowTaskAdapterWFMS.setImageResource(res)


                } else if (model.status.equals("Assigned")) {
                    val imagename = "icon_arrow_orange"
                    val res: Int = mContext.resources.getIdentifier(imagename, "drawable", mContext.getPackageName())
                    itemView.ivArrowRowTaskAdapterWFMS.setImageResource(res)
                } else if (model.status.equals("Pending")) {
                    val imagename = "icon_arrow_red"
                    val res: Int = mContext.resources.getIdentifier(imagename, "drawable", mContext.getPackageName())
                    itemView.ivArrowRowTaskAdapterWFMS.setImageResource(res)
                }
            }


            itemView.setOnClickListener { view ->
                val intent = Intent(mContext, PerformActivityWFMS::class.java)
                intent.putExtra("data", model)
                intent.putExtra("fromHome",false)
                mContext.startActivity(intent)


            }


        }


    }

}