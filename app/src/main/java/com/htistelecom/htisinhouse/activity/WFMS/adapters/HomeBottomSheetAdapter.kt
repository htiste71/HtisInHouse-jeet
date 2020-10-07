package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.PerformActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.dialog.SheetDialogFragment
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import kotlinx.android.synthetic.main.row_home_bottom_sheet.view.*
import java.util.*


class HomeBottomSheetAdapter(private val mContext: Context?, val taskAL: ArrayList<TaskListModel>, val sheetDialogFragment: SheetDialogFragment) : RecyclerView.Adapter<HomeBottomSheetAdapter.MyHolderBottomSheet>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolderBottomSheet {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_home_bottom_sheet, viewGroup, false)
        return HomeBottomSheetAdapter.MyHolderBottomSheet(view)
    }

    override fun getItemCount(): Int {
        return taskAL.size
    }

    override fun onBindViewHolder(holder: MyHolderBottomSheet, position: Int) {
        holder.bind(mContext!!, taskAL.get(position), sheetDialogFragment,taskAL)
    }

    class MyHolderBottomSheet(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, model: TaskListModel, sheetDialogFragment: SheetDialogFragment, taskAL: ArrayList<TaskListModel>) {
            itemView.tvSiteNameRowHomeBottomSheet!!.text = model.projectName
            itemView.tvSiteAddressRowHomeBottomSheet!!.text = model.siteName
            itemView.tvTaskNameRowHomeBottomSheet!!.text = model.activityName
            itemView.tvDateRowHomeBottomSheet!!.text = model.workDate

            if (model.status.equals("Completed")) {
                itemView.ivArrowRowHomeBottomSheet.visibility = View.GONE
            } else {
                itemView.ivArrowRowHomeBottomSheet.visibility = View.VISIBLE

                if (model.status.equals("Started")) {
                    val imagename = "icon_arrow_green"
                    val res: Int = mContext.resources.getIdentifier(imagename, "drawable", mContext.getPackageName())
                    itemView.ivArrowRowHomeBottomSheet.setImageResource(res)


                } else if (model.status.equals("Assigned")) {
                    val imagename = "icon_arrow_orange"
                    val res: Int = mContext.resources.getIdentifier(imagename, "drawable", mContext.getPackageName())
                    itemView.ivArrowRowHomeBottomSheet.setImageResource(res)
                } else if (model.status.equals("Pending")) {
                    val imagename = "icon_arrow_red"
                    val res: Int = mContext.resources.getIdentifier(imagename, "drawable", mContext.getPackageName())
                    itemView.ivArrowRowHomeBottomSheet.setImageResource(res)
                }
            }


            itemView.setOnClickListener { view ->
                val intent = Intent(mContext, PerformActivityWFMS::class.java)
                intent.putExtra("data", model)
                intent.putExtra("list",taskAL)
                intent.putExtra("fromHome", true)

                mContext.startActivity(intent)
                sheetDialogFragment.dismiss()


            }


        }


    }
}