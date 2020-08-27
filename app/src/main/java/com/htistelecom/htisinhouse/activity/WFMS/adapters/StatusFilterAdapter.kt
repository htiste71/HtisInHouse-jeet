package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.FilterActivity
import com.htistelecom.htisinhouse.activity.WFMS.models.ThreeParameterModel


class StatusFilterAdapter(val mContext: FilterActivity, val statusList: ArrayList<ThreeParameterModel>) : RecyclerView.Adapter<StatusFilterAdapter.MyHolder>() {

    private var mSelectedItem=-1

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_status_filter_adapter, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return FilterActivity.filterStatusList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.rbStatusRowStatusFilterAdapter.setChecked(position == mSelectedItem);
        var clickListener = View.OnClickListener {
           // mSelectedItem =
           // notifyDataSetChanged()
        }
        holder.rbStatusRowStatusFilterAdapter.setOnClickListener(clickListener);
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        
        var rbStatusRowStatusFilterAdapter=itemView.findViewById<RadioButton>(R.id.rbStatusRowStatusFilterAdapter)


//        fun bind(mContext: Context, model: ThreeParameterModel, adapter: StatusFilterAdapter, position: Int) {
//
//            itemView.rbStatusRowStatusFilterAdapter.text = model.name;
//
//            itemView.rbStatusRowStatusFilterAdapter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
//
//
//                if (b) {
//                    if(FilterActivity.lastPosition!=-1)
//                        FilterActivity.filterStatusList.get(FilterActivity.lastPosition).isChecked = false
//                    FilterActivity.lastPosition=position
//
//                    FilterActivity.filterStatusList.get(position).isChecked = true
//
//                    adapter.notifyDataSetChanged()
//                }
//
//            })
//            if (model.isChecked) {
//                itemView.rbStatusRowStatusFilterAdapter.isChecked = true
//            } else {
//                itemView.rbStatusRowStatusFilterAdapter.isChecked = false
//
//            }
//
//        }
    }
}