package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.interfaces.iActivity
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import kotlinx.android.synthetic.main.row_task_images_adapter.view.*
import java.util.*

class TaskImagesAdapter(private val mContext: Context?, val taskAL: ArrayList<TaskListModel>, val intrfc: iActivity) : RecyclerView.Adapter<TaskImagesAdapter.MyHolderBottomSheet>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolderBottomSheet {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_task_images_adapter, viewGroup, false)
        return TaskImagesAdapter.MyHolderBottomSheet(view)
    }

    override fun getItemCount(): Int {
        return taskAL.size
    }

    override fun onBindViewHolder(holder: MyHolderBottomSheet, position: Int) {
        holder.bind(mContext!!, taskAL.get(position),intrfc,position)
    }

    class MyHolderBottomSheet(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(mContext: Context, model: TaskListModel, intrfc: iActivity, position: Int) {
            itemView.tvSubActivityTaskImagesAdapter.text = model.subActivityTitle
            if(!model.activityImagePath.equals("") && model.activityImage.equals(""))
            {
                Glide.with(mContext)
                        .load(R.drawable.icon_camera_image)
                        .into(itemView.ivSubActivityTaskImagesAdapter);
            }
            else if(model.activityImagePath.equals("") && !model.activityImage.equals(""))
            {
                Glide.with(mContext)
                        .load(model.activityImage)
                        .into(itemView.ivSubActivityTaskImagesAdapter);
            }
            else {
                Glide.with(mContext)
                        .load(model.activityImagePath+model.activityImage)
                        .into(itemView.ivSubActivityTaskImagesAdapter);
            }



            itemView.ivSubActivityTaskImagesAdapter.setOnClickListener(View.OnClickListener {
                v: View? -> intrfc.callPosition(position)
            }
            )





        }


    }
}