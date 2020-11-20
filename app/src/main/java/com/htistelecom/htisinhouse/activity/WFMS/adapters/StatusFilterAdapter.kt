package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.FilterActivity
import com.htistelecom.htisinhouse.activity.WFMS.models.ThreeParameterModel
import kotlinx.android.synthetic.main.row_status_filter_adapter.view.*


class StatusFilterAdapter(val mContext: FilterActivity) : RecyclerView.Adapter<StatusFilterAdapter.MyHolder>() {

    private var mSelectedItem = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_status_filter_adapter, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return FilterActivity.filterStatusList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.bind(mContext, FilterActivity.filterStatusList.get(position), FilterActivity.filterStatusList)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(mContext: FilterActivity, model: ThreeParameterModel, statusList: ArrayList<ThreeParameterModel>) {

            itemView.rbStatusRowStatusFilterAdapter.text = model.name;
            if (model.isChecked) {
                itemView.rbStatusRowStatusFilterAdapter.isChecked = true
            } else {
                itemView.rbStatusRowStatusFilterAdapter.isChecked = false

            }
            itemView.rbStatusRowStatusFilterAdapter.setOnClickListener { view ->


                for (i in 0 until FilterActivity.filterStatusList.size) {
                    FilterActivity.filterStatusList.get(i).isChecked = false
                }
                FilterActivity.filterStatusList.get(position).isChecked = true
                mContext.notifyMethod()

            }


        }



    }

    fun getStatusId(): String
    {
        var mId=""
        for(i in 0 until FilterActivity.filterStatusList.size)
        {
            if(FilterActivity.filterStatusList.get(i).isChecked)
            {
                mId=FilterActivity.filterStatusList.get(i).id
                break
            }

        }
        return mId
    }
}
