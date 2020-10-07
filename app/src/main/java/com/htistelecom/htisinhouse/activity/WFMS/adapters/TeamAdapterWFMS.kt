package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.xizzhu.simpletooltip.ToolTip
import com.github.xizzhu.simpletooltip.ToolTipView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.TasksWeekDetailActivityWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import kotlinx.android.synthetic.main.row_team_adapter_wfms.view.*


class TeamAdapterWFMS(private val mContext: FragmentActivity?, val myTeamList: ArrayList<MyTeamModel>, val empId: String) : RecyclerView.Adapter<TeamAdapterWFMS.MyHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_team_adapter_wfms, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return myTeamList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(mContext, myTeamList.get(position), empId)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: FragmentActivity?, model: MyTeamModel, empId: String) {

            if (!empId.equals(model.empId)) {
                itemView.tvPersonNameRowTeamAdapterWFMS.text = model.empName
                itemView.tvTaskRowTeamAdapterWFMS.text = model.tasks
                itemView.tvCheckInRowTeamAdapterWFMS.text = "Punch In  " + model.checkInTime
                itemView.tvCheckOutRowTeamAdapterWFMS.text = "Punch Out  " + model.checkOutTime

                itemView.tvLocationRowTeamAdapterWFMS.text = model.empPunchCity
                if (model.empImg.equals("")) {
                    Glide.with(itemView).load(R.drawable.icon_man).into(itemView.ivProfileRowTeamAdapterWFMS);

                } else {
                    Glide.with(itemView).load(model.empImgPath + model.empImg).into(itemView.ivProfileRowTeamAdapterWFMS);


                }

                itemView.setOnClickListener { view ->
                    val intent = Intent(mContext, TasksWeekDetailActivityWFMS::class.java)
                    intent.putExtra("data", model)
                    intent.putExtra("UserProfile",false)

                    mContext!!.startActivity(intent)
                }
                itemView.ivInfoRowTeamAdapterWFMS.setOnClickListener { view ->
                    val toolTip = ToolTip.Builder()
                            .withText("Designation-" + model.empDesignation + "\n" + "Reporting Manager-" + model.empReportingManager)
                            .withTextSize(40.0f)
                            .withPadding(30, 30, 10, 10)
                            .build()
                    val toolTipView = ToolTipView.Builder(mContext)
                            .withAnchor(view)
                            .withToolTip(toolTip)
                            .withGravity(Gravity.TOP)

                            .build()
                    toolTipView.show()
                }
            }


        }


    }
}